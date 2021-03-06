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
 * TblHotelFinanceTransactionWithTransfer generated by hbm2java
 */
public class TblHotelFinanceTransactionWithTransfer implements java.io.Serializable {

    private final LongProperty iddetail;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblBankAccount> tblBankAccountByReceiverBankAccount;
    private final ObjectProperty<TblBankAccount> tblBankAccountBySenderBankAccount;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblHotelFinanceTransaction> tblHotelFinanceTransaction;
    private final StringProperty codeTransfer;
    private final StringProperty senderName;
    private final StringProperty receiverName;
    private final StringProperty transferNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblHotelFinanceTransactionWithTransfer() {
        this.iddetail = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblBankAccountByReceiverBankAccount = new SimpleObjectProperty<>();
        this.tblBankAccountBySenderBankAccount = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblHotelFinanceTransaction = new SimpleObjectProperty<>();
        this.codeTransfer = new SimpleStringProperty();
        this.senderName = new SimpleStringProperty();
        this.receiverName = new SimpleStringProperty();
        this.transferNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblHotelFinanceTransactionWithTransfer(long iddetail) {
        this();
        iddetailProperty().set(iddetail);
    }

    public TblHotelFinanceTransactionWithTransfer(long iddetail, RefRecordStatus refRecordStatus, TblBankAccount tblBankAccountByReceiverBankAccount, TblBankAccount tblBankAccountBySenderBankAccount, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblHotelFinanceTransaction tblHotelFinanceTransaction, String codeTransfer, String senderName, String receiverName, String transferNote, Date createDate, Date lastUpdateDate) {
        this();
        iddetailProperty().set(iddetail);
        refRecordStatusProperty().set(refRecordStatus);
        tblBankAccountByReceiverBankAccountProperty().set(tblBankAccountByReceiverBankAccount);
        tblBankAccountBySenderBankAccountProperty().set(tblBankAccountBySenderBankAccount);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblHotelFinanceTransactionProperty().set(tblHotelFinanceTransaction);
        codeTransferProperty().set(codeTransfer);
        senderNameProperty().set(senderName);
        receiverNameProperty().set(receiverName);
        transferNoteProperty().set(transferNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblHotelFinanceTransactionWithTransfer(TblHotelFinanceTransactionWithTransfer tblHotelFinanceTransactionWithTransfer) {
        this();
        iddetailProperty().set(tblHotelFinanceTransactionWithTransfer.getIddetail());
        refRecordStatusProperty().set(tblHotelFinanceTransactionWithTransfer.getRefRecordStatus());
        tblBankAccountByReceiverBankAccountProperty().set(tblHotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount());
        tblBankAccountBySenderBankAccountProperty().set(tblHotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount());
        tblEmployeeByCreateByProperty().set(tblHotelFinanceTransactionWithTransfer.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblHotelFinanceTransactionWithTransfer.getTblEmployeeByLastUpdateBy());
        tblHotelFinanceTransactionProperty().set(tblHotelFinanceTransactionWithTransfer.getTblHotelFinanceTransaction());
        codeTransferProperty().set(tblHotelFinanceTransactionWithTransfer.getCodeTransfer());
        senderNameProperty().set(tblHotelFinanceTransactionWithTransfer.getSenderName());
        receiverNameProperty().set(tblHotelFinanceTransactionWithTransfer.getReceiverName());
        transferNoteProperty().set(tblHotelFinanceTransactionWithTransfer.getTransferNote());
        createDateProperty().set(tblHotelFinanceTransactionWithTransfer.getCreateDate());
        lastUpdateDateProperty().set(tblHotelFinanceTransactionWithTransfer.getLastUpdateDate());

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

    public final ObjectProperty<TblBankAccount> tblBankAccountByReceiverBankAccountProperty() {
        return this.tblBankAccountByReceiverBankAccount;
    }

    public TblBankAccount getTblBankAccountByReceiverBankAccount() {
        return tblBankAccountByReceiverBankAccountProperty().get();
    }

    public void setTblBankAccountByReceiverBankAccount(TblBankAccount tblBankAccountByReceiverBankAccount) {
        tblBankAccountByReceiverBankAccountProperty().set(tblBankAccountByReceiverBankAccount);
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

    public final StringProperty codeTransferProperty() {
        return this.codeTransfer;
    }

    public String getCodeTransfer() {
        return codeTransferProperty().get();
    }

    public void setCodeTransfer(String codeTransfer) {
        codeTransferProperty().set(codeTransfer);
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

    public final StringProperty transferNoteProperty() {
        return this.transferNote;
    }

    public String getTransferNote() {
        return transferNoteProperty().get();
    }

    public void setTransferNote(String transferNote) {
        transferNoteProperty().set(transferNote);
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
