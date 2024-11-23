package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.installations.Switch;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

@Getter
public class SignallingEvent extends Event {
    private final Train train;
    private final SignallingSystem signallingSystem;

    public SignallingEvent(double eventTime, Train train,  SignallingSystem signallingSystem) {
        super(eventTime);
        this.train = train;
        this.signallingSystem = signallingSystem;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        System.out.println("Train passed signalling system");
        var route = train.getRoute();
        if (signallingSystem.getConnectedInstallations().stream().anyMatch(i -> i instanceof Switch)) {
            // Route finding + toggling covering semaphores --> TODO
        } else {
            simulator.insert(new TrainLeftSectionEvent(getEventTime(), train, signallingSystem.getPreviousSystem()));
            simulator.insert(new TrainEnteredSectionEvent(getEventTime(), train, signallingSystem));
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
