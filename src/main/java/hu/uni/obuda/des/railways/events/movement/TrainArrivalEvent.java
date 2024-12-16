package hu.uni.obuda.des.railways.events.movement;

import hu.uni.obuda.des.core.simulation.AbstractSimulator;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.trains.Train;

public class TrainArrivalEvent extends TrainMovementEvent {

    public TrainArrivalEvent(double eventTime, Train train, Station.Platform platform) {
        super(eventTime, train, platform);
    }

    @Override
    public void execute(AbstractSimulator simulator) {
        Station.Platform platform = getPlatform();
        platform.occupy(train);
        train.setCurrentStation(platform.getStation());
        double unBoardingTime = platform.getStation()
                .getBoardingTimeCalculator()
                .calculateUnBoardingTime(platform, train);
        double boardingTime = platform.getStation()
                .getBoardingTimeCalculator()
                .calculateBoardingTime(platform, train);
        platform.unBoardTrain(train);
        platform.boardTrain(train);
        if (!train.getSchedule().containsKey(platform.getStation().getName())) {
            throw new IllegalStateException("Train " + train.toString() + " does not have a schedule for station " + platform.getStation().getName());
        }
        double departureTime = Math.max(getEventTime() + unBoardingTime + boardingTime, train.getSchedule().get(platform.getStation().getName()).getSecond());
        if (!train.getRoute().isEmpty()) {
            simulator.insert(new TrainDepartureEvent(departureTime, train, platform));
        } else {
            simulator.insert(new TrainTerminatesEvent(getEventTime(), train, platform));
        }
        System.out.println("Train " + train.toString() + " arrived at station " + platform.getStation().getName() + " Platform: " + platform.getName() + " at time " + getEventTime());
    }

   public Station.Platform getPlatform() {
        return (Station.Platform) track;
    }
}
