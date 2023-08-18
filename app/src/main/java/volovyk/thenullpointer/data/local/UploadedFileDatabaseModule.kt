package volovyk.thenullpointer.data.local

import android.content.Context
import androidx.room.Room.databaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UploadedFileDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): UploadedFileDatabase {
        return databaseBuilder(
            appContext,
            RoomUploadedFileDatabase::class.java,
            "uploaded-file-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(uploadedFileDatabase: UploadedFileDatabase): UploadedFileDao {
        return uploadedFileDatabase.getUploadedFileDao()
    }
}