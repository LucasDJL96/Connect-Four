package connectfour

/**
 * Data class representing a player of the game
 *
 * @param name the name of this player
 * @param symbol the character used for representing the pieces of this player
 * @param score the score of this player
 */
data class Player(val name: String, val symbol: Char, var score: Int = 0) {
    /** Function to be called when the player wins */
    fun win() {
        score += 2
    }

    /** Function to be called when the game ends in a draw */
    fun draw() {
        score++
    }
}
