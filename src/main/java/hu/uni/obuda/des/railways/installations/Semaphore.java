package hu.uni.obuda.des.railways.installations;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.railways.simulation.DirectionalResource;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.tracks.Track;
import lombok.Getter;

@Getter
public class Semaphore extends Track implements DirectionalResource {
    public enum RailwaySignal {
        PROCEED(Integer.MAX_VALUE),
        MAX_SPEED_120(120),
        MAX_SPEED_80(80),
        MAX_SPEED_40(40),
        MAX_SPEED_15(15),
        STOP(0);

        private final int speedLimitInKmPerHour;

        RailwaySignal(int speedLimitInKmPerHour) {
            this.speedLimitInKmPerHour = speedLimitInKmPerHour;
        }

        public int getSpeedLimit() {
            return speedLimitInKmPerHour;
        }
    }

    private RailwaySignal forwardSignal;
    private RailwaySignal backwardSignal;

    public Semaphore(String id, int maxSpeed) {
        super(id, 0.0001, maxSpeed);
        forwardSignal = RailwaySignal.PROCEED;
        backwardSignal = RailwaySignal.PROCEED;
    }

    public void setGreen(Direction direction) {
        if (direction == Direction.FORWARD)
           forwardSignal = RailwaySignal.PROCEED;
        else if (direction == Direction.BACKWARD)
            backwardSignal = RailwaySignal.PROCEED;
    }

    public void setRed(Direction direction) {
        if (direction == Direction.FORWARD)
            forwardSignal = RailwaySignal.STOP;
        else if (direction == Direction.BACKWARD)
            backwardSignal = RailwaySignal.STOP;
    }

    public boolean isGreen(Direction direction) {
        if (direction == Direction.FORWARD)
            return forwardSignal == RailwaySignal.PROCEED;
        else
            return backwardSignal == RailwaySignal.PROCEED;
    }

    public boolean occupy(Actor actor, Direction direction) {
        if (direction == Direction.FORWARD) {
            if (forwardSignal.getSpeedLimit() > 0) {
                return super.occupy(actor);
            }
        } else if (direction == Direction.BACKWARD) {
            if (backwardSignal.getSpeedLimit() > 0) {
                return super.occupy(actor);
            }
        }
        return false;
    }

    public int getSpeedLimit(Direction direction) {
        if (direction == Direction.FORWARD)
            return forwardSignal.getSpeedLimit();
        else
            return backwardSignal.getSpeedLimit();
    }

    @Override
    public boolean occupy(Actor actor) {
        throw new UnsupportedOperationException("Semaphore must be occupied with direction");
    }
}
