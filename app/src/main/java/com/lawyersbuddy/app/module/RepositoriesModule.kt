package com.lawyersbuddy.app.module


import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoriesModule {
    @Binds
    fun mainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository

}