package com.kuroii.ppb_mod2_kel13

sealed class Screen(val route: String, val title: String) {
    object Anime : Screen("anime", "Anime")
    object About : Screen("about", "About")
    object Detail : Screen("detail/{animeId}", "Detail") {
        fun createRoute(animeId: Int) = "detail/$animeId"
    }
}