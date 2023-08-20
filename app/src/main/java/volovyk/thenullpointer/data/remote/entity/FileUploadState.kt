package volovyk.thenullpointer.data.remote.entity

import kotlinx.coroutines.flow.StateFlow
import java.util.Date

sealed class FileUploadState {
    data class Success(
        val url: String,
        val token: String?,
        val expiresAt: Date,
    ) : FileUploadState()

    data class InProgress(
        val progress: StateFlow<Int>
    ) : FileUploadState()

    data class Failure(
        val message: String?,
        val error: Throwable?
    ) : FileUploadState()
}