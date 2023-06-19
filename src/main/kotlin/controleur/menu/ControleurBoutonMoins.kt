package controleur.menu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import vue.VueMenu

class ControleurBoutonMoins(private val vueMenu: VueMenu) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        vueMenu.boutonPlus.isDisable = false
        vueMenu.boutonPlus.opacity = 1.0
        if (vueMenu.getNbJoueurs() == 3) {
            vueMenu.boutonMoins.isDisable = true
            vueMenu.boutonMoins.opacity = 0.4
        }
        vueMenu.labelNbrJoueur.text = "Joueurs : ${vueMenu.getNbJoueurs()-1}"
    }
}