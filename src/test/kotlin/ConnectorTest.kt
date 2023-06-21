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

class ConnectorTest {
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

    @Test
    fun testNewGame1() {
        assertEquals(Pair(-1, -1), connector.newGame(-3))
    }

    @Test
    fun testNewGame2() {
        assertEquals(Pair(-1, -1), connector.newGame(5))
    }

    @Test
    fun testNewGame3() {
        assertNotEquals(Pair(-1, -1), connector.newGame(3))
    }

    @Test
    fun testRollDices1(){
        assertThrows<UnknownIdException>{connector.rollDices(-1,0)}
    }
    @Test
    fun testRollDices2(){
        assertThrows<IncorrectKeyException>{connector.rollDices(id,key-1)}
    }
    @Test
    fun testRollDices3(){
        connector.rollDices(id,key)
        assertThrows<BadStepException>{connector.rollDices(id,key)}
    }
    @Test
    fun testRollDices4(){
        assertDoesNotThrow{connector.rollDices(id,key)}
    }

    @Test
    fun testChoiceDices1(){
        assertThrows<UnknownIdException>{connector.choiceDices(-1,0,List(8){DICE.d1})}
    }
    @Test
    fun testChoiceDices2(){
        assertThrows<IncorrectKeyException>{connector.choiceDices(id,key-1,List(8){DICE.d1})}
    }
    @Test
    fun testChoiceDices3(){
        connector.rollDices(id,key)
        assertThrows<BadStepException>{connector.choiceDices(id,key,List(8){DICE.d1})}
    }
    @Test
    fun testChoiceDices4(){
        val listeDes=List(8){DICE.d1}
        var listeSortie= listOf<DICE>()
        assertDoesNotThrow {listeSortie=connector.choiceDices(id,key,listeDes)}
        assertEquals(listeDes,listeSortie)
    }


    @Test
    fun testFinalScore1(){
        assertThrows<UnknownIdException>{connector.finalScore(-1,0)}
    }
    @Test
    fun testFinalScore2(){
        assertThrows<IncorrectKeyException>{connector.finalScore(id,key-1)}
    }
    @Test
    fun testFinalScore3(){
        assertThrows<BadStepException>{connector.finalScore(id,key)}
    }
    @Test
    fun testFinalScore4(){
        for (i in 0..15){
            connector.choiceDices(id,key, List(8){DICE.d1})
            connector.keepDices(id,key,DICE.d1)
        }
        assertDoesNotThrow { connector.finalScore(id,key)}
    }

    @Test
    fun testGameState1(){
        assertThrows<UnknownIdException>{connector.gameState(-1,0)}
    }
    @Test
    fun testGameState2(){
        assertThrows<IncorrectKeyException>{connector.gameState(id,key-1)}
    }
    @Test
    fun testGameState3(){
        assertDoesNotThrow{connector.gameState(id,key)}
    }

    @Test
    fun testKeepDices1(){
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        assertThrows<UnknownIdException>{connector.keepDices(-1,0,DICE.worm)}
    }
    @Test
    fun testKeepDices2(){
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        assertThrows<IncorrectKeyException>{connector.keepDices(id,key-1,DICE.worm)}
    }
    @Test
    fun testKeepDices3(){
        assertThrows<BadStepException>{connector.keepDices(id,key,DICE.worm)}
    }
    @Test
    fun testKeepDices4(){
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        assertThrows<DiceNotInRollException>{connector.keepDices(id,key,DICE.d1)}
    }
    @Test
    fun testKeepDices5(){
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.d1, DICE.d1, DICE.d1, DICE.d1, DICE.d1, DICE.d1, DICE.d1)
        )
        connector.keepDices(id,key,DICE.worm)
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.d1)
        )
        assertThrows<DiceAlreadyKeptException>{connector.keepDices(id,key,DICE.worm)}
    }

    @Test
    fun testKeepDices6(){
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.d1, DICE.d1, DICE.d1, DICE.d1, DICE.d1, DICE.d1, DICE.d1)
        )
        connector.keepDices(id,key,DICE.worm)
        connector.choiceDices(
            id, key,
            listOf(DICE.d1, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        assertTrue (connector.keepDices(id,key,DICE.d1))
    }

    //Test correpondant à l'Issue #16, erreur potentielle de la librairie
    /*@Test
    fun testTakePickomino1() {
        assertThrows<UnknownIdException> { connector.takePickomino(-1, 0, 0) }
    }

    @Test
    fun testTakePickomino2() {
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        connector.keepDices(id, key, DICE.worm)
        //connector.keepDices(id, key-1, DICE.worm) produit une erreur
        assertThrows<IncorrectKeyException>{connector.takePickomino(id, key-1, 0) }
    }
    @Test
    fun testTakePickomino3() {
        assertThrows<BadStepException> { connector.takePickomino(id, key, 0) }
    }*/

    @Test
    fun testTakePickomino4() {
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        connector.keepDices(id, key, DICE.worm)
        val etat=connector.gameState(id,key).current.status
        assertFalse(connector.takePickomino(id, key, 0))
        assertEquals(etat,connector.gameState(id,key).current.status)
    }
    @Test
    fun testTakePickomino5() {
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        connector.keepDices(id, key, DICE.worm)
        val etat=connector.gameState(id,key).current.status
        assertFalse(connector.takePickomino(id, key, 40))
        assertEquals(etat,connector.gameState(id,key).current.status)
    }
    @Test
    fun testTakePickomino6() {
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        connector.keepDices(id, key, DICE.worm)
        assertThrows<BadPickominoChosenException> { connector.takePickomino(id, key, 21) }
    }
    @Test
    fun testTakePickomino7() {
        connector.choiceDices(
            id, key,
            listOf(DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm, DICE.worm)
        )
        connector.keepDices(id, key, DICE.worm)
        val etat=connector.gameState(id,key).current.status
        assertTrue(connector.takePickomino(id, key, 36))
        assertNotEquals(etat,connector.gameState(id,key).current.status)
    }
}

