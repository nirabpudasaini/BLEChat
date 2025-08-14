package com.pudasaini.blechat.presentation.component

import android.os.Message
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(message: String, isSent: Boolean) {
    Row (
        horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ){
        Surface (
            shape = MaterialTheme.shapes.medium,
            color = if (isSent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 4.dp
        ){
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}