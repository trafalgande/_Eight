package sample;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import lmao.*;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import static lmao.DataBaseManager.currentUser;

public class DBController implements Initializable {
    private String currentLang;

    public String getCurrentLang() {
        return currentLang;
    }

    public void setCurrentLang(String currentLang) {
        this.currentLang = currentLang;
    }

    @FXML
    private ImageView engButton;

    @FXML
    private ImageView estButton;

    @FXML
    private ImageView ruButton;

    @FXML
    private ImageView ukrButton;

    @FXML
    private TextField propName;

    @FXML
    private TextField propSurname;

    @FXML
    private TextField propAge;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Label currentUserName;

    @FXML
    private Button logoutButton;

    @FXML
    private TableColumn<Character, Integer> idColumn;

    @FXML
    private TableColumn<Character, String> nameColumn;

    @FXML
    private TableColumn<Character, String> surnameColumn;

    @FXML
    private TableColumn<Character, Integer> ageColumn;

    @FXML
    private TableColumn<Character, String> timeColumn;

    @FXML
    private TableView<Character> characterTable;

    @FXML
    private Label currentUserLabel;

    @FXML
    private Pane graphicPane;

    @FXML
    private Label uidLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label snameLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private VBox vboxLmao;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("DataBase.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("bandles/Locale", Locale.getDefault()));
        Main.primaryStage.setTitle(fxmlLoader.getResources().getString("appTitle"));
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idPropertyProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNamePropertyProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNamePropertyProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().agePropertyProperty().asObject());
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeProperty().toString()));
        try {
            ObservableList<Character> characterObservableList = buildData();
            characterTable.setItems(characterObservableList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            setCurrentUserId();
        } catch (SQLException e) {
            System.out.println(e);
        }

       //graphicPane.setPadding(new Insets(10));
        //graphicPane.setStyle("-fx-border-width:1px;-fx-border-color:#E2E1E1;-fx-background-color:white;");
        new Thread(() -> currentUserName.setText(currentUser.getAccountName() + " : " + currentUser.getAccountId() + " uID")).start();
        characterTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        currentUserLabel.setText(resourceBundle.getString("CURRENT_USER"));
        switch (resources.getLocale().toString()) {
            case ("ru"):
                ruButton.setImage(new Image("sample/icons/ru_unpressed.png"));
                break;
            case ("en"):
                engButton.setImage(new Image("sample/icons/en_unpressed.png"));
                break;
            case ("ukr"):
                ukrButton.setImage(new Image("sample/icons/ukr_unpressed.png"));
                break;
            case ("est"):
                estButton.setImage(new Image("sample/icons/est_unpressed.png"));
                break;
        }


        //System.out.println(resources.getBaseBundleName());
    }

    public void logoutButtonAction() throws IOException {
        Window owner = logoutButton.getScene().getWindow();
        SocketDto socketDto = new SocketDto();
        String command = "logout";
        socketDto.setCommand(command);
        Client.out.writeObject(socketDto);
        String returnedMessage = Client.in.readLine();
        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, resourceBundle.getString("LOGOUT_CONFIRM"),
                resourceBundle.getString("BB"));
        Stage window = (Stage) owner;
        Parent loginForm = FXMLLoader.load(getClass().getResource("login.fxml"),
                ResourceBundle.getBundle("bandles.Locale", Locale.getDefault()));
        Scene loginFormScene = new Scene(loginForm, 300, 200);
        window.setScene(loginFormScene);
    }

    public ObservableList<Character> buildData() throws SQLException, ClassNotFoundException {
        String sql = "Select * from s263068.character";
        ResultSet resultSet = DBConnect.getConnection().createStatement().executeQuery(sql);
        ObservableList<Character> characterList = getCharacterData(resultSet);
        return characterList;
    }

    private ObservableList<Character> getCharacterData(ResultSet rs) throws SQLException {
        ObservableList<Character> charactersList = FXCollections.observableArrayList();
        graphicPane.getChildren().clear();
        while (rs.next()) {
            Character character = new Character();
            character.setIdProperty(rs.getInt("character_id"));
            character.setFirstNameProperty(rs.getString("firstname"));
            character.setLastNameProperty(rs.getString("secondname"));
            character.setAgeProperty(rs.getInt("age"));
            character.setTimeProperty(rs.getTime("time"));
            draw(character);
            charactersList.add(character);
        }
        return charactersList;
    }


    public void addButtonAction() throws IOException, SQLException, ClassNotFoundException {
        Window owner = addButton.getScene().getWindow();
        SocketDto socketDto = new SocketDto();
        String command = "add " + propName.getText() + " " + propSurname.getText() + " " + propAge.getText();
        socketDto.setCommand(command);
        Client.out.writeObject(socketDto);
        String returnedMessage = Client.in.readLine();
        ObservableList<Character> characterObservableList = buildData();
        characterTable.setItems(characterObservableList);
        if (returnedMessage.equals("Invalid command: add <Name> <Surname> <Age>. Try again.")) {
            if (propName.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("ADD_NAME_PROP"));
            } else if (propSurname.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("ADD_SNAME_PROP"));
            } else if (propAge.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("ADD_AGE_PROP"));
            }

        } else if (returnedMessage.equals("This character already exists.")) {
            AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, resourceBundle.getString("ADDING_CONFIRM"),
                    resourceBundle.getString("EL_EXISTS"));
        } else {
            AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, resourceBundle.getString("ADDING_CONFIRM"),
                    resourceBundle.getString("EL_ADDED"));
        }
    }

    public void removeButtonAction() throws IOException, SQLException, ClassNotFoundException {
        Window owner = removeButton.getScene().getWindow();
        SocketDto socketDto = new SocketDto();
        String command = "remove " + propName.getText() + " " + propSurname.getText() + " " + propAge.getText();
        socketDto.setCommand(command);
        Client.out.writeObject(socketDto);
        String returnedMessage = Client.in.readLine();
        ObservableList<Character> characterObservableList = buildData();
        characterTable.setItems(characterObservableList);
        if (returnedMessage.equals("Invalid command: remove <Name> <Surname> <Age>. Try again.")) {
            if (propName.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("REMOVE_NAME_PROP"));
            } else if (propSurname.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("REMOVE_SNAME_PROP"));

            } else if (propAge.getText().isEmpty()) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("FORM_ERROR"),
                        resourceBundle.getString("REMOVE_AGE_PROP"));
            }
        } else if (returnedMessage.equals("You don't have permissions to remove this element.")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("REMOVE_ERROR"),
                    returnedMessage);
        } else {
            AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, resourceBundle.getString("REMOVE_CONFIRM"),
                    resourceBundle.getString("REMOVE_1") + propName.getText() + resourceBundle.getString("REMOVE_2"));
        }
    }

    public void editFirstNameValueOfColumn(TableColumn.CellEditEvent editEvent) throws SQLException, ClassNotFoundException {
        Window owner = characterTable.getScene().getWindow();
        Character characterSelected = characterTable.getSelectionModel().getSelectedItem();
        if (characterSelected.getIdProperty() == currentUser.getAccountId()) {
            characterSelected.setFirstNameProperty(editEvent.getNewValue().toString());
            String sql = "Update character set firstname = ? where secondname = ?";
            PreparedStatement prs = DBConnect.getConnection().prepareStatement(sql);
            prs.setString(1, String.valueOf(editEvent.getNewValue()));
            prs.setString(2, characterSelected.getLastNameProperty());
            prs.execute();
        } else {
            ObservableList<Character> characterObservableList = buildData();
            characterTable.setItems(characterObservableList);
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("EDIT_ERROR"),
                    resourceBundle.getString("EDIT_PERM"));
        }
    }

    public void editLastNameValueOfColumn(TableColumn.CellEditEvent editEvent) throws SQLException, ClassNotFoundException {
        Window owner = characterTable.getScene().getWindow();
        Character characterSelected = characterTable.getSelectionModel().getSelectedItem();
        if (characterSelected.getIdProperty() == currentUser.getAccountId()) {
            characterSelected.setLastNameProperty(editEvent.getNewValue().toString());
            String sql = "Update character set secondname = ? where firstname = ?";
            PreparedStatement prs = DBConnect.getConnection().prepareStatement(sql);
            prs.setString(1, String.valueOf(editEvent.getNewValue()));
            prs.setString(2, characterSelected.getFirstNameProperty());
            prs.execute();
        } else {
            ObservableList<Character> characterObservableList = buildData();
            characterTable.setItems(characterObservableList);
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("EDIT_ERROR"),
                    resourceBundle.getString("EDIT_PERM"));
        }
    }

    public void editAgeValueOfColumn(TableColumn.CellEditEvent editEvent) throws SQLException, ClassNotFoundException {
        Window owner = characterTable.getScene().getWindow();
        Character characterSelected = characterTable.getSelectionModel().getSelectedItem();
        if (characterSelected.getIdProperty() == currentUser.getAccountId()) {
            characterSelected.setAgeProperty((Integer) editEvent.getNewValue());
            String sql = "Update character set age = ? where firstname = ?";
            PreparedStatement prs = DBConnect.getConnection().prepareStatement(sql);
            prs.setInt(1, (Integer) editEvent.getNewValue());
            prs.setString(2, characterSelected.getFirstNameProperty());
            prs.execute();
        } else {
            ObservableList<Character> characterObservableList = buildData();
            characterTable.setItems(characterObservableList);
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, resourceBundle.getString("EDIT_ERROR"),
                    resourceBundle.getString("EDIT_PERM"));
        }


    }
    /*public void updateCell (String column, Object newValue, String fnameprop, String snameprop) throws SQLException {
        String sql = "Update character set " + column + " = ? where firstname = ? and secondname = ?";
        PreparedStatement prs = DBConnect.getConnection().prepareStatement(sql);
        prs.setObject(1, newValue);
        prs.setString(2, fnameprop);
        prs.setString(3, snameprop);
        prs.execute();
    }*/


    public void setCurrentUserId() throws SQLException {
        String sql = "Select account_id from registrationform where accountname = ?";
        PreparedStatement prs = DBConnect.getConnection().prepareStatement(sql);
        prs.setString(1, currentUser.getAccountName());
        ResultSet rs = prs.executeQuery();
        while (rs.next()) {
            currentUser.setAccountId(rs.getInt("account_id"));
        }
    }

    public void changeLanguageToEnglish() throws IOException {
        Window owner = characterTable.getScene().getWindow();
        Stage window = (Stage) owner;
        engButton.setDisable(true);
        Locale.setDefault(new Locale("en"));
        Parent DBView = FXMLLoader.load(getClass().getResource("DataBase.fxml"),
                ResourceBundle.getBundle("bandles/Locale", Locale.getDefault()));
        Scene DBScene = new Scene(DBView);
        window.setScene(DBScene);
        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Changing confirmation",
                "You have successfully changed the language to English");
    }

    public void changeLanguageToRussian() throws IOException {
        Window owner = characterTable.getScene().getWindow();
        Stage window = (Stage) owner;
        ruButton.setDisable(true);
        Locale.setDefault(new Locale("ru"));
        Parent DBView = FXMLLoader.load(getClass().getResource("DataBase.fxml"),
                ResourceBundle.getBundle("bandles/Locale", Locale.getDefault()));
        Scene DBScene = new Scene(DBView);
        window.setScene(DBScene);
        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Подтверждение изменения",
                "Вы успешно сменили язык на русский");
    }

    public void changeLanguageToUkranian() throws IOException {
        Window owner = characterTable.getScene().getWindow();
        Stage window = (Stage) owner;
        ukrButton.setDisable(true);
        Locale.setDefault(new Locale("ukr"));
        Parent DBView = FXMLLoader.load(getClass().getResource("DataBase.fxml"),
                ResourceBundle.getBundle("bandles/Locale", Locale.getDefault()));
        Scene DBScene = new Scene(DBView);
        window.setScene(DBScene);
        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Зміна підтвердження",
                "Ви успішно змінили мову українською мовою");
    }

    public void changeLanguageToEstonian() throws IOException {
        Window owner = characterTable.getScene().getWindow();
        Stage window = (Stage) owner;
        estButton.setDisable(true);
        Locale.setDefault(new Locale("est"));
        Parent DBView = FXMLLoader.load(getClass().getResource("DataBase.fxml"),
                ResourceBundle.getBundle("bandles/Locale", Locale.getDefault()));
        Scene DBScene = new Scene(DBView);
        window.setScene(DBScene);
        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Kinnituse muutmine",
                "Te olete eesti keelt edukalt muutnud");
    }

    private SecureRandom random = new SecureRandom();

    public void draw(Character character) {
        int ageProp = character.getAgeProperty();
        int idProp = character.getIdProperty();
        double locProp;
        String nameProp = character.getFirstNameProperty();
        String lastNameProp = character.getLastNameProperty();
        Time timeProp = character.getTimeProperty();
        if (ageProp > 70) {
            locProp = 70;
        } else if (ageProp < 25) {
            locProp = 25;
        } else {
            locProp = ageProp;
        }

        int width = (int) (graphicPane.getMaxWidth());
        int height = (int) (graphicPane.getMaxHeight());
        double x = random.nextInt(width-1);
        double y = random.nextInt(height-1);
        Circle circle = new Circle(x, y, locProp*0.2);
        String color = String.format("#%06x", 0xffa * (idProp+1));
        circle.setFill(Paint.valueOf(color));
        circle.setStroke(Color.BLANCHEDALMOND);
        graphicPane.getChildren().add(circle);
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.seconds(1.5));
        scaleTransition.setNode(circle);
        scaleTransition.setByX(.2);
        scaleTransition.setByY(.2);
        scaleTransition.play();
        scaleTransition.setDelay(Duration.seconds(.5));

        circle.setOnMouseClicked(event -> {
            uidLabel.setText(String.valueOf(idProp));
            nameLabel.setText(nameProp);
            snameLabel.setText(lastNameProp);
            ageLabel.setText(String.valueOf(ageProp));
            timeLabel.setText(String.valueOf(timeProp));
        });

    }

}

                    //70McVkfTba