package com.kuroii.ppb_mod2_kel13.model

data class AnimeResponse(
    val data: Anime
)
data class Anime(
    val mal_id: Int,
    val title: String,
    val type: String?,
    val episodes: Int?,
    val score: Double?,
    val rating: String? = null,
    val status: String? = null,
    val aired: Aired? = null,
    val members: Int? = null,
    val images: Images
)
data class Aired(
    val from: String? = null,
    val to: String? = null,
    val string: String? = null
)
data class Images(
    val jpg: Jpg
)
data class Jpg(
    val image_url: String
)