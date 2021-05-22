package com.example.playlists.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.playlists.data.PlaylistObjectItem
import com.example.playlists.data.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

// PlaylistViewModel is used by the views (PlaylistsFragment, PlaylistItemsFragment) to fetch the data (MVVM design)

@HiltViewModel
class PlaylistViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    // the injection of PlaylistRepository enables to inject other repository, for example for testing (with Hilt testing, for example)
    @Inject lateinit var repository: PlaylistRepository

    var playlists: List<PlaylistObjectItem> = emptyList()

    // if playlists list is empty, fetch it from the PlaylistRepository (that get it from the network)
    fun getPlaylists() = liveData(Dispatchers.IO) {
        if (playlists.isNullOrEmpty()) {
            playlists = repository.getPlaylists()
        }
        emit(playlists)
    }

    // to get updated playlists list, anyway fetch it from the PlaylistRepository (that get it from the network)
    fun refreshPlaylists() = liveData(Dispatchers.IO) {
        playlists = repository.getPlaylists()
        emit(playlists)
    }
}