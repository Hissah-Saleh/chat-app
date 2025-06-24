package com.example.chatapp.data.remote

import io.socket.client.Ack
import io.socket.client.Socket
import io.socket.emitter.Emitter
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatSocketClient @Inject constructor(
    private val socket: Socket
) {


    fun connect() {
        socket.apply {
            if (connected())
                return
            connect()
            on(Socket.EVENT_CONNECT) {
                Timber.e("SOCKET ✅ Connected ")
            }
            on(Socket.EVENT_CONNECT_ERROR) { args ->
                Timber.e("SOCKET ❌ Connect error => ${args.joinToString()}")
            }
            on(Socket.EVENT_DISCONNECT) { args ->
                Timber.e("SOCKET = Disconnected ")
            }
        }


    }


    fun disconnect() {
        if (socket.connected()) {
            socket.disconnect()
        }
    }

    fun observe(event: String, listener: (Array<Any>) -> Unit): Emitter? {
        return socket.on(event) { args ->
            listener(args)
        }
    }

    fun send(event: String, args: String) {
        if (socket.connected()) {
            socket.emit(event, args)
        } else {

            socket.once(Socket.EVENT_CONNECT) {// since some times it take time to connect so it wait until it conncted
                socket.emit(event, args)
            }
        }
    }


    fun sendEventWithAck(event: String, ack: Ack){
        socket.emit(event, ack)
    }


}
