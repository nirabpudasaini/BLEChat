package com.pudasaini.blechat.di

import com.pudasaini.blechat.data.BleRepositoryImpl
import com.pudasaini.blechat.domain.repository.BleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BleModule {
    @Binds
    @Singleton
    abstract fun bindBleRepository(impl: BleRepositoryImpl): BleRepository
}