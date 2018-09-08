package data;

/**
 * Created by olive on 8/09/2018.
 */
public class Main {

    public static void main(String[] args) {
        String fileName = "data.csv";

        Input.initialise(fileName);
        double error = 0;

        System.out.println("start");

        for (int i = 0; i < 100; i++) {
            int index = (int) (Math.random() * 3000) + 22;
            double predicted = LearnHandler.predictHumidity(index);
            if (predicted == 0) {
                System.out.println(index);
            }
            double actual = LearnHandler.getHumidity(index);
            System.out.println("predicted is " + predicted +
                    " actual was " + actual);
            error += Math.abs((predicted - actual) / actual);
            if ((predicted - actual) / actual > 0.01) {
                System.out.println("the bad prediction was " + predicted + " actual was " + actual + " index was " + index);
            }
        }
        System.out.println(error / 100);

        error = 0;
        LearnHandler.crossValidate(100);
        for (int i = 0; i < 100; i++) {
            int index = (int) (Math.random() * 3000) + 22;
            double predicted = LearnHandler.predictHumidity(index);
            if (predicted == 0) {
                System.out.println(index);
            }
            double actual = LearnHandler.getHumidity(index);
            System.out.println("predicted is " + predicted +
                    " actual was " + actual);
            error += Math.abs((predicted - actual) / actual);
            if ((predicted - actual) / actual > 0.01) {
                System.out.println("the bad prediction was " + predicted + " actual was " + actual + " index was " + index);
            }
        }
        System.out.println(error / 100);
    }
}
