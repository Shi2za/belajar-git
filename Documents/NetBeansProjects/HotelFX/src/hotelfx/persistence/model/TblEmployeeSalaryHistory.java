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
 * TblEmployeeSalaryHistory generated by hbm2java
 */
public class TblEmployeeSalaryHistory implements java.io.Serializable {

    private final LongProperty idsalaryHistory;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdemployee;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblJob> tblJob;
    private final StringProperty codeSalaryHistory;
    private final ObjectProperty<Date> changedDate;
    private final StringProperty codeChange;
    private final LongProperty salary;
    private final StringProperty historyNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblEmployeeSalaryHistory() {
        this.idsalaryHistory = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByIdemployee = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblJob = new SimpleObjectProperty<>();
        this.codeSalaryHistory = new SimpleStringProperty();
        this.changedDate = new SimpleObjectProperty<>();
        this.codeChange = new SimpleStringProperty();
        this.salary = new SimpleLongProperty();
        this.historyNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblEmployeeSalaryHistory(long idsalaryHistory) {
        this();
        idsalaryHistoryProperty().set(idsalaryHistory);
    }

    public TblEmployeeSalaryHistory(long idsalaryHistory, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByIdemployee, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblJob tblJob, String codeSalaryHistory, Date changedDate, String codeChange, Long salary, String historyNote, Date createDate, Date lastUpdateDate) {
        this();
        idsalaryHistoryProperty().set(idsalaryHistory);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblJobProperty().set(tblJob);
        codeSalaryHistoryProperty().set(codeSalaryHistory);
        changedDateProperty().set(changedDate);
        codeChangeProperty().set(codeChange);
        salaryProperty().set(salary);
        historyNoteProperty().set(historyNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblEmployeeSalaryHistory(TblEmployeeSalaryHistory tblEmployeeSalaryHistory) {
        this();
        idsalaryHistoryProperty().set(tblEmployeeSalaryHistory.getIdsalaryHistory());
        refRecordStatusProperty().set(tblEmployeeSalaryHistory.getRefRecordStatus());
        tblEmployeeByIdemployeeProperty().set(tblEmployeeSalaryHistory.getTblEmployeeByIdemployee());
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeSalaryHistory.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblEmployeeSalaryHistory.getTblEmployeeByCreateBy());
        tblJobProperty().set(tblEmployeeSalaryHistory.getTblJob());
        codeSalaryHistoryProperty().set(tblEmployeeSalaryHistory.getCodeSalaryHistory());
        changedDateProperty().set(tblEmployeeSalaryHistory.getChangedDate());
        codeChangeProperty().set(tblEmployeeSalaryHistory.getCodeChange());
        salaryProperty().set(tblEmployeeSalaryHistory.getSalary());
        historyNoteProperty().set(tblEmployeeSalaryHistory.getHistoryNote());
        createDateProperty().set(tblEmployeeSalaryHistory.getCreateDate());
        lastUpdateDateProperty().set(tblEmployeeSalaryHistory.getLastUpdateDate());

    }

    public final LongProperty idsalaryHistoryProperty() {
        return this.idsalaryHistory;
    }

    public long getIdsalaryHistory() {
        return idsalaryHistoryProperty().get();
    }

    public void setIdsalaryHistory(long idsalaryHistory) {
        idsalaryHistoryProperty().set(idsalaryHistory);
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

    public final ObjectProperty<TblJob> tblJobProperty() {
        return this.tblJob;
    }

    public TblJob getTblJob() {
        return tblJobProperty().get();
    }

    public void setTblJob(TblJob tblJob) {
        tblJobProperty().set(tblJob);
    }

    public final StringProperty codeSalaryHistoryProperty() {
        return this.codeSalaryHistory;
    }

    public String getCodeSalaryHistory() {
        return codeSalaryHistoryProperty().get();
    }

    public void setCodeSalaryHistory(String codeSalaryHistory) {
        codeSalaryHistoryProperty().set(codeSalaryHistory);
    }

    public final ObjectProperty<Date> changedDateProperty() {
        return this.changedDate;
    }

    public Date getChangedDate() {
        return changedDateProperty().get();
    }

    public void setChangedDate(Date changedDate) {
        changedDateProperty().set(changedDate);
    }

    public final StringProperty codeChangeProperty() {
        return this.codeChange;
    }

    public String getCodeChange() {
        return codeChangeProperty().get();
    }

    public void setCodeChange(String codeChange) {
        codeChangeProperty().set(codeChange);
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

    public final StringProperty historyNoteProperty() {
        return this.historyNote;
    }

    public String getHistoryNote() {
        return historyNoteProperty().get();
    }

    public void setHistoryNote(String historyNote) {
        historyNoteProperty().set(historyNote);
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