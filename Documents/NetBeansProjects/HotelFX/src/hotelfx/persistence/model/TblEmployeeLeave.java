package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblEmployeeLeave generated by hbm2java
 */
public class TblEmployeeLeave implements java.io.Serializable {

    private final LongProperty idleave;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdemployee;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdalterEmployee;
    private final StringProperty codeLeave;
    private final ObjectProperty<Date> dateCreated;
    private final StringProperty leaveName;
    private final StringProperty leaveNote;
    private final ObjectProperty<Date> startDate;
    private final ObjectProperty<Date> finishDate;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblEmployeeLeaveDetails;

    public TblEmployeeLeave() {
        this.idleave = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByIdemployee = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByIdalterEmployee = new SimpleObjectProperty<>();
        this.codeLeave = new SimpleStringProperty();
        this.dateCreated = new SimpleObjectProperty<>();
        this.leaveName = new SimpleStringProperty();
        this.leaveNote = new SimpleStringProperty();
        this.startDate = new SimpleObjectProperty<>();
        this.finishDate = new SimpleObjectProperty<>();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblEmployeeLeaveDetails = new HashSet(0);
    }

    public TblEmployeeLeave(long idleave) {
        this();
        idleaveProperty().set(idleave);
    }

    public TblEmployeeLeave(long idleave, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByIdemployee, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByIdalterEmployee, String codeLeave, Date dateCreated, String leaveName, String leaveNote, Date startDate, Date finishDate, Date createDate, Date lastUpdateDate, Set tblEmployeeLeaveDetails) {
        this();
        idleaveProperty().set(idleave);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByIdalterEmployeeProperty().set(tblEmployeeByIdalterEmployee);
        codeLeaveProperty().set(codeLeave);
        dateCreatedProperty().set(dateCreated);
        leaveNameProperty().set(leaveName);
        leaveNoteProperty().set(leaveNote);
        startDateProperty().set(startDate);
        finishDateProperty().set(finishDate);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblEmployeeLeaveDetails = tblEmployeeLeaveDetails;
    }

    public TblEmployeeLeave(TblEmployeeLeave tblEmployeeLeave) {
        this();
        idleaveProperty().set(tblEmployeeLeave.getIdleave());
        refRecordStatusProperty().set(tblEmployeeLeave.getRefRecordStatus());
        tblEmployeeByIdemployeeProperty().set(tblEmployeeLeave.getTblEmployeeByIdemployee());
        tblEmployeeByCreateByProperty().set(tblEmployeeLeave.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeLeave.getTblEmployeeByLastUpdateBy());
        tblEmployeeByIdalterEmployeeProperty().set(tblEmployeeLeave.getTblEmployeeByIdalterEmployee());
        codeLeaveProperty().set(tblEmployeeLeave.getCodeLeave());
        dateCreatedProperty().set(tblEmployeeLeave.getDateCreated());
        leaveNameProperty().set(tblEmployeeLeave.getLeaveName());
        leaveNoteProperty().set(tblEmployeeLeave.getLeaveNote());
        startDateProperty().set(tblEmployeeLeave.getStartDate());
        finishDateProperty().set(tblEmployeeLeave.getFinishDate());
        createDateProperty().set(tblEmployeeLeave.getCreateDate());
        lastUpdateDateProperty().set(tblEmployeeLeave.getLastUpdateDate());

        this.tblEmployeeLeaveDetails = tblEmployeeLeave.getTblEmployeeLeaveDetails();
    }

    public final LongProperty idleaveProperty() {
        return this.idleave;
    }

    public long getIdleave() {
        return idleaveProperty().get();
    }

    public void setIdleave(long idleave) {
        idleaveProperty().set(idleave);
    }

    public final ObjectProperty<RefRecordStatus> refRecordStatusProperty() {
        return this.refRecordStatus;
    }

    public RefRecordStatus getRefRecordStatus() {
        return refRecordStatusProperty().get();
    }

    public void setRefRecordStatus(RefRecordStatus refRecordStatus) {
        refRecordStatusProperty().set(refRecordStatus);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByIdemployeeProperty() {
        return this.tblEmployeeByIdemployee;
    }

    public TblEmployee getTblEmployeeByIdemployee() {
        return tblEmployeeByIdemployeeProperty().get();
    }

    public void setTblEmployeeByIdemployee(TblEmployee tblEmployeeByIdemployee) {
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByCreateByProperty() {
        return this.tblEmployeeByCreateBy;
    }

    public TblEmployee getTblEmployeeByCreateBy() {
        return tblEmployeeByCreateByProperty().get();
    }

    public void setTblEmployeeByCreateBy(TblEmployee tblEmployeeByCreateBy) {
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateByProperty() {
        return this.tblEmployeeByLastUpdateBy;
    }

    public TblEmployee getTblEmployeeByLastUpdateBy() {
        return tblEmployeeByLastUpdateByProperty().get();
    }

    public void setTblEmployeeByLastUpdateBy(TblEmployee tblEmployeeByLastUpdateBy) {
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByIdalterEmployeeProperty() {
        return this.tblEmployeeByIdalterEmployee;
    }

    public TblEmployee getTblEmployeeByIdalterEmployee() {
        return tblEmployeeByIdalterEmployeeProperty().get();
    }

    public void setTblEmployeeByIdalterEmployee(TblEmployee tblEmployeeByIdalterEmployee) {
        tblEmployeeByIdalterEmployeeProperty().set(tblEmployeeByIdalterEmployee);
    }

    public final StringProperty codeLeaveProperty() {
        return this.codeLeave;
    }

    public String getCodeLeave() {
        return codeLeaveProperty().get();
    }

    public void setCodeLeave(String codeLeave) {
        codeLeaveProperty().set(codeLeave);
    }

    public final ObjectProperty<Date> dateCreatedProperty() {
        return this.dateCreated;
    }

    public Date getDateCreated() {
        return dateCreatedProperty().get();
    }

    public void setDateCreated(Date dateCreated) {
        dateCreatedProperty().set(dateCreated);
    }

    public final StringProperty leaveNameProperty() {
        return this.leaveName;
    }

    public String getLeaveName() {
        return leaveNameProperty().get();
    }

    public void setLeaveName(String leaveName) {
        leaveNameProperty().set(leaveName);
    }

    public final StringProperty leaveNoteProperty() {
        return this.leaveNote;
    }

    public String getLeaveNote() {
        return leaveNoteProperty().get();
    }

    public void setLeaveNote(String leaveNote) {
        leaveNoteProperty().set(leaveNote);
    }

    public final ObjectProperty<Date> startDateProperty() {
        return this.startDate;
    }

    public Date getStartDate() {
        return startDateProperty().get();
    }

    public void setStartDate(Date startDate) {
        startDateProperty().set(startDate);
    }

    public final ObjectProperty<Date> finishDateProperty() {
        return this.finishDate;
    }

    public Date getFinishDate() {
        return finishDateProperty().get();
    }

    public void setFinishDate(Date finishDate) {
        finishDateProperty().set(finishDate);
    }

    public final ObjectProperty<Date> createDateProperty() {
        return this.createDate;
    }

    public Date getCreateDate() {
        return createDateProperty().get();
    }

    public void setCreateDate(Date createDate) {
        createDateProperty().set(createDate);
    }

    public final ObjectProperty<Date> lastUpdateDateProperty() {
        return this.lastUpdateDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDateProperty().get();
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        lastUpdateDateProperty().set(lastUpdateDate);
    }

    public Set getTblEmployeeLeaveDetails() {
        return this.tblEmployeeLeaveDetails;
    }

    public void setTblEmployeeLeaveDetails(Set tblEmployeeLeaveDetails) {
        this.tblEmployeeLeaveDetails = tblEmployeeLeaveDetails;
    }

}
