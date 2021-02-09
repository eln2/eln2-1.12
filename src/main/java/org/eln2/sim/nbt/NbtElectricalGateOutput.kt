package org.eln2.sim.nbt

import org.eln2.mod.Eln2Object
import org.eln2.mod.misc.Utils

class NbtElectricalGateOutput(name: String?) : NbtElectricalLoad(name) {
    fun plot(str: String?): String {
        return "$u"
    }

    init {
        rs = Eln2Object.cableResistance
    }
}