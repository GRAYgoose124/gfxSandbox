package fields

import culebra.viz.Cameras
import processing.core.PApplet
import toxi.geom.Line3D
import toxi.geom.Vec3D
import toxi.processing.ToxiclibsSupport
import kotlin.random.Random


fun main() {
    Fields.run()
}

class Fields : PApplet() {
    private var cam: Cameras? = null
    private var gfx: ToxiclibsSupport? = null
    private var options: Options = Options()

    var field: VectorField = VectorField(options.dim, options.dim, options.dim, ::recipIdent)
    var unitVector: Vec3D = Vec3D(options.vecScale, options.vecScale, options.vecScale)

    var path: ArrayList<Vec3D>? = null

    companion object {
        fun run() {
            val f = Fields()
            f.runSketch()
        }
    }

    override fun settings() {
        fullScreen(P3D)
        smooth(8)
    }

    override fun setup() {
        cam = Cameras(this)
        cam!!.set3DCamera(500.0, 0, 500, intArrayOf(this.width / 2, this.height / 2, 1000), true)

        gfx = ToxiclibsSupport(this)

        val p = Particle(Random.nextFloat()*options.dim,
                         Random.nextFloat()*options.dim,
                         Random.nextFloat()*options.dim)

    }

    override fun draw() {
        background(0)

        if (!options.pause) {
            updatefieldVectors()
            path = field.flowParticle(Particle(50.0f, 150.0f, 50.0f))
        }

        if (options.grid) { drawfieldGrid() }
        if (options.vectors) { drawfieldVectors() }
        if (options.path) { drawPath(path!!) }
    }

    fun drawPath(path: ArrayList<Vec3D>) {
        var old_point = path[0]

        path.forEach { point ->
            gfx!!.line(Line3D(old_point, point))
            old_point = point
        }
    }

    fun drawfieldGrid() {
        val blen = options.gridScale * options.dim.toFloat() * 0.5f
        val blines = arrayOf(Vec3D(blen, 0.0f, 0.0f),
                             Vec3D(0.0f, blen, 0.0f),
                             Vec3D(0.0f, 0.0f, blen))

        for (i in 0..options.dim) {
            for (j in 0..options.dim) {
                for (k in 0..options.dim) {
                    val t = Vec3D(i.toFloat(), j.toFloat(), k.toFloat()).scale(blen)
                    stroke(options.gridColor[0], options.gridColor[1], options.gridColor[2])
                    gfx!!.line(Line3D(t, t.add(blines[0])))
                    gfx!!.line(Line3D(t, t.add(blines[1])))
                    gfx!!.line(Line3D(t, t.add(blines[2])))

                }
            }
        }
    }

    fun drawfieldVectors() {
        field.vectors.forEach { vr ->
            vr.value.forEach { vc ->
                vc.value.forEach { v ->
                    val ang = v.value.angleBetween(unitVector)
                    stroke(255.0f * sin(ang), 255.0f * cos(ang), 255.0f)
                    val pos = field.positions[vr.key]!![vc.key]!![v.key]!!.scale(options.gridScale)
                    gfx!!.line(Line3D(pos, pos.add(v.value.scale(options.vecScale))))
                }
            }
        }
    }

    fun updatefieldVectors() {
        field.vectors.forEach { vr ->
            vr.value.forEach { vc ->
                vc.value.forEach { v ->
                    v.value.rotateX(0.01f)
                    v.value.rotateY(-0.01f)
                    val rv = field.vectors[Random.nextInt(until=options.dim)]!![Random.nextInt(until=options.dim)]!![Random.nextInt(until=options.dim)]!!
                    rv.interpolateToSelf(v.value, 0.5f)
                }
            }
        }
    }

    override fun keyPressed() {
        when (key) {
            '_' -> { }
            'g' -> { options.grid = !options.grid }
            'v' -> { options.vectors = !options.vectors }
            'p' -> { options.pause = !options.pause }
            't' -> { options.path = !options.path }

        }
    }

    // Field Vector Transformers | TODO: Should exist in VectorFieldUtils
    fun random3(x: Int, y: Int, z: Int): Array<Float> {
        return Array(3) { Random.nextFloat() }
    }

    fun recipIdent(x: Int, y: Int, z: Int): Array<Float> {
        return arrayOf(1.0f / (x + 1), 1.0f / (y + 1), 1.0f / (z + 1))
    }

    fun normIdent(x: Int, y: Int, z: Int): Array<Float> {
        return arrayOf(x.toFloat() / options.dim, y.toFloat() / options.dim, z.toFloat() / options.dim)
    }
}

