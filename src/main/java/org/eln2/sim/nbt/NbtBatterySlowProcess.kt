package mods.eln.sim.nbt

import mods.eln.sim.BatteryProcess
import mods.eln.sim.BatterySlowProcess
import org.eln2.mod.node.NodeBase
import org.eln2.sim.ThermalLoad

class NbtBatterySlowProcess(
        var node: NodeBase,
        batteryProcess: BatteryProcess,
        thermalLoad: ThermalLoad
) : BatterySlowProcess(batteryProcess, thermalLoad) {

    var explosionRadius = 2f

    override fun destroy() {
        node.physicalSelfDestruction(explosionRadius)
    }
}
