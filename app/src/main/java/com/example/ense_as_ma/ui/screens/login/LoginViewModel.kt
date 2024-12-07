package com.example.ense_as_ma.ui.screens.login
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ense_as_ma.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            repository.signIn(email, password)
                .fold(
                    onSuccess = {
                        _uiState.value = LoginUiState.Success
                    },
                    onFailure = { exception ->
                        _uiState.value = LoginUiState.Error(exception.message ?: "Error desconocido")
                    }
                )
        }
    }

    fun signUp(email: String, password: String, user:String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            repository.signUp(email, password, user)
                .fold(
                    onSuccess = {
                        _uiState.value = LoginUiState.Success
                    },
                    onFailure = { exception ->
                        _uiState.value =
                            LoginUiState.Error(exception.message ?: "Error en el registro")
                    }
                )
        }
    }

}

