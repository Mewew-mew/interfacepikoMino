package modele

class Joueur(val id: Int) {

    var valueStackTop = 0
    var nombrePickomino = 0

    override fun toString(): String {
        return "J${id+1}"
    }
}