package Venue_Login;
import Venue_Interface.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Class that contains objects and methods that control the venue login GUI
 */
public class Venue_Login_Controller {

    /**
     * Login model object, used to call model methods that hold the database access and logic
     */
    final Venue_Login_Model model = new Venue_Login_Model();
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

    @FXML
    private TextField usernameSignup;

    @FXML
    private TextField passwordSignup;

    @FXML
    private TextField passwordVerify;

    @FXML
    private TextField orgName;

    @FXML
    private TextField email;

    @FXML
    private TextField usernameLogin;

    @FXML
    private PasswordField passwordLogin;

    @FXML
    private Label errorLabel;

    @FXML
    private Button backButton;

    /**
     * Method to go back to the last GUI stage
     * @param event GUI action to call method
     * @throws IOException  thrown if fxml can't be loaded
     */
    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Start/Start.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Covid-19 Registration");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Used to login to the app as a venue, checks if the venue info is stored
     * in the database and if so, allows a successful login
     * @param event GUI action used to call method
     * @throws IOException thrown if file can't be loaded
     */
    @FXML
    void venueLogin(ActionEvent event) throws IOException {
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();
        if (model.login(username, password)) {
            venueInitialization(event, username);
        } else {
            errorLabel.setText("Invalid login. Please check parameters");
        }
    }

    /**
     * Used for venue registration, sends venue info to be stored in database if they aren't already registered
     * Calls method to initialize the next GUI scene
     * @param event action to the method
     * @throws IOException  thrown if file can't be loaded
     */
    @FXML
    void venueSignup(ActionEvent event) throws IOException {
        String username = usernameSignup.getText();
        String password1 = passwordSignup.getText();
        String password2 = passwordVerify.getText();
        String venName = orgName.getText();
        String emailText = email.getText();
        if(model.checkSignUp(username, password1, password2, venName, emailText)){
            model.addVenue(username, password1, venName, emailText);
            venueInitialization(event, username);
        } else {
            errorLabel.setText("Invalid input. Please check parameters");
        }
    }

    /**
     * Method called to set up the next GUI stage
     * @param event GUI action used to call method
     * @param username  unique username to be carried from one stage to the next
     * @throws IOException  thrown if fxml file can't be loaded
     */
    @FXML
    void venueInitialization(ActionEvent event, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Venue_Interface/Venue_Interface.fxml"));
        root = loader.load();

        Venue_Interface_Controller venueInterfaceController = loader.getController();
        venueInterfaceController.setup(username);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Venue Interface");
        stage.setScene(scene);
        stage.show();
    }
}
