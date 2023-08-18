package volovyk.thenullpointer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import volovyk.thenullpointer.data.entity.UploadedFile

@Database(entities = [UploadedFile::class], version = 2)
@TypeConverters(Converters::class)
abstract class RoomUploadedFileDatabase : RoomDatabase(), UploadedFileDatabase {
    abstract override fun getUploadedFileDao(): UploadedFileDao
}