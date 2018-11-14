package hotelfx.persistence.model;
// Generated Oct 24, 2018 12:59:27 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblRestoBankAccount generated by hbm2java
 */
public class TblRestoBankAccount implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefBankAccountHolderStatus> refBankAccountHolderStatus;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblBankAccount> tblBankAccount;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblResto> tblResto;
    private final StringProperty restoBankAccountNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblRestoBankAccount() {
        this.idrelation = new SimpleLongProperty();
        this.refBankAccountHolderStatus = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblBankAccount = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblResto = new SimpleObjectProperty<>();
        this.restoBankAccountNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblRestoBankAccount(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblRestoBankAccount(long idrelation, RefBankAccountHolderStatus refBankAccountHolderStatus, RefRecordStatus refRecordStatus, TblBankAccount tblBankAccount, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblResto tblResto, String restoBankAccountNote, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refBankAccountHolderStatusProperty().set(refBankAccountHolderStatus);
        refRecordStatusProperty().set(refRecordStatus);
        tblBankAccountProperty().set(tblBankAccount);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblRestoProperty().set(tblResto);
        restoBankAccountNoteProperty().set(restoBankAccountNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblRestoBankAccount(TblRestoBankAccount tblRestoBankAccount) {
        this();
        idrelationProperty().set(tblRestoBankAccount.getIdrelation());
        refBankAccountHolderStatusProperty().set(tblRestoBankAccount.getRefBankAccountHolderStatus());
        refRecordStatusProperty().set(tblRestoBankAccount.getRefRecordStatus());
        tblBankAccountProperty().set(tblRestoBankAccount.getTblBankAccount());
        tblEmployeeByLastUpdateByProperty().set(tblRestoBankAccount.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblRestoBankAccount.getTblEmployeeByCreateBy());
        tblRestoProperty().set(tblRestoBankAccount.getTblResto());
        restoBankAccountNoteProperty().set(tblRestoBankAccount.getRestoBankAccountNote());
        createDateProperty().set(tblRestoBankAccount.getCreateDate());
        lastUpdateDateProperty().set(tblRestoBankAccount.getLastUpdateDate());

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

    public final ObjectProperty<RefBankAccountHolderStatus> refBankAccountHolderStatusProperty() {
        return this.refBankAccountHolderStatus;
    }

    public RefBankAccountHolderStatus getRefBankAccountHolderStatus() {
        return refBankAccountHolderStatusProperty().get();
    }

    public void setRefBankAccountHolderStatus(RefBankAccountHolderStatus refBankAccountHolderStatus) {
        refBankAccountHolderStatusProperty().set(refBankAccountHolderStatus);
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

    public final ObjectProperty<TblResto> tblRestoProperty() {
        return this.tblResto;
    }

    public TblResto getTblResto() {
        return tblRestoProperty().get();
    }

    public void setTblResto(TblResto tblResto) {
        tblRestoProperty().set(tblResto);
    }

    public final StringProperty restoBankAccountNoteProperty() {
        return this.restoBankAccountNote;
    }

    public String getRestoBankAccountNote() {
        return restoBankAccountNoteProperty().get();
    }

    public void setRestoBankAccountNote(String restoBankAccountNote) {
        restoBankAccountNoteProperty().set(restoBankAccountNote);
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

    @Override
    public String toString(){
        return getTblBankAccount().getCodeBankAccount() + " / " + getTblBankAccount().getTblBank().getBankName();
    }
    
}
