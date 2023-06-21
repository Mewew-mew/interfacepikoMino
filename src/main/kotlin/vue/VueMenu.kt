package vue

import controleur.menu.ControleurBoutonMoins
import controleur.menu.ControleurBoutonPlus
import javafx.animation.FadeTransition
import javafx.geometry.Insets
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.util.Duration

class VueMenu : BorderPane() {

    private val cadreCentre = VBox()
    val labelNbrJoueur = Label("Joueurs : 2")
    val boutonRegles = RulesButton()
    val boutonMoins = Button("-").also{it.styleClass.addAll("bouton","bouton-moins")}
    val boutonPlus = Button("+").also{it.styleClass.addAll("bouton","bouton-plus")}
    val boutonJouer = Button("Jouer").also{it.styleClass.addAll("bouton","bouton-jouer")}

    private val sonJouer = MediaPlayer(Media(javaClass.getResource("/sounds/effects/start_game.mp3")?.toExternalForm())).also{it.volume = 0.7}
    private val musiqueMenu = MediaPlayer(Media(javaClass.getResource("/sounds/musics/main_menu_theme.mp3")?.toExternalForm()))

    val fadeTransition = FadeTransition(Duration.seconds(1.5), this)

    init {
        background = Background(BackgroundImage(
            Image("images/BackgroundMenu.jpg"),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize(670.0,670.0, false, false, false, false)
        ))

        boutonMoins.onAction = ControleurBoutonMoins(this)
        boutonPlus.onAction = ControleurBoutonPlus(this)

        val labelTitre = Label("Pickomino")
        labelTitre.styleClass.addAll("handrawn","label-titre")

        labelNbrJoueur.effect = DropShadow(10.0, Color.BLACK)
        labelNbrJoueur.styleClass.addAll("itim","texte-nbr-joueurs")
        boutonMoins.prefHeight = 50.0
        boutonMoins.prefWidth = 50.0
        boutonMoins.isDisable = true
        boutonPlus.prefHeight = 50.0
        boutonPlus.prefWidth = 50.0

        val choixJoueurs = HBox(boutonMoins, labelNbrJoueur, boutonPlus)
        choixJoueurs.spacing = 20.0
        choixJoueurs.alignment = Pos.CENTER

        val cadreJouer = VBox(choixJoueurs, boutonJouer)
        cadreJouer.spacing = 15.0
        cadreJouer.alignment = Pos.CENTER

        cadreCentre.alignment = Pos.TOP_CENTER
        cadreCentre.spacing = 130.0
        setMargin(cadreCentre, Insets(25.0, 0.0, 120.0, 0.0))
        cadreCentre.children.addAll(labelTitre, cadreJouer)
        center = cadreCentre

        top = HBox(boutonRegles, VBox(Label("Règles").also{
            it.styleClass.add("handrawn")
            it.style = "-fx-font-size: 40px;"}).also{it.alignment = Pos.CENTER}
        ).also{it.spacing = 15.0; setMargin(it, Insets(10.0, 0.0, 0.0, 10.0))}

        musiqueMenu.volume = 0.5
        musiqueMenu.cycleCount = MediaPlayer.INDEFINITE
        musiqueMenu.play()
    }

    fun getNbJoueurs() : Int {
        return labelNbrJoueur.text.last().toString().toInt()
    }

    fun transitionJouer() {
        musiqueMenu.stop()
        sonJouer.setOnEndOfMedia{sonJouer.stop()}
        sonJouer.play()
        fadeTransition.fromValue = 1.0  // Opacité de départ (100% opaque)
        fadeTransition.toValue = 0.0    // Opacité finale (0% opaque)
        fadeTransition.play()
    }

    fun arreterMusique() {
        musiqueMenu.stop()
    }

    fun jouerMusique() {
        musiqueMenu.play()
    }

    fun desactiverToutLesBoutons() {
        when {
            !boutonMoins.isDisable -> {
                boutonMoins.isDisable = true
                boutonMoins.opacity = 1.0
            }

            !boutonPlus.isDisable -> {
                boutonPlus.isDisable = true
                boutonPlus.opacity = 1.0
            }
        }

        boutonJouer.isDisable = true
        boutonJouer.opacity = 1.0
    }

    fun activerToutLesBoutons() {
        when (getNbJoueurs()) {
            2 -> {
                boutonMoins.isDisable = true
                boutonPlus.isDisable = false
            }
            3 -> {
                boutonMoins.isDisable = false
                boutonPlus.isDisable = false
            }
            4 -> {
                boutonMoins.isDisable = false
                boutonPlus.isDisable = true
            }
        }
        boutonJouer.isDisable = false
    }
}