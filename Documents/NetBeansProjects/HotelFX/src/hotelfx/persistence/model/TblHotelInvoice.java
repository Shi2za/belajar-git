package hotelfx.persistence.model;
// Generated Jul 9, 2018 1:46:04 PM by Hibernate Tools 4.3.1

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
 * TblHotelInvoice generated by hbm2java
 */
public class TblHotelInvoice implements java.io.Serializable {

    private final LongProperty idhotelInvoice;
    private final ObjectProperty<RefHotelInvoiceType> refHotelInvoiceType;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblPartner> tblPartner;
    private final ObjectProperty<TblSupplier> tblSupplier;
    private final StringProperty codeHotelInvoice;
    private final StringProperty hotelInvoiceSubject;
    private final ObjectProperty<Date> issueDate;
    private final ObjectProperty<Date> dueDate;
    private final StringProperty hotelInvoiceNote;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblHotelPayables;
    private Set tblHotelReceivables;

    public TblHotelInvoice() {
        this.idhotelInvoice = new SimpleLongProperty();
        this.refHotelInvoiceType = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblPartner = new SimpleObjectProperty<>();
        this.tblSupplier = new SimpleObjectProperty<>();
        this.codeHotelInvoice = new SimpleStringProperty();
        this.hotelInvoiceSubject = new SimpleStringProperty();
        this.issueDate = new SimpleObjectProperty<>();
        this.dueDate = new SimpleObjectProperty<>();
        this.hotelInvoiceNote = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblHotelPayables = new HashSet(0);
        this.tblHotelReceivables = new HashSet(0);
    }

    public TblHotelInvoice(long idhotelInvoice) {
        this();
        idhotelInvoiceProperty().set(idhotelInvoice);
    }

    public TblHotelInvoice(long idhotelInvoice, RefHotelInvoiceType refHotelInvoiceType, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblPartner tblPartner, TblSupplier tblSupplier, String codeHotelInvoice, String hotelInvoiceSubject, Date issueDate, Date dueDate, String hotelInvoiceNote, Date createDate, Date lastUpdateDate, Set tblHotelPayables, Set tblHotelReceivables) {
        this();
        idhotelInvoiceProperty().set(idhotelInvoice);
        refHotelInvoiceTypeProperty().set(refHotelInvoiceType);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblPartnerProperty().set(tblPartner);
        tblSupplierProperty().set(tblSupplier);
        codeHotelInvoiceProperty().set(codeHotelInvoice);
        hotelInvoiceSubjectProperty().set(hotelInvoiceSubject);
        issueDateProperty().set(issueDate);
        dueDateProperty().set(dueDate);
        hotelInvoiceNoteProperty().set(hotelInvoiceNote);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblHotelPayables = tblHotelPayables;
        this.tblHotelReceivables = tblHotelReceivables;
    }

    public TblHotelInvoice(TblHotelInvoice tblHotelInvoice) {
        this();
        idhotelInvoiceProperty().set(tblHotelInvoice.getIdhotelInvoice());
        refHotelInvoiceTypeProperty().set(tblHotelInvoice.getRefHotelInvoiceType());
        refRecordStatusProperty().set(tblHotelInvoice.getRefRecordStatus());
        tblEmployeeByCreateByProperty().set(tblHotelInvoice.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblHotelInvoice.getTblEmployeeByLastUpdateBy());
        tblPartnerProperty().set(tblHotelInvoice.getTblPartner());
        tblSupplierProperty().set(tblHotelInvoice.getTblSupplier());
        codeHotelInvoiceProperty().set(tblHotelInvoice.getCodeHotelInvoice());
        hotelInvoiceSubjectProperty().set(tblHotelInvoice.getHotelInvoiceSubject());
        issueDateProperty().set(tblHotelInvoice.getIssueDate());
        dueDateProperty().set(tblHotelInvoice.getDueDate());
        hotelInvoiceNoteProperty().set(tblHotelInvoice.getHotelInvoiceNote());
        createDateProperty().set(tblHotelInvoice.getCreateDate());
        lastUpdateDateProperty().set(tblHotelInvoice.getLastUpdateDate());

        this.tblHotelPayables = tblHotelInvoice.getTblHotelPayables();
        this.tblHotelReceivables = tblHotelInvoice.getTblHotelReceivables();
    }

    public final LongProperty idhotelInvoiceProperty() {
        return this.idhotelInvoice;
    }

    public long getIdhotelInvoice() {
        return idhotelInvoiceProperty().get();
    }

    public void setIdhotelInvoice(long idhotelInvoice) {
        idhotelInvoiceProperty().set(idhotelInvoice);
    }

    public final ObjectProperty<RefHotelInvoiceType> refHotelInvoiceTypeProperty() {
        return this.refHotelInvoiceType;
    }

    public RefHotelInvoiceType getRefHotelInvoiceType() {
        return refHotelInvoiceTypeProperty().get();
    }

    public void setRefHotelInvoiceType(RefHotelInvoiceType refHotelInvoiceType) {
        refHotelInvoiceTypeProperty().set(refHotelInvoiceType);
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

    public final ObjectProperty<TblPartner> tblPartnerProperty() {
        return this.tblPartner;
    }

    public TblPartner getTblPartner() {
        return tblPartnerProperty().get();
    }

    public void setTblPartner(TblPartner tblPartner) {
        tblPartnerProperty().set(tblPartner);
    }

    public final ObjectProperty<TblSupplier> tblSupplierProperty() {
        return this.tblSupplier;
    }

    public TblSupplier getTblSupplier() {
        return tblSupplierProperty().get();
    }

    public void setTblSupplier(TblSupplier tblSupplier) {
        tblSupplierProperty().set(tblSupplier);
    }

    public final StringProperty codeHotelInvoiceProperty() {
        return this.codeHotelInvoice;
    }

    public String getCodeHotelInvoice() {
        return codeHotelInvoiceProperty().get();
    }

    public void setCodeHotelInvoice(String codeHotelInvoice) {
        codeHotelInvoiceProperty().set(codeHotelInvoice);
    }

    public final StringProperty hotelInvoiceSubjectProperty() {
        return this.hotelInvoiceSubject;
    }

    public String getHotelInvoiceSubject() {
        return hotelInvoiceSubjectProperty().get();
    }

    public void setHotelInvoiceSubject(String hotelInvoiceSubject) {
        hotelInvoiceSubjectProperty().set(hotelInvoiceSubject);
    }

    public final ObjectProperty<Date> issueDateProperty() {
        return this.issueDate;
    }

    public Date getIssueDate() {
        return issueDateProperty().get();
    }

    public void setIssueDate(Date issueDate) {
        issueDateProperty().set(issueDate);
    }

    public final ObjectProperty<Date> dueDateProperty() {
        return this.dueDate;
    }

    public Date getDueDate() {
        return dueDateProperty().get();
    }

    public void setDueDate(Date dueDate) {
        dueDateProperty().set(dueDate);
    }

    public final StringProperty hotelInvoiceNoteProperty() {
        return this.hotelInvoiceNote;
    }

    public String getHotelInvoiceNote() {
        return hotelInvoiceNoteProperty().get();
    }

    public void setHotelInvoiceNote(String hotelInvoiceNote) {
        hotelInvoiceNoteProperty().set(hotelInvoiceNote);
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

    public Set getTblHotelPayables() {
        return this.tblHotelPayables;
    }

    public void setTblHotelPayables(Set tblHotelPayables) {
        this.tblHotelPayables = tblHotelPayables;
    }

    public Set getTblHotelReceivables() {
        return this.tblHotelReceivables;
    }

    public void setTblHotelReceivables(Set tblHotelReceivables) {
        this.tblHotelReceivables = tblHotelReceivables;
    }

}
