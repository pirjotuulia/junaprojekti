package vr.data;

public class DistanceCalculator {

    public double calculateDistance(double x1, double y1, double x2, double y2) {
        // Ensimmäisenä latitude, seuraavana longitude
        x1 = Math.toRadians(x1);
        y1 = Math.toRadians(y1);
        x2 = Math.toRadians(x2);
        y2 = Math.toRadians(y2);

        /**
         * ***********************************************************************
         * * Compute using law of cosines. Use first latidude, then second longitude
         * Test: Helsinki - Turku = 150km: {60.16952, 24.93545, 60.45148, 22.26869};
        * ***********************************************************************
        */

        // great circle distance in radians
        double angle1 = Math.acos(Math.sin(x1) * Math.sin(x2)
                + Math.cos(x1) * Math.cos(x2) * Math.cos(y1 - y2));

        // convert back to degrees
        angle1 = Math.toDegrees(angle1);

        // each degree on a great circle of Earth is 60 nautical miles
        double distance1 = 60 * angle1 * 1.852001;

        return distance1;
    }

}

