package controleur.jeu

import Main
import iut.info1.pickomino.data.DICE
import javafx.event.ActionEvent
import javafx.event.EventHandler
import vue.DiceButton

class ControleurBoutonLancer(private val appli : Main) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        for (pickomino in appli.vueJeu!!.listeBoutonPickoAccess) {
            pickomino.isDisable = true
            pickomino.style = "-fx-opacity: 0.5;"
        }
        val listeDesLances = appli.modele!!.lancerDes()
        appli.modele!!.obtenirEtatJeu()
        appli.vueJeu!!.updateDesLances(listeDesLances)
        appli.vueJeu!!.boutonLancer.isDisable = true
    }
}