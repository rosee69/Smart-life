package ua.edu.mobile.smartlife.data.remote

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(): String {
    return when (this) {
        is SocketTimeoutException ->
            "Сервер не відповідає. Спробуйте пізніше."

        is UnknownHostException ->
            "Немає з'єднання з інтернетом."

        is HttpException ->
            "Помилка сервера: ${code()}"

        else ->
            "Невідома помилка"
    }
}