package perlinNoise

import processing.core.PApplet

fun main() {
    PerlinNoise.run()
}

class PerlinNoise : PApplet() {
    companion object {
        fun run() {
            val app = PerlinNoise()
            app.runSketch()
        }
    }

    override fun settings() {
        size(1280, 720)
    }

    override fun setup() {
        // fullScreen()    
    }

    override fun draw() {
        background(0)
    }
}