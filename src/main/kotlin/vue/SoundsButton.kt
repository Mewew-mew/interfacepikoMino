package vue

import javafx.scene.control.Button

abstract class SoundsButton : Button() {

    var isActive = true

    init {
        background = null
        border = null
        setOnAction{changerEtat()}

        setOnMouseEntered {
            graphic.scaleX = 1.1
            graphic.scaleY = 1.1
        }

        setOnMouseExited {
            graphic.scaleX = 1.0
            graphic.scaleY = 1.0
        }
    }

    protected abstract fun changerEtat()
}