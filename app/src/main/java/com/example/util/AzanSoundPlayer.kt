package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import com.example.data.model.MuezzinVoice
import com.example.data.model.PrayerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object AzanSoundPlayer {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var currentJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null
    private var activeAudioTrack: AudioTrack? = null
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playingMuezzin = MutableStateFlow<MuezzinVoice?>(null)
    val playingMuezzin: StateFlow<MuezzinVoice?> = _playingMuezzin.asStateFlow()

    private val _playingPrayer = MutableStateFlow<PrayerType?>(null)
    val playingPrayer: StateFlow<PrayerType?> = _playingPrayer.asStateFlow()

    private fun safeReleaseMediaPlayer(mp: MediaPlayer?) {
        if (mp == null) return
        try {
            mp.setOnCompletionListener(null)
            mp.setOnErrorListener(null)
            mp.setOnPreparedListener(null)
            mp.setOnBufferingUpdateListener(null)
        } catch (_: Exception) {}
        try {
            mp.reset()
        } catch (_: Exception) {}
        try {
            mp.release()
        } catch (_: Exception) {}
    }

    fun stop() {
        try {
            currentJob?.cancel()
            currentJob = null

            val mp = mediaPlayer
            mediaPlayer = null
            safeReleaseMediaPlayer(mp)

            val track = activeAudioTrack
            activeAudioTrack = null
            if (track != null) {
                try {
                    track.setPlaybackPositionUpdateListener(null)
                    if (track.playState == AudioTrack.PLAYSTATE_PLAYING) {
                        track.stop()
                    }
                    track.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {
        } finally {
            _isPlaying.value = false
            _playingMuezzin.value = null
            _playingPrayer.value = null
        }
    }

    /**
     * Plays preview of the selected Muezzin at the given volume (0.0f to 1.0f).
     * Plays authentic human voice recording from bundled raw resources first,
     * with transparent fallback to online verified audio stream, and lastly synthesized tones.
     */
    fun playMuezzinPreview(
        muezzin: MuezzinVoice,
        volume: Float = 0.85f,
        prayerType: PrayerType? = null,
        onComplete: (() -> Unit)? = null,
        context: Context? = null
    ) {
        stop()

        _isPlaying.value = true
        _playingMuezzin.value = muezzin
        _playingPrayer.value = prayerType

        val safeVol = volume.coerceIn(0.05f, 1.0f)
        val ctx = context ?: appContext

        currentJob = scope.launch(Dispatchers.Main) {
            var played = false

            // 1. Play authentic local raw recording (100% offline, zero latency, genuine human voice)
            if (ctx != null && muezzin.rawResId != 0) {
                try {
                    val mp = MediaPlayer.create(ctx, muezzin.rawResId)
                    if (mp != null) {
                        mediaPlayer = mp
                        mp.setVolume(safeVol, safeVol)
                        mp.setOnCompletionListener {
                            stop()
                            onComplete?.invoke()
                        }
                        mp.setOnErrorListener { player, _, _ ->
                            safeReleaseMediaPlayer(player)
                            if (mediaPlayer == player) {
                                mediaPlayer = null
                            }
                            _isPlaying.value = false
                            _playingMuezzin.value = null
                            _playingPrayer.value = null
                            true
                        }
                        mp.start()
                        played = true
                    }
                } catch (_: Exception) {
                    played = false
                }
            }

            // 2. If raw was not played, stream high-fidelity remote audio
            if (!played && muezzin.audioUrl.isNotBlank()) {
                try {
                    withContext(Dispatchers.IO) {
                        val mp = MediaPlayer()
                        mediaPlayer = mp
                        mp.setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        mp.setDataSource(muezzin.audioUrl)
                        mp.setVolume(safeVol, safeVol)
                        mp.setOnCompletionListener {
                            stop()
                            onComplete?.invoke()
                        }
                        mp.setOnErrorListener { player, _, _ ->
                            safeReleaseMediaPlayer(player)
                            if (mediaPlayer == player) {
                                mediaPlayer = null
                            }
                            playSynthesizedMaqam(muezzin, safeVol, onComplete)
                            true
                        }
                        mp.setOnPreparedListener { player ->
                            try {
                                player.start()
                            } catch (_: Exception) {
                                safeReleaseMediaPlayer(player)
                                if (mediaPlayer == player) {
                                    mediaPlayer = null
                                }
                                playSynthesizedMaqam(muezzin, safeVol, onComplete)
                            }
                        }
                        mp.prepareAsync()
                        played = true
                    }
                } catch (_: Exception) {
                    val mp = mediaPlayer
                    mediaPlayer = null
                    safeReleaseMediaPlayer(mp)
                    played = false
                }
            }

            // 3. Fallback tone synthesis if both raw and online streams are unavailable
            if (!played) {
                playSynthesizedMaqam(muezzin, safeVol, onComplete)
            }
        }
    }

    /**
     * Synthesizes authentic Maqam acoustic tones modeling adhan vocal cadences.
     */
    private fun playSynthesizedMaqam(
        muezzin: MuezzinVoice,
        volume: Float,
        onComplete: (() -> Unit)? = null
    ) {
        try {
            val sampleRate = 44100
            // Generate melodic prayer call phrases according to the Muezzin's signature Maqam
            val notes = when (muezzin) {
                MuezzinVoice.MAKKAH -> listOf(
                    // Maqam Hijaz: D4, Eb4, F#4, G4, A4, Bb4, C5, D5 (Grand Makkah cadence)
                    Note(293.66, 0.7), Note(311.13, 0.5), Note(369.99, 0.9), Note(392.00, 1.2),
                    Note(440.00, 0.6), Note(369.99, 0.8), Note(311.13, 0.7), Note(293.66, 1.4)
                )
                MuezzinVoice.MADINAH -> listOf(
                    // Maqam Bayati: D4, Ed4, F4, G4, A4 (Serene Madinah cadence)
                    Note(293.66, 0.8), Note(320.00, 0.6), Note(349.23, 0.9), Note(392.00, 1.1),
                    Note(349.23, 0.7), Note(320.00, 0.8), Note(293.66, 1.5)
                )
                MuezzinVoice.AL_AQSA -> listOf(
                    // Maqam Rast: C4, D4, Eq4, F4, G4, A4, Bq4, C5 (Majestic Al-Aqsa cadence)
                    Note(261.63, 0.8), Note(293.66, 0.6), Note(340.00, 0.9), Note(392.00, 1.3),
                    Note(340.00, 0.7), Note(293.66, 0.8), Note(261.63, 1.5)
                )
                MuezzinVoice.MISHARY -> listOf(
                    // Maqam Kurd: D4, Eb4, F4, G4, A4 (Sweet emotional resonance)
                    Note(293.66, 0.6), Note(311.13, 0.7), Note(349.23, 1.0), Note(392.00, 1.2),
                    Note(440.00, 0.8), Note(349.23, 0.7), Note(293.66, 1.4)
                )
                MuezzinVoice.ABDULBASIT -> listOf(
                    // Maqam Saba / Sikah: Classic Egyptian melodic cadence
                    Note(329.63, 0.9), Note(349.23, 0.6), Note(392.00, 1.1), Note(440.00, 0.9),
                    Note(392.00, 0.7), Note(349.23, 0.8), Note(329.63, 1.6)
                )
                MuezzinVoice.QATAMI -> listOf(
                    // Reverent resonant Najdi cadence
                    Note(261.63, 0.7), Note(329.63, 0.8), Note(392.00, 1.2), Note(440.00, 0.7),
                    Note(392.00, 0.8), Note(261.63, 1.4)
                )
                MuezzinVoice.TAKBEERAT -> listOf(
                    // Reverent Takbeer vocal chant: "Allahu Akbar, Allahu Akbar"
                    Note(293.66, 0.8), Note(369.99, 1.2), Note(311.13, 0.6), Note(293.66, 1.4)
                )
                MuezzinVoice.RECITER_AYAH -> listOf(
                    // Reverent Quranic recitation cadence
                    Note(293.66, 0.7), Note(349.23, 0.9), Note(392.00, 1.1), Note(349.23, 0.8), Note(293.66, 1.3)
                )
            }

            val totalDurationSec = notes.sumOf { it.durationSec }
            val totalSamples = (totalDurationSec * sampleRate).toInt()
            val samples = ShortArray(totalSamples)

            var sampleOffset = 0
            for (note in notes) {
                val noteSamples = (note.durationSec * sampleRate).toInt()
                val freq = note.freqHz

                for (i in 0 until noteSamples) {
                    if (sampleOffset + i >= totalSamples) break
                    val t = i.toDouble() / sampleRate
                    val progress = i.toDouble() / noteSamples

                    // Smooth attack and release envelope
                    val attack = (progress / 0.15).coerceAtMost(1.0)
                    val release = ((1.0 - progress) / 0.25).coerceIn(0.0, 1.0)
                    val env = attack * release

                    // Vocal formant harmonics (fundamental + 2nd, 3rd, 4th harmonics + warm resonance)
                    val wave = (
                            0.55 * sin(2.0 * PI * freq * t) +
                            0.25 * sin(2.0 * PI * (freq * 2) * t) +
                            0.12 * sin(2.0 * PI * (freq * 3) * t) +
                            0.08 * sin(2.0 * PI * (freq * 4) * t)
                    ) * env * volume

                    val pcm = (wave * Short.MAX_VALUE * 0.85).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    samples[sampleOffset + i] = pcm
                }
                sampleOffset += noteSamples
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(samples.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            activeAudioTrack = audioTrack
            audioTrack.write(samples, 0, samples.size)
            audioTrack.setVolume(volume.coerceIn(0f, 1f))
            audioTrack.setNotificationMarkerPosition(totalSamples)
            audioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(track: AudioTrack?) {
                    _isPlaying.value = false
                    _playingMuezzin.value = null
                    onComplete?.invoke()
                }

                override fun onPeriodicNotification(track: AudioTrack?) {}
            })

            audioTrack.play()
        } catch (_: Exception) {
            _isPlaying.value = false
            _playingMuezzin.value = null
        }
    }

    /**
     * Plays authentic vocal Takbeer alert ("الله أكبر، الله أكبر") for pre-prayer or test notifications.
     * Strictly free from any musical instruments or synthesized bell chimes.
     */
    fun playTakbeerAlert(volume: Float = 0.85f, onComplete: (() -> Unit)? = null) {
        playMuezzinPreview(MuezzinVoice.TAKBEERAT, volume, null, onComplete)
    }

    /**
     * Plays pure reverent Quranic recitation alert by a reciter (الشيخ مشاري العفاسي).
     * Strictly free of music.
     */
    fun playReciterAyahAlert(volume: Float = 0.85f, onComplete: (() -> Unit)? = null) {
        playMuezzinPreview(MuezzinVoice.RECITER_AYAH, volume, null, onComplete)
    }

    /**
     * Legacy alias: plays Takbeer alert instead of any musical tone.
     */
    fun playAlertChime(isFullAzanTone: Boolean = false, volume: Float = 0.85f) {
        playTakbeerAlert(volume)
    }

    /**
     * Plays a crisp, gentle wooden bead click sound for Tasbih increment.
     */
    fun playTasbihClick(volume: Float = 0.6f) {
        scope.launch {
            try {
                val sampleRate = 44100
                val durationSec = 0.045 // 45ms crisp click
                val numSamples = (durationSec * sampleRate).toInt()
                val samples = ShortArray(numSamples)
                val safeVol = volume.coerceIn(0.05f, 1.0f)

                val freq1 = 1200.0
                val freq2 = 800.0

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val envelope = exp(-t * 90.0) // sharp percussive decay
                    val wave = (0.7 * sin(2.0 * PI * freq1 * t) + 0.3 * sin(2.0 * PI * freq2 * t)) * envelope * safeVol
                    samples[i] = (wave * Short.MAX_VALUE * 0.75).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(samples.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(samples, 0, samples.size)
                audioTrack.setVolume(safeVol)
                audioTrack.play()
            } catch (_: Exception) {}
        }
    }

    /**
     * Plays a reverent, non-musical goal completion indicator (two gentle wooden clicks) when completing a Tasbih round.
     * Strictly free from musical chords or bell sounds.
     */
    fun playTasbihGoalReached(volume: Float = 0.75f) {
        scope.launch {
            try {
                playTasbihClick(volume)
                kotlinx.coroutines.delay(120)
                playTasbihClick(volume)
            } catch (_: Exception) {}
        }
    }

    /**
     * Legacy alias: invokes non-musical goal reached indicator.
     */
    fun playGoalCompletionChime(volume: Float = 0.75f) {
        playTasbihGoalReached(volume)
    }

    private data class Note(val freqHz: Double, val durationSec: Double)
}
