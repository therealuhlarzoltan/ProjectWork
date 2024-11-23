package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

@Getter
public abstract class TrainMovementEvent extends Event {
    protected final Train train;

    public TrainMovementEvent(double eventTime, Train train) {
        super(eventTime);
        this.train = train;
    }
}
