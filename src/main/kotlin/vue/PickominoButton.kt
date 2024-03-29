package vue

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class PickominoButton(val value : Int, isOnStackTop : Boolean = false) : Button() {

    var isSelected = false
    init {
        val imageView = ImageView(Image("images/pickominos/Pickomino_$value.png", 263.0, 518.0, true, false))
        if (isOnStackTop) {
            imageView.fitWidth = 40.0
            imageView.fitHeight = 77.75
        } else {
            imageView.fitHeight = 147.5
            imageView.fitWidth = 76.0
        }

        graphic = imageView

        isDisable = true
        style = "-fx-opacity: 0.5;"
        padding = Insets(0.0)
        border = null
        background = null
    }
}