package connectfour

import kotlin.math.min
import kotlin.math.max

/**
 * Class representing the game board
 *
 * @param rows the number of rows
 * @param cols the number of columns
 */
class GameBoard(private val rows: Int, private val cols: Int) {

    /**
     * A list with the columns of the board, each column containing the characters
     * representing the current pieces in it
     */
    private val columns : List<MutableList<Char>> = List(cols) {
        MutableList(rows) { ' ' }
    }

    /** The last move made on this board. A triple representing (symbol, row, column) */
    private lateinit var lastAdded: Triple<Char, Int, Int>

    /** Prints the current state of the board */
    fun printState() {
        for (j in 0 until cols) print(" ${j + 1}")
        println(" ")
        for (i in 0 until rows) {
            for (j in 0 until cols) print("║${columns[j][i]}")
            println("║")
        }
        print("╚")
        for (j in 0 until cols - 1) print("═╩")
        println("═╝")
    }

    /**
     * Adds a piece wo the board
     *
     * @param _col the number of the column. Starting at 1
     * @param symbol the character to add to the column
     */
    fun addPiece(_col: Int, symbol: Char) {
        val col = _col - 1
        require(col in 0 until cols) {
            "The column number is out of range (1 - $cols)"
        }
        require(checkColumnHasSpace(col)) {
            "Column $_col is full"
        }
        val column = columns[col]
        val lastEmpty = column.lastIndexOf(' ')
        column[lastEmpty] = symbol
        lastAdded = Triple(symbol, lastEmpty, col)
    }

    /**
     * Checks if there's empty space left in a column
     *
     * @param col the column number. Starting at 0
     */
    private fun checkColumnHasSpace(col: Int): Boolean {
        val column = columns[col]
        return column.any { it == ' ' }
    }

    /** Checks if this board is full */
    fun isFull(): Boolean {
        return columns.all { column -> column.all { it != ' ' } }
    }

    /** Checks if the last play made on this board won the game */
    fun lastPlayWon(): Boolean {
        val (symbol, row, col) = lastAdded
        val fullCol = columns[col].toList()
        if (areFourInRow(fullCol, symbol)) return true
        val fullRow = columns.map { it[row] }
        if (areFourInRow(fullRow, symbol)) return true
        val fullDiagonal1 = getDiagonal1(row, col)
        if (areFourInRow(fullDiagonal1, symbol)) return true
        val fullDiagonal2 = getDiagonal2(row, col)
        if (areFourInRow(fullDiagonal2, symbol)) return true
        return false
    }

    /**
     * Checks if there are for identical characters to a given one in a list
     *
     * @param list list with all the characters to check
     * @param symbol the character of which we want to find four in a row
     */
    private fun areFourInRow(list: List<Char>, symbol: Char): Boolean {
        for (i in 0..list.lastIndex - 3) {
            if (list.subList(i, i + 4).all { it == symbol }) return true
        }
        return false
    }

    /**
     * Returns the diagonal from top-left to bottom-right which contains a position
     * on this board
     *
     * @param row the row of the position. Starting at 0
     * @param col the column of the position. Starting at 0
     */
    private fun getDiagonal1(row: Int, col: Int): List<Char> {
        val minCol1 = max(0, col - row)
        val maxCol1 = min(cols, rows + col - row)
        val minRow1 = row - col + minCol1
        return columns.subList(minCol1, maxCol1).mapIndexed { index, column ->
            column[minRow1 + index]
        }
    }

    /**
     * Returns the diagonal from top-right to bottom-left which contains a position
     * on this board
     *
     * @param row the row of the position. Starting at 0
     * @param col the column of the position. Starting at 0
     */
    private fun getDiagonal2(row: Int, col: Int): List<Char> {
        val minCol2 = max(0, col + row + 1 - rows)
        val maxCol2 = min(cols, col + row + 1)
        val minRow2 = row + col + 1 - maxCol2
        return columns.subList(minCol2, maxCol2).reversed().mapIndexed { index, column ->
            column[minRow2 + index]
        }
    }

}
