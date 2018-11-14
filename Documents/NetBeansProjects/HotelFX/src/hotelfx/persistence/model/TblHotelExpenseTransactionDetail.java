package hotelfx.persistence.model;
// Generated Apr 19, 2018 10:44:34 AM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblHotelExpenseTransactionDetail generated by hbm2java
 */
public class TblHotelExpenseTransactionDetail implements java.io.Serializable {

    private final LongProperty iddetail;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblHotelExpenseTransaction> tblHotelExpenseTransaction;
    private final StringProperty itemName;
    private final ObjectProperty<BigDecimal> itemQuantity;
    private final ObjectProperty<BigDecimal> itemCost;
    private final StringProperty detailNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblHotelExpenseTransactionDetail() {
        this.iddetail = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblHotelExpenseTransaction = new SimpleObjectProperty<>();
        this.itemName = new SimpleStringProperty();
        this.itemQuantity = new SimpleObjectProperty<>();
        this.itemCost = new SimpleObjectProperty<>();
        this.detailNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblHotelExpenseTransactionDetail(long iddetail) {
        this();
        iddetailProperty().set(iddetail);
    }

    public TblHotelExpenseTransactionDetail(long iddetail, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblHotelExpenseTransaction tblHotelExpenseTransaction, String itemName, BigDecimal itemQuantity, BigDecimal itemCost, String detailNote, Date createDate, Date lastUpdateDate) {
        this();
        iddetailProperty().set(iddetail);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblHotelExpenseTransactionProperty().set(tblHotelExpenseTransaction);
        itemNameProperty().set(itemName);
        itemQuantityProperty().set(itemQuantity);
        itemCostProperty().set(itemCost);
        detailNoteProperty().set(detailNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblHotelExpenseTransactionDetail(TblHotelExpenseTransactionDetail tblHotelExpenseTransactionDetail) {
        this();
        iddetailProperty().set(tblHotelExpenseTransactionDetail.getIddetail());
        refRecordStatusProperty().set(tblHotelExpenseTransactionDetail.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblHotelExpenseTransactionDetail.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblHotelExpenseTransactionDetail.getTblEmployeeByCreateBy());
        tblHotelExpenseTransactionProperty().set(tblHotelExpenseTransactionDetail.getTblHotelExpenseTransaction());
        itemNameProperty().set(tblHotelExpenseTransactionDetail.getItemName());
        itemQuantityProperty().set(tblHotelExpenseTransactionDetail.getItemQuantity());
        itemCostProperty().set(tblHotelExpenseTransactionDetail.getItemCost());
        detailNoteProperty().set(tblHotelExpenseTransactionDetail.getDetailNote());
        createDateProperty().set(tblHotelExpenseTransactionDetail.getCreateDate());
        lastUpdateDateProperty().set(tblHotelExpenseTransactionDetail.getLastUpdateDate());

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

    public final ObjectProperty<TblHotelExpenseTransaction> tblHotelExpenseTransactionProperty() {
        return this.tblHotelExpenseTransaction;
    }

    public TblHotelExpenseTransaction getTblHotelExpenseTransaction() {
        return tblHotelExpenseTransactionProperty().get();
    }

    public void setTblHotelExpenseTransaction(TblHotelExpenseTransaction tblHotelExpenseTransaction) {
        tblHotelExpenseTransactionProperty().set(tblHotelExpenseTransaction);
    }

    public final StringProperty itemNameProperty() {
        return this.itemName;
    }

    public String getItemName() {
        return itemNameProperty().get();
    }

    public void setItemName(String itemName) {
        itemNameProperty().set(itemName);
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

    public final ObjectProperty<BigDecimal> itemCostProperty() {
        return this.itemCost;
    }

    public BigDecimal getItemCost() {
        return itemCostProperty().get();
    }

    public void setItemCost(BigDecimal itemCost) {
        itemCostProperty().set(itemCost);
    }

    public final StringProperty detailNoteProperty() {
        return this.detailNote;
    }

    public String getDetailNote() {
        return detailNoteProperty().get();
    }

    public void setDetailNote(String detailNote) {
        detailNoteProperty().set(detailNote);
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
