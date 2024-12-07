package com.example.ense_as_ma.ui.screens.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ense_as_ma.data.repository.ForumRepository
import com.example.ense_as_ma.data.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val forumRepository: ForumRepository
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
                forumRepository.getPosts().collect { posts ->
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
                forumRepository.createPost(post).fold(
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
                forumRepository.incrementLikeCount(postId).fold(
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