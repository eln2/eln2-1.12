package org.eln2.sim;

import net.minecraft.nbt.NBTTagCompound;
import org.eln2.mod.Eln2Object;
import org.eln2.mod.misc.INBTTReady;
import org.eln2.sim.nbt.NbtElectricalGateInput;
import org.jetbrains.annotations.NotNull;

public abstract class NodeElectricalGateInputHysteresisProcess implements IProcess, INBTTReady {

    NbtElectricalGateInput gate;
    String name;

    boolean state = false;

    public NodeElectricalGateInputHysteresisProcess(String name, NbtElectricalGateInput gate) {
        this.gate = gate;
        this.name = name;
    }

    protected abstract void setOutput(boolean value);

    @Override
    public void process(double time) {
        if (state) {
            if (gate.getU() < Eln2Object.INSTANCE.getSV() * 0.3) {
                state = false;
                setOutput(false);
            } else setOutput(true);
        } else {
            if (gate.getU() > Eln2Object.INSTANCE.getSV() * 0.7) {
                state = true;
                setOutput(true);
            } else setOutput(false);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, @NotNull String str) {
        state = nbt.getBoolean(str + name + "state");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, @NotNull String str) {
        nbt.setBoolean(str + name + "state", state);
    }
}
