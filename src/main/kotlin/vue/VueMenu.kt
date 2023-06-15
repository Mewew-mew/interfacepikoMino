package vue

import controleur.menu.ControleurBoutonMoins
import controleur.menu.ControleurBoutonPlus
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font



class VueMenu : VBox() {
    val labelNbrJoueur = Label("Joueurs : 2")
    val boutonMoins = Button("-")
    val boutonPlus = Button("+")
    val boutonJouer = Button("Jouer")

    init {
        background = Background(BackgroundImage(
            Image("BackgroundMenu.jpg"),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize(670.0,670.0, false, false, false, false)
        ))

        val labelTitre = Label("Pickomino")
        val inputStream = javaClass.getResourceAsStream("/fonts/DeliciousHandrawn-Regular.ttf")
        labelTitre.font = Font.loadFont(inputStream, 110.0)
        labelTitre.styleClass.add("label-titre")

        labelNbrJoueur.effect = DropShadow(10.0, Color.BLACK)
        labelNbrJoueur.styleClass.add("texte-nbr-joueurs")
        boutonMoins.styleClass.add("bouton-moins")
        boutonMoins.prefHeight = 50.0
        boutonMoins.prefWidth = 50.0
        boutonMoins.isDisable = true
        boutonPlus.styleClass.add("bouton-plus")
        boutonPlus.prefHeight = 50.0
        boutonPlus.prefWidth = 50.0
        boutonJouer.styleClass.add("bouton-jouer")

        val choixJoueurs = HBox(boutonMoins, labelNbrJoueur, boutonPlus)
        choixJoueurs.spacing = 20.0
        choixJoueurs.alignment = Pos.CENTER

        val cadreJouer = VBox(choixJoueurs, boutonJouer)
        cadreJouer.spacing = 15.0
        cadreJouer.alignment = Pos.CENTER

        alignment = Pos.TOP_CENTER
        setMargin(labelTitre, Insets(115.0, 0.0, 120.0, 0.0))
        children.addAll(labelTitre, cadreJouer)
    }

    fun fixeControleurBoutons() {
        boutonMoins.onAction = ControleurBoutonMoins(this)
        boutonPlus.onAction = ControleurBoutonPlus(this)
    }

    fun getNbJoueurs() : Int {
        return labelNbrJoueur.text.last().toString().toInt()
    }
}