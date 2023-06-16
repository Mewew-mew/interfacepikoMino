import controleur.menu.ControleurBoutonJouer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import modele.JeuPickomino
import vue.VueJeu
import vue.VueMenu

class Main : Application() {
    private val vueMenu = VueMenu()
    private var vueJeu = VueJeu()
    private var modele = JeuPickomino()
    init {
        vueMenu.fixeControleurBoutons()
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