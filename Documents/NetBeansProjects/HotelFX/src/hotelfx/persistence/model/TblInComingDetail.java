package hotelfx.persistence.model;
// Generated Oct 10, 2018 2:47:45 PM by Hibernate Tools 4.3.1

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
 * TblInComingDetail generated by hbm2java
 */
public class TblInComingDetail implements java.io.Serializable {

    private final LongProperty iddetail;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblInComing> tblInComing;
    private final ObjectProperty<TblItem> tblItem;
    private final ObjectProperty<BigDecimal> itemQuantity;
    private final StringProperty itemNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblInComingDetailItemMutationHistories;

    public TblInComingDetail() {
        this.iddetail = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblInComing = new SimpleObjectProperty<>();
        this.tblItem = new SimpleObjectProperty<>();
        this.itemQuantity = new SimpleObjectProperty<>();
        this.itemNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblInComingDetailItemMutationHistories = new HashSet(0);
    }

    public TblInComingDetail(long iddetail) {
        this();
        iddetailProperty().set(iddetail);
    }

    public TblInComingDetail(long iddetail, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblInComing tblInComing, TblItem tblItem, BigDecimal itemQuantity, String itemNote, Date createDate, Date lastUpdateDate, Set tblInComingDetailItemMutationHistories) {
        this();
        iddetailProperty().set(iddetail);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblInComingProperty().set(tblInComing);
        tblItemProperty().set(tblItem);
        itemQuantityProperty().set(itemQuantity);
        itemNoteProperty().set(itemNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblInComingDetailItemMutationHistories = tblInComingDetailItemMutationHistories;
    }

    public TblInComingDetail(TblInComingDetail tblInComingDetail) {
        this();
        iddetailProperty().set(tblInComingDetail.getIddetail());
        refRecordStatusProperty().set(tblInComingDetail.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblInComingDetail.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblInComingDetail.getTblEmployeeByLastUpdateBy());
        tblInComingProperty().set(tblInComingDetail.getTblInComing());
        tblItemProperty().set(tblInComingDetail.getTblItem());
        itemQuantityProperty().set(tblInComingDetail.getItemQuantity());
        itemNoteProperty().set(tblInComingDetail.getItemNote());
        createDateProperty().set(tblInComingDetail.getCreateDate());
        lastUpdateDateProperty().set(tblInComingDetail.getLastUpdateDate());

        this.tblInComingDetailItemMutationHistories = tblInComingDetail.getTblInComingDetailItemMutationHistories();
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

    public final ObjectProperty<TblInComing> tblInComingProperty() {
        return this.tblInComing;
    }

    public TblInComing getTblInComing() {
        return tblInComingProperty().get();
    }

    public void setTblInComing(TblInComing tblInComing) {
        tblInComingProperty().set(tblInComing);
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

    public Set getTblInComingDetailItemMutationHistories() {
        return this.tblInComingDetailItemMutationHistories;
    }

    public void setTblInComingDetailItemMutationHistories(Set tblInComingDetailItemMutationHistories) {
        this.tblInComingDetailItemMutationHistories = tblInComingDetailItemMutationHistories;
    }

}