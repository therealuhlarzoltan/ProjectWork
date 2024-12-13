package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Semaphore;
import hu.uni.obuda.des.railways.tracks.Direction;

import java.util.List;
import java.util.Objects;

public class SemaphoreChangeEvent extends Event {
    private final Semaphore semaphore;
    private final Direction[] greenDirections;

    public SemaphoreChangeEvent(double eventTime, Semaphore semaphore, Direction... greenDirections) {
        super(eventTime);
        if (greenDirections.length > 2)
            throw new IllegalArgumentException("Too many directions");
        this.semaphore = semaphore;
        this.greenDirections = greenDirections;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (semaphore == null)
            return; // No connected semaphore
        if (greenDirections.length != 0) {
           if (greenDirections.length == 1) {
               if (greenDirections[0] == Direction.FORWARD) {
                   semaphore.setGreen(Direction.FORWARD);
                   semaphore.setRed(Direction.BACKWARD);
               } else {
                   semaphore.setGreen(Direction.BACKWARD);
                   semaphore.setRed(Direction.FORWARD);
               }
           } else {
               semaphore.setGreen(Direction.FORWARD);
               semaphore.setGreen(Direction.BACKWARD);
           }
        } else {
            semaphore.setRed(Direction.FORWARD);
            semaphore.setRed(Direction.BACKWARD);
        }
        printLog(semaphore, greenDirections);
    }

    private void printLog(Semaphore semaphore, Direction[] greenDirections) {
       if (greenDirections.length == 0) {
           System.out.println("Semaphore " + semaphore.getId() + " changed to red at both directions");
       } else if (greenDirections.length == 1) {
           if (greenDirections[0] == Direction.FORWARD)
               System.out.println("Semaphore " + semaphore.getId() + " changed to green in FORWARD direction and red to BACKWARD direction");
           else
               System.out.println("Semaphore " + semaphore.getId() + " changed to green in BACKWARD direction and red to FORWARD direction");
       } else {
           System.out.println("Semaphore " + semaphore.getId() + " changed to green in both directions");
       }
    }
}
