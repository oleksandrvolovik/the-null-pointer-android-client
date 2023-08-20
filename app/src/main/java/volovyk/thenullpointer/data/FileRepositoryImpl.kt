package volovyk.thenullpointer.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import volovyk.thenullpointer.data.entity.UploadedFile
import volovyk.thenullpointer.data.local.UploadedFileDatabase
import volovyk.thenullpointer.data.remote.FileDatabase
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import java.io.InputStream
import java.util.Date

class FileRepositoryImpl(
    uploadedFileDatabase: UploadedFileDatabase,
    private val fileDatabase: FileDatabase
) : FileRepository {

    private val uploadedFileDao = uploadedFileDatabase.getUploadedFileDao()

    override fun getFilesFlow(): Flow<List<UploadedFile>> =
        uploadedFileDao.getAll()

    override suspend fun uploadFile(
        filename: String,
        fileSize: Long,
        inputStream: InputStream,
        mediaType: MediaType
    ): Flow<FileUploadState> {
        return withContext(Dispatchers.IO) {
            val fileUploadState =
                fileDatabase.uploadFile(filename, fileSize, inputStream, mediaType)

            val realFileUploadState = fileUploadState.onEach {
                if (it is FileUploadState.Success) {
                    val uploadedFile = UploadedFile(
                        filename,
                        it.token,
                        it.url,
                        Date(),
                        it.expiresAt
                    )

                    uploadedFileDao.insert(uploadedFile)
                }
            }.flowOn(Dispatchers.IO)

            realFileUploadState
        }
    }
}