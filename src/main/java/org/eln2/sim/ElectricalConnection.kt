package org.eln2.sim

import org.eln2.mod.misc.Utils
import org.eln2.sim.mna.component.InterSystem

class ElectricalConnection(L1: ElectricalLoad, L2: ElectricalLoad) : InterSystem() {
    var L1: ElectricalLoad
    var L2: ElectricalLoad
    fun notifyRsChange() {
        val R: Double = (aPin as ElectricalLoad).getRs() + (bPin as ElectricalLoad).getRs()
        setR(R)
    }

    override fun onAddToRootSystem() {
        this.connectTo(L1, L2)
        /*	((ElectricalLoad) aPin).electricalConnections.add(this);
		((ElectricalLoad) bPin).electricalConnections.add(this);*/notifyRsChange()
    }

    override fun onRemovefromRootSystem() {
        this.breakConnection()
        /*	((ElectricalLoad) aPin).electricalConnections.remove(this);
		((ElectricalLoad) bPin).electricalConnections.remove(this);*/
    }

    init {
        this.L1 = L1
        this.L2 = L2
        if (L1 === L2) Utils.println("WARNING: Attempt to connect load to itself?")
    }
}