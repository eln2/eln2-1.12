package org.eln2.sim.nbt

import net.minecraft.nbt.NBTTagCompound
import org.eln2.mod.misc.FunctionTable
import org.eln2.mod.misc.INBTTReady
import org.eln2.sim.BatteryProcess
import org.eln2.sim.ThermalLoad
import org.eln2.sim.mna.component.VoltageSource
import org.eln2.sim.mna.state.VoltageState

class NbtBatteryProcess(
        positiveLoad: VoltageState?,
        negativeLoad: VoltageState?,
        voltageFunction: FunctionTable,
        IMax: Double,
        voltageSource: VoltageSource,
        thermalLoad: ThermalLoad
) : BatteryProcess(positiveLoad, negativeLoad, voltageFunction, IMax, voltageSource, thermalLoad), INBTTReady {

    override fun readFromNBT(nbt: NBTTagCompound, str: String) {
        Q = nbt.getDouble(str + "NBP" + "Q")
        if (!Q.isFinite()) Q = 0.0
        life = nbt.getDouble(str + "NBP" + "life")
        if (!life.isFinite()) life = 1.0
    }

    override fun writeToNBT(nbt: NBTTagCompound, str: String) {
        nbt.setDouble(str + "NBP" + "Q", Q)
        nbt.setDouble(str + "NBP" + "life", life)
    }
}
