package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.image_placeholder
import org.jetbrains.compose.resources.painterResource

/**
 * Image of an article, with a placeholder while it loads or when it fails
 */
@Composable
fun ArticleImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    val placeholder = painterResource(Res.drawable.image_placeholder)

    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(MaterialTheme.shapes.medium)
    )
}
