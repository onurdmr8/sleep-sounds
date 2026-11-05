package com.sleep.sounds.model

data class SoundTrack(
    val id: String,
    val titleResKey: String,
    val iconEmoji: String,
    val defaultVolume: Float = 0.5f,
    val isPlaying: Boolean = false,
    val volume: Float = 0.5f
)

enum class SoundType {
    RAIN,
    OCEAN,
    FIREPLACE,
    WIND,
    CRICKETS,
    BROWN_NOISE,
    PINK_NOISE,
    WHITE_NOISE,
    STREAM,
    HEARTBEAT
}
