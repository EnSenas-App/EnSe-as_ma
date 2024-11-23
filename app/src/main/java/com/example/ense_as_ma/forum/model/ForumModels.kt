// Path: app/src/main/java/com/example/ense_as_ma/forum/model/ForumModels.kt
package com.example.ense_as_ma.forum.model

import com.google.firebase.Timestamp

data class Post(
    val postId: String = "",
    val userId: String = "",
    val categoryId: String = "",
    val title: String = "",
    val content: String = "",
    val attachments: List<String> = emptyList(),
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
)

data class Category(
    val categoryId: String = "",
    val name: String = "",
    val description: String = "",
    val moderators: List<String> = emptyList(),
    val createdAt: Timestamp = Timestamp.now()
)

data class Comment(
    val commentId: String = "",
    val postId: String = "",
    val userId: String = "",
    val content: String = "",
    val attachments: List<String> = emptyList(),
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
)