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
 * TblReservationCanceled generated by hbm2java
 */
public class TblReservationCanceled implements java.io.Serializable {

    private final LongProperty idcancel;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblReservation> tblReservation;
    private final StringProperty codeCancel;
    private final ObjectProperty<Date> cancelDateTime;
    private final StringProperty cancelNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblReservationCanceled() {
        this.idcancel = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblReservation = new SimpleObjectProperty<>();
        this.codeCancel = new SimpleStringProperty();
        this.cancelDateTime = new SimpleObjectProperty<>();
        this.cancelNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblReservationCanceled(long idcancel) {
        this();
        idcancelProperty().set(idcancel);
    }

    public TblReservationCanceled(long idcancel, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblReservation tblReservation, String codeCancel, Date cancelDateTime, String cancelNote, Date createDate, Date lastUpdateDate) {
        this();
        idcancelProperty().set(idcancel);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblReservationProperty().set(tblReservation);
        codeCancelProperty().set(codeCancel);
        cancelDateTimeProperty().set(cancelDateTime);
        cancelNoteProperty().set(cancelNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblReservationCanceled(TblReservationCanceled tblReservationCanceled) {
        this();
        idcancelProperty().set(tblReservationCanceled.getIdcancel());
        refRecordStatusProperty().set(tblReservationCanceled.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblReservationCanceled.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblReservationCanceled.getTblEmployeeByCreateBy());
        tblReservationProperty().set(tblReservationCanceled.getTblReservation());
        codeCancelProperty().set(tblReservationCanceled.getCodeCancel());
        cancelDateTimeProperty().set(tblReservationCanceled.getCancelDateTime());
        cancelNoteProperty().set(tblReservationCanceled.getCancelNote());
        createDateProperty().set(tblReservationCanceled.getCreateDate());
        lastUpdateDateProperty().set(tblReservationCanceled.getLastUpdateDate());

    }

    public final LongProperty idcancelProperty() {
        return this.idcancel;
    }

    public long getIdcancel() {
        return idcancelProperty().get();
    }

    public void setIdcancel(long idcancel) {
        idcancelProperty().set(idcancel);
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

    public final ObjectProperty<TblReservation> tblReservationProperty() {
        return this.tblReservation;
    }

    public TblReservation getTblReservation() {
        return tblReservationProperty().get();
    }

    public void setTblReservation(TblReservation tblReservation) {
        tblReservationProperty().set(tblReservation);
    }

    public final StringProperty codeCancelProperty() {
        return this.codeCancel;
    }

    public String getCodeCancel() {
        return codeCancelProperty().get();
    }

    public void setCodeCancel(String codeCancel) {
        codeCancelProperty().set(codeCancel);
    }

    public final ObjectProperty<Date> cancelDateTimeProperty() {
        return this.cancelDateTime;
    }

    public Date getCancelDateTime() {
        return cancelDateTimeProperty().get();
    }

    public void setCancelDateTime(Date cancelDateTime) {
        cancelDateTimeProperty().set(cancelDateTime);
    }

    public final StringProperty cancelNoteProperty() {
        return this.cancelNote;
    }

    public String getCancelNote() {
        return cancelNoteProperty().get();
    }

    public void setCancelNote(String cancelNote) {
        cancelNoteProperty().set(cancelNote);
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
