package User_Login;

import java.sql.*;
import java.time.LocalDate;

/**
 * Class that contains logic for the login controller and access needed to the SQL database
 */
public class User_Login_Model {

    /**
     * URL used to access database
     */
    static final String DATABASE_URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/swd_db001";
    /**
     * Admin username to allow access database
     */
    static final String USERNAME = "swd_group001";
    /**
     * Password used to login to admin account
     */
    static final String PASSWORD = "justinsucks1!";

    /**
     * Checks for valid user registration, checks against database records for username uniqueness
     * Then checks if all information is entered correctly
     * @param username  unique account username
     * @param password1 account password
     * @param password2 check to make sure password entered correctly
     * @param name  individuals name
     * @param email individuals email
     * @param DOB   individuals date of birth
     * @return  true if all info entered, and username is unique, false otherwise
     */
    public boolean checkSignUp(String username, String password1, String password2, String name, String email, LocalDate DOB) {
        boolean valid = true;
        final String USERQUERY = "SELECT * FROM Users";
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet userSet = statement.executeQuery(USERQUERY);
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
            // Check if passwords are equal, if there is an individuals name and if there is an email
            if(email.equals("") || name.equals("") || !password1.equals(password2) || DOB == null) {
                valid = false;
            }
        }
        return valid;
    }


    /**
     * Accesses and adds user info to the database
     * @param username  unique account username
     * @param password  account password
     * @param indName   individuals name
     * @param email individuals email
     * @param DOB   individuals date of birth
     */
    public void addUser(String username, String password, String indName, String email, LocalDate DOB) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("INSERT INTO `Users`(Usernames,Passwords,UserName,Email,DOB) VALUES (?, ?, ?, ?, ?)");
            psmt.setString(1, username);
            psmt.setString(2, password);
            psmt.setString(3, indName);
            psmt.setString(4, email);
            psmt.setDate(5, Date.valueOf(DOB));
            psmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accesses SQL database info, checks if username and password match
     * any stored info in user database, allows successful login info matching info is found
     * @param username username to be checked
     * @param password password to be checked
     * @return true if matching info is found in the database, false otherwise
     */
    public boolean login(String username, String password) {
        boolean valid = false;
        final String USERQUERY = "SELECT * FROM Users";
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet userSet = statement.executeQuery(USERQUERY);
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
}
