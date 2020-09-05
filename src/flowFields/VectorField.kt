package fields

import toxi.geom.Vec3D
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.KFunction3

class VectorField(val dimx: Int, val dimy: Int, val dimz: Int, var transform: KFunction3<Int, Int, Int, Array<Float>>) {
    private val options = Options()

    var vectors: MutableMap<Int, MutableMap<Int, MutableMap<Int, Vec3D>>> = mutableMapOf()
    var positions: MutableMap<Int, MutableMap<Int, MutableMap<Int, Vec3D>>> = mutableMapOf()

    fun flowParticle(particle: Particle, iterations: Int): Iterator<Particle> {
        return _flowParticle(particle, iterations).iterator()
    }

    fun _flowParticle(particle: Particle, iterations: Int) = sequence {
        for (i in 0 until iterations) {
            yield(stepParticle(particle))
        }
    }

    fun stepParticle(particle: Particle): Particle {
        val forces = ArrayList<Vec3D>()

        val neighbors = neighborhood(particle)
        neighbors.forEach { (vector, distance) ->
            forces.add(vector.scale(1 / distance * neighbors.size)) //.normalizeTo(options.maxVelocity))
        }


        particle.update(forces, maxVelocity=options.maxVelocity)
        particle.wrap(dimx * 5f)
        particle.updateTrail(options.trailLength, options.trailSize)


        return particle
    }

    private fun neighborhood(particle: Particle): MutableMap<Vec3D, Float> {
        val neighbors: MutableMap<Vec3D, Float> = mutableMapOf()

        vectors.forEach { vr ->
            vr.value.forEach { vc ->
                vc.value.forEach { v ->
                    val neighbor = v.value
                    val dist = sqrt((particle.x - neighbor.x).pow(2) +
                                (particle.y - neighbor.y).pow(2) +
                                (particle.z - neighbor.z).pow(2))

                    if (dist < options.neighborhoodSize) {
                        neighbors[neighbor] = dist
                    }
                }
            }
        }

        return neighbors
    }

    fun apply(transform: KFunction3<Int, Int, Int, Array<Float>>) {

    }

    init {
        for (i in 0..dimx) {
            vectors[i] = mutableMapOf()
            positions[i] = mutableMapOf()
            for (j in 0..dimy) {
                vectors[i]!![j] = mutableMapOf()
                positions[i]!![j] = mutableMapOf()
                for (k in 0..dimz) {
                    val (x, y, z) = transform.call(i, j, k)
                    vectors[i]!![j]!![k] = Vec3D(x, y, z)
                    positions[i]!![j]!![k] = Vec3D(i * options.gridScale, j * options.gridScale, k * options.gridScale)
                }
            }
        }
    }
}


