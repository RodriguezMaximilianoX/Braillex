package com.rmxdev.braillex.data.di

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.provider.MediaStore.Audio.Media
import android.util.Log
import coil.ImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.rmxdev.braillex.data.network.AndroidTextToSpeechGenerator
import com.rmxdev.braillex.data.network.QrCodeGenerator
import com.rmxdev.braillex.data.repository.FileRepositoryImpl
import com.rmxdev.braillex.data.repository.UserRepositoryImpl
import com.rmxdev.braillex.domain.repository.FileRepository
import com.rmxdev.braillex.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        Log.d("FirebaseModule", "FirebaseAuth instance provided")
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        Log.d("FirestoreModule", "Firestore instance provided")
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): UserRepository {
        Log.d("AppModule", "UserRepositoryImpl instance provided")
        return UserRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideFileRepository(
        @ApplicationContext context: Context,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        textToSpeech: AndroidTextToSpeechGenerator
    ): FileRepository {
        return FileRepositoryImpl(context, storage, firestore, textToSpeech)
    }

    @Provides
    @Singleton
    fun provideMedia(): MediaPlayer{
        return MediaPlayer()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson{
        return Gson()
    }

    @Provides
    @Singleton
    fun provideCoil(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context).build()
    }
}