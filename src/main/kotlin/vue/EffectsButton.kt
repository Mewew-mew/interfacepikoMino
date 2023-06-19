package vue

import javafx.scene.image.Image
import javafx.scene.image.ImageView

class EffectsButton : SoundsButton() {

    private val effectsOn = ImageView(Image("images/EffectsOn.png", 512.0, 512.0, true, false))
    private val effectsOff = ImageView(Image("images/EffectsOff.png", 512.0, 512.0, true, false))

    init {
        effectsOn.fitWidth = 40.0
        effectsOn.fitHeight = 40.0
        effectsOff.fitWidth = 40.0
        effectsOff.fitHeight = 40.0
        graphic = effectsOn
    }

    override fun changerEtat() {
        isActive = !isActive
        graphic = if (isActive) effectsOn else effectsOff
    }
}