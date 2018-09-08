package data;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.View;

/**
 * Created by olive on 8/09/2018.
 */
public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Pane pane = (Pane) FXMLLoader.load(View.class.getResource("gui.fxml"));
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
