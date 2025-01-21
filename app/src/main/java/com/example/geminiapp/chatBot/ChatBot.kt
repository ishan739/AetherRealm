package com.example.geminiapp.chatBot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.geminiapp.chatBot.components.ChatFooter
import com.example.geminiapp.chatBot.components.ChatHeader
import com.example.geminiapp.chatBot.components.ChatList

@Composable
fun ChatBot(
    viewModel: ChatBotVM = viewModel(),
    navController: NavController
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        //Header
        ChatHeader(navController)


        //Chat List
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ){
            if(viewModel.list.isNotEmpty()) {
                ChatList(
                    list = viewModel.list
                )
            }
            else{
                Text(text = "No Chat Available")
            }

        }


        //Footer
        ChatFooter(submitClick = {
            if (it.isNotEmpty()){
                viewModel.sendMessage(it)
            }
        })

    }
}
