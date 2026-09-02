package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.os.Build
import com.example.data.model.QuranReciter
import com.example.data.model.QuranRepeatMode
import com.example.data.model.Surah
import com.example.data.repository.QuranDataRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.PI
import kotlin.math.sin

object QuranAudioPlayer {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null
    private var sleepTimerJob: Job? = null
    private var fallbackAudioTrack: AudioTrack? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _currentSurah = MutableStateFlow<Surah?>(null)
    val currentSurah: StateFlow<Surah?> = _currentSurah.asStateFlow()

    private val _currentReciter = MutableStateFlow<QuranReciter>(QuranDataRepository.recitersList[0])
    val currentReciter: StateFlow<QuranReciter> = _currentReciter.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0)
    val currentPositionMs: StateFlow<Int> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0)
    val durationMs: StateFlow<Int> = _durationMs.asStateFlow()

    private val _repeatMode = MutableStateFlow(QuranRepeatMode.ALL)
    val repeatMode: StateFlow<QuranRepeatMode> = _repeatMode.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _sleepTimerMinutes = MutableStateFlow(0)
    val sleepTimerMinutes: StateFlow<Int> = _sleepTimerMinutes.asStateFlow()

    private val _sleepTimerRemainingSeconds = MutableStateFlow(0)
    val sleepTimerRemainingSeconds: StateFlow<Int> = _sleepTimerRemainingSeconds.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun setReciter(reciter: QuranReciter) {
        val wasPlaying = _isPlaying.value
        val surah = _currentSurah.value
        _currentReciter.value = reciter
        if (wasPlaying && surah != null) {
            playSurah(surah, reciter)
        }
    }

    fun playSurah(surah: Surah, reciter: QuranReciter = _currentReciter.value) {
        _currentSurah.value = surah
        _currentReciter.value = reciter
        _isBuffering.value = true
        _errorMessage.value = null

        scope.launch {
            stopCurrentInternal()

            val url = QuranDataRepository.getAudioUrl(reciter, surah.number)
            try {
                withContext(Dispatchers.IO) {
                    val mp = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                        )
                        setDataSource(url)
                        setOnPreparedListener { player ->
                            _isBuffering.value = false
                            _durationMs.value = player.duration
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                try {
                                    player.playbackParams = player.playbackParams.setSpeed(_playbackSpeed.value)
                                } catch (_: Exception) {}
                            }
                            player.start()
                            _isPlaying.value = true
                            startProgressTracking()
                        }
                        setOnCompletionListener {
                            onPlaybackCompleted()
                        }
                        setOnErrorListener { _, _, _ ->
                            _isBuffering.value = false
                            _isPlaying.value = false
                            _errorMessage.value = "تعذر الاتصال بالخادم الصوتي، جاري تشغيل نغمة بديلة."
                            playOfflineFallbackTone()
                            true
                        }
                        prepareAsync()
                    }
                    mediaPlayer = mp
                }
            } catch (e: Exception) {
                _isBuffering.value = false
                _isPlaying.value = false
                _errorMessage.value = "تعذر تشغيل التلاوة: ${e.localizedMessage}"
                playOfflineFallbackTone()
            }
        }
    }

    fun togglePlayPause() {
        val mp = mediaPlayer
        if (mp != null) {
            if (mp.isPlaying) {
                mp.pause()
                _isPlaying.value = false
            } else {
                mp.start()
                _isPlaying.value = true
                startProgressTracking()
            }
        } else {
            val surah = _currentSurah.value ?: QuranDataRepository.surahsList.first()
            playSurah(surah, _currentReciter.value)
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
            }
        }
    }

    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                _isPlaying.value = true
                startProgressTracking()
            }
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.let {
            try {
                it.seekTo(positionMs)
                _currentPositionMs.value = positionMs
            } catch (_: Exception) {}
        }
    }

    fun playNextSurah() {
        val current = _currentSurah.value ?: QuranDataRepository.surahsList.first()
        val nextNumber = if (current.number < 114) current.number + 1 else 1
        val nextSurah = QuranDataRepository.getSurahByNumber(nextNumber) ?: QuranDataRepository.surahsList.first()
        playSurah(nextSurah, _currentReciter.value)
    }

    fun playPreviousSurah() {
        val current = _currentSurah.value ?: QuranDataRepository.surahsList.first()
        val prevNumber = if (current.number > 1) current.number - 1 else 114
        val prevSurah = QuranDataRepository.getSurahByNumber(prevNumber) ?: QuranDataRepository.surahsList.first()
        playSurah(prevSurah, _currentReciter.value)
    }

    fun toggleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            QuranRepeatMode.OFF -> QuranRepeatMode.ONE
            QuranRepeatMode.ONE -> QuranRepeatMode.ALL
            QuranRepeatMode.ALL -> QuranRepeatMode.OFF
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
        mediaPlayer?.let { mp ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mp.isPlaying) {
                try {
                    mp.playbackParams = mp.playbackParams.setSpeed(speed)
                } catch (_: Exception) {}
            }
        }
    }

    fun setSleepTimer(minutes: Int) {
        _sleepTimerMinutes.value = minutes
        sleepTimerJob?.cancel()

        if (minutes <= 0) {
            _sleepTimerRemainingSeconds.value = 0
            return
        }

        _sleepTimerRemainingSeconds.value = minutes * 60
        sleepTimerJob = scope.launch {
            while (_sleepTimerRemainingSeconds.value > 0) {
                delay(1000)
                _sleepTimerRemainingSeconds.value -= 1
            }
            // Timer expired, pause audio
            stop()
            _sleepTimerMinutes.value = 0
        }
    }

    fun stop() {
        stopCurrentInternal()
        _isPlaying.value = false
        _isBuffering.value = false
        _currentPositionMs.value = 0
    }

    private fun stopCurrentInternal() {
        progressJob?.cancel()
        progressJob = null

        fallbackAudioTrack?.let {
            try {
                if (it.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    it.stop()
                }
                it.release()
            } catch (_: Exception) {}
        }
        fallbackAudioTrack = null

        mediaPlayer?.let { mp ->
            try {
                if (mp.isPlaying) {
                    mp.stop()
                }
                mp.release()
            } catch (_: Exception) {}
        }
        mediaPlayer = null
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (_isPlaying.value) {
                mediaPlayer?.let { mp ->
                    try {
                        if (mp.isPlaying) {
                            _currentPositionMs.value = mp.currentPosition
                            _durationMs.value = mp.duration
                        }
                    } catch (_: Exception) {}
                }
                delay(500)
            }
        }
    }

    private fun onPlaybackCompleted() {
        when (_repeatMode.value) {
            QuranRepeatMode.ONE -> {
                _currentSurah.value?.let { playSurah(it, _currentReciter.value) }
            }
            QuranRepeatMode.ALL -> {
                playNextSurah()
            }
            QuranRepeatMode.OFF -> {
                _isPlaying.value = false
                _currentPositionMs.value = 0
            }
        }
    }

    private fun playOfflineFallbackTone() {
        scope.launch(Dispatchers.Default) {
            try {
                val sampleRate = 44100
                val durationSec = 3.0
                val totalSamples = (sampleRate * durationSec).toInt()
                val buffer = ShortArray(totalSamples)

                val freqs = doubleArrayOf(432.0, 528.0, 648.0)
                for (i in 0 until totalSamples) {
                    val t = i.toDouble() / sampleRate
                    var s = 0.0
                    for (f in freqs) {
                        s += sin(2.0 * PI * f * t)
                    }
                    s /= freqs.size
                    val envelope = sin(PI * (i.toDouble() / totalSamples))
                    buffer[i] = (s * envelope * 12000).toInt().toShort()
                }

                val minBuf = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val track = AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    maxOf(minBuf, buffer.size * 2),
                    AudioTrack.MODE_STREAM
                )
                fallbackAudioTrack = track
                track.play()
                track.write(buffer, 0, buffer.size)
            } catch (_: Exception) {}
        }
    }

    fun formatTime(millis: Int): String {
        val totalSec = (millis / 1000).coerceAtLeast(0)
        val minutes = totalSec / 60
        val seconds = totalSec % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
