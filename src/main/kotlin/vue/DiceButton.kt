package vue

import iut.info1.pickomino.data.DICE
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView


class DiceButton(val type: DICE, var crossed: Boolean = false) : Button() {

    var isSelected = false

    init {
        val imageView = ImageView(Image(if (crossed) "images/dices/CrossedDice_${type.name}.png" else "images/dices/Dice_${type.name}.png"))
        imageView.fitHeight = 75.0
        imageView.fitWidth = 75.0

        graphic = imageView

        if (crossed) {
            isDisable = true
            style = "-fx-opacity: 1.0;"
        }

        padding = Insets(5.0)
        background = null
        border = null
    }
}