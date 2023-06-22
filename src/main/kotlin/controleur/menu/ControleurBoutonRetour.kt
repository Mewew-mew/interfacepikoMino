package controleur.menu

import Main
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import vue.VueMenu
import vue.VueRegles

class ControleurBoutonRetour(
    private val appli: Main,
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
        appli.lancerMenu(sceneMenu, stage)
    }
}