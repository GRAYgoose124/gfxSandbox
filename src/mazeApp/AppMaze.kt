package mazeApp

import processing.core.PApplet
import toxi.geom.Rect
import toxi.geom.Vec2D
import toxi.processing.ToxiclibsSupport
import java.time.Clock
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime


fun main() { mazeApp.run() }


class mazeApp : PApplet() {
    companion object {
        fun run() {
            val app = mazeApp()
            app.runSketch()
        }
    }

    private lateinit var gfx: ToxiclibsSupport
    private var maze = MazeGenerator.dfs(192, 108)
    private var sx by Delegates.notNull<Float>()
    private var sy by Delegates.notNull<Float>()

    override fun settings() {
        size(1920, 1080, P2D)
        smooth(8)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)
        sx = (this.width / maze.dimx).toFloat()
        sy = (this.height / maze.dimy).toFloat()
    }

    override fun draw() {
        background(255)
        drawmaze()
        //animatePath(maze.path)
    }

    fun drawmaze() {
        stroke(0)
        fill(0)
        for (i in 0 until maze.dimx) {
            for (j in 0 until maze.dimy) {
                val origin = Vec2D(i * sx, j * sy)
                val r = Rect(origin, origin.add(sx, sy))

                if (maze[i][j][4] == '_') {
                    stroke(0)
                    fill(0)
                    gfx.rect(r)
                } else {
                    stroke(0)
                    fill(255)
                    gfx.rect(r)
                }
            }
        }

        maze = MazeGenerator.dfs(384, 216)
        sx = (this.width / maze.dimx).toFloat()
        sy = (this.height / maze.dimy).toFloat()
    }

    fun animatePath(path: MutableList<Pair<Int, Int>>) {
        stroke(0)
        fill(0)

        path.forEach { p ->
            val origin = (Vec2D(p.first.toFloat() * sx, p.second.toFloat() * sx))
            val r = Rect(origin, origin.add(sx, sy))
            gfx.rect(r)
        }
    }

    fun animateFlood(maze: Maze) {

    }

    fun animateAstar(maze: Maze) {

    }
}