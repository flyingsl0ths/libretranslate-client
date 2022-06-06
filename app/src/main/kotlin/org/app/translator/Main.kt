package org.app.translator

import javafx.stage.Stage
import org.app.translator.view.MainScreen
import tornadofx.App
import tornadofx.launch

class TranslatorApp : App(MainScreen::class) {
        companion object {
                const val WINDOW_WIDTH: Double = 900.0
                const val WINDOW_HEIGHT: Double = 900.0
        }

        override fun start(stage: Stage) {
                val initStage: Stage.() -> Unit = {
                        minWidth = WINDOW_WIDTH
                        minHeight = WINDOW_HEIGHT
                        super.start(this)
                }

                with(stage, initStage)
        }
}

fun main(args: Array<String>) {
        launch<TranslatorApp>(args)
}
