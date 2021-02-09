package org.eln2.sim.process.heater;

import org.eln2.sim.IProcess;
import org.eln2.sim.ThermalLoad;
import org.eln2.sim.mna.component.Resistor;

public class DiodeHeatThermalLoad implements IProcess {

    Resistor r;
    ThermalLoad load;
    double lastR;

    public DiodeHeatThermalLoad(Resistor r, ThermalLoad load) {
        this.r = r;
        this.load = load;
        lastR = r.getR();
    }

    @Override
    public void process(double time) {
        if (r.getR() == lastR) {
            load.movePowerTo(r.getP());
        } else {
            lastR = r.getR();
        }
    }
}
