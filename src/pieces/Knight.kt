package pieces

import board.Board
import game.Game
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.abs

class Knight(x: Int, y: Int, alliance: Alliance, game: Game, board: Board) :
    Piece(x, y, alliance, game, board, "${alliance.string}-Knight") {

    override fun isValidMove(move: Point): Boolean {
        val bounds = Rectangle(0, 0, 8, 8)
        val diff = Point(move.x - x, move.y - y)
        val tilePiece = game.board.getPiece(move.x, move.y)

        var valid =  bounds.contains(move.x, move.y) && (tilePiece == null || tilePiece.alliance != this.alliance)
                && ((abs(diff.x) == 1 && abs(diff.y) == 2) || (abs(diff.y) == 1 && abs(diff.x) == 2))

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
        return arrayOf(
            Point(x + 1, y - 2),
            Point(x + 2, y - 1),
            Point(x + 2, y + 1),
            Point(x + 1, y + 2),
            Point(x - 1, y + 2),
            Point(x - 2, y - 1),
            Point(x - 2, y + 1),
            Point(x - 1, y - 2)
        ).filter(::isValidMove).toTypedArray()
    }

    override fun getCoveredSquares(): Array<Point> {
        return arrayOf(
            Point(x + 1, y - 2),
            Point(x + 2, y - 1),
            Point(x + 2, y + 1),
            Point(x + 1, y + 2),
            Point(x - 1, y + 2),
            Point(x - 2, y - 1),
            Point(x - 2, y + 1),
            Point(x - 1, y - 2)
        )
    }
}