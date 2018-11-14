package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

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
 * TblEmployeePayroll generated by hbm2java
 */
public class TblEmployeePayroll implements java.io.Serializable {

    private final LongProperty idpayroll;
    private final ObjectProperty<RefEmployeePayrollType> refEmployeePayrollType;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblFinanceData> tblFinanceData;
    private final StringProperty codePayroll;
    private final ObjectProperty<Date> payrollMonth;
    private final ObjectProperty<Date> beginPeriode;
    private final ObjectProperty<Date> endPeriode;
    private final LongProperty roundValue;
    private final ObjectProperty<Date> payrollDateCreated;
    private final StringProperty payrollNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblEmployeeEmployeePayrolls;
    private Set tblEmployeeResigns;

    public TblEmployeePayroll() {
        this.idpayroll = new SimpleLongProperty();
        this.refEmployeePayrollType = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblFinanceData = new SimpleObjectProperty<>();
        this.codePayroll = new SimpleStringProperty();
        this.payrollMonth = new SimpleObjectProperty<>();
        this.beginPeriode = new SimpleObjectProperty<>();
        this.endPeriode = new SimpleObjectProperty<>();
        this.roundValue = new SimpleLongProperty();
        this.payrollDateCreated = new SimpleObjectProperty<>();
        this.payrollNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblEmployeeEmployeePayrolls = new HashSet(0);
        this.tblEmployeeResigns = new HashSet(0);
    }

    public TblEmployeePayroll(long idpayroll) {
        this();
        idpayrollProperty().set(idpayroll);
    }

    public TblEmployeePayroll(long idpayroll, RefEmployeePayrollType refEmployeePayrollType, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblFinanceData tblFinanceData, String codePayroll, Date payrollMonth, Date beginPeriode, Date endPeriode, Long roundValue, Date payrollDateCreated, String payrollNote, Date createDate, Date lastUpdateDate, Set tblEmployeeEmployeePayrolls, Set tblEmployeeResigns) {
        this();
        idpayrollProperty().set(idpayroll);
        refEmployeePayrollTypeProperty().set(refEmployeePayrollType);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblFinanceDataProperty().set(tblFinanceData);
        codePayrollProperty().set(codePayroll);
        payrollMonthProperty().set(payrollMonth);
        beginPeriodeProperty().set(beginPeriode);
        endPeriodeProperty().set(endPeriode);
        roundValueProperty().set(roundValue);
        payrollDateCreatedProperty().set(payrollDateCreated);
        payrollNoteProperty().set(payrollNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblEmployeeEmployeePayrolls = tblEmployeeEmployeePayrolls;
        this.tblEmployeeResigns = tblEmployeeResigns;
    }

    public TblEmployeePayroll(TblEmployeePayroll tblEmployeePayroll) {
        this();
        idpayrollProperty().set(tblEmployeePayroll.getIdpayroll());
        refEmployeePayrollTypeProperty().set(tblEmployeePayroll.getRefEmployeePayrollType());
        refRecordStatusProperty().set(tblEmployeePayroll.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblEmployeePayroll.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblEmployeePayroll.getTblEmployeeByLastUpdateBy());
        tblFinanceDataProperty().set(tblEmployeePayroll.getTblFinanceData());
        codePayrollProperty().set(tblEmployeePayroll.getCodePayroll());
        payrollMonthProperty().set(tblEmployeePayroll.getPayrollMonth());
        beginPeriodeProperty().set(tblEmployeePayroll.getBeginPeriode());
        endPeriodeProperty().set(tblEmployeePayroll.getEndPeriode());
        roundValueProperty().set(tblEmployeePayroll.getRoundValue());
        payrollDateCreatedProperty().set(tblEmployeePayroll.getPayrollDateCreated());
        payrollNoteProperty().set(tblEmployeePayroll.getPayrollNote());
        createDateProperty().set(tblEmployeePayroll.getCreateDate());
        lastUpdateDateProperty().set(tblEmployeePayroll.getLastUpdateDate());

        this.tblEmployeeEmployeePayrolls = tblEmployeePayroll.getTblEmployeeEmployeePayrolls();
        this.tblEmployeeResigns = tblEmployeePayroll.getTblEmployeeResigns();
    }

    public final LongProperty idpayrollProperty() {
        return this.idpayroll;
    }

    public long getIdpayroll() {
        return idpayrollProperty().get();
    }

    public void setIdpayroll(long idpayroll) {
        idpayrollProperty().set(idpayroll);
    }

    public final ObjectProperty<RefEmployeePayrollType> refEmployeePayrollTypeProperty() {
        return this.refEmployeePayrollType;
    }

    public RefEmployeePayrollType getRefEmployeePayrollType() {
        return refEmployeePayrollTypeProperty().get();
    }

    public void setRefEmployeePayrollType(RefEmployeePayrollType refEmployeePayrollType) {
        refEmployeePayrollTypeProperty().set(refEmployeePayrollType);
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

    public final ObjectProperty<TblFinanceData> tblFinanceDataProperty() {
        return this.tblFinanceData;
    }

    public TblFinanceData getTblFinanceData() {
        return tblFinanceDataProperty().get();
    }

    public void setTblFinanceData(TblFinanceData tblFinanceData) {
        tblFinanceDataProperty().set(tblFinanceData);
    }

    public final StringProperty codePayrollProperty() {
        return this.codePayroll;
    }

    public String getCodePayroll() {
        return codePayrollProperty().get();
    }

    public void setCodePayroll(String codePayroll) {
        codePayrollProperty().set(codePayroll);
    }

    public final ObjectProperty<Date> payrollMonthProperty() {
        return this.payrollMonth;
    }

    public Date getPayrollMonth() {
        return payrollMonthProperty().get();
    }

    public void setPayrollMonth(Date payrollMonth) {
        payrollMonthProperty().set(payrollMonth);
    }

    public final ObjectProperty<Date> beginPeriodeProperty() {
        return this.beginPeriode;
    }

    public Date getBeginPeriode() {
        return beginPeriodeProperty().get();
    }

    public void setBeginPeriode(Date beginPeriode) {
        beginPeriodeProperty().set(beginPeriode);
    }

    public final ObjectProperty<Date> endPeriodeProperty() {
        return this.endPeriode;
    }

    public Date getEndPeriode() {
        return endPeriodeProperty().get();
    }

    public void setEndPeriode(Date endPeriode) {
        endPeriodeProperty().set(endPeriode);
    }

    public final LongProperty roundValueProperty() {
        return this.roundValue;
    }

    public Long getRoundValue() {
        return roundValueProperty().get();
    }

    public void setRoundValue(Long roundValue) {
        roundValueProperty().set(roundValue);
    }

    public final ObjectProperty<Date> payrollDateCreatedProperty() {
        return this.payrollDateCreated;
    }

    public Date getPayrollDateCreated() {
        return payrollDateCreatedProperty().get();
    }

    public void setPayrollDateCreated(Date payrollDateCreated) {
        payrollDateCreatedProperty().set(payrollDateCreated);
    }

    public final StringProperty payrollNoteProperty() {
        return this.payrollNote;
    }

    public String getPayrollNote() {
        return payrollNoteProperty().get();
    }

    public void setPayrollNote(String payrollNote) {
        payrollNoteProperty().set(payrollNote);
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

    public Set getTblEmployeeEmployeePayrolls() {
        return this.tblEmployeeEmployeePayrolls;
    }

    public void setTblEmployeeEmployeePayrolls(Set tblEmployeeEmployeePayrolls) {
        this.tblEmployeeEmployeePayrolls = tblEmployeeEmployeePayrolls;
    }

    public Set getTblEmployeeResigns() {
        return this.tblEmployeeResigns;
    }

    public void setTblEmployeeResigns(Set tblEmployeeResigns) {
        this.tblEmployeeResigns = tblEmployeeResigns;
    }

}
