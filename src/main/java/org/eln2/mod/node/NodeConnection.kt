package org.eln2.mod.node

import org.eln2.mod.Eln2Object
import org.eln2.mod.misc.Direction
import org.eln2.mod.misc.LRDU
import org.eln2.sim.ElectricalConnection
import org.eln2.sim.ThermalConnection
import java.util.*

class NodeConnection(var N1: NodeBase, var dir1: Direction, var lrdu1: LRDU, var N2: NodeBase, var dir2: Direction, var lrdu2: LRDU) {
    var EC: MutableList<ElectricalConnection> = ArrayList()
    var TC: MutableList<ThermalConnection> = ArrayList()
    fun destroy() {
        for (ec in EC) Eln2Object.sim.removeElectricalComponent(ec)
        for (tc in TC) Eln2Object.sim.removeThermalConnection(tc)
        N1.externalDisconnect(dir1, lrdu1)
        N2.externalDisconnect(dir2, lrdu2)
    }

    fun addConnection(ec: ElectricalConnection) {
        EC.add(ec)
    }

    fun addConnection(tc: ThermalConnection) {
        TC.add(tc)
    }
}
