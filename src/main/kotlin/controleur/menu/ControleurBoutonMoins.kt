package controleur.menu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import vue.VueMenu

class ControleurBoutonMoins(private val vueMenu: VueMenu) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        vueMenu.boutonPlus.isDisable = false
        if (vueMenu.getNbJoueurs() == 3)
            vueMenu.boutonMoins.isDisable = true
        vueMenu.labelNbrJoueur.text = "Joueurs : ${vueMenu.getNbJoueurs()-1}"
    }
}