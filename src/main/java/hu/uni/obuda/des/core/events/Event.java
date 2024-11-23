package hu.uni.obuda.des.core.events;

import lombok.Getter;

@Getter
public non-sealed abstract class Event extends AbstractEvent {
    private final double eventTime;

    protected Event(double eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public int compareTo(AbstractEvent o) {
        Event event = (Event) o; // Only Event can extend AbstractEvent directly -> o is always an Event
        return Double.compare(this.eventTime, event.eventTime);
    }
}
