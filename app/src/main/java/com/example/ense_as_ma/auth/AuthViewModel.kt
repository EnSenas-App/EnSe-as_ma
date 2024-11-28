package com.example.ense_as_ma.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// AuthViewModel.kt
class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.signIn(email, password)
                .fold(
                    onSuccess = {
                        _uiState.value = AuthUiState.Success
                    },
                    onFailure = { exception ->
                        _uiState.value = AuthUiState.Error(exception.message ?: "Error desconocido")
                    }
                )
        }
    }

    fun signUp(email: String, password: String, user:String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.signUp(email, password, user)
                .fold(
                    onSuccess = {
                        _uiState.value = AuthUiState.Success
                    },
                    onFailure = { exception ->
                        _uiState.value = AuthUiState.Error(exception.message ?: "Error en el registro")
                    }
                )
        }
    }

}

