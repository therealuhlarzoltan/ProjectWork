package hu.uni.obuda.des.railways.util;

import hu.uni.obuda.des.railways.stations.Station;
import hu.uni.obuda.des.railways.trains.Train;

public interface BoardingTimeCalculator {
    double calculateBoardingTime(Station.Platform platform, Train train);
    double calculateUnBoardingTime(Station.Platform platform, Train train);
}
