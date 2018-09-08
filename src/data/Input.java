package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by olive on 8/09/2018.
 */
public class Input {

    public static void initialise(String fileName) throws FileNotFoundException{
        boolean initial = true;
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        scanner.nextLine();

        //Scanning the file
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            LearnHandler.addMuseum(new Museum(Integer.parseInt(line[0]), Double.parseDouble(line[1]),
                    Double.parseDouble(line[2]), Boolean.parseBoolean(line[3]),
                    Boolean.parseBoolean(line[4]), Boolean.parseBoolean(line[5]))); //Adding in all the information
            if (initial) {
                LearnHandler.initialise(); //Initialises the original predictions
                initial = false;
            }
        }
    }
}
