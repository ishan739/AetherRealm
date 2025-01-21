package com.example.geminiapp.imageAndText.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.geminiapp.imageAndText.ImageAndTextVM
import com.example.geminiapp.imageAndText.ImageTextUiState

@Composable
fun  ImageTextBody(
    viewModel: ImageAndTextVM
) {
    val state by viewModel.imageUiState.collectAsStateWithLifecycle()

    when(state){

        is ImageTextUiState.Loading ->
            LoadingImageState(modifier = Modifier)

        is ImageTextUiState.Success ->
            SuccessImageBody(modifier = Modifier, text = (state as? ImageTextUiState.Success)?.data.orEmpty())

        is ImageTextUiState.Error ->
            ErrorImageBody(modifier = Modifier, error = (state as? ImageTextUiState.Error)?.error.orEmpty())

        else -> Unit
    }
}


@Composable
private fun LoadingImageState(modifier: Modifier){
    CircularProgressIndicator()
}


@Composable
private fun SuccessImageBody(modifier: Modifier, text: String){
    Text(
        text = text,
        modifier = modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    )
}

@Composable
private fun ErrorImageBody(modifier: Modifier, error: String){
    Text(error)
}


@Preview
@Composable
fun ImageTextBodyPreview(){
    SuccessImageBody(modifier = Modifier, "Body")
}