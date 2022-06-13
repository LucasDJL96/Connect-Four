package connectfour

/** Regex against which to check the input of board dimensions */
val dimensionRegex = """\s*+\d++\s*+[Xx]\s*+\d++\s*+""".toRegex()
/** Regex representing a series of whitespace characters */
val spaces = """\s++""".toRegex()
/** Error message for invalid input */
const val invalidInputMsg = "Invalid input"

/** Main function. Controls the flow of the program */
fun main() {
    println("Connect Four")
    val (playerName1, playerName2) = getPlayers()
    val player1 = Player(playerName1, 'o')
    val player2 = Player(playerName2, '*')
    val (rows, cols) = getDimensions()
    val games = getGames()
    println("${player1.name} VS ${player2.name}")
    println("$rows X $cols board")
    if (games == 1) println("Single game")
    else println("Total $games games")
    val gamePlayers = CyclicPair(CyclicPair(player1, player2), CyclicPair(player2, player1))
    for (i in 1 .. games) {
        if (games != 1) println("Game #$i")
        val board = GameBoard(rows, cols)
        board.printState()
        val players = gamePlayers.current()
        players.reset()
        if (!playGame(players, board)) break
        println("Score")
        println("${player1.name}: ${player1.score} ${player2.name}: ${player2.score}")
        gamePlayers.next()
    }
    println("Game over!")
}

/**
 * Gets the names of the players from the standard input
 *
 * @return Pair<String, String> with the names of the players
 */
private fun getPlayers(): Pair<String, String> {
    println("First player's name:")
    val player1 = readln()
    println("Second player's name:")
    val player2 = readln()
    return Pair(player1, player2)
}

/**
 * Gets the dimensions of the board from the standard input
 *
 * @return Pair<Int, Int> with the number of rows and columns
 */
private fun getDimensions(): Pair<Int, Int> {
    var cols: Int
    var rows: Int
    while (true) {
        rows = 6
        cols = 7
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        val input = readln()
        if (input == "") break
        if (!input.matches(dimensionRegex)) {
            println(invalidInputMsg)
            continue
        }
        val dimensions =
            input.lowercase()
                .replace(spaces, "")
                .split("x")
                .map(String::toInt)
        rows = dimensions[0]
        cols = dimensions[1]
        if (rows !in 5..9) {
            println("Board rows should be from 5 to 9")
        } else if (cols !in 5..9) {
            println("Board columns should be from 5 to 9")
        } else {
            break
        }
    }
    return Pair(rows, cols)
}

/**
 * Gets the number of games to play from the standard input
 *
 * @return Int The number of games to play
 */
private fun getGames(): Int {
    while (true) {
        println("Do you want to play single or multiple games?")
        println("For a single game, input 1 or press Enter")
        println("Input a number of games:")
        val input = readln()
        if (input == "") return 1
        val games = try {
            input.toInt()
        } catch (e: NumberFormatException) {
            println(invalidInputMsg)
            continue
        }
        if (games <= 0) {
            println(invalidInputMsg)
            continue
        }
        return games
    }
}

/**
 * Controls th flow of one game
 *
 * @param playersIterator a CyclicPair<Player> with the players of the game, starting
 * with the one that has the first turn
 * @param board the board of the game
 *
 * @return Boolean whether the game should continue. False only when a player asks
 * for the game to end
 */
private fun playGame(playersIterator: CyclicPair<Player>, board: GameBoard): Boolean {
    while (true) {
        val currentPlayer = playersIterator.current()
        println("${currentPlayer.name}'s turn:")
        val input = readln()
        if (input == "end") {
            return false
        }
        try {
            val col = input.toInt()
            board.addPiece(col, currentPlayer.symbol)
        } catch (e: NumberFormatException) {
            println("Incorrect column number")
            continue
        } catch (e: IllegalArgumentException) {
            println(e.message)
            continue
        }
        board.printState()
        if (board.lastPlayWon()) {
            println("Player ${currentPlayer.name} won")
            currentPlayer.win()
            return true
        }
        if (board.isFull()) {
            println("It is a draw")
            currentPlayer.draw()
            playersIterator.next().draw()
            return true
        }
        playersIterator.next()
    }
}
