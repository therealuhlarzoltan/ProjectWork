package hu.uni.obuda.des.core.events;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;

public sealed abstract class AbstractEvent implements Comparable<AbstractEvent> permits Event {
    public abstract void execute(AbstractSimulator simulator);
    public abstract int compareTo(AbstractEvent o);
}
