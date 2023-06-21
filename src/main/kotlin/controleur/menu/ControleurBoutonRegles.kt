package controleur.menu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import vue.VueRegles

class ControleurBoutonRegles(private val stage: Stage) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        stage.close()
        val sceneRegle = Scene(VueRegles())
        stage.minHeight = 700.0
        stage.minWidth = 700.0
        stage.height = 700.0
        stage.width = 700.0
        stage.scene = sceneRegle
        stage.title = "Règles - Pickomino"
        stage.icons[0] = Image("images/rules_icon.png")
        stage.show()
    }
}