package org.eln2.sim.mna.component;

import org.eln2.mod.misc.Utils;
import org.eln2.sim.mna.SubSystem;
import org.eln2.sim.mna.misc.MnaConst;
import org.eln2.sim.mna.state.State;

public class Resistor extends Bipole {

    public Resistor() {
    }

    public Resistor(State aPin, State bPin) {
        super(aPin, bPin);
    }

    //public SubSystem interSystemA, interSystemB;

/*	public Line line = null;
    public boolean lineReversDir;
	public boolean isInLine() {
		
		return line != null;
	}*/

    private double r = MnaConst.highImpedance, rInv = 1 / MnaConst.highImpedance;

    //public boolean usedAsInterSystem = false;

    public double getRInv() {
        return rInv;
    }

    public double getR() {
        return r;
    }

    public double getI() {
        return getCurrent();
    }

    public double getP() {
        return getU() * getCurrent();
    }

    public double getU() {
        return (aPin == null ? 0 : aPin.state) - (bPin == null ? 0 : bPin.state);
    }

    public Resistor setR(double r) {
        if (Double.isNaN(r) || Double.isInfinite(r)) {
            Utils.println("Error! Resistor cannot be set to " + r );
            // Call stack for debugging
            //new Throwable().printStackTrace();
            return this;
        }
        if (this.r != r) {
            this.r = r;
            this.rInv = 1 / r;
            dirty();
        }
        return this;
    }

    public void highImpedance() {
        setR(MnaConst.highImpedance);
    }

    public void ultraImpedance() {
        setR(MnaConst.ultraImpedance);
    }

    public Resistor pullDown() {
        setR(MnaConst.pullDown);
        return this;
    }
	
	/*@Override
	public void dirty() {
		if (line != null) {
			line.recalculateR();
		}
		if (usedAsInterSystem) {
			aPin.getSubSystem().breakSystem();
			if (aPin.getSubSystem() != bPin.getSubSystem()) {
				bPin.getSubSystem().breakSystem();
			}
		}
		
		super.dirty();
	}*/

    boolean canBridge() {
        return false;
    }

    @Override
    public void applyTo(SubSystem s) {
        s.addToA(aPin, aPin, rInv);
        s.addToA(aPin, bPin, -rInv);
        s.addToA(bPin, bPin, rInv);
        s.addToA(bPin, aPin, -rInv);
    }

    @Override
    public double getCurrent() {
        return getU() * rInv;
		/*if(line == null)
			return getU() * rInv;
		else if (lineReversDir)
			return -line.getCurrent();
		else
			return line.getCurrent();*/
    }
}
