package com.rmxdev.braillex.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rmxdev.braillex.data.network.AndroidTextToSpeechGenerator
import com.rmxdev.braillex.data.network.QrCodeGenerator
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.repository.FileRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val textToSpeech: AndroidTextToSpeechGenerator,
    private val qrCodeGenerator: QrCodeGenerator
): FileRepository {
    override suspend fun uploadPdfAndGeneratedAudio(fileUri: Uri, title: String): Result<PdfFile> {
        return try {
            //Subir el archivo PDF
            val pdfRef = storage.reference.child("pdfs/${UUID.randomUUID()}.pdf")
            pdfRef.putFile(fileUri).await()
            //val pdfUrl = pdfRef.downloadUrl.await().toString()

            //Generar el audio del PDF
            val audioUrl = textToSpeech.generateAudioFromPdf(fileUri)

            //Generar el codigo QR con la URL del audio
            val qrCodeUrl = qrCodeGenerator.generateQrCode(audioUrl)

            //Guardar los datos en Firestore
            val pdfFile = PdfFile(
                id = UUID.randomUUID().toString(),
                title = title,
                audioUrl = audioUrl,
                qrCodeUrl = qrCodeUrl
            )
            firestore.collection("generated_files").document(pdfFile.id).set(pdfFile).await()

            Result.success(pdfFile)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getGeneratedFiles(): Result<List<PdfFile>> {
        return try {
            val snapshot = firestore.collection("generated_files").get().await()
            val files = snapshot.documents.map { document -> document.toObject(PdfFile::class.java)!! }

            Result.success(files)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

}