package controleur.jeu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import modele.JeuPickomino
import vue.VueJeu

class ControleurBoutonJoueurSuivant(private val vueJeu: VueJeu, private val modele: JeuPickomino) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        vueJeu.cadreBoutons.children.removeLast() // Enlève le bouton joueur suivant
        vueJeu.boutonLancer.isDisable = false
        vueJeu.clearDesLances()
        vueJeu.clearDesGardes()
        modele.obtenirEtatJeu()
    }
}