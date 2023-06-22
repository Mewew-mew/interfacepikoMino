package controleur.jeu

import Main
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage
import modele.JeuPickomino
import vue.VueJeu

class ControleurBoutonLancer(
    private val appli: Main,
    private val stage: Stage,
    private val vueJeu: VueJeu,
    private val modele: JeuPickomino
) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        vueJeu.jouerSonDes()
        for (pickomino in vueJeu.listeBoutonPickoAccess + vueJeu.listeBoutonPickoSommetPile) {
            pickomino.isDisable = true
            pickomino.style = "-fx-opacity: 0.5;"
            pickomino.border = null
            pickomino.isSelected = false
        }

        val listeDesLances = modele.lancerDes()

        vueJeu.updateDesLances(listeDesLances)
        vueJeu.boutonLancer.isDisable = true

        // Cas où tout les dés lancés sont barrés
        if (vueJeu.listeDesLances.all{it.crossed}) {
            vueJeu.updatePickominos(modele.listePickominoAccessible())
            vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
            // Si il ne reste plus de Pickomino
            if (vueJeu.listeBoutonPickoAccess.isEmpty())
                vueJeu.declencherFinPartie(appli, stage, modele.obtenirScoreFinal(), modele.donnePickoMaxJoueurs())
            else {
                vueJeu.labelInformation.text = "C'est perdu... Vous pouvez passer au joueur suivant !"
                vueJeu.boutonValider.isDisable = true
                vueJeu.cadreBoutons.children.add(vueJeu.boutonJoueurSuivant)
            }
        } else vueJeu.labelInformation.text = "Vous pouvez choisir un type de dé à garder !"
        vueJeu.updateNombrePickomino(modele.donneNombrePickominoJoueurs())
    }
}