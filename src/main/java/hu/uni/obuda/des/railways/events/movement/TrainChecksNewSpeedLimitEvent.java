package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.MainSignal;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.List;

@Getter
public class TrainChecksNewSpeedLimitEvent extends TrainMovementEvent {
    private final double distanceToNextTrack;

    public TrainChecksNewSpeedLimitEvent(double eventTime, Train train, Track currentTrack, double distanceToNextTrack) {
        super(eventTime, train, currentTrack);
        this.distanceToNextTrack = distanceToNextTrack;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (checkIfTheresATrackBeforeSemaphore(train.getRouteAsList())) {
            var nextTrack = train.getRoute().poll();
            assert nextTrack != null : "Empty route provided inside TrainChecksNewSpeedLimitEvent";
            if (nextTrack.getMaxSpeed() < track.getMaxSpeed()) {
                // Train needs to slow down to the lower speed limit
                double vTarget = Math.min(nextTrack.getMaxSpeed(), train.getCurrentSpeed());
                double s = distanceToNextTrack;

                // Calculate time considering deceleration
                double deceleration = train.getMaxDeceleration();
                double vStart = train.getCurrentSpeed() * (5.0 / 18.0); // Convert to m/s
                double vTargetInMs = vTarget * (5.0 / 18.0); // Convert to m/s
                double timeToSlowDown = (vStart - vTargetInMs) / deceleration;
                double distanceDuringDeceleration = vStart * timeToSlowDown - 0.5 * deceleration * timeToSlowDown * timeToSlowDown;

                double remainingDistance = s - distanceDuringDeceleration;
                double totalTime = timeToSlowDown + (remainingDistance / vTargetInMs); // Add time to travel at vTarget

                // Update train's current state
                train.setCurrentSpeed((int)vTarget);
                train.setCurrentAcceleration(0);
                train.setTargetSpeed((int)vTarget);
                train.setTargetAcceleration(0);

                simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + totalTime, train, nextTrack, track));
            } else if (nextTrack.getMaxSpeed() > track.getMaxSpeed() && track.getMaxSpeed() > train.getCurrentSpeed() && track.getMaxSpeed() > train.getMaxSpeed()) {
                // Train needs to accelerate to the higher speed limit
                double vTarget = track.getMaxSpeed();
                double s = distanceToNextTrack;

                // Calculate time considering acceleration
                double acceleration = train.getMaxAcceleration();
                double vStart = train.getCurrentSpeed() * (5.0 / 18.0); // Convert to m/s
                double vTargetInMs = vTarget * (5.0 / 18.0); // Convert to m/s
                double timeToAccelerate = (vTargetInMs - vStart) / acceleration;
                double distanceDuringAcceleration = vStart * timeToAccelerate + 0.5 * acceleration * timeToAccelerate * timeToAccelerate;

                double remainingDistance = s - distanceDuringAcceleration;
                double totalTime = timeToAccelerate + (remainingDistance / vTargetInMs); // Add time to travel at vTarget

                // Update train's current state
                train.setCurrentSpeed((int)vTarget);
                train.setCurrentAcceleration(0);
                train.setTargetSpeed((int)vTarget);
                train.setTargetAcceleration(0);

                simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + totalTime, train, nextTrack, track));
            } else {
                // Train can continue at the current speed
                train.setTargetSpeed(train.getCurrentSpeed());
                train.setTargetAcceleration(0);
                double totalTime = distanceToNextTrack / train.getCurrentSpeed();
                simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + totalTime, train, nextTrack, track));
            }
        } else {
            // Semaphore is in sighting distance
            var nextSemaphore = train.getRouteAsList().stream().filter(track -> track instanceof MainSignal).findFirst().orElse(null);
            double distanceToNextSemaphore = findDistanceToNextSemaphore(train.getRouteAsList(), distanceToNextTrack);
            simulator.insert(new TrainInspectsSemaphoreEvent(getEventTime(), train, track, (MainSignal) nextSemaphore, distanceToNextSemaphore, distanceToNextTrack));
        }
    }

    private double findDistanceToNextSemaphore(List<Track> route, double distanceToNextTrack) {
        if (route.size() > 1) {
            double distance = 0;
            for (int i = 1; i < route.size(); i++) {
                if (route.get(i) instanceof MainSignal)
                    break;
                distance += route.get(i).getLengthInKm();
            }
            return distanceToNextTrack + distance;
        } else {
            return Double.NaN;
        }
    }

    private boolean checkIfTheresATrackBeforeSemaphore(List<Track> route) {
       if (route.stream().noneMatch(track -> track instanceof MainSignal)) {
           return true;
       }
       int semaphoreIndex = route.indexOf(route.stream().filter(track -> track instanceof MainSignal).findFirst().orElse(null));
       var routeBeforeSemaphore = route.subList(0, semaphoreIndex);
       double distanceBeforeSemaphore = routeBeforeSemaphore.stream().mapToDouble(Track::getLengthInKm).sum();
       distanceBeforeSemaphore += distanceToNextTrack;
       double sightingDistance = routeBeforeSemaphore.getLast().getMaxSpeed() * 10 * 0.33 * 1000;
       return distanceBeforeSemaphore > sightingDistance;
    }
}
