package controleur.menu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import vue.VueMenu

class ControleurBoutonPlus(private val vueMenu: VueMenu) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        vueMenu.boutonMoins.isDisable = false
        if (vueMenu.getNbJoueurs() == 3)
            vueMenu.boutonPlus.isDisable = true
        vueMenu.labelNbrJoueur.text = "Joueurs : ${vueMenu.getNbJoueurs()+1}"
    }
}