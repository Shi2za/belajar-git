package hotelfx.persistence.model;
// Generated Sep 26, 2018 4:51:58 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
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
 * TblStoreRequestDetail generated by hbm2java
 */
public class TblStoreRequestDetail implements java.io.Serializable {

    private final LongProperty iddetail;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblItem> tblItem;
    private final ObjectProperty<TblStoreRequest> tblStoreRequest;
    private final ObjectProperty<BigDecimal> itemQuantity;
    private final StringProperty itemNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblStoreRequestDetailItemMutationHistories;

    public TblStoreRequestDetail() {
        this.iddetail = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblItem = new SimpleObjectProperty<>();
        this.tblStoreRequest = new SimpleObjectProperty<>();
        this.itemQuantity = new SimpleObjectProperty<>();
        this.itemNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblStoreRequestDetailItemMutationHistories = new HashSet(0);
    }

    public TblStoreRequestDetail(long iddetail) {
        this();
        iddetailProperty().set(iddetail);
    }

    public TblStoreRequestDetail(long iddetail, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblItem tblItem, TblStoreRequest tblStoreRequest, BigDecimal itemQuantity, String itemNote, Date createDate, Date lastUpdateDate, Set tblStoreRequestDetailItemMutationHistories) {
        this();
        iddetailProperty().set(iddetail);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblItemProperty().set(tblItem);
        tblStoreRequestProperty().set(tblStoreRequest);
        itemQuantityProperty().set(itemQuantity);
        itemNoteProperty().set(itemNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblStoreRequestDetailItemMutationHistories = tblStoreRequestDetailItemMutationHistories;
    }

    public TblStoreRequestDetail(TblStoreRequestDetail tblStoreRequestDetail) {
        this();
        iddetailProperty().set(tblStoreRequestDetail.getIddetail());
        refRecordStatusProperty().set(tblStoreRequestDetail.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblStoreRequestDetail.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblStoreRequestDetail.getTblEmployeeByLastUpdateBy());
        tblItemProperty().set(tblStoreRequestDetail.getTblItem());
        tblStoreRequestProperty().set(tblStoreRequestDetail.getTblStoreRequest());
        itemQuantityProperty().set(tblStoreRequestDetail.getItemQuantity());
        itemNoteProperty().set(tblStoreRequestDetail.getItemNote());
        createDateProperty().set(tblStoreRequestDetail.getCreateDate());
        lastUpdateDateProperty().set(tblStoreRequestDetail.getLastUpdateDate());

        this.tblStoreRequestDetailItemMutationHistories = tblStoreRequestDetail.getTblStoreRequestDetailItemMutationHistories();
    }

    public final LongProperty iddetailProperty() {
        return this.iddetail;
    }

    public long getIddetail() {
        return iddetailProperty().get();
    }

    public void setIddetail(long iddetail) {
        iddetailProperty().set(iddetail);
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

    public final ObjectProperty<TblItem> tblItemProperty() {
        return this.tblItem;
    }

    public TblItem getTblItem() {
        return tblItemProperty().get();
    }

    public void setTblItem(TblItem tblItem) {
        tblItemProperty().set(tblItem);
    }

    public final ObjectProperty<TblStoreRequest> tblStoreRequestProperty() {
        return this.tblStoreRequest;
    }

    public TblStoreRequest getTblStoreRequest() {
        return tblStoreRequestProperty().get();
    }

    public void setTblStoreRequest(TblStoreRequest tblStoreRequest) {
        tblStoreRequestProperty().set(tblStoreRequest);
    }

    public final ObjectProperty<BigDecimal> itemQuantityProperty() {
        return this.itemQuantity;
    }

    public BigDecimal getItemQuantity() {
        return itemQuantityProperty().get();
    }

    public void setItemQuantity(BigDecimal itemQuantity) {
        itemQuantityProperty().set(itemQuantity);
    }

    public final StringProperty itemNoteProperty() {
        return this.itemNote;
    }

    public String getItemNote() {
        return itemNoteProperty().get();
    }

    public void setItemNote(String itemNote) {
        itemNoteProperty().set(itemNote);
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

    public Set getTblStoreRequestDetailItemMutationHistories() {
        return this.tblStoreRequestDetailItemMutationHistories;
    }

    public void setTblStoreRequestDetailItemMutationHistories(Set tblStoreRequestDetailItemMutationHistories) {
        this.tblStoreRequestDetailItemMutationHistories = tblStoreRequestDetailItemMutationHistories;
    }

}