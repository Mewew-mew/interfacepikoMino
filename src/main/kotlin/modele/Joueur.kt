package modele

class Joueur {
    private var valueStackTop = 0
    private val listePickomino = mutableListOf<Int>()

    fun ajouterPickomino(pickomino: Int) {
        listePickomino.add(pickomino)
    }

    fun retirerPickomino() {
        if (listePickomino.isNotEmpty())
            listePickomino.removeLast()
    }

    fun getNombrePickomino() : Int {
        return listePickomino.size
    }

    fun updateStackTop(pickomino : Int) {
        valueStackTop = pickomino
    }

    fun donnePickoMax() : Int {
        return listePickomino.maxOrNull() ?: 0
    }
}