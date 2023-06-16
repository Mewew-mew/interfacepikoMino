package controleur.jeu

import Main
import iut.info1.pickomino.data.DICE
import javafx.event.ActionEvent
import javafx.event.EventHandler
import vue.DiceButton

class ControleurBoutonValider(private val appli : Main) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        val desSelectionne = appli.vueJeu!!.listeDesLances.first{it.isSelected}.type
        appli.modele!!.garderDes(desSelectionne)

        appli.vueJeu!!.listeDesLances.removeAll { diceButton ->
            if (diceButton.type == desSelectionne) {
                appli.vueJeu!!.listeDesGardes.add(diceButton.also{it.isDisable = true; it.border = null})
                true
            } else {
                false
            }
        }

        appli.vueJeu!!.updateAffichageDes()
        appli.vueJeu!!.boutonValider.isDisable = true
        appli.vueJeu!!.clearDesLances()
        if (appli.vueJeu!!.listeDesGardes.size != 8)
            appli.vueJeu!!.boutonLancer.isDisable = false
        appli.modele!!.obtenirEtatJeu()
    }
}