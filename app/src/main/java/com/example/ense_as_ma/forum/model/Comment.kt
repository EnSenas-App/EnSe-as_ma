package com.example.ense_as_ma.forum.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Comment(
    @DocumentId
    val commentId: String = "",
    val postId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userImage: String = "",
    val content: String = "",
    val attachments: List<Attachment> = emptyList(),
    @ServerTimestamp
    val createdAt: Timestamp = Timestamp.now(),
    @ServerTimestamp
    val updatedAt: Timestamp = Timestamp.now(),
    val editHistory: List<EditRecord> = emptyList(),
    val likeCount: Int = 0,
    val replyTo: String? = null,
    val reports: List<Report> = emptyList(),
    val status: CommentStatus = CommentStatus.ACTIVE
)

enum class CommentStatus {
    ACTIVE,
    DELETED,
    HIDDEN,
    UNDER_REVIEW
}