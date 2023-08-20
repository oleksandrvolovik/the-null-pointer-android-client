package volovyk.thenullpointer.data.remote.nullpointer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import timber.log.Timber
import volovyk.thenullpointer.data.remote.FileDatabase
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import java.io.InputStream
import java.util.Date

class NullPointerFileDatabase(private val nullPointerApiInterface: NullPointerApiInterface) :
    FileDatabase {

    override fun uploadFile(
        filename: String,
        fileSize: Long,
        inputStream: InputStream,
        mediaType: MediaType
    ): Flow<FileUploadState> = channelFlow {
        val requestFile = ProgressEmittingRequestBody(fileSize, inputStream, mediaType)

        val progressJob = CoroutineScope(Dispatchers.IO).launch {
            requestFile.progress.takeWhile { it != ProgressEmittingRequestBody.MAX_PROGRESS }.collect {
                send(FileUploadState.InProgress(it))
            }
        }

        val body = MultipartBody.Part.createFormData("file", filename, requestFile)

        val response = nullPointerApiInterface.postFile(body).execute()

        progressJob.cancel() // Cancel the progress collection job

        val fileUrl = response.body()?.trim()
        val fullFileUrl = "$fileUrl/$filename"
        val fileToken = response.headers().get("x-token")
        val fileExpiresAt = response.headers().get("x-expires")?.toLong()?.let { Date(it) }

        Timber.d("URL - $fullFileUrl\nToken - $fileToken\nExpires at - $fileExpiresAt")
        if (fileUrl == null || fileExpiresAt == null) {
            throw RuntimeException()
        }

        send(FileUploadState.Success(fullFileUrl, fileToken, fileExpiresAt))
    }.flowOn(Dispatchers.IO)

}