package controleur.menu

import Main
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage
import vue.VueMenu

class ControleurBoutonJouer(
    private val appli: Main,
    private val vueMenu: VueMenu,
    private val stage: Stage
) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        vueMenu.desactiverToutLesBoutons()
        vueMenu.fadeTransition.setOnFinished{appli.lancerPartie(vueMenu.getNbJoueurs(), stage)}
        vueMenu.transitionJouer()
    }
}