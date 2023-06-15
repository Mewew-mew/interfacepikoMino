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
    crossed: Boolean = false
) : Button("", ImageView(Image(if (crossed) "Crossed$url" else url, 75.0, 75.5, true, false))) {

    var isSelected = false

    init {
        padding = Insets(5.0)
        background = null
        border = null
    }
}