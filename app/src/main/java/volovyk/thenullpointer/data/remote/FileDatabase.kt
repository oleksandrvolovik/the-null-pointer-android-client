package volovyk.thenullpointer.data.remote

import okhttp3.MediaType
import volovyk.thenullpointer.data.remote.entity.FileUploadedResponse
import java.io.InputStream

interface FileDatabase {
    fun uploadFile(
        filename: String,
        inputStream: InputStream,
        mediaType: MediaType
    ): FileUploadedResponse
}