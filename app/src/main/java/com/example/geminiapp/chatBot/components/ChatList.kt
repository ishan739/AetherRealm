package com.example.geminiapp.chatBot.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geminiapp.chatBot.ChatData

@Composable
fun ChatList(
    list: MutableList<ChatData>,
    listState: LazyListState = rememberLazyListState()
) {
//    val

    LaunchedEffect(list.size) {
        if (list.isNotEmpty()) {
            listState.animateScrollToItem(list.lastIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(list.size) { index ->
            if (list[index].role == "user") {
                UserChatMessage(message = list[index].message)
            } else {
                ModelChatMessage(message = list[index].message)
            }
        }
    }
}


@Composable
fun UserChatMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = message,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ModelChatMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = message,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
