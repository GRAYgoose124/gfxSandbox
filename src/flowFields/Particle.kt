package fields

import toxi.geom.Vec3D
import kotlin.math.pow

class Particle(x: Float, y: Float, z: Float, var acc: Vec3D = Vec3D(), var vel: Vec3D = Vec3D()) : Vec3D() {
    var trail: ArrayList<Vec3D> = arrayListOf(this.copy())

    fun updateTrail(trailLength: Float = 40.0f, maxLength: Int = 100) {
        if (trail.size >= maxLength) {
            trail.removeAt(0)
        }

        if (trail[trail.size - 1].sub(this).magSquared() > trailLength) {
            trail.add(this.copy())
        }
    }

    fun update(forces: ArrayList<Vec3D>, inverseDrag: Float = 1.0f, maxVelocity: Float = 4.0f): Particle {
        forces.forEach { f -> acc.addSelf(f.scale(1.0f / forces.size)) }

        acc = acc.scale(inverseDrag)
        vel.addSelf(acc)

        if (vel.magSquared() > maxVelocity.pow(2)) { vel.normalizeTo(maxVelocity) }
        this.addSelf(vel)

        return this
    }

    fun wrap(dim: Float): Boolean {
        var wrapped = true
        when {
            x < -dim -> x = dim
            y < -dim -> y = dim
            z < -dim -> z = dim
            y > dim -> y = -dim
            x > dim -> x = -dim
            z > dim -> z = -dim
            else -> wrapped = false
        }
        return wrapped
    }

        init {
        this.x = x
        this.y = y
        this.z = z
    }
}