package com.pudasaini.blechat.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable

@Composable
fun AnimatedMessage(message: String, isSent: Boolean){
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(
            initialOffsetX = {if (isSent) it else -it},
            animationSpec = spring()
        ) + fadeIn(animationSpec = spring()),
        exit = fadeOut()
    ) {
        ChatBubble(message, isSent)
    }
}