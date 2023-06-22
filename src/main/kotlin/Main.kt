
import controleur.jeu.ControleurBoutonJoueurSuivant
import controleur.jeu.ControleurBoutonLancer
import controleur.jeu.ControleurBoutonValider
import controleur.menu.ControleurBoutonJouer
import controleur.menu.ControleurBoutonRegles
import controleur.menu.ControleurBoutonRetour
import io.ktor.client.network.sockets.*
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import modele.JeuPickomino
import vue.VueJeu
import vue.VueMenu
import vue.VueRegles
import java.net.NoRouteToHostException
import java.net.http.HttpTimeoutException

class Main : Application() {
    private var vueMenu = VueMenu()
    private var vueJeu = VueJeu()
    private var vueRegles = VueRegles()
    private var modele = JeuPickomino()

    override fun start(stage: Stage) {
        stage.close()
        val sceneMenu = Scene(StackPane(Rectangle(670.0, 670.0, Color.web("#FAEBD7")), vueMenu))
        sceneMenu.stylesheets.add("stylesheets/styles.css")
        val sceneRegles = Scene(vueRegles)
        vueMenu.boutonJouer.onAction = ControleurBoutonJouer(this, vueMenu, stage)
        vueMenu.boutonRegles.onAction = ControleurBoutonRegles(sceneRegles, vueRegles, stage)
        vueRegles.boutonRetour.onAction = ControleurBoutonRetour(this, sceneMenu, stage)

        stage.icons.add(Image("images/icon.png"))
        lancerMenu(sceneMenu, stage)
    }

    fun relancerMenu(stage: Stage) {
        vueMenu = VueMenu()
        vueRegles = VueRegles()
        resetPartie()
        start(stage)
    }

    fun lancerMenu(sceneMenu : Scene, stage: Stage) {
        stage.isMaximized = false
        stage.minWidth = 670.0
        stage.minHeight = 670.0
        stage.width = 670.0
        stage.height = 670.0
        stage.isResizable = false
        stage.scene = sceneMenu
        stage.title = "Menu - Pickomino"
        stage.show()
    }

    fun lancerJeu(nbJoueurs : Int, stage: Stage) {
        try {
            modele.init(nbJoueurs)
            vueJeu.init(nbJoueurs)
            vueJeu.boutonLancer.onAction = ControleurBoutonLancer(this, stage, vueJeu, modele)
            vueJeu.boutonValider.onAction = ControleurBoutonValider(this, stage, vueJeu, modele)
            vueJeu.boutonJoueurSuivant.onAction = ControleurBoutonJoueurSuivant(vueJeu, modele)

            val spacingBinding = Bindings.createDoubleBinding(
                {(stage.width-231*nbJoueurs) / (nbJoueurs-1)},
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
            stage.minWidth = 960.0
            stage.minHeight = 940.0
            stage.width = 1600.0
            stage.height = 940.0
            stage.title = "Jeu - Pickomino"
            stage.show()
        } catch (e: HttpTimeoutException) {
            afficherAlert()
        } catch (e: ConnectTimeoutException) {
            afficherAlert()
        } catch (e: NoRouteToHostException) {
            afficherAlert()
        }
    }

    private fun afficherAlert() {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.headerText = ""
        alert.contentText = "Echec de connexion au serveur, vérifier la connexion."
        alert.title = "Erreur de connexion"
        vueMenu.opacity = 1.0
        vueMenu.activerToutLesBoutons()
        alert.show()
    }

    fun resetPartie() {
        vueJeu = VueJeu()
        modele = JeuPickomino()
    }
}
fun main() {
    Application.launch(Main::class.java)
}