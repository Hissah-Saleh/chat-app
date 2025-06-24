package com.example.chatapp.ui.common_componet

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.chatapp.domain.model.ChatItem
import com.example.chatapp.ui.theme.DarkGray
import com.example.chatapp.ui.theme.Gray
import com.example.chatapp.ui.theme.VeryDarkGray
import com.example.chatapp.util.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatBubble(
    chatMessage: ChatItem,
    onVideoClicked: (Uri) -> Unit,
    onImageClicked: (String) -> Unit,
    fetchVideoFrame: (String) -> Bitmap?,
    modifier: Modifier = Modifier
) {
    val bubbleColor =
        if (chatMessage.isMine) MaterialTheme.colorScheme.primary else Gray
    val textColor = if (chatMessage.isMine) MaterialTheme.colorScheme.background else Color.Black

    Column(horizontalAlignment = Alignment.Start) {
        if (!chatMessage.isMine)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                text = chatMessage.username,
                style = MaterialTheme.typography.titleMedium,
                textAlign = if (chatMessage.isMine) TextAlign.End else TextAlign.Start,
            )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = if (chatMessage.isMine) Arrangement.End else Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .background(
                        bubbleColor,
                        RoundedCornerShape(
                            16.dp,
                            16.dp,
                            bottomEnd = if (chatMessage.isMine) 0.dp else 16.dp,
                            bottomStart = if (chatMessage.isMine) 16.dp else 0.dp
                        )
                    )
                    .padding(12.dp)
                    .widthIn(max = 250.dp)
            ) {
                Column {
                    if (chatMessage is ChatItem.Text)
                        Text(
                            text = chatMessage.content,
                            color = textColor,
                            textAlign = if (chatMessage.isMine) TextAlign.Start else TextAlign.End
//                style = MaterialTheme.typography.body1
                        )
                    else if (chatMessage is ChatItem.Media)
                        when {
                            chatMessage.type.startsWith("image") ->
                                GlideImage(
                                    model = chatMessage.url,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(200.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            onImageClicked(chatMessage.url)
                                        },
                                    contentScale = ContentScale.Crop
                                )

                            chatMessage.type.startsWith("video") -> {
                                val uri = Uri.parse(chatMessage.url)
                                //BoxWithConstraints

                                val bitmap by produceState<Bitmap?>(null, chatMessage.url) {
                                    withContext(Dispatchers.IO) {
                                        value = fetchVideoFrame(chatMessage.url)
                                    }
                                }
                                Box(modifier = Modifier
                                    .clickable {
                                        onVideoClicked(uri)
                                    }
                                    .fillMaxWidth()
                                    .height(200.dp), contentAlignment = Alignment.Center) {
//
                                    VideoThumbnail(bitmap)
                                    PlayButton(Modifier.size(40.dp))
                                }


                            }

                            chatMessage.type.startsWith("audio") -> {
                                AudioPlayerWithProgress(audioUrl = chatMessage.url)
                            }
                        }
                    Text(
                        modifier = Modifier.padding(top = 6.dp),
                        text = formatDate(chatMessage.time),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = if (chatMessage.isMine) TextAlign.End else TextAlign.Start,
                        color = if (chatMessage.isMine) VeryDarkGray else DarkGray

                    )
                }

            }
        }
    }
}


@Composable
private fun VideoThumbnail(bitmap: Bitmap?) {
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}
