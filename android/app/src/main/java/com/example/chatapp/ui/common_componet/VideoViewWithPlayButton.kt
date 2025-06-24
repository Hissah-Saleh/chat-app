package com.example.chatapp.ui.common_componet

import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun VideoViewWithPlayButton(modifier: Modifier = Modifier, uri: Uri) {
    // Keep a reference to the VideoView
    val videoView = remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isReady by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }


    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {

                        setVideoURI(uri)

                        setOnCompletionListener {
                            isPlaying = false
                        }

                        setOnPreparedListener { player ->
                            isReady = true
                            videoView.value = player
                            isLoading = false

                        }

                    }
                },
                modifier = Modifier.clickable {
                    if (isPlaying == true) {
                        videoView.value?.pause()
                        isPlaying = false
                    } else if (isPlaying == false) {
                        videoView.value?.start()
                        isPlaying = true
                    }
                }
            )

            if (isReady && isPlaying == false)
                PlayButton(Modifier.size(80.dp))

            if (isLoading)
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp),
                )


        }
    }
}


