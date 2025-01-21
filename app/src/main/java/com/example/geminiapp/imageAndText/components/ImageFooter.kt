package com.example.geminiapp.imageAndText.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geminiapp.R
import com.example.geminiapp.imageAndText.ImageAndTextVM


@Composable
fun ImageFooter(
    viewModel: ImageAndTextVM = viewModel(),
    pickImageClick: () -> Unit = {},
    submitClick: (text: String) -> Unit = {}
    ) {
    val placeholderPrompt = ""
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.Gray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = prompt,
                label = { Text(stringResource(R.string.label_prompt)) },
                onValueChange = { prompt = it },
                leadingIcon = {
                    IconButton(onClick = {
                        pickImageClick()
                    }) {
                        Icon(Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 8.dp)
                                .clip(CircleShape)
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray),
                placeholder = { Text(text = "Enter your prompt here") }
            )

            IconButton(onClick = {
                submitClick(prompt)
                prompt = ""
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 8.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.size(5.dp))

        val data = viewModel.imageList

        AnimatedVisibility(visible = data.isNotEmpty()) {
            LazyRow (
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ){
                items(viewModel.imageList){image ->
                    Image(
                        bitmap = image.asImageBitmap(), // Convert Bitmap to ImageBitmap
                        contentDescription = "Horizontal Image",
                        modifier = Modifier
                            .size(50.dp).padding(5.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    )
                }
            }
        }

    }

}