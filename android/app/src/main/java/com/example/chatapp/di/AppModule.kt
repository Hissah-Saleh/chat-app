package com.example.chatapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.chatapp.data.remote.ChatSeviceApi
import com.example.chatapp.data.remote.ChatSocketClient
import com.example.chatapp.data.remote.NetworkConstants
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("shared_pref")
            }
        )
    }

    @Provides
    @Singleton
    fun provideSocket()= IO.socket(NetworkConstants.CHAT_SERVER_URL,IO.Options().apply {
        //here to insure connection
        reconnection = true
        reconnectionDelay= 1000
        reconnectionDelayMax=5000
        reconnectionAttempts = Integer.MAX_VALUE   // never give up
        timeout = 20000
    })

    @Provides
    @Singleton
    fun provideChatSocketClient(
        socket: Socket
    )= ChatSocketClient(socket)

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor,
    ) = OkHttpClient
        .Builder()
        .callTimeout(30L, TimeUnit.SECONDS)
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .writeTimeout(30L, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.CHAT_SERVER_URL)
            .client(okHttpClient)  // Use OkHttp client for logging hits
            .addConverterFactory(converterFactory)
            .build()
    }


    @Provides
    @Singleton
    @JvmStatic
    fun provideGson(): Gson = Gson()


    @Provides
    @Singleton
    @JvmStatic
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    internal fun provideChatSeviceApi(retrofit: Retrofit): ChatSeviceApi {
        return retrofit.create(ChatSeviceApi::class.java)
    }


}