package com.example.ense_as_ma.ui.screens.forum

import com.example.ense_as_ma.data.model.Post

sealed class ForumUiState {
    object Loading : ForumUiState()
    object Unauthorized : ForumUiState()
    data class Success(val posts: List<Post> = emptyList()) : ForumUiState()
    data class Error(val message: String) : ForumUiState()
}
