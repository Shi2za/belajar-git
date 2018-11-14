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
 * TblRfidcard generated by hbm2java
 */
public class TblRfidcard implements java.io.Serializable {

    private final LongProperty idrfidcard;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<RefRfidcardStatus> refRfidcardStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final StringProperty codeRfidcard;
    private final StringProperty note;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblRoomCards;

    public TblRfidcard() {
        this.idrfidcard = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.refRfidcardStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.codeRfidcard = new SimpleStringProperty();
        this.note = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblRoomCards = new HashSet(0);
    }

    public TblRfidcard(long idrfidcard) {
        this();
        idrfidcardProperty().set(idrfidcard);
    }

    public TblRfidcard(long idrfidcard, RefRecordStatus refRecordStatus, RefRfidcardStatus refRfidcardStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, String codeRfidcard, String note, Date createDate, Date lastUpdateDate, Set tblRoomCards) {
        this();
        idrfidcardProperty().set(idrfidcard);
        refRecordStatusProperty().set(refRecordStatus);
        refRfidcardStatusProperty().set(refRfidcardStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        codeRfidcardProperty().set(codeRfidcard);
        noteProperty().set(note);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblRoomCards = tblRoomCards;
    }

    public TblRfidcard(TblRfidcard tblRfidcard) {
        this();
        idrfidcardProperty().set(tblRfidcard.getIdrfidcard());
        refRecordStatusProperty().set(tblRfidcard.getRefRecordStatus());
        refRfidcardStatusProperty().set(tblRfidcard.getRefRfidcardStatus());
        tblEmployeeByLastUpdateByProperty().set(tblRfidcard.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblRfidcard.getTblEmployeeByCreateBy());
        codeRfidcardProperty().set(tblRfidcard.getCodeRfidcard());
        noteProperty().set(tblRfidcard.getNote());
        createDateProperty().set(tblRfidcard.getCreateDate());
        lastUpdateDateProperty().set(tblRfidcard.getLastUpdateDate());

        this.tblRoomCards = tblRfidcard.getTblRoomCards();
    }

    public final LongProperty idrfidcardProperty() {
        return this.idrfidcard;
    }

    public long getIdrfidcard() {
        return idrfidcardProperty().get();
    }

    public void setIdrfidcard(long idrfidcard) {
        idrfidcardProperty().set(idrfidcard);
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

    public final ObjectProperty<RefRfidcardStatus> refRfidcardStatusProperty() {
        return this.refRfidcardStatus;
    }

    public RefRfidcardStatus getRefRfidcardStatus() {
        return refRfidcardStatusProperty().get();
    }

    public void setRefRfidcardStatus(RefRfidcardStatus refRfidcardStatus) {
        refRfidcardStatusProperty().set(refRfidcardStatus);
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

    public final StringProperty codeRfidcardProperty() {
        return this.codeRfidcard;
    }

    public String getCodeRfidcard() {
        return codeRfidcardProperty().get();
    }

    public void setCodeRfidcard(String codeRfidcard) {
        codeRfidcardProperty().set(codeRfidcard);
    }

    public final StringProperty noteProperty() {
        return this.note;
    }

    public String getNote() {
        return noteProperty().get();
    }

    public void setNote(String note) {
        noteProperty().set(note);
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

    public Set getTblRoomCards() {
        return this.tblRoomCards;
    }

    public void setTblRoomCards(Set tblRoomCards) {
        this.tblRoomCards = tblRoomCards;
    }

}
