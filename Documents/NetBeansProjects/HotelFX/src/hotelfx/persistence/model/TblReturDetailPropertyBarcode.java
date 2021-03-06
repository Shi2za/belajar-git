package hotelfx.persistence.model;
// Generated Nov 9, 2017 9:09:02 AM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TblReturDetailPropertyBarcode generated by hbm2java
 */
public class TblReturDetailPropertyBarcode implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblPropertyBarcode> tblPropertyBarcode;
    private final ObjectProperty<TblReturDetail> tblReturDetail;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblReturDetailPropertyBarcode() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblPropertyBarcode = new SimpleObjectProperty<>();
        this.tblReturDetail = new SimpleObjectProperty<>();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblReturDetailPropertyBarcode(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblReturDetailPropertyBarcode(long idrelation, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblPropertyBarcode tblPropertyBarcode, TblReturDetail tblReturDetail, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblPropertyBarcodeProperty().set(tblPropertyBarcode);
        tblReturDetailProperty().set(tblReturDetail);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblReturDetailPropertyBarcode(TblReturDetailPropertyBarcode tblReturDetailPropertyBarcode) {
        this();
        idrelationProperty().set(tblReturDetailPropertyBarcode.getIdrelation());
        refRecordStatusProperty().set(tblReturDetailPropertyBarcode.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblReturDetailPropertyBarcode.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblReturDetailPropertyBarcode.getTblEmployeeByCreateBy());
        tblPropertyBarcodeProperty().set(tblReturDetailPropertyBarcode.getTblPropertyBarcode());
        tblReturDetailProperty().set(tblReturDetailPropertyBarcode.getTblReturDetail());
        createDateProperty().set(tblReturDetailPropertyBarcode.getCreateDate());
        lastUpdateDateProperty().set(tblReturDetailPropertyBarcode.getLastUpdateDate());

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

    public final ObjectProperty<TblPropertyBarcode> tblPropertyBarcodeProperty() {
        return this.tblPropertyBarcode;
    }

    public TblPropertyBarcode getTblPropertyBarcode() {
        return tblPropertyBarcodeProperty().get();
    }

    public void setTblPropertyBarcode(TblPropertyBarcode tblPropertyBarcode) {
        tblPropertyBarcodeProperty().set(tblPropertyBarcode);
    }

    public final ObjectProperty<TblReturDetail> tblReturDetailProperty() {
        return this.tblReturDetail;
    }

    public TblReturDetail getTblReturDetail() {
        return tblReturDetailProperty().get();
    }

    public void setTblReturDetail(TblReturDetail tblReturDetail) {
        tblReturDetailProperty().set(tblReturDetail);
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
