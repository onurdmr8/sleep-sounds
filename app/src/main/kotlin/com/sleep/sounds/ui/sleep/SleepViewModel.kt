package com.sleep.sounds.ui.sleep

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sleep.sounds.audio.AmbientSoundEngine
import com.sleep.sounds.model.SoundTrack
import com.sleep.sounds.model.SoundType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SleepUiState(
    val tracks: List<SoundTrack> = emptyList(),
    val isMasterPlaying: Boolean = false,
    val remainingTimerSeconds: Long = 0L,
    val isTimerActive: Boolean = false
)

class SleepViewModel(application: Application) : AndroidViewModel(application) {

    private val engine = AmbientSoundEngine()
    private var timerJob: Job? = null

    private val initialTracks = listOf(
        SoundTrack("rain", "sound_rain", "🌧️"),
        SoundTrack("ocean", "sound_ocean", "🌊"),
        SoundTrack("fireplace", "sound_fireplace", "🪵"),
        SoundTrack("wind", "sound_wind", "💨"),
        SoundTrack("crickets", "sound_crickets", "🦗"),
        SoundTrack("stream", "sound_stream", "💧"),
        SoundTrack("heartbeat", "sound_heartbeat", "💓"),
        SoundTrack("brown_noise", "sound_brown_noise", "🟤"),
        SoundTrack("pink_noise", "sound_pink_noise", "🌸"),
        SoundTrack("white_noise", "sound_white_noise", "⚪")
    )

    private val _uiState = MutableStateFlow(SleepUiState(tracks = initialTracks))
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()

    fun toggleTrack(trackId: String) {
        val currentTracks = _uiState.value.tracks
        val updated = currentTracks.map { track ->
            if (track.id == trackId) {
                val newPlaying = !track.isPlaying
                val type = mapIdToType(track.id)
                if (newPlaying) {
                    engine.setSoundVolume(type, track.volume)
                } else {
                    engine.stopSound(type)
                }
                track.copy(isPlaying = newPlaying)
            } else track
        }
        val anyPlaying = updated.any { it.isPlaying }
        _uiState.update { it.copy(tracks = updated, isMasterPlaying = anyPlaying) }
    }

    fun setTrackVolume(trackId: String, volume: Float) {
        val currentTracks = _uiState.value.tracks
        val updated = currentTracks.map { track ->
            if (track.id == trackId) {
                if (track.isPlaying) {
                    engine.setSoundVolume(mapIdToType(track.id), volume)
                }
                track.copy(volume = volume)
            } else track
        }
        _uiState.update { it.copy(tracks = updated) }
    }

    fun toggleMasterPlay() {
        val currentState = _uiState.value
        if (currentState.isMasterPlaying) {
            // Durdur
            engine.stopAll()
            val stopped = currentState.tracks.map { it.copy(isPlaying = false) }
            _uiState.update { it.copy(tracks = stopped, isMasterPlaying = false) }
        } else {
            // Varsayılan olarak yağmur ve şömineyi aç
            val active = currentState.tracks.map {
                if (it.id == "rain") {
                    engine.setSoundVolume(SoundType.RAIN, it.volume)
                    it.copy(isPlaying = true)
                } else it
            }
            _uiState.update { it.copy(tracks = active, isMasterPlaying = true) }
        }
    }

    fun setTimer(minutes: Int) {
        timerJob?.cancel()
        if (minutes <= 0) {
            _uiState.update { it.copy(isTimerActive = false, remainingTimerSeconds = 0L) }
            engine.setFadeMultiplier(1.0f)
            return
        }

        val totalSeconds = minutes * 60L
        _uiState.update { it.copy(isTimerActive = true, remainingTimerSeconds = totalSeconds) }

        timerJob = viewModelScope.launch {
            var remaining = totalSeconds
            while (remaining > 0) {
                delay(1000L)
                remaining--
                _uiState.update { it.copy(remainingTimerSeconds = remaining) }

                // Son 15 saniyede yumuşak kapanış (fade out)
                if (remaining <= 15) {
                    val fade = (remaining.toFloat() / 15f).coerceIn(0f, 1f)
                    engine.setFadeMultiplier(fade)
                }
            }

            // Süre doldu, tüm sesleri durdur
            engine.stopAll()
            engine.setFadeMultiplier(1.0f)
            val stopped = _uiState.value.tracks.map { it.copy(isPlaying = false) }
            _uiState.update {
                it.copy(
                    tracks = stopped,
                    isMasterPlaying = false,
                    isTimerActive = false,
                    remainingTimerSeconds = 0L
                )
            }
        }
    }

    private fun mapIdToType(id: String): SoundType {
        return when (id) {
            "rain" -> SoundType.RAIN
            "ocean" -> SoundType.OCEAN
            "fireplace" -> SoundType.FIREPLACE
            "wind" -> SoundType.WIND
            "crickets" -> SoundType.CRICKETS
            "stream" -> SoundType.STREAM
            "heartbeat" -> SoundType.HEARTBEAT
            "brown_noise" -> SoundType.BROWN_NOISE
            "pink_noise" -> SoundType.PINK_NOISE
            else -> SoundType.WHITE_NOISE
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        engine.release()
    }
}
