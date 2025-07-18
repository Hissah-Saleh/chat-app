package com.example.chatapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.R


@Composable
fun LoginScreen(
    viewModel: LoginViewModel= hiltViewModel()
){

    var text by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ){

        TextField(
            value = text,
            onValueChange = {  text= it},
            Modifier.fillMaxWidth()
                .padding(0.dp) ,
            placeholder = { Text(text = stringResource(id = R.string.username)) },
            singleLine = true
        )

        Button(
            onClick = { viewModel.addUser(text)},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.submit))
        }
    }
}
