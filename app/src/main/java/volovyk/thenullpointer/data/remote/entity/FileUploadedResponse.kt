package volovyk.thenullpointer.data.remote.entity

import java.util.Date

class FileUploadedResponse(
    val url: String,
    val token: String?,
    val expiresAt: Date,
)