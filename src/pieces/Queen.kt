package pieces

import board.Board
import game.Game
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.abs

class Queen(x: Int, y: Int, alliance: Alliance, game: Game, board: Board) :
    Piece(x, y, alliance, game, board, "${alliance.string}-Queen") {

    override fun isValidMove(move: Point): Boolean {
        val bounds = Rectangle(0, 0, 8, 8)
        val diff = Point(move.x - x, move.y - y)
        val tilePiece = game.board.getPiece(move.x, move.y)

        val isDiagonal = abs(diff.x) == abs(diff.y)
        val isHorizontal = diff.y == 0 && abs(diff.x) > 0
        val isVertical = diff.x == 0 && abs(diff.y) > 0

        val direction = when {
            isDiagonal -> Directions.Diagonal
            isHorizontal -> Directions.Horizontal
            isVertical -> Directions.Vertical
            else -> null
        }

        var valid = bounds.contains(move.x, move.y) && (isDiagonal || isHorizontal || isVertical) &&
                pathEmpty(move, direction!!.obj) && (tilePiece == null || tilePiece.alliance != this.alliance)

        // Check if king is in check after move
        if(valid) {
            val king = if (alliance == Alliance.White) game.board.whiteKing else game.board.blackKing
            val takeBack = this.testMove(move)
            valid = !king.inCheck()
            takeBack()
        }

        return valid
    }

    override fun getValidMoves(): Array<Point> {
        val diagonal = Diagonal.generateAll(x, y).filter(::isValidMove).toTypedArray()
        val horizontal = Horizontal.generateAll(x, y).filter(::isValidMove).toTypedArray()
        val vertical = Vertical.generateAll(x, y).filter(::isValidMove).toTypedArray()

        return arrayOf(*diagonal, *horizontal, *vertical)
    }

    override fun getCoveredSquares(): Array<Point> {
        val diagonal =
            Diagonal.generateAll(x, y).filter { pathEmpty(it, Directions.Diagonal.obj, true) }.toTypedArray()
        val horizontal =
            Horizontal.generateAll(x, y).filter { pathEmpty(it, Directions.Horizontal.obj, true) }.toTypedArray()
        val vertical =
            Vertical.generateAll(x, y).filter { pathEmpty(it, Directions.Vertical.obj, true) }.toTypedArray()

        return arrayOf(*diagonal, *horizontal, *vertical)
    }

    private fun pathEmpty(move: Point, direction: Direction, ignoreEnemyKing: Boolean = false): Boolean {
        for (tile in direction.generateInBetween(x, y, move.x, move.y)) {
            val tilePiece = game.board.getPiece(tile.x, tile.y)

            if (
                tilePiece != null &&
                !(ignoreEnemyKing && tilePiece::class == King::class && tilePiece.alliance != this.alliance)
            ) {
                return false
            }
        }

        return true
    }
}