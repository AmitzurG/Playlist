package com.example.playlists.di

import com.example.playlists.data.NetworkService
import com.example.playlists.data.PlaylistService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object PlaylistModule {
    @Provides
    fun providePlaylistService(): PlaylistService = NetworkService.retrofit.create(PlaylistService::class.java)
}