package com.example.shitzbank.ui.common.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Универсальная Composable-функция для создания списка с ленивой загрузкой (LazyColumn),
 * которая может включать опциональный элемент в начале списка.
 *
 * Этот компонент оборачивает [LazyColumn] внутри [Column], что позволяет
 * легко добавить фиксированный элемент ([topItem]) над прокручиваемым списком.
 *
 * @param T Тип элементов в списке [itemsList].
 * @param modifier [Modifier], применяемый к корневому [Column] для настройки его внешнего вида и поведения.
 * @param topItem Опциональная Composable-функция, которая будет отображена перед [LazyColumn].
 * Может быть `null`, если верхний элемент не нужен.
 * @param itemsList Список данных типа [T], которые будут отображены в [LazyColumn].
 * @param itemTemplate Composable-функция, которая определяет, как должен быть отрисован
 * каждый элемент из [itemsList]. Она принимает один элемент типа [T] в качестве параметра.
 */
@Composable
fun <T> CommonLazyColumn(
    modifier: Modifier = Modifier,
    topItem: (@Composable () -> Unit)? = null,
    itemsList: List<T>,
    itemTemplate: @Composable (T) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        topItem?.let { it() }

        LazyColumn {
            itemsIndexed(itemsList) { index, item ->
                itemTemplate(item)
            }
        }
    }
}
