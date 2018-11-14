package hotelfx.persistence.model;
// Generated Jul 9, 2018 4:37:20 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * TblHotelReceivable generated by hbm2java
 */
public class TblHotelReceivable implements java.io.Serializable {

    private final LongProperty idhotelReceivable;
    private final ObjectProperty<RefFinanceTransactionStatus> refFinanceTransactionStatus;
    private final ObjectProperty<RefHotelReceivableType> refHotelReceivableType;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblHotelInvoice> tblHotelInvoice;
    private final StringProperty codeHotelReceivable;
    private final ObjectProperty<BigDecimal> hotelReceivableNominal;
    private final StringProperty hotelReceivableNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblReturs;
    private Set tblHotelFinanceTransactionHotelReceivables;
    private Set tblReservationPaymentWithGuaranteePayments;

    public TblHotelReceivable() {
        this.idhotelReceivable = new SimpleLongProperty();
        this.refFinanceTransactionStatus = new SimpleObjectProperty<>();
        this.refHotelReceivableType = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblHotelInvoice = new SimpleObjectProperty<>();
        this.codeHotelReceivable = new SimpleStringProperty();
        this.hotelReceivableNominal = new SimpleObjectProperty<>();
        this.hotelReceivableNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblReturs = new HashSet(0);
        this.tblHotelFinanceTransactionHotelReceivables = new HashSet(0);
        this.tblReservationPaymentWithGuaranteePayments = new HashSet(0);
    }

    public TblHotelReceivable(long idhotelReceivable) {
        this();
        idhotelReceivableProperty().set(idhotelReceivable);
    }

    public TblHotelReceivable(long idhotelReceivable, RefFinanceTransactionStatus refFinanceTransactionStatus, RefHotelReceivableType refHotelReceivableType, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblHotelInvoice tblHotelInvoice, String codeHotelReceivable, BigDecimal hotelReceivableNominal, String hotelReceivableNote, Date createDate, Date lastUpdateDate, Set tblReturs, Set tblHotelFinanceTransactionHotelReceivables, Set tblReservationPaymentWithGuaranteePayments) {
        this();
        idhotelReceivableProperty().set(idhotelReceivable);
        refFinanceTransactionStatusProperty().set(refFinanceTransactionStatus);
        refHotelReceivableTypeProperty().set(refHotelReceivableType);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblHotelInvoiceProperty().set(tblHotelInvoice);
        codeHotelReceivableProperty().set(codeHotelReceivable);
        hotelReceivableNominalProperty().set(hotelReceivableNominal);
        hotelReceivableNoteProperty().set(hotelReceivableNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblReturs = tblReturs;
        this.tblHotelFinanceTransactionHotelReceivables = tblHotelFinanceTransactionHotelReceivables;
        this.tblReservationPaymentWithGuaranteePayments = tblReservationPaymentWithGuaranteePayments;
    }

    public TblHotelReceivable(TblHotelReceivable tblHotelReceivable) {
        this();
        idhotelReceivableProperty().set(tblHotelReceivable.getIdhotelReceivable());
        refFinanceTransactionStatusProperty().set(tblHotelReceivable.getRefFinanceTransactionStatus());
        refHotelReceivableTypeProperty().set(tblHotelReceivable.getRefHotelReceivableType());
        refRecordStatusProperty().set(tblHotelReceivable.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblHotelReceivable.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblHotelReceivable.getTblEmployeeByLastUpdateBy());
        tblHotelInvoiceProperty().set(tblHotelReceivable.getTblHotelInvoice());
        codeHotelReceivableProperty().set(tblHotelReceivable.getCodeHotelReceivable());
        hotelReceivableNominalProperty().set(tblHotelReceivable.getHotelReceivableNominal());
        hotelReceivableNoteProperty().set(tblHotelReceivable.getHotelReceivableNote());
        createDateProperty().set(tblHotelReceivable.getCreateDate());
        lastUpdateDateProperty().set(tblHotelReceivable.getLastUpdateDate());

        this.tblReturs = tblHotelReceivable.getTblReturs();
        this.tblHotelFinanceTransactionHotelReceivables = tblHotelReceivable.getTblHotelFinanceTransactionHotelReceivables();
        this.tblReservationPaymentWithGuaranteePayments = tblHotelReceivable.getTblReservationPaymentWithGuaranteePayments();
    }

    public final LongProperty idhotelReceivableProperty() {
        return this.idhotelReceivable;
    }

    public long getIdhotelReceivable() {
        return idhotelReceivableProperty().get();
    }

    public void setIdhotelReceivable(long idhotelReceivable) {
        idhotelReceivableProperty().set(idhotelReceivable);
    }

    public final ObjectProperty<RefFinanceTransactionStatus> refFinanceTransactionStatusProperty() {
        return this.refFinanceTransactionStatus;
    }

    public RefFinanceTransactionStatus getRefFinanceTransactionStatus() {
        return refFinanceTransactionStatusProperty().get();
    }

    public void setRefFinanceTransactionStatus(RefFinanceTransactionStatus refFinanceTransactionStatus) {
        refFinanceTransactionStatusProperty().set(refFinanceTransactionStatus);
    }

    public final ObjectProperty<RefHotelReceivableType> refHotelReceivableTypeProperty() {
        return this.refHotelReceivableType;
    }

    public RefHotelReceivableType getRefHotelReceivableType() {
        return refHotelReceivableTypeProperty().get();
    }

    public void setRefHotelReceivableType(RefHotelReceivableType refHotelReceivableType) {
        refHotelReceivableTypeProperty().set(refHotelReceivableType);
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

    public final ObjectProperty<TblHotelInvoice> tblHotelInvoiceProperty() {
        return this.tblHotelInvoice;
    }

    public TblHotelInvoice getTblHotelInvoice() {
        return tblHotelInvoiceProperty().get();
    }

    public void setTblHotelInvoice(TblHotelInvoice tblHotelInvoice) {
        tblHotelInvoiceProperty().set(tblHotelInvoice);
    }

    public final StringProperty codeHotelReceivableProperty() {
        return this.codeHotelReceivable;
    }

    public String getCodeHotelReceivable() {
        return codeHotelReceivableProperty().get();
    }

    public void setCodeHotelReceivable(String codeHotelReceivable) {
        codeHotelReceivableProperty().set(codeHotelReceivable);
    }

    public final ObjectProperty<BigDecimal> hotelReceivableNominalProperty() {
        return this.hotelReceivableNominal;
    }

    public BigDecimal getHotelReceivableNominal() {
        return hotelReceivableNominalProperty().get();
    }

    public void setHotelReceivableNominal(BigDecimal hotelReceivableNominal) {
        hotelReceivableNominalProperty().set(hotelReceivableNominal);
    }

    public final StringProperty hotelReceivableNoteProperty() {
        return this.hotelReceivableNote;
    }

    public String getHotelReceivableNote() {
        return hotelReceivableNoteProperty().get();
    }

    public void setHotelReceivableNote(String hotelReceivableNote) {
        hotelReceivableNoteProperty().set(hotelReceivableNote);
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

    public Set getTblReturs() {
        return this.tblReturs;
    }

    public void setTblReturs(Set tblReturs) {
        this.tblReturs = tblReturs;
    }

    public Set getTblHotelFinanceTransactionHotelReceivables() {
        return this.tblHotelFinanceTransactionHotelReceivables;
    }

    public void setTblHotelFinanceTransactionHotelReceivables(Set tblHotelFinanceTransactionHotelReceivables) {
        this.tblHotelFinanceTransactionHotelReceivables = tblHotelFinanceTransactionHotelReceivables;
    }

    public Set getTblReservationPaymentWithGuaranteePayments() {
        return this.tblReservationPaymentWithGuaranteePayments;
    }

    public void setTblReservationPaymentWithGuaranteePayments(Set tblReservationPaymentWithGuaranteePayments) {
        this.tblReservationPaymentWithGuaranteePayments = tblReservationPaymentWithGuaranteePayments;
    }

}