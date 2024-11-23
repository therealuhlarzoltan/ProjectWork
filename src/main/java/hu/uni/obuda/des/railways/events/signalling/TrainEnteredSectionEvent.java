package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.SignallingSystem;
import hu.uni.obuda.des.railways.trains.Train;
import lombok.Getter;

@Getter
public class TrainEnteredSectionEvent extends Event {
    private final Train train;
    private final SignallingSystem currentSignallingSystem;

    public TrainEnteredSectionEvent(double eventTime, Train train, SignallingSystem currentSignallingSystem) {
        super(eventTime);
        this.currentSignallingSystem = currentSignallingSystem;
        this.train = train;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        if (currentSignallingSystem == null) {
            System.out.println(train.toString() + " entered section guarded by no signalling system");
        return; // No signalling system connected
        }
        System.out.println(train.toString() + " entered section guarded by signalling system " + currentSignallingSystem.toString());
        currentSignallingSystem.setCurrentState(SignallingSystem.SignallingSystemState.OCCUPIED_SECTION);
        simulator.insert(new SemaphoreChangeEvent(getEventTime(), currentSignallingSystem.getMainLineSemaphore(), false));
    }
}
