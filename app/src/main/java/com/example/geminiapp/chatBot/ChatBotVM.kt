package com.example.geminiapp.chatBot

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminiapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
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
            modelName = "gemini-pro",
            apiKey = BuildConfig.apiKey
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
            val modelMessage = ChatData(role = ChatRoleEnum.MODEL.role, message = it)
            list.add(modelMessage)
            saveMessageToFirestore(modelMessage)
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
