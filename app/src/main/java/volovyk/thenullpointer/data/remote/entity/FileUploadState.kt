package volovyk.thenullpointer.data.remote.entity

import java.util.Date

sealed class FileUploadState(open val filename: String) {
    data class Success(
        override val filename: String,
        val url: String,
        val token: String?,
        val expiresAt: Date
    ) : FileUploadState(filename)

    data class InProgress(
        override val filename: String,
        val progress: Int
    ) : FileUploadState(filename)

    data class Failure(
        override val filename: String,
        val message: String?,
        val error: Throwable?
    ) : FileUploadState(filename)
}