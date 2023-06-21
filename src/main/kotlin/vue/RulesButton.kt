package vue

import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class RulesButton : Button() {
    init {
        val imageView = ImageView(Image("images/rules_icon.png", 160.0, 160.0, true, false))
        imageView.fitWidth = 70.0
        imageView.fitHeight = 70.0

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