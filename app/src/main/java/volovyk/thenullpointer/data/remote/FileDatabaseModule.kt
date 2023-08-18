package volovyk.thenullpointer.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import volovyk.thenullpointer.data.remote.nullpointer.NullPointerApiClient
import volovyk.thenullpointer.data.remote.nullpointer.NullPointerApiInterface
import volovyk.thenullpointer.data.remote.nullpointer.NullPointerFileDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FileDatabaseModule {
    @Provides
    @Singleton
    fun provideApiInterface(): NullPointerApiInterface {
        return NullPointerApiClient.client.create(NullPointerApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideFileDatabase(nullPointerApiInterface: NullPointerApiInterface): FileDatabase {
        return NullPointerFileDatabase(nullPointerApiInterface)
    }
}