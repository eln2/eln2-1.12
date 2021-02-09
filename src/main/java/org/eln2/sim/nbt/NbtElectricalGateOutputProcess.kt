package org.eln2.sim.nbt

import net.minecraft.nbt.NBTTagCompound
import org.eln2.mod.Eln2Config
import org.eln2.mod.Eln2Object
import org.eln2.mod.misc.INBTTReady
import org.eln2.mod.misc.Utils
import org.eln2.sim.ElectricalLoad
import org.eln2.sim.mna.SubSystem
import org.eln2.sim.mna.component.Capacitor

class NbtElectricalGateOutputProcess(var name: String, positiveLoad: ElectricalLoad?) : Capacitor(positiveLoad, null), INBTTReady {

    var outputVoltage: Double = 0.0

    var highImpedance = false
        set(enable) {
        field = enable
        val baseC: Double = Eln2Object.gateOutputCurrent / Eln2Config.electricalFrequency / Eln2Object.SV
        c = if (enable) {
            baseC / 1000
        } else {
            baseC
        }
    }

    override fun simProcessI(s: SubSystem) {
        if (!highImpedance) aPin.state = u
        super.simProcessI(s)
    }

    fun isHighImpedance(): Boolean {
        return highImpedance
    }

    override fun readFromNBT(nbt: NBTTagCompound, str: String) {
        highImpedance = (nbt.getBoolean(str + name + "highImpedance"))
        outputVoltage = nbt.getDouble(str + name + "U")
    }

    override fun writeToNBT(nbt: NBTTagCompound, str: String) {
        nbt.setBoolean(str + name + "highImpedance", highImpedance)
        nbt.setDouble(str + name + "U", outputVoltage)
    }

    fun state(value: Boolean) {
        outputVoltage = if (value) Eln2Object.SV else 0.0
    }

    var outputNormalized: Double
        get() = outputVoltage / Eln2Object.SV
        set(value) {
            setOutputNormalizedSafe(value)
        }
    val outputOnOff: Boolean
        get() = u >= Eln2Object.SV / 2

    fun setOutputNormalizedSafe(value: Double) {
        var value = value
        if (value > 1.0) value = 1.0
        if (value < 0.0) value = 0.0
        if (java.lang.Double.isNaN(value)) value = 0.0
        outputVoltage = value * Eln2Object.SV
    }

    fun setUSafe(value: Double) {
        var value = value
        value = Utils.limit(value, 0.0, Eln2Object.SV)
        if (java.lang.Double.isNaN(value)) value = 0.0
        outputVoltage = value
    }

    init {
        highImpedance = false
    }
}