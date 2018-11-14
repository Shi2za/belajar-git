package hotelfx.persistence.model;
// Generated May 18, 2018 3:25:45 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefReservationAdditionalItemReservedStatus generated by hbm2java
 */
public class RefReservationAdditionalItemReservedStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblReservationAdditionalItemReserveds;

    public RefReservationAdditionalItemReservedStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblReservationAdditionalItemReserveds = new HashSet(0);
    }

    public RefReservationAdditionalItemReservedStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefReservationAdditionalItemReservedStatus(int idstatus, String statusName, String statusNote, Set tblReservationAdditionalItemReserveds) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);

        this.tblReservationAdditionalItemReserveds = tblReservationAdditionalItemReserveds;
    }

    public RefReservationAdditionalItemReservedStatus(RefReservationAdditionalItemReservedStatus refReservationAdditionalItemReservedStatus) {
        this();
        idstatusProperty().set(refReservationAdditionalItemReservedStatus.getIdstatus());
        statusNameProperty().set(refReservationAdditionalItemReservedStatus.getStatusName());
        statusNoteProperty().set(refReservationAdditionalItemReservedStatus.getStatusNote());

        this.tblReservationAdditionalItemReserveds = refReservationAdditionalItemReservedStatus.getTblReservationAdditionalItemReserveds();
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

    public Set getTblReservationAdditionalItemReserveds() {
        return this.tblReservationAdditionalItemReserveds;
    }

    public void setTblReservationAdditionalItemReserveds(Set tblReservationAdditionalItemReserveds) {
        this.tblReservationAdditionalItemReserveds = tblReservationAdditionalItemReserveds;
    }

}
