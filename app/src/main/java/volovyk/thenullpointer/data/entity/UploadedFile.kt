package volovyk.thenullpointer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "uploaded_files")
data class UploadedFile(
    val name: String,
    val token: String?,
    @PrimaryKey val link: String,
    val uploadedAt: Date,
    val expiresAt: Date
)