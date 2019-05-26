package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import lmao.AlertHelper;
import lmao.Client;
import lmao.SocketDto;
import lmao.*;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import static lmao.DataBaseManager.currentUser;

public class RegController implements Initializable {

    @FXML
    private TextField email;

    @FXML
    private TextField username;

    @FXML
    private Button regButton;

    @FXML
    private Button cancerButton;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
    }


    public void registrationButtonAction() throws IOException {
        Window owner = regButton.getScene().getWindow();
        SocketDto socketDto = new SocketDto();
        String command = "reg " + username.getText() + " " + email.getText();
        socketDto.setCommand(command);
        Client.out.writeObject(socketDto);
        currentUser.setAccountName(username.getText());
        String returnedMessage = Client.in.readLine();
        if (returnedMessage.equals("Invalid command: reg <username> <e-mail>. Try again.")) {
            if(username.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("PLS_ENTER_USERNAME"));
            } else
            if(email.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("PLS_ENTER_EMAIL"));

            } else
            if(!email.getText().matches(CommandHandler.regex)) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("WRONG_FORMAT_EMAIL"));
            }
        } else {
                AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, resourceBundle.getString("REG_STATUS"),
                        resourceBundle.getString("REG_ANSWER_1") + username.getText() +
                                resourceBundle.getString("REG_ANSWER_2") + email.getText());
                Stage window = (Stage) owner;
                Parent DBView = FXMLLoader.load(getClass().getResource("DataBase.fxml"), resourceBundle);
                Scene DBScene = new Scene(DBView);
                window.setScene(DBScene);
            }
    }


    public void cancerButtonAction(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent loginForm = FXMLLoader.load(getClass().getResource("login.fxml"), resourceBundle);
        Scene loginFormScene = new Scene(loginForm, 300,200);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(loginFormScene);
        window.show();
    }


}