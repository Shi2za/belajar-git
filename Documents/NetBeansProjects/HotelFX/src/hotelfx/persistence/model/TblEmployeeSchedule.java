package hotelfx.persistence.model;
// Generated Mar 7, 2018 10:53:22 AM by Hibernate Tools 4.3.1

import java.sql.Time;
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
 * TblEmployeeSchedule generated by hbm2java
 */
public class TblEmployeeSchedule implements java.io.Serializable {

    private final LongProperty idschedule;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty codeSchedule;
    private final StringProperty scheduleName;
    private final ObjectProperty<Time> beginTime;
    private final ObjectProperty<Time> endTime;
    private final StringProperty scheduleNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblCalendarEmployeeSchedules;

    public TblEmployeeSchedule() {
        this.idschedule = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.codeSchedule = new SimpleStringProperty();
        this.scheduleName = new SimpleStringProperty();
        this.beginTime = new SimpleObjectProperty<>();
        this.endTime = new SimpleObjectProperty<>();
        this.scheduleNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblCalendarEmployeeSchedules = new HashSet(0);
    }

    public TblEmployeeSchedule(long idschedule) {
        this();
        idscheduleProperty().set(idschedule);
    }

    public TblEmployeeSchedule(long idschedule, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String codeSchedule, String scheduleName, Time beginTime, Time endTime, String scheduleNote, Date createDate, Date lastUpdateDate, Set tblCalendarEmployeeSchedules) {
        this();
        idscheduleProperty().set(idschedule);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        codeScheduleProperty().set(codeSchedule);
        scheduleNameProperty().set(scheduleName);
        beginTimeProperty().set(beginTime);
        endTimeProperty().set(endTime);
        scheduleNoteProperty().set(scheduleNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblCalendarEmployeeSchedules = tblCalendarEmployeeSchedules;
    }

    public TblEmployeeSchedule(TblEmployeeSchedule tblEmployeeSchedule) {
        this();
        idscheduleProperty().set(tblEmployeeSchedule.getIdschedule());
        refRecordStatusProperty().set(tblEmployeeSchedule.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblEmployeeSchedule.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeSchedule.getTblEmployeeByLastUpdateBy());
        codeScheduleProperty().set(tblEmployeeSchedule.getCodeSchedule());
        scheduleNameProperty().set(tblEmployeeSchedule.getScheduleName());
        beginTimeProperty().set(tblEmployeeSchedule.getBeginTime());
        endTimeProperty().set(tblEmployeeSchedule.getEndTime());
        scheduleNoteProperty().set(tblEmployeeSchedule.getScheduleNote());
        createDateProperty().set(tblEmployeeSchedule.getCreateDate());
        lastUpdateDateProperty().set(tblEmployeeSchedule.getLastUpdateDate());

        this.tblCalendarEmployeeSchedules = tblEmployeeSchedule.getTblCalendarEmployeeSchedules();
    }

    public final LongProperty idscheduleProperty() {
        return this.idschedule;
    }

    public long getIdschedule() {
        return idscheduleProperty().get();
    }

    public void setIdschedule(long idschedule) {
        idscheduleProperty().set(idschedule);
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

    public final StringProperty codeScheduleProperty() {
        return this.codeSchedule;
    }

    public String getCodeSchedule() {
        return codeScheduleProperty().get();
    }

    public void setCodeSchedule(String codeSchedule) {
        codeScheduleProperty().set(codeSchedule);
    }

    public final StringProperty scheduleNameProperty() {
        return this.scheduleName;
    }

    public String getScheduleName() {
        return scheduleNameProperty().get();
    }

    public void setScheduleName(String scheduleName) {
        scheduleNameProperty().set(scheduleName);
    }

    public final ObjectProperty<Time> beginTimeProperty() {
        return this.beginTime;
    }

    public Time getBeginTime() {
        return beginTimeProperty().get();
    }

    public void setBeginTime(Time beginTime) {
        beginTimeProperty().set(beginTime);
    }

    public final ObjectProperty<Time> endTimeProperty() {
        return this.endTime;
    }

    public Time getEndTime() {
        return endTimeProperty().get();
    }

    public void setEndTime(Time endTime) {
        endTimeProperty().set(endTime);
    }

    public final StringProperty scheduleNoteProperty() {
        return this.scheduleNote;
    }

    public String getScheduleNote() {
        return scheduleNoteProperty().get();
    }

    public void setScheduleNote(String scheduleNote) {
        scheduleNoteProperty().set(scheduleNote);
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

    public Set getTblCalendarEmployeeSchedules() {
        return this.tblCalendarEmployeeSchedules;
    }

    public void setTblCalendarEmployeeSchedules(Set tblCalendarEmployeeSchedules) {
        this.tblCalendarEmployeeSchedules = tblCalendarEmployeeSchedules;
    }

    @Override
    public String toString() {
        return getScheduleName();
    }
    
}
