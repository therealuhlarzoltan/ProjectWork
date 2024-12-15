package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class TrainMovementEvent extends Event {
    protected final Train train;
    protected final Track track;

    public TrainMovementEvent(double eventTime, Train train, Track track) {
        super(eventTime);
        this.train = Objects.requireNonNull(train);
        this.track = Objects.requireNonNull(track);
    }
}
