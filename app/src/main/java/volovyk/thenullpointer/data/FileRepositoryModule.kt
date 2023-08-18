package volovyk.thenullpointer.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import volovyk.thenullpointer.data.local.UploadedFileDatabase
import volovyk.thenullpointer.data.remote.FileDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FileRepositoryModule {
    @Provides
    @Singleton
    fun provideFileRepository(
        uploadedFileDatabase: UploadedFileDatabase,
        fileDatabase: FileDatabase
    ): FileRepository {
        return FileRepositoryImpl(uploadedFileDatabase, fileDatabase)
    }
}