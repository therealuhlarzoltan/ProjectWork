package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Semaphore;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

import java.util.Objects;

public class TrainInspectsSemaphoreEvent extends TrainMovementEvent {
    private final Semaphore semaphore;
    private final double distanceToSemaphore;

    public TrainInspectsSemaphoreEvent(double eventTime, Train train, Track currentTrack, Semaphore semaphore, double distanceToSemaphore) {
        super(eventTime, train, currentTrack);
        this.semaphore = Objects.requireNonNull(semaphore);
        this.distanceToSemaphore = distanceToSemaphore;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        int speedLimit = getSemaphoresSpeedLimit(semaphore, train, Direction.FORWARD);
        if ((speedLimit <= track.getMaxSpeed()) && speedLimit > train.getCurrentSpeed()) {
            double speedingTime = calculateSpeedingTime();
        } else if (speedLimit < train.getCurrentSpeed()) {
            double breakingTime = calculateBreakingTime();
        } else {
            double cruisingTime = calculateCruisingTime(train.getCurrentSpeed(), distanceToSemaphore);

        }


        System.out.println("Train " + train + " inspects semaphore " + semaphore + " at " + getEventTime());

    }

    private int getSemaphoresSpeedLimit(Semaphore semaphore, Train train, Direction direction) {
        int semaphoresSignal = semaphore.getSpeedLimit(direction);
        if (semaphoresSignal == Integer.MAX_VALUE) {
            return findNextTracksSpeedLimit(semaphore, train);
        } else {
            return semaphoresSignal;
        }
    }

    private int findNextTracksSpeedLimit(Semaphore semaphore, Train train) {
        var route = train.getRouteAsList();
        int semaphoreIndex = route.indexOf(semaphore);
        if (semaphoreIndex != route.size() - 2) {
            return route.get(semaphoreIndex + 1).getMaxSpeed();
        } else {
            return 0;
        }
    }

    private double calculateBreakingTime() {
        // TODO: related to speed profiles
        return 0;
    }

    private double calculateSpeedingTime() {
        // TODO: related to speed profiles
        return 0;
    }

    private double calculateCruisingTime(double speed, double distance) {
        // TODO: related to speed profiles
        return distance / speed;
    }
}
