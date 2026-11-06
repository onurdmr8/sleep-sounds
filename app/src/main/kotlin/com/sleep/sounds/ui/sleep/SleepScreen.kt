package com.sleep.sounds.ui.sleep

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sleep.sounds.R
import com.sleep.sounds.model.SoundTrack
import com.sleep.sounds.ui.components.AdBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(
    viewModel: SleepViewModel = viewModel(),
    onTriggerAd: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var showTimerSheet by remember { mutableStateOf(false) }

    val activeCount = state.tracks.count { it.isPlaying }
    val darkBg = Color(0xFF080B14)
    val cardBg = Color(0xFF131A2E)
    val cardActiveBg = Color(0xFF1E294B)
    val accentBlue = Color(0xFF6366F1)
    val cyanGlow = Color(0xFF38BDF8)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (activeCount > 0) stringResource(R.string.active_sounds_count, activeCount)
                            else stringResource(R.string.select_sound_hint),
                            fontSize = 12.sp,
                            color = if (activeCount > 0) cyanGlow else Color.White.copy(alpha = 0.6f)
                        )
                    }
                },
                actions = {
                    // Zamanlayıcı Butonu
                    IconButton(onClick = { showTimerSheet = true }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = stringResource(R.string.timer_title),
                                tint = if (state.isTimerActive) cyanGlow else Color.White
                            )
                            if (state.isTimerActive) {
                                val minutes = state.remainingTimerSeconds / 60
                                val seconds = state.remainingTimerSeconds % 60
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format("%02d:%02d", minutes, seconds),
                                    color = cyanGlow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Master Başlat / Durdur
                    IconButton(onClick = {
                        viewModel.toggleMasterPlay()
                        onTriggerAd()
                    }) {
                        Icon(
                            imageVector = if (state.isMasterPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Oynat/Durdur",
                            tint = if (state.isMasterPlaying) cyanGlow else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0D1224))
            )
        },
        bottomBar = {
            AdBanner()
        },
        containerColor = darkBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.tracks, key = { it.id }) { track ->
                    SoundCard(
                        track = track,
                        onToggle = { viewModel.toggleTrack(track.id) },
                        onVolumeChange = { vol -> viewModel.setTrackVolume(track.id, vol) },
                        cardBg = if (track.isPlaying) cardActiveBg else cardBg,
                        accentColor = if (track.isPlaying) cyanGlow else accentBlue
                    )
                }
            }
        }
    }

    if (showTimerSheet) {
        SleepTimerSheet(
            isTimerActive = state.isTimerActive,
            remainingSeconds = state.remainingTimerSeconds,
            onSetTimer = { minutes ->
                viewModel.setTimer(minutes)
                showTimerSheet = false
                if (minutes > 0) {
                    onTriggerAd()
                }
            },
            onDismiss = { showTimerSheet = false }
        )
    }
}

@Composable
fun SoundCard(
    track: SoundTrack,
    onToggle: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    cardBg: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = if (track.isPlaying) BorderStroke(1.5.dp, accentColor.copy(alpha = 0.8f)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Emoji İkonu
            Text(
                text = track.iconEmoji,
                fontSize = 36.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // İsim
            Text(
                text = getLocalizedSoundTitle(track.id),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (track.isPlaying) Color.White else Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Ses Ayar Slider'ı (Sadece çalarken görünür)
            AnimatedVisibility(visible = track.isPlaying) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Slider(
                        value = track.volume,
                        onValueChange = onVolumeChange,
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = accentColor,
                            activeTrackColor = accentColor,
                            inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                    Text(
                        text = "%${(track.volume * 100).toInt()}",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun getLocalizedSoundTitle(id: String): String {
    return when (id) {
        "rain" -> stringResource(R.string.sound_rain)
        "ocean" -> stringResource(R.string.sound_ocean)
        "fireplace" -> stringResource(R.string.sound_fireplace)
        "wind" -> stringResource(R.string.sound_wind)
        "crickets" -> stringResource(R.string.sound_crickets)
        "stream" -> stringResource(R.string.sound_stream)
        "heartbeat" -> stringResource(R.string.sound_heartbeat)
        "brown_noise" -> stringResource(R.string.sound_brown_noise)
        "pink_noise" -> stringResource(R.string.sound_pink_noise)
        else -> stringResource(R.string.sound_white_noise)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerSheet(
    isTimerActive: Boolean,
    remainingSeconds: Long,
    onSetTimer: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val presets = listOf(15, 30, 45, 60, 90, 120)

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.timer_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.timer_desc),
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                presets.take(3).forEach { min ->
                    FilterChip(
                        selected = false,
                        onClick = { onSetTimer(min) },
                        label = { Text("$min dk") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                presets.drop(3).forEach { min ->
                    FilterChip(
                        selected = false,
                        onClick = { onSetTimer(min) },
                        label = { Text("$min dk") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isTimerActive) {
                Button(
                    onClick = { onSetTimer(0) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.cancel_timer))
                }
            }
        }
    }
}
