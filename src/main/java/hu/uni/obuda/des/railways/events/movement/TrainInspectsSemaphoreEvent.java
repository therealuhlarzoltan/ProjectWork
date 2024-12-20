package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.MainSignal;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

import java.util.Objects;

public class TrainInspectsSemaphoreEvent extends TrainMovementEvent {
    private final MainSignal mainSignal;
    private final double distanceToSemaphore;
    private final double distanceToNextTrack;

    public TrainInspectsSemaphoreEvent(double eventTime, Train train, Track currentTrack, MainSignal mainSignal, double distanceToSemaphore, double distanceToNextTrack) {
        super(eventTime, train, currentTrack);
        this.mainSignal = Objects.requireNonNull(mainSignal);
        this.distanceToSemaphore = distanceToSemaphore;
        this.distanceToNextTrack = distanceToNextTrack;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        int speedLimit = getSemaphoresSpeedLimit(mainSignal, train, Direction.FORWARD);
        if ((speedLimit <= track.getMaxSpeed()) && speedLimit > train.getCurrentSpeed()) {
            double vTarget = Math.min(speedLimit, train.getMaxSpeed());
        } else if (speedLimit < train.getCurrentSpeed()) {
            double vTarget = speedLimit;

        } else {
           var nextTrack = train.getRoute().poll();
           assert nextTrack != null : "Empty route provided inside TrainInspectsSemaphoreEvent";


           double totalTime = 0;
           simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + totalTime, train, nextTrack, track));
        }


        System.out.println("Train " + train + " inspects semaphore " + mainSignal + " at " + getEventTime());

    }

    private int getSemaphoresSpeedLimit(MainSignal mainSignal, Train train, Direction direction) {
        int semaphoresSignal = mainSignal.getSpeedLimit(direction);
        if (semaphoresSignal == Integer.MAX_VALUE) {
            return findNextTracksSpeedLimit(mainSignal, train);
        } else {
            return semaphoresSignal;
        }
    }

    private int findNextTracksSpeedLimit(MainSignal mainSignal, Train train) {
        var route = train.getRouteAsList();
        int semaphoreIndex = route.indexOf(mainSignal);
        if (semaphoreIndex != route.size() - 2) {
            return route.get(semaphoreIndex + 1).getMaxSpeed();
        } else {
            return 0;
        }
    }
}
