package controleur.jeu

import iut.info1.pickomino.data.DICE
import javafx.event.ActionEvent
import javafx.event.EventHandler
import modele.JeuPickomino
import vue.DiceButton
import vue.VueJeu

class ControleurBoutonValider(private val vueJeu: VueJeu, private val modele: JeuPickomino) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        val desSelectionne = vueJeu.listeDesLances.firstOrNull{it.isSelected}
        val joueurActuel = modele.joueurActuel()
        if (desSelectionne != null) {
            modele.garderDes(desSelectionne.type)

            vueJeu.listeDesGardes.clear()
            for (desLances in modele.listeDesGardes())
                vueJeu.listeDesGardes.add(DiceButton(desLances).also{it.isDisable = true; it.border = null})
            vueJeu.listeDesLances.removeAll{it.type == desSelectionne.type}

            vueJeu.updateAffichageDes()
            vueJeu.boutonValider.isDisable = true
            vueJeu.clearDesLances()

            val sommeDesGardes = vueJeu.sommeDes(vueJeu.listeDesGardes)
            if (vueJeu.listeDesGardes.any{it.type == DICE.worm})
                if (modele.listeJoueurs[joueurActuel].valueStackTop != sommeDesGardes)
                    vueJeu.activerPickomino(sommeDesGardes, joueurActuel)

            if (vueJeu.listeDesGardes.size != 8)
                vueJeu.boutonLancer.isDisable = false
            // Sinon on est dans le cas ou il y a 8 dés gardé et que aucun pickomino n'est séléctionnable
            else if (vueJeu.valuePickominoSelectionne() == 0) {
                vueJeu.retirerUnPickomino(joueurActuel)
                vueJeu.cadreBoutons.children.add(vueJeu.boutonJoueurSuivant)
                vueJeu.updatePickominos(modele.listePickominoAccessible())
                vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
            }

            modele.obtenirEtatJeu() // Debug
        } else { // Sinon un pickomino est séléctionné
            val valuePickominoSelectionne = vueJeu.valuePickominoSelectionne()

            // On regarde si le pickomino a été pris sur le stack top d'un joueur
            val joueurPris = modele.listeJoueurs.indexOfFirst{it.valueStackTop == valuePickominoSelectionne}
            if (joueurPris != -1) // Si c'est le cas alors on retire un pickomino à son compteur au joueur concerné.
                vueJeu.retirerUnPickomino(joueurPris)

            modele.prendrePickomino(joueurActuel, valuePickominoSelectionne)
            vueJeu.ajouterUnPickomino(joueurActuel)
            vueJeu.listeBoutonPickoAccess.removeIf{it.value == valuePickominoSelectionne}
            vueJeu.updatePickominos(vueJeu.listeBoutonPickoAccess.map{it.value})
            vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
            vueJeu.boutonValider.isDisable = true
            vueJeu.boutonLancer.isDisable = false
            vueJeu.clearDesGardes()
        }
    }
}