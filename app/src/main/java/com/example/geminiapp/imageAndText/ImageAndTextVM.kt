package com.example.geminiapp.imageAndText

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.geminiapp.Constants


class ImageAndTextVM : ViewModel( ){

    var imageUiState = MutableStateFlow<ImageTextUiState>(ImageTextUiState.Ideal)
        private set

    val imageList by lazy { mutableStateListOf<Bitmap>() }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constants.API_KEY
    )

    @SuppressLint("SuspiciousIndentation")
    fun generateContentData(text: String) = viewModelScope.launch{
        if (imageList.isEmpty()) return@launch

        imageUiState.update { ImageTextUiState.Loading }

        val contentData = content {
            imageList.forEach { bitmap ->
                image(bitmap)
            }
            text(text)
        }

            generativeModel.generateContent(contentData).text?. let {data ->
                imageUiState.update { ImageTextUiState.Success(data) }
            } ?: run {
                imageUiState.update { ImageTextUiState.Error("Error") }
            }


    }
}