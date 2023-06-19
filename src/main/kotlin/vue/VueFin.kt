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

    private val boutonRejouer = Button("Rejouer").also{it.styleClass.addAll("bouton", "bouton-rejouer")}
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

    fun test(scoreFinaux : List<Int>) {
        val listeJoueurs = Array(nbJoueurs){i -> i+1}
        val podium = listeJoueurs.zip(scoreFinaux).sortedByDescending{it.second}
        val cadreCentre = HBox().also{it.spacing = 50.0; it.alignment = Pos.CENTER}
        val listeCouleursPodium = listOf("goldenrod", "silver", "#CD7F32", "gray")
        val listeRectanglePodium = mutableListOf<StackPane>()
        for (i in 0 until nbJoueurs) {
            val rectangle = Rectangle(240.0, 300.0-i*75).also{
                it.style = "-fx-fill: ${listeCouleursPodium[i]}; -fx-stroke: black; -fx-stroke-width: 5px;"
            }
            if (i != 3) {
                val img = ImageView(Image("images/${i+1}.png", rectangle.width-20, rectangle.height-20, true, false))
                listeRectanglePodium.add(StackPane(rectangle, img))
            } else listeRectanglePodium.add(StackPane(rectangle))

            val place = podium[i].first
            val score = podium[i].second
            val labelJoueur = Label("Joueur $place").also{it.styleClass.add("j$place"); it.style = "-fx-font-size: 35px;"}
            val labelPlace = Label(if (i == 0) "1er" else "${i+1}ème").also{it.style = "-fx-font-size: 25px; -fx-text-fill: ${listeCouleursPodium[i]};"}
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