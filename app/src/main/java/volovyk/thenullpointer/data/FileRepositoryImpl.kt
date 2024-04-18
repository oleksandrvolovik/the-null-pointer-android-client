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
import volovyk.thenullpointer.data.entity.FileUploadState
import java.io.InputStream
import java.util.Date

class FileRepositoryImpl(
    uploadedFileDatabase: UploadedFileDatabase,
    private val fileDatabase: FileDatabase
) : FileRepository {

    private val uploadedFileDao = uploadedFileDatabase.getUploadedFileDao()

    override fun getFilesFlow(): Flow<List<UploadedFile>> = uploadedFileDao.getAll()

    override fun uploadFile(
        filename: String,
        fileSize: Long,
        inputStream: InputStream,
        mediaType: MediaType
    ): Flow<FileUploadState> = fileDatabase.uploadFile(filename, fileSize, inputStream, mediaType)
        .onEach {
            if (it is FileUploadState.Success) {
                uploadedFileDao.insert(
                    UploadedFile(
                        filename,
                        it.token,
                        it.url,
                        Date(),
                        it.expiresAt
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun deleteFile(file: UploadedFile): Unit = withContext(Dispatchers.IO) {
        uploadedFileDao.delete(file)
        file.token?.let {
            fileDatabase.deleteFile(file.link, it)
        }
    }
}