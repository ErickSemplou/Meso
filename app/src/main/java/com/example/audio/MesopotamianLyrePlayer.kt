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
 * Atmospheric Ancient Mesopotamian Ambient Soundscape.
 * Generates an ultra-soft, meditative acoustic lyre & warm temple flute ambiance.
 * Designed with gentle, slow pacing, generous natural acoustic decay, soft volume,
 * and calming pauses between phrases to ensure non-intrusive, deeply relaxing background presence.
 */
class MesopotamianLyrePlayer {

    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var isMuted: Boolean = false

    private val sampleRate = 22050

    // Calming ancient pentatonic harmony frequencies (Warm D/A modal)
    // Low, soothing acoustic harp tones
    private val soothingNotes = doubleArrayOf(
        146.83, // D3 (Deep warm bass)
        196.00, // G3
        220.00, // A3
        261.63, // C4
        293.66, // D4
        329.63, // E4
        392.00, // G4
        440.00  // A4
    )

    // Gentle, sparse contemplative melody sequence
    private val serenePhrases: List<List<Int>> = listOf(
        listOf(0, 2, 4, 3),          // Warm opening chord arpeggio
        listOf(1, 4, 5, 4),          // River flow motif
        listOf(0, 3, 4, 6),          // Distant temple bells
        listOf(2, 4, 5, 2),          // Golden sunset melody
        listOf(0, 1, 3, 0)           // Grounding resolution
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
                var phraseIdx = 0
                while (isActive) {
                    if (isMuted) {
                        delay(500)
                        continue
                    }

                    val phrase = serenePhrases[phraseIdx % serenePhrases.size]

                    // Play gentle slow arpeggio
                    for (noteKey in phrase) {
                        if (isMuted || !isActive) break

                        val freq = soothingNotes[noteKey.coerceIn(0, soothingNotes.size - 1)]
                        val isDeepBass = noteKey <= 1
                        val durationMs = if (isDeepBass) 1800 else 1400

                        val audio = synthesizeSereneAcousticTone(
                            freq = freq,
                            durationMs = durationMs,
                            isBass = isDeepBass
                        )
                        audioTrack?.write(audio, 0, audio.size)

                        // Gentle, breathing spacing between individual plucked strings (800ms)
                        delay(750L)
                    }

                    phraseIdx++

                    // Generous, calming silence pause of 3.5 to 5 seconds between musical phrases
                    // Allows the player to focus peacefully without auditory fatigue!
                    delay(3800L)
                }
            }
        } catch (_: Exception) {
            // Gracefully ignore if audio device is unavailable
        }
    }

    /**
     * Synthesizes a warm, soft acoustic gut-string pluck with delicate wooden resonance.
     * Keeps high-frequency harmonics dampened to prevent harshness or ear fatigue.
     */
    private fun synthesizeSereneAcousticTone(freq: Double, durationMs: Int, isBass: Boolean): ShortArray {
        val totalSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(totalSamples)

        // Soft, smooth decay
        val decayRate = if (isBass) 1.5 else 2.2
        val volumeGain = if (isBass) 0.14 else 0.11 // Low, mellow, pleasant volume

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate

            // Gentle exponential decay curve
            val envelope = exp(-decayRate * t)

            // Warm fundamental tone + soft octaves (no sharp buzzing harmonics)
            val fundamental = sin(2.0 * PI * freq * t)
            val warmOctave = 0.22 * sin(4.0 * PI * freq * t) * exp(-decayRate * 1.5 * t)
            val subtleThird = 0.08 * sin(6.0 * PI * freq * t) * exp(-decayRate * 2.5 * t)

            // Very subtle gentle wooden flute drone in bass notes
            val fluteDrone = if (isBass) {
                0.06 * sin(2.0 * PI * (freq * 0.5) * t)
            } else 0.0

            val combined = (fundamental + warmOctave + subtleThird + fluteDrone) * envelope * volumeGain

            // Soft-clipping into 16-bit PCM
            buffer[i] = (combined * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
        }

        return buffer
    }

    fun stop() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
    }
}
