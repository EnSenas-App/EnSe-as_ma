package com.example.ense_as_ma.ui.screens.forum.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ense_as_ma.data.model.Post
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
    isLiked: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onPostClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header - User Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User info section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // User Avatar
                    if (post.userImage.isNotEmpty()) {
                        AsyncImage(
                            model = post.userImage,
                            contentDescription = "Avatar de ${post.userName}",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Placeholder para cuando no hay imagen
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar por defecto",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                    }

                    // User name and timestamp
                    Column {
                        Text(
                            text = post.userName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = formatTimestamp(post.createdAt),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Menu (opcional)
                IconButton(onClick = { /* Implementar menú */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Post Title
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Post Content
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Attachments preview if any
            if (post.attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                // Implementar preview de attachments
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Like Button
                IconButton(
                    onClick = onLikeClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (isLiked) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite
                            else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isLiked) "No me gusta" else "Me gusta"
                        )
                        if (post.likeCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatCount(post.likeCount),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                // Comment Button
                IconButton(onClick = onCommentClick) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.Comment,
                            contentDescription = "Comentar"
                        )
                        if (post.commentCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatCount(post.commentCount),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                // Share Button
                IconButton(onClick = onShareClick) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir"
                        )
                        if (post.shareCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatCount(post.shareCount),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 1000000 -> String.format("%.1fK", count / 1000.0)
        else -> String.format("%.1fM", count / 1000000.0)
    }
}

private fun formatTimestamp(timestamp: Timestamp): String {
    val now = System.currentTimeMillis()
    val timeInMillis = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    val diffInMillis = now - timeInMillis
    val diffInMinutes = diffInMillis / (60 * 1000)
    val diffInHours = diffInMinutes / 60
    val diffInDays = diffInHours / 24

    return when {
        diffInMinutes < 1 -> "Ahora"
        diffInMinutes < 60 -> "$diffInMinutes min"
        diffInHours < 24 -> "$diffInHours h"
        diffInDays < 7 -> "$diffInDays d"
        else -> {
            val sdf = SimpleDateFormat("dd MMM yy", Locale("es"))
            sdf.format(Date(timeInMillis))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostCardPreview() {
    val samplePost = Post(
        title = "Título de ejemplo",
        content = "Este es un contenido de ejemplo para mostrar cómo se vería un post en la aplicación.",
        userName = "Usuario Ejemplo",
        likeCount = 1234,
        commentCount = 56,
        shareCount = 7,
        createdAt = Timestamp.now()
    )

    PostCard(
        post = samplePost,
        isLiked = true
    )
}