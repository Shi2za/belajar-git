package hotelfx.persistence.model;
// Generated Mar 7, 2018 10:53:22 AM by Hibernate Tools 4.3.1

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
 * TblFinanceData generated by hbm2java
 */
public class TblFinanceData implements java.io.Serializable {

    private final LongProperty iddata;
    private final ObjectProperty<RefFinanceDataType> refFinanceDataType;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final StringProperty codeData;
    private final StringProperty dataNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblPurchaseOrders;
    private Set tblReservationPayments;
    private Set tblEmployeeServiceCharges;
    private Set tblFinanceTransactions;
    private Set tblEmployeeResigns;
    private Set tblReturs;
    private Set tblEmployeeThrs;
    private Set tblEmployeeCutOffLeaves;
    private Set tblEmployeePayrolls;
    private Set tblMemorandumInvoices;

    public TblFinanceData() {
        this.iddata = new SimpleLongProperty();
        this.refFinanceDataType = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.codeData = new SimpleStringProperty();
        this.dataNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblPurchaseOrders = new HashSet(0);
        this.tblReservationPayments = new HashSet(0);
        this.tblEmployeeServiceCharges = new HashSet(0);
        this.tblFinanceTransactions = new HashSet(0);
        this.tblEmployeeResigns = new HashSet(0);
        this.tblReturs = new HashSet(0);
        this.tblEmployeeThrs = new HashSet(0);
        this.tblEmployeeCutOffLeaves = new HashSet(0);
        this.tblEmployeePayrolls = new HashSet(0);
        this.tblMemorandumInvoices = new HashSet(0);
    }

    public TblFinanceData(long iddata) {
        this();
        iddataProperty().set(iddata);
    }

    public TblFinanceData(long iddata, RefFinanceDataType refFinanceDataType, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, String codeData, String dataNote, Date createDate, Date lastUpdateDate, Set tblPurchaseOrders, Set tblReservationPayments, Set tblEmployeeServiceCharges, Set tblFinanceTransactions, Set tblEmployeeResigns, Set tblReturs, Set tblEmployeeThrs, Set tblEmployeeCutOffLeaves, Set tblEmployeePayrolls, Set tblMemorandumInvoices) {
        this();
        iddataProperty().set(iddata);
        refFinanceDataTypeProperty().set(refFinanceDataType);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        codeDataProperty().set(codeData);
        dataNoteProperty().set(dataNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblPurchaseOrders = tblPurchaseOrders;
        this.tblReservationPayments = tblReservationPayments;
        this.tblEmployeeServiceCharges = tblEmployeeServiceCharges;
        this.tblFinanceTransactions = tblFinanceTransactions;
        this.tblEmployeeResigns = tblEmployeeResigns;
        this.tblReturs = tblReturs;
        this.tblEmployeeThrs = tblEmployeeThrs;
        this.tblEmployeeCutOffLeaves = tblEmployeeCutOffLeaves;
        this.tblEmployeePayrolls = tblEmployeePayrolls;
        this.tblMemorandumInvoices = tblMemorandumInvoices;
    }

    public TblFinanceData(TblFinanceData tblFinanceData) {
        this();
        iddataProperty().set(tblFinanceData.getIddata());
        refFinanceDataTypeProperty().set(tblFinanceData.getRefFinanceDataType());
        refRecordStatusProperty().set(tblFinanceData.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblFinanceData.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblFinanceData.getTblEmployeeByCreateBy());
        codeDataProperty().set(tblFinanceData.getCodeData());
        dataNoteProperty().set(tblFinanceData.getDataNote());
        createDateProperty().set(tblFinanceData.getCreateDate());
        lastUpdateDateProperty().set(tblFinanceData.getLastUpdateDate());

        this.tblPurchaseOrders = tblFinanceData.getTblPurchaseOrders();
        this.tblReservationPayments = tblFinanceData.getTblReservationPayments();
        this.tblEmployeeServiceCharges = tblFinanceData.getTblEmployeeServiceCharges();
        this.tblFinanceTransactions = tblFinanceData.getTblFinanceTransactions();
        this.tblEmployeeResigns = tblFinanceData.getTblEmployeeResigns();
        this.tblReturs = tblFinanceData.getTblReturs();
        this.tblEmployeeThrs = tblFinanceData.getTblEmployeeThrs();
        this.tblEmployeeCutOffLeaves = tblFinanceData.getTblEmployeeCutOffLeaves();
        this.tblEmployeePayrolls = tblFinanceData.getTblEmployeePayrolls();
        this.tblMemorandumInvoices = tblFinanceData.getTblMemorandumInvoices();
    }

    public final LongProperty iddataProperty() {
        return this.iddata;
    }

    public long getIddata() {
        return iddataProperty().get();
    }

    public void setIddata(long iddata) {
        iddataProperty().set(iddata);
    }

    public final ObjectProperty<RefFinanceDataType> refFinanceDataTypeProperty() {
        return this.refFinanceDataType;
    }

    public RefFinanceDataType getRefFinanceDataType() {
        return refFinanceDataTypeProperty().get();
    }

    public void setRefFinanceDataType(RefFinanceDataType refFinanceDataType) {
        refFinanceDataTypeProperty().set(refFinanceDataType);
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

    public final StringProperty codeDataProperty() {
        return this.codeData;
    }

    public String getCodeData() {
        return codeDataProperty().get();
    }

    public void setCodeData(String codeData) {
        codeDataProperty().set(codeData);
    }

    public final StringProperty dataNoteProperty() {
        return this.dataNote;
    }

    public String getDataNote() {
        return dataNoteProperty().get();
    }

    public void setDataNote(String dataNote) {
        dataNoteProperty().set(dataNote);
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

    public Set getTblPurchaseOrders() {
        return this.tblPurchaseOrders;
    }

    public void setTblPurchaseOrders(Set tblPurchaseOrders) {
        this.tblPurchaseOrders = tblPurchaseOrders;
    }

    public Set getTblReservationPayments() {
        return this.tblReservationPayments;
    }

    public void setTblReservationPayments(Set tblReservationPayments) {
        this.tblReservationPayments = tblReservationPayments;
    }

    public Set getTblEmployeeServiceCharges() {
        return this.tblEmployeeServiceCharges;
    }

    public void setTblEmployeeServiceCharges(Set tblEmployeeServiceCharges) {
        this.tblEmployeeServiceCharges = tblEmployeeServiceCharges;
    }

    public Set getTblFinanceTransactions() {
        return this.tblFinanceTransactions;
    }

    public void setTblFinanceTransactions(Set tblFinanceTransactions) {
        this.tblFinanceTransactions = tblFinanceTransactions;
    }

    public Set getTblEmployeeResigns() {
        return this.tblEmployeeResigns;
    }

    public void setTblEmployeeResigns(Set tblEmployeeResigns) {
        this.tblEmployeeResigns = tblEmployeeResigns;
    }

    public Set getTblReturs() {
        return this.tblReturs;
    }

    public void setTblReturs(Set tblReturs) {
        this.tblReturs = tblReturs;
    }

    public Set getTblEmployeeThrs() {
        return this.tblEmployeeThrs;
    }

    public void setTblEmployeeThrs(Set tblEmployeeThrs) {
        this.tblEmployeeThrs = tblEmployeeThrs;
    }

    public Set getTblEmployeeCutOffLeaves() {
        return this.tblEmployeeCutOffLeaves;
    }

    public void setTblEmployeeCutOffLeaves(Set tblEmployeeCutOffLeaves) {
        this.tblEmployeeCutOffLeaves = tblEmployeeCutOffLeaves;
    }

    public Set getTblEmployeePayrolls() {
        return this.tblEmployeePayrolls;
    }

    public void setTblEmployeePayrolls(Set tblEmployeePayrolls) {
        this.tblEmployeePayrolls = tblEmployeePayrolls;
    }

    public Set getTblMemorandumInvoices() {
        return this.tblMemorandumInvoices;
    }

    public void setTblMemorandumInvoices(Set tblMemorandumInvoices) {
        this.tblMemorandumInvoices = tblMemorandumInvoices;
    }

}
