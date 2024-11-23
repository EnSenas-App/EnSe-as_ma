package com.example.ense_as_ma.forum.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Category(
    @DocumentId
    val categoryId: String = "",
    val name: String = "",
    val description: String = "",
    val iconName: String = "",
    val color: String = "",
    val order: Int = 0,
    val moderators: List<String> = emptyList(),
    val isActive: Boolean = true,
    val parentCategoryId: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val postCount: Int = 0,
    val lastPostAt: Timestamp? = null
)