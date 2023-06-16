package controleur.menu

import javafx.beans.binding.Bindings
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.stage.Stage
import modele.JeuPickomino
import vue.VueJeu
import vue.VueMenu

class ControleurBoutonJouer(
    private val vueJeu: VueJeu,
    private val vueMenu: VueMenu,
    private val modele: JeuPickomino,
    private val stage: Stage
) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        stage.width = 1600.0
        stage.height = 900.0
        stage.minWidth = 914.0
        stage.minHeight = 734.0
        val nbJoueurs = vueMenu.getNbJoueurs()

        modele.init(nbJoueurs)
        vueJeu.init(nbJoueurs)
        vueJeu.fixeControleurBoutons(modele)

        val spacingBinding = Bindings.createDoubleBinding(
            {(stage.width-201*nbJoueurs) / (nbJoueurs-1)},
            stage.widthProperty()
        )

        vueJeu.cadreJoueurs.spacingProperty().bind(spacingBinding)
        stage.isResizable = true

        val sceneJeu = Scene(vueJeu)
        sceneJeu.stylesheets.add("styles.css")
        stage.scene = sceneJeu

        vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
        vueJeu.updatePickominos(modele.listePickominoAccessible())

        stage.close()
        stage.show()
    }
}