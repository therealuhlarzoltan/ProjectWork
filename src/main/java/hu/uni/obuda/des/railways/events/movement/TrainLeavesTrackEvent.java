package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.events.signalling.SignallingEvent;
import hu.uni.obuda.des.railways.installations.TrackCircuit;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

@Getter
public class TrainLeavesTrackEvent extends TrainMovementEvent {

    public TrainLeavesTrackEvent(double eventTime, Train train, Track trackLeft) {
        super(eventTime, train, trackLeft);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        //Logs current event
        System.out.println("Train " + train.toString() + " leaves the track " + track.getId() + " at time " + getEventTime());
        //Checks if signalling required
        if (track instanceof TrackCircuit) {
            var signallingEvent = createSignallingEvent(this, train);
            simulator.insert(signallingEvent);
        }
        //Releases the track
        track.release();
    }

    /**
     * Creates a SignallingEvent to notify the signalling system that the tail of the train triggered the sensors.
     * @param movementEvent The event that triggered the signalling event.
     * @param train The train whose tail triggered the signalling system's sensors.
     * @return  A new SignallingEvent that is scheduled to occur immediately at the current time.
     */
    private SignallingEvent createSignallingEvent(TrainMovementEvent movementEvent, Train train) {
        return new SignallingEvent(movementEvent, train, train.getCurrentDirection());
    }

}
