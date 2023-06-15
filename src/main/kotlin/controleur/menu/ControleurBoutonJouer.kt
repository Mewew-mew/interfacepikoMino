package controleur.menu

import Main
import javafx.beans.binding.Bindings
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.stage.Stage
import modele.JeuPickomino
import vue.VueJeu
import vue.VueMenu

class ControleurBoutonJouer(private val appli: Main, private val stage: Stage) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        stage.width = 1600.0
        stage.height = 900.0
        stage.minWidth = 914.0
        stage.minHeight = 734.0
        val nbJoueurs = appli.getNbJoueurs()
        appli.setVueJeu(VueJeu(nbJoueurs))

        val spacingBinding = Bindings.createDoubleBinding(
            {(stage.width-201*nbJoueurs) / (nbJoueurs-1)},
            stage.widthProperty()
        )
        appli.getVueJeu()!!.cadreJoueurs.spacingProperty().bind(spacingBinding)

        stage.scene = Scene(appli.getVueJeu())
        appli.nouvellePartie(nbJoueurs)
        stage.isResizable = true
        stage.close()
        stage.show()
    }
}