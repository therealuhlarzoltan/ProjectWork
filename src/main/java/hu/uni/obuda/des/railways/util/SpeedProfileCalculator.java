package hu.uni.obuda.des.railways.util;

import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

public interface SpeedProfileCalculator {
    double calculateTravelTimeSpeeding(double distance, double startingSpeed, double startingAcceleration, Train train, Track track);
    double calculateTravelTimeBraking(double distance, double startingSpeed, double startingAcceleration, Train train, Track track);
}
