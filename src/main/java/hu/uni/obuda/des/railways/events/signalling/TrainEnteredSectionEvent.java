package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Semaphore;
import hu.uni.obuda.des.railways.installations.BlockSignallingSystem;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TrainEnteredSectionEvent extends Event {
    private final Train train;
    private final BlockSignallingSystem currentBlockSignallingSystem;
    private final Direction direction;

    public TrainEnteredSectionEvent(double eventTime, Train train, BlockSignallingSystem currentBlockSignallingSystem, Direction direction) {
        super(eventTime);
        this.currentBlockSignallingSystem = currentBlockSignallingSystem;
        this.train = Objects.requireNonNull(train);
        this.direction = Objects.requireNonNull(direction);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (currentBlockSignallingSystem == null) {
            System.out.println(train.toString() + " entered section guarded by no signalling system at time " + getEventTime());
            return; // No signalling system connected
        }
        System.out.println(train.toString() + " entered section guarded by signalling system " + currentBlockSignallingSystem.toString() + " at time " + getEventTime());
        notifyCurrentSystem(simulator, direction);
    }

    private Semaphore findPreviousSemaphore(BlockSignallingSystem currentSystem, Direction direction) {
        if (direction.equals(Direction.FORWARD)) {
            return currentSystem.getStartSemaphore();
        } else if (direction.equals(Direction.BACKWARD)) {
            return currentSystem.getEndSemaphore();
        } else {
            assert false : "Invalid direction";
            return null;
        }
    }

    private Semaphore findNextSemaphore(BlockSignallingSystem currentSystem, Direction direction) {
        if (direction.equals(Direction.FORWARD)) {
            return currentSystem.getEndSemaphore();
        } else if (direction.equals(Direction.BACKWARD)) {
            return currentSystem.getStartSemaphore();
        } else {
            assert false : "Invalid direction";
            return null;
        }
    }


    private void notifyCurrentSystem(AbstractSimulator simulator, Direction direction) {
        currentBlockSignallingSystem.setCurrentState(BlockSignallingSystem.SignallingSystemState.OCCUPIED_SECTION);
        var prevSem = findPreviousSemaphore(currentBlockSignallingSystem, direction);
        var nextSem = findNextSemaphore(currentBlockSignallingSystem, direction);

        if (direction == Direction.FORWARD) {
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), prevSem, new Direction[] {}, new Direction[] {Direction.FORWARD}));
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), nextSem, new Direction[] {}, new Direction[] {Direction.BACKWARD}));
        } else if (direction == Direction.BACKWARD) {
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), prevSem, new Direction[] {}, new Direction[] {Direction.BACKWARD}));
            simulator.insert(new SemaphoreChangeEvent(getEventTime(), nextSem, new Direction[] {}, new Direction[] {Direction.FORWARD}));
        } else {
            assert false : "Invalid direction";
        }
    }
}
