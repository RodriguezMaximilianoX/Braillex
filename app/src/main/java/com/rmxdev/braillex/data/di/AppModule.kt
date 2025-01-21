package com.rmxdev.braillex.data.di

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.qrcode.QRCodeWriter
import com.rmxdev.braillex.data.network.AndroidTextToSpeechGenerator
import com.rmxdev.braillex.data.network.QrCodeGenerator
import com.rmxdev.braillex.data.repository.FileRepositoryImpl
import com.rmxdev.braillex.data.repository.UserRepositoryImpl
import com.rmxdev.braillex.domain.repository.FileRepository
import com.rmxdev.braillex.domain.repository.UserRepository
import com.tom_roush.pdfbox.pdfparser.PDFParser
import com.tom_roush.pdfbox.text.PDFTextStripper
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
    fun provideTextToSpeechGenerator(@ApplicationContext context: Context): AndroidTextToSpeechGenerator {
        return AndroidTextToSpeechGenerator(context)
    }

    @Provides
    @Singleton
    fun provideQrCodeGenerator(): QrCodeGenerator {
        return QrCodeGenerator()
    }

    @Provides
    @Singleton
    fun provideFileRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        textToSpeech: AndroidTextToSpeechGenerator,
        qrCodeGenerator: QrCodeGenerator
    ): FileRepository{
        return FileRepositoryImpl(firestore, storage, textToSpeech, qrCodeGenerator)
    }
}