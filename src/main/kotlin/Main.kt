
import controleur.jeu.ControleurBoutonJoueurSuivant
import controleur.jeu.ControleurBoutonLancer
import controleur.jeu.ControleurBoutonValider
import controleur.menu.ControleurBoutonJouer
import controleur.menu.ControleurBoutonRetour
import controleur.menu.ControleurBoutonRegles
import io.ktor.client.network.sockets.*
import iut.info1.pickomino.data.DICE
import iut.info1.pickomino.data.DICE.*
import iut.info1.pickomino.data.STATUS
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.input.KeyCode.*
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import modele.JeuPickomino
import vue.DiceButton
import vue.VueJeu
import vue.VueMenu
import vue.VueRegles
import java.net.http.HttpTimeoutException

class Main : Application() {
    private var vueMenu = VueMenu()
    private var vueJeu = VueJeu()
    private val vueRegles = VueRegles()
    private var modele = JeuPickomino()

    override fun start(stage: Stage) {
        stage.close()
        val sceneMenu = Scene(StackPane(Rectangle(670.0, 670.0, Color.web("#FAEBD7")), vueMenu))
        sceneMenu.stylesheets.add("stylesheets/styles.css")
        val sceneRegles = Scene(vueRegles)
        vueMenu.boutonJouer.onAction = ControleurBoutonJouer(this, vueMenu, stage)
        vueMenu.boutonRegles.onAction = ControleurBoutonRegles(sceneMenu, sceneRegles, vueRegles, stage)
        vueRegles.boutonRetour.onAction = ControleurBoutonRetour(sceneMenu, stage)

        activerModeDebug() // DEBUG
        stage.icons.add(Image("images/icon.png"))
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

    fun relancerMenu(stage: Stage) {
        vueMenu = VueMenu()
        resetPartie()
        start(stage)
    }

    fun lancerPartie(nbJoueurs : Int, stage: Stage) {
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

    @Deprecated("DEBUG")
    fun activerModeDebug() {
        if (modele.debug) // DEBUG
            vueJeu.setOnKeyPressed {
                if (it.code == NUMPAD7) {
                    modele.choisirDes(listOf(worm, worm, worm, worm, d1, d2, d2, d2))
                    modele.garderDes(worm)
                    modele.choisirDes(listOf(d1, d2, d2, d2))
                    modele.garderDes(d1)
                    modele.prendrePickomino(21)

                    modele.choisirDes(listOf(worm, worm, worm, worm, d2, d1, d1, d1))
                    modele.garderDes(worm)
                    modele.choisirDes(listOf(d2, d1, d1, d1))
                    modele.garderDes(d2)
                    modele.prendrePickomino(22)

                    modele.choisirDes(listOf(worm, worm, worm, worm, d3, d1, d1, d1))
                    modele.garderDes(worm)
                    modele.choisirDes(listOf(d3, d1, d1, d1))
                    modele.garderDes(d3)
                    modele.prendrePickomino(23)

                    modele.choisirDes(listOf(d1, d1, d1, d2, d2, d2, d2, d2))
                    modele.garderDes(d1)
                    modele.choisirDes(listOf(d2, d3, d3, d3, d3))
                    modele.garderDes(d2)
                    modele.choisirDes(listOf(d3, d4, d4, d4))
                    modele.garderDes(d3)
                    modele.choisirDes(listOf(d4, d5, d5))
                    modele.garderDes(d4)
                    modele.choisirDes(listOf(d5, worm))
                    modele.garderDes(d5)

                    vueJeu.updatePickominos(modele.listePickominoAccessible())
                    vueJeu.updateNombrePickomino(modele.donneNombrePickominoJoueurs())
                    vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
                    vueJeu.updateCadreInformation(1)
                }


                if (it.code == NUMPAD0 && modele.donneStatus() != STATUS.KEEP_DICE) {
                    for (i in 0 until vueJeu.listeBoutonPickoAccess.size-1) {
                        modele.choisirDes(listOf(d1, d1, d1, d1, d1, d1, d1, d1))
                        modele.garderDes(d1)
                    }
                    vueJeu.updatePickominos(modele.listePickominoAccessible())
                    vueJeu.updateNombrePickomino(modele.donneNombrePickominoJoueurs())
                    vueJeu.updateStackTops(modele.sommetsPilesPickominoJoueurs())
                }
                if (it.code in listOf(NUMPAD1, NUMPAD2, NUMPAD3, NUMPAD4, NUMPAD5, NUMPAD6)) {
                    if (vueJeu.listeDesLances.size < 8 - vueJeu.listeDesGardes.size) {
                        vueJeu.boutonLancer.isDisable = true
                        vueJeu.listeBoutonPickoAccess.forEach{picko -> picko.isDisable = true; picko.isSelected = false}
                        vueJeu.listeBoutonPickoSommetPile.forEach{picko -> picko.isDisable = true}
                        when (it.code) {
                            NUMPAD1 -> vueJeu.listeDesLances.add(DiceButton(d1, modele.listeDesGardes().contains(d1)))
                            NUMPAD2 -> vueJeu.listeDesLances.add(DiceButton(d2, modele.listeDesGardes().contains(d2)))
                            NUMPAD3 -> vueJeu.listeDesLances.add(DiceButton(d3, modele.listeDesGardes().contains(d3)))
                            NUMPAD4 -> vueJeu.listeDesLances.add(DiceButton(d4, modele.listeDesGardes().contains(d4)))
                            NUMPAD5 -> vueJeu.listeDesLances.add(DiceButton(d5, modele.listeDesGardes().contains(d5)))
                            NUMPAD6 -> vueJeu.listeDesLances.add(DiceButton(worm, modele.listeDesGardes().contains(worm)))
                            else -> {}
                        }
                    }
                    if (vueJeu.listeDesLances.size == 8 - vueJeu.listeDesGardes.size)
                        vueJeu.boutonLancer.isDisable = false
                    vueJeu.updateAffichageDes()
                }
            }
    }
}
fun main() {
    Application.launch(Main::class.java)
}