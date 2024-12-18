package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Semaphore;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.List;

@Getter
public class TrainDetectsNewSpeedLimitEvent extends TrainMovementEvent {
    private final double distanceToNextTrack;

    public TrainDetectsNewSpeedLimitEvent(double eventTime, Train train, Track currentTrack, double distanceToNextTrack) {
        super(eventTime, train, currentTrack);
        this.distanceToNextTrack = distanceToNextTrack;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        double distanceToNextSemaphore = findDistanceToNextSemaphore(train.getRouteAsList(), distanceToNextTrack);
        if (distanceToNextSemaphore == distanceToNextTrack) {
            // TODO: Implement the logic for the train to inspect the semaphore
        } else {
            // TODO: Implement the logic for the train to schedule the next TrainTravelsOnTrackEvent
        }
        // TODO: Log the event
    }


    private double findDistanceToNextSemaphore(List<Track> route, double distanceToNextTrack) {
        if (route.size() > 1) {
            double distance = 0;
            for (int i = 1; i < route.size(); i++) {
                if (route.get(i) instanceof Semaphore)
                    break;
                distance += route.get(i).getLengthInKm();
            }
            return distanceToNextTrack + distance;
        } else {
            return Double.NaN;
        }
    }
}
