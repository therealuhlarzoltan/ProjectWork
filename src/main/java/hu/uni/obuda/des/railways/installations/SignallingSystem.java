package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.railways.tracks.Track;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class SignallingSystem extends Track {
    private SignallingSystem previousSystem;
    private SignallingSystem nextSystem;
    private Switch connectedSwitch;
    private Semaphore mainLineSemaphore;
    private SignallingSystemState currentState;

    public enum SignallingSystemState {
        FREE_SECTION,
        OCCUPIED_SECTION,
        WAITING_FOR_TRACK_SWITCH
    }

    public SignallingSystem(String id, int maxSpeed) {
        super(id, 0.001, maxSpeed);
    }

    public SignallingSystem(String id, int maxSpeed, SignallingSystem previousSystem, SignallingSystem nextSystem, Switch connectedSwitch, Semaphore mainLineSemaphore) {
       this(id, maxSpeed);
        this.nextSystem = nextSystem;
        this.previousSystem = previousSystem;
        this.connectedSwitch = connectedSwitch;
        this.mainLineSemaphore = mainLineSemaphore;
        this.currentState = SignallingSystemState.FREE_SECTION;
    }

    private final List<Object> connectedInstallations = new ArrayList<>();

    public void addConnectedInstallation(Object installation) {
        connectedInstallations.add(installation);
    }

    @Override
    public String toString() {
        return "Signalling System: " + id;
    }
}
