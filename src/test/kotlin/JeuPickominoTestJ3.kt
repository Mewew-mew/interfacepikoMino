package modele

import org.junit.jupiter.api.Test
import iut.info1.pickomino.Connector
import iut.info1.pickomino.data.*
import iut.info1.pickomino.data.DICE.*
import iut.info1.pickomino.data.STATUS
import iut.info1.pickomino.exceptions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class JeuPickominoTestJ3 {
    private val connector = Connector.factory("172.26.82.76", "8080", true)
    private var id = 0
    private var key = 0
    private var nbjoueur = 3

    @BeforeEach
    fun setup() {
        val identification = connector.newGame(nbjoueur)
        id = identification.first
        key = identification.second
    }

    // Fonction de simulation pour choisir et garder des dés
    private fun simulation(liste: List<DICE>, des: DICE) {
        connector.choiceDices(id, key, liste) // Choix des dés à partir de la liste
        connector.keepDices(id, key, des) // Conservation du dé spécifié
    }


    @Test
    fun testSimulation() {
        //Joueur 1
        simulation(listOf(worm,worm,worm,worm,worm,worm, d4),worm)
        connector.takePickomino(id, key, 30)

        //Joueur 2
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        //Joueur 3
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        simulation(listOf(d1,d4, d4),d1)
        connector.takePickomino(id, key, 26)

        val pickosStackTops = connector.gameState(id, key).pickosStackTops()
        assertTrue(pickosStackTops[0] == 30)
        assertTrue(pickosStackTops[1] == 25)
        assertTrue(pickosStackTops[2] == 26)

    }

    // Test qui verifie que le joueur 2 perd un picko quand le joueur 3 prend son picko
    @Test
    fun testJoueurPertePicko() {
        //Joueur 1
        simulation(listOf(worm,worm,worm,worm,worm,worm, d4),worm)
        connector.takePickomino(id, key, 30)

        //Joueur 2
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        //Joueur 3
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        val pickosStackTops = connector.gameState(id, key).pickosStackTops()
        assertFalse(pickosStackTops[1] == 25)
    }

    // Test qui verifie que le joueur 3 prend bien le picko chez son adversaire
    @Test
    fun testJoueurSuivantPrendPicko() {
        //Joueur 1
        simulation(listOf(worm,worm,worm,worm,worm,worm, d4),worm)
        connector.takePickomino(id, key, 30)

        //Joueur 2
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        //Joueur 3
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        val pickosStackTops = connector.gameState(id, key).pickosStackTops()
        assertTrue(pickosStackTops[2] == 25)
    }

    // Test qui verifie que le joueur 1 passe son tour quand il fait moins de 21 avec le picko 36 dans sa liste
    @Test
    fun testJoueurMoinsDe21avecPickoDansLaListe36() {
        val pickosInitial = connector.gameState(id,key).accessiblePickos()

        // Joueur 1
        simulation(List(8){worm},worm)
        connector.takePickomino(id,key,36)

        // Joueur 2
        simulation( listOf(d4, d4, d4,d4,d4,d4, d4,worm),worm)
        simulation(listOf(d4, d4, d4,d4,d4,d4, d4),d4)
        connector.takePickomino(id,key,33)

        // Joueur 3
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)
        val pickoJ1 = connector.gameState(id, key).pickosStackTops()

        // Joueur 1
        simulation( listOf(d1, worm,d1,d1,d1,d1,d1, d1),d1)
        simulation( listOf(worm),worm)

        val pickosFinal = connector.gameState(id,key).accessiblePickos()
        val pickoJ1update = connector.gameState(id, key).pickosStackTops()

        assertFalse(!pickosFinal.contains(pickosInitial.last())) //
        assertTrue(connector.gameState(id,key).current.player == 1)
        assertTrue(pickoJ1update[0]!=pickoJ1[0])
    }

    // Test qui verifie que le joueur 1 passe son tour quand il fait moins de 21 avec un picko dans sa liste
    @Test
    fun testJoueurMoinsDe21avecPickoDansLaListe() {
        // Joueur 1
        simulation(listOf(d4, d4, d4,d4,d4,d4, worm,worm),worm)

        simulation(listOf(d4, d4, d4,d4,d4,d4),d4)
        connector.takePickomino(id,key,34)


        // Joueur 2
        simulation( listOf(d4, d4, d4,d4,d4,d4, d4,worm),worm)
        simulation(listOf(d4, d4, d4,d4,d4,d4, d4),d4)
        connector.takePickomino(id,key,33)

        // Joueur 3
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)
        val pickoJ1 = connector.gameState(id, key).pickosStackTops()

        // Joueur 1
        simulation(listOf(d1, worm,d1,d1,d1,d1,d1, d1),d1)
        simulation(listOf(worm),worm)

        val pickoJ1update = connector.gameState(id, key).pickosStackTops()

        assertTrue(connector.gameState(id,key).current.player == 1)
        assertTrue(pickoJ1update[0]!=pickoJ1[0])
    }

    // Test qui verifie que le plus grand picko s'enleve quand le joueur fait moins de 21 avec une pickos dans sa liste autre que 36
    @Test
    fun testJoueurMoinsDe21avecPerteduPlusGrandPicko() {
        val pickosInitial = connector.gameState(id,key).accessiblePickos()

        // Joueur 1
        simulation(listOf(d4, d4, d4,d4,d4,d4, worm,worm),worm)

        simulation(listOf(d4, d4, d4,d4,d4,d4),d4)
        connector.takePickomino(id,key,34)

        // Joueur 2
        simulation( listOf(d4, d4, d4,d4,d4,d4, d4,worm),worm)
        simulation(listOf(d4, d4, d4,d4,d4,d4, d4),d4)
        connector.takePickomino(id,key,33)

        // Joueur 3
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)
        val pickoJ1 = connector.gameState(id, key).pickosStackTops()

        // Joueur 1
        simulation(listOf(d1, worm,d1,d1,d1,d1,d1, d1),d1)
        simulation(listOf(worm),worm)

        val pickosFinal = connector.gameState(id,key).accessiblePickos()
        val pickoJ1update = connector.gameState(id, key).pickosStackTops()

        assertTrue(!pickosFinal.contains(pickosInitial.last()))
        assertTrue(pickoJ1update[0]!=pickoJ1[0])
    }

    // test qui verifie bien que la fin de game se lance avec 3 joueur
    @Test
    fun testFinDeGame() {
        //joueur 1, joueur 2 et joueur 3 font moins de 21 pour enlever les pickos
        for (i in 0..14) {
            simulation(listOf(d1, d1, d1, d1, d1, d1, d1, d1), d1)
        }
        // Joueur 2
        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d1, d2, d2, d2, d2), d4)
        simulation(List(6){d2}, d2)
        connector.takePickomino(id, key, 21)

        assertTrue(connector.gameState(id,key).current.status == STATUS.GAME_FINISHED)
        assertTrue(connector.finalScore(id, key) == listOf(1,0,0))
    }

    // Test qui verifie que lorsque une personne prend un picko qui n'est pas disponoble ici le 21
    @Test
    fun testJoueurPickos21() {
        //Joueur 1
        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d1, d2, d2, d2, d2), d4)
        simulation(List(6){d2}, d2)
        connector.takePickomino(id, key, 21)

        // Joueur 2
        simulation( listOf(d4, d4, d4,d4,d4,d4, d4,worm),worm)
        simulation(listOf(d4, d4, d4,d4,d4,d4, d4),d4)
        connector.takePickomino(id,key,33)

        // Joueur 3
        simulation(List(8){worm},worm)
        connector.takePickomino(id,key,36)

        //Joueur 1
        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d1, d2, d2, d2, d2), d4)
        simulation(List(6){d3}, d3)
        connector.takePickomino(id, key, 27)

        //Joueur 2
        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d4, d4, d4, d4, d4), d4)
        connector.takePickomino(id, key, 29)

        //Joueur 3
        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d1, d2, d2, d2, d2), d4)
        simulation(List(6){d2}, d2)
        assertThrows<BadStepException> { connector.takePickomino(id, key, 21) }
    }


}