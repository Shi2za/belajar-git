package hotelfx.persistence.model;
// Generated Jan 22, 2018 2:34:45 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory generated by
 * hbm2java
 */
public class TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory implements java.io.Serializable {

    private final LongProperty idhistory;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew;
    private final ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld;
    private final StringProperty historyNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory() {
        this.idhistory = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew = new SimpleObjectProperty<>();
        this.tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld = new SimpleObjectProperty<>();
        this.historyNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory(long idhistory) {
        this();
        idhistoryProperty().set(idhistory);
    }

    public TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory(long idhistory, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblReservationRoomTypeDetailRoomPriceDetail tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew, TblReservationRoomTypeDetailRoomPriceDetail tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld, String historyNote, Date createDate, Date lastUpdateDate) {
        this();
        idhistoryProperty().set(idhistory);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNewProperty().set(tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew);
        tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOldProperty().set(tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld);
        historyNoteProperty().set(historyNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory(TblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory) {
        this();
        idhistoryProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getIdhistory());
        refRecordStatusProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblEmployeeByLastUpdateBy());
        tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNewProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew());
        tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOldProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld());
        historyNoteProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getHistoryNote());
        createDateProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getCreateDate());
        lastUpdateDateProperty().set(tblReservationRoomTypeDetailRoomPriceDetailChangeRoomHistory.getLastUpdateDate());

    }

    public final LongProperty idhistoryProperty() {
        return this.idhistory;
    }

    public long getIdhistory() {
        return idhistoryProperty().get();
    }

    public void setIdhistory(long idhistory) {
        idhistoryProperty().set(idhistory);
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

    public final ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNewProperty() {
        return this.tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew;
    }

    public TblReservationRoomTypeDetailRoomPriceDetail getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew() {
        return tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNewProperty().get();
    }

    public void setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew(TblReservationRoomTypeDetailRoomPriceDetail tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew) {
        tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNewProperty().set(tblReservationRoomTypeDetailRoomPriceDetailByIdrelationNew);
    }

    public final ObjectProperty<TblReservationRoomTypeDetailRoomPriceDetail> tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOldProperty() {
        return this.tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld;
    }

    public TblReservationRoomTypeDetailRoomPriceDetail getTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld() {
        return tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOldProperty().get();
    }

    public void setTblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld(TblReservationRoomTypeDetailRoomPriceDetail tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld) {
        tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOldProperty().set(tblReservationRoomTypeDetailRoomPriceDetailByIdrelationOld);
    }

    public final StringProperty historyNoteProperty() {
        return this.historyNote;
    }

    public String getHistoryNote() {
        return historyNoteProperty().get();
    }

    public void setHistoryNote(String historyNote) {
        historyNoteProperty().set(historyNote);
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
