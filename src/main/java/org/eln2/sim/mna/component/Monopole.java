package org.eln2.sim.mna.component;

import org.eln2.sim.mna.state.State;
import org.eln2.sim.mna.state.VoltageState;

public abstract class Monopole extends Component {

    VoltageState pin;

    public Monopole connectTo(VoltageState pin) {
        this.pin = pin;
        if (pin != null) pin.add(this);
        return this;
    }

    @Override
    public State[] getConnectedStates() {
        return new State[]{pin};
    }
}
