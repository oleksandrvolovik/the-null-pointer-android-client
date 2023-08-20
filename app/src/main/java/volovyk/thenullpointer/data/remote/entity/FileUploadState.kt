package volovyk.thenullpointer.data.remote.entity

import java.util.Date

sealed class FileUploadState {
    data class Success(
        val url: String,
        val token: String?,
        val expiresAt: Date,
    ) : FileUploadState()

    data class InProgress(
        val progress: Int
    ) : FileUploadState()

    data class Failure(
        val message: String?,
        val error: Throwable?
    ) : FileUploadState()
}