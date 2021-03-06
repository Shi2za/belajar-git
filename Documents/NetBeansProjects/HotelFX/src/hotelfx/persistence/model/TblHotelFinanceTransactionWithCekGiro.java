package hotelfx.persistence.model;
// Generated Jul 12, 2018 12:59:54 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblHotelFinanceTransactionWithCekGiro generated by hbm2java
 */
public class TblHotelFinanceTransactionWithCekGiro implements java.io.Serializable {

    private final LongProperty iddetail;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblBank> tblBank;
    private final ObjectProperty<TblBankAccount> tblBankAccountBySenderBankAccount;
    private final ObjectProperty<TblBankAccount> tblBankAccountByReceiverBankAccount;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblHotelFinanceTransaction> tblHotelFinanceTransaction;
    private final StringProperty codeCekGiro;
    private final ObjectProperty<Date> issueDateTime;
    private final ObjectProperty<Date> validUntilDateTime;
    private final StringProperty senderName;
    private final StringProperty receiverName;
    private final StringProperty cekGiroNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblHotelFinanceTransactionWithCekGiro() {
        this.iddetail = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblBank = new SimpleObjectProperty<>();
        this.tblBankAccountBySenderBankAccount = new SimpleObjectProperty<>();
        this.tblBankAccountByReceiverBankAccount = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblHotelFinanceTransaction = new SimpleObjectProperty<>();
        this.codeCekGiro = new SimpleStringProperty();
        this.issueDateTime = new SimpleObjectProperty<>();
        this.validUntilDateTime = new SimpleObjectProperty<>();
        this.senderName = new SimpleStringProperty();
        this.receiverName = new SimpleStringProperty();
        this.cekGiroNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblHotelFinanceTransactionWithCekGiro(long iddetail) {
        this();
        iddetailProperty().set(iddetail);
    }

    public TblHotelFinanceTransactionWithCekGiro(long iddetail, RefRecordStatus refRecordStatus, TblBank tblBank, TblBankAccount tblBankAccountBySenderBankAccount, TblBankAccount tblBankAccountByReceiverBankAccount, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblHotelFinanceTransaction tblHotelFinanceTransaction, String codeCekGiro, Date issueDateTime, Date validUntilDateTime, String senderName, String receiverName, String cekGiroNote, Date createDate, Date lastUpdateDate) {
        this();
        iddetailProperty().set(iddetail);
        refRecordStatusProperty().set(refRecordStatus);
        tblBankProperty().set(tblBank);
        tblBankAccountBySenderBankAccountProperty().set(tblBankAccountBySenderBankAccount);
        tblBankAccountByReceiverBankAccountProperty().set(tblBankAccountByReceiverBankAccount);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblHotelFinanceTransactionProperty().set(tblHotelFinanceTransaction);
        codeCekGiroProperty().set(codeCekGiro);
        issueDateTimeProperty().set(issueDateTime);
        validUntilDateTimeProperty().set(validUntilDateTime);
        senderNameProperty().set(senderName);
        receiverNameProperty().set(receiverName);
        cekGiroNoteProperty().set(cekGiroNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblHotelFinanceTransactionWithCekGiro(TblHotelFinanceTransactionWithCekGiro tblHotelFinanceTransactionWithCekGiro) {
        this();
        iddetailProperty().set(tblHotelFinanceTransactionWithCekGiro.getIddetail());
        refRecordStatusProperty().set(tblHotelFinanceTransactionWithCekGiro.getRefRecordStatus());
        tblBankProperty().set(tblHotelFinanceTransactionWithCekGiro.getTblBank());
        tblBankAccountBySenderBankAccountProperty().set(tblHotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount());
        tblBankAccountByReceiverBankAccountProperty().set(tblHotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount());
        tblEmployeeByCreateByProperty().set(tblHotelFinanceTransactionWithCekGiro.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblHotelFinanceTransactionWithCekGiro.getTblEmployeeByLastUpdateBy());
        tblHotelFinanceTransactionProperty().set(tblHotelFinanceTransactionWithCekGiro.getTblHotelFinanceTransaction());
        codeCekGiroProperty().set(tblHotelFinanceTransactionWithCekGiro.getCodeCekGiro());
        issueDateTimeProperty().set(tblHotelFinanceTransactionWithCekGiro.getIssueDateTime());
        validUntilDateTimeProperty().set(tblHotelFinanceTransactionWithCekGiro.getValidUntilDateTime());
        senderNameProperty().set(tblHotelFinanceTransactionWithCekGiro.getSenderName());
        receiverNameProperty().set(tblHotelFinanceTransactionWithCekGiro.getReceiverName());
        cekGiroNoteProperty().set(tblHotelFinanceTransactionWithCekGiro.getCekGiroNote());
        createDateProperty().set(tblHotelFinanceTransactionWithCekGiro.getCreateDate());
        lastUpdateDateProperty().set(tblHotelFinanceTransactionWithCekGiro.getLastUpdateDate());

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

    public final ObjectProperty<TblBank> tblBankProperty() {
        return this.tblBank;
    }

    public TblBank getTblBank() {
        return tblBankProperty().get();
    }

    public void setTblBank(TblBank tblBank) {
        tblBankProperty().set(tblBank);
    }

    public final ObjectProperty<TblBankAccount> tblBankAccountBySenderBankAccountProperty() {
        return this.tblBankAccountBySenderBankAccount;
    }

    public TblBankAccount getTblBankAccountBySenderBankAccount() {
        return tblBankAccountBySenderBankAccountProperty().get();
    }

    public void setTblBankAccountBySenderBankAccount(TblBankAccount tblBankAccountBySenderBankAccount) {
        tblBankAccountBySenderBankAccountProperty().set(tblBankAccountBySenderBankAccount);
    }

    public final ObjectProperty<TblBankAccount> tblBankAccountByReceiverBankAccountProperty() {
        return this.tblBankAccountByReceiverBankAccount;
    }

    public TblBankAccount getTblBankAccountByReceiverBankAccount() {
        return tblBankAccountByReceiverBankAccountProperty().get();
    }

    public void setTblBankAccountByReceiverBankAccount(TblBankAccount tblBankAccountByReceiverBankAccount) {
        tblBankAccountByReceiverBankAccountProperty().set(tblBankAccountByReceiverBankAccount);
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

    public final ObjectProperty<TblHotelFinanceTransaction> tblHotelFinanceTransactionProperty() {
        return this.tblHotelFinanceTransaction;
    }

    public TblHotelFinanceTransaction getTblHotelFinanceTransaction() {
        return tblHotelFinanceTransactionProperty().get();
    }

    public void setTblHotelFinanceTransaction(TblHotelFinanceTransaction tblHotelFinanceTransaction) {
        tblHotelFinanceTransactionProperty().set(tblHotelFinanceTransaction);
    }

    public final StringProperty codeCekGiroProperty() {
        return this.codeCekGiro;
    }

    public String getCodeCekGiro() {
        return codeCekGiroProperty().get();
    }

    public void setCodeCekGiro(String codeCekGiro) {
        codeCekGiroProperty().set(codeCekGiro);
    }

    public final ObjectProperty<Date> issueDateTimeProperty() {
        return this.issueDateTime;
    }

    public Date getIssueDateTime() {
        return issueDateTimeProperty().get();
    }

    public void setIssueDateTime(Date issueDateTime) {
        issueDateTimeProperty().set(issueDateTime);
    }

    public final ObjectProperty<Date> validUntilDateTimeProperty() {
        return this.validUntilDateTime;
    }

    public Date getValidUntilDateTime() {
        return validUntilDateTimeProperty().get();
    }

    public void setValidUntilDateTime(Date validUntilDateTime) {
        validUntilDateTimeProperty().set(validUntilDateTime);
    }

    public final StringProperty senderNameProperty() {
        return this.senderName;
    }

    public String getSenderName() {
        return senderNameProperty().get();
    }

    public void setSenderName(String senderName) {
        senderNameProperty().set(senderName);
    }

    public final StringProperty receiverNameProperty() {
        return this.receiverName;
    }

    public String getReceiverName() {
        return receiverNameProperty().get();
    }

    public void setReceiverName(String receiverName) {
        receiverNameProperty().set(receiverName);
    }

    public final StringProperty cekGiroNoteProperty() {
        return this.cekGiroNote;
    }

    public String getCekGiroNote() {
        return cekGiroNoteProperty().get();
    }

    public void setCekGiroNote(String cekGiroNote) {
        cekGiroNoteProperty().set(cekGiroNote);
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
