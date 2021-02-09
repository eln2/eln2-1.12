package org.eln2.sim.nbt

import net.minecraft.nbt.NBTTagCompound
import org.eln2.mod.misc.INBTTReady
import org.eln2.sim.mna.component.Resistor
import org.eln2.sim.mna.state.State

class NbtResistor(var name: String, aPin: State?, bPin: State?) : Resistor(aPin, bPin), INBTTReady {
    override fun readFromNBT(nbt: NBTTagCompound, str: String) {
        name += str
        r = nbt.getDouble(str + "R")
    }

    override fun writeToNBT(nbt: NBTTagCompound, str: String) {
        name += str
        nbt.setDouble(str + "R", r)
    }
}