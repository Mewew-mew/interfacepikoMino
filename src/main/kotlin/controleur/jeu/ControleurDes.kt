package controleur.jeu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.layout.*
import javafx.scene.paint.Color
import vue.DiceButton
import vue.VueJeu

class ControleurDes(private val vueJeu: VueJeu) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        val typeDes = (event.source as DiceButton).type
        for (des in vueJeu.listeDesLances) {
            if (des.type == typeDes) {
                des.isSelected = !des.isSelected
                if (des.isSelected)
                    des.border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
                else
                    des.border = null
            }
            else {
                des.border = null
                des.isSelected = false
            }
        }
        vueJeu.boutonValider.isDisable = vueJeu.listeDesLances.none{it.isSelected}
    }
}