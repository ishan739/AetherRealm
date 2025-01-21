package com.example.geminiapp.imageAndText

sealed interface ImageTextUiState {
    data object Ideal : ImageTextUiState

    data object Loading: ImageTextUiState

    data class Success(val data: String): ImageTextUiState

    data class Error(val error: String): ImageTextUiState
}