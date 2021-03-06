package hotelfx.persistence.model;
// Generated Sep 5, 2017 11:14:20 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefReservationBillStatus generated by hbm2java
 */
public class RefReservationBillStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblReservationBills;

    public RefReservationBillStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblReservationBills = new HashSet(0);
    }

    public RefReservationBillStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefReservationBillStatus(int idstatus, String statusName, String statusNote, Set tblReservationBills) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);

        this.tblReservationBills = tblReservationBills;
    }

    public RefReservationBillStatus(RefReservationBillStatus refReservationBillStatus) {
        this();
        idstatusProperty().set(refReservationBillStatus.getIdstatus());
        statusNameProperty().set(refReservationBillStatus.getStatusName());
        statusNoteProperty().set(refReservationBillStatus.getStatusNote());

        this.tblReservationBills = refReservationBillStatus.getTblReservationBills();
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

    public Set getTblReservationBills() {
        return this.tblReservationBills;
    }

    public void setTblReservationBills(Set tblReservationBills) {
        this.tblReservationBills = tblReservationBills;
    }

}
