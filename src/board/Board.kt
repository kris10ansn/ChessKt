package board

import java.awt.Graphics2D
import game.Game
import game.Move
import pieces.*
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle

class Board(private val game: Game, public val tileSize: Int) {
    val tiles: Array<Array<Tile>> = Array(8) { y ->
        Array(8) { x ->
            Tile(x, y)
        }
    }

    public var whiteKing: King
    public var blackKing: King

    private var whiteKingInCheck = false
    private var blackKingInCheck = false

    init {
        tiles[1].forEachIndexed { x, _ ->
            addPiece(Pawn(x, 1, Alliance.Black, game, this))
        }

        addPiece(Rook(0, 0, Alliance.Black, game, this))
        addPiece(Rook(7, 0, Alliance.Black, game, this))

        addPiece(Knight(1, 0, Alliance.Black, game, this))
        addPiece(Knight(6, 0, Alliance.Black, game, this))

        addPiece(Bishop(2, 0, Alliance.Black, game, this))
        addPiece(Bishop(5, 0, Alliance.Black, game, this))

        addPiece(Queen(3, 0, Alliance.Black, game, this))
        blackKing = King(4, 0, Alliance.Black, game, this)
        addPiece(blackKing)

        tiles[6].forEachIndexed { x, _ ->
            addPiece(Pawn(x, 6, Alliance.White, game, this))
        }

        addPiece(Rook(0, 7, Alliance.White, game, this))
        addPiece(Rook(7, 7, Alliance.White, game, this))

        addPiece(Knight(1, 7, Alliance.White, game, this))
        addPiece(Knight(6, 7, Alliance.White, game, this))

        addPiece(Bishop(2, 7, Alliance.White, game, this))
        addPiece(Bishop(5, 7, Alliance.White, game, this))

        addPiece(Queen(3, 7, Alliance.White, game, this))
        whiteKing = King(4, 7, Alliance.White, game, this)
        addPiece(whiteKing)
    }

    fun render(g: Graphics2D) {
        tiles.forEachIndexed { y, row ->
            row.forEachIndexed { x, tile ->
                g.color = tile.color
                g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize)
            }
        }

        g.color = Color(255, 0, 0, 127)

        if(whiteKingInCheck)
            g.fillOval(whiteKing.x * tileSize, whiteKing.y * tileSize, tileSize, tileSize)

        if(blackKingInCheck)
            g.fillOval(blackKing.x * tileSize, blackKing.y * tileSize, tileSize, tileSize)
    }

    fun renderPieces(g: Graphics2D) {
        tiles.forEach {
            it.forEach { tile ->
                tile.piece?.let {
                    tile.piece!!.render(g)
                }
            }
        }
    }

    fun tick() {
        tiles.forEach {
            it.forEach { tile ->
                tile.piece?.let {
                    tile.piece!!.tick()
                }
            }
        }
    }

    public fun alertMoveAdded(move: Move) {
        whiteKingInCheck = whiteKing.inCheck()
        blackKingInCheck = blackKing.inCheck()
    }

    private fun addPiece(piece: Piece) {
        tiles[piece.y][piece.x].piece = piece
    }

    public fun setPiece(point: Point, piece: Piece?) { setPiece(point.x, point.y, piece); }
    public fun setPiece(x: Int, y: Int, piece: Piece?) {
        tiles[y][x].piece = piece
        piece?.x = x
        piece?.y = y
    }

    public fun getPiece(point: Point): Piece? { return getPiece(point.x, point.y); }
    public fun getPiece(x: Int, y: Int): Piece? {
        return if (Rectangle(0, 0, 8, 8).contains(x, y)) tiles[y][x].piece else null
    }
}
