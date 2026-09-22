package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Procedural ancient Sumerian Lyre & Flute synthesizer using native AudioTrack.
 * Generates an authentic, soothing ancient Mesopotamian pentatonic melody
 * inspired by the Hurrian Hymn to Nikkal and the Royal Lyre of Ur.
 */
class MesopotamianLyrePlayer {

    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var isMuted: Boolean = false

    private val sampleRate = 22050

    // Ancient Mesopotamian pentatonic / diatonic scale (in Hz)
    // Based on the Silver Lyre of Ur (D minor pentatonic / Dorian tone mode)
    private val scale = doubleArrayOf(
        146.83, // D3 (Bass drone)
        220.00, // A3
        261.63, // C4
        293.66, // D4
        329.63, // E4
        349.23, // F4
        392.00, // G4
        440.00, // A4
        523.25, // C5
        587.33  // D5
    )

    // A meditative ancient melodic motif pattern (indices in the scale array)
    private val melodyPattern = intArrayOf(
        0, 3, 5, 6, 7, 6, 5, 3,
        1, 4, 6, 7, 8, 7, 6, 4,
        0, 5, 7, 8, 9, 8, 7, 5,
        3, 6, 7, 6, 5, 4, 3, 1,
        0, 3, 7, 9, 7, 5, 3, 0,
        1, 4, 6, 7, 5, 4, 3, 1
    )

    fun start() {
        if (playbackJob?.isActive == true) return

        try {
            val minBufSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
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
                .setBufferSizeInBytes(minBufSize * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.play()

            playbackJob = scope.launch {
                var step = 0
                while (isActive) {
                    if (isMuted) {
                        delay(200)
                        continue
                    }

                    val noteIdx = melodyPattern[step % melodyPattern.size]
                    val freq = scale[noteIdx]
                    val isDrone = (step % 8 == 0)
                    val durationMs = if (isDrone) 1200 else 600

                    val audioData = generatePluckedNote(freq, durationMs, isDrone)
                    audioTrack?.write(audioData, 0, audioData.size)

                    step++
                    // Gentle pause between phrases for contemplative mood
                    val pause = if (step % 8 == 0) 350L else 120L
                    delay(pause)
                }
            }
        } catch (_: Exception) {
            // Graceful fallback if audio device is occupied or unavailable
        }
    }

    private fun generatePluckedNote(freq: Double, durationMs: Int, addDrone: Boolean): ShortArray {
        val totalSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(totalSamples)

        val decayRate = 3.2
        val droneFreq = 146.83 // Low D drone

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            // Plucked string envelope: fast rise, gentle exponential decay
            val envelope = exp(-decayRate * t)

            // Primary harmonic + warm subtle 2nd and 3rd harmonics of lyre
            val wave1 = sin(2.0 * PI * freq * t)
            val wave2 = 0.4 * sin(4.0 * PI * freq * t)
            val wave3 = 0.2 * sin(6.0 * PI * freq * t)

            var sample = (wave1 + wave2 + wave3) * envelope * 0.28

            if (addDrone) {
                val droneEnv = exp(-1.8 * t)
                val droneWave = sin(2.0 * PI * droneFreq * t) * droneEnv * 0.18
                sample += droneWave
            }

            // Convert to 16-bit PCM
            val pcm = (sample * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
            buffer[i] = pcm
        }

        return buffer
    }

    fun stop() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {
        }
        audioTrack = null
    }
}
