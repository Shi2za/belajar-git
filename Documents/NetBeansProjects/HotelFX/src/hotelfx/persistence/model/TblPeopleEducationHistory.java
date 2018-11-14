package hotelfx.persistence.model;
// Generated Aug 21, 2017 1:49:32 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblPeopleEducationHistory generated by hbm2java
 */
public class TblPeopleEducationHistory implements java.io.Serializable {

    private final LongProperty idhistory;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblPeople> tblPeople;
    private final StringProperty educationLevel;
    private final StringProperty schoolName;
    private final StringProperty schoolPlace;
    private final ObjectProperty<Date> joinDate;
    private final ObjectProperty<Date> dropDate;
    private final StringProperty major;
    private final StringProperty gpa;
    private final StringProperty standardGpa;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblPeopleEducationHistory() {
        this.idhistory = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblPeople = new SimpleObjectProperty<>();
        this.educationLevel = new SimpleStringProperty();
        this.schoolName = new SimpleStringProperty();
        this.schoolPlace = new SimpleStringProperty();
        this.joinDate = new SimpleObjectProperty<>();
        this.dropDate = new SimpleObjectProperty<>();
        this.major = new SimpleStringProperty();
        this.gpa = new SimpleStringProperty();
        this.standardGpa = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblPeopleEducationHistory(long idhistory) {
        this();
        idhistoryProperty().set(idhistory);
    }

    public TblPeopleEducationHistory(long idhistory, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblPeople tblPeople, String educationLevel, String schoolName, String schoolPlace, Date joinDate, Date dropDate, String major, String gpa, String standardGpa, Date createDate, Date lastUpdateDate) {
        this();
        idhistoryProperty().set(idhistory);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblPeopleProperty().set(tblPeople);
        educationLevelProperty().set(educationLevel);
        schoolNameProperty().set(schoolName);
        schoolPlaceProperty().set(schoolPlace);
        joinDateProperty().set(joinDate);
        dropDateProperty().set(dropDate);
        majorProperty().set(major);
        gpaProperty().set(gpa);
        standardGpaProperty().set(standardGpa);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblPeopleEducationHistory(TblPeopleEducationHistory tblPeopleEducationHistory) {
        this();
        idhistoryProperty().set(tblPeopleEducationHistory.getIdhistory());
        refRecordStatusProperty().set(tblPeopleEducationHistory.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblPeopleEducationHistory.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblPeopleEducationHistory.getTblEmployeeByCreateBy());
        tblPeopleProperty().set(tblPeopleEducationHistory.getTblPeople());
        educationLevelProperty().set(tblPeopleEducationHistory.getEducationLevel());
        schoolNameProperty().set(tblPeopleEducationHistory.getSchoolName());
        schoolPlaceProperty().set(tblPeopleEducationHistory.getSchoolPlace());
        joinDateProperty().set(tblPeopleEducationHistory.getJoinDate());
        dropDateProperty().set(tblPeopleEducationHistory.getDropDate());
        majorProperty().set(tblPeopleEducationHistory.getMajor());
        gpaProperty().set(tblPeopleEducationHistory.getGpa());
        standardGpaProperty().set(tblPeopleEducationHistory.getStandardGpa());
        createDateProperty().set(tblPeopleEducationHistory.getCreateDate());
        lastUpdateDateProperty().set(tblPeopleEducationHistory.getLastUpdateDate());

    }

    public final LongProperty idhistoryProperty() {
        return this.idhistory;
    }

    public long getIdhistory() {
        return idhistoryProperty().get();
    }

    public void setIdhistory(long idhistory) {
        idhistoryProperty().set(idhistory);
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

    public final ObjectProperty<TblPeople> tblPeopleProperty() {
        return this.tblPeople;
    }

    public TblPeople getTblPeople() {
        return tblPeopleProperty().get();
    }

    public void setTblPeople(TblPeople tblPeople) {
        tblPeopleProperty().set(tblPeople);
    }

    public final StringProperty educationLevelProperty() {
        return this.educationLevel;
    }

    public String getEducationLevel() {
        return educationLevelProperty().get();
    }

    public void setEducationLevel(String educationLevel) {
        educationLevelProperty().set(educationLevel);
    }

    public final StringProperty schoolNameProperty() {
        return this.schoolName;
    }

    public String getSchoolName() {
        return schoolNameProperty().get();
    }

    public void setSchoolName(String schoolName) {
        schoolNameProperty().set(schoolName);
    }

    public final StringProperty schoolPlaceProperty() {
        return this.schoolPlace;
    }

    public String getSchoolPlace() {
        return schoolPlaceProperty().get();
    }

    public void setSchoolPlace(String schoolPlace) {
        schoolPlaceProperty().set(schoolPlace);
    }

    public final ObjectProperty<Date> joinDateProperty() {
        return this.joinDate;
    }

    public Date getJoinDate() {
        return joinDateProperty().get();
    }

    public void setJoinDate(Date joinDate) {
        joinDateProperty().set(joinDate);
    }

    public final ObjectProperty<Date> dropDateProperty() {
        return this.dropDate;
    }

    public Date getDropDate() {
        return dropDateProperty().get();
    }

    public void setDropDate(Date dropDate) {
        dropDateProperty().set(dropDate);
    }

    public final StringProperty majorProperty() {
        return this.major;
    }

    public String getMajor() {
        return majorProperty().get();
    }

    public void setMajor(String major) {
        majorProperty().set(major);
    }

    public final StringProperty gpaProperty() {
        return this.gpa;
    }

    public String getGpa() {
        return gpaProperty().get();
    }

    public void setGpa(String gpa) {
        gpaProperty().set(gpa);
    }

    public final StringProperty standardGpaProperty() {
        return this.standardGpa;
    }

    public String getStandardGpa() {
        return standardGpaProperty().get();
    }

    public void setStandardGpa(String standardGpa) {
        standardGpaProperty().set(standardGpa);
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