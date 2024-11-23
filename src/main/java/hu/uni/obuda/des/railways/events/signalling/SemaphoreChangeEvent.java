package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Semaphore;

public class SemaphoreChangeEvent extends Event {
    private final Semaphore semaphore;
    private final boolean isGreen;

    public SemaphoreChangeEvent(double eventTime, Semaphore semaphore, boolean isGreen) {
        super(eventTime);
        this.semaphore = semaphore;
        this.isGreen = isGreen;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (semaphore == null)
            return; // No connected semaphore
        if (isGreen) {
            semaphore.setGreen();
        } else {
            semaphore.setRed();
        }
        System.out.println("Semaphore " + semaphore.getId() + " changed to " + (semaphore.isGreen() ? "green" : "red") +
                " at time " + getEventTime());
    }
}
