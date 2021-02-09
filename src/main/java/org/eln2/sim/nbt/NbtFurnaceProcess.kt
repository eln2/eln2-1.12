package org.eln2.sim.nbt

import net.minecraft.nbt.NBTTagCompound
import org.eln2.mod.misc.INBTTReady
import org.eln2.sim.FurnaceProcess
import org.eln2.sim.ThermalLoad

class NbtFurnaceProcess(var name: String, load: ThermalLoad?) : FurnaceProcess(load), INBTTReady {
    override fun readFromNBT(nbt: NBTTagCompound, str: String) {
        combustibleEnergy = nbt.getFloat(str + name + "Q").toDouble()
        gain = nbt.getDouble(str + name + "gain")
    }

    override fun writeToNBT(nbt: NBTTagCompound, str: String) {
        nbt.setFloat(str + name + "Q", combustibleEnergy.toFloat())
        nbt.setDouble(str + name + "gain", gain)
    }
}