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
    var reglesActuel = 0
    private val cadreRegle = BorderPane()
    val boutonPrecedent = Button("<")
    val boutonSuivant = Button(">")
    val boutonRetour = Button("Retour")

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
        }

        isCollapsible = false
        text = "1)"

        boutonPrecedent.isDisable = true
        boutonPrecedent.maxHeight = Double.MAX_VALUE
        boutonSuivant.maxHeight = Double.MAX_VALUE

        cadreRegle.left = boutonPrecedent
        cadreRegle.center = listeRegles[0]
        cadreRegle.right = boutonSuivant
        cadreRegle.bottom = boutonRetour

        padding = Insets(0.0)
        content = StackPane(Rectangle(700.0, 606.0, Color.WHITE), cadreRegle)
    }

    private fun reglePrecedente() {
        reglesActuel--
        cadreRegle.center = listeRegles[reglesActuel]
        if (reglesActuel == 0)
            boutonPrecedent.isDisable = true
    }

    private fun regleSuivante() {
        reglesActuel++
        cadreRegle.center = listeRegles[reglesActuel]
        if (reglesActuel == 7)
            boutonSuivant.isDisable = true
    }
}