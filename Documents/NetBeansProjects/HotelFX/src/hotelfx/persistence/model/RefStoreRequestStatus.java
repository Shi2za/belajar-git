package hotelfx.persistence.model;
// Generated Aug 9, 2018 10:52:06 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefStoreRequestStatus generated by hbm2java
 */
public class RefStoreRequestStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblStoreRequests;

    public RefStoreRequestStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblStoreRequests = new HashSet(0);
    }

    public RefStoreRequestStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefStoreRequestStatus(int idstatus, String statusName, String statusNote, Set tblStoreRequests) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);

        this.tblStoreRequests = tblStoreRequests;
    }

    public RefStoreRequestStatus(RefStoreRequestStatus refStoreRequestStatus) {
        this();
        idstatusProperty().set(refStoreRequestStatus.getIdstatus());
        statusNameProperty().set(refStoreRequestStatus.getStatusName());
        statusNoteProperty().set(refStoreRequestStatus.getStatusNote());

        this.tblStoreRequests = refStoreRequestStatus.getTblStoreRequests();
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

    public Set getTblStoreRequests() {
        return this.tblStoreRequests;
    }

    public void setTblStoreRequests(Set tblStoreRequests) {
        this.tblStoreRequests = tblStoreRequests;
    }

}
