import javafx.application.Platform
import kotlin.concurrent.thread

fun <T> async(action: () -> T, ui: ((T) -> Unit)? = null) {
    thread {
        val result = action()
        ui?.let { publish -> Platform.runLater { publish(result) } }
    }
}
