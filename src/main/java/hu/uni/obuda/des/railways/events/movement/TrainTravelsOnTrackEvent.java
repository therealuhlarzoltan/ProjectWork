package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.events.signalling.SignallingEvent;
import hu.uni.obuda.des.railways.installations.TrackCircuit;
import hu.uni.obuda.des.railways.simulation.DirectionalResource;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

import java.util.Objects;

public class TrainTravelsOnTrackEvent extends TrainMovementEvent {
    private final Track previousTrack;

    public TrainTravelsOnTrackEvent(double eventTime, Train train, Track currentTrack, Track previousTrack) {
        super(eventTime, train, currentTrack);
        this.previousTrack = Objects.requireNonNull(previousTrack);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        //Logs current event
        System.out.println("Train " + train.toString() + " is travelling on track " + track.toString() + " at direction " + train.getCurrentDirection() + " at time " + getEventTime());
        //Tries to acquire track and performs necessary actions
        if (!tryToEnterTrack(simulator, train, track)) {
            //Handles failure to acquire track (stops the train)
            var stopEvent = createStopEvent(getEventTime(), train, track, previousTrack);
            simulator.insert(stopEvent);
        }
    }

    /**
     * Tries to enter the current section of the track.
     * If it succeeds, it schedules all the required events for moving the train.
     * @return True if the train successfully entered the track, false otherwise
     */
    private boolean tryToEnterTrack(AbstractSimulator simulator, Train train, Track currentTrack) {
        //Tries to acquire the track
        if (currentTrack instanceof DirectionalResource directionalTrack) {
            if (!directionalTrack.occupy(train, train.getCurrentDirection())) {
                return false;
            }
        } else {
            if (!currentTrack.occupy(train)) {
                return false;
            }
        }
        //Determines required non-movement events
        if (track instanceof TrackCircuit) {
            var signallingEvent = createSignallingEvent(this, train);
            simulator.insert(signallingEvent);
        }
        //Determines required movement events
        moveTrainHeadForward(simulator, train);
        moveTrainTailForward(simulator, train);

        return true;
    }

    /**
     * Calculates the time it takes for the train to reach the next section,
     * then inserts a TrainTravelsOnTrackEvent at the appropriate time.
     * If the next track is part of a station, schedules a stop if necessary.
     * @param simulator The simulator that the event is inserted into.
     * @param train The train that is moving forward.
     */
    private void moveTrainHeadForward(AbstractSimulator simulator, Train train) {
        double vStart = train.getCurrentSpeed() * (5.0 / 18.0); // Convert from km/h to m/s
        double vTarget = train.getTargetSpeed() * (5.0 / 18.0); // Convert from km/h to m/s
        double aMax = train.getMaxAcceleration(); // Already in m/s²
        double decMax = train.getMaxDeceleration(); // Already in m/s²
        double trackLength = Math.min(track.getLengthInKm() * 1000, train.getLengthInKm() * 1000); // Convert from km to m

        // Time and distance for acceleration
        double tAcc = (vTarget - vStart) / aMax;
        double sAcc = (vStart * tAcc) + (0.5 * aMax * tAcc * tAcc);

        // Time and distance for deceleration
        double tDec = vTarget / decMax;
        double sDec = (vTarget * tDec) - (0.5 * decMax * tDec * tDec);

        // Time and distance at constant speed
        double tConst = 0;
        if (sAcc + sDec < trackLength) {
            double sConst = trackLength - (sAcc + sDec);
            tConst = sConst / vTarget;
        }

        // Total time in seconds
        double totalTime = tAcc + tConst + tDec;

        // Convert total time to minutes
        double totalTimeInMinutes = totalTime / 60.0;

        /*
        double timeToNextTrack = (track.getLengthInKm() / Math.min(train.getMaxSpeed(), track.getMaxSpeed())) * 60;
        var nextTrack = train.getRoute().poll();

        //If the next track is part of a station, a scheduled stop might be required
        if (nextTrack instanceof Station.Platform platform) {
            moveThroughStation(simulator, train, platform, timeToNextTrack);
        } else {
            var nextTrackEvent = new TrainTravelsOnTrackEvent(getEventTime() + timeToNextTrack, train, nextTrack, track);
            simulator.insert(nextTrackEvent);
        }
         */
        simulator.insert(new TrainChecksCurrentSpeedLimitEvent(getEventTime() + totalTimeInMinutes, train, track, track.getLengthInKm() - trackLength));
    }

    /**
    *Calculates the time it takes for the train to exit the previous section,
    *then inserts a TrainLeavesTrackEvent at the appropriate time.
    * @param simulator The simulator that the event is inserted into
    * @param train The train that is leaving the track
     */
    private void moveTrainTailForward(AbstractSimulator simulator, Train train) {
        double vStart = train.getCurrentSpeed() * (5.0 / 18.0); // Convert from km/h to m/s
        double vTarget = train.getTargetSpeed() * (5.0 / 18.0); // Convert from km/h to m/s
        double aMax = train.getMaxAcceleration(); // Already in m/s²
        double decMax = train.getMaxDeceleration(); // Already in m/s²
        double trainLength = train.getLengthInKm() * 1000; // Convert train length from km to m

        // Time and distance for acceleration
        double tAcc = (vTarget - vStart) / aMax;
        double sAcc = (vStart * tAcc) + (0.5 * aMax * tAcc * tAcc);

        // Time and distance for deceleration
        double tDec = vTarget / decMax;
        double sDec = (vTarget * tDec) - (0.5 * decMax * tDec * tDec);

        // Time and distance at constant speed
        double tConst = 0;
        if (sAcc + sDec < trainLength) {
            double sConst = trainLength - (sAcc + sDec);
            tConst = sConst / vTarget;
        }

        // Total time in seconds
        double totalTime = tAcc + tConst + tDec;

        // Convert total time to minutes
        double totalTimeInMinutes = totalTime / 60.0;

        var trainLeavesTrackEvent = new TrainLeavesTrackEvent(getEventTime() + totalTimeInMinutes, train, previousTrack);
        simulator.insert(trainLeavesTrackEvent);
    }

    /**
     * Moves the train through the station, either by creating a TrainArrivalEvent or a TrainTravelsOnTrackEvent
     * depending on the train's next stop.
     * @param simulator The simulator that the event is inserted into
     * @param train The train that is moving through the station
     * @param nextTrack The Station.Platform that the train is moving to
     * @param travelTime The time it takes for the train to reach the station
     */
    private void moveThroughStation(AbstractSimulator simulator, Train train, Station.Platform nextTrack, double travelTime) {
        if (train.getStops().get(train.getStops().indexOf(train.getCurrentStation()) + 1).equals((nextTrack).getStation())) {
            simulator.insert(new TrainArrivalEvent(getEventTime() + travelTime, train, nextTrack));
        } else {
            simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + travelTime, train, nextTrack, track));
        }
    }

    /**
     * Creates a TrainStopsEvent to stop the train on the track.
     * @param eventTime The time when the train was stopped.
     * @param train The referred train that was stopped.
     * @param nextTrack The track that the train failed to enter.
     * @param currentTrack The track that the train remained on.
     * @return  A new TrainStopsEvent that is scheduled to occur immediately at the current time.
     */
    private TrainStopsEvent createStopEvent(double eventTime, Train train, Track nextTrack, Track currentTrack) {
        return new TrainStopsEvent(eventTime, train, nextTrack, currentTrack);
    }

    /**
     * Creates a SignallingEvent to notify the signalling system that the head of the train triggered the sensors.
     * @param movementEvent The event that triggered the signalling event.
     * @param train The train whose head triggered the signalling system's sensors.
     * @return  A new SignallingEvent that is scheduled to occur immediately at the current time.
     */
    private SignallingEvent createSignallingEvent(TrainMovementEvent movementEvent, Train train) {
        return new SignallingEvent(movementEvent, train, train.getCurrentDirection());
    }
}
