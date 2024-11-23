// Path: app/src/main/java/com/example/ense_as_ma/ui/components/forum/PostCard.kt
package com.example.ense_as_ma.ui.components.forum

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ense_as_ma.forum.model.Post
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado del post
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Título
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contenido
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fecha y métricas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Fecha formateada
                Text(
                    text = formatDate(post.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                // Métricas con iconos
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PostMetric(
                        icon = "👁️",
                        count = post.viewCount
                    )
                    PostMetric(
                        icon = "❤️",
                        count = post.likeCount
                    )
                    PostMetric(
                        icon = "💬",
                        count = post.commentCount
                    )
                }
            }
        }
    }
}

@Composable
private fun PostMetric(
    icon: String,
    count: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = formatCount(count),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

private fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 1000000 -> String.format("%.1fK", count / 1000.0)
        else -> String.format("%.1fM", count / 1000000.0)
    }
}

private fun formatDate(date: Any): String {
    val timestamp = when (date) {
        is Long -> date
        is Timestamp -> date.seconds * 1000 // Convertir segundos a milisegundos
        else -> return "Fecha desconocida"
    }

    val now = System.currentTimeMillis()
    val diffInMillis = now - timestamp
    val diffInMinutes = diffInMillis / (60 * 1000)
    val diffInHours = diffInMinutes / 60
    val diffInDays = diffInHours / 24

    return when {
        diffInMinutes < 1 -> "Ahora"
        diffInMinutes < 60 -> "$diffInMinutes min"
        diffInHours < 24 -> "$diffInHours h"
        diffInDays < 7 -> "$diffInDays d"
        else -> {
            val sdf = SimpleDateFormat("dd MMM", Locale("es"))
            sdf.format(Date(timestamp))
        }
    }
}
