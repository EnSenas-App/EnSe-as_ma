package com.example.ense_as_ma.forum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ense_as_ma.forum.data.ForumRepository
import com.example.ense_as_ma.forum.model.Post
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ForumUiState {
    object Loading : ForumUiState()
    object Unauthorized : ForumUiState()
    data class Success(val posts: List<Post> = emptyList()) : ForumUiState()
    data class Error(val message: String) : ForumUiState()
}

class ForumViewModel(
    private val repository: ForumRepository = ForumRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForumUiState>(ForumUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = ForumUiState.Loading
            try {
                repository.getPosts().collect { posts ->
                    _uiState.value = ForumUiState.Success(posts)
                }
            } catch (e: Exception) {
                _uiState.value = ForumUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun createPost(post: Post) {
        viewModelScope.launch {
            try {
                _uiState.value = ForumUiState.Loading
                repository.createPost(post).fold(
                    onSuccess = {
                        loadPosts() // Recargar posts después de crear uno nuevo
                    },
                    onFailure = { e ->
                        _uiState.value = ForumUiState.Error(e.message ?: "Error al crear el post")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = ForumUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun increaseLikeCount(postId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = ForumUiState.Loading
                // Actualizar el post en Firestore
                repository.incrementLikeCount(postId).fold(
                    onSuccess = {
                       loadPosts() // Recargar posts
                    },
                    onFailure = { e ->
                        _uiState.value = ForumUiState.Error(e.message ?: "Error desconocido")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = ForumUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}