package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.events.movement.TrainLeavesTrackEvent;
import hu.uni.obuda.des.railways.events.movement.TrainMovementEvent;
import hu.uni.obuda.des.railways.events.movement.TrainTravelsOnTrackEvent;
import hu.uni.obuda.des.railways.installations.BlockSignallingSystem;
import hu.uni.obuda.des.railways.installations.TrackCircuit;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.Objects;

@Getter
public class SignallingEvent extends Event {
    private final Train train;
    private final Direction direction;
    private final TrainMovementEvent movementEvent;

    public SignallingEvent(TrainMovementEvent movementEvent, Train train, Direction direction) {
        super(movementEvent.getEventTime());
        this.train = Objects.requireNonNull(train);
        this.direction = Objects.requireNonNull(direction);
        this.movementEvent = Objects.requireNonNull(movementEvent);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (movementEvent instanceof TrainTravelsOnTrackEvent) {
            System.out.println("Train's head passed track circuit " + movementEvent.getTrack().toString() + " in direction " + direction + " at time " + getEventTime());
            if (direction.equals(Direction.FORWARD)) {
                simulator.insert(new TrainEnteredSectionEvent(getEventTime(), train, ((TrackCircuit) movementEvent.getTrack()).getNextBlockSignallingSystem(), direction));
            } else if (direction.equals(Direction.BACKWARD)) {
                simulator.insert(new TrainEnteredSectionEvent(getEventTime(), train, ((TrackCircuit) movementEvent.getTrack()).getPreviousBlockSignallingSystem(), direction));
            } else {
                assert false : "Invalid direction";
            }
        } else if (movementEvent instanceof TrainLeavesTrackEvent) {
            System.out.println("Train's tail passed track circuit " + movementEvent.getTrack().toString() + " in direction " + direction + " at time " + getEventTime());
            if (direction.equals(Direction.FORWARD)) {
                simulator.insert(new TrainLeftSectionEvent(getEventTime(), train, ((TrackCircuit) movementEvent.getTrack()).getPreviousBlockSignallingSystem(), direction));
            } else if (direction.equals(Direction.BACKWARD)) {
                simulator.insert(new TrainLeftSectionEvent(getEventTime(), train, ((TrackCircuit) movementEvent.getTrack()).getPreviousBlockSignallingSystem(), direction));
            } else {
                assert false : "Invalid direction";
            }
        } else {
            assert false : "Invalid movement event type";
        }
    }
}
