package com.example.ense_as_ma.ui.screens.forum

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ense_as_ma.ui.screens.forum.components.NewPostDialog
import com.example.ense_as_ma.ui.screens.forum.components.PostCard

@Composable
fun ForumScreen(
    forumViewModel: ForumViewModel = hiltViewModel()
) {
    val uiState by forumViewModel.uiState.collectAsState()
    var showNewPostDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            floatingActionButton = {
                if (uiState !is ForumUiState.Unauthorized) {
                    FloatingActionButton(
                        onClick = { showNewPostDialog = true }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo post")
                    }
                }
            }
        ) { innerPadding ->
            when (val state = uiState) {
                is ForumUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ForumUiState.Unauthorized -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Por favor, inicia sesión para acceder al foro",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is ForumUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.posts) { post ->
                            PostCard(post = post,
                                onLikeClick = {
                                    // Llamamos al método en el ViewModel para incrementar el likeCount
                                    forumViewModel.increaseLikeCount(post.postId)
                                },
                                isLiked = post.likeCount > 0)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                is ForumUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }

    // Diálogo como overlay
    if (showNewPostDialog) {
        NewPostDialog(
            onDismiss = { showNewPostDialog = false },
            onPostCreated = { post ->
                forumViewModel.createPost(post)
                showNewPostDialog = false
            }
        )
    }
}