package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblEmployeeContract generated by hbm2java
 */
public class TblEmployeeContract implements java.io.Serializable {

    private final LongProperty idcontract;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdemployee;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdpersonInCharge;
    private final StringProperty codeContract;
    private final ObjectProperty<Date> dateCreated;
    private final StringProperty placeCreated;
    private final ObjectProperty<Date> beginValidity;
    private final ObjectProperty<Date> endValidity;
    private final StringProperty contractDetail;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblEmployeeContract() {
        this.idcontract = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByIdemployee = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByIdpersonInCharge = new SimpleObjectProperty<>();
        this.codeContract = new SimpleStringProperty();
        this.dateCreated = new SimpleObjectProperty<>();
        this.placeCreated = new SimpleStringProperty();
        this.beginValidity = new SimpleObjectProperty<>();
        this.endValidity = new SimpleObjectProperty<>();
        this.contractDetail = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblEmployeeContract(long idcontract) {
        this();
        idcontractProperty().set(idcontract);
    }

    public TblEmployeeContract(long idcontract, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByIdemployee, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByIdpersonInCharge, String codeContract, Date dateCreated, String placeCreated, Date beginValidity, Date endValidity, String contractDetail, Date createDate, Date lastUpdateDate) {
        this();
        idcontractProperty().set(idcontract);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByIdpersonInChargeProperty().set(tblEmployeeByIdpersonInCharge);
        codeContractProperty().set(codeContract);
        dateCreatedProperty().set(dateCreated);
        placeCreatedProperty().set(placeCreated);
        beginValidityProperty().set(beginValidity);
        endValidityProperty().set(endValidity);
        contractDetailProperty().set(contractDetail);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblEmployeeContract(TblEmployeeContract tblEmployeeContract) {
        this();
        idcontractProperty().set(tblEmployeeContract.getIdcontract());
        refRecordStatusProperty().set(tblEmployeeContract.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeContract.getTblEmployeeByLastUpdateBy());
        tblEmployeeByIdemployeeProperty().set(tblEmployeeContract.getTblEmployeeByIdemployee());
        tblEmployeeByCreateByProperty().set(tblEmployeeContract.getTblEmployeeByCreateBy());
        tblEmployeeByIdpersonInChargeProperty().set(tblEmployeeContract.getTblEmployeeByIdpersonInCharge());
        codeContractProperty().set(tblEmployeeContract.getCodeContract());
        dateCreatedProperty().set(tblEmployeeContract.getDateCreated());
        placeCreatedProperty().set(tblEmployeeContract.getPlaceCreated());
        beginValidityProperty().set(tblEmployeeContract.getBeginValidity());
        endValidityProperty().set(tblEmployeeContract.getEndValidity());
        contractDetailProperty().set(tblEmployeeContract.getContractDetail());
        createDateProperty().set(tblEmployeeContract.getCreateDate());
        lastUpdateDateProperty().set(tblEmployeeContract.getLastUpdateDate());

    }

    public final LongProperty idcontractProperty() {
        return this.idcontract;
    }

    public long getIdcontract() {
        return idcontractProperty().get();
    }

    public void setIdcontract(long idcontract) {
        idcontractProperty().set(idcontract);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByIdemployeeProperty() {
        return this.tblEmployeeByIdemployee;
    }

    public TblEmployee getTblEmployeeByIdemployee() {
        return tblEmployeeByIdemployeeProperty().get();
    }

    public void setTblEmployeeByIdemployee(TblEmployee tblEmployeeByIdemployee) {
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByIdpersonInChargeProperty() {
        return this.tblEmployeeByIdpersonInCharge;
    }

    public TblEmployee getTblEmployeeByIdpersonInCharge() {
        return tblEmployeeByIdpersonInChargeProperty().get();
    }

    public void setTblEmployeeByIdpersonInCharge(TblEmployee tblEmployeeByIdpersonInCharge) {
        tblEmployeeByIdpersonInChargeProperty().set(tblEmployeeByIdpersonInCharge);
    }

    public final StringProperty codeContractProperty() {
        return this.codeContract;
    }

    public String getCodeContract() {
        return codeContractProperty().get();
    }

    public void setCodeContract(String codeContract) {
        codeContractProperty().set(codeContract);
    }

    public final ObjectProperty<Date> dateCreatedProperty() {
        return this.dateCreated;
    }

    public Date getDateCreated() {
        return dateCreatedProperty().get();
    }

    public void setDateCreated(Date dateCreated) {
        dateCreatedProperty().set(dateCreated);
    }

    public final StringProperty placeCreatedProperty() {
        return this.placeCreated;
    }

    public String getPlaceCreated() {
        return placeCreatedProperty().get();
    }

    public void setPlaceCreated(String placeCreated) {
        placeCreatedProperty().set(placeCreated);
    }

    public final ObjectProperty<Date> beginValidityProperty() {
        return this.beginValidity;
    }

    public Date getBeginValidity() {
        return beginValidityProperty().get();
    }

    public void setBeginValidity(Date beginValidity) {
        beginValidityProperty().set(beginValidity);
    }

    public final ObjectProperty<Date> endValidityProperty() {
        return this.endValidity;
    }

    public Date getEndValidity() {
        return endValidityProperty().get();
    }

    public void setEndValidity(Date endValidity) {
        endValidityProperty().set(endValidity);
    }

    public final StringProperty contractDetailProperty() {
        return this.contractDetail;
    }

    public String getContractDetail() {
        return contractDetailProperty().get();
    }

    public void setContractDetail(String contractDetail) {
        contractDetailProperty().set(contractDetail);
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
