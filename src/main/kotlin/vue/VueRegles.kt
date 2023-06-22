package vue

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class VueRegles : TitledPane() {

    private val listeRegles = List(8){i -> ImageView(Image("images/rules/${i + 1}.png", 440.0, 440.0, true, false))}
    private var reglesActuel = 0
    private val cadreRegle = BorderPane()
    private val boutonPrecedent = Button("<")
    private val boutonSuivant = Button(">")
    val boutonRetour = ReturnButton()

    init {
        boutonPrecedent.setOnAction{
            boutonSuivant.isDisable = false
            reglePrecedente()
        }

        boutonSuivant.setOnAction{
            boutonPrecedent.isDisable = false
            regleSuivante()
        }

        for (regle in listeRegles) {
            regle.fitWidth = 570.0
            regle.fitHeight = 570.0
            BorderPane.setMargin(regle, Insets(-80.0, 0.0, 0.0, 0.0))
        }

        isCollapsible = false

        boutonPrecedent.maxHeight = Double.MAX_VALUE
        boutonPrecedent.style = "-fx-background-color: #FAEBD7;"
        BorderPane.setMargin(boutonPrecedent, Insets(-149.0, 0.0, 0.0, 0.0))
        boutonPrecedent.setOnMouseEntered {
            boutonPrecedent.style = "-fx-background-color: #E9D3C0;"
        }

        boutonPrecedent.setOnMouseExited {
            boutonPrecedent.style = "-fx-background-color: #FAEBD7;"
        }

        boutonSuivant.maxHeight = Double.MAX_VALUE
        boutonSuivant.style = "-fx-background-color: #FAEBD7;"
        BorderPane.setMargin(boutonSuivant, Insets(-149.0, 0.0, 0.0, 0.0))
        boutonSuivant.setOnMouseEntered {
            boutonSuivant.style = "-fx-background-color: #E9D3C0;"
        }

        boutonSuivant.setOnMouseExited {
            boutonSuivant.style = "-fx-background-color: #FAEBD7;"
        }

        BorderPane.setMargin(boutonRetour, Insets(35.0, 0.0, 0.0, 35.0))

        cadreRegle.left = boutonPrecedent
        resetAffichage()
        cadreRegle.right = boutonSuivant
        cadreRegle.top = boutonRetour


        padding = Insets(0.0)
        content = StackPane(Rectangle(668.0, 739.0, Color.WHITE), cadreRegle)
    }

    private fun reglePrecedente() {
        reglesActuel--
        cadreRegle.center = listeRegles[reglesActuel]
        text = "${reglesActuel+1} -"
        if (reglesActuel == 0)
            boutonPrecedent.isDisable = true
    }

    private fun regleSuivante() {
        reglesActuel++
        text = "${reglesActuel+1} -"
        cadreRegle.center = listeRegles[reglesActuel]
        if (reglesActuel == 7)
            boutonSuivant.isDisable = true
    }

    fun resetAffichage() {
        reglesActuel = 0
        text = "1 -"
        cadreRegle.center = listeRegles[0]
        boutonPrecedent.isDisable = true
    }
}