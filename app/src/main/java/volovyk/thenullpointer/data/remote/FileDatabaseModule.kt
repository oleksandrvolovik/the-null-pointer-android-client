package volovyk.thenullpointer.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import volovyk.thenullpointer.BuildConfig
import volovyk.thenullpointer.data.remote.nullpointer.NullPointerApiInterface
import volovyk.thenullpointer.data.remote.nullpointer.NullPointerFileDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FileDatabaseModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.NULL_POINTER_API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): NullPointerApiInterface {
        return retrofit.create(NullPointerApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideFileDatabase(nullPointerApiInterface: NullPointerApiInterface): FileDatabase {
        return NullPointerFileDatabase(nullPointerApiInterface)
    }
}