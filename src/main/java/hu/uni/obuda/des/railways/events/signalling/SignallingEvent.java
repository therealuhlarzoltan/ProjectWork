package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.events.movement.TrainLeavesTrackEvent;
import hu.uni.obuda.des.railways.events.movement.TrainMovementEvent;
import hu.uni.obuda.des.railways.events.movement.TrainTravelsOnTrackEvent;
import hu.uni.obuda.des.railways.installations.BlockSignallingSystem;
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
            System.out.println("Train's head passed signalling system in direction " + direction + " at time " + getEventTime());
            simulator.insert(new TrainEnteredSectionEvent(getEventTime(), train, (BlockSignallingSystem) movementEvent.getTrack(), direction));
        } else if (movementEvent instanceof TrainLeavesTrackEvent) {
            System.out.println("Train's tail passed signalling system in direction " + direction + " at time " + getEventTime());
            simulator.insert(new TrainLeftSectionEvent(getEventTime(), train, (BlockSignallingSystem) movementEvent.getTrack(), direction));
        } else {
            assert false : "Invalid movement event type";
        }


       /* double travelTime = signallingSystem.getLengthInKm() / Math.min(train.getMaxSpeed(), signallingSystem.getMaxSpeed());
        Track nextTrack = train.getRoute().poll();
        if (nextTrack instanceof Station.Platform && train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals(((Station.Platform) nextTrack).getStation())) {
            simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, (Station.Platform) nextTrack));
        } else {
            simulator.insert(new TrainTravelsEvent(getEventTime() + travelTime, train, signallingSystem, nextTrack));
        }*/
    }
}
