package com.example.playlists.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.playlists.R
import com.example.playlists.databinding.FragmentVideoPlayerBinding
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment

// VideoPlayerFragment plays the YouTube video in YouTubePlayerFragment (from YouTube library YouTubeAndroidPlayerApi)

// Note: the library YouTubeAndroidPlayerApi doesn't support androidx.Fragment (YouTubePlayerFragment isn't androidx.Fragment,
// but rather android.app.Fragment which is deprecated, YouTubePlayerSupportFragment is android.support.v4.app.Fragment, also not androidx.Fragment),
// so I cannot add YouTubePlayerFragment to childFragmentManager and it is added to the activity old deprecated fragmentManager

class VideoPlayerFragment : Fragment() {
    companion object {
        private const val apiKey = "AIzaSyCgFnTcpREA9hxtJOHI2j4Qsrs9YW-izn0"
    }
    private val TAG = VideoPlayerFragment::class.java.name

    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!     // this property is only valid between onCreateView and onDestroyView
    private val args: VideoPlayerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = String.format(getString(R.string.videoPlayerFragmentLabel), args.videoName)

        // youtube library (YouTubeAndroidPlayerApi) doesn't support androidx.Fragment (YouTubePlayerFragment isn't androidx.Fragment),
        // so I cannot add YouTubePlayerFragment to childFragmentManager and it is added to the activity old deprecated fragmentManager,
        @Suppress("DEPRECATION")
        val youtubePlayerFragment = activity?.fragmentManager?.findFragmentById(R.id.youtubePlayerFragment) as? YouTubePlayerFragment
        youtubePlayerFragment?.initialize(apiKey, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                // when the initialization succeeded, playing the video in YouTubePlayerFragment
                youTubePlayer.loadVideo(args.videoId)
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
                Log.w(TAG, "YouTubePlayerFragment initialization failed, ${youTubeInitializationResult.name}")
            }
        })
    }

    @Suppress("DEPRECATION")
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // youtube library (YouTubeAndroidPlayerApi) doesn't support androidx.Fragment (YouTubePlayerFragment isn't androidx.Fragment),
        // so I cannot add YouTubePlayerFragment to childFragmentManager and it is added to the activity old deprecated fragmentManager,
        // when VideoPlayerFragment view destroyed need to remove it from fragmentManager, for prevent crash
        val youtubePlayerFragment = activity?.fragmentManager?.findFragmentById(R.id.youtubePlayerFragment)
        if (youtubePlayerFragment != null) {
            activity?.fragmentManager?.beginTransaction()?.remove(youtubePlayerFragment)?.commit()
        }
    }
}