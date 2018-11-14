package hotelfx.persistence.model;
// Generated Feb 12, 2018 3:42:31 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * SysCurrentHotelDate generated by hbm2java
 */
public class SysCurrentHotelDate implements java.io.Serializable {

    private final IntegerProperty id;
    private final StringProperty currentHotelDateName;
    private final ObjectProperty<Date> currentHotelDate;
    private final StringProperty currentHotelDateNote;

    public SysCurrentHotelDate() {
        this.id = new SimpleIntegerProperty();
        this.currentHotelDateName = new SimpleStringProperty();
        this.currentHotelDate = new SimpleObjectProperty<>();
        this.currentHotelDateNote = new SimpleStringProperty();

    }

    public SysCurrentHotelDate(int id) {
        this();
        idProperty().set(id);
    }

    public SysCurrentHotelDate(int id, String currentHotelDateName, Date currentHotelDate, String currentHotelDateNote) {
        this();
        idProperty().set(id);
        currentHotelDateNameProperty().set(currentHotelDateName);
        currentHotelDateProperty().set(currentHotelDate);
        currentHotelDateNoteProperty().set(currentHotelDateNote);

    }

    public SysCurrentHotelDate(SysCurrentHotelDate sysCurrentHotelDate) {
        this();
        idProperty().set(sysCurrentHotelDate.getId());
        currentHotelDateNameProperty().set(sysCurrentHotelDate.getCurrentHotelDateName());
        currentHotelDateProperty().set(sysCurrentHotelDate.getCurrentHotelDate());
        currentHotelDateNoteProperty().set(sysCurrentHotelDate.getCurrentHotelDateNote());

    }

    public final IntegerProperty idProperty() {
        return this.id;
    }

    public int getId() {
        return idProperty().get();
    }

    public void setId(int id) {
        idProperty().set(id);
    }

    public final StringProperty currentHotelDateNameProperty() {
        return this.currentHotelDateName;
    }

    public String getCurrentHotelDateName() {
        return currentHotelDateNameProperty().get();
    }

    public void setCurrentHotelDateName(String currentHotelDateName) {
        currentHotelDateNameProperty().set(currentHotelDateName);
    }

    public final ObjectProperty<Date> currentHotelDateProperty() {
        return this.currentHotelDate;
    }

    public Date getCurrentHotelDate() {
        return currentHotelDateProperty().get();
    }

    public void setCurrentHotelDate(Date currentHotelDate) {
        currentHotelDateProperty().set(currentHotelDate);
    }

    public final StringProperty currentHotelDateNoteProperty() {
        return this.currentHotelDateNote;
    }

    public String getCurrentHotelDateNote() {
        return currentHotelDateNoteProperty().get();
    }

    public void setCurrentHotelDateNote(String currentHotelDateNote) {
        currentHotelDateNoteProperty().set(currentHotelDateNote);
    }

}