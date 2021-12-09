package Venue_Interface;
import Extra.Names;
import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.util.Properties;

/**
 * Contains logic and database connections for the venue interface
 */
public class Venue_Interface_Model {

    /**
     * URL to access the database
     */
    private static final String DATABASE_URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/swd_db001";

    /**
     * Admin username used to access the database
     */
    private static final String USERNAME = "swd_group001";

    /**
     * Password used to login to the database
     */
    private static final String PASSWORD = "justinsucks1!";
    private static final String QUERY = "SELECT MaskReq, VacReq, TestReq, AdditionalComments FROM Venues";

    /**
     * Used to update the venues name in the database to the new desired name.
     * Establishes a connection to the database then sets the name value to the new value.
     * @param user the organizations unique username
     * @param name the new name
     */
    public void changeName(String user, String name){
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Venues set OrgName = ? where Usernames = ?");
            psmt.setString(1,name);
            psmt.setString(2,user);
            psmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("Got an exception");
        }
    }

    /**
     * Used to update the venues password in the database to the desired password
     * Establishes a connection to the database then sets the password value to the new string value
     * @param user individuals username
     * @param password  new password value
     */
    public void changePassword(String user, String password){
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Venues set Passwords = ? where Usernames = ?");
            psmt.setString(1,password);
            psmt.setString(2,user);
            psmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("Got an exception");
        }
    }

    /**
     * Used to update whether or not the venue is requiring masks.
     * Establishes a connection to the database and sets the new requirement.
     * @param user  venues username
     * @param check boolean standing for the requirement (i.e. true if masks are required)
     */
    public void changeMaskReq(String user, boolean check){
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Venues set MaskReq = ? where Usernames = ?");
            psmt.setBoolean(1,check);
            psmt.setString(2,user);
            psmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("Got an exception");
        }
    }

    /**
     * Used to update whether or not the venue is requiring individuals trying to access the venue tp be vaccinated.
     * Establishes a connection to the database and sets the new requirement.
     * @param user  venues username
     * @param check boolean standing for the requirement (i.e. true if vaccines are required)
     */
    public void changeVaxReq(String user, boolean check){
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Venues set VacReq = ? where Usernames = ?");
            psmt.setBoolean(1,check);
            psmt.setString(2,user);
            psmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("Got an exception");
        }
    }

    /**
     * Used to update whether or not the venue is requiring COVID tests.
     * Establishes a connection to the database and sets the new requirement.
     * @param user  venues username
     * @param check boolean standing for the requirement (i.e. true if tests are required)
     */
    public void changeTestReq(String user, boolean check){
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Venues set TestReq = ? where Usernames = ?");
            psmt.setBoolean(1,check);
            psmt.setString(2,user);
            psmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("Got an exception");
        }
    }

    /**
     * Used to update the stored value of the venues additional comments.
     * Establishes a connection to the database and sets the new comments.
     * @param user venues username
     * @param comments new comments
     */
    public void changeAdditionalComments(String user, String comments){
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Venues set AdditionalComments = ? where Usernames = ?");
            psmt.setString(1,comments);
            psmt.setString(2,user);
            psmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the stored value of a venue's location.
     * Establishes a connection to the database and sets the new location
     * @param location new location to be updated too
     * @param user  venues username
     */
    public void location(String location, String user) {
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            PreparedStatement psmt = connection.prepareStatement("update Venues set Location = ? where Usernames= ?");
            psmt.setString(1, location);
            psmt.setString(2, user);
            psmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the list of individuals that have been approved access to the venue.
     * When the venue approves an individual tha requested access, they are moved
     * out of the requested list and into the venues approved list
     * @param user the individual being approved access
     * @param status the individuals approval status
     */
    public void changeApprovalStatus(String venue, String user, boolean status){
        if(status){
            try {
                //gets list of approved users from venue
                Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                String get_list_command = "select Approved from Venues where Usernames = '"+ venue +"';";
                ResultSet rs1 = connection.createStatement().executeQuery(get_list_command);
                rs1.next();
                String list = rs1.getString("Approved");
                String new_approved;

                //checks if approved list is empty
                if(list.equals("")){
                    new_approved = user;
                    PreparedStatement psmt = connection.prepareStatement("update Venues set Approved = ? where Usernames = ?");
                    psmt.setString(1,new_approved);
                    psmt.setString(2,venue);
                    psmt.executeUpdate();
                }// if it isnt empty it checks if it only has one value by checking for a comma
                else {
                    boolean flag = true;
                    boolean has_comma = false;
                    String[] check_for_comma = list.split("");
                    for(String value : check_for_comma){
                        if(value.equals(",")){
                            has_comma = true;
                        }
                    }
                    //if it has a comma, it will split the list from the commas and check if the person has already been approved
                    if(has_comma){
                        String[] all_approved = list.split(",");
                        for(String approved_users : all_approved){
                            if(user.equals(approved_users)){
                                flag = false;
                            }
                        }
                    } else {
                        if(user.equals(list)){
                            flag = false;
                        }
                    }

                    if(flag){
                        new_approved = list + "," + user;
                        PreparedStatement psmt = connection.prepareStatement("update Venues set Approved = ? where Usernames = ?");
                        psmt.setString(1,new_approved);
                        psmt.setString(2,venue);
                        psmt.executeUpdate();
                    }
                }
                // get approved list for updating values
                String getUser = "select * from Users where Usernames= '" + user + "'";
                rs1 = connection.createStatement().executeQuery(getUser);
                rs1.next();
                String approved = rs1.getString("Approved");
                // set the user to approved
                String setUser = "update Users set Approved = ? where Usernames = ?";
                PreparedStatement psmt = connection.prepareStatement(setUser);
                psmt.setString(1, approved+venue+",");
                psmt.setString(2, user);
                psmt.executeUpdate();

            } catch(SQLException e){
                System.err.println("Got an exception");
            }
        }

        //remove from requested
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            String get_requests_command = "select Requested from Venues where Usernames = '" + venue + "';";
            ResultSet request_list = connection.createStatement().executeQuery(get_requests_command);
            request_list.next();
            String requests = request_list.getString("Requested");
            String new_requested="";

            String[] all_requested = requests.split(",");
            for(String requested_users : all_requested){
                if(!user.equals(requested_users)){
                    new_requested += requested_users + ",";
                }
            }
            System.out.println(new_requested);


            PreparedStatement update_requests = connection.prepareStatement("update Venues set Requested = ? where Usernames = ?");
            update_requests.setString(1, new_requested);
            update_requests.setString(2,venue);
            update_requests.executeUpdate();

        } catch(SQLException e){
            System.err.println("oops");
        }

    }




}
