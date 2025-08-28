package com.example.geminiapp.chatBot

import com.google.ai.client.generativeai.GenerativeModel


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminiapp.Constants
import com.google.ai.client.generativeai.type.content
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class ChatState(){
    object Loading : ChatState()
    object Error : ChatState()
    object Success : ChatState()

}


class ChatBotVM : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    val list by lazy { mutableStateListOf<ChatData>()}

   private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = Constants.API_KEY
        )
    }

    private val _chatState = MutableStateFlow<ChatState>(ChatState.Loading)
    val chatState: StateFlow<ChatState> = _chatState

    fun setContext(context: Context) {
        this.context = context
    }
    @SuppressLint("StaticFieldLeak")
    private  var context: Context? = null


    fun sendMessage(message: String) = viewModelScope.launch {

        val userMessage = ChatData(role = ChatRoleEnum.USER.role, message = message)
        list.add(userMessage)
        saveMessageToFirestore(userMessage)

        try {
            val chat = generativeModel.startChat(
                history = listOf(
                    content(role = ChatRoleEnum.USER.role) { text("My name is Ishan, I am a Software Developer") },
                    content(role = ChatRoleEnum.MODEL.role) { text("Great. Need any Help with android development?") }
                )
            )

            val responseText = chat.sendMessage(
                content(role = ChatRoleEnum.USER.role) { text(message) }
            ).text

            responseText?.let {
                val modelMessage = ChatData(role = ChatRoleEnum.MODEL.role, message = it)
                list.add(modelMessage)
                saveMessageToFirestore(modelMessage)
            }

        } catch (e: Exception) {
            Log.e("GeminiError", "API call failed", e)
            _chatState.value = ChatState.Error
        }

    }
    private fun saveMessageToFirestore(chatData: ChatData) {

        context?.let {
            db.collection("chats")
                .add(chatData)
                .addOnSuccessListener {
                    Log.d("Firestore", "Message saved successfully ")
                }
                .addOnFailureListener {
                    Log.e("Error", "Error saving message ", it)
                }
        } ?: Log.e("Error", "Context is null")
    }
    fun fetchMessages() {

        viewModelScope.launch {
            _chatState.value = ChatState.Loading

            try {
                context?.let {
                    db.collection("chats")
                        .get()
                        .addOnSuccessListener { result ->
                            list.clear()

                            for (document in result) {
                                val chat = document.toObject(ChatData::class.java)
                                list.add(chat)
                            }
                            _chatState.value = ChatState.Success
                        }
                        .addOnFailureListener {
                            Log.d("Firestore", "Error fetching messages", it)
                            _chatState.value = ChatState.Error
                        }
                }

            } catch (
                e: Exception
            ) {
                Log.d("Firestore", "Error fetching messages", e)
                _chatState.value = ChatState.Error
            }
        }
    }
}
