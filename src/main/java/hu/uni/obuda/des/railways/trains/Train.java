package hu.uni.obuda.des.railways.trains;

import hu.uni.obuda.des.core.entities.Actor;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.tracks.Track;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.math3.util.Pair;

import java.util.*;

@Data
@Builder
public class Train implements Actor {
    private final String id;
    private final String manufacturer;
    private final String model;
    private final String lineNumber;
    private final int maxSpeed;
    private final double lengthInKm;
    private final int capacity;
    private final String departureStation;
    private final String arrivalStation;
    private final Queue<Track> route = new LinkedList<>();
    private final List<Station> stops = new ArrayList<>();
    private Station currentStation;
    private final Map<String, Integer> passengersByDestination = new HashMap<>();
    private final Map<String, Pair<Double, Double>> schedule = new HashMap<>();

    private int currentSpeed;
    private int passengers;
    private Track currentTrack;

    public void addStops(Station... stations) {
        Objects.requireNonNull(stations, "Stations cannot be null");
        if (stations.length == 0)
            throw new IllegalArgumentException("Stations cannot be empty");
        stops.addAll(Arrays.asList(stations));
    }

    public List<Station> getStops() {
        return new ArrayList<>(stops);
    }

    public Map<String, Pair<Double, Double>> getSchedule() {
        return Collections.unmodifiableMap(schedule);
    }

    public int addPassengers(String destination, int count) {
        Objects.requireNonNull(destination, "Destination cannot be null");
        if (destination.isBlank())
            throw new IllegalArgumentException("Destination cannot be blank");
        if (count < 0)
            throw new IllegalArgumentException("Passenger count cannot be negative");
        int passengersToAdd = Math.min(count, capacity - passengers);
        passengersByDestination.computeIfPresent(destination, (k, v) -> v + passengersToAdd);
        passengersByDestination.putIfAbsent(destination, passengersToAdd);
        passengers += passengersToAdd;
        return passengersToAdd;
    }

    @Override
    public String toString() {
        return lineNumber + " [" + departureStation + "->" + arrivalStation + "] (Train number: " + id + ") {" + manufacturer + " " + model + "}";
    }
}
