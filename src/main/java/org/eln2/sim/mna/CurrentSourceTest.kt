package org.eln2.sim.mna

import org.eln2.sim.mna.component.Resistor
import org.eln2.sim.mna.component.VoltageSource
import org.eln2.sim.mna.state.State
import org.eln2.sim.mna.state.VoltageState

fun main(args: Array<String>) {
    val s = SubSystem(null, 0.05)

    val n1: State = VoltageState()

    val vs = VoltageSource("voltage", n1, null)
    //val cs = CurrentSource("current", n1, null)
    val r1 = Resistor(n1, null)

    r1.r = 10.0
    vs.u = 1.0
    //cs.current = 1.0

    s.addState(n1)

    //s.addComponent(cs)
    s.addComponent(vs)
    s.addComponent(r1)

    s.step()
    s.step()

    println(s.A.data.contentDeepToString())
    println(s.Idata.contentToString())
    println(s.XtempData.contentToString())
}
