package hotelfx.persistence.model;
// Generated Jan 11, 2018 9:40:01 AM by Hibernate Tools 4.3.1

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
 * TblBankCard generated by hbm2java
 */
public class TblBankCard implements java.io.Serializable {

    private final LongProperty idbankCard;
    private final ObjectProperty<RefBankCardType> refBankCardType;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblBank> tblBank;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty bankCardName;
    private final StringProperty bankCardClassName;
    private final StringProperty bankCardNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblBankEventCards;
    private Set tblReservationBills;

    public TblBankCard() {
        this.idbankCard = new SimpleLongProperty();
        this.refBankCardType = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblBank = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.bankCardName = new SimpleStringProperty();
        this.bankCardClassName = new SimpleStringProperty();
        this.bankCardNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblBankEventCards = new HashSet(0);
        this.tblReservationBills = new HashSet(0);
    }

    public TblBankCard(long idbankCard) {
        this();
        idbankCardProperty().set(idbankCard);
    }

    public TblBankCard(long idbankCard, RefBankCardType refBankCardType, RefRecordStatus refRecordStatus, TblBank tblBank, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String bankCardName, String bankCardClassName, String bankCardNote, Date createDate, Date lastUpdateDate, Set tblBankEventCards, Set tblReservationBills) {
        this();
        idbankCardProperty().set(idbankCard);
        refBankCardTypeProperty().set(refBankCardType);
        refRecordStatusProperty().set(refRecordStatus);
        tblBankProperty().set(tblBank);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        bankCardNameProperty().set(bankCardName);
        bankCardClassNameProperty().set(bankCardClassName);
        bankCardNoteProperty().set(bankCardNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblBankEventCards = tblBankEventCards;
        this.tblReservationBills = tblReservationBills;
    }

    public TblBankCard(TblBankCard tblBankCard) {
        this();
        idbankCardProperty().set(tblBankCard.getIdbankCard());
        refBankCardTypeProperty().set(tblBankCard.getRefBankCardType());
        refRecordStatusProperty().set(tblBankCard.getRefRecordStatus());
        tblBankProperty().set(tblBankCard.getTblBank());
        tblEmployeeByCreateByProperty().set(tblBankCard.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblBankCard.getTblEmployeeByLastUpdateBy());
        bankCardNameProperty().set(tblBankCard.getBankCardName());
        bankCardClassNameProperty().set(tblBankCard.getBankCardClassName());
        bankCardNoteProperty().set(tblBankCard.getBankCardNote());
        createDateProperty().set(tblBankCard.getCreateDate());
        lastUpdateDateProperty().set(tblBankCard.getLastUpdateDate());

        this.tblBankEventCards = tblBankCard.getTblBankEventCards();
        this.tblReservationBills = tblBankCard.getTblReservationBills();
    }

    public final LongProperty idbankCardProperty() {
        return this.idbankCard;
    }

    public long getIdbankCard() {
        return idbankCardProperty().get();
    }

    public void setIdbankCard(long idbankCard) {
        idbankCardProperty().set(idbankCard);
    }

    public final ObjectProperty<RefBankCardType> refBankCardTypeProperty() {
        return this.refBankCardType;
    }

    public RefBankCardType getRefBankCardType() {
        return refBankCardTypeProperty().get();
    }

    public void setRefBankCardType(RefBankCardType refBankCardType) {
        refBankCardTypeProperty().set(refBankCardType);
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

    public final StringProperty bankCardNameProperty() {
        return this.bankCardName;
    }

    public String getBankCardName() {
        return bankCardNameProperty().get();
    }

    public void setBankCardName(String bankCardName) {
        bankCardNameProperty().set(bankCardName);
    }

    public final StringProperty bankCardClassNameProperty() {
        return this.bankCardClassName;
    }

    public String getBankCardClassName() {
        return bankCardClassNameProperty().get();
    }

    public void setBankCardClassName(String bankCardClassName) {
        bankCardClassNameProperty().set(bankCardClassName);
    }

    public final StringProperty bankCardNoteProperty() {
        return this.bankCardNote;
    }

    public String getBankCardNote() {
        return bankCardNoteProperty().get();
    }

    public void setBankCardNote(String bankCardNote) {
        bankCardNoteProperty().set(bankCardNote);
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

    public Set getTblReservationBills() {
        return this.tblReservationBills;
    }

    public void setTblReservationBills(Set tblReservationBills) {
        this.tblReservationBills = tblReservationBills;
    }

    @Override
    public String toString(){
        return getBankCardName();
    }
    
}
