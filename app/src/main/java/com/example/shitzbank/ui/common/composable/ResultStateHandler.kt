package com.example.shitzbank.ui.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shitzbank.common.ResultState

/**
 * Composable-функция-обработчик, которая отображает различные состояния [ResultState]
 * (загрузка, успешное выполнение, ошибка) в пользовательском интерфейсе.
 *
 * Эта функция автоматически реагирует на изменения состояния и отображает соответствующий UI:
 * - [CircularProgressIndicator] во время загрузки.
 * - Пользовательский контент, определенный в [onSuccess], при успешном получении данных.
 * - Сообщение об ошибке при возникновении [ResultState.Error].
 *
 * @param T Тип данных, которые ожидаются в [ResultState.Success].
 * @param state Текущее состояние [ResultState], которое нужно обработать.
 * @param onSuccess Composable-функция, которая будет вызвана и отображена,
 * если состояние [state] является [ResultState.Success]. Она принимает полученные данные в качестве параметра.
 * @param modifier [Modifier], применяемый к корневому контейнеру (Box) для настройки его внешнего вида и поведения.
 */
@Composable
fun <T> ResultStateHandler(
    state: ResultState<T>,
    onSuccess: @Composable (data: T) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is ResultState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is ResultState.Success -> {
            onSuccess(state.data)
        }
        is ResultState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CommonText(
                    text = state.message ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
