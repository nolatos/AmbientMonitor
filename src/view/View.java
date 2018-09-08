package view;

import data.Input;
import data.LearnHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.naming.Context;
import java.io.FileNotFoundException;

public class View {

    private int maxValues = 15;


    @FXML
    private TextField textField;

    @FXML
    private Button goButton;

    @FXML
    private Button validateButton;

    @FXML
    private TextArea outputBox;

    @FXML
    private ChoiceBox<Integer> choiceBox;

    @FXML
    private Button predictButton;

    @FXML
    private Label validatingLabel;


    @FXML
    void go(ActionEvent event) {
        if (textField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid file name", ButtonType.OK);
            alert.showAndWait();
        }
        else {

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        Input.initialise(textField.getText());
                        maxValues = LearnHandler.getSize();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                outputBox.setText("Done!");
                            }
                        });
                    }
                    catch (FileNotFoundException fnfex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid file name", ButtonType.OK);
                        alert.showAndWait();
                    }
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.start();
            predictButton.setDisable(false);
        }
    }



    @FXML
    void validate(ActionEvent event) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                LearnHandler.crossValidate(choiceBox.getValue());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        validatingLabel.setText("");
                        outputBox.setText("Finished validation!");
                    }
                });
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        validatingLabel.setText("validating...");
    }

    @FXML
    public void initialize() {
        choiceBox.setItems(FXCollections.observableArrayList(10, 100, 1000));

    }

    @FXML
    void predict(ActionEvent event) {
        double humidity = LearnHandler.predictHumidity(maxValues);
        double temperature = LearnHandler.predictTemperature(maxValues);
        outputBox.setText("Predicted values are: \n temp = " + temperature + "  humidity = " + humidity);
    }


}


