package game

import pieces.Piece

class Move(val x: Int, val y: Int, val dx: Int, val dy: Int, val piece: Piece) {
    override fun toString(): String {
        return "Move[x: $x, y: $y, dx: $dx, dy: $dy]"
    }
}