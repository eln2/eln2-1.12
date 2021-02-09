package org.eln2.sim;

import org.eln2.sim.mna.component.ResistorSwitch;

public class DiodeProcess implements IProcess {

    ResistorSwitch resistor;

    public DiodeProcess(ResistorSwitch resistor) {
        this.resistor = resistor;
    }

    @Override
    public void process(double time) {
        resistor.setState(resistor.getU() > 0);
    }
}
