package org.eln2.sim.nbt

import org.eln2.mod.Eln2Object
import org.eln2.mod.misc.Utils

class NbtElectricalGateInputOutput(name: String?) : NbtElectricalLoad(name) {
    fun plot(str: String): String {
        return str + " " + Utils.plotVolt("", u) + Utils.plotAmpere("", current)
    }

    val isInputHigh: Boolean
        get() = u > Eln2Object.SV * 0.6
    val isInputLow: Boolean
        get() = u < Eln2Object.SV * 0.2
    val inputNormalized: Double
        get() {
            var norm: Double = u * (1/Eln2Object.SV)
            if (norm < 0.0) norm = 0.0
            if (norm > 1.0) norm = 1.0
            return norm
        }
    val inputBornedU: Double
        get() {
            var U = this.u
            if (U < 0.0) U = 0.0
            if (U > Eln2Object.LV) U = Eln2Object.SV
            return U
        }

    init {
        rs = Eln2Object.cableResistance
    }
}