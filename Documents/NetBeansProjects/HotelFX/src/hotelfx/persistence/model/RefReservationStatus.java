package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefReservationStatus generated by hbm2java
 */
public class RefReservationStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblReservations;

    public RefReservationStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblReservations = new HashSet(0);
    }

    public RefReservationStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefReservationStatus(int idstatus, String statusName, String statusNote, Set tblReservations) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);

        this.tblReservations = tblReservations;
    }

    public RefReservationStatus(RefReservationStatus refReservationStatus) {
        this();
        idstatusProperty().set(refReservationStatus.getIdstatus());
        statusNameProperty().set(refReservationStatus.getStatusName());
        statusNoteProperty().set(refReservationStatus.getStatusNote());

        this.tblReservations = refReservationStatus.getTblReservations();
    }

    public final IntegerProperty idstatusProperty() {
        return this.idstatus;
    }

    public int getIdstatus() {
        return idstatusProperty().get();
    }

    public void setIdstatus(int idstatus) {
        idstatusProperty().set(idstatus);
    }

    public final StringProperty statusNameProperty() {
        return this.statusName;
    }

    public String getStatusName() {
        return statusNameProperty().get();
    }

    public void setStatusName(String statusName) {
        statusNameProperty().set(statusName);
    }

    public final StringProperty statusNoteProperty() {
        return this.statusNote;
    }

    public String getStatusNote() {
        return statusNoteProperty().get();
    }

    public void setStatusNote(String statusNote) {
        statusNoteProperty().set(statusNote);
    }

    public Set getTblReservations() {
        return this.tblReservations;
    }

    public void setTblReservations(Set tblReservations) {
        this.tblReservations = tblReservations;
    }

    @Override
    public String toString(){
        return getStatusName();
    }
    
}
