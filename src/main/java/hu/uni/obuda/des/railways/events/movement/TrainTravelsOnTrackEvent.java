package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.events.signalling.SignallingEvent;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.simulation.DirectionalResource;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

public class TrainTravelsOnTrackEvent extends TrainMovementEvent {
    private final Track previousTrack;

    public TrainTravelsOnTrackEvent(double eventTime, Train train, Track currentTrack, Track previousTrack) {
        super(eventTime, train, currentTrack);
        this.previousTrack = previousTrack;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        System.out.println("Train " + train.toString() + " is travelling on track " + track.toString() + " at time " + getEventTime());
        if (track instanceof SignallingSystem) {
            simulator.insert(new SignallingEvent(this, train, train.getCurrentDirection()));
            previousTrack.release();
            double travelTime = (track.getLengthInKm() / Math.max(train.getMaxSpeed(), track.getMaxSpeed())) * 60;
            Track nextTrack = train.getRoute().poll();
            if (nextTrack instanceof Station.Platform && train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals(((Station.Platform) nextTrack).getStation())) {
                simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, (Station.Platform) nextTrack));
            } else {
                simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + travelTime, train, nextTrack, track));
            }
        } else if (track instanceof DirectionalResource dirTrack) {
            if (!dirTrack.occupy(train, train.getCurrentDirection())) {
                simulator.insert(new TrainStopsEvent(getEventTime(), train, track, previousTrack));
            } else {
                previousTrack.release();
                double travelTime = (track.getLengthInKm() / Math.max(train.getMaxSpeed(), track.getMaxSpeed())) * 60;
                Track nextTrack = train.getRoute().poll();
                if (nextTrack instanceof Station.Platform && train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals(((Station.Platform) nextTrack).getStation())) {
                    simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, (Station.Platform) nextTrack));
                } else {
                    simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + travelTime, train, nextTrack, track));
                }
            }
        }
        else if (!track.occupy(train)) {
            simulator.insert(new TrainStopsEvent(getEventTime(), train, track, previousTrack));
        } else {
            previousTrack.release();
            double travelTime = (track.getLengthInKm() / Math.max(train.getMaxSpeed(), track.getMaxSpeed())) * 60;
            Track nextTrack = train.getRoute().poll();
            if (nextTrack instanceof Station.Platform && train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals(((Station.Platform) nextTrack).getStation())) {
                simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, (Station.Platform) nextTrack));
            } else {
                simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + travelTime, train, nextTrack, track));
            }
        }
    }
}
