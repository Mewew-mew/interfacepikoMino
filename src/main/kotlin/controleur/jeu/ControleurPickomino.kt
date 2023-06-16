package controleur.jeu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.layout.*
import javafx.scene.paint.Color
import vue.PickominoButton
import vue.VueJeu

class ControleurPickomino(private val vueJeu: VueJeu) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        val pickomino = event.source as PickominoButton
        pickomino.setOnMouseEntered {
            val targetType = pickomino.value
            vueJeu.listeBoutonPickoAccess.forEach { otherButton ->
                if (otherButton.value == targetType) {
                    otherButton.graphic.scaleX = 1.1
                    otherButton.graphic.scaleY = 1.1
                    otherButton.padding = Insets(10.0)
                }
            }
        }

        pickomino.setOnMouseExited {
            val targetType = pickomino.value
            vueJeu.listeBoutonPickoAccess.forEach { otherButton ->
                if (otherButton.value == targetType) {
                    otherButton.graphic.scaleX = 1.0
                    otherButton.graphic.scaleY = 1.0
                    otherButton.padding = Insets(5.0)
                }
            }
        }

        if (pickomino in vueJeu.listeBoutonPickoAccess)
            for (pickominoStackTop in vueJeu.listeBoutonPickoSommetPile) {
                pickominoStackTop.isSelected = false
                pickominoStackTop.border = null
            }
        else
            for (pickominoAccess in vueJeu.listeBoutonPickoAccess) {
                pickominoAccess.isSelected = false
                pickominoAccess.border = null
            }

        pickomino.isSelected = !pickomino.isSelected
        vueJeu.boutonValider.isDisable = !pickomino.isSelected
        if (pickomino.isSelected)
            pickomino.border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
        else
            pickomino.border = null
    }
}