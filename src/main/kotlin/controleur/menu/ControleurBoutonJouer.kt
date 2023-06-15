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
        appli.vueJeu = VueJeu(nbJoueurs)

        appli.vueJeu!!.fixeControleurDes() // Debug

        val spacingBinding = Bindings.createDoubleBinding(
            {(stage.width-201*nbJoueurs) / (nbJoueurs-1)},
            stage.widthProperty()
        )
        appli.vueJeu!!.cadreJoueurs.spacingProperty().bind(spacingBinding)

        stage.scene = Scene(appli.vueJeu)
        appli.modele = JeuPickomino(nbJoueurs)
        stage.isResizable = true

        appli.update() // Debug

        appli.vueJeu!!.fixeControleurBoutonLancer(appli)

        stage.close()
        stage.show()
    }
}