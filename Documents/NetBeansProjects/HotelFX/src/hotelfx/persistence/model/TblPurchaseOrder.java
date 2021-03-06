package hotelfx.persistence.model;
// Generated Aug 8, 2018 9:14:49 AM by Hibernate Tools 4.3.1

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
 * TblPurchaseOrder generated by hbm2java
 */
public class TblPurchaseOrder implements java.io.Serializable {

    private final LongProperty idpo;
    private final ObjectProperty<RefPurchaseOrderItemArriveStatus> refPurchaseOrderItemArriveStatus;
    private final ObjectProperty<RefPurchaseOrderPaymentStatus> refPurchaseOrderPaymentStatus;
    private final ObjectProperty<RefPurchaseOrderStatus> refPurchaseOrderStatus;
    private final ObjectProperty<RefRecordStatus> refRecordStatus;
    private final ObjectProperty<TblApprovalData> tblApprovalData;
    private final ObjectProperty<TblEmployee> tblEmployeeByApprovedBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCanceledBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByOrderedBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByLastUpdateBy;
    private final ObjectProperty<TblEmployee> tblEmployeeByCreatedBy;
    private final ObjectProperty<TblFinanceData> tblFinanceData;
    private final ObjectProperty<TblHotelPayable> tblHotelPayable;
    private final ObjectProperty<TblPurchaseRequest> tblPurchaseRequest;
    private final ObjectProperty<TblRetur> tblRetur;
    private final ObjectProperty<TblSupplier> tblSupplier;
    private final StringProperty codePo;
    private final ObjectProperty<Date> podate;
    private final ObjectProperty<Date> podueDate;
    private final ObjectProperty<Date> createdDate;
    private final ObjectProperty<Date> approvedDate;
    private final ObjectProperty<Date> orderedDate;
    private final ObjectProperty<Date> canceledDate;
    private final StringProperty canceledNote;
    private final ObjectProperty<BigDecimal> nominalDiscount;
    private final ObjectProperty<BigDecimal> taxPecentage;
    private final ObjectProperty<BigDecimal> deliveryCost;
    private final StringProperty popaymentTypeInformation;
    private final StringProperty ponote;
    private final ObjectProperty<Date> receivingFinishedDate;
    private final ObjectProperty<Date> createDate;
    private final ObjectProperty<Date> lastUpdateDate;

    private Set tblPurchaseOrderRevisionHistoriesForIdposource;
    private Set tblMemorandumInvoices;
    private Set tblPurchaseOrderDetails;
    private Set tblPurchaseOrderRevisionHistoriesForIdponew;

    public TblPurchaseOrder() {
        this.idpo = new SimpleLongProperty();
        this.refPurchaseOrderItemArriveStatus = new SimpleObjectProperty<>();
        this.refPurchaseOrderPaymentStatus = new SimpleObjectProperty<>();
        this.refPurchaseOrderStatus = new SimpleObjectProperty<>();
        this.refRecordStatus = new SimpleObjectProperty<>();
        this.tblApprovalData = new SimpleObjectProperty<>();
        this.tblEmployeeByApprovedBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCanceledBy = new SimpleObjectProperty<>();
        this.tblEmployeeByOrderedBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByLastUpdateBy = new SimpleObjectProperty<>();
        this.tblEmployeeByCreatedBy = new SimpleObjectProperty<>();
        this.tblFinanceData = new SimpleObjectProperty<>();
        this.tblHotelPayable = new SimpleObjectProperty<>();
        this.tblPurchaseRequest = new SimpleObjectProperty<>();
        this.tblRetur = new SimpleObjectProperty<>();
        this.tblSupplier = new SimpleObjectProperty<>();
        this.codePo = new SimpleStringProperty();
        this.podate = new SimpleObjectProperty<>();
        this.podueDate = new SimpleObjectProperty<>();
        this.createdDate = new SimpleObjectProperty<>();
        this.approvedDate = new SimpleObjectProperty<>();
        this.orderedDate = new SimpleObjectProperty<>();
        this.canceledDate = new SimpleObjectProperty<>();
        this.canceledNote = new SimpleStringProperty();
        this.nominalDiscount = new SimpleObjectProperty<>();
        this.taxPecentage = new SimpleObjectProperty<>();
        this.deliveryCost = new SimpleObjectProperty<>();
        this.popaymentTypeInformation = new SimpleStringProperty();
        this.ponote = new SimpleStringProperty();
        this.receivingFinishedDate = new SimpleObjectProperty<>();
        this.createDate = new SimpleObjectProperty<>();
        this.lastUpdateDate = new SimpleObjectProperty<>();

        this.tblPurchaseOrderRevisionHistoriesForIdposource = new HashSet(0);
        this.tblMemorandumInvoices = new HashSet(0);
        this.tblPurchaseOrderDetails = new HashSet(0);
        this.tblPurchaseOrderRevisionHistoriesForIdponew = new HashSet(0);
    }

    public TblPurchaseOrder(long idpo) {
        this();
        idpoProperty().set(idpo);
    }

    public TblPurchaseOrder(long idpo, RefPurchaseOrderItemArriveStatus refPurchaseOrderItemArriveStatus, RefPurchaseOrderPaymentStatus refPurchaseOrderPaymentStatus, RefPurchaseOrderStatus refPurchaseOrderStatus, RefRecordStatus refRecordStatus, TblApprovalData tblApprovalData, TblEmployee tblEmployeeByApprovedBy, TblEmployee tblEmployeeByCanceledBy, TblEmployee tblEmployeeByOrderedBy, TblEmployee tblEmployeeByCreateBy, TblEmployee tblEmployeeByLastUpdateBy, TblEmployee tblEmployeeByCreatedBy, TblFinanceData tblFinanceData, TblHotelPayable tblHotelPayable, TblPurchaseRequest tblPurchaseRequest, TblRetur tblRetur, TblSupplier tblSupplier, String codePo, Date podate, Date podueDate, Date createdDate, Date approvedDate, Date orderedDate, Date canceledDate, String canceledNote, BigDecimal nominalDiscount, BigDecimal taxPecentage, BigDecimal deliveryCost, String popaymentTypeInformation, String ponote, Date receivingFinishedDate, Date createDate, Date lastUpdateDate, Set tblPurchaseOrderRevisionHistoriesForIdposource, Set tblMemorandumInvoices, Set tblPurchaseOrderDetails, Set tblPurchaseOrderRevisionHistoriesForIdponew) {
        this();
        idpoProperty().set(idpo);
        refPurchaseOrderItemArriveStatusProperty().set(refPurchaseOrderItemArriveStatus);
        refPurchaseOrderPaymentStatusProperty().set(refPurchaseOrderPaymentStatus);
        refPurchaseOrderStatusProperty().set(refPurchaseOrderStatus);
        refRecordStatusProperty().set(refRecordStatus);
        tblApprovalDataProperty().set(tblApprovalData);
        tblEmployeeByApprovedByProperty().set(tblEmployeeByApprovedBy);
        tblEmployeeByCanceledByProperty().set(tblEmployeeByCanceledBy);
        tblEmployeeByOrderedByProperty().set(tblEmployeeByOrderedBy);
        tblEmployeeByCreateByProperty().set(tblEmployeeByCreateBy);
        tblEmployeeByLastUpdateByProperty().set(tblEmployeeByLastUpdateBy);
        tblEmployeeByCreatedByProperty().set(tblEmployeeByCreatedBy);
        tblFinanceDataProperty().set(tblFinanceData);
        tblHotelPayableProperty().set(tblHotelPayable);
        tblPurchaseRequestProperty().set(tblPurchaseRequest);
        tblReturProperty().set(tblRetur);
        tblSupplierProperty().set(tblSupplier);
        codePoProperty().set(codePo);
        podateProperty().set(podate);
        podueDateProperty().set(podueDate);
        createdDateProperty().set(createdDate);
        approvedDateProperty().set(approvedDate);
        orderedDateProperty().set(orderedDate);
        canceledDateProperty().set(canceledDate);
        canceledNoteProperty().set(canceledNote);
        nominalDiscountProperty().set(nominalDiscount);
        taxPecentageProperty().set(taxPecentage);
        deliveryCostProperty().set(deliveryCost);
        popaymentTypeInformationProperty().set(popaymentTypeInformation);
        ponoteProperty().set(ponote);
        receivingFinishedDateProperty().set(receivingFinishedDate);
        createDateProperty().set(createDate);
        lastUpdateDateProperty().set(lastUpdateDate);

        this.tblPurchaseOrderRevisionHistoriesForIdposource = tblPurchaseOrderRevisionHistoriesForIdposource;
        this.tblMemorandumInvoices = tblMemorandumInvoices;
        this.tblPurchaseOrderDetails = tblPurchaseOrderDetails;
        this.tblPurchaseOrderRevisionHistoriesForIdponew = tblPurchaseOrderRevisionHistoriesForIdponew;
    }

    public TblPurchaseOrder(TblPurchaseOrder tblPurchaseOrder) {
        this();
        idpoProperty().set(tblPurchaseOrder.getIdpo());
        refPurchaseOrderItemArriveStatusProperty().set(tblPurchaseOrder.getRefPurchaseOrderItemArriveStatus());
        refPurchaseOrderPaymentStatusProperty().set(tblPurchaseOrder.getRefPurchaseOrderPaymentStatus());
        refPurchaseOrderStatusProperty().set(tblPurchaseOrder.getRefPurchaseOrderStatus());
        refRecordStatusProperty().set(tblPurchaseOrder.getRefRecordStatus());
        tblApprovalDataProperty().set(tblPurchaseOrder.getTblApprovalData());
        tblEmployeeByApprovedByProperty().set(tblPurchaseOrder.getTblEmployeeByApprovedBy());
        tblEmployeeByCanceledByProperty().set(tblPurchaseOrder.getTblEmployeeByCanceledBy());
        tblEmployeeByOrderedByProperty().set(tblPurchaseOrder.getTblEmployeeByOrderedBy());
        tblEmployeeByCreateByProperty().set(tblPurchaseOrder.getTblEmployeeByCreateBy());
        tblEmployeeByLastUpdateByProperty().set(tblPurchaseOrder.getTblEmployeeByLastUpdateBy());
        tblEmployeeByCreatedByProperty().set(tblPurchaseOrder.getTblEmployeeByCreatedBy());
        tblFinanceDataProperty().set(tblPurchaseOrder.getTblFinanceData());
        tblHotelPayableProperty().set(tblPurchaseOrder.getTblHotelPayable());
        tblPurchaseRequestProperty().set(tblPurchaseOrder.getTblPurchaseRequest());
        tblReturProperty().set(tblPurchaseOrder.getTblRetur());
        tblSupplierProperty().set(tblPurchaseOrder.getTblSupplier());
        codePoProperty().set(tblPurchaseOrder.getCodePo());
        podateProperty().set(tblPurchaseOrder.getPodate());
        podueDateProperty().set(tblPurchaseOrder.getPodueDate());
        createdDateProperty().set(tblPurchaseOrder.getCreatedDate());
        approvedDateProperty().set(tblPurchaseOrder.getApprovedDate());
        orderedDateProperty().set(tblPurchaseOrder.getOrderedDate());
        canceledDateProperty().set(tblPurchaseOrder.getCanceledDate());
        canceledNoteProperty().set(tblPurchaseOrder.getCanceledNote());
        nominalDiscountProperty().set(tblPurchaseOrder.getNominalDiscount());
        taxPecentageProperty().set(tblPurchaseOrder.getTaxPecentage());
        deliveryCostProperty().set(tblPurchaseOrder.getDeliveryCost());
        popaymentTypeInformationProperty().set(tblPurchaseOrder.getPopaymentTypeInformation());
        ponoteProperty().set(tblPurchaseOrder.getPonote());
        receivingFinishedDateProperty().set(tblPurchaseOrder.getReceivingFinishedDate());
        createDateProperty().set(tblPurchaseOrder.getCreateDate());
        lastUpdateDateProperty().set(tblPurchaseOrder.getLastUpdateDate());

        this.tblPurchaseOrderRevisionHistoriesForIdposource = tblPurchaseOrder.getTblPurchaseOrderRevisionHistoriesForIdposource();
        this.tblMemorandumInvoices = tblPurchaseOrder.getTblMemorandumInvoices();
        this.tblPurchaseOrderDetails = tblPurchaseOrder.getTblPurchaseOrderDetails();
        this.tblPurchaseOrderRevisionHistoriesForIdponew = tblPurchaseOrder.getTblPurchaseOrderRevisionHistoriesForIdponew();
    }

    public final LongProperty idpoProperty() {
        return this.idpo;
    }

    public long getIdpo() {
        return idpoProperty().get();
    }

    public void setIdpo(long idpo) {
        idpoProperty().set(idpo);
    }

    public final ObjectProperty<RefPurchaseOrderItemArriveStatus> refPurchaseOrderItemArriveStatusProperty() {
        return this.refPurchaseOrderItemArriveStatus;
    }

    public RefPurchaseOrderItemArriveStatus getRefPurchaseOrderItemArriveStatus() {
        return refPurchaseOrderItemArriveStatusProperty().get();
    }

    public void setRefPurchaseOrderItemArriveStatus(RefPurchaseOrderItemArriveStatus refPurchaseOrderItemArriveStatus) {
        refPurchaseOrderItemArriveStatusProperty().set(refPurchaseOrderItemArriveStatus);
    }

    public final ObjectProperty<RefPurchaseOrderPaymentStatus> refPurchaseOrderPaymentStatusProperty() {
        return this.refPurchaseOrderPaymentStatus;
    }

    public RefPurchaseOrderPaymentStatus getRefPurchaseOrderPaymentStatus() {
        return refPurchaseOrderPaymentStatusProperty().get();
    }

    public void setRefPurchaseOrderPaymentStatus(RefPurchaseOrderPaymentStatus refPurchaseOrderPaymentStatus) {
        refPurchaseOrderPaymentStatusProperty().set(refPurchaseOrderPaymentStatus);
    }

    public final ObjectProperty<RefPurchaseOrderStatus> refPurchaseOrderStatusProperty() {
        return this.refPurchaseOrderStatus;
    }

    public RefPurchaseOrderStatus getRefPurchaseOrderStatus() {
        return refPurchaseOrderStatusProperty().get();
    }

    public void setRefPurchaseOrderStatus(RefPurchaseOrderStatus refPurchaseOrderStatus) {
        refPurchaseOrderStatusProperty().set(refPurchaseOrderStatus);
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

    public final ObjectProperty<TblApprovalData> tblApprovalDataProperty() {
        return this.tblApprovalData;
    }

    public TblApprovalData getTblApprovalData() {
        return tblApprovalDataProperty().get();
    }

    public void setTblApprovalData(TblApprovalData tblApprovalData) {
        tblApprovalDataProperty().set(tblApprovalData);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByApprovedByProperty() {
        return this.tblEmployeeByApprovedBy;
    }

    public TblEmployee getTblEmployeeByApprovedBy() {
        return tblEmployeeByApprovedByProperty().get();
    }

    public void setTblEmployeeByApprovedBy(TblEmployee tblEmployeeByApprovedBy) {
        tblEmployeeByApprovedByProperty().set(tblEmployeeByApprovedBy);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByCanceledByProperty() {
        return this.tblEmployeeByCanceledBy;
    }

    public TblEmployee getTblEmployeeByCanceledBy() {
        return tblEmployeeByCanceledByProperty().get();
    }

    public void setTblEmployeeByCanceledBy(TblEmployee tblEmployeeByCanceledBy) {
        tblEmployeeByCanceledByProperty().set(tblEmployeeByCanceledBy);
    }

    public final ObjectProperty<TblEmployee> tblEmployeeByOrderedByProperty() {
        return this.tblEmployeeByOrderedBy;
    }

    public TblEmployee getTblEmployeeByOrderedBy() {
        return tblEmployeeByOrderedByProperty().get();
    }

    public void setTblEmployeeByOrderedBy(TblEmployee tblEmployeeByOrderedBy) {
        tblEmployeeByOrderedByProperty().set(tblEmployeeByOrderedBy);
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

    public final ObjectProperty<TblEmployee> tblEmployeeByCreatedByProperty() {
        return this.tblEmployeeByCreatedBy;
    }

    public TblEmployee getTblEmployeeByCreatedBy() {
        return tblEmployeeByCreatedByProperty().get();
    }

    public void setTblEmployeeByCreatedBy(TblEmployee tblEmployeeByCreatedBy) {
        tblEmployeeByCreatedByProperty().set(tblEmployeeByCreatedBy);
    }

    public final ObjectProperty<TblFinanceData> tblFinanceDataProperty() {
        return this.tblFinanceData;
    }

    public TblFinanceData getTblFinanceData() {
        return tblFinanceDataProperty().get();
    }

    public void setTblFinanceData(TblFinanceData tblFinanceData) {
        tblFinanceDataProperty().set(tblFinanceData);
    }

    public final ObjectProperty<TblHotelPayable> tblHotelPayableProperty() {
        return this.tblHotelPayable;
    }

    public TblHotelPayable getTblHotelPayable() {
        return tblHotelPayableProperty().get();
    }

    public void setTblHotelPayable(TblHotelPayable tblHotelPayable) {
        tblHotelPayableProperty().set(tblHotelPayable);
    }

    public final ObjectProperty<TblPurchaseRequest> tblPurchaseRequestProperty() {
        return this.tblPurchaseRequest;
    }

    public TblPurchaseRequest getTblPurchaseRequest() {
        return tblPurchaseRequestProperty().get();
    }

    public void setTblPurchaseRequest(TblPurchaseRequest tblPurchaseRequest) {
        tblPurchaseRequestProperty().set(tblPurchaseRequest);
    }

    public final ObjectProperty<TblRetur> tblReturProperty() {
        return this.tblRetur;
    }

    public TblRetur getTblRetur() {
        return tblReturProperty().get();
    }

    public void setTblRetur(TblRetur tblRetur) {
        tblReturProperty().set(tblRetur);
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

    public final StringProperty codePoProperty() {
        return this.codePo;
    }

    public String getCodePo() {
        return codePoProperty().get();
    }

    public void setCodePo(String codePo) {
        codePoProperty().set(codePo);
    }

    public final ObjectProperty<Date> podateProperty() {
        return this.podate;
    }

    public Date getPodate() {
        return podateProperty().get();
    }

    public void setPodate(Date podate) {
        podateProperty().set(podate);
    }

    public final ObjectProperty<Date> podueDateProperty() {
        return this.podueDate;
    }

    public Date getPodueDate() {
        return podueDateProperty().get();
    }

    public void setPodueDate(Date podueDate) {
        podueDateProperty().set(podueDate);
    }

    public final ObjectProperty<Date> createdDateProperty() {
        return this.createdDate;
    }

    public Date getCreatedDate() {
        return createdDateProperty().get();
    }

    public void setCreatedDate(Date createdDate) {
        createdDateProperty().set(createdDate);
    }

    public final ObjectProperty<Date> approvedDateProperty() {
        return this.approvedDate;
    }

    public Date getApprovedDate() {
        return approvedDateProperty().get();
    }

    public void setApprovedDate(Date approvedDate) {
        approvedDateProperty().set(approvedDate);
    }

    public final ObjectProperty<Date> orderedDateProperty() {
        return this.orderedDate;
    }

    public Date getOrderedDate() {
        return orderedDateProperty().get();
    }

    public void setOrderedDate(Date orderedDate) {
        orderedDateProperty().set(orderedDate);
    }

    public final ObjectProperty<Date> canceledDateProperty() {
        return this.canceledDate;
    }

    public Date getCanceledDate() {
        return canceledDateProperty().get();
    }

    public void setCanceledDate(Date canceledDate) {
        canceledDateProperty().set(canceledDate);
    }

    public final StringProperty canceledNoteProperty() {
        return this.canceledNote;
    }

    public String getCanceledNote() {
        return canceledNoteProperty().get();
    }

    public void setCanceledNote(String canceledNote) {
        canceledNoteProperty().set(canceledNote);
    }

    public final ObjectProperty<BigDecimal> nominalDiscountProperty() {
        return this.nominalDiscount;
    }

    public BigDecimal getNominalDiscount() {
        return nominalDiscountProperty().get();
    }

    public void setNominalDiscount(BigDecimal nominalDiscount) {
        nominalDiscountProperty().set(nominalDiscount);
    }

    public final ObjectProperty<BigDecimal> taxPecentageProperty() {
        return this.taxPecentage;
    }

    public BigDecimal getTaxPecentage() {
        return taxPecentageProperty().get();
    }

    public void setTaxPecentage(BigDecimal taxPecentage) {
        taxPecentageProperty().set(taxPecentage);
    }

    public final ObjectProperty<BigDecimal> deliveryCostProperty() {
        return this.deliveryCost;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCostProperty().get();
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        deliveryCostProperty().set(deliveryCost);
    }

    public final StringProperty popaymentTypeInformationProperty() {
        return this.popaymentTypeInformation;
    }

    public String getPopaymentTypeInformation() {
        return popaymentTypeInformationProperty().get();
    }

    public void setPopaymentTypeInformation(String popaymentTypeInformation) {
        popaymentTypeInformationProperty().set(popaymentTypeInformation);
    }

    public final StringProperty ponoteProperty() {
        return this.ponote;
    }

    public String getPonote() {
        return ponoteProperty().get();
    }

    public void setPonote(String ponote) {
        ponoteProperty().set(ponote);
    }

    public final ObjectProperty<Date> receivingFinishedDateProperty() {
        return this.receivingFinishedDate;
    }

    public Date getReceivingFinishedDate() {
        return receivingFinishedDateProperty().get();
    }

    public void setReceivingFinishedDate(Date receivingFinishedDate) {
        receivingFinishedDateProperty().set(receivingFinishedDate);
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

    public Set getTblPurchaseOrderRevisionHistoriesForIdposource() {
        return this.tblPurchaseOrderRevisionHistoriesForIdposource;
    }

    public void setTblPurchaseOrderRevisionHistoriesForIdposource(Set tblPurchaseOrderRevisionHistoriesForIdposource) {
        this.tblPurchaseOrderRevisionHistoriesForIdposource = tblPurchaseOrderRevisionHistoriesForIdposource;
    }

    public Set getTblMemorandumInvoices() {
        return this.tblMemorandumInvoices;
    }

    public void setTblMemorandumInvoices(Set tblMemorandumInvoices) {
        this.tblMemorandumInvoices = tblMemorandumInvoices;
    }

    public Set getTblPurchaseOrderDetails() {
        return this.tblPurchaseOrderDetails;
    }

    public void setTblPurchaseOrderDetails(Set tblPurchaseOrderDetails) {
        this.tblPurchaseOrderDetails = tblPurchaseOrderDetails;
    }

    public Set getTblPurchaseOrderRevisionHistoriesForIdponew() {
        return this.tblPurchaseOrderRevisionHistoriesForIdponew;
    }

    public void setTblPurchaseOrderRevisionHistoriesForIdponew(Set tblPurchaseOrderRevisionHistoriesForIdponew) {
        this.tblPurchaseOrderRevisionHistoriesForIdponew = tblPurchaseOrderRevisionHistoriesForIdponew;
    }

    @Override
    public String toString() {
        return getCodePo();
    }
    
}
