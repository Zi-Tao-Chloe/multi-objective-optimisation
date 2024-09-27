public class ChargeConsumption {
    private static final double RED_CONSUMPTION = 130.77;
    private static final double GREEN_CONSUMPTION = 141.89;
    private static final double BLUE_CONSUMPTION = 241.24;
    private static final int NUM_PIXEL = 23686400;

    public static double calculateChargeConsumptionPerPixel(int red, int green, int blue) {
        // Normalize the pixel values to [0, 1]
        double normalizedRed = red / 255.0;
        double normalizedGreen = green / 255.0;
        double normalizedBlue = blue / 255.0;

        // Calculate the charge consumption
        double chargeConsumption = Math.sqrt(Math.pow(normalizedRed * RED_CONSUMPTION, 2) +
                Math.pow(normalizedGreen * GREEN_CONSUMPTION, 2) +
                Math.pow(normalizedBlue * BLUE_CONSUMPTION,2));
        return chargeConsumption/NUM_PIXEL;
    }
}
