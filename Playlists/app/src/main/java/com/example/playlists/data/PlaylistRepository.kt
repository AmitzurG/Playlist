package com.example.playlists.data

import android.util.Log
import java.net.UnknownHostException
import javax.inject.Inject

// the injection of PlaylistService enables to inject other services, for example service
// that provides mock playlists for testing, and using Hilt testing

class PlaylistRepository @Inject constructor(private val playlistService: PlaylistService) {
    private val TAG = PlaylistRepository::class.java.name

    // get the playlists list (list of PlaylistObjectItem) from the network (using retrofit)
    suspend fun getPlaylists(): List<PlaylistObjectItem> {
        var playlists = emptyList<PlaylistObjectItem>()
        try {
            val playlistObject = playlistService.getPlaylistObject()
            playlists = playlistObject.items
        } catch (e: UnknownHostException) {
            Log.e(TAG, "Networking problem, UnknownHostException, empty list will be returned, error=${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Exception, error=${e.message}, empty list will be returned")
        }
        return playlists
    }
}