package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.trains.Train;

public class TrainLeftSectionEvent extends Event {
    private final Train train;
    private final SignallingSystem previousSignallingSystem;

    public TrainLeftSectionEvent(double eventTime, Train train, SignallingSystem previousSignallingSystem) {
        super(eventTime);
        this.previousSignallingSystem = previousSignallingSystem;
        this.train = train;
    }

    @Override
    public void execute(AbstractSimulator simulator) {

        if (previousSignallingSystem == null) {
            System.out.println(train.toString() + " left section guarded by no signalling system");
            return; // No connected signalling system
        }
        System.out.println(train.toString() + " left section guarded by signalling system " + previousSignallingSystem.toString());
        previousSignallingSystem.setCurrentState(SignallingSystem.SignallingSystemState.FREE_SECTION);
        simulator.insert(new SemaphoreChangeEvent(getEventTime(), previousSignallingSystem.getMainLineSemaphore(), true));
    }
}
