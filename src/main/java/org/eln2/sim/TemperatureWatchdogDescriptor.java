package org.eln2.sim;

public interface TemperatureWatchdogDescriptor {
    public double getUmax();

    public double getUmin();

    public double getBreakPropPerVoltOverflow();
}
