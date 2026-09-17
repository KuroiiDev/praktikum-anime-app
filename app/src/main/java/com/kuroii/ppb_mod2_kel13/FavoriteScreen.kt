package com.kuroii.ppb_mod2_kel13

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    viewModel: AnimeViewModel,
    onAnimeClick: (Int) -> Unit = {}
) {
    val favoriteList by viewModel.favoriteAnimeList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime Favorit") }
            )
        }
    ) { innerPadding ->
        if (favoriteList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada anime favorit",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp)
            ) {
                items(favoriteList, key = { it.mal_id }) { anime ->
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

                            // Ikon bintang (selalu filled karena ini halaman favorit)
                            IconButton(
                                onClick = { viewModel.toggleFavorite(anime.mal_id) },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Hapus dari favorit",
                                    tint = Color(0xFFFFD700)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

