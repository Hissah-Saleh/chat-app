package com.example.chatapp.ui.login

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.chatapp.ui.common_componet.VideoViewWithPlayButton

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ExpandMediaMassage(uri: Uri?, url: String?, onClose: () -> Unit) {
    Dialog(
        onDismissRequest = {
            onClose()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ), content = {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
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
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        Box(Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(.9f)) {

                            if (uri != null)
                                VideoViewWithPlayButton(
                                    Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(),
                                    uri
                                )
                            else if (url != null)
                                GlideImage(
                                    model = url,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                        }

                    }
                }

            }

        }
    )
}
