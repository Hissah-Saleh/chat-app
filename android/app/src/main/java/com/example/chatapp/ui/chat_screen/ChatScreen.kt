package com.example.chatapp.ui.chat_screen

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chatapp.R
import com.example.chatapp.ui.common_componet.ChatBubble
import com.example.chatapp.ui.login.ExpandMediaMassage
import com.example.chatapp.ui.login.PreviewSelectedMedia
import com.example.chatapp.util.canUpload


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    viewModel: ChatViewModel = hiltViewModel(),
) {

    val listState = rememberLazyListState()
    val chat by viewModel.chatFlow.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getNumberOfUsers()
    }

    LaunchedEffect(chat) {
        if (chat.isNotEmpty()) {
            val viewportHeight = listState.layoutInfo.viewportEndOffset
            listState.animateScrollToItem(index = chat.lastIndex, scrollOffset = viewportHeight)
        }
    }

    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                if (canUpload(uri, context, 20 * 1024 * 1024)) //check size before uploading 20 MB
                    viewModel.pickMedia(uri)
                else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_max_size),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    )

    Column(Modifier.fillMaxSize()) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                Modifier
                    .size(10.dp)
                    .background(
                        color = Color.Green,
                        CircleShape
                    )
            ) {}

            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "${viewModel.numUsers.intValue} ${stringResource(R.string.online)}",
            )
        }

        Box(
            Modifier
                .fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 100.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                state = listState
            ) {

                items(chat.size) { index ->
                    chat.getOrNull(index)?.let { msg ->
                        ChatBubble(msg,
                            onImageClicked = {
                                viewModel.setClickedImage(it)
                            },
                            onVideoClicked = {
                                viewModel.setClickedVideo(it)
                            },
                            fetchVideoFrame = {
                                viewModel.fetchVideoFrame(it)
                            }
                        )
                    }

                }
            }

            Column(Modifier.background(Color.Transparent)) {

                if (isTyping)
                    TypingLottieIndicator()

                Row(
                    Modifier
                        .fillMaxWidth()

                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = text,
                        onValueChange = {
                            viewModel.userTyping()
                            text = it
                        },
                        Modifier.weight(1f),
                        placeholder = { Text(stringResource(id = R.string.message)) },
                        trailingIcon = {
                            IconButton(onClick = {
                                picker.launch(arrayOf("image/*", "video/*", "audio/*"))
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_attach_file),
                                    contentDescription = "Attach"
                                )
                            }
                        },
                        maxLines = 3,
                    )
                    Button(onClick = {
                        if (text.isNotEmpty()) {
                            viewModel.sendMessage(text)
                            text = ""
                        }
                    }) {
                        Text(text = stringResource(R.string.send))
                    }
                }
            }
        }

    }


    if (viewModel.showSelectedMediaPreview.value)
        PreviewSelectedMedia(
            viewModel.selectedUri.value,
            viewModel.loading,
            { viewModel.resetPickMedia() },
            { viewModel.uploadMedia() })

    if (viewModel.clickedVideo.value != null || viewModel.clickedImage.value != null)
        ExpandMediaMassage(viewModel.clickedVideo.value, viewModel.clickedImage.value, {
            viewModel.setClickedImage(null)
            viewModel.setClickedVideo(null)
        })


}

@Composable
fun TypingLottieIndicator(
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.typing)
    )

    LottieAnimation(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .background(Color.Transparent)
            .width(80.dp)
            .height(40.dp)
            .padding(horizontal = 16.dp)

    )
}