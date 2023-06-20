package controleur.jeu

import Main
import iut.info1.pickomino.data.DICE
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage
import modele.JeuPickomino
import vue.DiceButton
import vue.VueJeu

class ControleurBoutonValider(
    private val appli: Main,
    private val stage: Stage,
    private val vueJeu: VueJeu,
    private val modele: JeuPickomino
) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        val desSelectionne = vueJeu.listeDesLances.firstOrNull{it.isSelected} // On prend le déséléctionné
        val joueurActuel = modele.joueurActuel()
        if (desSelectionne != null) {
            /*
            On prend la liste des gardes actuel et on ajoute ceux qu'on garde.
            Si le joueur perd en gardant un dés alors on n'aura plus accès à la liste des gardés
            car elle sera reset.
            */
            val listeGarde = modele.listeDesGardes() + modele.listeDesLances().filter{it == desSelectionne.type}

            modele.garderDes(desSelectionne.type) // On garde le dé

            // On update nbr pickomino car garderDes met à jour les nombres
            vueJeu.updateNombrePickomino(modele.donneNombrePickominoJoueurs())

            // Actualisation des listes des dés lances et gardes
            vueJeu.listeDesGardes.clear()
            for (desGardes in listeGarde)
                vueJeu.listeDesGardes.add(DiceButton(desGardes).also{it.isDisable = true; it.border = null})

            vueJeu.updateAffichageDes()
            vueJeu.boutonValider.isDisable = true
            vueJeu.clearDesLances()

            val ilYaUnVers = vueJeu.listeDesGardes.any{it.type == DICE.worm}
            val sommeDesGardes = modele.sommeDes(vueJeu.listeDesGardes.map{it.type})

            vueJeu.updatePickominos(modele.listePickominoAccessible())
            vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
            vueJeu.updateNombrePickomino(modele.donneNombrePickominoJoueurs())

            // Cas ou il y a 8 dés gardés et qu'aucun Pickomino n'a été activé ou alors qu'il n'y a pas de vers
            if (vueJeu.listeDesGardes.size == 8) {
                vueJeu.boutonLancer.isDisable = true // On désactive à coup sur le boutonLancer
                if (!ilYaUnVers || !vueJeu.activerPickomino(sommeDesGardes, joueurActuel)) {
                    // Si il ne reste plus de Pickomino
                    if (vueJeu.listeBoutonPickoAccess.isEmpty())
                        vueJeu.declencherFinPartie(appli, stage, modele.obtenirScoreFinal(), modele.donnePickoMaxJoueurs())
                    else {
                        vueJeu.cadreBoutons.children.add(vueJeu.boutonJoueurSuivant)
                        vueJeu.labelInformation.text = "C'est perdu... Vous pouvez passer au joueur suivant !"
                    }
                } else
                    vueJeu.labelInformation.text = "Vous pouvez prendre un Pickomino !"
            } else {
                vueJeu.boutonLancer.isDisable = false
                if (ilYaUnVers && vueJeu.activerPickomino(sommeDesGardes, joueurActuel))
                    vueJeu.labelInformation.text = "Vous pouvez lancer les dés ou prendre un Pickomino !"
                else
                    vueJeu.labelInformation.text = "Vous pouvez lancer les dés !"
            }

        } else { // Sinon un pickomino est séléctionné
            vueJeu.labelInformation.text = "Vous pouvez lancer les dés !"
            val valuePickominoSelectionne = vueJeu.valuePickominoSelectionne()
            modele.prendrePickomino(valuePickominoSelectionne)
            vueJeu.listeBoutonPickoAccess.removeIf{it.value == valuePickominoSelectionne}
            vueJeu.updatePickominos(modele.listePickominoAccessible())
            vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
            vueJeu.updateNombrePickomino(modele.donneNombrePickominoJoueurs())
            vueJeu.boutonValider.isDisable = true
            vueJeu.boutonLancer.isDisable = false
            vueJeu.clearDesGardes()
            vueJeu.updateCadreInformation(modele.joueurActuel())
            // Si il ne reste plus de Pickomino
            if (vueJeu.listeBoutonPickoAccess.isEmpty())
                vueJeu.declencherFinPartie(appli, stage, modele.obtenirScoreFinal(), modele.donnePickoMaxJoueurs())
        }
    }
}