package hotelfx.persistence.model;
// Generated Jul 12, 2018 12:59:54 PM by Hibernate Tools 4.3.1

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
 * TblBank generated by hbm2java
 */
public class TblBank implements java.io.Serializable {

    private final LongProperty idbank;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty codeBank;
    private final StringProperty bankName;
    private final StringProperty bankNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblBankEventCards;
    private Set tblBankAccounts;
    private Set tblBankCards;
    private Set tblHotelFinanceTransactionWithCekGiros;
    private Set tblBankEdcs;
    private Set tblReservationPaymentWithBankCards;
    private Set tblReservationPaymentWithCekGiros;

    public TblBank() {
        this.idbank = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.codeBank = new SimpleStringProperty();
        this.bankName = new SimpleStringProperty();
        this.bankNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblBankEventCards = new HashSet(0);
        this.tblBankAccounts = new HashSet(0);
        this.tblBankCards = new HashSet(0);
        this.tblHotelFinanceTransactionWithCekGiros = new HashSet(0);
        this.tblBankEdcs = new HashSet(0);
        this.tblReservationPaymentWithBankCards = new HashSet(0);
        this.tblReservationPaymentWithCekGiros = new HashSet(0);
    }

    public TblBank(long idbank) {
        this();
        idbankProperty().set(idbank);
    }

    public TblBank(long idbank, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String codeBank, String bankName, String bankNote, Date createDate, Date lastUpdateDate, Set tblBankEventCards, Set tblBankAccounts, Set tblBankCards, Set tblHotelFinanceTransactionWithCekGiros, Set tblBankEdcs, Set tblReservationPaymentWithBankCards, Set tblReservationPaymentWithCekGiros) {
        this();
        idbankProperty().set(idbank);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        codeBankProperty().set(codeBank);
        bankNameProperty().set(bankName);
        bankNoteProperty().set(bankNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblBankEventCards = tblBankEventCards;
        this.tblBankAccounts = tblBankAccounts;
        this.tblBankCards = tblBankCards;
        this.tblHotelFinanceTransactionWithCekGiros = tblHotelFinanceTransactionWithCekGiros;
        this.tblBankEdcs = tblBankEdcs;
        this.tblReservationPaymentWithBankCards = tblReservationPaymentWithBankCards;
        this.tblReservationPaymentWithCekGiros = tblReservationPaymentWithCekGiros;
    }

    public TblBank(TblBank tblBank) {
        this();
        idbankProperty().set(tblBank.getIdbank());
        refRecordStatusProperty().set(tblBank.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblBank.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblBank.getTblEmployeeByLastUpdateBy());
        codeBankProperty().set(tblBank.getCodeBank());
        bankNameProperty().set(tblBank.getBankName());
        bankNoteProperty().set(tblBank.getBankNote());
        createDateProperty().set(tblBank.getCreateDate());
        lastUpdateDateProperty().set(tblBank.getLastUpdateDate());

        this.tblBankEventCards = tblBank.getTblBankEventCards();
        this.tblBankAccounts = tblBank.getTblBankAccounts();
        this.tblBankCards = tblBank.getTblBankCards();
        this.tblHotelFinanceTransactionWithCekGiros = tblBank.getTblHotelFinanceTransactionWithCekGiros();
        this.tblBankEdcs = tblBank.getTblBankEdcs();
        this.tblReservationPaymentWithBankCards = tblBank.getTblReservationPaymentWithBankCards();
        this.tblReservationPaymentWithCekGiros = tblBank.getTblReservationPaymentWithCekGiros();
    }

    public final LongProperty idbankProperty() {
        return this.idbank;
    }

    public long getIdbank() {
        return idbankProperty().get();
    }

    public void setIdbank(long idbank) {
        idbankProperty().set(idbank);
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

    public final StringProperty codeBankProperty() {
        return this.codeBank;
    }

    public String getCodeBank() {
        return codeBankProperty().get();
    }

    public void setCodeBank(String codeBank) {
        codeBankProperty().set(codeBank);
    }

    public final StringProperty bankNameProperty() {
        return this.bankName;
    }

    public String getBankName() {
        return bankNameProperty().get();
    }

    public void setBankName(String bankName) {
        bankNameProperty().set(bankName);
    }

    public final StringProperty bankNoteProperty() {
        return this.bankNote;
    }

    public String getBankNote() {
        return bankNoteProperty().get();
    }

    public void setBankNote(String bankNote) {
        bankNoteProperty().set(bankNote);
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

    public Set getTblBankEventCards() {
        return this.tblBankEventCards;
    }

    public void setTblBankEventCards(Set tblBankEventCards) {
        this.tblBankEventCards = tblBankEventCards;
    }

    public Set getTblBankAccounts() {
        return this.tblBankAccounts;
    }

    public void setTblBankAccounts(Set tblBankAccounts) {
        this.tblBankAccounts = tblBankAccounts;
    }

    public Set getTblBankCards() {
        return this.tblBankCards;
    }

    public void setTblBankCards(Set tblBankCards) {
        this.tblBankCards = tblBankCards;
    }

    public Set getTblHotelFinanceTransactionWithCekGiros() {
        return this.tblHotelFinanceTransactionWithCekGiros;
    }

    public void setTblHotelFinanceTransactionWithCekGiros(Set tblHotelFinanceTransactionWithCekGiros) {
        this.tblHotelFinanceTransactionWithCekGiros = tblHotelFinanceTransactionWithCekGiros;
    }

    public Set getTblBankEdcs() {
        return this.tblBankEdcs;
    }

    public void setTblBankEdcs(Set tblBankEdcs) {
        this.tblBankEdcs = tblBankEdcs;
    }

    public Set getTblReservationPaymentWithBankCards() {
        return this.tblReservationPaymentWithBankCards;
    }

    public void setTblReservationPaymentWithBankCards(Set tblReservationPaymentWithBankCards) {
        this.tblReservationPaymentWithBankCards = tblReservationPaymentWithBankCards;
    }

    public Set getTblReservationPaymentWithCekGiros() {
        return this.tblReservationPaymentWithCekGiros;
    }

    public void setTblReservationPaymentWithCekGiros(Set tblReservationPaymentWithCekGiros) {
        this.tblReservationPaymentWithCekGiros = tblReservationPaymentWithCekGiros;
    }

    @Override
    public String toString() {
        return getBankName();
    }
    
}
