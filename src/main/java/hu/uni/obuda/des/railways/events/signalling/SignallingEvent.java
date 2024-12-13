package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.installations.Switch;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.Objects;

@Getter
public class SignallingEvent extends Event {
    private final Train train;
    private final SignallingSystem signallingSystem;
    private final Direction direction;

    public SignallingEvent(double eventTime, Train train, SignallingSystem signallingSystem, Direction direction) {
        super(eventTime);
        this.train = Objects.requireNonNull(train);
        this.signallingSystem = Objects.requireNonNull(signallingSystem);
        this.direction = Objects.requireNonNull(direction);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        System.out.println("Train passed signalling system in direction " + direction);
        var route = train.getRoute();

        simulator.insert(new TrainLeftSectionEvent(getEventTime(), train, signallingSystem.getPreviousSystem(), direction);
        simulator.insert(new TrainEnteredSectionEvent(getEventTime(), train, signallingSystem, direction));

       /* double travelTime = signallingSystem.getLengthInKm() / Math.min(train.getMaxSpeed(), signallingSystem.getMaxSpeed());
        Track nextTrack = train.getRoute().poll();
        if (nextTrack instanceof Station.Platform && train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals(((Station.Platform) nextTrack).getStation())) {
            simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, (Station.Platform) nextTrack));
        } else {
            simulator.insert(new TrainTravelsEvent(getEventTime() + travelTime, train, signallingSystem, nextTrack));
        }*/
    }
}
