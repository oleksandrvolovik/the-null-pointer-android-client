package volovyk.thenullpointer.data.remote

import kotlinx.coroutines.flow.Flow
import volovyk.thenullpointer.data.entity.FileUploadState
import java.io.InputStream

interface FileDatabase {
    fun uploadFile(
        filename: String,
        fileSize: Long,
        inputStream: InputStream,
        mimeType: String
    ): Flow<FileUploadState>

    fun deleteFile(
        link: String,
        token: String
    )
}