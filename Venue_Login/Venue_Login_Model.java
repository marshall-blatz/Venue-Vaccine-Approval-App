package Venue_Login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * Class that contains logic for the login controller and access needeed to the SQL database
 */
public class Venue_Login_Model {

    /**
     * URL used to access database
     */
    static final String DATABASE_URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/swd_db001";
    /**
     * Admin username to allow access to database
     */
    static final String USERNAME = "swd_group001";
    /**
     * Password to login to admin account
     */
    static final String PASSWORD = "justinsucks1!";

    /**
     * Checks for valid organization registration, checks against database records for username uniqueness
     * Then checks if all information is entered correctly
     * @param username unique organization username to sign up with
     * @param password1 what the account password will be set to
     * @param password2 check to make sure password is entered correctly
     * @param orgName   actual organization name
     * @param email organization's email
     * @return true if all information needed is entered and username is unique, false otherwise
     */
    public boolean checkSignUp(String username, String password1, String password2, String orgName, String email) {
        boolean valid = true;
        final String VENUEQUERY = "SELECT * FROM Venues";
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet userSet = statement.executeQuery(VENUEQUERY);
            // Check to see if the username is unique to the query
            while(userSet.next()){
                if(userSet.getString("Usernames").equals(username)){
                    valid = false;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        // Check the rest of the inputs and put them in the server
        if(valid){
            // Check if passwords are equal, if there is an org name and if there is an email
            if(email.equals("") || orgName.equals("") || !password1.equals(password2)) {
                valid = false;
            }
        }
        return valid;
    }


    /**
     * Method that accesses and adds an organization's info to the shared database
     * @param username organizations unique username
     * @param password  organizations account password
     * @param orgName   name of the organization
     * @param email organization's account email
     */
    public void addVenue(String username, String password, String orgName, String email) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("INSERT INTO `Venues`(Usernames,Passwords,OrgName,Email) VALUES (?, ?, ?, ?)");
            psmt.setString(1, username);
            psmt.setString(2, password);
            psmt.setString(3, orgName);
            psmt.setString(4, email);
            psmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accesses SQL database info, checks if username and password match
     * any stored info in venue database, allows successful login info matching info is found
     * @param username username to be checked
     * @param password password to be checked
     * @return true if matching info is found in the database, false otherwise
     */
    public boolean login(String username, String password) {
        boolean valid = false;
        final String VENUEQUERY = "SELECT * FROM Venues";
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet userSet = statement.executeQuery(VENUEQUERY);
            // Check to see if the username is unique to the query
            while(userSet.next()){
                if(userSet.getString("Usernames").equals(username) && userSet.getString("Passwords").equals(password)){
                    valid = true;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return valid;
    }


    /**
     * method to securely encrypt any password
     * @param password the password to be encrypted
     * @return  the now encrypted password
     */
    // Encrypt the passwords
    public String encryptPassword(String password) {
        String encrypted = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest();
            messageDigest.update(password.getBytes());
        } catch(NoSuchAlgorithmException noSuchAlgorithmException){
            noSuchAlgorithmException.printStackTrace();
        }
        return encrypted;
    }
}
