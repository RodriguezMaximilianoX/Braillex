package com.rmxdev.braillex.domain.repository

import android.content.Context
import android.net.Uri
import com.rmxdev.braillex.domain.entities.PdfFile
import java.io.File
import java.io.InputStream

interface FileRepository {
    suspend fun uploadPdfAndGeneratedAudio(fileUri: Uri, title: String, context: Context): Result<PdfFile>
    suspend fun getGeneratedFiles(): Result<List<PdfFile>>
    fun getFileUri(file: File, context: Context): Uri
    fun getFileFromUri(context: Context, uri: Uri): File
    fun copyStream(inputStream: InputStream, outputFile: File)
}