package org.eln2.sim.mna.component;

public class InterSystem extends org.eln2.sim.mna.component.Resistor {

    public static class InterSystemDestructor {
        boolean done = false;
    }

    @Override
    public boolean canBeReplacedByInterSystem() {
        return true;
    }
}
