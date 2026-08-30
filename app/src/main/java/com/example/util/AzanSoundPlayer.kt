package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import com.example.data.model.MuezzinVoice
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

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playingMuezzin = MutableStateFlow<MuezzinVoice?>(null)
    val playingMuezzin: StateFlow<MuezzinVoice?> = _playingMuezzin.asStateFlow()

    fun stop() {
        try {
            currentJob?.cancel()
            currentJob = null

            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.stop()
                }
                mp.release()
            }
            mediaPlayer = null

            activeAudioTrack?.let { track ->
                if (track.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    track.stop()
                }
                track.release()
            }
            activeAudioTrack = null
        } catch (_: Exception) {
        } finally {
            _isPlaying.value = false
            _playingMuezzin.value = null
        }
    }

    /**
     * Plays preview of the selected Muezzin at the given volume (0.0f to 1.0f).
     */
    fun playMuezzinPreview(
        muezzin: MuezzinVoice,
        volume: Float = 0.8f,
        onComplete: (() -> Unit)? = null
    ) {
        stop()

        _isPlaying.value = true
        _playingMuezzin.value = muezzin

        val safeVol = volume.coerceIn(0.05f, 1.0f)

        currentJob = scope.launch {
            // If the muezzin is online and has an audio URL, try streaming; fallback gracefully to rich synthesis
            var playedSuccessfully = false

            if (muezzin.audioUrl.isNotEmpty()) {
                try {
                    withContext(Dispatchers.IO) {
                        val mp = MediaPlayer().apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build()
                            )
                            setDataSource(muezzin.audioUrl)
                            setVolume(safeVol, safeVol)
                            setOnCompletionListener {
                                stop()
                                onComplete?.invoke()
                            }
                            setOnErrorListener { _, _, _ ->
                                stop()
                                // Fallback to synthesis
                                playSynthesizedMaqam(muezzin, safeVol, onComplete)
                                true
                            }
                            prepareAsync()
                            setOnPreparedListener {
                                it.start()
                            }
                        }
                        mediaPlayer = mp
                        playedSuccessfully = true
                    }
                } catch (_: Exception) {
                    playedSuccessfully = false
                }
            }

            if (!playedSuccessfully) {
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
                MuezzinVoice.TAKBEER_CHIME -> listOf(
                    // Peaceful harmonic bell chime
                    Note(329.63, 0.5), Note(415.30, 0.5), Note(493.88, 0.6), Note(659.25, 1.2)
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
     * Plays a pleasant harmonic alert chime for pre-prayer alert (3 min before)
     */
    fun playAlertChime(isFullAzanTone: Boolean = false, volume: Float = 0.8f) {
        scope.launch {
            try {
                val sampleRate = 44100
                val durationSec = if (isFullAzanTone) 2.5 else 1.2
                val numSamples = (durationSec * sampleRate).toInt()
                val samples = ShortArray(numSamples)

                val baseFreq1 = 329.63 // E4
                val baseFreq2 = 415.30 // G#4
                val baseFreq3 = 493.88 // B4
                val baseFreq4 = 659.25 // E5

                val safeVol = volume.coerceIn(0.05f, 1.0f)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val envelope = exp(-t * (if (isFullAzanTone) 1.5 else 3.0))

                    val wave = (
                            0.4 * sin(2.0 * PI * baseFreq1 * t) +
                            0.3 * sin(2.0 * PI * baseFreq2 * t) +
                            0.2 * sin(2.0 * PI * baseFreq3 * t) +
                            0.1 * sin(2.0 * PI * baseFreq4 * t)
                    ) * envelope * safeVol

                    samples[i] = (wave * Short.MAX_VALUE * 0.7).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
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
            } catch (_: Exception) {
            }
        }
    }

    private data class Note(val freqHz: Double, val durationSec: Double)
}
