package hu.uni.obuda.des.railways.events.delays;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;

import hu.uni.obuda.des.railways.events.movement.TrainMovementEvent;
import hu.uni.obuda.des.railways.simulation.RailwaySimulator;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

import lombok.Getter;

@Getter
public class TrainCollisionEvent extends DelayEvent {
    private final Train secondTrain;
    private final Track track;

    public TrainCollisionEvent(double eventTime, Train firstTrain, Train secondTrain, Track track) {
        super(eventTime, Double.POSITIVE_INFINITY, firstTrain, CollisionType.TRAIN_COLLISION);
        this.secondTrain = secondTrain;
        this.track = track;
    }

    private enum CollisionType {
        TRAIN_COLLISION
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        System.out.println("Train " + train.toString() + " collided with train " + secondTrain.toString() + " on track " + track.toString());
        if (simulator instanceof RailwaySimulator rs) {
            rs.cancelAll(event -> {
                if (event instanceof TrainMovementEvent tme) {
                    return tme.getTrain().equals(train) || tme.getTrain().equals(secondTrain);
                } else {
                    return false;
                }
            });
        } else {
            throw new UnsupportedOperationException("This event can only be executed in a RailwaySimulator");
        }
    }
}
