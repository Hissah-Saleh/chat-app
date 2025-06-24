package com.example.chatapp.ui.nav

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapp.ui.chat_screen.ChatsScreen
import com.example.chatapp.ui.login.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavDestination.LOGIN.route
) {


    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {


        composable(route = NavDestination.LOGIN.route) {
            LoginScreen()
        }

        composable(route = NavDestination.CHAT.route) {
//            ChatScreen()
            ChatsScreen()
        }



    }

}