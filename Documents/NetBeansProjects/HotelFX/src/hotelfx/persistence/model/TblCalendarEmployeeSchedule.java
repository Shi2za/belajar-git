package hotelfx.persistence.model;
// Generated Nov 1, 2017 3:39:36 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TblCalendarEmployeeSchedule generated by hbm2java
 */
public class TblCalendarEmployeeSchedule implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<SysCalendar> sysCalendar;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdemployee;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployeeSchedule> tblEmployeeSchedule;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblCalendarEmployeeSchedule() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.sysCalendar = new SimpleObjectProperty<>();
        this.tblEmployeeByIdemployee = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeSchedule = new SimpleObjectProperty<>();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblCalendarEmployeeSchedule(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblCalendarEmployeeSchedule(long idrelation, RefRecordStatus refRecordStatus, SysCalendar sysCalendar, TblEmployee tblEmployeeByIdemployee, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblEmployeeSchedule tblEmployeeSchedule, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        sysCalendarProperty().set(sysCalendar);
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeScheduleProperty().set(tblEmployeeSchedule);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblCalendarEmployeeSchedule(TblCalendarEmployeeSchedule tblCalendarEmployeeSchedule) {
        this();
        idrelationProperty().set(tblCalendarEmployeeSchedule.getIdrelation());
        refRecordStatusProperty().set(tblCalendarEmployeeSchedule.getRefRecordStatus());
        sysCalendarProperty().set(tblCalendarEmployeeSchedule.getSysCalendar());
        tblEmployeeByIdemployeeProperty().set(tblCalendarEmployeeSchedule.getTblEmployeeByIdemployee());
        tblEmployeeByLastUpdateByProperty().set(tblCalendarEmployeeSchedule.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblCalendarEmployeeSchedule.getTblEmployeeByCreateBy());
        tblEmployeeScheduleProperty().set(tblCalendarEmployeeSchedule.getTblEmployeeSchedule());
        createDateProperty().set(tblCalendarEmployeeSchedule.getCreateDate());
        lastUpdateDateProperty().set(tblCalendarEmployeeSchedule.getLastUpdateDate());

    }

    public final LongProperty idrelationProperty() {
        return this.idrelation;
    }

    public long getIdrelation() {
        return idrelationProperty().get();
    }

    public void setIdrelation(long idrelation) {
        idrelationProperty().set(idrelation);
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

    public final ObjectProperty<SysCalendar> sysCalendarProperty() {
        return this.sysCalendar;
    }

    public SysCalendar getSysCalendar() {
        return sysCalendarProperty().get();
    }

    public void setSysCalendar(SysCalendar sysCalendar) {
        sysCalendarProperty().set(sysCalendar);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateByProperty() {
        return this.tblEmployeeByLastUpdateBy;
    }

    public TblEmployee getTblEmployeeByLastUpdateBy() {
        return tblEmployeeByLastUpdateByProperty().get();
    }

    public void setTblEmployeeByLastUpdateBy(TblEmployee tblEmployeeByLastUpdateBy) {
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
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

    public final ObjectProperty<TblEmployeeSchedule> tblEmployeeScheduleProperty() {
        return this.tblEmployeeSchedule;
    }

    public TblEmployeeSchedule getTblEmployeeSchedule() {
        return tblEmployeeScheduleProperty().get();
    }

    public void setTblEmployeeSchedule(TblEmployeeSchedule tblEmployeeSchedule) {
        tblEmployeeScheduleProperty().set(tblEmployeeSchedule);
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

}
