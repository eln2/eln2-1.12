package org.eln2.sim.mna.component;

import org.eln2.sim.mna.SubSystem;

public interface IAbstractor {

    void dirty(Component component);

    SubSystem getAbstractorSubSystem();
}
