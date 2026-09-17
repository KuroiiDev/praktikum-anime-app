package com.kuroii.ppb_mod2_kel13

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    animeId: Int,
    viewModel: AnimeViewModel,
    onBackClick: () -> Unit
) {
    val anime = viewModel.getAnimeById(animeId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(anime?.title ?: "Detail Anime") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (anime == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Data anime tidak ditemukan")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gambar Anime
                Image(
                    painter = rememberAsyncImagePainter(anime.images.jpg.image_url),
                    contentDescription = anime.title,
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(260.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Judul Anime
                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Informasi Tambahan dalam Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(3.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DetailItemRow(label = "Tipe", value = anime.type ?: "-")
                        DetailItemRow(label = "Episode", value = "${anime.episodes ?: 0}")
                        DetailItemRow(label = "Skor", value = "${anime.score ?: "N/A"}")
                        DetailItemRow(label = "Rating", value = anime.rating ?: "-")
                        DetailItemRow(label = "Status", value = anime.status ?: "-")
                        val airedFrom = anime.aired?.from?.take(10) ?: "-"
                        val airedTo = anime.aired?.to?.take(10) ?: "-"
                        DetailItemRow(label = "Ditayangkan", value = "$airedFrom - $airedTo")
                        DetailItemRow(label = "Members", value = "${anime.members ?: "-"}")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

