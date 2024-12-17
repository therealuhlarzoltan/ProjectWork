package hu.uni.obuda.des.railways.util;

import hu.uni.obuda.des.railways.tracks.Track;
import hu.uni.obuda.des.railways.trains.Train;

public class SpeedProfileCalculatorImpl implements SpeedProfileCalculator {
    @Override
    public double calculateTravelTimeSpeeding(double distance, double startingSpeed, double startingAcceleration, Train train, Track track) {
       throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public double calculateTravelTimeBraking(double distance, double startingSpeed, double startingAcceleration, double targetSpeed, Train train, Track track) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


}
