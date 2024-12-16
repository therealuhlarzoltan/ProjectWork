package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.railways.simulation.DirectionalResource;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BlockSignallingSystem implements DirectionalResource {

    public enum SignallingSystemState {
        FREE_SECTION,
        OCCUPIED_SECTION
    }

    private final String id;

    private Semaphore startSemaphore;
    private Semaphore endSemaphore;

    private TrackCircuit startTrackCircuit;
    private TrackCircuit endTrackCircuit;

    private BlockSignallingSystem previousSystem;
    private BlockSignallingSystem nextSystem;

    private Train currentTrain;
    private SignallingSystemState currentState;

    public BlockSignallingSystem(String id) {
        this.id = id;
        currentState = SignallingSystemState.FREE_SECTION;
    }

    @Override
    public boolean occupy(Actor actor, Direction direction) {
        return false;
    }

    @Override
    public boolean occupy(Actor actor) {
        return false;
    }

    @Override
    public boolean release() {

        return true;
    }

    @Override
    public String toString() {
        return "Signalling System: " + id;
    }
}
