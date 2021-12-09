import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Start.*;

import java.util.Objects;

/**
 * Main class, starts the registration app by showing the entry screen
 */
public class Covid19_Registration extends Application {
    /**
     * Main method
     * @param args  run condition
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method that displays the GUI
     * @param stage stage the GUI scene is set on
     * @throws Exception thrown if file can't be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root =
                FXMLLoader.load(getClass().getResource("Start/Start.fxml"));

        Scene scene = new Scene(root); // attach scene graph to scene
        stage.setTitle("Covid-19 Registration"); // displayed in window's title bar
        stage.setScene(scene); // attach scene to stage
        stage.show(); // display the stage
    }
}