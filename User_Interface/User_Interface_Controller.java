package User_Interface;
import Start.*;
import User_Login.*;

import User_Login.User_Login_Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Extra.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;

/**
 * Contains objects and methods that control the operations of the user interface GUI
 */
public class User_Interface_Controller {

    /**
     * URL to access the database
     */
    static final String DATABASE_URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/swd_db001";

    /**
     * Admin username to access the database
     */
    static final String USERNAME = "swd_group001";

    /**
     * Password to login to the database
     */
    static final String PASSWORD = "justinsucks1!";

    /**
     * Local variable to store the curretnly logged in users username
     * Allows easy access to correct database info
     */
    String userString;

    /**
     * Local user interface object, used to call the methods that hold database access and logic
     */
    User_Interface_Model model = new User_Interface_Model();

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
    private ImageView imageView;

    @FXML
    private Label usernameInfo;

    @FXML
    private Label nameLabel;

    @FXML
    private Label DOBLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Button passwordButton;

    @FXML
    private TextField passwordText;

    @FXML
    private Button uploadImageButton;

    @FXML
    private Button requestButton;

    @FXML
    private TextArea venueText;

    @FXML
    private Label username;

    @FXML
    private TableView<Names> requestTable;

    @FXML
    private TableColumn<Names, String> column1;

    @FXML
    private TableView<Names> approvedTable;

    @FXML
    private TableColumn<Names, String> column2;

    @FXML
    private Button logout;

    @FXML
    private Button refreshButton;

    @FXML
    private TextArea approvedText;

    /**
     * Used to update the individuals stored password value, pulls the new value from
     * the GUI then sends the value to the database using the local models method
     * @param event action to call the method
     */
    @FXML
    void changePassword(ActionEvent event) {
        model.changePswrd(userString, passwordText.getText());
    }

    /**
     * Used to actively refresh data displayed on the interface after the user has logged in
     * Establishes a connection to the database, pulls the info needed and
     * resets the interface display to the updated info
     * @param event action to call method
     */
    @FXML
    void refreshAll(ActionEvent event) {
        requestTable.getItems().clear();
        final String USERNAMEQUERY = "SELECT * FROM Users WHERE Usernames='" + userString + "';";
        final String VENUEQUERY = "SELECT * FROM Venues";
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet userSet = statement.executeQuery(USERNAMEQUERY);

            userSet.next();
            nameLabel.setText("Name: " + userSet.getString("UserName"));
            emailLabel.setText("Email: " + userSet.getString("Email"));
            DOBLabel.setText("Birth Date: " + userSet.getDate("DOB"));

            // Set the image if available
            if(!userSet.getBinaryStream("Image").equals("")){
                Image image = new Image(userSet.getBinaryStream("Image"));
                imageView.setImage(image);
            }

            statement = connection.createStatement();
            ResultSet venueSet = statement.executeQuery(VENUEQUERY);
            String[] approved = userSet.getString("Approved").split(",");
            // Check to see if the username is unique to the query
            while(venueSet.next()){
                if(!Arrays.asList(approved).contains(venueSet.getString("Usernames"))) {
                    Names current = new Names(venueSet.getString("OrgName"), venueSet.getString("Usernames"));
                    requestTable.getItems().add(current);
                }
            }

            approvedTable.getItems().clear();
            if(!approved[0].equals("")) {
                for (int i = 0; i < approved.length; i++) {
                    final String APPROVEDQUERY = "SELECT * FROM Venues WHERE Usernames='" + approved[i] + "'";
                    statement = connection.createStatement();
                    ResultSet approvedVen = statement.executeQuery(APPROVEDQUERY);
                    approvedVen.next();
                    Names ven = new Names(approvedVen.getString("OrgName"), approvedVen.getString("Usernames"));
                    approvedTable.getItems().add(ven);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
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
     * Used when an individual wants to request access to enter a venue.
     * User selects a venue from the list, their request is sent to the venue by
     * adding them to a list of people that need either approved or denied.
     * The venue can then check their info and either or deny them.
     * @param event action to call the method
     */
    @FXML
    void requestAccess(ActionEvent event) {
        if(requestTable.getSelectionModel().getSelectedItem() != null){
            Names venue = requestTable.getSelectionModel().getSelectedItem();
            String venue_name = venue.getName();
            try{
                Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);

                String get_list_command = "select Requested from Venues where OrgName = '" + venue_name + "'";
                ResultSet rs1 = connection.createStatement().executeQuery(get_list_command);
                rs1.next();
                String new_requests;
                String requests = rs1.getString("Requested");
                if(requests.equals("")){
                    new_requests = userString;
                    PreparedStatement psmt = connection.prepareStatement("update Venues set Requested = ? where OrgName = ?");
                    psmt.setString(1,new_requests);
                    psmt.setString(2,venue_name);
                    psmt.executeUpdate();
                }
                else {
                    boolean flag = true;
                    boolean has_comma = false;
                    String[] check_for_comma = requests.split("");
                    for(String value : check_for_comma){
                        if(value.equals(",")){
                            has_comma = true;
                        }
                    }
                    if(has_comma){
                        String[] all_requests = requests.split(",");
                        for(String user : all_requests){
                            if(userString.equals(user)){
                                flag = false;
                            }
                        }
                    } else {
                        if(userString.equals(requests)){
                            flag = false;
                        }
                    }
                    if(flag){
                        new_requests = requests + "," + userString;
                        PreparedStatement psmt = connection.prepareStatement("update Venues set Requested = ? where OrgName = ?");
                        psmt.setString(1,new_requests);
                        psmt.setString(2,venue_name);
                        psmt.executeUpdate();
                    }
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to upload an image to the user account
     * Displays the chosen file and then sends it to be stored in the database
     * @param event action to call the method
     */
    @FXML
    void uploadImage(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            Image image = new Image(fileInputStream);
            imageView.setImage(image);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        if (file != null) {
            model.uploadFile(userString, file);
        }
    }
    /**
     * Retrieves the venues the that the user has been approved access to
     * @param event action to call the method
     */
    @FXML
    void getVenueData(MouseEvent event) {
        if(requestTable.getSelectionModel().getSelectedItem() != null){
            venueText.clear();
            Names current = requestTable.getSelectionModel().getSelectedItem();
            String VENUEQUERY = "select * from Venues where Usernames = '" + current.getUserName() + "'";
            try {
                Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet venueSet = statement.executeQuery(VENUEQUERY);

                venueSet.next();
                venueText.appendText("\nOrganization Name: " + venueSet.getString("OrgName"));
                venueText.appendText("\n\nLocation: " + venueSet.getString("Location"));
                venueText.appendText("\n\nEmail: " + venueSet.getString("Email"));
                if(venueSet.getBoolean("MaskReq")) {
                    venueText.appendText("\n\nMask Requirement: Yes");
                } else {
                    venueText.appendText("\n\nMask Requirement: No");
                }
                if(venueSet.getBoolean("VacReq")){
                    venueText.appendText("\n\nVaccine Requirement: Yes");
                } else {
                    venueText.appendText("\n\nVaccine Requirement: No");
                }
                if(venueSet.getBoolean("TestReq")){
                    venueText.appendText("\n\nRecent Covid Test: Yes");
                } else {
                    venueText.appendText("\n\nRecent Covid Test: No");
                }
                venueText.appendText("\n\nAdditional Comments: " + venueSet.getString("AdditionalComments"));

            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the venues the that the user has been approved access to
     * @param event action to call the method
     */
    @FXML
    void getApprovedData(MouseEvent event) {
        if(approvedTable.getSelectionModel().getSelectedItem() != null){
            approvedText.clear();
            Names current = approvedTable.getSelectionModel().getSelectedItem();
            String VENUEQUERY = "select * from Venues where Usernames = '" + current.getUserName() + "'";
            try {
                Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet venueSet = statement.executeQuery(VENUEQUERY);

                venueSet.next();
                approvedText.appendText("\nOrganization Name: " + venueSet.getString("OrgName"));
                approvedText.appendText("\n\nLocation: " + venueSet.getString("Location"));
                approvedText.appendText("\n\nEmail: " + venueSet.getString("Email"));
                if(venueSet.getBoolean("MaskReq")) {
                    approvedText.appendText("\n\nMask Requirement: Yes");
                } else {
                    approvedText.appendText("\n\nMask Requirement: No");
                }
                if(venueSet.getBoolean("VacReq")){
                    approvedText.appendText("\n\nVaccine Requirement: Yes");
                } else {
                    approvedText.appendText("\n\nVaccine Requirement: No");
                }
                if(venueSet.getBoolean("TestReq")){
                    approvedText.appendText("\n\nRecent Covid Test: Yes");
                } else {
                    approvedText.appendText("\n\nRecent Covid Test: No");
                }
                approvedText.appendText("\n\nAdditional Comments: " + venueSet.getString("AdditionalComments"));

            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to initialize the user interface, fires the refresh button to pull data from the database
     * @param word  username transferred from login
     */
    public void setup(String word){
        username.setText(username.getText()+word);
        usernameInfo.setText(usernameInfo.getText()+word);
        column1.setCellValueFactory(new PropertyValueFactory("name"));
        column2.setCellValueFactory(new PropertyValueFactory("name"));
        userString = word;

        refreshButton.fire();

    }




}
