package volovyk.thenullpointer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import volovyk.thenullpointer.data.entity.UploadedFile

@Dao
interface UploadedFileDao {
    @Query("SELECT * FROM uploaded_files")
    fun getAll(): Flow<List<UploadedFile>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(file: UploadedFile)

    @Delete
    fun delete(file: UploadedFile)
}