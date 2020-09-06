package lAttractor

import processing.core.PApplet
import toxi.geom.Line3D
import toxi.geom.Sphere
import toxi.geom.Vec3D
import toxi.processing.ToxiclibsSupport

fun main() { lAttractor() }


open class lAttractor : PApplet() { // TODO: inherit from this by overloading and supering settings/setup/draw?
    lateinit var gfx: ToxiclibsSupport
    var w = 1920f
    var h = 1080f

    var a1 = Vec3D(w/4f, h/2f, 0f)
    var a2 = Vec3D(w-w/4f, h/2f, 0f)
    var sp1 = Sphere(a1, 20f)
    var sp2 = Sphere(a2, 20f)
    var ln1 = Line3D(a1, a2)

    override fun settings() {
        size(w.toInt(), h.toInt(), P3D )
        smooth(8)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)
    }

    override fun draw() {
        background(0)

        stroke(255)
        fill(125f,0f,125f)
        gfx.sphere(sp1, 5)
        gfx.sphere(sp2, 5)
        gfx.line(ln1)
    }

    init {
        this.runSketch()
    }
}