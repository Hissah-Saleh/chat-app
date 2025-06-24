package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.ui.nav.MainNav
import com.example.chatapp.ui.nav.NavDestination
import com.example.chatapp.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize().imePadding()
                ) { innerPadding ->
                    if(viewModel.loading.value)
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp).padding(16.dp),
                            )
                        }

                    else
                MainNav(
                        modifier = Modifier.padding(innerPadding),
                        rememberNavController(),
                        if(viewModel.username.value?.isNotEmpty() == true)
                            NavDestination.CHAT.route
                        else
                            NavDestination.LOGIN.route
                    )
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fireOnResume()
    }
}
