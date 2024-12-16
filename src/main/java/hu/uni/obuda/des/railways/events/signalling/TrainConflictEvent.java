package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.BlockSignallingSystem;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

@Getter
public class TrainConflictEvent extends Event {
    private final BlockSignallingSystem conflictingBlockSignallingSystem;
    private final Train train1;
    private final Train train2;

    public TrainConflictEvent(double eventTime, BlockSignallingSystem conflictingBlockSignallingSystem, Train train1, Train train2) {
        super(eventTime);
        this.conflictingBlockSignallingSystem = conflictingBlockSignallingSystem;
        this.train1 = train1;
        this.train2 = train2;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        
    }
}
