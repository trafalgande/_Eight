package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.management.jfr.FlightRecorderMXBean;
import lmao.Client;
import lmao.Server;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("start.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("bandles/Locale", Locale.getDefault()));
        primaryStage.setTitle(fxmlLoader.getResources().getString("appTitle"));
        Parent root = fxmlLoader.load();
        System.out.println(Locale.getDefault());

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        new Client("127.0.0.1", 8200);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
