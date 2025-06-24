package com.example.chatapp.ui.common_componet

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.ui.theme.VeryDarkGray
import com.example.chatapp.util.formatMs
import kotlinx.coroutines.delay

@Composable
fun AudioPlayerWithProgress(
    modifier: Modifier = Modifier,
    audioUri: Uri? = null,
    audioUrl: String? = null,
) {
    val context = LocalContext.current
    var duration by remember { mutableStateOf(0) }
    var currentPosition by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }

    // Initialize MediaPlayer once
    val mediaPlayer = remember {
        MediaPlayer().apply {
            audioUri?.let { setDataSource(context, it) }
            audioUrl?.let {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                setDataSource(it)
            }
            prepareAsync()
            setOnPreparedListener { duration = it.duration }
        }
    }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer.release() }
    }

    // Update currentPosition when playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = mediaPlayer.currentPosition
            delay(200)
        }
    }

    Column(modifier.padding(top = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        isPlaying = false
                    } else if (duration > 0) {
                        mediaPlayer.start()
                        isPlaying = true
                    }
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow
                    ),
                    contentDescription = null,
                    tint = VeryDarkGray
                )
            }

            Slider(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                value = currentPosition.coerceIn(0, duration).toFloat(),
                onValueChange = { newValue ->
                    // while dragging
                    currentPosition = newValue.toInt()
                },
                onValueChangeFinished = {
                    // when drag ends, seek to position
                    mediaPlayer.seekTo(currentPosition)
                },
                valueRange = 0f..duration.coerceAtLeast(0).toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.background,
                    activeTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondary,

                    )
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            text = "${formatMs(currentPosition)} / ${formatMs(duration)}",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.End,
            color = VeryDarkGray
        )
    }
}