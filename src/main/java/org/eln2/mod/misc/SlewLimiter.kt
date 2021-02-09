package org.eln2.mod.misc

import kotlin.math.abs

class SlewLimiter {
    var positiveSlewRate = 0f
        private set
    var negativeSlewRate = 0f
        private set
    var target = 0f
    var position = 0f

    constructor(slewRate: Float) {
        setSlewRate(slewRate)
    }

    constructor(positive: Float, negative: Float) {
        setSlewRate(positive, negative)
    }

    fun targetReached(): Boolean {
        return position == target
    }

    fun targetReached(tolerance: Float): Boolean {
        return abs(position - target) <= tolerance
    }

    fun setSlewRate(slewRate: Float) {
        positiveSlewRate = abs(slewRate)
        negativeSlewRate = abs(slewRate)
    }

    fun setSlewRate(positive: Float, negative: Float) {
        positiveSlewRate = abs(positive)
        negativeSlewRate = abs(negative)
    }

    fun step(deltaTime: Float) {
        var delta = target - position
        if (delta > 0f) delta = delta.coerceAtMost(positiveSlewRate * deltaTime) else if (delta < 0f) delta = delta.coerceAtLeast(-negativeSlewRate * deltaTime)
        position += delta
    }
}
