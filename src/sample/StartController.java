package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class StartController {

    @FXML
    private BorderPane currentPane;

    public void engButtonPressed() throws IOException {
        Locale.setDefault(new Locale ("en"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bandles.Locale", new Locale("en"));
        swapScene(resourceBundle);
    }
    public void estonButtonPressed() throws IOException {
        Locale.setDefault(new Locale ("est"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bandles.Locale", new Locale("est"));
        swapScene(resourceBundle);
    }
    public void rusButtonPressed() throws IOException {
        Locale.setDefault(new Locale ("ru"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bandles.Locale", new Locale("ru"));
        swapScene(resourceBundle);
    }
    public void ukrButtonPressed() throws IOException {
        Locale.setDefault(new Locale ("ukr"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bandles.Locale", new Locale("ukr"));
        swapScene(resourceBundle);

    }
    public void swapScene(ResourceBundle resourceBundle) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"), resourceBundle);
        Window owner = currentPane.getScene().getWindow();
        Stage window = (Stage) owner;
        Scene nextScene = new Scene(root);
        window.setScene(nextScene);
    }
}

