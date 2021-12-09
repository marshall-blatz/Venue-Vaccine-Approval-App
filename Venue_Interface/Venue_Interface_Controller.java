package Venue_Interface;
import Extra.Names;
import Start.*;

import Venue_Login.Venue_Login_Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;


public class Venue_Interface_Controller {

    /**
     * Local venue interface model object
     */
    final Venue_Interface_Model model = new Venue_Interface_Model();

    /**
     * Local variable to store the venue's username
     */
    String username;

    /**
     * URL to access the database
     */
    private static final String DATABASE_URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/swd_db001";

    /**
     * Admin username to access the database
     */
    private static final String USERNAME = "swd_group001";

    /**
     * Password to access the database
     */
    private static final String PASSWORD = "justinsucks1!";

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
     * Sets the value of the local username
     * @param newname the new username
     */
    private void setUsername(String newname){
        username = newname;
    }

    /**
     * Returns the username
     * @return the username value
     */
    private String getUsername(){
        return username;
    }

    @FXML
    private TextArea additional_comments;

    @FXML
    private CheckBox masks_required_button;

    @FXML
    private CheckBox vaccine_required_button;

    @FXML
    private CheckBox testing_required_button;

    @FXML
    private ImageView vaccine_card_image;

    @FXML
    private TextArea applicant_name;

    @FXML
    private TextArea applicant_dob;

    @FXML
    private TableView<Names> pending_requests;

    @FXML
    private TableColumn<Names, String> column;

    @FXML
    private TextField new_org_password;

    @FXML
    private TextField new_org_name;

    @FXML
    private Label user;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField locationText;

    @FXML
    private Button locationButton;

    /**
     * Used to actively refresh data displayed on the interface after the venue has logged in
     * Establishes a connection to the database, pulls the info needed and
     * resets the interface display to the updated info
     * @param event action to call method
     */
    @FXML
    void refresh(ActionEvent event) {
        pending_requests.getItems().clear();
        vaccine_card_image.setImage(null);

        String USERQUERY = "SELECT * FROM Users;";
        String VENUEQUERY = "SELECT * FROM Venues WHERE Usernames='" + getUsername() + "';";
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet venueSet = statement.executeQuery(VENUEQUERY);

            venueSet.next();
            if (venueSet.getBoolean(5) && !masks_required_button.isSelected()) {
                masks_required_button.fire();
            }
            if (venueSet.getBoolean(6) && !vaccine_required_button.isSelected()) {
                vaccine_required_button.fire();
            }
            if (venueSet.getBoolean(7) && !testing_required_button.isSelected()) {
                testing_required_button.fire();
            }
            if (venueSet.getString(9)!=""){
                additional_comments.setText(venueSet.getString(9));
            }


            String REQUESTQUERY = "SELECT Requested FROM Venues WHERE Usernames = '" + getUsername() + "';";
            ResultSet requestSet = statement.executeQuery(REQUESTQUERY);
            requestSet.next();
            String[] requested = requestSet.getString("Requested").split(",");
            if(!requested[0].equals("")) {
                for (int i = 0; i < requested.length; i++) {
                    Names name = new Names(requested[i], requested[i]);
                    pending_requests.getItems().add(name);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used when an organization decides to approve an individual that has requested access to their venue
     * @param event action to call the method
     */
    @FXML
    void approveRequest(ActionEvent event) {
        String pending_user ="";
        if(pending_requests.getSelectionModel().getSelectedItem() != null) {
            Names user = pending_requests.getSelectionModel().getSelectedItem();
            pending_user = user.getName();
        }
        model.changeApprovalStatus(getUsername(),pending_user, true);
        refreshButton.fire();
    }

    /**
     * Used when an organization decides to deny an individual that has requested access to their venue
     * @param event action to call the method
     */
    @FXML
    void denyRequest(ActionEvent event) {
        String pending_user ="";
        if(pending_requests.getSelectionModel().getSelectedItem() != null) {
            Names user = pending_requests.getSelectionModel().getSelectedItem();
            pending_user = user.getName();
        }
        model.changeApprovalStatus(getUsername(),pending_user,false);
        refreshButton.fire();
    }

    /**
     * Used to update the stored value of an organizations name
     * Calls a model method to access the database and change the stored value
     * @param event action used to call the method
     */
    @FXML
    void changeOrgName(ActionEvent event) {
        String new_name = new_org_name.getText();
        model.changeName(getUsername(),new_name);
    }

    /**
     * Used to update the stored value of an organization's location
     * Calls a model method to access the database and change the stored value
     * @param event Action used to call the method
     */
    @FXML
    void changeLocation(ActionEvent event){
        String location = locationText.getText();
        model.location(location, username);
    }

    /**
     * Used to update the stored value of an organization's password
     * Calls a model method to access the database and change the stored value
     * @param event action used to call the method
     */
    @FXML
    void changePassword(ActionEvent event) {
        String new_password = new_org_password.getText();
        model.changePassword(getUsername(),new_password);
    }

    /**
     * Used to logout of the app, switches the GUI back to the initial start up screen
     * @param event action to call the method
     * @throws IOException thrown if the fxml file can't be loaded
     */
    @FXML
    void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Start/Start.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Covid-19 Registration");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Used to update the stored comments of an organization
     * Calls a model method to access the database and store the new info
     * @param event action used to call the method
     */
    @FXML
    void updateComments(ActionEvent event) {
        String comments = additional_comments.getText();
        model.changeAdditionalComments(getUsername(),comments);
    }

    /**
     * Used to update the stored COVID guidelines of an organization
     * Calls a model method to access the database and store the updated guidelines
     * @param event action used to call the method
     */
    @FXML
    void updateGuidelines(ActionEvent event) {
        boolean mask_check = masks_required_button.isSelected();
        model.changeMaskReq(getUsername(),mask_check);

        boolean vax_check = vaccine_required_button.isSelected();
        model.changeVaxReq(getUsername(),vax_check);

        boolean test_check = testing_required_button.isSelected();
        model.changeTestReq(getUsername(),test_check);
    }

    /**
     * Method to set up the venue interface
     * @param word  username transferred from login/signup
     */
    public void setup(String word){
        setUsername(word);
        user.setText(user.getText()+word);
        column.setCellValueFactory(new PropertyValueFactory<>("name"));
        refreshButton.fire();
    }

    /**
     * Used to display the stored info of a selected user to the screen.
     * Called when a venue selects a user requesting access to their
     * venue to decide whether to allow access or not
     * @param event action to call the method
     */
    @FXML
    void getUserData(MouseEvent event) {
        if(pending_requests.getSelectionModel().getSelectedItem() != null){
            applicant_name.clear();
            applicant_dob.clear();
            Names current = pending_requests.getSelectionModel().getSelectedItem();
            String USERQUERY = "select * from Users where Usernames = '" + current.getUserName() + "'";
            try {
                Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet userSet = statement.executeQuery(USERQUERY);

                userSet.next();
                applicant_name.setText(userSet.getString("UserName"));
                applicant_dob.setText(String.valueOf(userSet.getDate("DOB")));

                // Set the image if available
                if(!userSet.getBinaryStream("Image").equals("")){
                    Image image = new Image(userSet.getBinaryStream("Image"));
                    vaccine_card_image.setImage(image);
                }


            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
