package hu.uni.obuda.des.core.simulation;

import hu.uni.obuda.des.core.events.AbstractEvent;
import hu.uni.obuda.des.core.events.Event;

public class Simulator extends AbstractSimulator {
    double simulationTime;

    public Simulator(AbstractEventList events) {
        super(events);
        simulationTime = 0.0;
    }

    public double now() {
        return simulationTime;
    }

    @SuppressWarnings("null")
    public void processAllEvents() {
        for (AbstractEvent e = events.removeFirst(); e != null; e = events.removeFirst()) {
          processEvent(e);
        }
    }

    @SuppressWarnings("null")
    protected void processEvent(AbstractEvent e) {
        simulationTime = ((Event) e).getEventTime();
        e.execute(this);
    }
}

