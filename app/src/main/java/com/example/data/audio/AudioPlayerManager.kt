package com.example.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.example.data.repository.AceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

/**
 * Real Android hardware MediaPlayer engine for ACE Publishing.
 * Supports playing actual on-device master MP3 files from ZipAudioPackager,
 * streaming preview clips, and fallback progression for seed catalog items.
 */
class AudioPlayerManager(private val context: Context) {

  private var mediaPlayer: MediaPlayer? = null
  private val playerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
  private var progressJob: Job? = null

  private val _playingSample = MutableStateFlow<AceRepository.PlayingAudioSample?>(null)
  val playingSample: StateFlow<AceRepository.PlayingAudioSample?> = _playingSample.asStateFlow()

  fun play(
    title: String,
    authorName: String,
    coverRes: Int,
    durationSeconds: Int = 0,
    audioFilePath: String? = null,
    localCoverUri: String? = null
  ) {
    // Release any previous playback session
    releasePlayer()

    var isRealFile = false
    var realDurationSec = durationSeconds
    var fileSizeBytes = 0L

    if (!audioFilePath.isNullOrBlank()) {
      val file = File(audioFilePath)
      if (file.exists() && file.length() > 0) {
        try {
          val player = MediaPlayer().apply {
            setAudioAttributes(
              AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            )
            setDataSource(file.absolutePath)
            prepare()
            start()
          }
          mediaPlayer = player
          isRealFile = true
          fileSizeBytes = file.length()
          val measuredDuration = player.duration
          if (measuredDuration > 0) {
            realDurationSec = measuredDuration / 1000
          }
          Log.i("AudioPlayerManager", "Playing real audio file: ${file.name} (${file.length()} bytes, duration: ${realDurationSec}s)")

          player.setOnCompletionListener {
            _playingSample.value = _playingSample.value?.copy(
              isPlaying = false,
              currentPositionSeconds = realDurationSec
            )
          }

          player.setOnErrorListener { _, what, extra ->
            Log.e("AudioPlayerManager", "MediaPlayer error: what=$what, extra=$extra")
            releasePlayer()
            false
          }
        } catch (e: Exception) {
          Log.e("AudioPlayerManager", "Failed to start MediaPlayer for path: $audioFilePath", e)
          releasePlayer()
        }
      }
    }

    val initialDuration = if (realDurationSec > 0) realDurationSec else 180
    _playingSample.value = AceRepository.PlayingAudioSample(
      title = title,
      authorName = authorName,
      coverRes = coverRes,
      localCoverUri = localCoverUri,
      audioFilePath = audioFilePath,
      isRealAudioFile = isRealFile,
      durationSeconds = initialDuration,
      currentPositionSeconds = 0,
      isPlaying = true,
      fileSizeBytes = fileSizeBytes
    )

    startProgressTracker()
  }

  fun togglePlayPause() {
    val current = _playingSample.value ?: return
    val player = mediaPlayer

    if (player != null && current.isRealAudioFile) {
      if (player.isPlaying) {
        player.pause()
        _playingSample.value = current.copy(isPlaying = false)
      } else {
        player.start()
        _playingSample.value = current.copy(isPlaying = true)
      }
    } else {
      // Virtual/simulated track toggle
      _playingSample.value = current.copy(isPlaying = !current.isPlaying)
    }
  }

  fun seekTo(seconds: Int) {
    val current = _playingSample.value ?: return
    val targetSec = seconds.coerceIn(0, current.durationSeconds)

    val player = mediaPlayer
    if (player != null && current.isRealAudioFile) {
      try {
        player.seekTo(targetSec * 1000)
      } catch (e: Exception) {
        Log.w("AudioPlayerManager", "Seek failed on MediaPlayer", e)
      }
    }

    _playingSample.value = current.copy(currentPositionSeconds = targetSec)
  }

  fun stop() {
    releasePlayer()
    _playingSample.value = null
  }

  private fun startProgressTracker() {
    progressJob?.cancel()
    progressJob = playerScope.launch {
      while (isActive) {
        delay(500)
        val current = _playingSample.value ?: break
        if (!current.isPlaying) continue

        val player = mediaPlayer
        if (player != null && current.isRealAudioFile) {
          try {
            if (player.isPlaying) {
              val currentPosSec = player.currentPosition / 1000
              val totalDurSec = if (player.duration > 0) player.duration / 1000 else current.durationSeconds
              _playingSample.value = current.copy(
                currentPositionSeconds = currentPosSec,
                durationSeconds = totalDurSec
              )
            }
          } catch (e: Exception) {
            // Player might be releasing
          }
        } else {
          // Virtual progression for catalog seed books without bundled audio files
          if (current.currentPositionSeconds < current.durationSeconds) {
            _playingSample.value = current.copy(currentPositionSeconds = current.currentPositionSeconds + 1)
          } else {
            _playingSample.value = current.copy(currentPositionSeconds = 0, isPlaying = false)
          }
        }
      }
    }
  }

  private fun releasePlayer() {
    progressJob?.cancel()
    progressJob = null
    mediaPlayer?.let { player ->
      try {
        if (player.isPlaying) {
          player.stop()
        }
        player.release()
      } catch (e: Exception) {
        Log.w("AudioPlayerManager", "Error releasing MediaPlayer", e)
      }
    }
    mediaPlayer = null
  }
}
