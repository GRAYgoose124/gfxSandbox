package worleyApp

import processing.core.PApplet
import toxi.geom.Vec2D
import toxi.geom.Vec3D
import toxi.processing.ToxiclibsSupport
import kotlin.random.Random

import toxi.geom.PointQuadtree
import toxi.geom.Sphere
import kotlin.math.absoluteValue

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

    lateinit var features: Array<Vec3D>

    val spaceSize = Vec3D(1920f, 1080f, 50f)
    var zOff: Int = 0
    
    var DRAWMIDLINES: Boolean = false
    lateinit var gfx: ToxiclibsSupport

    override fun settings() {
        size(spaceSize.x.toInt(), spaceSize.y.toInt(), P3D)
        smooth(8)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)

        // TODO: Change to perlin noise input?
         features = Array<Vec3D>(40) {
            Vec3D(Random.nextInt(width).toFloat(),
                Random.nextInt(height).toFloat(),
                Random.nextInt(spaceSize.z.toInt()).toFloat())
        }

    }

    override fun draw() {
        background(0)
        //worley(2, 0)

        noise(0)
        if (DRAWMIDLINES) drawFeatures()
        zOff = frameCount % spaceSize.z.toInt()
        if (frameCount == spaceSize.z.toInt()) {
           //noLoop()
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
                var rt = distances[order] - (((distances[order+2]) * cos(distances[order])))
                var gt = distances[order+1] - (((distances[order+1]) * sin(distances[order+2])))
                var bt = distances[order+2] - (((distances[order]) * tan(distances[order])))
                val rtt = rt
                val gtt = gt
                rt = (gt + bt) / 2
                gt = (rtt + bt) / 2
                bt = (gtt + rtt) / 2
                val r = map(rt, 0f, 255f, 50f, 200f)
                val g = map(gt, 0f, 255f, 0f, 180f)
                val b = map(bt, 0f, 255f, 30f, 210f)

                val c = color(r, g, b)
                pixels[x + y * width] = c
            }
        }

        updatePixels()
        //saveFrame()
    }

    fun drawFeatures() {
        val drawMidLines = {
            features.forEach { f1 ->
                features.forEach { f2 ->
                    val midpoint = f1.interpolateTo(f2, 0.5f).to2DXY()
                    // val a = Vec2D((f2.x - f1.x), (f2.y - f1.y))
                    val b = Vec2D((f1.y - f2.y), (f2.x - f1.x)).normalize()
                    val d = midpoint.add(b.scale(25f))
                    gfx.line(midpoint, d)
                    // perhaps save list of midpoitns
                    // then midpoint of midpoints
                    // then draw mp-> mmp -> nmp
                }
            }
        }

        stroke(0)
        fill(0f, 255f, 0f)
        drawMidLines()


        fill(0f, 255f, 0f)
        features.forEach { it ->
            if (zOff.toFloat() == it.z) {
                fill(255f, 0f, 0f)
            }
            gfx.circle(Vec2D(it.x, it.y), 5f)
            text(frameRate.toString(), 20f, 20f)
        }
    }
}
