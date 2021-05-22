package com.example.playlists.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlists.R
import com.example.playlists.data.PlaylistObjectItem
import com.example.playlists.databinding.FragmentPlaylistsBinding
import com.example.playlists.databinding.PlaylistItemLayoutBinding
import com.manojbhadane.genericadapter.GenericAdapter

// PlaylistsFragment shows the playlists with RecyclerView

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    // this property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPlaylistsRecyclerView()
        setPlaylists()
        setSwipeRefreshLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // to produce the playlists RecyclerView (list) use GenericAdapter which ease to create the list
    private val playlistsRecyclerViewAdapter = object : GenericAdapter<PlaylistObjectItem, PlaylistItemLayoutBinding>(context, ArrayList()) {
        override fun getLayoutResId() = R.layout.playlist_item_layout

        override fun onBindData(playlistItem: PlaylistObjectItem?, position: Int, dataBinding: PlaylistItemLayoutBinding?) {
            dataBinding?.playlistNameTextView?.text = playlistItem?.snippet?.title
        }

        override fun onItemClick(playlistItem: PlaylistObjectItem?, position: Int) {
            val action = PlaylistsFragmentDirections.actionPlaylistFragmentToPlaylistItemsFragment(position)
            findNavController().navigate(action)
        }
    }

    private fun setPlaylistsRecyclerView() = binding.playlistsRecyclerView.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        adapter = playlistsRecyclerViewAdapter
    }

    private fun setPlaylists() {
        binding.swipeRefreshLayout.isRefreshing = true  // using SwipeRefreshLayout progress mark during getting the playlists
        (activity as MainActivity).playlistViewModel.getPlaylists().observe(viewLifecycleOwner) {
            //  observe on the getting of playlists, when the playlists list is gotten we are notified here and set the RecyclerView adapter with the list
            playlistsRecyclerViewAdapter.addItems(ArrayList(it))
            // dismiss the progress mark when finish getting the playlists
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    // enable to refresh/update the playlist list by vertical swipe gesture
    private fun setSwipeRefreshLayout() = binding.swipeRefreshLayout.setOnRefreshListener {
        (activity as MainActivity).playlistViewModel.refreshPlaylists().observe(viewLifecycleOwner) {
            // when the updated playlists list is gotten we are notified here and set the RecyclerView adapter with the updated list
            playlistsRecyclerViewAdapter.addItems(ArrayList(it))
            // dismiss the SwipeRefreshLayout progress mark when finishing to update the playlists
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}