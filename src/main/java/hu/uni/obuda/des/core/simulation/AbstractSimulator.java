package hu.uni.obuda.des.core.simulation;

import hu.uni.obuda.des.core.events.AbstractEvent;

public class AbstractSimulator {
    protected AbstractEventList events;

    public AbstractSimulator(AbstractEventList events) {
        this.events = events;
    }

    public void insert(AbstractEvent e) {
        events.insert(e);
    }

    public AbstractEvent cancel(AbstractEvent e)  {
        throw new UnsupportedOperationException("Not implemented");
    }
}
