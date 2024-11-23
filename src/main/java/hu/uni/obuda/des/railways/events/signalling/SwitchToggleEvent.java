package hu.uni.obuda.des.railways.events.signalling;

import hu.uni.obuda.des.core.events.Event;
import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.installations.Switch;

public class SwitchToggleEvent extends Event {
    private final Switch trackSwitch;

    public SwitchToggleEvent(double eventTime, Switch trackSwitch) {
        super(eventTime);
        this.trackSwitch = trackSwitch;
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        trackSwitch.switchTrack();
        System.out.println("Switch " + trackSwitch.getId() + " toggled at time " + getEventTime());
    }
}
