package com.rmxdev.braillex.domain.useCase.getFileUriUseCase

import android.content.Context
import android.net.Uri
import com.rmxdev.braillex.domain.repository.FileRepository
import java.io.File
import javax.inject.Inject

class GetFileUriUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    operator fun invoke(file: File, context: Context): Uri {
        return fileRepository.getFileUri(file, context)
    }
}