package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Semaphore;
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
        notifyPreviousSystem(simulator, direction);
    }

    private Semaphore findPreviousSemaphore(SignallingSystem previousSystem, Direction direction) {
        if (direction.equals(Direction.FORWARD)) {
            return previousSystem.getEndSemaphore();
        } else if (direction.equals(Direction.BACKWARD)) {
            return previousSystem.getStartSemaphore();
        } else {
            assert false : "Invalid direction";
            return null;
        }
    }

    private Semaphore findSecondPreviousSemaphore(SignallingSystem previousSystem, Direction direction) {
       if (direction.equals(Direction.FORWARD)) {
           return previousSystem.getStartSemaphore();
       } else if (direction.equals(Direction.BACKWARD)) {
           return previousSystem.getEndSemaphore();
       } else {
            assert false : "Invalid direction";
            return null;
       }
    }


    private void notifyPreviousSystem(AbstractSimulator simulator, Direction direction) {
        previousSignallingSystem.setCurrentState(SignallingSystem.SignallingSystemState.FREE_SECTION);
        var prevSem = findPreviousSemaphore(previousSignallingSystem, direction);
        var secondPrevSem = findSecondPreviousSemaphore(previousSignallingSystem, direction);
        if (direction.equals(Direction.FORWARD)) {
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), secondPrevSem, Direction.FORWARD));
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), prevSem, Direction.BACKWARD));
        } else if (direction.equals(Direction.BACKWARD)) {
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), secondPrevSem, Direction.BACKWARD));
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), prevSem, Direction.FORWARD));
        } else {
            assert false : "Invalid direction";
        }
    }
}
