package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by olive on 8/09/2018.
 */
public class Model {

    private List<Museum> museumList = new ArrayList<>();

    public void addMuseum(Museum museum) {
        museumList.add(museum);
    }
}
