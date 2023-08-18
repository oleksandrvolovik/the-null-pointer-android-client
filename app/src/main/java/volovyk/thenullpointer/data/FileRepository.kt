package volovyk.thenullpointer.data

import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType
import volovyk.thenullpointer.data.entity.UploadedFile
import java.io.InputStream

interface FileRepository {
    fun getFilesFlow(): Flow<List<UploadedFile>>
    suspend fun uploadFile(filename: String, inputStream: InputStream, mediaType: MediaType)
}