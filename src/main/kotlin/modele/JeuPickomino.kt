package modele

import iut.info1.pickomino.Connector
import iut.info1.pickomino.data.DICE
import iut.info1.pickomino.data.Game

class JeuPickomino {
    private val connect = Connector.factory("172.26.82.76", "8080", true)
    private var id = 0
    private var key = 0
    lateinit var listeJoueurs : Array<Joueur>

    fun init(nbJoueurs : Int) {
        val identification = connect.newGame(nbJoueurs)
        id = identification.first
        key = identification.second
        listeJoueurs = Array(nbJoueurs){i -> Joueur(i)}
    }

    fun lancerDes(): List<DICE> {
        return connect.rollDices(id, key)
    }

    fun garderDes(dice: DICE): Boolean {
        if (!connect.keepDices(id, key, dice))
            return false
        return true
    }

    fun prendrePickomino(joueur : Int, pickomino: Int): Boolean {
        if (!connect.takePickomino(id, key, pickomino))
            return false
        listeJoueurs[joueur].valueStackTop = sommetsPilesPickominoJoueurs()[joueur]
        listeJoueurs[joueur].nombrePickomino++
        return true
    }

    fun obtenirScoreFinal(): List<Int> {
        return connect.finalScore(id, key)
    }
    fun obtenirEtatJeu(): Game {
        return connect.gameState(id, key)
    }

    fun listeDesGardes() : List<DICE> {
        return obtenirEtatJeu().current.keptDices
    }

    fun joueurActuel() : Int {
        return obtenirEtatJeu().current.player
    }

    fun listePickominoAccessible() : List<Int> {
        return obtenirEtatJeu().accessiblePickos()
    }

    fun sommetsPilesPickominoJoueurs(): List<Int> {
        return obtenirEtatJeu().pickosStackTops()
    }
}