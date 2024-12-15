package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

public class TrainStopsEvent extends Event {
    private final Train train;
    private final Track nextTrack;
    private final Track currentTrack;

    public TrainStopsEvent(double eventTime, Train train, Track nextTrack, Track currentTrack) {
        super(eventTime);
        this.train = train;
        this.nextTrack = nextTrack;
        this.currentTrack = currentTrack;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        System.out.println(train.toString() + " stopped on " + currentTrack.toString());
        train.setCurrentSpeed(0);
        train.setCurrentTrack(currentTrack);
        simulator.insert(new TrainTravelsOnTrackEvent(getEventTime() + 1, train, nextTrack, currentTrack)); // Wait for 1 minute
    }
}
