package worleyApp

import processing.core.PApplet
import toxi.geom.Vec2D
import toxi.geom.Vec3D
import toxi.processing.ToxiclibsSupport
import kotlin.random.Random

//TODO: shadify
//TODO: quadtree

fun main() { Worley.run() }


class Worley : PApplet() { // TODO: functionalize and move to .demo
    companion object {
        fun run() {
            val app = Worley()
            app.runSketch()
        }
    }

    lateinit var gfx: ToxiclibsSupport
    lateinit var dim: Vec3D
    var zdepth: Int = 0
    var zOff: Int = 0

    lateinit var features: Array<Vec3D>

    override fun settings() {
        val s = Pair(400, 400)
        size(s.first, s.second, P2D)
        zdepth = 100
        smooth(15)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)

         features = Array<Vec3D>(7) {
            Vec3D(Random.nextInt(width).toFloat(),
                Random.nextInt(height).toFloat(),
                Random.nextInt(zdepth).toFloat())
        }

    }

    override fun draw() {
        background(0)
        //worley(2, 0)

        noise(0)
        drawFeatures()
        zOff = frameCount % zdepth
    }

    private fun noise(order: Int) {
        loadPixels()
        for (x in 0 until width) {
            for (y in 0 until height ) {
                var distances = FloatArray(features.size)
                for (i in 0 until features.size) {
                    val p2 = features[i]
                    distances[i] = dist(x.toFloat(), y.toFloat(), zOff.toFloat(), p2.x, p2.y, p2.z)
                }
                distances.sort()
                val r = map(distances[order], 0f, 150f, 0f, 255f)
                val g = map(distances[order+1], 0f, 50f, 0f, 255f)
                val b = map(distances[order+2], 0f, 200f, 0f, 200f)

                pixels[x + y * width] = color(r, g, b)
            }
        }

        updatePixels()
    }

    fun drawFeatures() {
        val drawMidLines = {
            features.forEach { f1 ->
                features.forEach { f2 ->
                    val midpoint = f1.interpolateTo(f2, 0.5f)
                    val a = Vec2D((f2.x - f1.x), (f2.y - f1.y))
                    val b = Vec2D((f1.y - f2.y), (f2.x - f1.x))
                    val c = b.normalize()
                    val d = midpoint.to2DXY().add(c.scale(100f))
                    line(midpoint.x, midpoint.y, d.x, d.y)
                    // perhaps save list of midpoitns
                    // then midpoint of midpoints
                    // then draw mp-> mmp -> nmp
                }
            }
        }

        stroke(0)
        fill(0)
        drawMidLines()

        noStroke()
        fill(255f, 0f, 0f)
        features.forEach { circle(it.x, it.y, 5f) }
    }
}