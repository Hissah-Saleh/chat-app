package com.example.chatapp.ui.login

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.chatapp.R
import com.example.chatapp.ui.common_componet.AudioPlayerWithProgress
import com.example.chatapp.ui.common_componet.VideoViewWithPlayButton

@Composable
fun PreviewSelectedMedia(
    uri: Uri?,
    loading: MutableState<Boolean>,
    onClose: () -> Unit,
    sendMedia: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onClose()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ), content = {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 20.dp), contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                    /*verticalArrangement = Arrangement.Center*/
                ) {

                    Row(modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {
                            onClose()
                        }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.9f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        MediaPreview(
                            uri,
                            imageModifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight(.7f),
                            videoModifier = Modifier
                                .fillMaxWidth(0.9f)
                                .fillMaxHeight(.7f),
                            audioModifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(16.dp)
                                )

                        )

                        Button(
                            modifier = Modifier
                                .fillMaxWidth(.9f)
                                .padding(top = 16.dp),
                            enabled = !loading.value,
                            onClick = {
//                        if (viewModel.selectedUri.value != null) {
                                sendMedia()
//                        }
                            }) {
                            Text(text = stringResource(R.string.send))
                        }
                    }
                }
                if (loading.value)
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp),
                    )
            }

        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MediaPreview(
    uri: Uri?,
    audioModifier: Modifier = Modifier,
    videoModifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
) {
    uri ?: return
    val context = LocalContext.current
    val mime = context.contentResolver.getType(uri) ?: return

    when {
        mime.startsWith("image") -> {
            GlideImage(
                uri,
                contentDescription = null,
                modifier = imageModifier
//                    .clip(RoundedCornerShape(8.dp)),
            )
        }

        mime.startsWith("video") -> {
            VideoViewWithPlayButton(
                videoModifier,
                uri
            )

        }

        mime.startsWith("audio") -> {
            AudioPlayerWithProgress(audioUri = uri, modifier = audioModifier)

        }
    }
}
