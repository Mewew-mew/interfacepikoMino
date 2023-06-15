package vue

import javafx.geometry.Insets
import javafx.geometry.Orientation.HORIZONTAL
import javafx.geometry.Pos
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
    val cadreJouable = HBox(desGardes, desLances, boutonValider)
    val cadreCentre = VBox(cadrePickominos, cadreJouable, boutonLancer)

    val cadreJoueurs = HBox()
    private val listeJoueurs = Array(nbJoueurs){HBox()}
    private val listeInfoJoueurs = Array(nbJoueurs){VBox()}
    private val listeLabelJoueurs = Array(nbJoueurs){i -> Label("J${i+1}")}
    private val listeLabelNbPickomino = Array(nbJoueurs){Label("\tNombre\nde Pickomino : 0")}
    private val listeImgPickoSommetPile = Array(nbJoueurs){ImageView(Image("Pickominos/Pickomino_0.png"))}


    init {
        cadreCentre.alignment = Pos.CENTER
        cadreCentre.spacing = 60.0

        cadreJouable.alignment = Pos.CENTER
        cadreJouable.spacing = 50.0

        desGardes.spacing = 10.0
        desLances.spacing = 10.0

        cadrePickominos.vgap = 15.0
        cadrePickominos.hgap = 15.0
        cadrePickominos.padding = Insets(20.0)
        cadrePickominos.maxWidth = 753.0



        for (i in 21..36)
            cadrePickominos.children.add(ImageView(Image("Pickominos/Pickomino_$i.png", 76.0, 147.5, true, false)))

        for (i in 0 until 8) {
            val boutonDes = Button("", ImageView(Image("Dices/Dice_1.png", 75.0, 75.0, true, false)))
            boutonDes.padding = Insets(0.0)
            boutonDes.border = null
            desLances.children.add(boutonDes)
        }


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
