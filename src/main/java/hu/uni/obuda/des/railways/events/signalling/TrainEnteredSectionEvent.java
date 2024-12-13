package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

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
        currentSignallingSystem.setCurrentState(SignallingSystem.SignallingSystemState.OCCUPIED_SECTION);
        simulator.insert(new SemaphoreChangeEvent(getEventTime(), currentSignallingSystem.getMainLineSemaphore(), false));
    }

    private void notifyCurrentSystem(AbstractSimulator simulator, Direction direction) {
        currentSignallingSystem.setCurrentState(SignallingSystem.SignallingSystemState.OCCUPIED_SECTION);
        if (direction == Direction.FORWARD) {
            if (currentSignallingSystem.getEndSemaphore() != null) {
                simulator.insert(new SemaphoreChangeEvent(getEventTime(), currentSignallingSystem.getEndSemaphore(), Direction.FORWARD));
            }
            if (currentSignallingSystem.getStartSemaphore() != null) {
                simulator.insert(new SemaphoreChangeEvent(getEventTime(), currentSignallingSystem.getStartSemaphore(), Direction.BACKWARD));
            }
        } else {
            if (currentSignallingSystem.getEndSemaphore() != null) {
                simulator.insert(new SemaphoreChangeEvent(getEventTime(), currentSignallingSystem.getEndSemaphore(), Direction.BACKWARD));
            }
            if (currentSignallingSystem.getStartSemaphore() != null) {
                simulator.insert(new SemaphoreChangeEvent(getEventTime(), currentSignallingSystem.getStartSemaphore(), Direction.BACKWARD));
            }
        }
    }
}
