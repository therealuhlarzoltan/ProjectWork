package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.railways.tracks.Track;
import lombok.Getter;

@Getter
public class Semaphore extends Track {
    private boolean isGreen;

    public Semaphore(String id, int maxSpeed) {
        super(id, 0.001, maxSpeed);
        isGreen = true;
    }

    public void setGreen() {
        isGreen = true;
    }

    public void setRed() {
        isGreen = false;
    }

    @Override
    public boolean occupy(Actor actor) {
        if (isGreen) {
            return super.occupy(actor);
        }
        return false;
    }

}
