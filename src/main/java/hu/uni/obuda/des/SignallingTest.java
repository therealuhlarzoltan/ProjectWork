package hu.uni.obuda.des;

import hu.uni.obuda.des.core.simulation.EventQueue;
import hu.uni.obuda.des.core.simulation.Simulator;
import hu.uni.obuda.des.railways.events.movement.TrainArrivalEvent;
import hu.uni.obuda.des.railways.installations.Semaphore;
import hu.uni.obuda.des.railways.installations.BlockSignallingSystem;
import hu.uni.obuda.des.railways.installations.TrackCircuit;
import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.tracks.Direction;
import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;
import hu.uni.obuda.des.railways.util.BoardingTimeCalculator;
import hu.uni.obuda.des.railways.util.DefaultBoardingTimeCalculator;

import java.util.List;

public class SignallingTest {
    public static void main(String[] args) {
        BoardingTimeCalculator btc = new DefaultBoardingTimeCalculator();
        Station start = new Station("Start Station", btc);
        Station end = new Station("End Station", btc);
        Station.Platform platform1 = new Station.Platform("1", start, 1, 80);
        Station.Platform platform2 = new Station.Platform("2", end, 1, 80);
        start.addPlatforms(List.of(platform1));
        end.addPlatforms(List.of(platform2));
        TrackCircuit circuit1 = new TrackCircuit("Circuit1", 120);
        Track track1 = new Track("Track1", 5, 120);
        BlockSignallingSystem sys1 = new BlockSignallingSystem("Block1");
        Track track12 = new Track("Track12", 1, 120);
        Track track13 = new Track("Track13", 3, 120);
        TrackCircuit circuit2 = new TrackCircuit("Circuit2", 120);
        Semaphore semaphore1 = new Semaphore("Sem1", 120);
        TrackCircuit circuit3 = new TrackCircuit("Circuit3", 100);
        Track track2 = new Track("Track2", 5, 100);
        Track track21 = new Track("Track21", 3, 80);
        BlockSignallingSystem sys2 = new BlockSignallingSystem("Block2");
        Track track22 = new Track("Track22", 2, 120);
        Track track23 = new Track("Track23", 8, 160);
        TrackCircuit circuit4 = new TrackCircuit("Circuit4", 160);
        Semaphore semaphore2 = new Semaphore("Sem2", 120);
        TrackCircuit circuit5 = new TrackCircuit("Circuit5", 80);
        Track track3 = new Track("Track3", 10, 80);
        TrackCircuit circuit6 = new TrackCircuit("Circuit6", 80);
        BlockSignallingSystem sys3 = new BlockSignallingSystem("Block3");

        sys1.setStartSemaphore(null);
        sys1.setNextSystem(sys2);
        sys1.setEndSemaphore(semaphore1);
        sys2.setPreviousSystem(sys1);
        sys2.setStartSemaphore(semaphore1);
        sys2.setNextSystem(sys3);
        sys2.setEndSemaphore(semaphore2);
        sys3.setStartSemaphore(semaphore2);
        sys3.setPreviousSystem(sys2);
        sys3.setEndSemaphore(null);

        circuit1.setBlockSignallingSystem(sys1);
        circuit2.setBlockSignallingSystem(sys1);
        circuit3.setBlockSignallingSystem(sys2);
        circuit4.setBlockSignallingSystem(sys2);
        circuit5.setBlockSignallingSystem(sys3);
        circuit6.setBlockSignallingSystem(sys3);

        sys1.setStartTrackCircuit(circuit1);
        sys1.setEndTrackCircuit(circuit2);
        sys2.setStartTrackCircuit(circuit2);
        sys2.setEndTrackCircuit(circuit3);
        sys3.setStartTrackCircuit(circuit3);
        sys3.setEndTrackCircuit(circuit4);

        Train train = Train.builder().id("Train1").maxSpeed(160).manufacturer("Siemens").model("DesiroML")
                .departureStation("Start Station").lineNumber("S10").capacity(300).arrivalStation("End Station")
                .currentTrack(platform1).currentStation(platform1.getStation())
                .lengthInKm(0.6)
                .build();

        train.modifySchedule(start.getName(), 0, 0);
        train.modifySchedule(end.getName(), 10, 10);

        train.setCurrentDirection(Direction.FORWARD);

        List<Track> routes = List.of(circuit1, track1, track12, track13, circuit2, semaphore1, circuit3, track2, track21, track22, track23, circuit4, semaphore2, circuit5, track3, circuit6, platform2);
        train.addStops(start, end);
        train.getRoute().addAll(routes);

      /*  Train train2 = Train.builder().id("Train2").maxSpeed(160).manufacturer("Siemens").model("Minero")
                .departureStation("Start Station").lineNumber("Z10").capacity(300).arrivalStation("End Station")
                .build();
        train2.addStops(start, end);
        train2.getRoute().addAll(routes);

        train2.modifySchedule(start.getName(), 4, 4);
        train2.modifySchedule(end.getName(), 14, 14);

        train2.setCurrentDirection(Direction.FORWARD);*/

        EventQueue eventQueue = new EventQueue();
        eventQueue.insert(new TrainArrivalEvent(0, train, platform1));
        //eventQueue.insert(new TrainArrivalEvent(4, train2, platform1));
        //eventQueue.insert(new TrainDepartureEvent(0, train, szob.getPlatforms()[0]));
        Simulator simulator = new Simulator(eventQueue);
        simulator.processAllEvents();
    }
}
