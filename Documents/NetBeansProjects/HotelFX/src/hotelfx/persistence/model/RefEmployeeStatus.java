package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefEmployeeStatus generated by hbm2java
 */
public class RefEmployeeStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblEmployees;

    public RefEmployeeStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblEmployees = new HashSet(0);
    }

    public RefEmployeeStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefEmployeeStatus(int idstatus, String statusName, String statusNote, Set tblEmployees) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);
        this.tblEmployees = tblEmployees;
    }

    public RefEmployeeStatus(RefEmployeeStatus refEmployeeStatus) {
        this();
        idstatusProperty().set(refEmployeeStatus.getIdstatus());
        statusNameProperty().set(refEmployeeStatus.getStatusName());
        statusNoteProperty().set(refEmployeeStatus.getStatusNote());

        this.tblEmployees = refEmployeeStatus.getTblEmployees();
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

    public Set getTblEmployees() {
        return this.tblEmployees;
    }

    public void setTblEmployees(Set tblEmployees) {
        this.tblEmployees = tblEmployees;
    }

    @Override
    public String toString() {
        return getStatusName();
    }

}
