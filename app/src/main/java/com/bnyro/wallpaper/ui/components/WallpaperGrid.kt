package com.bnyro.wallpaper.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bnyro.wallpaper.db.obj.Wallpaper

@Composable
fun WallpaperGrid(
    wallpapers: List<Wallpaper>,
    onScrollEnd: () -> Unit = {}
) {
    val listState = rememberLazyGridState()
    val scrollEnded by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    val shape = RoundedCornerShape(10.dp)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState
    ) {
        items(wallpapers) {
            var showFullscreen by remember {
                mutableStateOf(false)
            }

            ElevatedCard(
                modifier = Modifier
                    .height(300.dp)
                    .padding(5.dp)
                    .clip(shape)
                    .clickable {
                        showFullscreen = true
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(10.dp)
                ) {
                    AsyncImage(
                        model = it.thumb,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape)
                    )
                }
            }

            if (showFullscreen) {
                WallpaperPreview(it) {
                    showFullscreen = false
                }
            }
        }
    }

    if (scrollEnded) {
        LaunchedEffect(Unit) {
            onScrollEnd.invoke()
        }
    }
}
