package org.eln2.sim.nbt

import org.eln2.mod.Eln2Object

class NbtElectricalGateInput(name: String?) : NbtElectricalLoad(name) {
    fun plot(str: String?): String {
        return "$u"
    }

    fun stateHigh(): Boolean {
        return u > Eln2Object.SV * 0.6
    }

    fun stateLow(): Boolean {
        return u < Eln2Object.SV * 0.2
    }

    val normalized: Double
        get() {
            var norm: Double = u * (1/Eln2Object.SV)
            if (norm < 0.0) norm = 0.0
            if (norm > 1.0) norm = 1.0
            return norm
        }
    val bornedU: Double
        get() {
            var U = this.u
            if (U < 0.0) U = 0.0
            if (U > Eln2Object.SV) U = Eln2Object.SV
            return U
        }

    init {
        this.rs = Eln2Object.cableResistance
    }
}