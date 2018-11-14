package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefJobRecruitmentShowStatus generated by hbm2java
 */
public class RefJobRecruitmentShowStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblJobs;

    public RefJobRecruitmentShowStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblJobs = new HashSet(0);
    }

    public RefJobRecruitmentShowStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefJobRecruitmentShowStatus(int idstatus, String statusName, String statusNote, Set tblJobs) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);
        this.tblJobs = tblJobs;
    }

    public RefJobRecruitmentShowStatus(RefJobRecruitmentShowStatus refJobRecruitmentShowStatus) {
        this();
        idstatusProperty().set(refJobRecruitmentShowStatus.getIdstatus());
        statusNameProperty().set(refJobRecruitmentShowStatus.getStatusName());
        statusNoteProperty().set(refJobRecruitmentShowStatus.getStatusNote());

        this.tblJobs = refJobRecruitmentShowStatus.getTblJobs();
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

    public Set getTblJobs() {
        return this.tblJobs;
    }

    public void setTblJobs(Set tblJobs) {
        this.tblJobs = tblJobs;
    }

    @Override
    public String toString() {
        return getStatusName();
    }

}