package org.eln2.sim.process.destruct;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.eln2.mod.Eln2Config;
import org.eln2.mod.misc.Coordinate;

public class WorldExplosion implements IDestructable {

    Object origine;

    Coordinate c;
    float strength;
    String type;

    public WorldExplosion(Coordinate c) {
        this.c = c;
    }
/*
    public WorldExplosion(SixNodeElement e) {
        this.c = e.getCoordinate();
        this.type = e.toString();
        origine = e;
    }

    public WorldExplosion(TransparentNodeElement e) {
        this.c = e.coordinate();
        this.type = e.toString();
        origine = e;
    }*/

    public WorldExplosion cableExplosion() {
        strength = 1.5f;
        return this;
    }

    public WorldExplosion machineExplosion() {
        strength = 3;
        return this;
    }

    @Override
    public void destructImpl() {
        if (Eln2Config.INSTANCE.getBigExplosionsEnabled())
            c.world().createExplosion((Entity) null, c.x, c.y, c.z, strength, true);
        else
            c.world().setBlockToAir(new BlockPos(c.getBlockPos()));
    }

    @Override
    public String describe() {
        return String.format("%s (%s)", this.type, this.c.toString());
    }
}
