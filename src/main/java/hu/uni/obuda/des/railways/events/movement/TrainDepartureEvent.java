package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

public class TrainDepartureEvent extends TrainMovementEvent {

    public TrainDepartureEvent(double eventTime, Train train, Station.Platform platform) {
        super(eventTime, train, platform);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        Station.Platform platform = getPlatform();
        Track nextTrack = train.getRoute().poll();
        //Assuming the train stopped at the end of the platform ---> required travel time is approximately 0
        simulator.insert(new TrainTravelsOnTrackEvent(getEventTime(), train, nextTrack, platform));
        System.out.println("Train " + train.toString() + " departed from station " + platform.getStation().getName() + " at time " + getEventTime());
    }

    public Station.Platform getPlatform() {
        return (Station.Platform) track;
    }
}
