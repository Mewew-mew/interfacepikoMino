package vue

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane

class SoundButton : Button() {

    var isActive = true
    private val soundOn = ImageView(Image("images/SoundOn.png", 512.0, 512.0, true, false))
    private val soundOff = ImageView(Image("images/SoundOff.png", 512.0, 512.0, true, false))

    init {
        soundOn.fitWidth = 40.0
        soundOn.fitHeight = 40.0
        soundOff.fitWidth = 40.0
        soundOff.fitHeight = 40.0
        graphic = soundOn
        background = null
        border = null
        BorderPane.setMargin(this, Insets(15.0, 0.0, 0.0, 15.0))
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

    private fun changerEtat() {
        isActive = !isActive
        graphic = if (isActive) soundOn else soundOff
    }
}