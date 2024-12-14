package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SignallingSystem extends Track {

    public enum SignallingSystemState {
        FREE_SECTION,
        OCCUPIED_SECTION
    }

    private Semaphore startSemaphore;
    private Semaphore endSemaphore;

    private SignallingSystem previousSystem;
    private SignallingSystem nextSystem;
    private SignallingSystemState currentState;
    private Track track = null;

    public SignallingSystem(String id, int maxSpeed) {
        super(id, 0.001, maxSpeed);
        currentState = SignallingSystemState.FREE_SECTION;
    }

    @Override
    public String toString() {
        return "Signalling System: " + id;
    }
}
