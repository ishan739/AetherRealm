package com.example.geminiapp.chatBot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminiapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatBotVM : ViewModel() {

    val list by lazy { mutableStateListOf<ChatData>()}

   private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.apiKey
        )
    }

    fun sendMessage(message: String) = viewModelScope.launch {

        list.add(ChatData(role = ChatRoleEnum.USER.role, message = message))

        val chat =  generativeModel.startChat(
            history = listOf(
                content (role = ChatRoleEnum.USER.role)
                {
                    // Add the history you want to the bot to remember , like for example if this
                    // bot is used in any other app , then things related to that app etc...
                    text("My name is Ishan , I am a Software Developer")
                },
                content (role = ChatRoleEnum.MODEL.role){
                    // Add the things that you want the bot to focus on..
                    text("Great. Need any Help with android development?")
                }

            )
        )
        chat.sendMessage(
            content (role = ChatRoleEnum.USER.role)
            {
                text(message)
            }
        ).text?.let {
            list.add(ChatData(role = ChatRoleEnum.MODEL.role, message = it))
        }
    }
}