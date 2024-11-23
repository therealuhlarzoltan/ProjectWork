package hu.uni.obuda.des.railways.util;

import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.trains.Train;

public class DefaultBoardingTimeCalculator implements BoardingTimeCalculator {

    @Override
    public double calculateBoardingTime(Station.Platform platform, Train train) {
        var possibleStations = train.getStops().subList(train.getStops().indexOf(train.getCurrentStation()) + 1, train.getStops().size());
        int passengersWaiting = 0;
        var filteredEntries = platform.getStation().getWaitingPassengers().entrySet().stream()
                .filter(e -> possibleStations.stream().anyMatch(s -> s.getName().equals(e.getKey()))).toList();
        for (var entry : filteredEntries) {
            passengersWaiting += entry.getValue();
        };
        int passengerCount = Math.min(train.getCapacity() - train.getPassengers(), passengersWaiting);
        if (passengerCount <= 0)
            return 0;
        return 0.083 * passengerCount;
    }

    @Override
    public double calculateUnBoardingTime(Station.Platform platform, Train train) {
        int passengersToUnBoard = train.getPassengersByDestination().getOrDefault(platform.getStation().getName(), 0);
        return 0.083 * passengersToUnBoard;
    }
}
