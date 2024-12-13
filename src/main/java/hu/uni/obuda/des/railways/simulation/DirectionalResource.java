package hu.uni.obuda.des.railways.simulation;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.core.entities.Resource;
import hu.uni.obuda.des.railways.tracks.Direction;

public interface DirectionalResource extends Resource {
    boolean occupy(Actor actor, Direction direction);
}