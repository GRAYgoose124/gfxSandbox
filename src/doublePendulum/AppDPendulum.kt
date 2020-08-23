package doublePendulum

import processing.core.PApplet
import toxi.geom.Line2D
import toxi.geom.Vec2D
import toxi.processing.ToxiclibsSupport

fun main() {
    DPendApp.run()
}


class DPendApp : PApplet() { // TODO: functionalize and move to .demo
    companion object {
        fun run() {
            val app = DPendApp()
            app.runSketch()
        }
    }

    lateinit var pendulum: DPendulum
    lateinit var gfx: ToxiclibsSupport

    override fun settings() {
        size(600, 1080)
        smooth(8)
    }

    override fun setup() {
        gfx = ToxiclibsSupport(this)

        pendulum = DPendulum(Vec2D(this.height / 2.0f, this.width / 2.0f), 15.0f, 20.0f)
    }

    override fun draw() {
        background(0)
        pendulum.step(5.0f)
        draw_pend(pendulum)
    }

    fun draw_pend(p: DPendulum) {
        stroke(0.0f, 255.0f, 0.0f)

        // draw pendulum bars
        val a = Line2D(p.anchor, p.joint)
        val b = Line2D(p.joint, p.weight)
        gfx.line(a)
        gfx.line(b)
        text(p.anchor.toString(), 20.0f, 1060.0f)
        // draw the weight
        gfx.circle(p.weight, p.bMass)

        stroke(0)
    }

}