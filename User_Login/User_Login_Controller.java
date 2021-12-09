package User_Login;
import User_Interface.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Class that contains objects and methods that control the user login GUI
 */
public class User_Login_Controller {


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
     * Login model object, used to call model methods that hold the database logic
     */
    final User_Login_Model model = new User_Login_Model();

    @FXML
    private GridPane leftSide;

    @FXML
    private TextField usernameEnter;

    @FXML
    private TextField passEnter;

    @FXML
    private GridPane rightSide;

    @FXML
    private TextField usernameSignUp;

    @FXML
    private TextField emailEnter;

    @FXML
    private TextField passConfirm;

    @FXML
    private TextField passSignUp;

    @FXML
    private DatePicker birthdayEnter;

    @FXML
    private TextField name;

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
     * Method called to set up the next user GUI stage
     * @param event GUI button action used to call method
     * @throws IOException thrown if fxml file can't be loaded
     */
    @FXML
    void userInitialization(ActionEvent event, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("User_Interface/User_Interface.fxml"));
        root = loader.load();

        User_Interface_Controller userInterfaceController = loader.getController();
        userInterfaceController.setup(username);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("User Interface");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Used to login to the app as a user, checks if the user's info is stored
     * in the database and if so, allows a successful login
     * @param event GUI action used to call method
     * @throws IOException thrown if file can't be loaded
     */
    @FXML
    void userLogin(ActionEvent event) throws IOException {
        String username = usernameEnter.getText();
        String password = passEnter.getText();
        if (model.login(username, password)) {
            userInitialization(event, username);
        } else {
            errorLabel.setText("Invalid login. Please check parameters");
        }
    }

    /**
     * Used for individual user registration, sends user info to be stored in database if
     * they aren't already registered. Calls method to initialize the next GUI scene
     * @param event action to the method
     * @throws IOException  thrown if file can't be loaded
     */
    @FXML
    void userSignup(ActionEvent event) throws IOException {
        String username = usernameSignUp.getText();
        String password1 = passSignUp.getText();
        String password2 = passConfirm.getText();
        String venName = name.getText();
        String emailText = emailEnter.getText();
        LocalDate birthdate = birthdayEnter.getValue();
        if(model.checkSignUp(username, password1, password2, venName, emailText, birthdayEnter.getValue())){
            model.addUser(username, password1, venName, emailText, birthdate);
            userInitialization(event, username);
        } else {
            errorLabel.setText("Invalid input. Please check parameters");
        }
    }


}
