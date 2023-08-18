package volovyk.thenullpointer.data.remote.nullpointer

import okhttp3.MediaType
import okhttp3.MultipartBody
import timber.log.Timber
import volovyk.thenullpointer.data.remote.FileDatabase
import volovyk.thenullpointer.data.remote.entity.FileUploadedResponse
import java.io.InputStream
import java.util.Date

class NullPointerFileDatabase(private val nullPointerApiInterface: NullPointerApiInterface) :
    FileDatabase {

    override fun uploadFile(
        filename: String,
        inputStream: InputStream,
        mediaType: MediaType
    ): FileUploadedResponse {
        val requestFile = InputStreamRequestBody(inputStream, mediaType)

        val body = MultipartBody.Part.createFormData("file", filename, requestFile)

        val response = nullPointerApiInterface.postFile(body).execute()

        val fileUrl = response.body()?.trim()
        val fullFileUrl = "$fileUrl/$filename"
        val fileToken = response.headers().get("x-token")
        val fileExpiresAt = response.headers().get("x-expires")?.toLong()?.let { Date(it) }

        Timber.d("URL - $fullFileUrl\nToken - $fileToken\nExpires at - $fileExpiresAt")
        if (fileUrl == null || fileExpiresAt == null) {
            throw RuntimeException()
        }

        return FileUploadedResponse(fullFileUrl, fileToken, fileExpiresAt)
    }

}