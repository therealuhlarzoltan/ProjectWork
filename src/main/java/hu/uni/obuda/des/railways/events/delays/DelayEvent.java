package hu.uni.obuda.des.railways.events.delays;

import hu.uni.obuda.des.core.events.AbstractEvent;
import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

@Getter
public abstract class DelayEvent extends Event {
    protected final Train train;
    protected final double delayInMinutes;
    protected final Enum<?> delayCause;

    public DelayEvent(double eventTime, double delayInMinutes, Train train, Enum<?> delayCause) {
        super(eventTime);
        this.train = train;
        this.delayInMinutes = delayInMinutes;
        this.delayCause = delayCause;
    }
}
