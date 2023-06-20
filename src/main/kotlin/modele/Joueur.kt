package modele

import iut.info1.pickomino.data.Pickomino

class Joueur {
    private var valueStackTop = 0
    private var nombrePickomino = 0
    private val listePickomino = mutableListOf<Int>()

    fun ajouterPickomino(pickomino: Int) {
        nombrePickomino++
        listePickomino.add(pickomino)
    }

    fun retirerPickomino() {
        if (nombrePickomino != 0) {
            nombrePickomino--
            listePickomino.removeLast()
        }

    }

    fun getNombrePickomino() : Int {
        return nombrePickomino
    }

    fun updateStackTop(pickomino : Int) {
        valueStackTop = pickomino
    }

    fun donnePickoMax() : Int {
        return listePickomino.maxOrNull() ?: 0
    }
}