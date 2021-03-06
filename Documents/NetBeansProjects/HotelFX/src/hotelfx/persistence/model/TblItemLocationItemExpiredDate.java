package hotelfx.persistence.model;
// Generated Sep 21, 2018 1:57:14 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblItemLocationItemExpiredDate generated by hbm2java
 */
public class TblItemLocationItemExpiredDate implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblItemLocation> tblItemLocation;
    private final ObjectProperty<TblItemExpiredDate> tblItemExpiredDate;
    private final ObjectProperty<BigDecimal> itemQuantity;
    private final StringProperty note;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblItemLocationItemExpiredDate() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblItemLocation = new SimpleObjectProperty<>();
        this.tblItemExpiredDate = new SimpleObjectProperty<>();
        this.itemQuantity = new SimpleObjectProperty<>();
        this.note = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblItemLocationItemExpiredDate(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblItemLocationItemExpiredDate(long idrelation, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblItemLocation tblItemLocation, TblItemExpiredDate tblItemExpiredDate, BigDecimal itemQuantity, String note, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblItemLocationProperty().set(tblItemLocation);
        tblItemExpiredDateProperty().set(tblItemExpiredDate);
        itemQuantityProperty().set(itemQuantity);
        noteProperty().set(note);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblItemLocationItemExpiredDate(TblItemLocationItemExpiredDate tblItemLocationItemExpiredDate) {
        this();
        idrelationProperty().set(tblItemLocationItemExpiredDate.getIdrelation());
        refRecordStatusProperty().set(tblItemLocationItemExpiredDate.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblItemLocationItemExpiredDate.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblItemLocationItemExpiredDate.getTblEmployeeByLastUpdateBy());
        tblItemLocationProperty().set(tblItemLocationItemExpiredDate.getTblItemLocation());
        tblItemExpiredDateProperty().set(tblItemLocationItemExpiredDate.getTblItemExpiredDate());
        itemQuantityProperty().set(tblItemLocationItemExpiredDate.getItemQuantity());
        noteProperty().set(tblItemLocationItemExpiredDate.getNote());
        createDateProperty().set(tblItemLocationItemExpiredDate.getCreateDate());
        lastUpdateDateProperty().set(tblItemLocationItemExpiredDate.getLastUpdateDate());

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

    public final ObjectProperty<TblItemLocation> tblItemLocationProperty() {
        return this.tblItemLocation;
    }

    public TblItemLocation getTblItemLocation() {
        return tblItemLocationProperty().get();
    }

    public void setTblItemLocation(TblItemLocation tblItemLocation) {
        tblItemLocationProperty().set(tblItemLocation);
    }

    public final ObjectProperty<TblItemExpiredDate> tblItemExpiredDateProperty() {
        return this.tblItemExpiredDate;
    }

    public TblItemExpiredDate getTblItemExpiredDate() {
        return tblItemExpiredDateProperty().get();
    }

    public void setTblItemExpiredDate(TblItemExpiredDate tblItemExpiredDate) {
        tblItemExpiredDateProperty().set(tblItemExpiredDate);
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

    public final StringProperty noteProperty() {
        return this.note;
    }

    public String getNote() {
        return noteProperty().get();
    }

    public void setNote(String note) {
        noteProperty().set(note);
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
