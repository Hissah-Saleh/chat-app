package com.example.chatapp.ui.common_componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.R

@Composable
fun PlayButton(modifier: Modifier) {
    Box(
        Modifier.background(
            Color.White.copy(alpha = .8f),
            CircleShape
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_play_arrow),
            contentDescription = "Play",
            modifier = modifier.size(40.dp)
        )
    }
}