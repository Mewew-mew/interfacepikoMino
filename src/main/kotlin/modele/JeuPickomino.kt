package modele

import iut.info1.pickomino.Connector
import iut.info1.pickomino.data.DICE
import iut.info1.pickomino.data.Game
import vue.DiceButton

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
        val joueurActuel = joueurActuel()
        val listeDesGardes = listeDesGardes()
        val listeDesLances = connect.rollDices(id, key)
        if (listeDesLances.all{it in listeDesGardes}) {
            if (listeJoueurs[joueurActuel].nombrePickomino != 0)
                listeJoueurs[joueurActuel].nombrePickomino--
            listeJoueurs[joueurActuel].valueStackTop = sommetsPilesPickominoJoueurs()[joueurActuel]
        }
        return listeDesLances
    }

    fun garderDes(dice: DICE): Boolean {
        return connect.keepDices(id, key, dice)
    }

    fun prendrePickomino(pickomino: Int): Boolean {
        val joueurActuel = joueurActuel()
        val stackTopsBefore = sommetsPilesPickominoJoueurs()

        if (!connect.takePickomino(id, key, pickomino))
            return false
        val stackTopsAfter = sommetsPilesPickominoJoueurs()
        for (i in stackTopsBefore.indices) {
            if (i != joueurActuel && stackTopsBefore[i] != stackTopsAfter[i]) {
                if (listeJoueurs[i].nombrePickomino != 0)
                    listeJoueurs[i].nombrePickomino--
                break
            }
        }

        listeJoueurs[joueurActuel].valueStackTop = stackTopsAfter[joueurActuel]
        listeJoueurs[joueurActuel].nombrePickomino++
        return true
    }

    fun donneNombrePickominoJoueurs(): List<Int> {
        return listeJoueurs.map{it.nombrePickomino}
    }

    fun obtenirScoreFinal(): List<Int> {
        return connect.finalScore(id, key)
    }
    private fun obtenirEtatJeu(): Game {
        return connect.gameState(id, key)
    }

    fun listeDesGardes() : List<DICE> {
        return obtenirEtatJeu().current.keptDices
    }

    fun listeDesLances() : List<DICE> {
        return obtenirEtatJeu().current.rolls
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

    fun sommeDes(dices : List<DiceButton>) : Int {
        var somme = 0
        for (dice in dices.map{it.type})
            somme += when (dice) {
                DICE.d1 -> 1
                DICE.d2 -> 2
                DICE.d3 -> 3
                DICE.d4 -> 4
                else -> 5
            }
        return somme
    }
}