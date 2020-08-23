package fields

import toxi.geom.Vec3D

class Options {
    // Sim Options only used by fields.Fields
    var pause = false
    var grid = false
    var vectors = true
    var path = true


    // Config Options
    val dim = 20

    val gridScale = 10.0f
    val gridColor = arrayOf(255.0f, 0.0f, 0.0f)

    val vecScale = gridScale * 4.0f

    val particleSize = 30.0f
    val trailLength = 50.0f
    val trailSize = 1000
    val maxVelocity = 4.0f

    val neighborhoodSize = 30.0f
}
