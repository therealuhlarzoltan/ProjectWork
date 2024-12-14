package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.events.signalling.SignallingEvent;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.simulation.DirectionalResource;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

public class TrainTravelsOnTrackEvent extends TrainMovementEvent {
    private final Track currentTrack;
    private final Track previousTrack;

    public TrainTravelsOnTrackEvent(double eventTime, Train train, Track currentTrack, Track previousTrack) {
        super(eventTime, train);
        this.currentTrack = currentTrack;
        this.previousTrack = previousTrack;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        System.out.println("Train " + train.toString() + " is travelling on track " + currentTrack.toString() + " at time " + getEventTime());
        if (currentTrack instanceof SignallingSystem) {
            simulator.insert(new SignallingEvent(getEventTime(), train, (SignallingSystem) currentTrack, train.getCurrentDirection()));
            previousTrack.release();
            double travelTime = (currentTrack.getLengthInKm() / Math.max(train.getMaxSpeed(), currentTrack.getMaxSpeed())) * 60;
            Track nextTrack = train.getRoute().poll();
            if (nextTrack instanceof Station.Platform && train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals(((Station.Platform) nextTrack).getStation())) {
                simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, (Station.Platform) nextTrack));
            } else {
                simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + travelTime, train, nextTrack, currentTrack));
            }
        } else if (currentTrack instanceof DirectionalResource dirTrack) {
            if (!dirTrack.occupy(train, train.getCurrentDirection())) {
                simulator.insert(new TrainStopsEvent(getEventTime(), train, currentTrack, previousTrack));
            } else {
                previousTrack.release();
                double travelTime = (currentTrack.getLengthInKm() / Math.max(train.getMaxSpeed(), currentTrack.getMaxSpeed())) * 60;
                Track nextTrack = train.getRoute().poll();
                if (nextTrack instanceof Station.Platform && train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals(((Station.Platform) nextTrack).getStation())) {
                    simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, (Station.Platform) nextTrack));
                } else {
                    simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + travelTime, train, nextTrack, currentTrack));
                }
            }
        }
        else if (!currentTrack.occupy(train)) {
            simulator.insert(new TrainStopsEvent(getEventTime(), train, currentTrack, previousTrack));
        } else {
            previousTrack.release();
            double travelTime = (currentTrack.getLengthInKm() / Math.max(train.getMaxSpeed(), currentTrack.getMaxSpeed())) * 60;
            Track nextTrack = train.getRoute().poll();
            if (nextTrack instanceof Station.Platform && train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals(((Station.Platform) nextTrack).getStation())) {
                simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, (Station.Platform) nextTrack));
            } else {
                simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + travelTime, train, nextTrack, currentTrack));
            }
        }
    }
}
