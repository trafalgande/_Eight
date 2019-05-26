package sample;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableNumberValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.OffsetDateTime;
import java.util.Date;

public class Character {

    private StringProperty firstNameProperty;
    private StringProperty lastNameProperty;
    private IntegerProperty ageProperty;
    private IntegerProperty idProperty;
    private Time timeProperty;


    public String getFirstNameProperty() {
        return firstNameProperty.getValue();
    }

    public StringProperty firstNamePropertyProperty() {
        return firstNameProperty;
    }

    public String getLastNameProperty() {
        return lastNameProperty.getValue();
    }

    public StringProperty lastNamePropertyProperty() {
        return lastNameProperty;
    }

    public int getAgeProperty() {
        return ageProperty.getValue();
    }

    public IntegerProperty agePropertyProperty() {
        return ageProperty;
    }

    public int getIdProperty() {
        return idProperty.getValue();
    }

    public IntegerProperty idPropertyProperty() {
        return idProperty;
    }

    public Time getTimeProperty() {
        return timeProperty;
    }

    public Character(String firstNameProperty, String lastNameProperty, Integer ageProperty, Integer idProperty, Time timeProperty) {
        this.firstNameProperty = new SimpleStringProperty(firstNameProperty);
        this.lastNameProperty = new SimpleStringProperty(lastNameProperty);
        this.ageProperty = new SimpleIntegerProperty(ageProperty);
        this.idProperty = new SimpleIntegerProperty(idProperty);
        this.timeProperty = timeProperty;
    }

    public Character(){
        this.firstNameProperty = new SimpleStringProperty();
        this.lastNameProperty = new SimpleStringProperty();
        this.ageProperty = new SimpleIntegerProperty();
        this.idProperty = new SimpleIntegerProperty();
        this.timeProperty = new Time(new Date().getTime());
    }

    public void setFirstNameProperty(String firstNameProperty) {
        this.firstNameProperty.setValue(firstNameProperty);
    }

    public void setLastNameProperty(String lastNameProperty) {
        this.lastNameProperty.setValue(lastNameProperty);
    }

    public void setAgeProperty(int ageProperty) {
        this.ageProperty.setValue(ageProperty);
    }

    public void setTimeProperty(Time time) {
        this.timeProperty = time;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty.setValue(idProperty);
    }
}
