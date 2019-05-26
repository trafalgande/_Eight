package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lmao.*;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;

import static lmao.DataBaseManager.currentUser;

public class Controller implements Initializable {
    public String command;
    //private lmao.SocketDto socketDto;
    private DataBaseManager dbm;
    @FXML
    private PasswordField pass;
    @FXML
    private TextField user;
    @FXML
    private Hyperlink hl;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancerButton;

    private ResourceBundle resourceBundle;

    @FXML
    private AnchorPane currentLoginPanel;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("login.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("bandles/Locale", Locale.getDefault()));
        Main.primaryStage.setTitle(fxmlLoader.getResources().getString("appTitle"));
    }

    public void loginButtonAction () throws IOException {
        Window owner = loginButton.getScene().getWindow();
        SocketDto socketDto = new SocketDto();
        command = "login " + user.getText() + " " + pass.getText();
        socketDto.setCommand(command);
        Client.out.writeObject(socketDto);
        currentUser.setAccountName(user.getText());
        String returnedMessage = Client.in.readLine();
        if (returnedMessage.equals("Invalid command: login <username> <password>. Try again.")) {
            if (user.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("PLS_ENTER_USERNAME"));
            } else
                if (pass.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("PLS_ENTER_PASSWORD"));
            }
        } else if (returnedMessage.equals("Wrong password or username.")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                    resourceBundle.getString("WRONG_PASS_OR_USER"));
        } else {
            AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, resourceBundle.getString("LOG_STATUS"),
                    resourceBundle.getString("LOGIN_1") + user.getText() + ".");
            Stage window = (Stage) owner;
            Parent DBView = FXMLLoader.load(getClass().getResource("DataBase.fxml"),
                    ResourceBundle.getBundle("bandles.Locale", Locale.getDefault()));
            Scene DBScene = new Scene(DBView);
            window.setScene(DBScene);
        }
    }

    public void cancerButtonAction() throws IOException {
        Window owner = cancerButton.getScene().getWindow();
        Stage window = (Stage) owner;
        Parent DBView = FXMLLoader.load(getClass().getResource("start.fxml"),resourceBundle);
        Scene DBScene = new Scene(DBView);
        window.setScene(DBScene);
    }



    public void hyperLinkAction(ActionEvent event) throws IOException {
        Parent registrationForm = FXMLLoader.load(getClass().getResource("registrationForm.fxml"), resourceBundle);
        Scene registrationFormScene = new Scene(registrationForm, 300,200);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(registrationFormScene);
        window.show();
     }


}


