package com.kuroii.ppb_mod2_kel13.model

data class CharacterListResponse(
    val data: List<Character>
)

data class Character(
    val mal_id: Int,
    val url: String? = null,
    val name: String,
    val name_kanji: String? = null,
    val nicknames: List<String>? = null,
    val favorites: Int? = null,
    val about: String? = null,
    val images: CharacterImages
)

data class CharacterImages(
    val jpg: CharacterJpg
)

data class CharacterJpg(
    val image_url: String,
    val small_image_url: String? = null
)

