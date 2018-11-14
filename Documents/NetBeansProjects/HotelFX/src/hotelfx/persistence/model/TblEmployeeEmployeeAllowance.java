package hotelfx.persistence.model;
// Generated Apr 27, 2018 3:35:28 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblEmployeeEmployeeAllowance generated by hbm2java
 */
public class TblEmployeeEmployeeAllowance implements java.io.Serializable {

    private final LongProperty idrelation;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByIdemployee;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployeeAllowance> tblEmployeeAllowance;
    private final ObjectProperty<BigDecimal> allowanceNominal;
    private final StringProperty allowanceNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblEmployeeEmployeeAllowance() {
        this.idrelation = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByIdemployee = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeAllowance = new SimpleObjectProperty<>();
        this.allowanceNominal = new SimpleObjectProperty<>();
        this.allowanceNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblEmployeeEmployeeAllowance(long idrelation) {
        this();
        idrelationProperty().set(idrelation);
    }

    public TblEmployeeEmployeeAllowance(long idrelation, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByIdemployee, TblEmployee tblEmployeeByLastUpdateBy, TblEmployeeAllowance tblEmployeeAllowance, BigDecimal allowanceNominal, String allowanceNote, Date createDate, Date lastUpdateDate) {
        this();
        idrelationProperty().set(idrelation);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByIdemployeeProperty().set(tblEmployeeByIdemployee);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeAllowanceProperty().set(tblEmployeeAllowance);
        allowanceNominalProperty().set(allowanceNominal);
        allowanceNoteProperty().set(allowanceNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblEmployeeEmployeeAllowance(TblEmployeeEmployeeAllowance tblEmployeeEmployeeAllowance) {
        this();
        idrelationProperty().set(tblEmployeeEmployeeAllowance.getIdrelation());
        refRecordStatusProperty().set(tblEmployeeEmployeeAllowance.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblEmployeeEmployeeAllowance.getTblEmployeeByCreateBy());
        tblEmployeeByIdemployeeProperty().set(tblEmployeeEmployeeAllowance.getTblEmployeeByIdemployee());
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeEmployeeAllowance.getTblEmployeeByLastUpdateBy());
        tblEmployeeAllowanceProperty().set(tblEmployeeEmployeeAllowance.getTblEmployeeAllowance());
        allowanceNominalProperty().set(tblEmployeeEmployeeAllowance.getAllowanceNominal());
        allowanceNoteProperty().set(tblEmployeeEmployeeAllowance.getAllowanceNote());
        createDateProperty().set(tblEmployeeEmployeeAllowance.getCreateDate());
        lastUpdateDateProperty().set(tblEmployeeEmployeeAllowance.getLastUpdateDate());

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

    public final ObjectProperty<TblEmployee> tblEmployeeByCreateByProperty() {
        return this.tblEmployeeByCreateBy;
    }

    public TblEmployee getTblEmployeeByCreateBy() {
        return tblEmployeeByCreateByProperty().get();
    }

    public void setTblEmployeeByCreateBy(TblEmployee tblEmployeeByCreateBy) {
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
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

    public final ObjectProperty<TblEmployeeAllowance> tblEmployeeAllowanceProperty() {
        return this.tblEmployeeAllowance;
    }

    public TblEmployeeAllowance getTblEmployeeAllowance() {
        return tblEmployeeAllowanceProperty().get();
    }

    public void setTblEmployeeAllowance(TblEmployeeAllowance tblEmployeeAllowance) {
        tblEmployeeAllowanceProperty().set(tblEmployeeAllowance);
    }

    public final ObjectProperty<BigDecimal> allowanceNominalProperty() {
        return this.allowanceNominal;
    }

    public BigDecimal getAllowanceNominal() {
        return allowanceNominalProperty().get();
    }

    public void setAllowanceNominal(BigDecimal allowanceNominal) {
        allowanceNominalProperty().set(allowanceNominal);
    }

    public final StringProperty allowanceNoteProperty() {
        return this.allowanceNote;
    }

    public String getAllowanceNote() {
        return allowanceNoteProperty().get();
    }

    public void setAllowanceNote(String allowanceNote) {
        allowanceNoteProperty().set(allowanceNote);
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
