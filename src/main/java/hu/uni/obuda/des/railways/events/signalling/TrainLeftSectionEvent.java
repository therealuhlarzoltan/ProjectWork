package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.MainSignal;
import hu.uni.obuda.des.railways.installations.BlockSignallingSystem;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.trains.Train;

import java.util.Objects;

public class TrainLeftSectionEvent extends Event {
    private final Train train;
    private final BlockSignallingSystem previousBlockSignallingSystem;
    private final Direction direction;

    public TrainLeftSectionEvent(double eventTime, Train train, BlockSignallingSystem previousBlockSignallingSystem, Direction direction) {
        super(eventTime);
        this.previousBlockSignallingSystem = previousBlockSignallingSystem;
        this.train = Objects.requireNonNull(train);
        this.direction = Objects.requireNonNull(direction);
    }

    @Override
    public void execute(AbstractSimulator simulator) {

        if (previousBlockSignallingSystem == null) {
            System.out.println(train.toString() + " left section guarded by no signalling system at time " + getEventTime());
            return; // No connected signalling system
        }
        System.out.println(train.toString() + " left section guarded by signalling system " + previousBlockSignallingSystem.toString() + " at time " + getEventTime());
        notifyPreviousSystem(simulator, direction);
    }

    private MainSignal findPreviousSemaphore(BlockSignallingSystem previousSystem, Direction direction) {
        if (direction.equals(Direction.FORWARD)) {
            return previousSystem.getEndMainSignal();
        } else if (direction.equals(Direction.BACKWARD)) {
            return previousSystem.getStartMainSignal();
        } else {
            assert false : "Invalid direction";
            return null;
        }
    }

    private MainSignal findSecondPreviousSemaphore(BlockSignallingSystem previousSystem, Direction direction) {
       if (direction.equals(Direction.FORWARD)) {
           return previousSystem.getStartMainSignal();
       } else if (direction.equals(Direction.BACKWARD)) {
           return previousSystem.getEndMainSignal();
       } else {
            assert false : "Invalid direction";
            return null;
       }
    }


    private void notifyPreviousSystem(AbstractSimulator simulator, Direction direction) {
        previousBlockSignallingSystem.release();
        var prevSem = findPreviousSemaphore(previousBlockSignallingSystem, direction);
        var secondPrevSem = findSecondPreviousSemaphore(previousBlockSignallingSystem, direction);
        if (direction.equals(Direction.FORWARD)) {
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), secondPrevSem, new Direction[] {Direction.FORWARD}, new Direction[] {}));
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), prevSem, new Direction[] {Direction.BACKWARD}, new Direction[] {}));
        } else if (direction.equals(Direction.BACKWARD)) {
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), secondPrevSem, new Direction[] {Direction.BACKWARD}, new Direction[] {}));
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), prevSem, new Direction[] {Direction.FORWARD}, new Direction[] {}));
        } else {
            assert false : "Invalid direction";
        }
    }
}
