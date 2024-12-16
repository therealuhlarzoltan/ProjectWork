package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.core.entities.Resource;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BlockSignallingSystem implements Resource {

    public enum BlockSignallingSystemState {
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
    private BlockSignallingSystemState currentState;

    public BlockSignallingSystem(String id) {
        this.id = id;
        currentState = BlockSignallingSystemState.FREE_SECTION;
    }

   private void entersSection(Train train) {
        currentTrain = train;
        currentState = BlockSignallingSystemState.OCCUPIED_SECTION;
    }

    private void exitsSection() {
        currentTrain = null;
        currentState = BlockSignallingSystemState.FREE_SECTION;
    }

    @Override
    public boolean occupy(Actor actor) {
       if (actor instanceof Train train) {
            if (!currentState.equals(BlockSignallingSystemState.FREE_SECTION)) {
                return false;
            }
            entersSection(train);
        } else {
            throw new UnsupportedOperationException("Only trains can occupy signalling systems");
        }
        return true;
    }

    @Override
    public boolean release() {
        exitsSection();
        return true;
    }

    @Override
    public String toString() {
        return "Signalling System: " + id;
    }
}
