package vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.shape.Rectangle

class VueFin(private val nbJoueurs : Int) : BorderPane() {

    val boutonRejouer = Button("Rejouer").also{it.styleClass.addAll("bouton", "bouton-rejouer")}
    val boutonMenu = Button("Menu").also{it.styleClass.addAll("bouton", "bouton-menu")}

    init {
        val labelFinaux = Label("Résultats finaux :")
        labelFinaux.styleClass.addAll("itim", "label-resultats")
        val labelPodium = Label("Voici le podium !")
        labelPodium.styleClass.addAll("itim")
        labelPodium.style += "-fx-font-size: 50px;"
        val cadreLabelHaut = VBox(labelFinaux, labelPodium).also{it.alignment = Pos.CENTER; setMargin(it, Insets(-275.0, -260.0, 0.0, 15.0))}
        val imagePoulet = ImageView(Image("images/poulet-sartek.png", 450.0, 338.0, true, false))
        setMargin(imagePoulet, Insets(100.0, 20.0, 0.0, 0.0))
        top = BorderPane(cadreLabelHaut, null, imagePoulet, null, null)
    }

    fun init(scoreFinaux : List<Int>, listePickoMax : List<Int>) {
        val listeJoueurs = Array(nbJoueurs){i -> i}

        val podium = listeJoueurs.sortedWith(compareByDescending<Int>{scoreFinaux[it]}.thenByDescending{listePickoMax[it]})

        val cadreCentre = HBox().also{it.spacing = 50.0; it.alignment = Pos.CENTER}
        val listeCouleursPodium = listOf("goldenrod", "silver", "#CD7F32", "gray")
        val listeRectanglePodium = mutableListOf<StackPane>()

        var placement = -1
        for (i in 0 until nbJoueurs) {
            // Si il y a égalité
            val index : Int
            if (i != 0 && listePickoMax[podium[i-1]] == listePickoMax[podium[i]])
                index = i-1
            else {
                index = i
                placement++
            }

            val rectangle = Rectangle(240.0, 300.0-placement*75).also{
                it.style = "-fx-fill: ${listeCouleursPodium[placement]}; -fx-stroke: black; -fx-stroke-width: 5px;"
            }
            if (index != 3) {
                val img = ImageView(Image("images/medailles/${placement+1}.png", rectangle.width-20, rectangle.height-20, true, false))
                listeRectanglePodium.add(StackPane(rectangle, img))
            } else listeRectanglePodium.add(StackPane(rectangle))

            val joueur = podium[i]
            val score = scoreFinaux[joueur]
            val labelJoueur = Label("Joueur ${joueur+1}").also{it.styleClass.add("j${joueur+1}"); it.style = "-fx-font-size: 35px;"}
            val labelPlace = Label(if (placement == 0) "1er" else "${index+1}ème").also{it.style = "-fx-font-size: 25px; -fx-text-fill: ${listeCouleursPodium[placement]};"}
            val scoreJoueur = Label("Score : $score vers").also{it.style = "-fx-font-size: 25px; -fx-font-weight: bold;"}
            val spacer = Region()
            VBox.setVgrow(spacer, Priority.ALWAYS)
            val rectanglePodium = VBox(spacer, labelJoueur, labelPlace, listeRectanglePodium[i], scoreJoueur).also{it.alignment = Pos.CENTER}
            rectanglePodium.spacing = 10.0
            cadreCentre.children.add(rectanglePodium)
        }

        style = "-fx-background-color: #FAEBD7;"
        center = VBox(cadreCentre).also{it.alignment = Pos.CENTER; setMargin(it, Insets(-275.0, -25.0, 0.0 ,0.0))}
        bottom = HBox(boutonRejouer, boutonMenu).also{it.spacing = 100.0; it.alignment = Pos.CENTER; setMargin(it, Insets(50.0))}
    }
}