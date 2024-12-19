package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.MainSignal;
import hu.uni.obuda.des.railways.tracks.Direction;

import java.util.Arrays;

public class SemaphoreChangeEvent extends Event {
    private final MainSignal mainSignal;
    private final Direction[] greenDirections;
    private final Direction[] redDirections;

    public SemaphoreChangeEvent(double eventTime, MainSignal mainSignal, Direction[] greenDirections, Direction[] redDirections) {
        super(eventTime);
        if (greenDirections.length > 2)
            throw new IllegalArgumentException("Too many directions");
        if (redDirections.length > 2)
            throw new IllegalArgumentException("Too many directions");
        this.mainSignal = mainSignal;
        this.greenDirections = Arrays.copyOf(greenDirections, greenDirections.length);
        this.redDirections = Arrays.copyOf(redDirections, redDirections.length);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (mainSignal == null)
            return; // No connected semaphore
        for (var dir : greenDirections) {
            mainSignal.setGreen(dir);
        }
        for (var dir : redDirections) {
            mainSignal.setRed(dir);
        }
        printLog(mainSignal);
    }

    private void printLog(MainSignal mainSignal) {
        System.out.println("New state of semaphore " + mainSignal.getId() + " FORWARD: " + (mainSignal.isGreen(Direction.FORWARD) ? "Green" : "Red") + " BACKWARD: " + (mainSignal.isGreen(Direction.BACKWARD) ? "Green" : "Red"));
    }
}
