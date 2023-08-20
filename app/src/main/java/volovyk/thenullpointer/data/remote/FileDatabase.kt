package volovyk.thenullpointer.data.remote

import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import java.io.InputStream

interface FileDatabase {
    fun uploadFile(
        filename: String,
        fileSize: Long,
        inputStream: InputStream,
        mediaType: MediaType
    ): Flow<FileUploadState>
}