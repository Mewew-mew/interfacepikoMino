package controleur.menu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import vue.VueMenu
import vue.VueRegles

class ControleurBoutonRetour(
    private val vueMenu: VueMenu,
    private val sceneMenu: Scene,
    private val vueRegles: VueRegles,
    private val stage: Stage
) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        vueRegles.arreterMusique()
        vueMenu.jouerMusique()

        stage.close()
        stage.icons[0] = Image("images/icon.png")
        stage.isMaximized = false
        stage.minWidth = 670.0
        stage.minHeight = 670.0
        stage.width = 670.0
        stage.height = 670.0
        stage.isResizable = false
        stage.scene = sceneMenu
        stage.title = "Menu - Pickomino"
        stage.show()
    }
}