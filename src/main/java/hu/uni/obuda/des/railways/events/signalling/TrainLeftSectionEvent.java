package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.trains.Train;

import java.util.Objects;

public class TrainLeftSectionEvent extends Event {
    private final Train train;
    private final SignallingSystem previousSignallingSystem;
    private final Direction direction;

    public TrainLeftSectionEvent(double eventTime, Train train, SignallingSystem previousSignallingSystem, Direction direction) {
        super(eventTime);
        this.previousSignallingSystem = previousSignallingSystem;
        this.train = Objects.requireNonNull(train);
        this.direction = Objects.requireNonNull(direction);
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
        notifyPreviousSystem(direction);
    }

    private void notifyPreviousSystem(AbstractSimulator simulator, Direction direction) {
        previousSignallingSystem.setCurrentState(SignallingSystem.SignallingSystemState.FREE_SECTION);
        if (direction.equals(Direction.FORWARD)) {
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), previousSignallingSystem.getStartSemaphore(), Direction.FORWARD));
        } else {
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), previousSignallingSystem.getEndSemaphore(), Direction.BACKWARD));
        }
    }
}
