package lAttractor

import processing.core.PApplet
import toxi.processing.ToxiclibsSupport

fun main() { lAttractor.run() }


class lAttractor : PApplet() {
    companion object {
        fun run() {
            val app = lAttractor()
            app.runSketch()
        }
    }

    lateinit var gfx: ToxiclibsSupport

    override fun settings() {
        fullScreen(P3D)
        smooth(8)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)
    }

    override fun draw() {
        background(0)
    }
}