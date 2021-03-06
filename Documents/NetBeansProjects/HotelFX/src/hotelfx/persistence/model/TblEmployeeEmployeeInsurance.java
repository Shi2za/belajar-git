package hotelfx.persistence.model;
// Generated Apr 27, 2018 3:35:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblEmployeeEmployeeInsurance generated by hbm2java
 */
public class TblEmployeeEmployeeInsurance implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdemployee;
    private final ObjectProperty<TblEmployeeInsurance> tblEmployeeInsurance;
    private final ObjectProperty<BigDecimal> companyPayment;
    private final ObjectProperty<BigDecimal> personalPayment;
    private final StringProperty insuranceNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblEmployeeEmployeeInsurance() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByIdemployee = new SimpleObjectProperty<>();
        this.tblEmployeeInsurance = new SimpleObjectProperty<>();
        this.companyPayment = new SimpleObjectProperty<>();
        this.personalPayment = new SimpleObjectProperty<>();
        this.insuranceNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblEmployeeEmployeeInsurance(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblEmployeeEmployeeInsurance(long idrelation, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByIdemployee, TblEmployeeInsurance tblEmployeeInsurance, BigDecimal companyPayment, BigDecimal personalPayment, String insuranceNote, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
        tblEmployeeInsuranceProperty().set(tblEmployeeInsurance);
        companyPaymentProperty().set(companyPayment);
        personalPaymentProperty().set(personalPayment);
        insuranceNoteProperty().set(insuranceNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblEmployeeEmployeeInsurance(TblEmployeeEmployeeInsurance tblEmployeeEmployeeInsurance) {
        this();
        idrelationProperty().set(tblEmployeeEmployeeInsurance.getIdrelation());
        refRecordStatusProperty().set(tblEmployeeEmployeeInsurance.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeEmployeeInsurance.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblEmployeeEmployeeInsurance.getTblEmployeeByCreateBy());
        tblEmployeeByIdemployeeProperty().set(tblEmployeeEmployeeInsurance.getTblEmployeeByIdemployee());
        tblEmployeeInsuranceProperty().set(tblEmployeeEmployeeInsurance.getTblEmployeeInsurance());
        companyPaymentProperty().set(tblEmployeeEmployeeInsurance.getCompanyPayment());
        personalPaymentProperty().set(tblEmployeeEmployeeInsurance.getPersonalPayment());
        insuranceNoteProperty().set(tblEmployeeEmployeeInsurance.getInsuranceNote());
        createDateProperty().set(tblEmployeeEmployeeInsurance.getCreateDate());
        lastUpdateDateProperty().set(tblEmployeeEmployeeInsurance.getLastUpdateDate());

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

    public final ObjectProperty<TblEmployee> tblEmployeeByIdemployeeProperty() {
        return this.tblEmployeeByIdemployee;
    }

    public TblEmployee getTblEmployeeByIdemployee() {
        return tblEmployeeByIdemployeeProperty().get();
    }

    public void setTblEmployeeByIdemployee(TblEmployee tblEmployeeByIdemployee) {
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
    }

    public final ObjectProperty<TblEmployeeInsurance> tblEmployeeInsuranceProperty() {
        return this.tblEmployeeInsurance;
    }

    public TblEmployeeInsurance getTblEmployeeInsurance() {
        return tblEmployeeInsuranceProperty().get();
    }

    public void setTblEmployeeInsurance(TblEmployeeInsurance tblEmployeeInsurance) {
        tblEmployeeInsuranceProperty().set(tblEmployeeInsurance);
    }

    public final ObjectProperty<BigDecimal> companyPaymentProperty() {
        return this.companyPayment;
    }

    public BigDecimal getCompanyPayment() {
        return companyPaymentProperty().get();
    }

    public void setCompanyPayment(BigDecimal companyPayment) {
        companyPaymentProperty().set(companyPayment);
    }

    public final ObjectProperty<BigDecimal> personalPaymentProperty() {
        return this.personalPayment;
    }

    public BigDecimal getPersonalPayment() {
        return personalPaymentProperty().get();
    }

    public void setPersonalPayment(BigDecimal personalPayment) {
        personalPaymentProperty().set(personalPayment);
    }

    public final StringProperty insuranceNoteProperty() {
        return this.insuranceNote;
    }

    public String getInsuranceNote() {
        return insuranceNoteProperty().get();
    }

    public void setInsuranceNote(String insuranceNote) {
        insuranceNoteProperty().set(insuranceNote);
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
