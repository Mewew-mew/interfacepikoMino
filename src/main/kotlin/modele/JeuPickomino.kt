package modele

import iut.info1.pickomino.Connector
import iut.info1.pickomino.data.DICE
import iut.info1.pickomino.data.DICE.*
import iut.info1.pickomino.data.Game
import iut.info1.pickomino.data.Pickomino
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import kotlin.math.floor

class JeuPickomino(nbJoueurs : Int) {
    private val connect = Connector.factory("172.26.82.76", "8080", true)
    private val id : Int
    private val key : Int
    val listeJoueurs = Array(nbJoueurs){i -> Joueur(i)}
    val tourDuJoueur = 0

    init {
        val identification = connect.newGame(nbJoueurs)
        id = identification.first
        key = identification.second

        // Debug
        /*
        val sommets=sommetsPilesPickominoJoueurs()
        for (i in 0 until 4)
            try {
                listeJoueurs[i].valueStackTop=sommets[i]
            }
            catch (e: ArrayIndexOutOfBoundsException) {
                break
            }
        */
    }

    fun lancerDes(): List<DICE> {
        return connect.rollDices(id, key)
    }

    fun choisirDes(dices: List<DICE>): List<DICE> {
        return connect.choiceDices(id, key, dices)
    }

    fun garderDes(dice: DICE): Boolean {
        if (!connect.keepDices(id, key, dice))
            return false
        return true
    }

    fun prendrePickomino(pickomino: Int): Boolean {
        if (!connect.takePickomino(id, key, pickomino))
            return false
        val stackTops = sommetsPilesPickominoJoueurs()
        for (i in listeJoueurs.indices) {
            listeJoueurs[i].valueStackTop = stackTops[i]
            listeJoueurs[i].nombrePickomino++
        }
        return true
    }

    fun obtenirScoreFinal(): List<Int> {
        return connect.finalScore(id, key)
    }
    fun obtenirEtatJeu(): Game {
        return connect.gameState(id, key)
    }

    fun listeDesLance() : List<DICE> {
        return obtenirEtatJeu().current.rolls
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

    private fun sommetsPilesPickominoJoueurs(): List<Int> {
        return obtenirEtatJeu().pickosStackTops()
    }
}