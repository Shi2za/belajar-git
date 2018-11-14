package hotelfx.persistence.model;
// Generated Sep 25, 2018 4:01:53 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblCompanyBalanceBankAccount generated by hbm2java
 */
public class TblCompanyBalanceBankAccount implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblBankAccount> tblBankAccount;
    private final ObjectProperty<TblCompanyBalance> tblCompanyBalance;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<BigDecimal> companyBalanceBankAccountNominal;
    private final StringProperty companyBalanceBankAccountNote;
    private final IntegerProperty companyBalanceBankAccountStatus;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblCalendarEmployeeDebts;
    private Set tblCalendarEmployeePaymentDebtHistories;
    private Set tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender;
    private Set tblCalendarEmployeePaymentHistories;
    private Set tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived;

    public TblCompanyBalanceBankAccount() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblBankAccount = new SimpleObjectProperty<>();
        this.tblCompanyBalance = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.companyBalanceBankAccountNominal = new SimpleObjectProperty<>();
        this.companyBalanceBankAccountNote = new SimpleStringProperty();
        this.companyBalanceBankAccountStatus = new SimpleIntegerProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblCalendarEmployeeDebts = new HashSet(0);
        this.tblCalendarEmployeePaymentDebtHistories = new HashSet(0);
        this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender = new HashSet(0);
        this.tblCalendarEmployeePaymentHistories = new HashSet(0);
        this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived = new HashSet(0);
    }

    public TblCompanyBalanceBankAccount(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblCompanyBalanceBankAccount(long idrelation, RefRecordStatus refRecordStatus, TblBankAccount tblBankAccount, TblCompanyBalance tblCompanyBalance, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, BigDecimal companyBalanceBankAccountNominal, String companyBalanceBankAccountNote, Integer companyBalanceBankAccountStatus, Date createDate, Date lastUpdateDate, Set tblCalendarEmployeeDebts, Set tblCalendarEmployeePaymentDebtHistories, Set tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender, Set tblCalendarEmployeePaymentHistories, Set tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        tblBankAccountProperty().set(tblBankAccount);
        tblCompanyBalanceProperty().set(tblCompanyBalance);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        companyBalanceBankAccountNominalProperty().set(companyBalanceBankAccountNominal);
        companyBalanceBankAccountNoteProperty().set(companyBalanceBankAccountNote);
        companyBalanceBankAccountStatusProperty().set(companyBalanceBankAccountStatus);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblCalendarEmployeeDebts = tblCalendarEmployeeDebts;
        this.tblCalendarEmployeePaymentDebtHistories = tblCalendarEmployeePaymentDebtHistories;
        this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender = tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender;
        this.tblCalendarEmployeePaymentHistories = tblCalendarEmployeePaymentHistories;
        this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived = tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived;
    }

    public TblCompanyBalanceBankAccount(TblCompanyBalanceBankAccount tblCompanyBalanceBankAccount) {
        this();
        idrelationProperty().set(tblCompanyBalanceBankAccount.getIdrelation());
        refRecordStatusProperty().set(tblCompanyBalanceBankAccount.getRefRecordStatus());
        tblBankAccountProperty().set(tblCompanyBalanceBankAccount.getTblBankAccount());
        tblCompanyBalanceProperty().set(tblCompanyBalanceBankAccount.getTblCompanyBalance());
        tblEmployeeByCreateByProperty().set(tblCompanyBalanceBankAccount.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblCompanyBalanceBankAccount.getTblEmployeeByLastUpdateBy());
        companyBalanceBankAccountNominalProperty().set(tblCompanyBalanceBankAccount.getCompanyBalanceBankAccountNominal());
        companyBalanceBankAccountNoteProperty().set(tblCompanyBalanceBankAccount.getCompanyBalanceBankAccountNote());
        companyBalanceBankAccountStatusProperty().set(tblCompanyBalanceBankAccount.getCompanyBalanceBankAccountStatus());
        createDateProperty().set(tblCompanyBalanceBankAccount.getCreateDate());
        lastUpdateDateProperty().set(tblCompanyBalanceBankAccount.getLastUpdateDate());

        this.tblCalendarEmployeeDebts = tblCompanyBalanceBankAccount.getTblCalendarEmployeeDebts();
        this.tblCalendarEmployeePaymentDebtHistories = tblCompanyBalanceBankAccount.getTblCalendarEmployeePaymentDebtHistories();
        this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender = tblCompanyBalanceBankAccount.getTblCompanyBalanceTransferReceivedsForIdcbbankAccountSender();
        this.tblCalendarEmployeePaymentHistories = tblCompanyBalanceBankAccount.getTblCalendarEmployeePaymentHistories();
        this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived = tblCompanyBalanceBankAccount.getTblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived();
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

    public final ObjectProperty<TblBankAccount> tblBankAccountProperty() {
        return this.tblBankAccount;
    }

    public TblBankAccount getTblBankAccount() {
        return tblBankAccountProperty().get();
    }

    public void setTblBankAccount(TblBankAccount tblBankAccount) {
        tblBankAccountProperty().set(tblBankAccount);
    }

    public final ObjectProperty<TblCompanyBalance> tblCompanyBalanceProperty() {
        return this.tblCompanyBalance;
    }

    public TblCompanyBalance getTblCompanyBalance() {
        return tblCompanyBalanceProperty().get();
    }

    public void setTblCompanyBalance(TblCompanyBalance tblCompanyBalance) {
        tblCompanyBalanceProperty().set(tblCompanyBalance);
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

    public final ObjectProperty<BigDecimal> companyBalanceBankAccountNominalProperty() {
        return this.companyBalanceBankAccountNominal;
    }

    public BigDecimal getCompanyBalanceBankAccountNominal() {
        return companyBalanceBankAccountNominalProperty().get();
    }

    public void setCompanyBalanceBankAccountNominal(BigDecimal companyBalanceBankAccountNominal) {
        companyBalanceBankAccountNominalProperty().set(companyBalanceBankAccountNominal);
    }

    public final StringProperty companyBalanceBankAccountNoteProperty() {
        return this.companyBalanceBankAccountNote;
    }

    public String getCompanyBalanceBankAccountNote() {
        return companyBalanceBankAccountNoteProperty().get();
    }

    public void setCompanyBalanceBankAccountNote(String companyBalanceBankAccountNote) {
        companyBalanceBankAccountNoteProperty().set(companyBalanceBankAccountNote);
    }

    public final IntegerProperty companyBalanceBankAccountStatusProperty() {
        return this.companyBalanceBankAccountStatus;
    }

    public Integer getCompanyBalanceBankAccountStatus() {
        return companyBalanceBankAccountStatusProperty().get();
    }

    public void setCompanyBalanceBankAccountStatus(Integer companyBalanceBankAccountStatus) {
        companyBalanceBankAccountStatusProperty().set(companyBalanceBankAccountStatus);
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

    public Set getTblCalendarEmployeeDebts() {
        return this.tblCalendarEmployeeDebts;
    }

    public void setTblCalendarEmployeeDebts(Set tblCalendarEmployeeDebts) {
        this.tblCalendarEmployeeDebts = tblCalendarEmployeeDebts;
    }

    public Set getTblCalendarEmployeePaymentDebtHistories() {
        return this.tblCalendarEmployeePaymentDebtHistories;
    }

    public void setTblCalendarEmployeePaymentDebtHistories(Set tblCalendarEmployeePaymentDebtHistories) {
        this.tblCalendarEmployeePaymentDebtHistories = tblCalendarEmployeePaymentDebtHistories;
    }

    public Set getTblCompanyBalanceTransferReceivedsForIdcbbankAccountSender() {
        return this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender;
    }

    public void setTblCompanyBalanceTransferReceivedsForIdcbbankAccountSender(Set tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender) {
        this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender = tblCompanyBalanceTransferReceivedsForIdcbbankAccountSender;
    }

    public Set getTblCalendarEmployeePaymentHistories() {
        return this.tblCalendarEmployeePaymentHistories;
    }

    public void setTblCalendarEmployeePaymentHistories(Set tblCalendarEmployeePaymentHistories) {
        this.tblCalendarEmployeePaymentHistories = tblCalendarEmployeePaymentHistories;
    }

    public Set getTblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived() {
        return this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived;
    }

    public void setTblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived(Set tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived) {
        this.tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived = tblCompanyBalanceTransferReceivedsForIdcbbankAccountReceived;
    }

    @Override
    public String toString(){
        return getTblBankAccount().getCodeBankAccount() + " / " + getTblBankAccount().getTblBank().getBankName();
    }
    
}
