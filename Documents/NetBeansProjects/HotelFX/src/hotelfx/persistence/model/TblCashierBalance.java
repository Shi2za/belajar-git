package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

/**
 * TblCashierBalance generated by hbm2java
 */
public class TblCashierBalance implements java.io.Serializable {

    private final LongProperty idbalance;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdemployee;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty codeBalance;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblCashierBalance() {
        this.idbalance = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByIdemployee = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.codeBalance = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblCashierBalance(long idbalance, TblEmployee tblEmployeeByIdemployee) {
        this();
        idbalanceProperty().set(idbalance);
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
    }

    public TblCashierBalance(long idbalance, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByIdemployee, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String codeBalance, Date createDate, Date lastUpdateDate) {
        this();
        idbalanceProperty().set(idbalance);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        codeBalanceProperty().set(codeBalance);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblCashierBalance(TblCashierBalance tblCashierBalance) {
        this();
        idbalanceProperty().set(tblCashierBalance.getIdbalance());
        refRecordStatusProperty().set(tblCashierBalance.getRefRecordStatus());
        tblEmployeeByIdemployeeProperty().set(tblCashierBalance.getTblEmployeeByIdemployee());
        tblEmployeeByCreateByProperty().set(tblCashierBalance.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblCashierBalance.getTblEmployeeByLastUpdateBy());
        codeBalanceProperty().set(tblCashierBalance.getCodeBalance());
        createDateProperty().set(tblCashierBalance.getCreateDate());
        lastUpdateDateProperty().set(tblCashierBalance.getLastUpdateDate());

    }

    public final LongProperty idbalanceProperty() {
        return this.idbalance;
    }

    public long getIdbalance() {
        return idbalanceProperty().get();
    }

    public void setIdbalance(long idbalance) {
        idbalanceProperty().set(idbalance);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByIdemployeeProperty() {
        return this.tblEmployeeByIdemployee;
    }

    public TblEmployee getTblEmployeeByIdemployee() {
        return tblEmployeeByIdemployeeProperty().get();
    }

    public void setTblEmployeeByIdemployee(TblEmployee tblEmployeeByIdemployee) {
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
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

    public final StringProperty codeBalanceProperty() {
        return this.codeBalance;
    }

    public String getCodeBalance() {
        return codeBalanceProperty().get();
    }

    public void setCodeBalance(String codeBalance) {
        codeBalanceProperty().set(codeBalance);
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