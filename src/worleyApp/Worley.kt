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

    var DRAWMIDLINES: Boolean = false

    override fun settings() {
        val s = Pair(400, 400)
        size(s.first, s.second, P2D)
        zdepth = 400
        smooth(8)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)

         features = Array<Vec3D>(30) {
            Vec3D(Random.nextInt(width).toFloat(),
                Random.nextInt(height).toFloat(),
                Random.nextInt(zdepth).toFloat())
        }

    }

    override fun draw() {
        background(0)
        //worley(2, 0)

        noise(0)
        if (DRAWMIDLINES) drawFeatures()
        zOff = frameCount % zdepth
        if (frameCount == zdepth) {
           // noLoop()
        }
    }

    override fun keyPressed() {
        when (key) {
            'm' -> DRAWMIDLINES = !DRAWMIDLINES
        }
    }

    private fun noise(order: Int) {
        loadPixels()
        for (x in 0 until width) {
            for (y in 0 until height ) {
                val distances = FloatArray(features.size)
                for (i in 0 until features.size) {
                    val p2 = features[i]
                    distances[i] = dist(x.toFloat(), y.toFloat(), zOff.toFloat(), p2.x, p2.y, p2.z)
                }
                distances.sort()
                val rt = distances[order] * ((sin(distances[order+2]) + cos(distances[order])) / 2)
                val gt = distances[order+1] * ((sin(distances[order+1]) + cos(distances[order+2])) / 2)
                val bt = distances[order+2] * ((sin(distances[order]) + cos(distances[order])) / 2)
                val r = map(rt, 0f, 255f, 0f, 200f)
                val g = map(gt, 0f, 255f, 20f, 230f)
                val b = map(bt, 0f, 255f, 100f, 250f)

                pixels[x + y * width] = color(r, g, b)
            }
        }

        updatePixels()
        // saveFrame()
    }

    fun drawFeatures() {
        val drawMidLines = {
            features.forEach { f1 ->
                features.forEach { f2 ->
                    val midpoint = f1.interpolateTo(f2, 0.5f)
                    // val a = Vec2D((f2.x - f1.x), (f2.y - f1.y))
                    val b = Vec2D((f1.y - f2.y), (f2.x - f1.x)).normalize()
                    val d = midpoint.to2DXY().add(b.scale(100f))
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