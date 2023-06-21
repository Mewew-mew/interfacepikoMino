package controleur.menu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import vue.VueRegles

class ControleurBoutonRegles(
    private val sceneMenu: Scene,
    private val sceneRegles: Scene,
    private val vueRegles: VueRegles,
    private val stage: Stage
) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        stage.close()
        vueRegles.resetAffichage()
        stage.minHeight = 739.0
        stage.scene = sceneRegles
        stage.title = "Règles - Pickomino"
        stage.icons[0] = Image("images/rules_icon.png")
        stage.show()
    }
}