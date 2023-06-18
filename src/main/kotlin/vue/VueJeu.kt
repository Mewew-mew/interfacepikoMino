package vue

import controleur.jeu.*
import iut.info1.pickomino.data.DICE
import javafx.geometry.Insets
import javafx.geometry.Orientation.HORIZONTAL
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import modele.JeuPickomino

class VueJeu : BorderPane() {
    private val sonDes = MediaPlayer(Media(javaClass.getResource("/sounds/dice_rolling.mp3")!!.toString()))
    private val sonSelectionne = MediaPlayer(Media(javaClass.getResource("/sounds/selected.mp3")!!.toString()))
    private val sonDeselectionne = MediaPlayer(Media(javaClass.getResource("/sounds/unselected.mp3")!!.toString()))
    private val sonPickoPris = MediaPlayer(Media(javaClass.getResource("/sounds/take_pickomino.mp3")!!.toString()))

    val boutonVolume = SoundButton()
    private val cadreTourJoueur = HBox(
        Label("C'est au  tour du joueur : ").also{it.style = "-fx-font-size: 55px;"; it.styleClass.add("handrawn")},
        Label("J1").also{it.style = "-fx-font-size: 55px;"; it.styleClass.addAll("itim","j1")}
    ).also{it.alignment = Pos.CENTER}
    private val labelInformation = Label("Vous pouvez lancer les dés !").also{it.style="-fx-font-size: 30px; -fx-underline: true;"; it.styleClass.add("handrawn")}
    private val cadrePickominos = FlowPane(HORIZONTAL)
    val boutonLancer = Button("Lancer").also{it.styleClass.addAll("bouton","bouton-lancer")}
    private val desGardes = HBox()
    private val desLances = HBox()
    val boutonValider = Button("Valider").also{it.styleClass.addAll("bouton","bouton-valider")}
    val boutonJoueurSuivant = Button("Joueur suivant").also{it.styleClass.addAll("bouton","bouton-joueur-suivant")}
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

        cadreCentre.top = VBox(cadrePickominos).also{
            it.alignment = Pos.CENTER
            setMargin(it, Insets(50.0, 0.0, 0.0,0.0))
        }
        cadreCentre.center = cadreDes
        cadreCentre.bottom = cadreBoutons

        cadreBoutons.spacing = 15.0

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
            listeJoueurs[i].spacing = 10.0
            listeJoueurs[i].padding = Insets(10.0)
            listeJoueurs[i].border = Border(BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
            cadreJoueurs.children.add(listeJoueurs[i])
        }

        setMargin(cadreJoueurs, Insets(15.0))

        cadreJoueurs.alignment = Pos.CENTER



        top = BorderPane(
            VBox(cadreTourJoueur, labelInformation).also{it.alignment = Pos.CENTER; setMargin(it, Insets(-55.0, 15.0, 15.0, 15.0))},
            boutonVolume,
            null,
            null,
            null
        )
        center = cadreCentre
        bottom = cadreJoueurs

        style = "-fx-background-color: #FAEBD7;"
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

    fun fixeControleurBoutons(modele: JeuPickomino) {
        boutonLancer.onAction = ControleurBoutonLancer(this, modele)
        boutonValider.onAction = ControleurBoutonValider(this, modele)
        boutonJoueurSuivant.onAction = ControleurBoutonJoueurSuivant(this, modele)
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

    fun jouerSonDes() {
        if (boutonVolume.isActive) {
            sonDes.stop()
            sonDes.play()
        }
    }

    fun jouerSonSelectionne() {
        if (boutonVolume.isActive) {
            sonSelectionne.stop()
            sonSelectionne.play()
        }
    }

    fun jouerSonDeselectionne() {
        if (boutonVolume.isActive) {
            sonDeselectionne.stop()
            sonDeselectionne.play()
        }
    }

    fun jouerSonPickoPris() {
        if (boutonVolume.isActive) {
            sonPickoPris.stop()
            sonPickoPris.play()
        }
    }
}
