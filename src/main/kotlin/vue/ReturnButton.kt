package vue

import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ReturnButton : Button() {
    init {
        val imageView = ImageView(Image("images/left_arrow.png", 128.0, 128.0, true, false))
        imageView.fitWidth = 64.0
        imageView.fitHeight = 64.0

        graphic = imageView
        border = null
        background = null

        setOnMouseEntered {
            graphic.scaleX = 1.1
            graphic.scaleY = 1.1
        }

        setOnMouseExited {
            graphic.scaleX = 1.0
            graphic.scaleY = 1.0
        }
    }
}