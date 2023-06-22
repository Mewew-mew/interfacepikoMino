package controleur.menu

import Main
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class ControleurBoutonRetour(
    private val appli: Main,
    private val sceneMenu: Scene,
    private val stage: Stage
) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        stage.close()
        stage.icons[0] = Image("images/icon.png")
        appli.lancerMenu(sceneMenu, stage)
    }
}