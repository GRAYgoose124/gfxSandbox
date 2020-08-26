package voronoiApp

import processing.core.PApplet

fun main() {
    VoronoiApp.run()
}

class VoronoiApp : PApplet() {
    companion object {
        fun run() {
            val app = VoronoiApp()
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