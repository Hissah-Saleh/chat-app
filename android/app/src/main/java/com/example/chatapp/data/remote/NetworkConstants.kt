package com.example.chatapp.data.remote

import com.example.chatapp.util.isEmulator

object NetworkConstants {
    val CHAT_SERVER_URL= if (isEmulator())
        "http://10.0.2.2:3000" // running on emulator
    else
        TODO() // if you running on real android device handle your own server ip
}