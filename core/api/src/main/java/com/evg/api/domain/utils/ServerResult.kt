package com.evg.api.domain.utils

typealias RootError = Error

/**
 * Результат выполнения операции, содержащий данные или ошибку
 *
 * @param D Тип данных при успешном результате
 * @param E Тип ошибки, наследуемый от [RootError]
 */
sealed interface ServerResult<out D, out E: RootError> {
    data class Success<out D, out E: RootError>(val data: D): ServerResult<D, E>
    data class Error<out D, out E: RootError>(val error: E): ServerResult<D, E>
}

/**
 * Преобразование данных внутри [ServerResult.Success], оставляя ошибку без изменений
 *
 * @param D Тип исходных данных
 * @param E Тип ошибки
 * @param R Тип преобразованных данных
 * @param transform Функция преобразования данных
 * @return Новый [ServerResult] с преобразованными данными или исходной ошибкой
 */
inline fun <D, E : RootError, R> ServerResult<D, E>.mapData(transform: (D) -> R): ServerResult<R, E> {
    return when (this) {
        is ServerResult.Success -> ServerResult.Success(transform(this.data))
        is ServerResult.Error -> ServerResult.Error(this.error)
    }
}