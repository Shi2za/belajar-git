package hotelfx.persistence.model;
// Generated Sep 29, 2017 12:46:59 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblReservationPaymentWithDeposit generated by hbm2java
 */
public class TblReservationPaymentWithDeposit implements java.io.Serializable {

    private final LongProperty iddetail;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblReservationCheckInOut> tblReservationCheckInOut;
    private final ObjectProperty<TblReservationPayment> tblReservationPayment;
    private final StringProperty codeDeposit;
    private final StringProperty depositNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    public TblReservationPaymentWithDeposit() {
        this.iddetail = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblReservationCheckInOut = new SimpleObjectProperty<>();
        this.tblReservationPayment = new SimpleObjectProperty<>();
        this.codeDeposit = new SimpleStringProperty();
        this.depositNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

    }

    public TblReservationPaymentWithDeposit(long iddetail) {
        this();
        iddetailProperty().set(iddetail);
    }

    public TblReservationPaymentWithDeposit(long iddetail, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblReservationCheckInOut tblReservationCheckInOut, TblReservationPayment tblReservationPayment, String codeDeposit, String depositNote, Date createDate, Date lastUpdateDate) {
        this();
        iddetailProperty().set(iddetail);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblReservationCheckInOutProperty().set(tblReservationCheckInOut);
        tblReservationPaymentProperty().set(tblReservationPayment);
        codeDepositProperty().set(codeDeposit);
        depositNoteProperty().set(depositNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

    }

    public TblReservationPaymentWithDeposit(TblReservationPaymentWithDeposit tblReservationPaymentWithDeposit) {
        this();
        iddetailProperty().set(tblReservationPaymentWithDeposit.getIddetail());
        refRecordStatusProperty().set(tblReservationPaymentWithDeposit.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblReservationPaymentWithDeposit.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblReservationPaymentWithDeposit.getTblEmployeeByLastUpdateBy());
        tblReservationCheckInOutProperty().set(tblReservationPaymentWithDeposit.getTblReservationCheckInOut());
        tblReservationPaymentProperty().set(tblReservationPaymentWithDeposit.getTblReservationPayment());
        codeDepositProperty().set(tblReservationPaymentWithDeposit.getCodeDeposit());
        depositNoteProperty().set(tblReservationPaymentWithDeposit.getDepositNote());
        createDateProperty().set(tblReservationPaymentWithDeposit.getCreateDate());
        lastUpdateDateProperty().set(tblReservationPaymentWithDeposit.getLastUpdateDate());

    }

    public final LongProperty iddetailProperty() {
        return this.iddetail;
    }

    public long getIddetail() {
        return iddetailProperty().get();
    }

    public void setIddetail(long iddetail) {
        iddetailProperty().set(iddetail);
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

    public final ObjectProperty<TblReservationCheckInOut> tblReservationCheckInOutProperty() {
        return this.tblReservationCheckInOut;
    }

    public TblReservationCheckInOut getTblReservationCheckInOut() {
        return tblReservationCheckInOutProperty().get();
    }

    public void setTblReservationCheckInOut(TblReservationCheckInOut tblReservationCheckInOut) {
        tblReservationCheckInOutProperty().set(tblReservationCheckInOut);
    }

    public final ObjectProperty<TblReservationPayment> tblReservationPaymentProperty() {
        return this.tblReservationPayment;
    }

    public TblReservationPayment getTblReservationPayment() {
        return tblReservationPaymentProperty().get();
    }

    public void setTblReservationPayment(TblReservationPayment tblReservationPayment) {
        tblReservationPaymentProperty().set(tblReservationPayment);
    }

    public final StringProperty codeDepositProperty() {
        return this.codeDeposit;
    }

    public String getCodeDeposit() {
        return codeDepositProperty().get();
    }

    public void setCodeDeposit(String codeDeposit) {
        codeDepositProperty().set(codeDeposit);
    }

    public final StringProperty depositNoteProperty() {
        return this.depositNote;
    }

    public String getDepositNote() {
        return depositNoteProperty().get();
    }

    public void setDepositNote(String depositNote) {
        depositNoteProperty().set(depositNote);
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
