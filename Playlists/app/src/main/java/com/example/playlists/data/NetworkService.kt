package com.example.playlists.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object NetworkService {
    private const val baseUrl = "https://landing.cal-online.co.il/"
    val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
}

interface PlaylistService {
    @GET("youtube/playlists.json")
    suspend fun getPlaylistObject(): PlaylistObject

    // the PlaylistObject contains the list of the playlists items (the playlist details/snippet like a title, etc.
    // and the playlist videos)
}