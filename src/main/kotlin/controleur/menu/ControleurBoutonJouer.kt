package controleur.menu

import io.ktor.client.network.sockets.*
import javafx.beans.binding.Bindings
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Alert
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
        vueMenu.desactiverToutLesBoutons()
        val nbJoueurs = vueMenu.getNbJoueurs()
        vueMenu.fadeTransition.setOnFinished{lancerJeu(nbJoueurs)}
        vueMenu.transitionJouer()
    }

    private fun lancerJeu(nbJoueurs : Int) {
        try {
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
            sceneJeu.stylesheets.add("stylesheets/styles.css")
            stage.scene = sceneJeu

            vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
            vueJeu.updatePickominos(modele.listePickominoAccessible())

            stage.close()
            stage.width = 1600.0
            stage.height = 900.0
            stage.minWidth = 920.0
            stage.minHeight = 940.0
            stage.show()
        } catch (e: ConnectTimeoutException) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.headerText = ""
            alert.contentText = "Echec de connexion au serveur, vérifier la connexion."
            alert.title = "Erreur de connexion"
            vueMenu.opacity = 1.0
            vueMenu.activerToutLesBoutons()
            alert.show()
        }
    }
}