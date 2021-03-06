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
 * TblPeopleEmploymentHistory generated by hbm2java
 */
public class TblPeopleEmploymentHistory implements java.io.Serializable {

    private final LongProperty idhistory;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblPeople> tblPeople;
    private final StringProperty job;
    private final StringProperty jobDes;
    private final StringProperty workPlace;
    private final StringProperty companyName;
    private final LongProperty salary;
    private final StringProperty seniorContactNumber;
    private final ObjectProperty<Date> joinDate;
    private final ObjectProperty<Date> dropDate;
    private final StringProperty dropReasons;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lasUpdateDate;

    public TblPeopleEmploymentHistory() {
        this.idhistory = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblPeople = new SimpleObjectProperty<>();
        this.job = new SimpleStringProperty();
        this.jobDes = new SimpleStringProperty();
        this.workPlace = new SimpleStringProperty();
        this.companyName = new SimpleStringProperty();
        this.salary = new SimpleLongProperty();
        this.seniorContactNumber = new SimpleStringProperty();
        this.joinDate = new SimpleObjectProperty<>();
        this.dropDate = new SimpleObjectProperty<>();
        this.dropReasons = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lasUpdateDate = new SimpleObjectProperty<>();

    }

    public TblPeopleEmploymentHistory(long idhistory) {
        this();
        idhistoryProperty().set(idhistory);
    }

    public TblPeopleEmploymentHistory(long idhistory, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblPeople tblPeople, String job, String jobDes, String workPlace, String companyName, Long salary, String seniorContactNumber, Date joinDate, Date dropDate, String dropReasons, Date createDate, Date lasUpdateDate) {
        this();
        idhistoryProperty().set(idhistory);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblPeopleProperty().set(tblPeople);
        jobProperty().set(job);
        jobDesProperty().set(jobDes);
        workPlaceProperty().set(workPlace);
        companyNameProperty().set(companyName);
        salaryProperty().set(salary);
        seniorContactNumberProperty().set(seniorContactNumber);
        joinDateProperty().set(joinDate);
        dropDateProperty().set(dropDate);
        dropReasonsProperty().set(dropReasons);
        createDateProperty().set(createDate);
        lasUpdateDateProperty().set(lasUpdateDate);

    }

    public TblPeopleEmploymentHistory(TblPeopleEmploymentHistory tblPeopleEmploymentHistory) {
        this();
        idhistoryProperty().set(tblPeopleEmploymentHistory.getIdhistory());
        refRecordStatusProperty().set(tblPeopleEmploymentHistory.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblPeopleEmploymentHistory.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblPeopleEmploymentHistory.getTblEmployeeByCreateBy());
        tblPeopleProperty().set(tblPeopleEmploymentHistory.getTblPeople());
        jobProperty().set(tblPeopleEmploymentHistory.getJob());
        jobDesProperty().set(tblPeopleEmploymentHistory.getJobDes());
        workPlaceProperty().set(tblPeopleEmploymentHistory.getWorkPlace());
        companyNameProperty().set(tblPeopleEmploymentHistory.getCompanyName());
        salaryProperty().set(tblPeopleEmploymentHistory.getSalary());
        seniorContactNumberProperty().set(tblPeopleEmploymentHistory.getSeniorContactNumber());
        joinDateProperty().set(tblPeopleEmploymentHistory.getJoinDate());
        dropDateProperty().set(tblPeopleEmploymentHistory.getDropDate());
        dropReasonsProperty().set(tblPeopleEmploymentHistory.getDropReasons());
        createDateProperty().set(tblPeopleEmploymentHistory.getCreateDate());
        lasUpdateDateProperty().set(tblPeopleEmploymentHistory.getLasUpdateDate());

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

    public final StringProperty jobProperty() {
        return this.job;
    }

    public String getJob() {
        return jobProperty().get();
    }

    public void setJob(String job) {
        jobProperty().set(job);
    }

    public final StringProperty jobDesProperty() {
        return this.jobDes;
    }

    public String getJobDes() {
        return jobDesProperty().get();
    }

    public void setJobDes(String jobDes) {
        jobDesProperty().set(jobDes);
    }

    public final StringProperty workPlaceProperty() {
        return this.workPlace;
    }

    public String getWorkPlace() {
        return workPlaceProperty().get();
    }

    public void setWorkPlace(String workPlace) {
        workPlaceProperty().set(workPlace);
    }

    public final StringProperty companyNameProperty() {
        return this.companyName;
    }

    public String getCompanyName() {
        return companyNameProperty().get();
    }

    public void setCompanyName(String companyName) {
        companyNameProperty().set(companyName);
    }

    public final LongProperty salaryProperty() {
        return this.salary;
    }

    public Long getSalary() {
        return salaryProperty().get();
    }

    public void setSalary(Long salary) {
        salaryProperty().set(salary);
    }

    public final StringProperty seniorContactNumberProperty() {
        return this.seniorContactNumber;
    }

    public String getSeniorContactNumber() {
        return seniorContactNumberProperty().get();
    }

    public void setSeniorContactNumber(String seniorContactNumber) {
        seniorContactNumberProperty().set(seniorContactNumber);
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

    public final StringProperty dropReasonsProperty() {
        return this.dropReasons;
    }

    public String getDropReasons() {
        return dropReasonsProperty().get();
    }

    public void setDropReasons(String dropReasons) {
        dropReasonsProperty().set(dropReasons);
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

    public final ObjectProperty<Date> lasUpdateDateProperty() {
        return this.lasUpdateDate;
    }

    public Date getLasUpdateDate() {
        return lasUpdateDateProperty().get();
    }

    public void setLasUpdateDate(Date lasUpdateDate) {
        lasUpdateDateProperty().set(lasUpdateDate);
    }

}
