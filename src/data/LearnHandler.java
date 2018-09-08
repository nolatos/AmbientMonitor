package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by olive on 8/09/2018.
 */
public class LearnHandler {

    public final static double DEFAULT_ALPHA = 0.9;

    private static List<Museum> museumList = new ArrayList<>();


    private static double alphaTemperature; //The value of alpha for tmperature. At a quick glance, the temperature is not affected by the state of the other variables
    private static double alphaHumidityClosed; //The value of humidity for closed door. At a quick glance, neither weather conditions seem to have any effect
    private static double alphaHumidityOpen; //The alpha value for humidity for an open door

    private static int humidityPreviousIterations = 5;
    private static int temperaturePreviousIterations = 5;


    /**
     * Initialises the original temperature and humidity
     */
    public static void initialise() {

        double humidity = museumList.get(0).getHumidity();
        double temperature = museumList.get(0).getTemperature();

        //Obtaining the alpha and temperatures from the predictions.xls file
        //If they exist
        alphaHumidityOpen = DEFAULT_ALPHA;
        alphaHumidityClosed = DEFAULT_ALPHA;
        alphaTemperature = DEFAULT_ALPHA;
    }





    /**
     * Returns the humidity at the specified index i
     * @param i
     * @return
     */
    public static double getHumidity(int i) {
        return museumList.get(i).getHumidity();
    }

    public static double getTemperature(int i) {
        return museumList.get(i).getTemperature();
    }

    public static void addMuseum(Museum museum) {
        museumList.add(museum);
    }

    public static void crossValidate(int iterations) {

        List<Integer> temperatureIterations = new ArrayList<>();
        List<Integer> humidityIterations = new ArrayList<>();

        List<Double> alphasTemperature = new ArrayList<>();
        List<Double> alphasClosedHumidity = new ArrayList<>();
        List<Double> alphasOpenHumidity = new ArrayList<>();

        int count = 0;

        for (int i = 0; i < iterations; i++) {
            int index = (int) ((museumList.size() - 20) * Math.random()) + 20;
            Museum museum = museumList.get(index);
            double actualHumidity = museum.getHumidity();
            double actualTemperature = museum.getTemperature();

            double leastTemperatureError = -1;
            double leastHumidityError = -1;

            int bestPreviousTemperature = 0;
            int bestPreviousHumidity = 0;
            double bestAlphaTemperature = 0;
            double bestAlphaHumidityClosed = 0;
            double bestAlphaHumidityOpen = 0;

            //Calculating the previous
            for (int previous = 1; previous < 21; previous += 2) {
                double humidity = predictHumidity(index, previous, DEFAULT_ALPHA, DEFAULT_ALPHA);
                double temperature = predictTemperature(index, previous, DEFAULT_ALPHA);

                double errorHumidity = Math.abs(humidity - actualHumidity);
                double errorTemperature = Math.abs(actualTemperature - temperature);

                //Initial iteration
                if (leastHumidityError == -1) {
                    leastHumidityError = errorHumidity;
                    leastTemperatureError = errorTemperature;

                    bestPreviousHumidity = previous;
                    bestPreviousTemperature = previous;
                }
                else {
                    if (leastHumidityError > errorHumidity) { //Else we only update once we have a smaller error
                        leastHumidityError = errorHumidity;
                        bestPreviousHumidity = previous;
                    }
                    if (leastTemperatureError > errorTemperature) {
                        leastTemperatureError = errorTemperature;
                        bestPreviousTemperature = previous;

                    }
                }

            }

            leastTemperatureError = -1;
            double leastHumidityClosedError = -1;
            double leastHumidityOpenError = -1;

            //Iterating through alphas to find best one
            for (double alpha = 0.2; alpha <= 1; alpha += 0.2) {
                double humidityClosed = predictHumidity(index, bestPreviousHumidity, alpha, DEFAULT_ALPHA);
                double humidityOpen = predictHumidity(index, bestPreviousHumidity, DEFAULT_ALPHA, alpha);
                double temperature = predictTemperature(index, bestPreviousTemperature, alpha);

                double errorHumidityClosed = Math.abs(humidityClosed - actualHumidity);
                double errorHumidityOpen = Math.abs(humidityOpen - actualHumidity);
                double errorTemperature = Math.abs(actualTemperature - temperature);

                //Initial iteration
                if (leastHumidityClosedError == -1) {
                    leastHumidityClosedError = errorHumidityClosed;
                    leastHumidityOpenError = errorHumidityOpen;
                    leastTemperatureError = errorTemperature;

                    bestAlphaTemperature = alpha;
                    bestAlphaHumidityClosed = alpha;
                    bestAlphaHumidityOpen = alpha;
                }
                else  {
                    if (leastHumidityClosedError > errorHumidityClosed) { //Else we only update once we have a smaller error
                        leastHumidityClosedError = errorHumidityClosed;
                        bestAlphaHumidityClosed = alpha;
                    }
                    if (leastTemperatureError > errorTemperature) {
                        leastTemperatureError = errorTemperature;
                        bestAlphaTemperature = alpha;
                    }
                    if (leastHumidityOpenError > errorHumidityOpen) {
                        leastHumidityOpenError = errorHumidityOpen;
                        bestAlphaHumidityOpen = alpha;
                    }
                }

            }

            alphasClosedHumidity.add(bestAlphaHumidityClosed);
            alphasOpenHumidity.add(bestAlphaHumidityOpen);
            alphasTemperature.add(bestAlphaTemperature);
            humidityIterations.add(bestPreviousHumidity);
            temperatureIterations.add(bestPreviousTemperature);
            count++;
        }


        Collections.sort(alphasClosedHumidity);
        Collections.sort(alphasOpenHumidity);
        Collections.sort(alphasTemperature);
        Collections.sort(temperatureIterations);
        Collections.sort(humidityIterations);
        double alphaClosedHumidity = alphasClosedHumidity.get(alphasClosedHumidity.size() / 2);
        double alphaOpenHumidity = alphasOpenHumidity.get(alphasOpenHumidity.size() / 2);
        double alphaTemperature = alphasTemperature.get(alphasTemperature.size() / 2);
        int previousHumidityIterations = temperatureIterations.get(temperatureIterations.size() / 2);///= iterations;
        int previousTemperatureIterations = humidityIterations.get(humidityIterations.size() / 2);//= iterations;

        alphaHumidityClosed = alphaClosedHumidity;
        alphaHumidityOpen = alphaOpenHumidity;
        LearnHandler.alphaTemperature = alphaTemperature;
        temperaturePreviousIterations = previousTemperatureIterations;
        humidityPreviousIterations = previousHumidityIterations;

        System.out.println("alpha is " + LearnHandler.alphaTemperature  + " previous is " + temperaturePreviousIterations);
    }

    public static double predictHumidity(int index, int humidityPreviousIterations, double alphaHumidityClosed, double alphaHumidityOpen) {
        double thisHumidity = museumList.get(index - 1).getHumidity();
        double previousHumidity = museumList.get(index - 2).getHumidity();
        double sign;

        //Seeing if its an increasing or decreasing trend
        if (thisHumidity > previousHumidity) {
            sign = 1;
        }
        else if (thisHumidity < previousHumidity){
            sign = -1;
        }
        else {
            return thisHumidity;
        }

        double difference = Math.abs(museumList.get(index - 3).getHumidity() - museumList.get(index - 2).getHumidity());

        for (int i = humidityPreviousIterations; i > 0; i--) {
            if (museumList.get(index - i).getHumidity() - museumList.get(index - i - 1).getHumidity() > 0 != sign > 0) {
                continue;
            }
            double alpha = DEFAULT_ALPHA;
            if (museumList.get(index - 1).isDoorOpened()) {
                alpha = alphaHumidityOpen;
            }
            else {
                alpha = alphaHumidityClosed;
            }
            double thisDifference = Math.abs(museumList.get(index - i).getHumidity() - museumList.get(index - i - 1).getHumidity());
            difference = (1 - alpha) * difference + alpha * thisDifference;
        }

        double result = thisHumidity + difference * sign;
        return result;
    }


    public static double predictTemperature(int index, int temperaturePreviousIterations, double alphaTemperature) {
//        System.out.println(temperaturePreviousIterations);
        double thisTemperature = museumList.get(index - 1).getTemperature();
        double previousTemperature = museumList.get(index - 2).getTemperature();
        double sign;

        if (thisTemperature > previousTemperature) {
            sign = 1;
        }
        else {
            sign = -1;
        }

        double difference = Math.abs(museumList.get(index - 3).getTemperature() - museumList.get(index - 2).getTemperature());

        for (int i = temperaturePreviousIterations; i > 0; i--) {
            if (museumList.get(index - i).getTemperature() - museumList.get(index - i - 1).getTemperature() > 0 != sign > 0) {
                continue;
            }
            double thisDifference = Math.abs(museumList.get(index - i).getTemperature() - museumList.get(index - i - 1).getTemperature());
            difference = (1 - alphaTemperature) * difference + alphaTemperature * thisDifference;
        }

        double result = thisTemperature + difference * sign;
        return result;

    }

    public static double predictTemperature(int index) {
        return predictTemperature(index, temperaturePreviousIterations, alphaTemperature);
    }

    public static double predictHumidity(int index) {
        return predictHumidity(index, temperaturePreviousIterations, alphaHumidityClosed, alphaHumidityOpen);
    }

}


