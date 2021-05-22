package com.example.playlists.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlists.R
import com.example.playlists.data.Item
import com.example.playlists.databinding.FragmentPlaylistItemsBinding
import com.example.playlists.databinding.ItemLayoutBinding
import com.manojbhadane.genericadapter.GenericAdapter

// PlaylistItemsFragment shows a playlist items/videos in RecyclerView (the playlist, which show its items,
// is gotten by the Fragment arguments)

class PlaylistItemsFragment : Fragment() {
    private val TAG = PlaylistItemsFragment::class.java.name

    private var _binding: FragmentPlaylistItemsBinding? = null
    private val binding get() = _binding!!     // this property is only valid between onCreateView and onDestroyView
    private val args: PlaylistItemsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setPlaylistItemsRecyclerView()
        setPlaylistItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTitle() {
        val viewModel = (activity as MainActivity).playlistViewModel
        val playlistName = if (args.playlistIndex < viewModel.playlists.size) viewModel.playlists[args.playlistIndex].snippet.title else ""
        (activity as? AppCompatActivity)?.supportActionBar?.title = String.format(getString(R.string.playlistItemsFragmentLabel), playlistName)
    }

    // to produce the playlist items RecyclerView (list) use GenericAdapter which ease to create the list
    private val playlistItemsRecyclerViewAdapter = object : GenericAdapter<Item, ItemLayoutBinding>(context, ArrayList()) {
        override fun getLayoutResId() = R.layout.item_layout

        override fun onBindData(item: Item?, position: Int, dataBinding: ItemLayoutBinding?) {
            val thumbnailUrl = item?.snippet?.thumbnails?.default?.url // using the default thumbnail
            val thumbnailImageView = dataBinding?.thumbnailImageView
            if (thumbnailUrl != null && thumbnailImageView != null) {
                setThumbnail(thumbnailUrl, thumbnailImageView)
            }
            dataBinding?.itemNameTextView?.text = item?.snippet?.title
        }

        override fun onItemClick(item: Item?, position: Int) {
            // when click on playlist item/video we play this video, we have implemented two ways to play the video
            // 1. using CustomTabsIntent (currently commented)
            // 2. using YouTube library YouTubeAndroidPlayerApi (which is currently used)
            //
            // Note: when using YouTubeAndroidPlayerApi, on few of the videos we get the message: "This video contains content from ...
            // who has blocked it from display on this website or application. Watch on YouTube",
            // on this case you can uncomment the CustomTabsIntent option (uncomment videoId?.let { playVideo(it) }),
            // and comment the option YouTubeAndroidPlayerApi, and the video will be played

            val videoId = item?.contentDetails?.videoId

            // implementation that open the video with CustomTabsIntent (isn't executed, commented)
          // videoId?.let { playVideo(it) }

            // implementation that open the vide with YouTubePlayerFragment (YouTubeAndroidPlayerApi)
            videoId?.let {
                val action = PlaylistItemsFragmentDirections.actionPlaylistItemsFragmentToVideoPlayerFragment(it, item.snippet.title)
                findNavController().navigate(action)
            }
        }

        private fun setThumbnail(thumbnailUrl: String, imageView: ImageView) = Glide
            .with(imageView.context)
            .load(thumbnailUrl)
            .error(R.drawable.n_a)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)

        // play the video with custom tab
        private fun playVideo(videoId: String) {
            try {
                val builder = CustomTabsIntent.Builder()
                val customParams = CustomTabColorSchemeParams.Builder().setToolbarColor(requireContext().getColor(R.color.purple_500)).build()
                builder.setDefaultColorSchemeParams(customParams)
                val customTabsIntent = builder.build()
                // open the video into the app, in chrome browser, otherwise youtube app will be opened (in separate window) if it is installed
                customTabsIntent.intent.setPackage("com.android.chrome")
                customTabsIntent.launchUrl(requireContext(), Uri.parse("https://www.youtube.com/watch?v=$videoId"))
            } catch (e: IllegalStateException) {
                Log.w(TAG, "IllegalStateException - error=${e.message}")
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "IllegalArgumentException - error=${e.message}")
            } catch (e: Exception) {
                Log.w(TAG, "Exception - error=${e.message}")
            }
        }
    }

    private fun setPlaylistItemsRecyclerView() = binding.playlistItemsRecyclerView.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        adapter = playlistItemsRecyclerViewAdapter
    }

    private fun setPlaylistItems() {
        val playlists = (activity as MainActivity).playlistViewModel.playlists
        if (playlists.size > args.playlistIndex) {
            playlistItemsRecyclerViewAdapter.addItems(ArrayList(playlists[args.playlistIndex].playlistItems.items))
        }
    }
}