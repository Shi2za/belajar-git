package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TblReservationDefaultBar generated by hbm2java
 */
public class TblReservationDefaultBar implements java.io.Serializable {

    private final LongProperty iddefaultBar;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblReservationBar> tblReservationBar;
    private final IntegerProperty dayOfWeek;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblReservationDefaultBar() {
        this.iddefaultBar = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblReservationBar = new SimpleObjectProperty<>();
        this.dayOfWeek = new SimpleIntegerProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblReservationDefaultBar(long iddefaultBar) {
        this();
        iddefaultBarProperty().set(iddefaultBar);
    }

    public TblReservationDefaultBar(long iddefaultBar, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblReservationBar tblReservationBar, Integer dayOfWeek, Date createDate, Date lastUpdateDate) {
        this();
        iddefaultBarProperty().set(iddefaultBar);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblReservationBarProperty().set(tblReservationBar);
        dayOfWeekProperty().set(dayOfWeek);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblReservationDefaultBar(TblReservationDefaultBar tblReservationDefaultBar) {
        this();
        iddefaultBarProperty().set(tblReservationDefaultBar.getIddefaultBar());
        refRecordStatusProperty().set(tblReservationDefaultBar.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblReservationDefaultBar.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblReservationDefaultBar.getTblEmployeeByCreateBy());
        tblReservationBarProperty().set(tblReservationDefaultBar.getTblReservationBar());
        dayOfWeekProperty().set(tblReservationDefaultBar.getDayOfWeek());
        createDateProperty().set(tblReservationDefaultBar.getCreateDate());
        lastUpdateDateProperty().set(tblReservationDefaultBar.getLastUpdateDate());

    }

    public final LongProperty iddefaultBarProperty() {
        return this.iddefaultBar;
    }

    public long getIddefaultBar() {
        return iddefaultBarProperty().get();
    }

    public void setIddefaultBar(long iddefaultBar) {
        iddefaultBarProperty().set(iddefaultBar);
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

    public final ObjectProperty<TblReservationBar> tblReservationBarProperty() {
        return this.tblReservationBar;
    }

    public TblReservationBar getTblReservationBar() {
        return tblReservationBarProperty().get();
    }

    public void setTblReservationBar(TblReservationBar tblReservationBar) {
        tblReservationBarProperty().set(tblReservationBar);
    }

    public final IntegerProperty dayOfWeekProperty() {
        return this.dayOfWeek;
    }

    public Integer getDayOfWeek() {
        return dayOfWeekProperty().get();
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        dayOfWeekProperty().set(dayOfWeek);
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
