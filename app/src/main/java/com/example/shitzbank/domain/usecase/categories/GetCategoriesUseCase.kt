package com.example.shitzbank.domain.usecase.categories

import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.repository.CategoryRepository
import javax.inject.Inject

/**
 * Сценарий использования (Use Case) для получения списка всех доступных категорий.
 * Инкапсулирует бизнес-логику, необходимую для выполнения этой операции,
 * взаимодействуя с [CategoryRepository] для получения данных.
 *
 * @param categoryRepository Репозиторий для доступа к данным категорий. Внедряется через Hilt/Dagger.
 */
class GetCategoriesUseCase
    @Inject
    constructor(
        private val categoryRepository: CategoryRepository,
    ) {
        /**
         * Выполняет операцию получения списка категорий.
         *
         * @return Список объектов [Category], представляющих все доступные категории.
         * Может вернуть пустой список, если категории отсутствуют.
         * @throws Exception Может выбросить исключение, если операция получения категорий завершится неудачей
         * (например, из-за сетевой ошибки или проблем на сервере).
         */
        suspend fun execute(): List<Category> {
            return categoryRepository.getCategories()
        }
    }
