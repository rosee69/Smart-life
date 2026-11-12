package ua.edu.mobile.smartlife.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.edu.mobile.smartlife.data.repository.InMemoryRecordRepository
import ua.edu.mobile.smartlife.data.repository.RecordRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecordRepository(
        repository: InMemoryRecordRepository
    ): RecordRepository
}
