package org.eln2.mod.misc

import net.minecraft.util.math.Vec3d
import java.lang.Float.NEGATIVE_INFINITY
import java.lang.Float.POSITIVE_INFINITY

class BoundingBox(xMin: Float, xMax: Float, yMin: Float, yMax: Float, zMin: Float, zMax: Float) {
    val min: Vec3d = Vec3d(xMin.toDouble(), yMin.toDouble(), zMin.toDouble())
    val max: Vec3d = Vec3d(xMax.toDouble(), yMax.toDouble(), zMax.toDouble())

    fun merge(other: BoundingBox): BoundingBox {
        return BoundingBox(
            min.x.coerceAtMost(other.min.x).toFloat(),
            max.x.coerceAtLeast(other.max.x).toFloat(),
            min.y.coerceAtMost(other.min.y).toFloat(),
            max.y.coerceAtLeast(other.max.y).toFloat(),
            min.z.coerceAtMost(other.min.z).toFloat(),
            max.z.coerceAtLeast(other.max.z).toFloat()
        )
    }

    fun centre(): Vec3d {
        return Vec3d(
            min.x + (max.x - min.x) / 2,
            min.y + (max.y - min.y) / 2,
            min.z + (max.z - min.z) / 2
        )
    }

    override fun toString(): String {
        return "min: $min max: $max"
    }

    companion object {
        @JvmStatic
        fun mergeIdentity(): BoundingBox {
            return BoundingBox(POSITIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY, NEGATIVE_INFINITY, POSITIVE_INFINITY, NEGATIVE_INFINITY)
        }
    }
}
