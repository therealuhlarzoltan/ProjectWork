package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.MainSignal;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

import java.util.List;

@Getter
public class TrainChecksCurrentSpeedLimitEvent extends TrainMovementEvent {
    private final double distanceToNextTrack;

    public TrainChecksCurrentSpeedLimitEvent(double eventTime, Train train, Track currentTrack, double distanceToNextTrack) {
        super(eventTime, train, currentTrack);
        this.distanceToNextTrack = distanceToNextTrack;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        double distanceToNextSemaphore = findDistanceToNextSemaphore(train.getRouteAsList(), distanceToNextTrack);
        double mainSignalSightingDistance = speedLimitSightingDistance(track, train);
        if (distanceToNextSemaphore <= mainSignalSightingDistance) {
            // TODO: Implement the logic for the train to inspect the semaphore --> no room to accelerate
            simulator.insert(new TrainChecksNewSpeedLimitEvent(getEventTime(), train, track, distanceToNextTrack));
        } else {
            if (mainSignalSightingDistance <= distanceToNextTrack) {
                // TODO: Schedule TrainChecksNewSpeedLimitEvent --> no room to accelerate
                simulator.insert(new TrainChecksNewSpeedLimitEvent(getEventTime(), train, track, distanceToNextTrack));
            } else {
                // TODO: Calculate acceleration
                double sToAccelerate = (distanceToNextTrack * 1000) - mainSignalSightingDistance; // Convert distance to meters
                double vStart = train.getCurrentSpeed() * (5.0 / 18.0); // Convert to m/s
                double vTarget = Math.min(track.getMaxSpeed(), train.getMaxSpeed()) * (5.0 / 18.0); // Convert to m/s
                double aMax = train.getMaxAcceleration(); // m/s²

                double achievableSpeed = vStart + Math.sqrt(2 * aMax * sToAccelerate); // Maximum achievable speed
                achievableSpeed = Math.min(achievableSpeed, vTarget); // Cap at target speed

                double sToReachAchievableSpeed = Math.pow(achievableSpeed - vStart, 2) / (2 * aMax); // Distance to reach achievable speed
                double timeToReachAchievableSpeed = (achievableSpeed - vStart) / aMax; // Time to reach achievable speed

                double remainingDistance = sToAccelerate - sToReachAchievableSpeed;
                double timeAtAchievableSpeed = remainingDistance > 0 ? remainingDistance / achievableSpeed : 0;

                double totalTime = timeToReachAchievableSpeed + timeAtAchievableSpeed;
                totalTime /= 60; // Convert to minutes

                // Update train's current state
                train.setCurrentSpeed((int)(achievableSpeed * (18.0 / 5.0))); // Convert back to km/h
                train.setCurrentAcceleration(0); // At the end of the distance, no more acceleration

                double remainingDistanceToNextTrack = distanceToNextTrack - (sToAccelerate / 1000); // Convert back to km

                // TODO: Schedule TrainChecksNewSpeedLimitEvent
                simulator.insert(new TrainChecksNewSpeedLimitEvent(getEventTime() + totalTime, train, track, remainingDistanceToNextTrack));
            }
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
            return (distanceToNextTrack + distance) * 1000; // m
        } else {
            return Double.NaN;
        }
    }

    private double speedLimitSightingDistance(Track currentTrack, Train train) {
        int sSpeed = currentTrack.getMaxSpeed() * 10; // km/h
        double sConstant = 0.33; // km
        double s = sSpeed * sConstant;
        s *= 1000; // m
        return Math.min(200, s);
    }
}
