package com.example.playlists.data

// the PlaylistObject contains the list of PlaylistObjectItem (the playlists items)
data class PlaylistObject(val pageInfo: PageInfo, val items: ArrayList<PlaylistObjectItem>)

data class PageInfo(val totalResults: Int, val resultsPerPage: Int)

// PlaylistObjectItem contains the playlist snippet (details like a title, description, etc.)
// and PlaylistObjectItem which contains the playlist videos
data class PlaylistObjectItem(val playlistItems: PlaylistItemsItem, val snippet: Snippet)

data class PlaylistItemsItem(val items: List<Item>)

// playlist item/video
data class Item(val snippet: Snippet, val contentDetails: ContentDetails)

data class Snippet(val publishedAt: String, val title: String, val description: String, val thumbnails: Thumbnails, val resourceId: ResourceId)

data class ContentDetails(val videoId: String, val videoPublishedAt: String)

data class Thumbnails(val default: Thumbnail, val medium: Thumbnail, val high: Thumbnail, val standard: Thumbnail, val maxres: Thumbnail)

data class Thumbnail(val url: String, val width: Int, val height: Int)

data class ResourceId(val kind: String, val videoId: String)
