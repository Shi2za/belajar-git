package hotelfx.persistence.model;
// Generated Sep 26, 2018 4:51:58 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TblStoreRequestDetailItemMutationHistory generated by hbm2java
 */
public class TblStoreRequestDetailItemMutationHistory implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblItemMutationHistory> tblItemMutationHistory;
    private final ObjectProperty<TblStoreRequestDetail> tblStoreRequestDetail;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblStoreRequestDetailItemMutationHistory() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblItemMutationHistory = new SimpleObjectProperty<>();
        this.tblStoreRequestDetail = new SimpleObjectProperty<>();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblStoreRequestDetailItemMutationHistory(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblStoreRequestDetailItemMutationHistory(long idrelation, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblItemMutationHistory tblItemMutationHistory, TblStoreRequestDetail tblStoreRequestDetail, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblItemMutationHistoryProperty().set(tblItemMutationHistory);
        tblStoreRequestDetailProperty().set(tblStoreRequestDetail);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblStoreRequestDetailItemMutationHistory(TblStoreRequestDetailItemMutationHistory tblStoreRequestDetailItemMutationHistory) {
        this();
        idrelationProperty().set(tblStoreRequestDetailItemMutationHistory.getIdrelation());
        refRecordStatusProperty().set(tblStoreRequestDetailItemMutationHistory.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblStoreRequestDetailItemMutationHistory.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblStoreRequestDetailItemMutationHistory.getTblEmployeeByCreateBy());
        tblItemMutationHistoryProperty().set(tblStoreRequestDetailItemMutationHistory.getTblItemMutationHistory());
        tblStoreRequestDetailProperty().set(tblStoreRequestDetailItemMutationHistory.getTblStoreRequestDetail());
        createDateProperty().set(tblStoreRequestDetailItemMutationHistory.getCreateDate());
        lastUpdateDateProperty().set(tblStoreRequestDetailItemMutationHistory.getLastUpdateDate());

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

    public final ObjectProperty<TblItemMutationHistory> tblItemMutationHistoryProperty() {
        return this.tblItemMutationHistory;
    }

    public TblItemMutationHistory getTblItemMutationHistory() {
        return tblItemMutationHistoryProperty().get();
    }

    public void setTblItemMutationHistory(TblItemMutationHistory tblItemMutationHistory) {
        tblItemMutationHistoryProperty().set(tblItemMutationHistory);
    }

    public final ObjectProperty<TblStoreRequestDetail> tblStoreRequestDetailProperty() {
        return this.tblStoreRequestDetail;
    }

    public TblStoreRequestDetail getTblStoreRequestDetail() {
        return tblStoreRequestDetailProperty().get();
    }

    public void setTblStoreRequestDetail(TblStoreRequestDetail tblStoreRequestDetail) {
        tblStoreRequestDetailProperty().set(tblStoreRequestDetail);
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
