package modele

class Joueur(private val id: Int) {

    var valueStackTop = 0
    var nombrePickomino : Int = 0

    override fun toString(): String {
        return "J${id+1}"
    }
}