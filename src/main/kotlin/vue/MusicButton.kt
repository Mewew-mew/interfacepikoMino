package vue

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.media.MediaPlayer

class MusicButton(private val listeMusiques : List<MediaPlayer>) : SoundsButton() {

    private val musicOn = ImageView(Image("images/MusicOn.png", 512.0, 512.0, true, false))
    private val musicOff = ImageView(Image("images/MusicOff.png", 512.0, 512.0, true, false))

    init {
        musicOn.fitWidth = 40.0
        musicOn.fitHeight = 40.0
        musicOff.fitWidth = 40.0
        musicOff.fitHeight = 40.0
        graphic = musicOn
    }

    override fun changerEtat() {
        isActive = !isActive
        if (isActive) {
            graphic = musicOn
            for (musique in listeMusiques)
                musique.volume = 0.5
        } else {
            graphic = musicOff
            for (musique in listeMusiques)
                musique.volume = 0.0
        }

    }
}