package org.eln2.mod.misc

import net.minecraft.nbt.NBTTagCompound

interface INBTTReady {
    fun readFromNBT(nbt: NBTTagCompound, str: String)
    fun writeToNBT(nbt: NBTTagCompound, str: String)
}
