package com.kuroii.ppb_mod2_kel13

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun AnimeListScreen(
    viewModel: AnimeViewModel = viewModel(),
    onAnimeClick: (Int) -> Unit = {}
) {
    val animeList by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTopAnime()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 96.dp)
        ) {
            items(animeList, key = { it.mal_id }) { anime ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 4.dp)
                        .clickable { onAnimeClick(anime.mal_id) },
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Box {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(anime.images.jpg.image_url),
                                contentDescription = anime.title,
                                modifier = Modifier
                                    .width(85.dp)
                                    .height(125.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = anime.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Type: ${anime.type ?: "-"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Episodes: ${anime.episodes ?: 0}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Score: ${anime.score ?: "N/A"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Rating: ${anime.rating ?: "-"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Status: ${anime.status ?: "-"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                val airedFrom = anime.aired?.from?.take(10) ?: "-"
                                val airedTo = anime.aired?.to?.take(10) ?: "-"
                                Text(
                                    text = "Aired: $airedFrom - $airedTo",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Members: ${anime.members ?: "-"}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        val isFav = anime.mal_id in favoriteIds
                        IconButton(
                            onClick = { viewModel.toggleFavorite(anime.mal_id) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                contentDescription = if (isFav) "Hapus dari favorit" else "Tambah ke favorit",
                                tint = if (isFav) Color(0xFFFFD700) else Color.Gray
                            )
                        }
                    }
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (animeList.isEmpty()) {
            Text(
                text = "Tidak ada anime ditemukan",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Floating Search Bar di bagian bawah
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.onSearchQueryChanged(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari anime...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.onSearchQueryChanged("")
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeListScreenPreview() {
    AnimeListScreen()
}