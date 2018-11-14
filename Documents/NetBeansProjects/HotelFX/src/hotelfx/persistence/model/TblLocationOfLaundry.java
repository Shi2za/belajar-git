package hotelfx.persistence.model;
// Generated Sep 5, 2017 4:52:36 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblLocationOfLaundry generated by hbm2java
 */
public class TblLocationOfLaundry implements java.io.Serializable {

    private final LongProperty idlaundry;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblLocation> tblLocation;
    private final StringProperty laundryName;
    private final StringProperty laundryNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblLocationOfLaundry() {
        this.idlaundry = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblLocation = new SimpleObjectProperty<>();
        this.laundryName = new SimpleStringProperty();
        this.laundryNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblLocationOfLaundry(long idlaundry) {
        this();
        idlaundryProperty().set(idlaundry);
    }

    public TblLocationOfLaundry(long idlaundry, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblLocation tblLocation, String laundryName, String laundryNote, Date createDate, Date lastUpdateDate) {
        this();
        idlaundryProperty().set(idlaundry);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblLocationProperty().set(tblLocation);
        laundryNameProperty().set(laundryName);
        laundryNoteProperty().set(laundryNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblLocationOfLaundry(TblLocationOfLaundry tblLocationOfLaundry) {
        this();
        idlaundryProperty().set(tblLocationOfLaundry.getIdlaundry());
        refRecordStatusProperty().set(tblLocationOfLaundry.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblLocationOfLaundry.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblLocationOfLaundry.getTblEmployeeByCreateBy());
        tblLocationProperty().set(tblLocationOfLaundry.getTblLocation());
        laundryNameProperty().set(tblLocationOfLaundry.getLaundryName());
        laundryNoteProperty().set(tblLocationOfLaundry.getLaundryNote());
        createDateProperty().set(tblLocationOfLaundry.getCreateDate());
        lastUpdateDateProperty().set(tblLocationOfLaundry.getLastUpdateDate());

    }

    public final LongProperty idlaundryProperty() {
        return this.idlaundry;
    }

    public long getIdlaundry() {
        return idlaundryProperty().get();
    }

    public void setIdlaundry(long idlaundry) {
        idlaundryProperty().set(idlaundry);
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

    public final ObjectProperty<TblLocation> tblLocationProperty() {
        return this.tblLocation;
    }

    public TblLocation getTblLocation() {
        return tblLocationProperty().get();
    }

    public void setTblLocation(TblLocation tblLocation) {
        tblLocationProperty().set(tblLocation);
    }

    public final StringProperty laundryNameProperty() {
        return this.laundryName;
    }

    public String getLaundryName() {
        return laundryNameProperty().get();
    }

    public void setLaundryName(String laundryName) {
        laundryNameProperty().set(laundryName);
    }

    public final StringProperty laundryNoteProperty() {
        return this.laundryNote;
    }

    public String getLaundryNote() {
        return laundryNoteProperty().get();
    }

    public void setLaundryNote(String laundryNote) {
        laundryNoteProperty().set(laundryNote);
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
    public String toString() {
        return getLaundryName();
    }

}