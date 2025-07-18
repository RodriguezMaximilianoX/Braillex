package com.rmxdev.braillex.data.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import coil.ImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.rmxdev.braillex.data.network.AndroidTextToSpeechGenerator
import com.rmxdev.braillex.data.repository.FileRepositoryImpl
import com.rmxdev.braillex.data.repository.MediaRepositoryImpl
import com.rmxdev.braillex.data.repository.ReproductorRepositoryImpl
import com.rmxdev.braillex.data.repository.UserRepositoryImpl
import com.rmxdev.braillex.domain.repository.FileRepository
import com.rmxdev.braillex.domain.repository.MediaRepository
import com.rmxdev.braillex.domain.repository.ReproductorRepository
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
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
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
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): UserRepository {
        return UserRepositoryImpl(auth, firestore, storage)
    }

    @Provides
    @Singleton
    fun provideFileRepository(
        @ApplicationContext context: Context,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        textToSpeech: AndroidTextToSpeechGenerator,
        auth: FirebaseAuth
    ): FileRepository {
        return FileRepositoryImpl(context, storage, firestore, textToSpeech, auth)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideCoil(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideReproductorRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): ReproductorRepository {
        return ReproductorRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideMediaRepository(
        storage: FirebaseStorage
    ): MediaRepository{
        return MediaRepositoryImpl(storage)
    }
}