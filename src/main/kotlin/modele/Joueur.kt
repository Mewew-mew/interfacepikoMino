package modele

class Joueur {
    private var valueStackTop = 0
    private var nombrePickomino = 0

    fun ajouterPickomino() {
        nombrePickomino++
    }

    fun retirerPickomino() {
        if (nombrePickomino != 0)
            nombrePickomino--
    }

    fun getNombrePickomino() : Int {
        return nombrePickomino
    }

    fun updateStackTop(pickomino : Int) {
        valueStackTop = pickomino
    }
}