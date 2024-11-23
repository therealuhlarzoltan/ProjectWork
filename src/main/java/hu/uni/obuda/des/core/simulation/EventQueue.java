package hu.uni.obuda.des.core.simulation;

import hu.uni.obuda.des.core.events.AbstractEvent;

import java.util.Collection;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.Predicate;

public class EventQueue extends AbstractEventList {
    private final PriorityQueue<AbstractEvent> events = new PriorityQueue<>();

    public void insert(AbstractEvent event) {
        Objects.requireNonNull(event, "Event cannot be null");
        events.add(event);
    }

    public AbstractEvent removeFirst() {
        if (events.isEmpty())
            return null;

        return events.poll();
    }

    public AbstractEvent remove(AbstractEvent event) {
        Objects.requireNonNull(event, "Event cannot be null");
        AbstractEvent ret = null;
        for (var e : events) {
            if (e.equals(event)) {
                ret = e;
                break;
            }
        }
        if (ret == null)
            return null;
        events.remove(event);
        return ret;
    }

    public int remainingEvents() {
        return events.size();
    }

    public AbstractEvent getFirst() {
        return events.peek();
    }


    public void removeAll(Collection<AbstractEvent> events) {
        this.events.removeAll(events);
    }

    public void removeAll(Predicate<AbstractEvent> predicate) {
        events.removeIf(predicate);
    }

    public void removeAll() {
        events.clear();
    }
}
