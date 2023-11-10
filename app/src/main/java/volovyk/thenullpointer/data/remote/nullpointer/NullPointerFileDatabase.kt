package volovyk.thenullpointer.data.remote.nullpointer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import volovyk.thenullpointer.data.remote.FileDatabase
import volovyk.thenullpointer.data.remote.entity.FileUploadState
import java.io.IOException
import java.io.InputStream
import java.util.Date

class NullPointerFileDatabase(private val nullPointerApiInterface: NullPointerApiInterface) :
    FileDatabase {

    private val uploadCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun uploadFile(
        filename: String,
        fileSize: Long,
        inputStream: InputStream,
        mediaType: MediaType
    ): Flow<FileUploadState> = channelFlow {
        try {
            val requestFile = ProgressEmittingRequestBody(fileSize, inputStream, mediaType)

            val progressJob = uploadCoroutineScope.launch {
                requestFile.progress.takeWhile { it <= ProgressEmittingRequestBody.MAX_PROGRESS }
                    .collect {
                        send(FileUploadState.InProgress(filename, it))
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

            if (response.code() == 415) {
                val exception = IOException("415 Unsupported Media Type")
                send(
                    FileUploadState.Failure(
                        filename,
                        "This media type is unsupported",
                        exception
                    )
                )
                return@channelFlow
            }

            if (fileUrl == null || fileExpiresAt == null) {
                val exception = IOException("Server response is not complete")
                send(
                    FileUploadState.Failure(
                        filename,
                        "Server response is not complete",
                        exception
                    )
                )
                return@channelFlow
            }

            send(FileUploadState.Success(filename, fullFileUrl, fileToken, fileExpiresAt))
        } catch (e: IOException) {
            send(FileUploadState.Failure(filename, null, e))
        } catch (e: RuntimeException) {
            send(FileUploadState.Failure(filename, null, e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteFile(link: String, token: String) {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("token", token)
            .addFormDataPart("delete", "")
            .build()

        // https://0x0.st/HOvW.zip/testfile.zip -> HOvW.zip
        val filename = link.substringBeforeLast("/").substringAfterLast("/")

        nullPointerApiInterface.deleteFile(filename, requestBody).execute()
    }
}