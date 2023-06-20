package modele

import iut.info1.pickomino.Connector
import iut.info1.pickomino.data.DICE
import iut.info1.pickomino.data.Game
import iut.info1.pickomino.data.STATUS

class JeuPickomino {
    @Deprecated("DEBUG") val debug = true
    private lateinit var connect : Connector
    private lateinit var etatJeu : Game
    private var id = 0
    private var key = 0
    private lateinit var listeJoueurs : Array<Joueur>

    @Deprecated("DEBUG")
    fun donneStatus() : STATUS { // DEBUG
        return etatJeu.current.status
    }

    fun init(nbJoueurs : Int) {
        connect = Connector.factory("172.26.82.76", "8080", debug)
        val identification = connect.newGame(nbJoueurs)
        id = identification.first
        key = identification.second
        etatJeu = connect.gameState(id, key)
        listeJoueurs = Array(nbJoueurs){Joueur()}
    }

    fun lancerDes(): List<DICE> {
        val joueurActuel = joueurActuel()
        val listeDesGardesBefore = listeDesGardes()
        val listeDesLances = connect.rollDices(id, key)
        etatJeu = connect.gameState(id, key)
        val listeDesGardesAfter = listeDesGardes()
        // On regarde si premièrement on est pas au début d'un tour
        // Puis si la liste des gardes après avoir lancé ne s'est pas vidé
        if (listeDesGardesBefore.isNotEmpty() && listeDesGardesAfter.isEmpty()) {
            listeJoueurs[joueurActuel].retirerPickomino()
            listeJoueurs[joueurActuel].updateStackTop(sommetsPilesPickominoJoueurs()[joueurActuel])
        }
        return listeDesLances
    }

    @Deprecated("DEBUG")
    fun choisirDes(listDices : List<DICE>): List<DICE> /*DEBUG*/ {
        val joueurActuel = joueurActuel()
        val listeDesGardesBefore = listeDesGardes()
        val listeDesLances = connect.choiceDices(id, key, listDices)
        etatJeu = connect.gameState(id, key)
        val listeDesGardesAfter = listeDesGardes()
        // On regarde si premièrement on est pas au début d'un tour
        // Puis si la liste des gardes après avoir lancé ne s'est pas vidé
        if (listeDesGardesBefore.isNotEmpty() && listeDesGardesAfter.isEmpty()) {
            listeJoueurs[joueurActuel].retirerPickomino()
            listeJoueurs[joueurActuel].updateStackTop(sommetsPilesPickominoJoueurs()[joueurActuel])
        }
        return listeDesLances
    }

    fun garderDes(dice: DICE): Boolean {
        val joueurActuel = joueurActuel()
        if (!connect.keepDices(id, key, dice))
            return false
        etatJeu = connect.gameState(id, key)
        if (listeDesGardes().isEmpty()) {
            listeJoueurs[joueurActuel].retirerPickomino()
        }
        etatJeu = connect.gameState(id, key)
        return true
    }

    fun prendrePickomino(pickomino: Int): Boolean {
        val joueurActuel = joueurActuel()
        val stackTopsBefore = sommetsPilesPickominoJoueurs()
        if (!connect.takePickomino(id, key, pickomino))
            return false
        etatJeu = connect.gameState(id, key)
        for (joueur in stackTopsBefore.indices)
            if (joueur != joueurActuel && stackTopsBefore[joueur] != stackTopJoueur(joueur)) {
                listeJoueurs[joueur].retirerPickomino()
                break
            }
        listeJoueurs[joueurActuel].ajouterPickomino(pickomino)
        listeJoueurs[joueurActuel].updateStackTop(stackTopJoueur(joueurActuel))
        etatJeu = connect.gameState(id, key)
        return true
    }

    private fun stackTopJoueur(joueur : Int) : Int {
        return sommetsPilesPickominoJoueurs()[joueur]
    }

    fun donneNombrePickominoJoueurs(): List<Int> {
        return listeJoueurs.map{it.getNombrePickomino()}
    }

    fun obtenirScoreFinal(): List<Int> {
        return connect.finalScore(id, key)
    }

    fun listeDesGardes() : List<DICE> {
        return etatJeu.current.keptDices
    }

    fun listeDesLances() : List<DICE> {
        return etatJeu.current.rolls
    }

    fun joueurActuel() : Int {
        return etatJeu.current.player
    }

    fun listePickominoAccessible() : List<Int> {
        return etatJeu.accessiblePickos()
    }

    fun sommetsPilesPickominoJoueurs(): List<Int> {
        return etatJeu.pickosStackTops()
    }

    fun donnePickoMaxJoueurs() : List<Int> {
        return listeJoueurs.map{it.donnePickoMax()}
    }

    fun sommeDes(dices : List<DICE>) : Int {
        var somme = 0
        for (dice in dices)
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