package org.eln2.sim.nbt

import net.minecraft.nbt.NBTTagCompound
import org.eln2.mod.misc.INBTTReady
import org.eln2.sim.ElectricalLoad

open class NbtElectricalLoad(var name: String) : ElectricalLoad(), INBTTReady {
    override fun readFromNBT(nbt: NBTTagCompound, str: String) {
        u = nbt.getFloat(str + name + "Uc").toDouble()
        if (java.lang.Double.isNaN(u)) u = 0.0
        if (u == Float.NEGATIVE_INFINITY.toDouble()) u = 0.0
        if (u == Float.POSITIVE_INFINITY.toDouble()) u = 0.0
    }

    override fun writeToNBT(nbt: NBTTagCompound, str: String) {
        nbt.setFloat(str + name + "Uc", u.toFloat())
    }
}