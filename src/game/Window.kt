package game

import java.awt.Dimension
import javax.swing.JFrame

class Window(width: Int, height: Int, title: String, game: Game) {
    public val frame = JFrame()

    public val width
        get() = frame.width;
    public val height
        get() = frame.width;

    init {
        frame.title = title;
        val size = Dimension(width, height)
        this.setSize(size)

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)
        frame.isResizable = false

        frame.isVisible = true

        frame.add(game)
        game.start(this)

        // The title bar takes up some of the height, therefore i get the actual window size and
        // add the extra height
        val actualSize = frame.contentPane.size;
        val newSize = Dimension(width + width - actualSize.width, height + height - actualSize.height)
        this.setSize(newSize)
    }

    private fun setSize(size: Dimension) {
        frame.preferredSize = size
        frame.minimumSize = size
        frame.maximumSize = size
    }
}