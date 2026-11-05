package com.sleep.sounds.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.sleep.sounds.model.SoundType
import java.util.Random
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.*

class AmbientSoundEngine {

    private val sampleRate = 44100
    private var audioTrack: AudioTrack? = null
    private var isRunning = false
    private var playbackThread: Thread? = null

    // Her ses türü için aktiflik ve ses seviyesi (0.0f - 1.0f)
    private val activeSounds = ConcurrentHashMap<SoundType, Float>()

    // Master volume (0.0f - 1.0f)
    private var masterVolume = 1.0f
    private var fadeMultiplier = 1.0f

    // DSP Durum Değişkenleri
    private val random = Random()
    private var brownLast = 0.0
    private var b0 = 0.0; private var b1 = 0.0; private var b2 = 0.0
    private var b3 = 0.0; private var b4 = 0.0; private var b5 = 0.0; private var b6 = 0.0
    private var wavePhase = 0.0
    private var windPhase = 0.0
    private var cricketPhase = 0.0
    private var streamFilter = 0.0
    private var heartbeatTimer = 0

    init {
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(maxOf(bufferSize, 4096))
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    fun setSoundVolume(type: SoundType, volume: Float) {
        if (volume <= 0.01f) {
            activeSounds.remove(type)
        } else {
            activeSounds[type] = volume.coerceIn(0f, 1f)
        }
        checkAutoStartStop()
    }

    fun stopSound(type: SoundType) {
        activeSounds.remove(type)
        checkAutoStartStop()
    }

    fun setMasterVolume(volume: Float) {
        masterVolume = volume.coerceIn(0f, 1f)
    }

    fun setFadeMultiplier(multiplier: Float) {
        fadeMultiplier = multiplier.coerceIn(0f, 1f)
    }

    fun stopAll() {
        activeSounds.clear()
        stopPlayback()
    }

    private fun checkAutoStartStop() {
        if (activeSounds.isNotEmpty() && !isRunning) {
            startPlayback()
        } else if (activeSounds.isEmpty() && isRunning) {
            stopPlayback()
        }
    }

    @Synchronized
    fun startPlayback() {
        if (isRunning) return
        isRunning = true

        try {
            audioTrack?.play()
        } catch (e: Exception) {
            // Ignore
        }

        playbackThread = Thread {
            val buffer = ShortArray(1024)
            while (isRunning) {
                generateAudioBlock(buffer)
                audioTrack?.write(buffer, 0, buffer.size)
            }
        }.apply {
            priority = Thread.MAX_PRIORITY
            start()
        }
    }

    @Synchronized
    fun stopPlayback() {
        if (!isRunning) return
        isRunning = false
        try {
            playbackThread?.join(500)
        } catch (e: InterruptedException) {
            // Ignore
        }
        playbackThread = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun generateAudioBlock(buffer: ShortArray) {
        val totalVolume = masterVolume * fadeMultiplier
        val activeMap = HashMap(activeSounds)

        if (activeMap.isEmpty() || totalVolume <= 0.001f) {
            buffer.fill(0)
            return
        }

        for (i in buffer.indices) {
            var sample = 0.0
            val white = (random.nextDouble() * 2.0 - 1.0)

            // 1. Pembe Gürültü (Pink Noise) - Voss-McCartney filtresi
            b0 = 0.99886 * b0 + white * 0.0555179
            b1 = 0.99332 * b1 + white * 0.0750759
            b2 = 0.96900 * b2 + white * 0.1538520
            b3 = 0.86650 * b3 + white * 0.3104856
            b4 = 0.55000 * b4 + white * 0.5329522
            b5 = -0.7616 * b5 - white * 0.0168980
            val pink = (b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362) * 0.11
            b6 = white * 0.115926

            // 2. Kahverengi Gürültü (Brown Noise) - Derin Leaky Integrator
            brownLast = (brownLast + (0.02 * white)) / 1.02
            val brown = brownLast * 3.5

            // Ses Kanallarını Birleştirme (DSP Mixing)
            activeMap[SoundType.WHITE_NOISE]?.let { vol ->
                sample += white * 0.25 * vol
            }
            activeMap[SoundType.PINK_NOISE]?.let { vol ->
                sample += pink * 0.6 * vol
            }
            activeMap[SoundType.BROWN_NOISE]?.let { vol ->
                sample += brown * 0.7 * vol
            }

            // Yağmur (Hafif yağmur: filtreli pembe gürültü + damla çıtırtıları)
            activeMap[SoundType.RAIN]?.let { vol ->
                var rainSample = pink * 0.55
                if (random.nextDouble() < 0.004) {
                    rainSample += (random.nextDouble() * 2.0 - 1.0) * 0.35
                }
                sample += rainSample * vol
            }

            // Deniz Dalgası (Okyanus uğultusu dalga salınımları)
            activeMap[SoundType.OCEAN]?.let { vol ->
                wavePhase += 2.0 * Math.PI / (sampleRate * 8.0) // 8 saniyelik dalga periyodu
                if (wavePhase > 2.0 * Math.PI) wavePhase -= 2.0 * Math.PI
                val waveEnvelope = (sin(wavePhase).pow(2.0)) * 0.8 + 0.2
                sample += brown * waveEnvelope * 0.8 * vol
            }

            // Şömine Çıtırtısı (Campfire)
            activeMap[SoundType.FIREPLACE]?.let { vol ->
                var fireSample = brown * 0.25
                if (random.nextDouble() < 0.0015) {
                    fireSample += (random.nextDouble() * 1.5) // Odun çıtırtısı
                }
                sample += fireSample * vol
            }

            // Rüzgar (Soft Wind - frekans salınımı)
            activeMap[SoundType.WIND]?.let { vol ->
                windPhase += 2.0 * Math.PI / (sampleRate * 6.0)
                val windEnv = (sin(windPhase) * 0.4 + 0.6)
                sample += (pink * 0.3 + brown * 0.4) * windEnv * vol
            }

            // Cırcır Böcekleri (Night Crickets)
            activeMap[SoundType.CRICKETS]?.let { vol ->
                cricketPhase += 2.0 * Math.PI * 4500.0 / sampleRate
                if (cricketPhase > 2.0 * Math.PI) cricketPhase -= 2.0 * Math.PI
                val mod = sin(cricketPhase * 0.05)
                val cricket = if (mod > 0.7) sin(cricketPhase) * 0.15 else 0.0
                sample += cricket * vol
            }

            // Nehir & Su Deresi (Water Stream)
            activeMap[SoundType.STREAM]?.let { vol ->
                streamFilter = streamFilter * 0.92 + white * 0.08
                sample += streamFilter * 0.7 * vol
            }

            // Kalp Atışı (Heartbeat - 60 BPM)
            activeMap[SoundType.HEARTBEAT]?.let { vol ->
                heartbeatTimer++
                val period = sampleRate // 60 bpm = saniyede 1 atım
                val t = heartbeatTimer % period
                var hb = 0.0
                if (t < 2500) {
                    hb = sin(2.0 * Math.PI * 65.0 * t / sampleRate) * sin(Math.PI * t / 2500) * 0.8
                } else if (t in 4000..6500) {
                    val t2 = t - 4000
                    hb = sin(2.0 * Math.PI * 75.0 * t2 / sampleRate) * sin(Math.PI * t2 / 2500) * 0.6
                }
                sample += hb * vol
            }

            // Master Volume ve Clipping Koruma (Limiter)
            val mixed = (sample * totalVolume).coerceIn(-1.0, 1.0)
            buffer[i] = (mixed * 32767.0).toInt().toShort()
        }
    }

    fun release() {
        stopPlayback()
        try {
            audioTrack?.release()
        } catch (e: Exception) {
            // Ignore
        }
        audioTrack = null
    }
}
