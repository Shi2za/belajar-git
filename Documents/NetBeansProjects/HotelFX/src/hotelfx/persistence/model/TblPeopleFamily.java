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
 * TblPeopleFamily generated by hbm2java
 */
public class TblPeopleFamily implements java.io.Serializable {

    private final LongProperty idfamily;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblPeople> tblPeople;
    private final StringProperty familyName;
    private final StringProperty familyRelation;
    private final ObjectProperty<Date> birthDate;
    private final StringProperty homePlace;
    private final StringProperty contactNumber;
    private final StringProperty lastEducation;
    private final StringProperty job;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblPeopleFamily() {
        this.idfamily = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblPeople = new SimpleObjectProperty<>();
        this.familyName = new SimpleStringProperty();
        this.familyRelation = new SimpleStringProperty();
        this.birthDate = new SimpleObjectProperty<>();
        this.homePlace = new SimpleStringProperty();
        this.contactNumber = new SimpleStringProperty();
        this.lastEducation = new SimpleStringProperty();
        this.job = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblPeopleFamily(long idfamily) {
        this();
        idfamilyProperty().set(idfamily);
    }

    public TblPeopleFamily(long idfamily, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblPeople tblPeople, String familyName, String familyRelation, Date birthDate, String homePlace, String contactNumber, String lastEducation, String job, Date createDate, Date lastUpdateDate) {
        this();
        idfamilyProperty().set(idfamily);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblPeopleProperty().set(tblPeople);
        familyNameProperty().set(familyName);
        familyRelationProperty().set(familyRelation);
        birthDateProperty().set(birthDate);
        homePlaceProperty().set(homePlace);
        contactNumberProperty().set(contactNumber);
        lastEducationProperty().set(lastEducation);
        jobProperty().set(job);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblPeopleFamily(TblPeopleFamily tblPeopleFamily) {
        this();
        idfamilyProperty().set(tblPeopleFamily.getIdfamily());
        refRecordStatusProperty().set(tblPeopleFamily.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblPeopleFamily.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblPeopleFamily.getTblEmployeeByLastUpdateBy());
        tblPeopleProperty().set(tblPeopleFamily.getTblPeople());
        familyNameProperty().set(tblPeopleFamily.getFamilyName());
        familyRelationProperty().set(tblPeopleFamily.getFamilyRelation());
        birthDateProperty().set(tblPeopleFamily.getBirthDate());
        homePlaceProperty().set(tblPeopleFamily.getHomePlace());
        contactNumberProperty().set(tblPeopleFamily.getContactNumber());
        lastEducationProperty().set(tblPeopleFamily.getLastEducation());
        jobProperty().set(tblPeopleFamily.getJob());
        createDateProperty().set(tblPeopleFamily.getCreateDate());
        lastUpdateDateProperty().set(tblPeopleFamily.getLastUpdateDate());

    }

    public final LongProperty idfamilyProperty() {
        return this.idfamily;
    }

    public long getIdfamily() {
        return idfamilyProperty().get();
    }

    public void setIdfamily(long idfamily) {
        idfamilyProperty().set(idfamily);
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

    public final ObjectProperty<TblPeople> tblPeopleProperty() {
        return this.tblPeople;
    }

    public TblPeople getTblPeople() {
        return tblPeopleProperty().get();
    }

    public void setTblPeople(TblPeople tblPeople) {
        tblPeopleProperty().set(tblPeople);
    }

    public final StringProperty familyNameProperty() {
        return this.familyName;
    }

    public String getFamilyName() {
        return familyNameProperty().get();
    }

    public void setFamilyName(String familyName) {
        familyNameProperty().set(familyName);
    }

    public final StringProperty familyRelationProperty() {
        return this.familyRelation;
    }

    public String getFamilyRelation() {
        return familyRelationProperty().get();
    }

    public void setFamilyRelation(String familyRelation) {
        familyRelationProperty().set(familyRelation);
    }

    public final ObjectProperty<Date> birthDateProperty() {
        return this.birthDate;
    }

    public Date getBirthDate() {
        return birthDateProperty().get();
    }

    public void setBirthDate(Date birthDate) {
        birthDateProperty().set(birthDate);
    }

    public final StringProperty homePlaceProperty() {
        return this.homePlace;
    }

    public String getHomePlace() {
        return homePlaceProperty().get();
    }

    public void setHomePlace(String homePlace) {
        homePlaceProperty().set(homePlace);
    }

    public final StringProperty contactNumberProperty() {
        return this.contactNumber;
    }

    public String getContactNumber() {
        return contactNumberProperty().get();
    }

    public void setContactNumber(String contactNumber) {
        contactNumberProperty().set(contactNumber);
    }

    public final StringProperty lastEducationProperty() {
        return this.lastEducation;
    }

    public String getLastEducation() {
        return lastEducationProperty().get();
    }

    public void setLastEducation(String lastEducation) {
        lastEducationProperty().set(lastEducation);
    }

    public final StringProperty jobProperty() {
        return this.job;
    }

    public String getJob() {
        return jobProperty().get();
    }

    public void setJob(String job) {
        jobProperty().set(job);
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
