package com.example.chatapp.util

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.documentfile.provider.DocumentFile
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun canUpload(uri: Uri, context: Context, maxBytes: Long): Boolean {
    // wrap the picked Uri in a DocumentFile
    val doc = DocumentFile.fromSingleUri(context, uri)
    val size = doc?.length() ?: return false
    return size <= maxBytes
}

fun formatDate(dateString: String): String{
    return try {
        val inputFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat =  SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

        val date = inputFormat.parse(dateString)
        if(date!=null) {
            val formattedDate = outputFormat.format(date)
            formattedDate
        }else
            dateString
    } catch ( e:ParseException) {
        e.printStackTrace()
        return dateString
    }
}

fun formatMs(ms: Int): String {
    val secs = ms / 1000
    return "%d:%02d".format(secs / 60, secs % 60)
}


fun getYourOwnDeviceIpAddress():String{
   return if (isEmulator())
        "http://10.0.2.2:3000" // running on emulator
   else
       TODO() // if you running on real android device handle your own server ip
}


fun isEmulator(): Boolean {
    return (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.lowercase().contains("vbox")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || Build.PRODUCT == "google_sdk"
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.HARDWARE.contains("qemu"))
}