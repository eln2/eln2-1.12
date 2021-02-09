package org.eln2.sim.nbt

import net.minecraft.nbt.NBTTagCompound
import org.eln2.mod.misc.INBTTReady
import org.eln2.sim.ThermalLoad

class NbtThermalLoad : ThermalLoad, INBTTReady {
    var name: String

    constructor(name: String, Tc: Double, Rp: Double, Rs: Double, C: Double) : super(Tc, Rp, Rs, C) {
        this.name = name
    }

    constructor(name: String) : super() {
        this.name = name
    }

    override fun readFromNBT(nbt: NBTTagCompound, str: String) {
        Tc = nbt.getFloat(str + name + "Tc").toDouble()
        if (java.lang.Double.isNaN(Tc)) Tc = 0.0
        if (Tc == Float.NEGATIVE_INFINITY.toDouble()) Tc = 0.0
        if (Tc == Float.POSITIVE_INFINITY.toDouble()) Tc = 0.0
    }

    override fun writeToNBT(nbt: NBTTagCompound, str: String) {
        nbt.setFloat(str + name + "Tc", Tc.toFloat())
    }
}