import controleur.menu.ControleurBoutonJouer
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import modele.JeuPickomino
import vue.VueJeu
import vue.VueMenu

class Main : Application() {
    val vueMenu = VueMenu()
    var vueJeu : VueJeu? = null
    var modele : JeuPickomino? = null
    init {
        vueMenu.fixeControleurBoutons()

    }
    override fun start(stage: Stage) {
        val sceneMenu = Scene(vueMenu)
        sceneMenu.stylesheets.add("styles.css")


        vueMenu.boutonJouer.onAction = ControleurBoutonJouer(this, stage)

        stage.width = 670.0
        stage.height = 670.0
        stage.isResizable = false
        stage.scene = sceneMenu
        stage.title = "Pickomino"
        stage.show()

        // Debug
        /*
        stage.width = 1600.0
        stage.height = 900.0
        stage.minWidth = 820.0
        stage.minHeight = 600.0
        stage.scene = Scene(VueJeu(4))
        stage.isResizable = true
        stage.title = "Pickomino"
        stage.show()
        */
    }

    fun getNbJoueurs(): Int {
        return vueMenu.getNbJoueurs()
    }

    fun update(){
        val liste_adresses_StackTops= Array(getNbJoueurs()){""}
        for (i in 0 until getNbJoueurs()){
            liste_adresses_StackTops[i]="Pickominos/Pickomino_${modele!!.listeJoueurs[i].valueStackTop}.png"
        }
        vueJeu!!.updateStackTops(liste_adresses_StackTops)
        val listePickominoAccessibles=modele!!.listePickominoAccessible()
        val liste_adresses_Pickkominos= Array(listePickominoAccessibles.size){""}
        for (i in listePickominoAccessibles.indices){
            liste_adresses_Pickkominos[i]="Pickominos/Pickomino_${listePickominoAccessibles[i]}.png"
        }
        vueJeu!!.updatePickominos(liste_adresses_Pickkominos)
    }
}
fun main() {
    Application.launch(Main::class.java)
}