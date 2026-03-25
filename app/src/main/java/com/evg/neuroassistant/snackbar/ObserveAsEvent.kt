package com.evg.neuroassistant.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Подписка на [Flow] как событие с учетом жизненного цикла
 *
 * @param T Тип данных потока
 * @param flow Поток данных для наблюдения
 * @param key1 Ключ для управления перезапуском эффекта
 * @param key2 Второй ключ для управления перезапуском эффекта
 * @param onEvent Колбэк, вызываемый при каждом новом элементе потока
 */
@Composable
fun <T> ObserveAsEvent(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}