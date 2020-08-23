package doublePendulum

import toxi.geom.Vec2D
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class DPendulum(var anchor: Vec2D, var armLen: Float, var bMass: Float) {
    var joint: Vec2D = anchor.add(armLen, 0.0f)
    var weight: Vec2D = anchor.add(armLen * 2.0f, 0.0f)
    var vel: Vec2D = Vec2D()
    var acc: Vec2D = Vec2D()

    // util vars
    var trails: MutableList<Pair<Vec2D, Vec2D>> = ArrayList()


    fun step(maxSpd: Float, gravity: Boolean = true) {
        if (gravity) {
            acc.addSelf(Vec2D(0.0f, 0.981f))
        }

        //acc = Vec2D.randomVector().scale(maxSpd)
        vel.addSelf(acc).normalizeTo(maxSpd)

        val vw = weight.add(vel).magSquared()
        val w = weight.magSquared()
        if (vw != w) {
            joint.rotate(sin(vel.x+joint.x).toFloat())
            weight.rotate(sin(vel.x+weight.x).toFloat())
            val vj = joint.add(vel).magSquared()
            val j = joint.magSquared()
            if (vj != j) {
                weight.rotate(cos(vel.y+weight.y).toFloat())
                joint.rotate(cos(vel.y+joint.y).toFloat())
            } else {
                joint.addSelf(vel)
            }
        } else {
            weight.addSelf(vel)
        }


        // Saving trail data with a max length. remove Pair
        if (joint.distanceToSquared(weight) > 1000.0f) {
            trails.add(Pair(joint, weight))
        }
        if (trails.size > 100) {
            trails.removeAt(0)
        }
    }

    fun rand_state(maxSpd: Float, randForces: Boolean = true) {
        joint = Vec2D.randomVector().scale(armLen)
        weight = Vec2D.randomVector().scale(armLen)

        if (randForces) {
            vel = Vec2D.randomVector().scale(maxSpd)
            acc = Vec2D.randomVector().scale(maxSpd / 4.0f)
        }
    }


}