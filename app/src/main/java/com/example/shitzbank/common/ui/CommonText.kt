package com.example.shitzbank.common.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CommonText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    style: TextStyle,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.MiddleEllipsis,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        color = color,
        style = style,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        modifier = modifier
    )
}