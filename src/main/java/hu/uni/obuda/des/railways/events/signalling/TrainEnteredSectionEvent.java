package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Semaphore;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class TrainEnteredSectionEvent extends Event {
    private final Train train;
    private final SignallingSystem currentSignallingSystem;
    private final Direction direction;

    public TrainEnteredSectionEvent(double eventTime, Train train, SignallingSystem currentSignallingSystem, Direction direction) {
        super(eventTime);
        this.currentSignallingSystem = currentSignallingSystem;
        this.train = Objects.requireNonNull(train);
        this.direction = Objects.requireNonNull(direction);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (currentSignallingSystem == null) {
            System.out.println(train.toString() + " entered section guarded by no signalling system");
            return; // No signalling system connected
        }
        System.out.println(train.toString() + " entered section guarded by signalling system " + currentSignallingSystem.toString());
        notifyCurrentSystem(simulator, direction);
    }

    private Semaphore findPreviousSemaphore(SignallingSystem currentSystem, Direction direction) {
        if (direction.equals(Direction.FORWARD)) {
            return currentSystem.getStartSemaphore();
        } else if (direction.equals(Direction.BACKWARD)) {
            return currentSystem.getEndSemaphore();
        } else {
            assert false : "Invalid direction";
            return null;
        }
    }

    private Semaphore findNextSemaphore(SignallingSystem currentSystem, Direction direction) {
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
        currentSignallingSystem.setCurrentState(SignallingSystem.SignallingSystemState.OCCUPIED_SECTION);
        var prevSem = findPreviousSemaphore(currentSignallingSystem, direction);
        var nextSem = findNextSemaphore(currentSignallingSystem, direction);

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
