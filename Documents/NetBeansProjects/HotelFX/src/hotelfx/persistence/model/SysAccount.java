package hotelfx.persistence.model;
// Generated Apr 28, 2018 9:53:55 AM by Hibernate Tools 4.3.1

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
 * SysAccount generated by hbm2java
 */
public class SysAccount implements java.io.Serializable {

    private final LongProperty idaccount;
    private final ObjectProperty<RefAccountType> refAccountType;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final StringProperty codeAccount;
    private final StringProperty accountName;
    private final StringProperty accountNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set sysProgramAccounts;
    private Set tblStockOpnames;

    public SysAccount() {
        this.idaccount = new SimpleLongProperty();
        this.refAccountType = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.codeAccount = new SimpleStringProperty();
        this.accountName = new SimpleStringProperty();
        this.accountNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.sysProgramAccounts = new HashSet(0);
        this.tblStockOpnames = new HashSet(0);
    }

    public SysAccount(long idaccount) {
        this();
        idaccountProperty().set(idaccount);
    }

    public SysAccount(long idaccount, RefAccountType refAccountType, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, String codeAccount, String accountName, String accountNote, Date createDate, Date lastUpdateDate, Set sysProgramAccounts, Set tblStockOpnames) {
        this();
        idaccountProperty().set(idaccount);
        refAccountTypeProperty().set(refAccountType);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        codeAccountProperty().set(codeAccount);
        accountNameProperty().set(accountName);
        accountNoteProperty().set(accountNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.sysProgramAccounts = sysProgramAccounts;
        this.tblStockOpnames = tblStockOpnames;
    }

    public SysAccount(SysAccount sysAccount) {
        this();
        idaccountProperty().set(sysAccount.getIdaccount());
        refAccountTypeProperty().set(sysAccount.getRefAccountType());
        refRecordStatusProperty().set(sysAccount.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(sysAccount.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(sysAccount.getTblEmployeeByCreateBy());
        codeAccountProperty().set(sysAccount.getCodeAccount());
        accountNameProperty().set(sysAccount.getAccountName());
        accountNoteProperty().set(sysAccount.getAccountNote());
        createDateProperty().set(sysAccount.getCreateDate());
        lastUpdateDateProperty().set(sysAccount.getLastUpdateDate());

        this.sysProgramAccounts = sysAccount.getSysProgramAccounts();
        this.tblStockOpnames = sysAccount.getTblStockOpnames();
    }

    public final LongProperty idaccountProperty() {
        return this.idaccount;
    }

    public long getIdaccount() {
        return idaccountProperty().get();
    }

    public void setIdaccount(long idaccount) {
        idaccountProperty().set(idaccount);
    }

    public final ObjectProperty<RefAccountType> refAccountTypeProperty() {
        return this.refAccountType;
    }

    public RefAccountType getRefAccountType() {
        return refAccountTypeProperty().get();
    }

    public void setRefAccountType(RefAccountType refAccountType) {
        refAccountTypeProperty().set(refAccountType);
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

    public final StringProperty codeAccountProperty() {
        return this.codeAccount;
    }

    public String getCodeAccount() {
        return codeAccountProperty().get();
    }

    public void setCodeAccount(String codeAccount) {
        codeAccountProperty().set(codeAccount);
    }

    public final StringProperty accountNameProperty() {
        return this.accountName;
    }

    public String getAccountName() {
        return accountNameProperty().get();
    }

    public void setAccountName(String accountName) {
        accountNameProperty().set(accountName);
    }

    public final StringProperty accountNoteProperty() {
        return this.accountNote;
    }

    public String getAccountNote() {
        return accountNoteProperty().get();
    }

    public void setAccountNote(String accountNote) {
        accountNoteProperty().set(accountNote);
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

    public Set getSysProgramAccounts() {
        return this.sysProgramAccounts;
    }

    public void setSysProgramAccounts(Set sysProgramAccounts) {
        this.sysProgramAccounts = sysProgramAccounts;
    }

    public Set getTblStockOpnames() {
        return this.tblStockOpnames;
    }

    public void setTblStockOpnames(Set tblStockOpnames) {
        this.tblStockOpnames = tblStockOpnames;
    }

    @Override
    public String toString(){
        return getCodeAccount();
    }
    
}
