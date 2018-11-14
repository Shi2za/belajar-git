package hotelfx.persistence.model;
// Generated Apr 9, 2018 10:55:23 AM by Hibernate Tools 4.3.1

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
 * TblSupplier generated by hbm2java
 */
public class TblSupplier implements java.io.Serializable {

    private final LongProperty idsupplier;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblLocation> tblLocation;
    private final StringProperty codeSupplier;
    private final StringProperty supplierName;
    private final StringProperty supplierAddress;
    private final StringProperty supplierPhoneNumber;
    private final StringProperty supplierFax;
    private final StringProperty supplierEmailAddress;
    private final StringProperty supplierWebsite;
    private final StringProperty picname;
    private final StringProperty picphoneNumber;
    private final StringProperty picemailAddress;
    private final StringProperty supplierInfo;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblMemorandumInvoices;
    private Set tblSupplierBankAccounts;
    private Set tblHotelInvoices;
    private Set tblPurchaseRequests;
    private Set tblReturs;
    private Set tblSupplierItems;
    private Set tblPurchaseOrders;

    public TblSupplier() {
        this.idsupplier = new SimpleLongProperty();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblLocation = new SimpleObjectProperty<>();
        this.codeSupplier = new SimpleStringProperty();
        this.supplierName = new SimpleStringProperty();
        this.supplierAddress = new SimpleStringProperty();
        this.supplierPhoneNumber = new SimpleStringProperty();
        this.supplierFax = new SimpleStringProperty();
        this.supplierEmailAddress = new SimpleStringProperty();
        this.supplierWebsite = new SimpleStringProperty();
        this.picname = new SimpleStringProperty();
        this.picphoneNumber = new SimpleStringProperty();
        this.picemailAddress = new SimpleStringProperty();
        this.supplierInfo = new SimpleStringProperty();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblMemorandumInvoices = new HashSet(0);
        this.tblSupplierBankAccounts = new HashSet(0);
        this.tblHotelInvoices = new HashSet(0);
        this.tblPurchaseRequests = new HashSet(0);
        this.tblReturs = new HashSet(0);
        this.tblSupplierItems = new HashSet(0);
        this.tblPurchaseOrders = new HashSet(0);
    }

    public TblSupplier(long idsupplier) {
        this();
        idsupplierProperty().set(idsupplier);
    }

    public TblSupplier(long idsupplier, RefRecordStatus refRecordStatus, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreateBy, TblLocation tblLocation, String codeSupplier, String supplierName, String supplierAddress, String supplierPhoneNumber, String supplierFax, String supplierEmailAddress, String supplierWebsite, String picname, String picphoneNumber, String picemailAddress, String supplierInfo, Date createDate, Date lastUpdateDate, Set tblMemorandumInvoices, Set tblSupplierBankAccounts, Set tblHotelInvoices, Set tblPurchaseRequests, Set tblReturs, Set tblSupplierItems, Set tblPurchaseOrders) {
        this();
        idsupplierProperty().set(idsupplier);
        refRecordStatusProperty().set(refRecordStatus);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblLocationProperty().set(tblLocation);
        codeSupplierProperty().set(codeSupplier);
        supplierNameProperty().set(supplierName);
        supplierAddressProperty().set(supplierAddress);
        supplierPhoneNumberProperty().set(supplierPhoneNumber);
        supplierFaxProperty().set(supplierFax);
        supplierEmailAddressProperty().set(supplierEmailAddress);
        supplierWebsiteProperty().set(supplierWebsite);
        picnameProperty().set(picname);
        picphoneNumberProperty().set(picphoneNumber);
        picemailAddressProperty().set(picemailAddress);
        supplierInfoProperty().set(supplierInfo);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblMemorandumInvoices = tblMemorandumInvoices;
        this.tblSupplierBankAccounts = tblSupplierBankAccounts;
        this.tblHotelInvoices = tblHotelInvoices;
        this.tblPurchaseRequests = tblPurchaseRequests;
        this.tblReturs = tblReturs;
        this.tblSupplierItems = tblSupplierItems;
        this.tblPurchaseOrders = tblPurchaseOrders;
    }

    public TblSupplier(TblSupplier tblSupplier) {
        this();
        idsupplierProperty().set(tblSupplier.getIdsupplier());
        refRecordStatusProperty().set(tblSupplier.getRefRecordStatus());
        tblEmployeeByLastUpdateByProperty().set(tblSupplier.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreateByProperty().set(tblSupplier.getTblEmployeeByCreateBy());
        tblLocationProperty().set(tblSupplier.getTblLocation());
        codeSupplierProperty().set(tblSupplier.getCodeSupplier());
        supplierNameProperty().set(tblSupplier.getSupplierName());
        supplierAddressProperty().set(tblSupplier.getSupplierAddress());
        supplierPhoneNumberProperty().set(tblSupplier.getSupplierPhoneNumber());
        supplierFaxProperty().set(tblSupplier.getSupplierFax());
        supplierEmailAddressProperty().set(tblSupplier.getSupplierEmailAddress());
        supplierWebsiteProperty().set(tblSupplier.getSupplierWebsite());
        picnameProperty().set(tblSupplier.getPicname());
        picphoneNumberProperty().set(tblSupplier.getPicphoneNumber());
        picemailAddressProperty().set(tblSupplier.getPicemailAddress());
        supplierInfoProperty().set(tblSupplier.getSupplierInfo());
        createDateProperty().set(tblSupplier.getCreateDate());
        lastUpdateDateProperty().set(tblSupplier.getLastUpdateDate());

        this.tblMemorandumInvoices = tblSupplier.getTblMemorandumInvoices();
        this.tblSupplierBankAccounts = tblSupplier.getTblSupplierBankAccounts();
        this.tblHotelInvoices = tblSupplier.getTblHotelInvoices();
        this.tblPurchaseRequests = tblSupplier.getTblPurchaseRequests();
        this.tblReturs = tblSupplier.getTblReturs();
        this.tblSupplierItems = tblSupplier.getTblSupplierItems();
        this.tblPurchaseOrders = tblSupplier.getTblPurchaseOrders();
    }

    public final LongProperty idsupplierProperty() {
        return this.idsupplier;
    }

    public long getIdsupplier() {
        return idsupplierProperty().get();
    }

    public void setIdsupplier(long idsupplier) {
        idsupplierProperty().set(idsupplier);
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

    public final StringProperty codeSupplierProperty() {
        return this.codeSupplier;
    }

    public String getCodeSupplier() {
        return codeSupplierProperty().get();
    }

    public void setCodeSupplier(String codeSupplier) {
        codeSupplierProperty().set(codeSupplier);
    }

    public final StringProperty supplierNameProperty() {
        return this.supplierName;
    }

    public String getSupplierName() {
        return supplierNameProperty().get();
    }

    public void setSupplierName(String supplierName) {
        supplierNameProperty().set(supplierName);
    }

    public final StringProperty supplierAddressProperty() {
        return this.supplierAddress;
    }

    public String getSupplierAddress() {
        return supplierAddressProperty().get();
    }

    public void setSupplierAddress(String supplierAddress) {
        supplierAddressProperty().set(supplierAddress);
    }

    public final StringProperty supplierPhoneNumberProperty() {
        return this.supplierPhoneNumber;
    }

    public String getSupplierPhoneNumber() {
        return supplierPhoneNumberProperty().get();
    }

    public void setSupplierPhoneNumber(String supplierPhoneNumber) {
        supplierPhoneNumberProperty().set(supplierPhoneNumber);
    }

    public final StringProperty supplierFaxProperty() {
        return this.supplierFax;
    }

    public String getSupplierFax() {
        return supplierFaxProperty().get();
    }

    public void setSupplierFax(String supplierFax) {
        supplierFaxProperty().set(supplierFax);
    }

    public final StringProperty supplierEmailAddressProperty() {
        return this.supplierEmailAddress;
    }

    public String getSupplierEmailAddress() {
        return supplierEmailAddressProperty().get();
    }

    public void setSupplierEmailAddress(String supplierEmailAddress) {
        supplierEmailAddressProperty().set(supplierEmailAddress);
    }

    public final StringProperty supplierWebsiteProperty() {
        return this.supplierWebsite;
    }

    public String getSupplierWebsite() {
        return supplierWebsiteProperty().get();
    }

    public void setSupplierWebsite(String supplierWebsite) {
        supplierWebsiteProperty().set(supplierWebsite);
    }

    public final StringProperty picnameProperty() {
        return this.picname;
    }

    public String getPicname() {
        return picnameProperty().get();
    }

    public void setPicname(String picname) {
        picnameProperty().set(picname);
    }

    public final StringProperty picphoneNumberProperty() {
        return this.picphoneNumber;
    }

    public String getPicphoneNumber() {
        return picphoneNumberProperty().get();
    }

    public void setPicphoneNumber(String picphoneNumber) {
        picphoneNumberProperty().set(picphoneNumber);
    }

    public final StringProperty picemailAddressProperty() {
        return this.picemailAddress;
    }

    public String getPicemailAddress() {
        return picemailAddressProperty().get();
    }

    public void setPicemailAddress(String picemailAddress) {
        picemailAddressProperty().set(picemailAddress);
    }

    public final StringProperty supplierInfoProperty() {
        return this.supplierInfo;
    }

    public String getSupplierInfo() {
        return supplierInfoProperty().get();
    }

    public void setSupplierInfo(String supplierInfo) {
        supplierInfoProperty().set(supplierInfo);
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

    public Set getTblMemorandumInvoices() {
        return this.tblMemorandumInvoices;
    }

    public void setTblMemorandumInvoices(Set tblMemorandumInvoices) {
        this.tblMemorandumInvoices = tblMemorandumInvoices;
    }

    public Set getTblSupplierBankAccounts() {
        return this.tblSupplierBankAccounts;
    }

    public void setTblSupplierBankAccounts(Set tblSupplierBankAccounts) {
        this.tblSupplierBankAccounts = tblSupplierBankAccounts;
    }

    public Set getTblHotelInvoices() {
        return this.tblHotelInvoices;
    }

    public void setTblHotelInvoices(Set tblHotelInvoices) {
        this.tblHotelInvoices = tblHotelInvoices;
    }

    public Set getTblPurchaseRequests() {
        return this.tblPurchaseRequests;
    }

    public void setTblPurchaseRequests(Set tblPurchaseRequests) {
        this.tblPurchaseRequests = tblPurchaseRequests;
    }

    public Set getTblReturs() {
        return this.tblReturs;
    }

    public void setTblReturs(Set tblReturs) {
        this.tblReturs = tblReturs;
    }

    public Set getTblSupplierItems() {
        return this.tblSupplierItems;
    }

    public void setTblSupplierItems(Set tblSupplierItems) {
        this.tblSupplierItems = tblSupplierItems;
    }

    public Set getTblPurchaseOrders() {
        return this.tblPurchaseOrders;
    }

    public void setTblPurchaseOrders(Set tblPurchaseOrders) {
        this.tblPurchaseOrders = tblPurchaseOrders;
    }

    @Override
    public String toString() {
        return getSupplierName();
    }
    
}