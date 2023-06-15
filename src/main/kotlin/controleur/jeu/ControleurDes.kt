package controleur.jeu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.layout.*
import javafx.scene.paint.Color
import vue.DiceButton
import vue.VueJeu

class ControleurDes(private val vueJeu: VueJeu) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        val typeDes = (event.source as DiceButton).type
        for (des in vueJeu.listeDesLances) {
            if (des.type == typeDes) {
                if (des.isSelected) {
                    des.border = null
                    des.isSelected = false
                } else {
                    des.border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
                    des.isSelected = true
                }
            }
            else {
                des.border = null
                des.isSelected = false
            }
        }
    }
}