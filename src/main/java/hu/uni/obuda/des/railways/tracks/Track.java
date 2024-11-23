package hu.uni.obuda.des.railways.tracks;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.core.entities.Resource;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.*;

@Getter
public class Track implements Resource {
    protected final String id;
    protected final double lengthInKm;
    protected final int maxSpeed;
    protected Train currentTrain;

    public Track(String id, double lengthInKm, int maxSpeed) {
        this.id = id;
        this.lengthInKm = lengthInKm;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public boolean occupy(Actor actor) {
    /*  if (currentTrain == null) {
            currentTrain = (Train) actor;
            return true;
        }
        return false;*/
        currentTrain = (Train) actor;
        return true;
    }

    @Override
    public boolean release() {
       /* if (currentTrain != null) {
            currentTrain = null;
            return true;
        }
        return false;*/
        currentTrain = null;
        return true;
    }

    @Override
    public String toString() {
        return id;
    }
}
