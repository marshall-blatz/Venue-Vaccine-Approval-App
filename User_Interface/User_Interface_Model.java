package User_Interface;

import java.io.*;
import javafx.scene.image.Image;
import java.sql.*;

/**
 * Contains logic and database connections for the user interface controller
 */
public class User_Interface_Model {

    /**
     * URL used to access the database
     */
    static final String DATABASE_URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/swd_db001";
    /**
     * Admin username used to access the database
     */
    static final String USERNAME = "swd_group001";
    /**
     * Password used to login to the database
     */
    static final String PASSWORD = "justinsucks1!";

    /**
     * Used to update the individuals password in the database to the desired password
     * Establishes a connection to the database then sets the password value to the new string value
     * @param username individuals username
     * @param password  new password value
     */
    public void changePswrd(String username, String password){
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Users set Passwords = ? where Usernames = ?");
            psmt.setString(1, password);
            psmt.setString(2, username);
            psmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Used to upload an image file to a users account
     * Takes in a new image file, establishes a connection to the server
     * database then uploads the image as a Blob to the correct location
     * @param username individuals username
     * @param file  image file input
     */
    public void uploadFile(String username, File file){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Users set Image = ? where Usernames = ?");
            psmt.setBlob(1, fileInputStream, file.length());
            psmt.setString(2, username);
            psmt.executeUpdate();

            psmt.close();
            connection.close();
        } catch(SQLException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

    }
}