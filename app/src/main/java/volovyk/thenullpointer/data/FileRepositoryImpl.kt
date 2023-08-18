package volovyk.thenullpointer.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.data.local.UploadedFileDatabase
import volovyk.thenullpointer.data.remote.FileDatabase
import java.io.InputStream
import java.util.Date

class FileRepositoryImpl(
    uploadedFileDatabase: UploadedFileDatabase,
    private val fileDatabase: FileDatabase
) : FileRepository {

    private val uploadedFileDao = uploadedFileDatabase.getUploadedFileDao()

    override fun getFilesFlow(): Flow<List<UploadedFile>> =
        uploadedFileDao.getAll()

    override suspend fun uploadFile(filename: String, inputStream: InputStream, mediaType: MediaType) {
        withContext(Dispatchers.IO) {
            val fileUploadedResponse = fileDatabase.uploadFile(filename, inputStream, mediaType)

            val uploadedFile = UploadedFile(
                filename,
                fileUploadedResponse.token,
                fileUploadedResponse.url,
                Date(),
                fileUploadedResponse.expiresAt
            )

            uploadedFileDao.insert(uploadedFile)
        }
    }
}