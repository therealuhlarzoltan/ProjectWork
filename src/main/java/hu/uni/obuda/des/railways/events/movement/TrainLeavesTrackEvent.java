package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TrainLeavesTrackEvent extends TrainMovementEvent {

    public TrainLeavesTrackEvent(double eventTime, Train train, Track trackLeft) {
        super(eventTime, train, trackLeft);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        System.out.println("Train " + train.toString() + " leaves the track " + track.getId() + " at time " + getEventTime());
    }
}
