package hu.uni.obuda.des.railways.stations;

import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import hu.uni.obuda.des.railways.util.BoardingTimeCalculator;
import lombok.Getter;

import java.util.*;

@Getter
public class Station {
    private final String name;
    private Platform[] platforms;
    private final Map<String, Integer> waitingPassengers = new HashMap<>();
    private final BoardingTimeCalculator boardingTimeCalculator;

    @Getter
    public static class Platform extends Track {
        private final String name;
        private final Station station;

        public Platform(String name, Station station, double lengthInKm, int maxSpeedInKmPH) {
            super(name, lengthInKm, maxSpeedInKmPH);
            this.name = name;
            this.station = station;
        }

        public void boardTrain(Train train) {
            var remainingStations = train.getStops().subList(train.getStops().indexOf(train.getCurrentStation()) + 1, train.getStops().size());
           int passengersToBoard = 0;
            for (var station : remainingStations) {
                passengersToBoard += this.station.getWaitingPassengers().getOrDefault(station.getName(), 0);
            }
            if (passengersToBoard == 0)
                return;

            int passengersUnBoarding = train.getPassengersByDestination().getOrDefault(station.getName(), 0);
            if (passengersToBoard <= train.getCapacity() - train.getPassengers() - passengersUnBoarding) {
                for (var station : remainingStations) {
                    train.addPassengers(station.getName(), this.station.getWaitingPassengers().getOrDefault(station.getName(), 0));
                    this.station.waitingPassengers.computeIfPresent(station.getName(), (k, v) -> 0);
                }
            } else {
                var filteredEntries = this.station.waitingPassengers.entrySet()
                        .stream().filter(e -> remainingStations.stream().anyMatch(s -> s.getName().equals(e.getKey()))).toList();
                Map<String, Integer> evenDistribution = new HashMap<>();
                for (var entry : filteredEntries) {
                    evenDistribution.put(entry.getKey(), entry.getValue() * (train.getCapacity() - train.getPassengers() - passengersUnBoarding) / passengersToBoard);
                }
                for (var entry : evenDistribution.entrySet()) {
                    train.addPassengers(entry.getKey(), entry.getValue());
                    this.station.waitingPassengers.computeIfPresent(entry.getKey(), (k, v) -> v - entry.getValue());
                }
            }
        }

        public void unBoardTrain(Train train) {
            train.getPassengersByDestination().computeIfPresent(station.getName(), (k, v) -> 0);
        }

        @Override
        public String toString() {
            return station.getName() + " - Platform " + name;
        }

    }


    public Station(String name, BoardingTimeCalculator boardingTimeCalculator) {
        this.name = name;
        this.boardingTimeCalculator = boardingTimeCalculator;
    }

    public void addPlatforms(List<Platform> platforms) {
        this.platforms = platforms.toArray(new Platform[0]);
    }

    public void addPassengers(String destination, int count) {
        Objects.requireNonNull(destination, "Destination cannot be null");
        if (destination.isBlank())
            throw new IllegalArgumentException("Destination cannot be blank");
        waitingPassengers.computeIfPresent(destination, (k, v) -> v + count);
        waitingPassengers.putIfAbsent(destination, count);
    }

    public void removePassengers(String destination, int count) {
        Objects.requireNonNull(destination, "Destination cannot be null");
        if (destination.isBlank())
            throw new IllegalArgumentException("Destination cannot be blank");
        if (count < 0)
            throw new IllegalArgumentException("Passenger count cannot be negative");
        waitingPassengers.computeIfPresent(destination, (k, v) -> v - count);
        waitingPassengers.putIfAbsent(destination, 0);
    }
}
