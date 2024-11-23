package hu.uni.obuda.des.core.simulation;

import hu.uni.obuda.des.core.events.AbstractEvent;

public abstract class AbstractEventList {
    public abstract void insert(AbstractEvent event);
    public abstract AbstractEvent  removeFirst();
    public abstract int remainingEvents();
    public abstract AbstractEvent remove(AbstractEvent event);
    public abstract AbstractEvent getFirst();
}
