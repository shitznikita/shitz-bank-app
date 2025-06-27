package com.example.shitzbank.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Интерфейс, определяющий различные типы [CoroutineDispatcher] для управления потоками выполнения корутин.
 * Предоставляет диспетчеры для операций ввода-вывода, фоновых вычислений и взаимодействия с основным потоком UI.
 */
interface CoroutineDispatchers {
    /**
     * [CoroutineDispatcher] оптимизированный для выполнения блокирующих операций ввода-вывода,
     * таких как работа с сетью или диском.
     */
    val io: CoroutineDispatcher

    /**
     * [CoroutineDispatcher] оптимизированный для выполнения интенсивных CPU-операций,
     * таких как сортировка больших списков или парсинг JSON.
     */
    val default: CoroutineDispatcher

    /**
     * [CoroutineDispatcher], который выполняет корутины в основном потоке Android UI.
     * Используется для обновления пользовательского интерфейса.
     */
    val main: CoroutineDispatcher

    /**
     * [CoroutineDispatcher], который выполняет корутины немедленно, если текущий поток является основным.
     * Если не в основном потоке, переключается на него.
     */
    val mainImmediate: CoroutineDispatcher

    /**
     * Реализация [CoroutineDispatchers] по умолчанию, использующая стандартные диспетчеры из [Dispatchers].
     * Эта реализация предназначена для использования в реальных приложениях.
     */
    class DefaultCoroutineDispatchers
    @Inject
    constructor() : CoroutineDispatchers {
        override val io: CoroutineDispatcher = Dispatchers.IO
        override val default: CoroutineDispatcher = Dispatchers.Default
        override val main: CoroutineDispatcher = Dispatchers.Main
        override val mainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate
    }
}
