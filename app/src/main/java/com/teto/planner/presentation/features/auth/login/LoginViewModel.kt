package com.teto.planner.presentation.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Input())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onLoginInputChange(newValue: String) {
        updateInputState { it.copy(login = newValue, errorMessage = null) }
    }

    fun onPasswordInputChange(newValue: String) {
        updateInputState { it.copy(password = newValue, errorMessage = null) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value as? LoginUiState.Input ?: return

        if (!currentState.canSubmit) return

        viewModelScope.launch {
            updateInputState { it.copy(isSubmitting = true, errorMessage = null) }

            repository.login(currentState.login, currentState.password)
                .fold(
                    onSuccess = {
                        updateInputState { it.copy(isSubmitting = false, isLoginSuccessful = true) }
                    },
                    onFailure = { error ->
                        val msg = error.localizedMessage ?: "Ошибка входа"
                        updateInputState { it.copy(isSubmitting = false, errorMessage = msg) }
                    }
                )
        }
    }

    fun resetState() {
        updateInputState { it.copy(isLoginSuccessful = false, errorMessage = null) }
    }

    private fun updateInputState(transform: (LoginUiState.Input) -> LoginUiState.Input) {
        _uiState.update { currentState ->
            if (currentState is LoginUiState.Input) {
                transform(currentState)
            } else {
                currentState
            }
        }
    }
}