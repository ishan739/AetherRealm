package com.example.geminiapp.imageAndText

import android.graphics.ImageDecoder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.geminiapp.imageAndText.components.ImageFooter
import com.example.geminiapp.imageAndText.components.ImageHeader
import com.example.geminiapp.imageAndText.components.ImageTextBody

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImageAndText(
    viewModel: ImageAndTextVM = viewModel(),
    navController: NavController
)
{
    val context = LocalContext.current
    val pickImage = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
        it?.let {
            val bitmap = ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    it
                )
            )
            viewModel.imageList.add(bitmap)
        }
    }
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
        )  {
        //Header
        ImageHeader(navController)

        //Body

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ){
            ImageTextBody(viewModel = viewModel)
        }

        //Footer
       ImageFooter(
           viewModel = viewModel,
           pickImageClick = {
               pickImage.launch(
                   PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
               )
           }
           , submitClick = {
               viewModel.generateContentData(it)
           }
       )

    }
}