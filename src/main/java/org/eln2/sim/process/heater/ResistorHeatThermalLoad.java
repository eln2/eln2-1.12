package org.eln2.sim.process.heater;

import org.eln2.sim.IProcess;
import org.eln2.sim.ThermalLoad;
import org.eln2.sim.mna.component.Resistor;

public class ResistorHeatThermalLoad implements IProcess {

    Resistor r;
    ThermalLoad load;

    public ResistorHeatThermalLoad(Resistor r, ThermalLoad load) {
        this.r = r;
        this.load = load;
    }

    @Override
    public void process(double time) {
        load.movePowerTo(r.getP());
    }
}
