package hotelfx.persistence.model;
// Generated Jan 29, 2018 9:24:40 AM by Hibernate Tools 4.3.1


import java.util.Date;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * SysPasswordDeleteDebt generated by hbm2java
 */
public class SysPasswordDeleteDebt  implements java.io.Serializable {

    private final LongProperty idpasswordDeleteDebt;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreatedBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdatedBy;
    private final StringProperty passwordName;
    private final StringProperty passwordValue;
    private final ObjectProperty<Date> createdDate;
    private final ObjectProperty<Date> lastUpdatedDate;


    public SysPasswordDeleteDebt() {
        this.idpasswordDeleteDebt = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreatedBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdatedBy = new SimpleObjectProperty<>();
        this.passwordName = new SimpleStringProperty();
        this.passwordValue = new SimpleStringProperty();
        this.createdDate = new SimpleObjectProperty<>();
        this.lastUpdatedDate = new SimpleObjectProperty<>();
    }

    public SysPasswordDeleteDebt(long idpasswordDeleteDebt){
        this();
        idpasswordDeleteDebtProperty().set(idpasswordDeleteDebt);
    }

    public SysPasswordDeleteDebt(long idpasswordDeleteDebt, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreatedBy, TblEmployee tblEmployeeByLastUpdatedBy, String passwordName, String passwordValue, Date createdDate, Date lastUpdatedDate) {
        this();
        idpasswordDeleteDebtProperty().set(idpasswordDeleteDebt);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreatedByProperty().set(tblEmployeeByCreatedBy);
        tblEmployeeByLastUpdatedByProperty().set(tblEmployeeByLastUpdatedBy);
        passwordNameProperty().set(passwordName);
        passwordValueProperty().set(passwordValue);
        createdDateProperty().set(createdDate);
        lastUpdatedDateProperty().set(lastUpdatedDate);
    }
    
    public SysPasswordDeleteDebt(SysPasswordDeleteDebt sysPasswordDeleteDebt){
        this();
        idpasswordDeleteDebtProperty().set(sysPasswordDeleteDebt.getIdpasswordDeleteDebt());
        refRecordStatusProperty().set(sysPasswordDeleteDebt.getRefRecordStatus());
        tblEmployeeByCreatedByProperty().set(sysPasswordDeleteDebt.getTblEmployeeByCreatedBy());
        tblEmployeeByLastUpdatedByProperty().set(sysPasswordDeleteDebt.getTblEmployeeByLastUpdatedBy());
        passwordNameProperty().set(sysPasswordDeleteDebt.getPasswordName());
        passwordValueProperty().set(sysPasswordDeleteDebt.getPasswordValue());
        createdDateProperty().set(sysPasswordDeleteDebt.getCreatedDate());
        lastUpdatedDateProperty().set(sysPasswordDeleteDebt.getLastUpdatedDate());
    }
    
    public final LongProperty idpasswordDeleteDebtProperty() {
            return this.idpasswordDeleteDebt;
    }

    public long getIdpasswordDeleteDebt() {
            return idpasswordDeleteDebtProperty().get();
    }

    public void setIdpasswordDeleteDebt(long idpasswordDeleteDebt) {
            idpasswordDeleteDebtProperty().set(idpasswordDeleteDebt);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByCreatedByProperty() {
            return this.tblEmployeeByCreatedBy;
    }

    public TblEmployee getTblEmployeeByCreatedBy() {
            return tblEmployeeByCreatedByProperty().get();
    }

    public void setTblEmployeeByCreatedBy(TblEmployee tblEmployeeByCreatedBy) {
            tblEmployeeByCreatedByProperty().set(tblEmployeeByCreatedBy);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByLastUpdatedByProperty() {
            return this.tblEmployeeByLastUpdatedBy;
    }

    public TblEmployee getTblEmployeeByLastUpdatedBy() {
            return tblEmployeeByLastUpdatedByProperty().get();
    }

    public void setTblEmployeeByLastUpdatedBy(TblEmployee tblEmployeeByLastUpdatedBy) {
            tblEmployeeByLastUpdatedByProperty().set(tblEmployeeByLastUpdatedBy);
    }

    public final StringProperty passwordNameProperty() {
            return this.passwordName;
    }

    public String getPasswordName() {
            return passwordNameProperty().get();
    }

    public void setPasswordName(String passwordName) {
            passwordNameProperty().set(passwordName);
    }

    public final StringProperty passwordValueProperty() {
            return this.passwordValue;
    }

    public String getPasswordValue() {
            return passwordValueProperty().get();
    }

    public void setPasswordValue(String passwordValue) {
            passwordValueProperty().set(passwordValue);
    }

    public final ObjectProperty<Date> createdDateProperty() {
            return this.createdDate;
    }

    public Date getCreatedDate() {
            return createdDateProperty().get();
    }

    public void setCreatedDate(Date createdDate) {
            createdDateProperty().set(createdDate);
    }

    public final ObjectProperty<Date> lastUpdatedDateProperty() {
            return this.lastUpdatedDate;
    }

    public Date getLastUpdatedDate() {
            return lastUpdatedDateProperty().get();
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
            lastUpdatedDateProperty().set(lastUpdatedDate);
    }

}


