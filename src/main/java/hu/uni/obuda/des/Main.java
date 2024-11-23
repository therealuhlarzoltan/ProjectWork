package hu.uni.obuda.des;

import hu.uni.obuda.des.core.simulation.EventQueue;
import hu.uni.obuda.des.core.simulation.Simulator;
import hu.uni.obuda.des.railways.events.movement.TrainArrivalEvent;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import hu.uni.obuda.des.railways.util.DefaultBoardingTimeCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Train train = Train.builder()
                .id("1")
                .capacity(420)
                .maxSpeed(160)
                .lengthInKm(0.6)
                .manufacturer("Stadler")
                .model("FlirtX2")
                .lineNumber("Z70")
                .departureStation("Szob")
                .arrivalStation("Budapest-Nyugati")
                .build();

        var btCalc = new DefaultBoardingTimeCalculator();
        var szob = new Station("Szob", btCalc);
        List<Station.Platform> szobPlatforms = List.of(
                new Station.Platform("1", szob, 0.8, 60),
                new Station.Platform("2", szob, 0.8, 60)
        );
        szob.addPlatforms(szobPlatforms);

        var budapestNyugati = new Station("Budapest-Nyugati", btCalc);
        List<Station.Platform> budapestNyugatiPlatforms = List.of(
                new Station.Platform("1", budapestNyugati, 0.9, 80),
                new Station.Platform("2", budapestNyugati, 0.9, 80)
        );
        budapestNyugati.addPlatforms(budapestNyugatiPlatforms);

        var dunakeszi = new Station("Dunakeszi", btCalc);
        List<Station.Platform> dunakesziPlatforms = List.of(
                new Station.Platform("1", dunakeszi, 0.7, 80),
                new Station.Platform("2", dunakeszi, 0.7, 80)
        );
        dunakeszi.addPlatforms(dunakesziPlatforms);

        var vac = new Station("Vac", btCalc);
        List<Station.Platform> vacPlatforms = List.of(
                new Station.Platform("1", vac, 0.8, 80),
                new Station.Platform("2", vac, 0.8, 80)
        );
        vac.addPlatforms(vacPlatforms);

        Map<String, List<Track>> tracks = Map.of(
                "Szob-Vác", List.of(new Track("Szob-Vac-1", 20, 80),
                            new Track("Szob-Vac-2", 10, 100)),
                "Vac-Dunakeszi", List.of(new Track("Vac-Dunakeszi-1", 20, 120)),
                "Dunakeszi-Budapest", List.of(new Track("Dunakeszi-Budapest-1", 13, 80))
        );

        List<Track> route = new ArrayList<>(tracks.get("Szob-Vác"));
        route.add(vac.getPlatforms()[0]);
        route.addAll(tracks.get("Vac-Dunakeszi"));
        route.add(dunakeszi.getPlatforms()[0]);
        route.addAll(tracks.get("Dunakeszi-Budapest"));
        route.add(budapestNyugati.getPlatforms()[0]);

        train.getRoute().addAll(route);
        train.setCurrentTrack(szob.getPlatforms()[0]);

        szob.addPassengers("Vac", 100);
        szob.addPassengers("Budapest-Nyugati", 200);

        vac.addPassengers("Szob", 40);
        vac.addPassengers("Dunakeszi", 50);
        vac.addPassengers("Budapest-Nyugati", 500);

        dunakeszi.addPassengers("Vac", 50);
        dunakeszi.addPassengers("Budapest-Nyugati", 100);

        train.addStops(szob, vac, budapestNyugati);

        EventQueue eventQueue = new EventQueue();
        eventQueue.insert(new TrainArrivalEvent(0, train, szob.getPlatforms()[0]));
        //eventQueue.insert(new TrainDepartureEvent(0, train, szob.getPlatforms()[0]));
        Simulator simulator = new Simulator(eventQueue);
        simulator.processAllEvents();
    }
}