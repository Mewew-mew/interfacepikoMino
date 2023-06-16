package vue

import Main
import controleur.jeu.ControleurBoutonLancer
import controleur.jeu.ControleurBoutonValider
import controleur.jeu.ControleurDes
import controleur.jeu.ControleurPickomino
import iut.info1.pickomino.data.DICE
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
    val boutonLancer = Button("Lancer").also{it.styleClass.addAll("bouton","bouton-lancer")}
    val desGardes = HBox()
    val desLances = HBox()
    val boutonValider = Button("Valider").also{it.styleClass.addAll("bouton","bouton-valider")}
    val boutonJoueurSuivant = Button("Joueur suivant").also{it.styleClass.addAll("bouton","bouton-joueur-suivant")}
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
        for (des in listeDesLances) {
            des.onAction = ControleurDes(this)
            des.setOnMouseEntered {
                val targetType = des.type
                listeDesLances.forEach { otherButton ->
                    if (otherButton.type == targetType) {
                        otherButton.graphic.scaleX = 1.1
                        otherButton.graphic.scaleY = 1.1
                        otherButton.padding = Insets(10.0)
                    }
                }
            }

            des.setOnMouseExited {
                val targetType = des.type
                listeDesLances.forEach { otherButton ->
                    if (otherButton.type == targetType) {
                        otherButton.graphic.scaleX = 1.0
                        otherButton.graphic.scaleY = 1.0
                        otherButton.padding = Insets(5.0)
                    }
                }
            }
        }
    }

    private fun fixeControleurPickominos() {
        for (pickomino in listeBoutonPickoAccess) {
            pickomino.onAction = ControleurPickomino(this)
            pickomino.setOnMouseEntered {
                val targetType = pickomino.value
                listeBoutonPickoAccess.forEach { otherButton ->
                    if (otherButton.value == targetType) {
                        otherButton.graphic.scaleX = 1.1
                        otherButton.graphic.scaleY = 1.1
                        otherButton.padding = Insets(10.0)
                    }
                }
            }

            pickomino.setOnMouseExited {
                val targetType = pickomino.value
                listeBoutonPickoAccess.forEach { otherButton ->
                    if (otherButton.value == targetType) {
                        otherButton.graphic.scaleX = 1.0
                        otherButton.graphic.scaleY = 1.0
                        otherButton.padding = Insets(5.0)
                    }
                }
            }
        }
    }

    fun fixeControleurBoutons(appli : Main) {
        boutonLancer.onAction = ControleurBoutonLancer(appli)
        boutonValider.onAction = ControleurBoutonValider(appli)
    }

    fun updateDesLances(listeDes : List<DICE>) {
        listeDesLances.clear()
        desLances.children.clear()
        for (des in listeDes) {
            val boutonDes = DiceButton("Dice_${des.ordinal+1}.png", des, des in listeDesGardes.map{it.type})
            listeDesLances.add(boutonDes)
            fixeControleurDes()
            desLances.children.add(VBox(boutonDes).also{it.alignment = Pos.CENTER})
        }
    }

    fun activerPickomino(value : Int) {
        val pickomino = listeBoutonPickoAccess.lastOrNull{it.value <= value}
        pickomino?.isDisable = false
        pickomino?.style = ""
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

    fun updatePickominos(listePickominoAccessible: List<Int>){
        for (value in listePickominoAccessible)
            listeBoutonPickoAccess.add(PickominoButton(value))
        cadrePickominos.children.setAll(listeBoutonPickoAccess)
        fixeControleurPickominos()
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

    fun sommeDes(dices : List<DiceButton>) : Int {
        var somme = 0
        for (dice in dices.map{it.type})
            somme += when (dice) {
                DICE.d1 -> 1
                DICE.d2 -> 2
                DICE.d3 -> 3
                DICE.d4 -> 4
                else -> 5
            }
        return somme
    }
}
