
import controleur.menu.ControleurBoutonJouer
import iut.info1.pickomino.data.DICE
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.input.KeyCode.*
import javafx.stage.Stage
import modele.JeuPickomino
import vue.DiceButton
import vue.VueJeu
import vue.VueMenu

class Main : Application() {
    private val vueMenu = VueMenu()
    private var vueJeu = VueJeu()
    private var modele = JeuPickomino()
    init {
        vueMenu.fixeControleurBoutons()
        if (modele.debug)
        vueJeu.setOnKeyPressed {
            if (it.code in listOf(NUMPAD1, NUMPAD2, NUMPAD3, NUMPAD4, NUMPAD5, NUMPAD6)) {
                if (vueJeu.listeDesLances.size < 8 - vueJeu.listeDesGardes.size) {
                    vueJeu.boutonLancer.isDisable = true
                    vueJeu.listeBoutonPickoAccess.forEach{picko -> picko.isDisable = true; picko.isSelected = false}
                    vueJeu.listeBoutonPickoSommetPile.forEach{picko -> picko.isDisable = true}
                    when (it.code) {
                        NUMPAD1 -> vueJeu.listeDesLances.add(DiceButton(DICE.d1, modele.listeDesGardes().contains(DICE.d1)))
                        NUMPAD2 -> vueJeu.listeDesLances.add(DiceButton(DICE.d2, modele.listeDesGardes().contains(DICE.d2)))
                        NUMPAD3 -> vueJeu.listeDesLances.add(DiceButton(DICE.d3, modele.listeDesGardes().contains(DICE.d3)))
                        NUMPAD4 -> vueJeu.listeDesLances.add(DiceButton(DICE.d4, modele.listeDesGardes().contains(DICE.d4)))
                        NUMPAD5 -> vueJeu.listeDesLances.add(DiceButton(DICE.d5, modele.listeDesGardes().contains(DICE.d5)))
                        NUMPAD6 -> vueJeu.listeDesLances.add(DiceButton(DICE.worm, modele.listeDesGardes().contains(DICE.worm)))
                        else -> {}
                    }
                }
                if (vueJeu.listeDesLances.size == 8 - vueJeu.listeDesGardes.size)
                    vueJeu.boutonLancer.isDisable = false
                vueJeu.updateAffichageDes()
            }
        }
    }
    override fun start(stage: Stage) {
        val sceneMenu = Scene(vueMenu)
        sceneMenu.stylesheets.add("styles.css")

        vueMenu.boutonJouer.onAction = ControleurBoutonJouer(vueJeu, vueMenu, modele, stage)

        stage.icons.add(Image("icon.png"))
        stage.width = 670.0
        stage.height = 670.0
        stage.isResizable = false
        stage.scene = sceneMenu
        stage.title = "Pickomino"
        stage.show()
    }
}
fun main() {
    Application.launch(Main::class.java)
}