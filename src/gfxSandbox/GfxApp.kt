package gfxSandbox

import processing.core.PApplet
import toxi.processing.ToxiclibsSupport

fun main() { App() }


open class GfxApp : PApplet() { // TODO: inherit from this by overloading and supering settings/setup/draw?
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

    init {
        this.runSketch()
    }
}

class App : GfxApp() {
    override fun draw() {
        background(255)
    }
}