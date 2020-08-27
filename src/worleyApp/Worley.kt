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
    val spaceSize = Vec3D(1280f, 720f, 400f)
    var zOff: Int = 0
    
    var bDRAWDEBUG: Boolean = false
    var bSAVESEQ: Boolean = true

    lateinit var gfx: ToxiclibsSupport

    override fun settings() {
        size(spaceSize.x.toInt(), spaceSize.y.toInt(), P3D)
        smooth(8)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)

        // TODO: Change to perlin noise input or to RGB?
         features = Array(30) {
             //Vec3D((it * 20) % spaceSize.z, (it * 20) % spaceSize.y, (it * 20) % spaceSize.z)
            Vec3D(Random.nextInt(width).toFloat(),
                  Random.nextInt(height).toFloat(),
                  Random.nextInt(spaceSize.z.toInt()).toFloat())
         }


    }

    override fun draw() {
        background(0)

        worleyNoise(0, bSAVESEQ)

        if (bDRAWDEBUG) debugDraw()

        zOff = frameCount % spaceSize.z.toInt()
        if (frameCount == spaceSize.z.toInt() && bSAVESEQ) {
            noLoop()
        }
    }

    override fun keyPressed() {
        when (key) {
            'd' -> bDRAWDEBUG = !bDRAWDEBUG
            's' -> {
                zOff = 0
                bSAVESEQ = !bSAVESEQ
            }
         }
    }

    private fun worleyNoise(order: Int, save: Boolean=false) {

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
                var rt: Float = (distances[order] * cos(distances[order+1] * 0.05f))
                var gt: Float = (distances[order] * sin(distances[order+2] * 0.05f))
                var bt: Float = (distances[order] * cos(distances[order+3] * 0.05f))

                val rtt = rt
                val gtt = gt
                val btt = bt

                var qo = 1
                var ro = 2
                var so = 3
                var t: Int
                var t2: Int
                for (i in 0 until distances.size-3) {
                    rt += (distances[i] * sin(distances[i+qo] * 0.05f))
                    bt += (distances[i] * cos(distances[i+ro]))
                    gt += (distances[i] * sin(distances[i+so] * 0.3f))

                    t = so
                    so = qo
                    t2 = qo
                    qo = t
                    ro = t2

                }
                rt /= distances.size
                gt /= distances.size
                bt /= distances.size


                rt = rtt * bt / (rt)
                gt = gtt * rt / (gt)
                bt = btt * gt / (bt)
//              rt = sin(gtt * btt * .001f) * rtt
//              gt = sin(rtt * btt * .01f) * gtt
//              bt = sin(gtt * rtt * .01f) * btt

                // Map the distance to a color value and update the pixel.
                val r = map(rt, 0f, 255f, 0f, 255f)
                val g = map(gt, 0f, 255f, 0f, 255f)
                val b = map(bt, 0f, 255f, 0f, 255f)

                val c = color(r, g, b)
                pixels[x + y * width] = c
            }
        }

        updatePixels()
        if (save) saveFrame("/seq/###.png")

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
