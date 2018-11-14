package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * SysProgramAccount generated by hbm2java
 */
public class SysProgramAccount implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<SysAccount> sysAccount;
    private final ObjectProperty<SysProgram> sysProgram;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public SysProgramAccount() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.sysAccount = new SimpleObjectProperty<>();
        this.sysProgram = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public SysProgramAccount(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public SysProgramAccount(long idrelation, RefRecordStatus refRecordStatus, SysAccount sysAccount, SysProgram sysProgram, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        sysAccountProperty().set(sysAccount);
        sysProgramProperty().set(sysProgram);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public SysProgramAccount(SysProgramAccount sysProgramAccount) {
        this();
        idrelationProperty().set(sysProgramAccount.getIdrelation());
        refRecordStatusProperty().set(sysProgramAccount.getRefRecordStatus());
        sysAccountProperty().set(sysProgramAccount.getSysAccount());
        sysProgramProperty().set(sysProgramAccount.getSysProgram());
        tblEmployeeByLastUpdateByProperty().set(sysProgramAccount.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(sysProgramAccount.getTblEmployeeByCreateBy());
        createDateProperty().set(sysProgramAccount.getCreateDate());
        lastUpdateDateProperty().set(sysProgramAccount.getLastUpdateDate());

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

    public final ObjectProperty<SysAccount> sysAccountProperty() {
        return this.sysAccount;
    }

    public SysAccount getSysAccount() {
        return sysAccountProperty().get();
    }

    public void setSysAccount(SysAccount sysAccount) {
        sysAccountProperty().set(sysAccount);
    }

    public final ObjectProperty<SysProgram> sysProgramProperty() {
        return this.sysProgram;
    }

    public SysProgram getSysProgram() {
        return sysProgramProperty().get();
    }

    public void setSysProgram(SysProgram sysProgram) {
        sysProgramProperty().set(sysProgram);
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
