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

class JeuPickominoTest {
    private val connector = Connector.factory("172.26.82.76", "8080", true)
    private var id = 0
    private var key = 0
    private lateinit var jeuPickomino: JeuPickomino
    private var nbjoueur = 2

    @BeforeEach
    fun setup() {
        val identification = connector.newGame(nbjoueur)
        id = identification.first
        key = identification.second
        jeuPickomino = JeuPickomino(nbjoueur)
    }

    // Fonction de simulation pour choisir et garder des dés
    private fun simulation(liste: List<DICE>, des: DICE) {
        val dicesToChoose = liste // Liste des dés à choisir
        connector.choiceDices(id, key, dicesToChoose) // Choix des dés à partir de la liste
        connector.keepDices(id, key, des) // Conservation du dé spécifié
    }

    // test clé et id du connector
    @Test
    fun testGoodKeyId(){
        assertNotNull(connector.gameState(id,key))
    }

    // test clé du connector mauvaise
    @Test
    fun testWrongKey(){
        assertThrows<IncorrectKeyException> {connector.gameState(id,0) }
    }


    // test id du connector mauvaise
    @Test
    fun testWrongId(){
        assertThrows<UnknownIdException> {connector.gameState(0,key) }
    }

    // test qui verifie que la liste de pickos est bien valide
    @Test
    fun testListePickominoAccessible() {
        val listePickominoAccessible = jeuPickomino.listePickominoAccessible()
        val expectedList = connector.gameState(id,key).accessiblePickos()
        assertEquals(expectedList, listePickominoAccessible)
    }

    // Test qui verifie que les dés sois bien valide
    @Test
    fun testLancerDes() {
        assertTrue(connector.rollDices(id, key).all{it is DICE})
    }

    // Test qui verifie que la liste des dés sois bien valide
    @Test
    fun testListeLancerDes() {
        assertTrue(connector.rollDices(id, key).size in 0..8)
    }

    // test qui verfie bien que les des sois bien lancé et qui enleve bien le des qui sont génére de facon random dans la liste de des possible
    @Test
    fun testSimulationChoiceDices() {
        val diceOptions = listOf(d1, d2, d3, d4, d5, worm)
        val des = diceOptions.random()
        simulation(listOf(d1, worm,d2, d3, d4, d5, d4),des)

        val finalEmptyDiceList = connector.gameState(id, key).current.keptDices

        assertTrue(finalEmptyDiceList.contains(des))

    }

    // test qui verfie bien que les des sois bien lancé et qui enleve bien le des qui sont génére de facon random dans la liste de des possible
    @Test
    fun tesDesgarder() {
        val diceOptions = listOf(d1, d2, d3, d4, d5, worm)
        val des = diceOptions.random()
        simulation(listOf(d1, worm,d2, d3, d4, d5, d4),des)

        val listeDesDes = connector.gameState(id, key).current.keptDices

        assertTrue(listeDesDes.contains(des))
    }

    // test qui verfie bien que tout les des sois bien lancé et qui enleve bien le des qui sont génére de facon random dans la liste de des possible
    @Test
    fun tesDesgarderMultiple() {

        var diceOptions = listOf(d1, d2, d3, d4, d5, worm)
        var tab = listOf(d1, worm,d2, d3, d4, d5, d4)

        for (i in connector.gameState(id, key).current.rolls.indices) {
            val des = diceOptions.random()
            simulation(tab, des)
            tab = tab.filterNot { it == des }
            diceOptions = diceOptions.filterNot { it == des }
        }

        assertTrue(connector.gameState(id, key).current.rolls.isEmpty())
    }

    // test qui verfie bien que lorsque qu'on lance des des il les enleve bien de la liste roll
    @Test
    fun tesDesPerdu() {
        val diceOptions = listOf(d1, d2, d3, d4, d5, worm)
        val des = diceOptions.random()
        simulation(listOf(d1, worm,d2, d3, d4, d5, d4),des)

        val listeDesDes = connector.gameState(id, key).current.rolls

        assertTrue(!listeDesDes.contains(des))
    }

    //test qui permet de voir qu'un joueur a bien pris un picko dans la liste
    @Test
    fun testSimulationTakePickomino1() {
        val initial = connector.gameState(id, key).accessiblePickos()
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4), worm)
        connector.takePickomino(id, key, 25)
        val fin = connector.gameState(id, key).accessiblePickos()
        assertNotEquals(initial, fin)
    }

    //test qui permet de voir qu'un pickos n'est plus dans la liste de pickos lorsqu'il a été pris
    @Test
    fun testSimulationTakePikoValeur() {
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        val fin = connector.gameState(id,key).accessiblePickos()
        assertFalse(fin.contains(25))
    }

    @Test
    fun testSimulationTakePickomino2() {
        val initial = connector.gameState(id, key).accessiblePickos()
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4), worm)
        simulation(listOf(d1, d2, d3, d4, d5), d1)
        connector.takePickomino(id, key, 26)
        val fin = connector.gameState(id, key).accessiblePickos()
        assertNotEquals(initial, fin)
        assertFalse(fin.contains(26))
    }

    @Test
    fun testSimulationTakePickomino3() {
        val initial = connector.gameState(id, key).accessiblePickos()
        simulation(listOf(worm,worm,worm,worm,worm,worm,worm,d1), worm)
        connector.takePickomino(id, key, 35)
        val fin = connector.gameState(id, key).accessiblePickos()
        assertNotEquals(initial, fin)
        assertFalse(fin.contains(35))
    }

    //test qui permet de voir qu'un joueur a bien pris un picko avec plusieurs tirage
    @Test
    fun testSimulationChoiceDicesPlusieursEtapes() {
        val initial = connector.gameState(id,key).accessiblePickos()

        simulation(listOf(d4, worm,d2,d2,d2,d4,d4),d2)
        simulation(listOf(d4, worm,d4,d4),d4)
        simulation(listOf(worm),worm)
        connector.takePickomino(id,key,23)

        val apressim = connector.gameState(id,key).accessiblePickos()
        assertNotEquals(initial, apressim)
    }

    //test qui permet de voir qu'un joueur ne peut pas prendre un picko qui a la mauvaise valeur
    @Test
    fun testSimulationMauvaisPicko() {
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        assertThrows<BadPickominoChosenException>{connector.takePickomino(id, key, 22) }
    }

    //test qui permet de voir qu'un joueur ne peut pas prendre un des qu'il a deja pris
    @Test
    fun testSimulationDesDejaPris() {
        simulation(listOf(d1, d1, d1, d1,d3,d4 , d5, worm),d1)

        val dicesToChoose = listOf(d2,d3,d4 ,worm)
        connector.choiceDices(id,key,dicesToChoose)
        assertThrows<DiceAlreadyKeptException> {connector.keepDices(id,key,d1)
        }
    }

    //Test permettant de voir qu'on ne peut pas prendre un des lorsqu'il est pas dans la liste
    @Test
    fun testSimulationDesIncorrect() {
        val dicesToChoose = listOf(d1, d1, d1, d1,d3,d4 , d5, worm)
        connector.choiceDices(id,key,dicesToChoose)
        assertThrows<DiceNotInRollException> { connector.keepDices(id,key,d2)
        }
    }

    //Test permettant de voir qu'on ne peut pas prendre un picko qui etais deja prenable lorsque les des ne sont plus validés
    @Test
    fun testSimulationDesIncorrect2() {
        simulation(listOf(worm,worm,worm,worm,worm,d4 , d5, d1),worm)

        val dicesToChoose = listOf(worm , worm,worm)
        connector.choiceDices(id,key,dicesToChoose)

        assertThrows<BadStepException> { connector.takePickomino(id,key,25)}
        }

    // test qui verifie qu'on peut pas prendre de picko sans worm
    @Test
    fun testvaleurCorrectMaisSansWorm(){
        simulation(listOf(d5, d5, d5, d5,d5,d4 , d3, d1),d5)
        assertThrows<BadStepException> { connector.takePickomino(id,key,25)}

    }

    // test qui permet de voir qu'on change bien de joueur
    @Test
    fun testJoueurSuivant() {
        val joueurDebut= connector.gameState(id,key).current.player

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)

        connector.takePickomino(id, key, 25)

        val joueurActuel= connector.gameState(id,key).current.player
        assertNotEquals(joueurDebut, joueurActuel)
    }

    // Test qui verifie que le joueur 1 perd un picko quand le joueur 2 prend son picko
    @Test
    fun testJoueurPertePicko() {

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)

        connector.takePickomino(id, key, 25)

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        val pickosStackTops = connector.gameState(id, key).pickosStackTops()
        assertFalse(pickosStackTops[0] == 25)
    }

    // Test qui verifie que le joueur 2 prend bien le picko chez son adversaire
    @Test
    fun testJoueurSuivantPrendPicko() {

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)

        connector.takePickomino(id, key, 25)

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        val pickosStackTops = connector.gameState(id, key).pickosStackTops()
        assertTrue(pickosStackTops[1] == 25)
    }


    // Test qui verifie que le joueur 2 prend bien le picko en dessous de la valeur qu'il a tirer dans la liste
    @Test
    fun testJoueurSuivantPrendPickoplusfaible() {
        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 24)

        val pickosStackTops = connector.gameState(id, key).pickosStackTops()
        assertTrue(pickosStackTops[0] == 25)
        assertTrue(pickosStackTops[1] == 24)
    }

    // Test qui verifie que le joueur 1 prend bien le picko en dessous de la valeur qu'il a tirer dans la liste malgré quil y a un ecart
    @Test
    fun testJoueurSuivantPrendPickoplusfaible2() {

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        connector.takePickomino(id, key, 25)

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)
        simulation(listOf(d1, d4),d1)
        connector.takePickomino(id, key, 26)

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)

        simulation(listOf(d1, d4),d1)
        connector.takePickomino(id, key, 24)

        val pickosStackTops = connector.gameState(id, key).pickosStackTops()
        assertTrue(pickosStackTops[0] == 24)
        assertTrue(pickosStackTops[1] == 26)
    }

    // Test qui verifie que le joueur 1 passe son tour quand il fait moins de 21
    @Test
    fun testJoueurMoinsDe21() {
        simulation(listOf(d1, worm,d1,d1,d1,d1,d1, d1),d1)
        simulation( listOf(worm),worm)
        assertTrue(connector.gameState(id,key).current.player == 1)
    }

    // Test qui verifie que le joueur 1 passe son tour quand il fait moins de 21 avec un picko dans se liste avec le picko 36
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
        simulation(listOf(d4, d4, d4,d4,d4,d4, d4,worm),worm)
        simulation(listOf(d4, d4, d4,d4,d4,d4, d4),d4)
        connector.takePickomino(id,key,33)

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
        simulation(listOf(d4, d4, d4,d4,d4,d4, d4,worm),worm)
        simulation(listOf(d4, d4, d4,d4,d4,d4, d4),d4)
        connector.takePickomino(id,key,33)

        val pickoJ1 = connector.gameState(id, key).pickosStackTops()

        // Joueur 1
        simulation(listOf(d1, worm,d1,d1,d1,d1,d1, d1),d1)
        simulation(listOf(worm),worm)

        val pickosFinal = connector.gameState(id,key).accessiblePickos()
        val pickoJ1update = connector.gameState(id, key).pickosStackTops()

        assertTrue(!pickosFinal.contains(pickosInitial.last()))
        assertTrue(pickoJ1update[0]!=pickoJ1[0])
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

        // Joueur 1
        simulation(List(8){worm},worm)
        connector.takePickomino(id,key,36)

        //Joueur 2
        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d1, d2, d2, d2, d2), d4)
        simulation(List(6){d2}, d2)
        assertThrows<BadStepException> { connector.takePickomino(id, key, 21) }

    }

    // test qui verifie bien que la fin de game se lance
    @Test
    fun testFinDeGame() {
        //joueur 1 et joueur 2 font moins de 21 pour enlever les pickos
        for (i in 0..14) {
            simulation(listOf(d1, d1, d1, d1, d1, d1, d1, d1), d1)
        }
        // Joueur 2
        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d1, d2, d2, d2, d2), d4)
        simulation(List(6){d2}, d2)
        connector.takePickomino(id, key, 21)

        assertTrue(connector.gameState(id,key).current.status == STATUS.GAME_FINISHED)
        assertTrue(connector.finalScore(id, key) == listOf(0, 1))
    }

    //test qui verifie bien que le statut ce met à jour lorsque on peut prendre un picko
    @Test
    fun testJeuStatus() {
        val initial =  connector.gameState(id, key).current.status

        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d1, d2, d2, d2, d2), d4)
        simulation(List(6){d2}, d2)
        connector.gameState(id, key).current.status

        val status2 = connector.gameState(id, key).current.status
        connector.takePickomino(id,key,21)

        assertTrue(initial!=status2)
    }

    //test qui verifie bien que le statut ce met à jour lorsque on peut prendre un picko ou roll a nouveau
    @Test
    fun testJeuStatusTest() {
        val initial =  connector.gameState(id, key).current.status
        simulation(listOf(worm,d5, worm,worm,worm,worm,d2, d2), worm)
        connector.gameState(id, key).current.status
        val status2 = connector.gameState(id, key).current.status
        assertTrue(initial!=status2)
    }

    //test qui verifie bien que le statut ce met à jour en fonction de la simulation des qu'on passe à un autre joueur
    @Test
    fun testJeuStatusChangementDeJoueur() {
        val initial =  connector.gameState(id, key).current.status

        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d2, d1, d2, d2, d2, d2), d4)
        simulation(List(6){d2}, d2)
        connector.gameState(id, key).current.status
        connector.takePickomino(id,key,21)

        val status = connector.gameState(id, key).current.status
        assertTrue(initial==status)
    }

    // test qui permet de voir si les point s'acutualise bien pour un picko de valeur 4 pt
    @Test
    fun testPointPicko() {
        simulation(List(8){worm}, worm)
        connector.takePickomino(id,key,36)
        assertEquals( listOf(4,0),connector.gameState(id,key).score())
    }

    // test qui permet de voir si les point s'acutualise bien pour un picko de valeur 3 pt
    @Test
    fun testPointPicko2() {
        simulation(listOf(worm,worm,worm,worm,worm,d4,d2,d2), worm)
        simulation(listOf(d4,d2,d2), d4)
        connector.takePickomino(id,key,29)
        assertEquals( listOf(3,0),connector.gameState(id,key).score())
    }

    // test qui permet de voir si les point s'acutualise bien pour un picko de valeur 2 pt
    @Test
    fun testPointPicko3() {
        simulation(listOf(worm,worm,worm,worm,worm,d1,d1,d1), worm)
        connector.takePickomino(id,key,25)
        assertEquals( listOf(2,0),connector.gameState(id,key).score())
    }

    // test qui permet de voir si les point s'acutualise bien pour un picko de valeur 1 pt
    @Test
    fun testPointPicko4() {
        simulation(listOf(worm,d5, d2, d1, d2, d2, d2, d2), worm)
        simulation(listOf(d4, d1) + List(5){d2}, d4)
        simulation(List(6){d2}, d2)
        connector.takePickomino(id,key,21)
        assertEquals(listOf(1,0),connector.gameState(id,key).score())
    }


    // test qui permet de voir si les point s'adittionne
    @Test
    fun testPointPickoAddition() {
        simulation(List(8){worm}, worm)
        connector.takePickomino(id,key,36)

        simulation(List(8){d2}, d2)

        simulation(listOf(worm,worm,worm,worm,worm,d1,d1,d1), worm)
        connector.takePickomino(id,key,25)

        assertEquals(listOf(6,0),connector.gameState(id,key).score())
    }


    // test qui permet de verifier que quand la game est fini on peut plus relancer les des
    @Test
    fun testRelancé() {
        //joueur 1 et joueur 2 font moins 21
        for (i in 0..15) {
            simulation(List(8){d1}, d1)
        }
        assertThrows<BadStepException> {  simulation(listOf(d1, d1, d1, d1, d1, d1, d1, d1), d1)}
    }


}


