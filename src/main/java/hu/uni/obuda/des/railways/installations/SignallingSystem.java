package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SignallingSystem extends Track {

    public enum SignallingSystemState {
        FREE_SECTION,
        OCCUPIED_SECTION,
        WAITING_FOR_TRACK_SWITCH
    }

    private final Semaphore startSemaphore;
    private final Semaphore endSemaphore;

    private SignallingSystem previousSystem;
    private SignallingSystem nextSystem;
    private SignallingSystemState currentState;
    private Track track = null;

    public SignallingSystem(String id, int maxSpeed, Semaphore startSemaphore, Semaphore endSemaphore) {
        super(id, 0.001, maxSpeed);
        this.startSemaphore = startSemaphore;
        this.endSemaphore = endSemaphore;
        currentState = SignallingSystemState.FREE_SECTION;
    }

    public boolean requestAccess(Train train, Direction direction) {
        if (track.isOccupied()) return false; // Track already in use

        // Update semaphores
        if (direction == Direction.FORWARD) {
            startSemaphore.setGreen(Direction.FORWARD);
            endSemaphore.setRed();
            if (nextSystem != null) nextSystem.notifyIncomingTrain(train, Direction.FORWARD);
        } else {
            endSemaphore.setGreen(Direction.BACKWARD);
            startSemaphore.setRed();
            if (previousSystem != null) previousSystem.notifyIncomingTrain(train, Direction.BACKWARD);
        }

        return track.occupy(train, direction);
    }

    public void notifyIncomingTrain(Train train, Direction direction) {
        if (direction == Direction.FORWARD) {
            startSemaphore.setRed(); // Prevent new trains from entering this system
        } else {
            endSemaphore.setRed();
        }
    }

    public synchronized void release(Train train) {
        track.release();
        startSemaphore.setRed();
        endSemaphore.setRed();

        // Notify neighbors
        if (nextSystem != null) nextSystem.notifyTrainLeft(train, Direction.FORWARD);
        if (previousSystem != null) previousSystem.notifyTrainLeft(train, Direction.BACKWARD);
    }

    public synchronized void notifyTrainLeft(Train train, Direction direction) {
        if (direction == Direction.FORWARD) {
            startSemaphore.setGreen(Direction.FORWARD);
        } else {
            endSemaphore.setGreen(Direction.BACKWARD);
        }
    }

    @Override
    public String toString() {
        return "Signalling System: " + id;
    }
}
