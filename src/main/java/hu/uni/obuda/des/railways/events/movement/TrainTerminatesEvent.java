package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.core.simulation.Simulator;
import hu.uni.obuda.des.railways.simulation.RailwaySimulator;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

public class TrainTerminatesEvent extends TrainMovementEvent {
    public TrainTerminatesEvent(double eventTime, Train train, Track track) {
        super(eventTime, train, track);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (simulator instanceof RailwaySimulator sim) {
            System.out.println("Train " + train.toString() + " terminates at " + track.toString() + " at time " + getEventTime());
            sim.cancelAll(event -> event instanceof TrainMovementEvent && ((TrainMovementEvent) event).getTrain().equals(train));
        } else {
            throw new UnsupportedOperationException("This event can only be executed in a RailwaySimulator");
        }
    }
}
