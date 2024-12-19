package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.railways.simulation.DirectionalResource;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.tracks.Track;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistantSignal extends Track {
    private Semaphore mainSignal;

    public DistantSignal(String id, int maxSpeed) {
        super(id, 0.0001, maxSpeed);
    }

    public int getMainSignalSpeedLimit(Direction direction) {
        if (isStartSignal()) {
            if (direction.equals(Direction.FORWARD)) {
                return mainSignal.getForwardSignal().getSpeedLimit();
            } else {
                return Integer.MAX_VALUE;
            }
        } else {
            if (direction.equals(Direction.FORWARD)) {
                return Integer.MAX_VALUE;
            } else {
                return mainSignal.getBackwardSignal().getSpeedLimit();
            }
        }
    }

    private boolean isStartSignal() {
       return true;
    }





}
