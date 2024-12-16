package hu.uni.obuda.des.railways.tracks;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.core.entities.Resource;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.*;

import java.util.Objects;

@Getter
public class Track implements Resource {
    protected final String id;
    protected final double lengthInKm;
    protected final int maxSpeed;
    protected Train currentTrain;

    public Track(String id, double lengthInKm, int maxSpeed) {
        this.id = Objects.requireNonNull(id);
        this.lengthInKm = lengthInKm;
        this.maxSpeed = maxSpeed;
    }

    private boolean occupy(Train train) {
        currentTrain = train;
        train.setCurrentTrack(this);
        return true;
    }

    @Override
    public boolean occupy(Actor actor) {
        if (actor instanceof Train) {
            return occupy((Train) actor);
        } else {
            throw new UnsupportedOperationException("Only trains can occupy tracks");
        }
    }

    public boolean release() {
        currentTrain = null;
        return true;
    }

    public boolean isOccupied() {
        return currentTrain != null;
    }

    @Override
    public String toString() {
        return id;
    }
}
