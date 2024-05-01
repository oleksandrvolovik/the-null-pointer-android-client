package volovyk.thenullpointer.data

import kotlinx.coroutines.flow.Flow
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.data.entity.FileUploadState
import java.io.InputStream

interface FileRepository {
    fun getFilesFlow(): Flow<List<UploadedFile>>
    fun uploadFile(
        filename: String,
        fileSize: Long,
        inputStream: InputStream,
        mimeType: String
    ): Flow<FileUploadState>

    suspend fun deleteFile(file: UploadedFile)
}