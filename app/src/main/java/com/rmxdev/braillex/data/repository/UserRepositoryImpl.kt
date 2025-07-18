package com.rmxdev.braillex.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rmxdev.braillex.domain.entities.User
import com.rmxdev.braillex.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
): UserRepository {

    override suspend fun registerUser(email: String, password: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("User creation failed")

            //Crear la entidad usuario con ID de Firebase
            val user = User(
                id = firebaseUser.uid,
                email = email,
                password = password
            )

            //Almacenar usuario en Firestore
            firestore.collection("users").document(firebaseUser.uid).set(user).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(password: String) {
        val user = auth.currentUser ?: throw Exception("Usuario no autenticado")
        val uid = user.uid
        val email = user.email ?: throw Exception("Email no disponible")

        try {
            val credential = EmailAuthProvider.getCredential(email, password)

            // Reautenticación
            user.reauthenticate(credential).await()

            // Borrar documentos en Firestore
            val userDocRef = firestore.collection("users").document(uid)
            val filesQuery = firestore.collection("generated_files").whereEqualTo("userId", uid)

            userDocRef.delete().await()
            val filesSnapshot = filesQuery.get().await()
            filesSnapshot.documents.forEach { it.reference.delete().await() }

            // Borrar archivos en Firebase Storage
            val pdfsRef = storage.reference.child("pdfs/$uid")
            val pdfsAudioRef = storage.reference.child("pdfsAudio/$uid")

            val pdfsResult = pdfsRef.listAll().await()
            pdfsResult.items.forEach { it.delete().await() }

            val pdfsAudioResult = pdfsAudioRef.listAll().await()
            pdfsAudioResult.items.forEach { it.delete().await() }

            // Eliminar cuenta Firebase Auth
            user.delete().await()

        } catch (e: Exception) {
            throw Exception("Error al eliminar la cuenta: ${e.message}", e)
        }
    }

}