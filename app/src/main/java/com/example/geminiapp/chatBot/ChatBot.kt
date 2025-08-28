package com.example.geminiapp.chatBot

import com.example.geminiapp.R
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.geminiapp.chatBot.components.ChatFooter
import com.example.geminiapp.chatBot.components.ChatHeader
import com.example.geminiapp.chatBot.components.ChatList
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ChatBot(
    viewModel: ChatBotVM = viewModel(),
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg3),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                viewModel.setContext(context = context)
                viewModel.fetchMessages()
            }

            //Header
            ChatHeader(navController)

            //Chat List
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(viewModel.list.size) {
                if (viewModel.list.isNotEmpty()) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(viewModel.list.size - 1)
                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (viewModel.chatState.value) {
                    is ChatState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is ChatState.Error -> {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }

                    is ChatState.Success -> {}
                }

                if (viewModel.list.isNotEmpty()) {
                    ChatList(
                        list = viewModel.list,
                        listState = listState
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.itt),
                        contentDescription = "image"
                    )
                }
            }

            //Footer
            ChatFooter(submitClick = {
                if (it.isNotEmpty()) {
                    viewModel.sendMessage(it)
                }
            })
        }
    }
}
