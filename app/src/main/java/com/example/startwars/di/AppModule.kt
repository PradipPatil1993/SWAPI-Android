package com.example.startwars.di

import android.app.Application
import android.content.Context
import com.example.startwars.StarWarApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: StarWarApplication): StarWarApplication {
        return application
    }

}

