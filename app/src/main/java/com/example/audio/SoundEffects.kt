package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Synthesizes instantaneous ancient action sound effects (SFX)
 * without requiring external sound asset files:
 * - Bronze sword clash in battle
 * - Silver shekel coin jingle
 * - Clay tablet stylus stamp
 * - War horn blast
 * - Water splash / irrigation
 */
object SoundEffects {

    private val scope = CoroutineScope(Dispatchers.Default)
    private const val SAMPLE_RATE = 22050

    var isMuted: Boolean = false

    fun playSwordClash() {
        if (isMuted) return
        scope.launch {
            try {
                // High metallic clang + white noise scrape + deep ring
                val durationMs = 380
                val totalSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(totalSamples)

                for (i in 0 until totalSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val env = exp(-9.0 * t)

                    // Inharmonic metallic partials for bronze clashing
                    val tone1 = sin(2.0 * PI * 1840.0 * t)
                    val tone2 = 0.7 * sin(2.0 * PI * 2920.0 * t)
                    val tone3 = 0.5 * sin(2.0 * PI * 4210.0 * t)
                    val noise = (Math.random() * 2.0 - 1.0) * exp(-28.0 * t) * 0.4

                    val sample = (tone1 + tone2 + tone3 + noise) * env * 0.35
                    buffer[i] = (sample * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
                }
                playPcm(buffer)
            } catch (_: Exception) {}
        }
    }

    fun playCoinClink() {
        if (isMuted) return
        scope.launch {
            try {
                // Bright high metallic bell chime of silver coins
                val durationMs = 350
                val totalSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(totalSamples)

                for (i in 0 until totalSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val env = exp(-7.5 * t)

                    val bell1 = sin(2.0 * PI * 2637.0 * t) // E7
                    val bell2 = 0.6 * sin(2.0 * PI * 3951.0 * t) // B7

                    val sample = (bell1 + bell2) * env * 0.22
                    buffer[i] = (sample * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
                }
                playPcm(buffer)
            } catch (_: Exception) {}
        }
    }

    fun playClayStamp() {
        if (isMuted) return
        scope.launch {
            try {
                // Deep solid thud of pressing an edict into wet clay
                val durationMs = 220
                val totalSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(totalSamples)

                for (i in 0 until totalSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val env = exp(-18.0 * t)

                    val thud = sin(2.0 * PI * (120.0 * exp(-12.0 * t)) * t)
                    val click = (Math.random() * 2.0 - 1.0) * exp(-45.0 * t) * 0.3

                    val sample = (thud + click) * env * 0.40
                    buffer[i] = (sample * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
                }
                playPcm(buffer)
            } catch (_: Exception) {}
        }
    }

    fun playWarHorn() {
        if (isMuted) return
        scope.launch {
            try {
                // Resonant brass horn blast (Sumerian battle horn)
                val durationMs = 700
                val totalSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(totalSamples)

                for (i in 0 until totalSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val attack = (t / 0.08).coerceAtMost(1.0)
                    val decay = exp(-2.5 * t)
                    val env = attack * decay

                    val freq = 220.0 // A3
                    val h1 = sin(2.0 * PI * freq * t)
                    val h2 = 0.7 * sin(4.0 * PI * freq * t)
                    val h3 = 0.5 * sin(6.0 * PI * freq * t)
                    val h4 = 0.3 * sin(8.0 * PI * freq * t)

                    val sample = (h1 + h2 + h3 + h4) * env * 0.28
                    buffer[i] = (sample * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
                }
                playPcm(buffer)
            } catch (_: Exception) {}
        }
    }

    fun playWaterSplash() {
        if (isMuted) return
        scope.launch {
            try {
                val durationMs = 400
                val totalSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                val buffer = ShortArray(totalSamples)

                for (i in 0 until totalSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val env = exp(-8.0 * t)

                    val bubble = sin(2.0 * PI * (320.0 + 180.0 * sin(30.0 * t)) * t)
                    val noise = (Math.random() * 2.0 - 1.0) * 0.4

                    val sample = (bubble + noise) * env * 0.25
                    buffer[i] = (sample * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
                }
                playPcm(buffer)
            } catch (_: Exception) {}
        }
    }

    private fun playPcm(buffer: ShortArray) {
        var track: AudioTrack? = null
        try {
            track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            Thread.sleep((buffer.size * 1000L / SAMPLE_RATE) + 50)
            track.stop()
            track.release()
        } catch (_: Exception) {
            track?.release()
        }
    }
}
