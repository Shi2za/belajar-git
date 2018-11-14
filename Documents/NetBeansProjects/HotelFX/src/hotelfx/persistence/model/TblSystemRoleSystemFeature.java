package hotelfx.persistence.model;
// Generated Mar 6, 2018 9:26:16 AM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * TblSystemRoleSystemFeature generated by hbm2java
 */
public class TblSystemRoleSystemFeature implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblSystemFeature> tblSystemFeature;
    private final ObjectProperty<TblSystemRole> tblSystemRole;
    private final BooleanProperty createData;
    private final BooleanProperty updateData;
    private final BooleanProperty approveData;
    private final BooleanProperty deleteData;
    private final BooleanProperty printData;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblSystemRoleSystemFeature() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblSystemFeature = new SimpleObjectProperty<>();
        this.tblSystemRole = new SimpleObjectProperty<>();
        this.createData = new SimpleBooleanProperty();
        this.updateData = new SimpleBooleanProperty();
        this.approveData = new SimpleBooleanProperty();
        this.deleteData = new SimpleBooleanProperty();
        this.printData = new SimpleBooleanProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblSystemRoleSystemFeature(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblSystemRoleSystemFeature(long idrelation, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblSystemFeature tblSystemFeature, TblSystemRole tblSystemRole, Boolean createData, Boolean updateData, Boolean approveData, Boolean deleteData, Boolean printData, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblSystemFeatureProperty().set(tblSystemFeature);
        tblSystemRoleProperty().set(tblSystemRole);
        createDataProperty().set(createData);
        updateDataProperty().set(updateData);
        approveDataProperty().set(approveData);
        deleteDataProperty().set(deleteData);
        printDataProperty().set(printData);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblSystemRoleSystemFeature(TblSystemRoleSystemFeature tblSystemRoleSystemFeature) {
        this();
        idrelationProperty().set(tblSystemRoleSystemFeature.getIdrelation());
        refRecordStatusProperty().set(tblSystemRoleSystemFeature.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblSystemRoleSystemFeature.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblSystemRoleSystemFeature.getTblEmployeeByCreateBy());
        tblSystemFeatureProperty().set(tblSystemRoleSystemFeature.getTblSystemFeature());
        tblSystemRoleProperty().set(tblSystemRoleSystemFeature.getTblSystemRole());
        createDataProperty().set(tblSystemRoleSystemFeature.getCreateData());
        updateDataProperty().set(tblSystemRoleSystemFeature.getUpdateData());
        approveDataProperty().set(tblSystemRoleSystemFeature.getApproveData());
        deleteDataProperty().set(tblSystemRoleSystemFeature.getDeleteData());
        printDataProperty().set(tblSystemRoleSystemFeature.getPrintData());
        createDateProperty().set(tblSystemRoleSystemFeature.getCreateDate());
        lastUpdateDateProperty().set(tblSystemRoleSystemFeature.getLastUpdateDate());

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

    public final ObjectProperty<TblSystemFeature> tblSystemFeatureProperty() {
        return this.tblSystemFeature;
    }

    public TblSystemFeature getTblSystemFeature() {
        return tblSystemFeatureProperty().get();
    }

    public void setTblSystemFeature(TblSystemFeature tblSystemFeature) {
        tblSystemFeatureProperty().set(tblSystemFeature);
    }

    public final ObjectProperty<TblSystemRole> tblSystemRoleProperty() {
        return this.tblSystemRole;
    }

    public TblSystemRole getTblSystemRole() {
        return tblSystemRoleProperty().get();
    }

    public void setTblSystemRole(TblSystemRole tblSystemRole) {
        tblSystemRoleProperty().set(tblSystemRole);
    }

    public final BooleanProperty createDataProperty() {
        return this.createData;
    }

    public Boolean getCreateData() {
        return createDataProperty().get();
    }

    public void setCreateData(Boolean createData) {
        createDataProperty().set(createData);
    }

    public final BooleanProperty updateDataProperty() {
        return this.updateData;
    }

    public Boolean getUpdateData() {
        return updateDataProperty().get();
    }

    public void setUpdateData(Boolean updateData) {
        updateDataProperty().set(updateData);
    }

    public final BooleanProperty approveDataProperty() {
        return this.approveData;
    }

    public Boolean getApproveData() {
        return approveDataProperty().get();
    }

    public void setApproveData(Boolean approveData) {
        approveDataProperty().set(approveData);
    }

    public final BooleanProperty deleteDataProperty() {
        return this.deleteData;
    }

    public Boolean getDeleteData() {
        return deleteDataProperty().get();
    }

    public void setDeleteData(Boolean deleteData) {
        deleteDataProperty().set(deleteData);
    }

    public final BooleanProperty printDataProperty() {
        return this.printData;
    }

    public Boolean getPrintData() {
        return printDataProperty().get();
    }

    public void setPrintData(Boolean printData) {
        printDataProperty().set(printData);
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