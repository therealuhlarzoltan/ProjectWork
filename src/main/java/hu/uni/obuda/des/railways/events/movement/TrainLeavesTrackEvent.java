package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TrainLeavesTrackEvent extends TrainMovementEvent {
    private final Track previousTrack;

    public TrainLeavesTrackEvent(double eventTime, Train train, Track previousTrack) {
        super(eventTime, Objects.requireNonNull(train));
        this.previousTrack = Objects.requireNonNull(previousTrack);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        System.out.println("Train " + train.toString() + " leaves the track " + previousTrack.getId() + " at time " + getEventTime());
    }
}
