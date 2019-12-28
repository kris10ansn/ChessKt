package pieces

import board.Board
import game.Game
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.abs

class Bishop(x: Int, y: Int, alliance: Alliance, game: Game, board: Board) :
    Piece(x, y, alliance, game, board, "${alliance.string}-Bishop") {

    override fun isValidMove(move: Point): Boolean {
        val bounds = Rectangle(0, 0, 8, 8)
        val diff = Point(move.x - x, move.y - y)
        val tilePiece = game.board.getPiece(move.x, move.y)

        val isDiagonal = abs(diff.x) == abs(diff.y)

        val pathEmpty = pathEmpty(move) && isDiagonal

        var valid =  bounds.contains(move.x, move.y) && isDiagonal && pathEmpty &&
                (tilePiece == null || tilePiece.alliance != this.alliance)

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
        return Diagonal.generateAll(x, y).filter(::isValidMove).toTypedArray()
    }

    override fun getCoveredSquares(): Array<Point> {
        return Diagonal.generateAll(x, y).filter { pathEmpty(it, ignoreEnemyKing = true) }.toTypedArray()
    }

    private fun pathEmpty(move: Point, ignoreEnemyKing: Boolean = false): Boolean {
        for (tile in Diagonal.generateInBetween(x, y, move.x, move.y)) {
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