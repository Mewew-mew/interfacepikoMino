package vue

import controleur.jeu.*
import iut.info1.pickomino.data.DICE
import javafx.geometry.Insets
import javafx.geometry.Orientation.HORIZONTAL
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import modele.JeuPickomino

class VueJeu : BorderPane(){

    private val cadrePickominos = FlowPane(HORIZONTAL).also{it.style = "-fx-background-color: lightcoral;"}
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
        cadrePickominos.maxWidth = 780.0
        cadrePickominos.alignment = Pos.CENTER
        cadrePickominos.children.addAll(listeBoutonPickoAccess)

        val listeCouleur = listOf("red", "blue", "green", "gold")
        for (i in 0 until nbJoueurs) {
            listeInfoJoueurs[i].children.addAll(listeLabelJoueurs[i], listeLabelNbPickomino[i])
            listeInfoJoueurs[i].alignment = Pos.CENTER
            listeInfoJoueurs[i].spacing = 10.0
            listeLabelJoueurs[i].style = "-fx-font-size: 25px; -fx-text-fill: ${listeCouleur[i]};"
            listeJoueurs[i].children.addAll(listeInfoJoueurs[i], listeBoutonPickoSommetPile[i])
            listeJoueurs[i].spacing = 10.0
            listeJoueurs[i].padding = Insets(10.0)
            listeJoueurs[i].border = Border(BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
            cadreJoueurs.children.add(listeJoueurs[i])
        }

        setMargin(cadreJoueurs, Insets(15.0))

        cadreJoueurs.alignment = Pos.CENTER

        center = cadreCentre
        bottom = cadreJoueurs
    }

    private fun fixeControleurDes() {
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

    fun activerPickomino(value : Int, joueur : Int) {
        val pickominoAccess = listeBoutonPickoAccess.lastOrNull{it.value <= value}
        pickominoAccess?.isDisable = false
        pickominoAccess?.style = ""

        for (i in listeBoutonPickoSommetPile.indices) {
            if (i != joueur && listeBoutonPickoSommetPile[i].value == value) {
                listeBoutonPickoSommetPile[i].isDisable = false
                listeBoutonPickoSommetPile[i].style = ""
            }
        }
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
        cadreJoueurs.children.clear()
        for (i in listeJoueurs.indices){
            val pickomino =PickominoButton(sommetsPilesPickominoJoueurs[i], true)
            pickomino.onAction = ControleurPickomino(this)

            listeBoutonPickoSommetPile[i] = pickomino
            listeJoueurs[i].children.clear()
            listeJoueurs[i].children.addAll(listeInfoJoueurs[i], listeBoutonPickoSommetPile[i])
            listeJoueurs[i].spacing = 10.0
            listeJoueurs[i].padding = Insets(10.0)
            listeJoueurs[i].border = Border(BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
            cadreJoueurs.children.add(listeJoueurs[i])
        }
    }

    fun updatePickominos(listePickominoAccessible: List<Int>){
        listeBoutonPickoAccess.clear()
        for (value in listePickominoAccessible)
            listeBoutonPickoAccess.add(PickominoButton(value))
        cadrePickominos.children.setAll(listeBoutonPickoAccess)
        fixeControleurPickominos()
    }

    fun valuePickominoSelectionne() : Int {
        val listePickomino = listeBoutonPickoAccess+listeBoutonPickoSommetPile
        val i = listePickomino.indexOfFirst{it.isSelected}
        return if (i != -1) listePickomino[i].value else 0
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
