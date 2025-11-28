package com.example.evaluacion2_petsonline.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.evaluacion2_petsonline.data.repository.AuthRepository
import com.example.evaluacion2_petsonline.data.local.SessionManager
import com.example.evaluacion2_petsonline.utils.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val email: String? = null,
    val avatarUri: String? = null,
    val error: String? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)
    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            val result = repository.getProfile()
            val email = result.getOrNull()

            val avatarUri = sessionManager.getAvatarUri()

            _uiState.value = ProfileUiState(
                isLoading = false,
                email = email,
                avatarUri = avatarUri
            )
        }
    }

    fun saveAvatar(uri: String) {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val localPath = ImageUtils.saveImageToInternalStorage(
                context,
                Uri.parse(uri)
            )

            localPath?.let {
                sessionManager.saveAvatarUri(it)
                _uiState.value = _uiState.value.copy(avatarUri = it)
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            sessionManager.clearAvatar()
            sessionManager.clearToken()
        }
    }
}
