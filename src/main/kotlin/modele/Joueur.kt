package modele

import iut.info1.pickomino.data.Pickomino
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import kotlin.math.floor

class Joueur(private val id: Int) {

    val listePickomino = mutableListOf<Pickomino>()
    var valueStackTop = 0

    fun prendrePickomino(value: Int) {
        listePickomino.add(Pickomino(value, nombreVers(value)))
    }

    private fun nombreVers(value : Int) : Int {
        return floor(((value - 21) / 4).toFloat()).toInt() + 1
    }

    fun rendrePickomino(joueur : Int) {
        if (listePickomino.isNotEmpty())
            listePickomino.removeLast()
    }

    override fun toString(): String {
        return "J${id+1}"
    }
}