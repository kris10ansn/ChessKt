package game
import pieces.Piece
import java.awt.Canvas
import java.awt.MouseInfo
import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseListener



class Mouse(private val canvas: Canvas) : MouseListener {
    public var isDown = false
    public var selected: Piece? = null

    public val x
        get() = MouseInfo.getPointerInfo().location.x - canvas.locationOnScreen.x

    public val y
        get() = MouseInfo.getPointerInfo().location.y - canvas.locationOnScreen.y

    public val point
        get() = Point(x, y)

    private val oneTimeEvents = mutableMapOf<MouseEventType, ArrayList<() -> Unit>>()
    private val preservedEvents = mutableMapOf<MouseEventType, ArrayList<() -> Unit>>()

    public fun addEventListener(type: MouseEventType, func: () -> Unit, removeAfterCall: Boolean = true) {
        val events = if (removeAfterCall) oneTimeEvents else preservedEvents

        if(events.containsKey(type)) {
            events[type]?.add(func)
        } else if(!events.containsKey(type)) {
            events[type] = arrayListOf(func)
        }
    }

    override fun mousePressed(e: MouseEvent?) {
        isDown = true

        execEvents(MouseEventType.Pressed)
    }

    override fun mouseReleased(e: MouseEvent?) {
        isDown = false

        execEvents(MouseEventType.Released)
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    private fun execEvents(type: MouseEventType) {
        if(oneTimeEvents.containsKey(type)) {
            oneTimeEvents[type]?.forEach {
                it()
            }
            oneTimeEvents[type]?.clear()
        }
        if(preservedEvents.containsKey(type)) {
            preservedEvents[type]?.forEach {
                it()
            }
        }
    }
}