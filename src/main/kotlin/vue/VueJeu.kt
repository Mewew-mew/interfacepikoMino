package vue

import Main
import controleur.jeu.ControleurBoutonLancer
import controleur.jeu.ControleurDes
import iut.info1.pickomino.data.DICE
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.geometry.Orientation.HORIZONTAL
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color

class VueJeu(nbJoueurs : Int) : BorderPane(){

    val cadrePickominos = FlowPane(HORIZONTAL)
    val boutonLancer = Button("Lancer")
    val desGardes = HBox()
    val desLances = HBox()
    val boutonValider = Button("Valider")
    val cadreDes = HBox(desGardes, desLances)
    val cadreBoutons = HBox(boutonLancer, boutonValider)
    val cadreCentre = BorderPane()

    val cadreJoueurs = HBox()
    val listeJoueurs = Array(nbJoueurs){HBox()}
    val listeInfoJoueurs = Array(nbJoueurs){VBox()}
    val listeLabelJoueurs = Array(nbJoueurs){i -> Label("J${i+1}")}
    val listeLabelNbPickomino = Array(nbJoueurs){Label("\tNombre\nde Pickomino : 0")}
    val listeImgPickoSommetPile = Array(nbJoueurs){ImageView(Image("Pickominos/Pickomino_0.png"))}
    val listeBoutonPickoAccess = mutableListOf<PickominoButton>()
    var listeDesLances = mutableListOf<DiceButton>()
    val listeDesGardes = mutableListOf<DiceButton>()

    init {
        //------------ Debug
        /*
        val listeDes = listOf(
            DiceButton("Dices/Dice_1.png", DICE.d1),
            DiceButton("Dices/Dice_1.png", DICE.d1),
            DiceButton("Dices/Dice_2.png", DICE.d2),
            DiceButton("Dices/Dice_2.png", DICE.d2),
            DiceButton("Dices/Dice_3.png", DICE.d3),
            DiceButton("Dices/Dice_4.png", DICE.d4),
            DiceButton("Dices/Dice_5.png", DICE.d5),
            DiceButton("Dices/Dice_6.png", DICE.worm)
        )

        for (boutonDes in listeDes) {
            listeDesLances.add(boutonDes)
            desLances.children.add(VBox(boutonDes).also{it.alignment = Pos.CENTER})
        }
        */
        // -----------------------

        boutonLancer.style = "-fx-background-color: #348ded;" +
        "-fx-background-radius: 30px;" +
        "-fx-border-radius:30px;" +
        "-fx-border-width: 2px;" +
        "-fx-border-color: #000000;" +
        "-fx-display: inline-block;" +
        "-fx-cursor: hand;" +
        "-fx-text-fill: #FFFFFF;" +
        "-fx-font-size:17px;" +
        "-fx-padding:22px 39px;" +
        "-fx-text-decoration: none;"
        boutonLancer.setOnMouseEntered {
            boutonLancer.style += "-fx-background-color:#266fbd;"
        }

        boutonLancer.setOnMouseExited {
            boutonLancer.style = boutonLancer.style.removeSuffix("-fx-background-color:#266fbd;")
        }

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
        cadrePickominos.maxWidth = 753.0
        cadrePickominos.alignment = Pos.CENTER
        cadrePickominos.children.addAll(listeBoutonPickoAccess)

        val listeCouleur = listOf("red", "blue", "green", "gold")
        for (i in 0 until nbJoueurs) {
            listeInfoJoueurs[i].children.addAll(listeLabelJoueurs[i], listeLabelNbPickomino[i])
            listeInfoJoueurs[i].alignment = Pos.CENTER
            listeInfoJoueurs[i].spacing = 10.0
            listeLabelJoueurs[i].style = "-fx-font-size: 25px; -fx-text-fill: ${listeCouleur[i]};"
            listeJoueurs[i].children.addAll(listeInfoJoueurs[i], listeImgPickoSommetPile[i])
            listeJoueurs[i].spacing = 10.0
            listeJoueurs[i].padding = Insets(10.0)
            listeJoueurs[i].border = Border(BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
            cadreJoueurs.children.add(listeJoueurs[i])
            listeImgPickoSommetPile[i].fitWidth = 38.0
            listeImgPickoSommetPile[i].fitHeight = 73.75
        }

        setMargin(cadreJoueurs, Insets(15.0))

        cadreJoueurs.alignment = Pos.CENTER

        center = cadreCentre
        bottom = cadreJoueurs
    }

    fun fixeControleurDes() {
        for (des in listeDesLances)
            des.onAction = ControleurDes(this)
    }

    fun fixeControleurBoutonLancer(appli : Main) {
        boutonLancer.onAction = ControleurBoutonLancer(appli)
    }

    fun updateDesLances(listeDes : List<DICE>) {
        listeDesLances.clear()
        desLances.children.clear()
        for (des in listeDes) {
            val boutonDes = DiceButton("Dices/Dice_${des.ordinal+1}.png", des)
            listeDesLances.add(boutonDes)
            fixeControleurDes()
            desLances.children.add(VBox(boutonDes).also{it.alignment = Pos.CENTER})
        }
    }

    fun updateStackTops(newImages:Array<String>){
        cadreJoueurs.children.clear()
        for (i in listeJoueurs.indices){
            listeImgPickoSommetPile[i]= ImageView(Image(newImages[i]))
            listeJoueurs[i].children.clear()
            listeJoueurs[i].children.addAll(listeInfoJoueurs[i], listeImgPickoSommetPile[i])
            listeJoueurs[i].spacing = 10.0
            listeJoueurs[i].padding = Insets(10.0)
            listeJoueurs[i].border = Border(BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
            cadreJoueurs.children.add(listeJoueurs[i])
            listeImgPickoSommetPile[i].fitWidth = 38.0
            listeImgPickoSommetPile[i].fitHeight = 73.75
        }
        bottom = cadreJoueurs
    }

    fun updatePickominos(newImages: Array<String>){
        cadrePickominos.children.clear()
        for (i in newImages.indices)
            cadrePickominos.children.add(ImageView(Image(newImages[i], 76.0, 147.5, true, false)))
    }


    fun ajouterUnPickomino(joueur : Int) {

        val nombreActuel = listeLabelNbPickomino[joueur].text.takeLastWhile{it != ' '}.toInt()
        listeLabelNbPickomino[joueur].text = "\tNombre\nde Pickomino : ${nombreActuel+1}"
    }

    fun retirerUnPickomino(joueur : Int) {
        val nombreActuel = listeLabelNbPickomino[joueur].text.takeLastWhile{it != ' '}.toInt()
        if (nombreActuel != 0)
            listeLabelNbPickomino[joueur].text = "\tNombre\nde Pickomino : ${nombreActuel-1}"
    }
}
