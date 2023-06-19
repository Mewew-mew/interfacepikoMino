package controleur.jeu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.layout.*
import javafx.scene.paint.Color
import vue.PickominoButton
import vue.VueJeu

class ControleurPickomino(private val vueJeu: VueJeu) : EventHandler<ActionEvent> {
    override fun handle(event: ActionEvent) {
        val pickomino = event.source as PickominoButton

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
        if (pickomino.isSelected) {
            vueJeu.jouerSonSelectionne()
            pickomino.border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
        } else {
            vueJeu.jouerSonDeselectionne()
            pickomino.border = null
        }
    }
}