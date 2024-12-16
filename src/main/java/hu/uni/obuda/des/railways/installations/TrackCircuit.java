package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.railways.tracks.Track;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackCircuit extends Track {
    private BlockSignallingSystem blockSignallingSystem;

    public TrackCircuit(String id, int maxSpeed) {
        super(id, 0.001, maxSpeed);
    }

    public boolean isStartCircuit() {
        return blockSignallingSystem.getStartTrackCircuit() == this;
    }
}
