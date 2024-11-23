package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.railways.tracks.Track;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Switch extends Track {
    private final Track mainTrack;
    private final Track connectedTrack1;
    private final Track connectedTrack2;

    public Switch(String id, int lengthInKm, int maxSpeed, Track mainTrack, Track connectedTrack1, Track connectedTrack2) {
        super(id, lengthInKm, maxSpeed);
        this.mainTrack = mainTrack;
        this.connectedTrack1 = connectedTrack1;
        this.connectedTrack2 = connectedTrack2;
        this.selectedTrack = connectedTrack1;
    }

    private final List<SignallingSystem> signallingSystems = new ArrayList<>(4);
    private final List<Semaphore> semaphores = new ArrayList<>(4);
    private Track selectedTrack;

    public boolean switchTrack() {
        if (currentTrain != null) {
            return false;
        }
        if (selectedTrack == connectedTrack1) {
            selectedTrack = connectedTrack2;
        } else {
            selectedTrack = connectedTrack1;
        }
        return true;
    }

    public void addSignallingSystem(SignallingSystem signallingSystem) {
        signallingSystems.add(signallingSystem);
    }

    public void addSemaphore(Semaphore semaphore) {
        semaphores.add(semaphore);
    }

}
