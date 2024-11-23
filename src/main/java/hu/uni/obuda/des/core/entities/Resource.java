package hu.uni.obuda.des.core.entities;

public interface Resource {
    String getId();
    boolean occupy(Actor actor);
    boolean release();
}
