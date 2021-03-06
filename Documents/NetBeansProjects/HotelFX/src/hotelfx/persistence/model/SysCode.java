package hotelfx.persistence.model;
// Generated Oct 3, 2017 10:16:46 AM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * SysCode generated by hbm2java
 */
public class SysCode implements java.io.Serializable {

    private final LongProperty idcode;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final StringProperty dataName;
    private final StringProperty codePrefix;
    private final LongProperty codeLastNumber;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public SysCode() {
        this.idcode = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.dataName = new SimpleStringProperty();
        this.codePrefix = new SimpleStringProperty();
        this.codeLastNumber = new SimpleLongProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public SysCode(long idcode) {
        this();
        idcodeProperty().set(idcode);
    }

    public SysCode(long idcode, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, String dataName, String codePrefix, Long codeLastNumber, Date createDate, Date lastUpdateDate) {
        this();
        idcodeProperty().set(idcode);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        dataNameProperty().set(dataName);
        codePrefixProperty().set(codePrefix);
        codeLastNumberProperty().set(codeLastNumber);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public SysCode(SysCode sysCode) {
        this();
        idcodeProperty().set(sysCode.getIdcode());
        refRecordStatusProperty().set(sysCode.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(sysCode.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(sysCode.getTblEmployeeByLastUpdateBy());
        dataNameProperty().set(sysCode.getDataName());
        codePrefixProperty().set(sysCode.getCodePrefix());
        codeLastNumberProperty().set(sysCode.getCodeLastNumber());
        createDateProperty().set(sysCode.getCreateDate());
        lastUpdateDateProperty().set(sysCode.getLastUpdateDate());

    }

    public final LongProperty idcodeProperty() {
        return this.idcode;
    }

    public long getIdcode() {
        return idcodeProperty().get();
    }

    public void setIdcode(long idcode) {
        idcodeProperty().set(idcode);
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

    public final StringProperty dataNameProperty() {
        return this.dataName;
    }

    public String getDataName() {
        return dataNameProperty().get();
    }

    public void setDataName(String dataName) {
        dataNameProperty().set(dataName);
    }

    public final StringProperty codePrefixProperty() {
        return this.codePrefix;
    }

    public String getCodePrefix() {
        return codePrefixProperty().get();
    }

    public void setCodePrefix(String codePrefix) {
        codePrefixProperty().set(codePrefix);
    }

    public final LongProperty codeLastNumberProperty() {
        return this.codeLastNumber;
    }

    public Long getCodeLastNumber() {
        return codeLastNumberProperty().get();
    }

    public void setCodeLastNumber(Long codeLastNumber) {
        codeLastNumberProperty().set(codeLastNumber);
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
