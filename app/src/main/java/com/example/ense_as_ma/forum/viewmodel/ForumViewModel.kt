package com.example.ense_as_ma.forum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ense_as_ma.forum.data.ForumRepository
import com.example.ense_as_ma.forum.model.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ForumUiState {
    object Loading : ForumUiState()
    object Unauthorized : ForumUiState()
    data class Success(
        val posts: List<Post> = emptyList(),
        val categories: List<Category> = emptyList()
    ) : ForumUiState()
    data class Error(val message: String) : ForumUiState()
}

class ForumViewModel(
    private val repository: ForumRepository = ForumRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    private val _uiState = MutableStateFlow<ForumUiState>(ForumUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        checkAuthAndLoadData()
    }

    private fun checkAuthAndLoadData() {
        if (auth.currentUser == null) {
            _uiState.value = ForumUiState.Unauthorized
            return
        }
        loadForumData()
    }

    private fun loadForumData() {
        viewModelScope.launch {
            try {
                combine(
                    repository.getPosts(),
                    repository.getCategories()
                ) { posts, categories ->
                    ForumUiState.Success(posts, categories)
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = ForumUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun createPost(post: Post) {
        if (auth.currentUser == null) {
            _uiState.value = ForumUiState.Unauthorized
            return
        }

        viewModelScope.launch {
            try {
                val postWithUser = post.copy(userId = auth.currentUser?.uid ?: "")
                repository.createPost(postWithUser)
                loadForumData()
            } catch (e: Exception) {
                _uiState.value = ForumUiState.Error(e.message ?: "Error al crear el post")
            }
        }
    }
}