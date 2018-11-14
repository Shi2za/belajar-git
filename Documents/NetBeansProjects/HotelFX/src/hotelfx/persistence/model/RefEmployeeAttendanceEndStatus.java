package hotelfx.persistence.model;
// Generated Dec 18, 2017 3:39:13 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * RefEmployeeAttendanceEndStatus generated by hbm2java
 */
public class RefEmployeeAttendanceEndStatus implements java.io.Serializable {

    private final IntegerProperty idstatus;
    private final StringProperty statusName;
    private final StringProperty statusNote;

    private Set tblCalendarEmployeeAttendances;

    public RefEmployeeAttendanceEndStatus() {
        this.idstatus = new SimpleIntegerProperty();
        this.statusName = new SimpleStringProperty();
        this.statusNote = new SimpleStringProperty();

        this.tblCalendarEmployeeAttendances = new HashSet(0);
    }

    public RefEmployeeAttendanceEndStatus(int idstatus) {
        this();
        idstatusProperty().set(idstatus);
    }

    public RefEmployeeAttendanceEndStatus(int idstatus, String statusName, String statusNote, Set tblCalendarEmployeeAttendances) {
        this();
        idstatusProperty().set(idstatus);
        statusNameProperty().set(statusName);
        statusNoteProperty().set(statusNote);

        this.tblCalendarEmployeeAttendances = tblCalendarEmployeeAttendances;
    }

    public RefEmployeeAttendanceEndStatus(RefEmployeeAttendanceEndStatus refEmployeeAttendanceEndStatus) {
        this();
        idstatusProperty().set(refEmployeeAttendanceEndStatus.getIdstatus());
        statusNameProperty().set(refEmployeeAttendanceEndStatus.getStatusName());
        statusNoteProperty().set(refEmployeeAttendanceEndStatus.getStatusNote());

        this.tblCalendarEmployeeAttendances = refEmployeeAttendanceEndStatus.getTblCalendarEmployeeAttendances();
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

    public Set getTblCalendarEmployeeAttendances() {
        return this.tblCalendarEmployeeAttendances;
    }

    public void setTblCalendarEmployeeAttendances(Set tblCalendarEmployeeAttendances) {
        this.tblCalendarEmployeeAttendances = tblCalendarEmployeeAttendances;
    }
    
    @Override
    public String toString(){
        return getStatusName();
    }

}