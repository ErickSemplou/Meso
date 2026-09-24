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
import kotlin.random.Random

/**
 * Procedural synthesizer for instant tactical sound effects:
 * - War March Drum
 * - Bronze Hammer / Construction
 * - Silver Coins Jingle
 * - Cuneiform Tablet Tap
 * - Victory Fanfare Horn
 */
class SoundEffectsPlayer {

    private val sampleRate = 22050
    private val scope = CoroutineScope(Dispatchers.Default)

    var isMuted: Boolean = false

    private fun playPcm(data: ShortArray) {
        if (isMuted) return
        scope.launch {
            try {
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
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
                    .setBufferSizeInBytes(data.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(data, 0, data.size)
                track.play()
            } catch (_: Exception) {
            }
        }
    }

    fun playClick() {
        val samples = (sampleRate * 0.05).toInt()
        val buf = ShortArray(samples)
        for (i in 0 until samples) {
            val t = i.toDouble() / sampleRate
            val env = exp(-80.0 * t)
            val wave = sin(2.0 * PI * 880.0 * t)
            buf[i] = (wave * env * 20000.0).toInt().toShort()
        }
        playPcm(buf)
    }

    fun playWarDrum() {
        val samples = (sampleRate * 0.35).toInt()
        val buf = ShortArray(samples)
        for (i in 0 until samples) {
            val t = i.toDouble() / sampleRate
            val env = exp(-12.0 * t)
            // Pitch drop for drum punch
            val freq = 120.0 * (1.0 - t * 1.5).coerceAtLeast(0.4)
            val noise = (Random.nextDouble() - 0.5) * 0.3
            val wave = sin(2.0 * PI * freq * t) + noise
            buf[i] = (wave * env * 28000.0).toInt().toShort()
        }
        playPcm(buf)
    }

    fun playCoins() {
        val samples = (sampleRate * 0.3).toInt()
        val buf = ShortArray(samples)
        for (i in 0 until samples) {
            val t = i.toDouble() / sampleRate
            val env = exp(-15.0 * t)
            val wave1 = sin(2.0 * PI * 1800.0 * t)
            val wave2 = sin(2.0 * PI * 2400.0 * t)
            val sample = (wave1 + wave2) * 0.5 * env
            buf[i] = (sample * 24000.0).toInt().toShort()
        }
        playPcm(buf)
    }

    fun playConstructionHammer() {
        val samples = (sampleRate * 0.25).toInt()
        val buf = ShortArray(samples)
        for (i in 0 until samples) {
            val t = i.toDouble() / sampleRate
            val env = exp(-25.0 * t)
            val wave = sin(2.0 * PI * 420.0 * t) + sin(2.0 * PI * 840.0 * t) * 0.5
            buf[i] = (wave * env * 26000.0).toInt().toShort()
        }
        playPcm(buf)
    }

    fun playVictoryFanfare() {
        val samples = (sampleRate * 0.7).toInt()
        val buf = ShortArray(samples)
        val note1Samples = (sampleRate * 0.2).toInt()
        val note2Samples = (sampleRate * 0.2).toInt()
        val note3Samples = samples - note1Samples - note2Samples

        for (i in 0 until samples) {
            val t = i.toDouble() / sampleRate
            val freq = when {
                i < note1Samples -> 440.0 // A4
                i < note1Samples + note2Samples -> 554.37 // C#5
                else -> 659.25 // E5
            }
            val env = exp(-3.0 * (t % 0.25))
            val wave = sin(2.0 * PI * freq * t) + 0.3 * sin(4.0 * PI * freq * t)
            buf[i] = (wave * env * 24000.0).toInt().toShort()
        }
        playPcm(buf)
    }
}
