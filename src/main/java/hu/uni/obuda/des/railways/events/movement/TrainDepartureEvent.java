package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

public class TrainDepartureEvent extends TrainMovementEvent {
    private final Station.Platform platform;

    public TrainDepartureEvent(double eventTime, Train train, Station.Platform platform) {
        super(eventTime, train);
        this.platform = platform;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        double travelTime = (platform.getLengthInKm() / Math.max(train.getMaxSpeed(), platform.getMaxSpeed())) * 60;
        Track nextTrack = train.getRoute().poll();
        simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + travelTime, train, nextTrack, platform));
        System.out.println("Train " + train.toString() + " departed from station " + platform.getStation().getName() + " at time " + getEventTime());
    }
}
