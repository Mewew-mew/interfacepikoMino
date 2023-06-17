package controleur.jeu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import modele.JeuPickomino
import vue.VueJeu

class ControleurBoutonLancer(private val vueJeu: VueJeu, private val modele: JeuPickomino) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        for (pickomino in vueJeu.listeBoutonPickoAccess) {
            pickomino.isDisable = true
            pickomino.style = "-fx-opacity: 0.5;"
            pickomino.border = null
            pickomino.isSelected = false
        }

        val listeDesLances = modele.lancerDes()

        modele.obtenirEtatJeu()
        vueJeu.updateDesLances(listeDesLances)
        vueJeu.boutonLancer.isDisable = true

        // Cas où tout les dés lancés sont barrés
        if (vueJeu.listeDesLances.all{it.crossed}) {
            vueJeu.cadreBoutons.children.add(vueJeu.boutonJoueurSuivant)
            vueJeu.updatePickominos(modele.listePickominoAccessible())
            vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
        }

        vueJeu.updateNombrePickomino(modele.donneNombrePickominoJoueurs())
    }
}