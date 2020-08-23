package gfxSandbox

import processing.core.PApplet
import toxi.processing.ToxiclibsSupport

fun main() { gfxSandbox.run() }


class gfxSandbox : PApplet() { // TODO: functionalize and move to .demo
    companion object {
        fun run() {
            val app = gfxSandbox()
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