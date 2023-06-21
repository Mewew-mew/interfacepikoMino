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

class JeuPickominoTest {
    private val connector = Connector.factory("172.26.82.76", "8080", true)
    private var id = 0
    private var key = 0
    private var nbjoueur = 2

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

    // Fonction de simulation pour les tests parametriques
    private fun simulationPara(liste: MutableList<DICE>, picko: Int) {
        var sum = 0
        var impasse = 0
        while (liste.isNotEmpty() && sum != picko) {
            val des = liste.first()

            connector.choiceDices(id, key, liste)
            connector.keepDices(id, key, des)

            while (liste.contains(des)) {
                liste.remove(des)
                if (des != worm){
                    sum += (des.ordinal +1)
                }else{
                    sum += 5
                    impasse = 1
                }
            }
        }
        if (sum >36){
            sum = 36
        }

        try {
            require(sum == picko)
            require(impasse <= 1)
            connector.takePickomino(id, key, picko)
        } catch (e: BadStepException) {
            print("erreur")
        }
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
        assertTrue( connector.gameState(id, key).accessiblePickos().size in 0..16)
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

    // test qui verfie bien que lorsque qu'on lance des des il les met bien dans keptDices
    @Test
    fun testDesGagner() {
        val diceOptions = listOf(d1, d2, d3, d4, d5, worm)
        val des = diceOptions.random()
        simulation(listOf(d1, worm,d2, d3, d4, d5, d4),des)

        val listeDesDes = connector.gameState(id, key).current.keptDices

        assertTrue(listeDesDes.contains(des))
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

    // test qui permet de voir qu'on change bien de joueur
    @Test
    fun testJoueurSuivant() {
        val joueurDebut= connector.gameState(id,key).current.player

        simulation(listOf(worm,worm,worm,worm,worm,d4, d4),worm)

        connector.takePickomino(id, key, 25)

        val joueurActuel= connector.gameState(id,key).current.player
        assertNotEquals(joueurDebut, joueurActuel)
    }

    // Test qui verifie que le joueur 1 passe son tour quand il fait moins de 21
    @Test
    fun testJoueurMoinsDe21() {
        simulation(listOf(d1, worm,d1,d1,d1,d1,d1, d1),d1)
        simulation( listOf(worm),worm)
        assertTrue(connector.gameState(id,key).current.player == 1)
    }

    // Test qui verifie que le joueur 1 passe son tour quand il fait moins de 21
    @Test
    fun testPertePickoAvecMoinsDe21() {
        simulation(listOf(d1, worm,d1,d1,d1,d1,d1, d1),d1)
        simulation( listOf(worm),worm)
        assertTrue(connector.gameState(id,key).accessiblePickos().last() != 36)
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

    @Test
    fun testNouvelleGame(){
        val a = connector.gameState(id,key)
        connector.newGame(2)
        assertFalse(a==connector.gameState(id,key))
    }

    // test qui permet de verifier que quand la game est fini on peut plus relancer les des
    @Test
    fun testRelance() {
        //joueur 1 et joueur 2 font moins 21
        for (i in 0..15) {
            simulation(List(8){d1}, d1)
        }
        assertThrows<BadStepException> {  simulation(listOf(d1, d1, d1, d1, d1, d1, d1, d1), d1)}
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

    // test qui verifie qu'un peut pas prendre de picko sans lancer de des
    @Test
    fun testTakePickoSansLancerDes() {
        assertThrows<BadStepException>{ connector.takePickomino(id,key,21)}
    }

    // test qui verifie qu'un peut pas prendre de des sans lancer de des
    @Test
    fun testTakeDesSansLancerDes() {
        assertThrows<BadStepException>{ connector.keepDices(id,key,d2)}
    }

    // test qui verifie qu'un peut pas prendre de des qui n'est pas dans la liste
    @Test
    fun testSansLancerDes() {
        simulation(listOf(worm,worm,worm,worm,worm,d4,d4,d4),worm)
        assertThrows<BadStepException>{ connector.keepDices(id,key,d2)}
    }

    // test qui verifie qu'on peut pas prendre de des quand on dois prendre un picko
    @Test
    fun testSansPicko() {
        simulation(listOf(worm,worm,worm,worm,worm,d4,d4,d4),worm)
        connector.takePickomino(id,key,25)
        assertThrows<BadStepException>{ connector.keepDices(id,key,d2)}
    }

    // test qui verifie qu'on peut pas relancer les des lorsqu'on les a tous lancer
    @Test
    fun testRelancerDes() {
        simulation(listOf(d1,d2,d3,d4,d5,worm,worm,worm),worm)
        simulation(listOf(d1,d2,d3,d4,d5),d5)
        simulation(listOf(d1,d2,d3,d4),d4)
        simulation(listOf(d1,d2,d3),d3)
        simulation(listOf(d1,d2),d2)
        simulation(listOf(d1),d1)
        assertThrows<BadStepException>{ connector.keepDices(id,key,d2)}
    }

    // test qui verifie qu'on peut pas prendre 2 picko
    @Test
    fun testPrendre2Picko(){
        simulation(listOf(worm,worm,worm,worm,worm,d4,d4,d4),worm)
        connector.takePickomino(id,key,25)
        assertThrows<BadStepException>{connector.takePickomino(id,key,24) }
    }

    @ParameterizedTest
    @MethodSource("pickoValues")
    fun testSimulationPara(picko: Int, diceList: List<DICE>) {
        val initialPickosStackTops = connector.gameState(id, key).pickosStackTops().toList()
        print(initialPickosStackTops)

        simulationPara(diceList.toMutableList(), picko)

        val finalPickosStackTops = connector.gameState(id, key).pickosStackTops().toList()
        print(finalPickosStackTops)

        assertEquals(picko, finalPickosStackTops.first())
    }

    companion object {
        @JvmStatic
        fun pickoValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(21, listOf(worm, d5, d3, d3, d2, d1, d1,d1)),
                Arguments.of(36, listOf(worm,worm,worm,worm,worm,worm,worm,worm)),
                Arguments.of(25, listOf(worm,d5,d5,d5,d5,d1,d1,d1)),
                Arguments.of(30, listOf(d1, d2, d3, d4, d5, worm , worm ,worm)) ,
                Arguments.of(26, listOf(d1, d1, d2, d3, d4, d5, worm, worm)),
                Arguments.of(22, listOf(d1, d2, d3, d4, d5, d1, d1, worm)),
                Arguments.of(28, listOf(d2, d2, d3, d3, d3, d5, worm, worm)),
                Arguments.of(27, listOf(d4, d4, d4, d4, d4, d1, d1, worm)),
                Arguments.of(31, listOf(d2, d2, d3, d4, d5, worm, d5, d5)),
                Arguments.of(31, listOf(d2, d2, d3, d4, d5, worm, d5, d5)),
                )
        }


    }
}

