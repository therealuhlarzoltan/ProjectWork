package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Semaphore;
import hu.uni.obuda.des.railways.tracks.Direction;

import java.util.Arrays;

public class SemaphoreChangeEvent extends Event {
    private final Semaphore semaphore;
    private final Direction[] greenDirections;
    private final Direction[] redDirections;

    public SemaphoreChangeEvent(double eventTime, Semaphore semaphore, Direction[] greenDirections, Direction[] redDirections) {
        super(eventTime);
        if (greenDirections.length > 2)
            throw new IllegalArgumentException("Too many directions");
        if (redDirections.length > 2)
            throw new IllegalArgumentException("Too many directions");
        this.semaphore = semaphore;
        this.greenDirections = Arrays.copyOf(greenDirections, greenDirections.length);
        this.redDirections = Arrays.copyOf(redDirections, redDirections.length);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (semaphore == null)
            return; // No connected semaphore
        for (var dir : greenDirections) {
            semaphore.setGreen(dir);
        }
        for (var dir : redDirections) {
            semaphore.setRed(dir);
        }
        printLog(semaphore);
    }

    private void printLog(Semaphore semaphore) {
        System.out.println("New state of semaphore " + semaphore.getId() + " FORWARD: " + (semaphore.isGreen(Direction.FORWARD) ? "Green" : "Red") + " BACKWARD: " + (semaphore.isGreen(Direction.BACKWARD) ? "Green" : "Red"));
    }
}
