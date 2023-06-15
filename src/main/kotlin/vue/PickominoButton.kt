package vue

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class PickominoButton(value : Int) : Button("", ImageView(Image("Pickominos/Pickomino_$value.png", 76.0, 147.5, true, false))) {
    init {
        padding = Insets(0.0)
        border = null
    }
}