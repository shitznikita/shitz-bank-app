package com.example.shitzbank.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Composable-функция, которая отображает круглую иконку с односимвольной меткой внутри.
 *
 * Этот компонент часто используется как ведущая иконка в элементах списка или аналогичных элементах UI,
 * где требуется небольшой цветной кружок с меткой.
 *
 * @param modifier [Modifier], применяемый к корневому [Box] для настройки его макета и стиля.
 * @param label Односимвольная [String] для отображения внутри круглой иконки (например, эмодзи или инициал).
 * @param backgroundColor [Color] фона круглой иконки. По умолчанию [Color.White].
 */
@Composable
fun LeadIcon(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color = Color.White,
) {
    Box(
        modifier =
            modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        CommonText(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
