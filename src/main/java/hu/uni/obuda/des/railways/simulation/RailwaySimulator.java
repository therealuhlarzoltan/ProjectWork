package hu.uni.obuda.des.railways.simulation;

import hu.uni.obuda.des.core.events.AbstractEvent;
import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.EventQueue;
import hu.uni.obuda.des.core.simulation.Simulator;

import hu.uni.obuda.des.railways.events.delays.DelayEvent;
import hu.uni.obuda.des.railways.events.signalling.TrainConflictEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class RailwaySimulator extends Simulator {
    private final List<DelayEvent> delayEvents = new LinkedList<>();
    private final List<TrainConflictEvent> conflictEvents = new LinkedList<>();
    private final List<Event> eventArchive = new LinkedList<>();

    public RailwaySimulator() {
        super(new EventQueue());
    }

    @Override
    public void processAllEvents() {
        super.processAllEvents();
    }

    @Override
    protected void processEvent(AbstractEvent e) {
        if (e instanceof DelayEvent) {
            delayEvents.add((DelayEvent) e);
        } else if (e instanceof TrainConflictEvent) {
            conflictEvents.add((TrainConflictEvent) e);
        } else {
            eventArchive.add((Event) e);
        }
        super.processEvent(e);
    }

    public void run(double simulationTime) {
        for (AbstractEvent e = events.removeFirst(); e != null && simulationTime <= now(); e = events.removeFirst()) {
            this.processEvent(e);
        }
    }

    @Override
    public AbstractEvent cancel(AbstractEvent e) {
        return events.remove(e);
    }

    public void cancelAll(Predicate<AbstractEvent> predicate) {
        ((EventQueue)events).removeAll(predicate);
    }
}
