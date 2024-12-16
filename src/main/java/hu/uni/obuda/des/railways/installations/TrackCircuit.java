package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.railways.tracks.Track;

public class TrackCircuit extends Track {
    public TrackCircuit(String id, int maxSpeed) {
        super(id, 0.001, maxSpeed);
    }
}
