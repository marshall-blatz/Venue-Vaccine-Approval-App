package Start;
import Venue_Login.*;
import User_Login.*;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Class controls the operations of the initial start screen GUI
 */
public class Start_Controller {

    @FXML
    private Button orgButton;

    @FXML
    private Button userButton;

    /**
     * GUI display, displays scene attached to it
     */
    private Stage stage;
    /**
     * GUI layout, assigned by root attached to it
     */
    private Scene scene;
    /**
     * Scene graph, contains JavaFX elements to be displayed
     */
    private Parent root;

    /**
     * Method called to set up the next GUI stage
     * @param event GUI button action used to call method
     * @throws IOException thrown if fxml file can't be loaded
     */
    @FXML
    void orgButtonClicked(ActionEvent  event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Venue_Login/Venue_Login.fxml"));
        root = loader.load();

        //root = FXMLLoader.load(getClass().getResource("Venue_Login/Venue_Login.fxml"));

        //root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("HangmanGUI.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Venue Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method called to set up the next GUI stage
     * @param event GUI button action used to call method
     * @throws IOException thrown if fxml file can't be loaded
     */
    @FXML
    void userButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("User_Login/User_Login.fxml"));
        root = loader.load();


        //root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("HangmanGUI.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("User Login");
        stage.setScene(scene);
        stage.show();
    }

}


