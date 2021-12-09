package Extra;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Test extends JFrame {

    // Database URL, username and password. URL in format jbdc:mysql://hostname:portNumber:databaseName. Need vpn for this if off campus
    static final String DATABASE_URL = "jdbc:mysql://s-l112.engr.uiowa.edu:3306/swd_db001";
    static final String USERNAME = "swd_group001";
    static final String PASSWORD = "justinsucks1!";

    // query for testing

    public static void main(String[] args) {

        final String QUERY = "SELECT * FROM Organizations";
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY);

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public Test() {

    }

}
