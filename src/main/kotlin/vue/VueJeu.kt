package vue

import Main
import controleur.jeu.*
import iut.info1.pickomino.data.DICE
import javafx.geometry.Insets
import javafx.geometry.Orientation.HORIZONTAL
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.stage.Stage

class VueJeu : BorderPane() {

    private val listeMusiques = mutableListOf(
        //createMediaPlayer("/sounds/musics/theme5.mp3").also{it.volume = 0.6}
        createMediaPlayer("/sounds/musics/theme1.mp3"),
        createMediaPlayer("/sounds/musics/theme2.mp3"),
        createMediaPlayer("/sounds/musics/theme3.mp3"),
        createMediaPlayer("/sounds/musics/theme4.mp3")
    ).shuffled()

    private val sonDes = createMediaPlayer("/sounds/effects/dice_rolling.mp3").also{it.volume = 0.5; it.setOnEndOfMedia{it.stop()}}
    private val sonSelectionne = createMediaPlayer("/sounds/effects/selected.mp3").also{it.volume = 0.5; it.setOnEndOfMedia{it.stop()}}
    private val sonDeselectionne = createMediaPlayer("/sounds/effects/unselected.mp3").also{it.volume = 0.5; it.setOnEndOfMedia{it.stop()}}
    private val sonPickoPris = createMediaPlayer("/sounds/effects/take_pickomino.mp3").also{it.volume = 0.8; it.setOnEndOfMedia{it.stop()}}

    private val boutonEffets = EffectsButton()
    private val boutonMusique = MusicButton(listeMusiques)

    private val cadreTourJoueur = HBox(
        Label("C'est au  tour du joueur : ").also{it.style = "-fx-font-size: 55px;"; it.styleClass.add("handrawn")},
        Label("J1").also{it.style = "-fx-font-size: 55px;"; it.styleClass.addAll("itim","j1")}
    ).also{it.alignment = Pos.CENTER}
    val labelInformation = Label("Vous pouvez lancer les dés !").also{
        it.style="-fx-font-size: 30px; -fx-underline: true;"
        it.styleClass.add("handrawn")
        it.padding = Insets(0.0,0.0,2.5,0.0)
    }
    private val cadrePickominos = FlowPane(HORIZONTAL)
    val boutonLancer = Button("Lancer").also{it.styleClass.addAll("bouton","bouton-lancer")}
    private val desGardes = HBox()
    private val desLances = HBox()
    val boutonValider = Button("Valider").also{it.styleClass.addAll("bouton","bouton-valider")}
    val boutonJoueurSuivant = Button("Joueur suivant").also{it.styleClass.addAll("bouton","bouton-joueur-suivant")}
    private val boutonResultats = Button("Résultats finaux").also{it.styleClass.addAll("bouton", "bouton-resultats")}
    private val cadreDes = HBox(desGardes, desLances)
    val cadreBoutons = HBox(boutonLancer, boutonValider)
    private val cadreCentre = BorderPane()
    val cadreJoueurs = HBox()

    private lateinit var listeJoueurs : Array<HBox>
    private lateinit var listeInfoJoueurs : Array<VBox>
    private lateinit var listeLabelJoueurs : Array<Label>
    private lateinit var listeLabelNbPickomino : Array<Label>
    lateinit var listeBoutonPickoSommetPile : Array<PickominoButton>
    val listeBoutonPickoAccess = mutableListOf<PickominoButton>()

    var listeDesLances = mutableListOf<DiceButton>()
    val listeDesGardes = mutableListOf<DiceButton>()

    fun init(nbJoueurs : Int) {
        listeJoueurs = Array(nbJoueurs){HBox()}
        listeInfoJoueurs = Array(nbJoueurs){VBox()}
        listeLabelJoueurs = Array(nbJoueurs){i -> Label("J${i+1}")}
        listeLabelNbPickomino = Array(nbJoueurs){Label("\tNombre\nde Pickomino : 0")}
        listeBoutonPickoSommetPile = Array(nbJoueurs){PickominoButton(0)}
        boutonValider.isDisable = true

        cadreCentre.top = VBox(cadrePickominos).also{it.alignment = Pos.CENTER}
        cadreCentre.center = cadreDes
        cadreCentre.bottom = cadreBoutons

        cadreBoutons.spacing = 15.0
        setMargin(cadreBoutons, Insets(0.0, 0.0, 25.0, 0.0))

        desGardes.padding = Insets(15.0)
        desLances.padding = Insets(15.0)


        cadreDes.alignment = Pos.CENTER
        cadreDes.spacing = 0.0

        cadreBoutons.alignment = Pos.CENTER

        desGardes.spacing = 10.0
        desLances.spacing = 10.0

        cadrePickominos.vgap = 15.0
        cadrePickominos.hgap = 15.0
        cadrePickominos.padding = Insets(20.0)
        cadrePickominos.maxWidth = 781.0
        cadrePickominos.alignment = Pos.TOP_CENTER
        cadrePickominos.children.addAll(listeBoutonPickoAccess)

        for (i in 0 until nbJoueurs) {
            listeInfoJoueurs[i].children.addAll(listeLabelJoueurs[i], listeLabelNbPickomino[i])
            listeInfoJoueurs[i].alignment = Pos.CENTER
            listeInfoJoueurs[i].spacing = 10.0
            listeLabelJoueurs[i].style = "-fx-font-size: 32px;"
            listeLabelJoueurs[i].styleClass.addAll("itim","j${i+1}")
            listeJoueurs[i].children.addAll(listeInfoJoueurs[i], listeBoutonPickoSommetPile[i])
            listeJoueurs[i].alignment = Pos.CENTER
            listeJoueurs[i].spacing = 30.0
            listeJoueurs[i].padding = Insets(15.0)
            listeJoueurs[i].border = Border(BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
            cadreJoueurs.children.add(listeJoueurs[i])
        }

        cadreJoueurs.alignment = Pos.CENTER
        cadreJoueurs.padding = Insets(15.0)
        cadreJoueurs.styleClass.add("cadre-joueurs")

        style = "-fx-background-color: #FAEBD7;"
        top = BorderPane(
            VBox(cadreTourJoueur, labelInformation).also{it.alignment = Pos.CENTER; setMargin(it, Insets(-55.0, 15.0, 15.0, 15.0))},
            HBox(boutonEffets, boutonMusique).also{it.spacing = 5.0; setMargin(it, Insets(15.0, 0.0, 0.0, 15.0))},
            null,
            null,
            null
        )
        center = cadreCentre
        bottom = cadreJoueurs

        listeMusiques.forEachIndexed{index, mediaPlayer ->
            mediaPlayer.volume = 0.5
            mediaPlayer.setOnEndOfMedia {
                mediaPlayer.stop()
                listeMusiques[(index + 1) % listeMusiques.size].play()
            }
        }
        listeMusiques.first().play()
    }

    private fun fixeControleurDes() {
        listeDesLances.forEach { des ->
            des.onAction = ControleurDes(this)
            setEffetAuSurvol(des, des.type, listeDesLances)
        }
    }

    private fun fixeControleurPickominos() {
        listeBoutonPickoAccess.forEach { pickomino ->
            pickomino.onAction = ControleurPickomino(this)
            setEffetAuSurvol(pickomino, pickomino.value, listeBoutonPickoAccess)
        }
    }

    private fun setEffetAuSurvol(button: Button, targetType: Any, buttonsList: List<Button>) {
        button.setOnMouseEntered {
            handleButtonHover(buttonsList, targetType, 1.1, 10.0)
        }

        button.setOnMouseExited {
            handleButtonHover(buttonsList, targetType, 1.0, 5.0)
        }
    }

    private fun handleButtonHover(buttonsList: List<Button>, targetType: Any, scale: Double, padding: Double) {
        buttonsList.forEach { otherButton ->
            val targetValue = when(otherButton) {
                is DiceButton -> otherButton.type
                is PickominoButton -> otherButton.value
                else -> null
            }
            if (targetValue == targetType) {
                otherButton.graphic.scaleX = scale
                otherButton.graphic.scaleY = scale
                otherButton.padding = Insets(padding)
            }
        }
    }

    fun updateDesLances(listeDes : List<DICE>) {
        listeDesLances.clear()
        desLances.children.clear()
        for (des in listeDes) {
            val boutonDes = DiceButton(des, des in listeDesGardes.map{it.type})
            listeDesLances.add(boutonDes)
            fixeControleurDes()
            desLances.children.add(VBox(boutonDes).also{it.alignment = Pos.CENTER})
        }
    }

    fun activerPickomino(value : Int, joueur : Int) : Boolean {
        var pickominoActive = false
        val pickominoAccess = listeBoutonPickoAccess.lastOrNull{it.value <= value}
        if (pickominoAccess != null) {
            pickominoActive = true
            pickominoAccess.isDisable = false
            pickominoAccess.style = ""
        }

        for (i in listeBoutonPickoSommetPile.indices) {
            if (i != joueur && listeBoutonPickoSommetPile[i].value == value) {
                pickominoActive = true
                listeBoutonPickoSommetPile[i].isDisable = false
                listeBoutonPickoSommetPile[i].style = ""
            }
        }
        return pickominoActive
    }

    fun updateAffichageDes() {
        desLances.children.clear()
        desGardes.children.clear()
        for (boutonDes in listeDesLances) {
            desLances.children.add(VBox(boutonDes).also{it.alignment = Pos.CENTER})
        }
        for (boutonDes in listeDesGardes) {
            desGardes.children.add(VBox(boutonDes).also{it.alignment = Pos.CENTER})
        }
    }

    fun clearDesLances() {
        desLances.children.clear()
        listeDesLances.clear()
    }

    fun clearDesGardes() {
        desGardes.children.clear()
        listeDesGardes.clear()
    }

    fun updateStackTops(sommetsPilesPickominoJoueurs: List<Int>){
        for (i in listeJoueurs.indices){
            val pickomino = PickominoButton(sommetsPilesPickominoJoueurs[i], true)
            pickomino.onAction = ControleurPickomino(this)
            listeBoutonPickoSommetPile[i] = pickomino
            listeJoueurs[i].children[1] = listeBoutonPickoSommetPile[i]
        }
    }

    fun updatePickominos(listePickominoAccessible: List<Int>){
        listeBoutonPickoAccess.clear()
        for (value in listePickominoAccessible)
            listeBoutonPickoAccess.add(PickominoButton(value))
        cadrePickominos.children.setAll(listeBoutonPickoAccess)
        fixeControleurPickominos()
    }

    fun updateNombrePickomino(nombrePickominoJoueurs : List<Int>) {
        for (i in nombrePickominoJoueurs.indices)
            listeLabelNbPickomino[i].text = "\tNombre\nde Pickomino : ${nombrePickominoJoueurs[i]}"
    }

    fun updateCadreInformation(joueurActuel : Int) {
        val labelJoueur = cadreTourJoueur.children[1] as Label
        labelJoueur.text = "J${joueurActuel+1}"
        labelJoueur.styleClass[0] = "j${joueurActuel+1}"
    }

    fun valuePickominoSelectionne() : Int {
        val listePickomino = listeBoutonPickoAccess+listeBoutonPickoSommetPile
        val i = listePickomino.indexOfFirst{it.isSelected}
        return if (i != -1) listePickomino[i].value else 0
    }

    fun declencherFinPartie(appli: Main, stage: Stage, scoreFinaux : List<Int>, listePickoMax : List<Int>) {
        boutonLancer.isDisable = true
        boutonValider.isDisable = true
        cadreTourJoueur.children.setAll(Label("C'est la fin de la partie !").also{it.style = "-fx-font-size: 55px;"; it.styleClass.add("handrawn")})
        labelInformation.text = ""
        cadreCentre.center = boutonResultats
        boutonResultats.setOnAction {
            val vueFin = VueFin(listeBoutonPickoSommetPile.size)
            vueFin.boutonMenu.setOnAction{appli.relancerMenu(stage)}
            vueFin.boutonRejouer.setOnAction{appli.resetPartie(); appli.lancerPartie(listeJoueurs.size, stage); appli.activerModeDebug()}
            vueFin.init(scoreFinaux, listePickoMax)
            val sceneFin = Scene(vueFin)
            sceneFin.stylesheets.add("stylesheets/styles.css")
            stage.close()
            stage.minWidth = 1280.0
            stage.minHeight = 940.0
            stage.width = 1600.0
            stage.height = 900.0
            stage.scene = sceneFin
            stage.show()
        }
    }

    private fun createMediaPlayer(musicPath: String): MediaPlayer {
        return MediaPlayer(Media(javaClass.getResource(musicPath)?.toExternalForm()))
    }

    fun jouerSonDes() {
        if (boutonEffets.isActive) {
            sonDes.stop()
            sonDes.play()
        }
    }

    fun jouerSonSelectionne() {
        if (boutonEffets.isActive) {
            sonSelectionne.stop()
            sonSelectionne.play()
        }
    }

    fun jouerSonDeselectionne() {
        if (boutonEffets.isActive) {
            sonDeselectionne.stop()
            sonDeselectionne.play()
        }
    }

    fun jouerSonPickoPris() {
        if (boutonEffets.isActive) {
            sonPickoPris.stop()
            sonPickoPris.play()
        }
    }

    fun couperTouteLesMusiques() {
        for (musique in listeMusiques)
            musique.stop()
    }
}
