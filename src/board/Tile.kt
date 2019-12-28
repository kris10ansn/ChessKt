package board

import pieces.Piece
import java.awt.Color

class Tile(public val x: Int, public val y: Int) {
    public var piece: Piece? = null
    public val color: Color = arrayOf(Color.darkGray, Color.gray)[(x+y+1) % 2]
}