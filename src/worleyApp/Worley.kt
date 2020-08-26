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

    lateinit var features: Array<Vec3D>

    val spaceSize = Vec3D(400f, 400f, 200f)
    var zOff: Int = 0
    
    var DRAWDEBUG: Boolean = false
    lateinit var gfx: ToxiclibsSupport

    override fun settings() {
        size(spaceSize.x.toInt(), spaceSize.y.toInt(), P3D)
        smooth(8)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)

        // TODO: Change to perlin noise input or to RGB?
         features = Array(50) {
             //Vec3D((it * 20) % spaceSize.z, (it * 20) % spaceSize.y, (it * 20) % spaceSize.z)
            Vec3D(Random.nextInt(width).toFloat(),
                  Random.nextInt(height).toFloat(),
                  Random.nextInt(spaceSize.z.toInt()).toFloat())
         }


    }

    override fun draw() {
        background(0)

        noise(0)

        if (DRAWDEBUG) debugDraw()

        zOff = frameCount % spaceSize.z.toInt()
        if (frameCount == spaceSize.z.toInt()) {
//           noLoop()
        }
    }

    override fun keyPressed() {
        when (key) {
            'd' -> DRAWDEBUG = !DRAWDEBUG
        }
    }

    private fun noise(order: Int) {
        loadPixels()
        for (x in 0 until width) {
            for (y in 0 until height ) {
                // Worley Noise Algorithm TODO: Octreeify
                val distances = FloatArray(features.size)
                for (i in features.indices) {
                    val p2 = features[i]
                    distances[i] = dist(x.toFloat(), y.toFloat(), zOff.toFloat(), p2.x, p2.y, p2.z)
                }
                distances.sort()

                // Parameterizing colors using the noise computed.
                var rt = distances[order] - (((distances[order+1]) * (distances[order]) / spaceSize.x))
                var gt = distances[order+1] - (((distances[order+2]) * sin(distances[order+1])))
                var bt = distances[order+2] + (((distances[order]) * (distances[order+1]) / spaceSize.z))
                val rtt = rt
                val gtt = gt
                val btt = bt
                rt = (gt * bt) / rt
                gt = (rtt * bt) / gtt
                bt = (gtt * rtt) / btt

                // Map the distance to a color value and update the pixel.
                val r = map(rt, 0f, 255f, 50f, 200f)
                val g = map(gt, 0f, 255f, 0f, 180f)
                val b = map(bt, 0f, 255f, 30f, 210f)

                val c = color(r, g, b)
                pixels[x + y * width] = c
            }
        }

        updatePixels()
//        saveFrame("/seq/###.png")
    }

    fun debugDraw() {
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
            gfx.circle(it.to2DXY(), 5f)
            text(frameRate.toString(), 20f, 20f)
        }
    }
}
