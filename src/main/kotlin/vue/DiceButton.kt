package vue

import controleur.jeu.ControleurDes
import iut.info1.pickomino.data.DICE
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView


class DiceButton(
    url: String,
    val type: DICE,
    crossed: Boolean
) : Button() {

    var isSelected = false

    init {
        val imageView = ImageView(Image(if (crossed) "Dices/Crossed$url" else "Dices/$url"))
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