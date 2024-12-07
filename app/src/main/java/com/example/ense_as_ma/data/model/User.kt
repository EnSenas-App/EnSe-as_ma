package com.example.ense_as_ma.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val profileImage: String = "",
    val bio: String = "",
    val isAdmin: Boolean = false,
    val lastActive: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val preferences: UserPreferences = UserPreferences(),
    val stats: UserStats = UserStats()
)

data class UserPreferences(
    val emailNotifications: Boolean = true,
    val pushNotifications: Boolean = true,
    val language: String = "es",
    val theme: String = "system"
)

data class UserStats(
    val postsCount: Int = 0,
    val commentsCount: Int = 0,
    val likesGiven: Int = 0,
    val likesReceived: Int = 0,
    val lastPostDate: Timestamp? = null
)

enum class UserRole {
    USER,
    MODERATOR,
    ADMIN
}