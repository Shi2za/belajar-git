/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.LogCompanyBalanceCashFlow;
import hotelfx.persistence.model.RefBankAccountHolderStatus;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefFinanceTransactionStatus;
import hotelfx.persistence.model.RefFinanceTransactionType;
import hotelfx.persistence.model.RefHotelInvoiceType;
import hotelfx.persistence.model.RefHotelReceivableType;
import hotelfx.persistence.model.RefItemExpiredDateStatus;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefPurchaseOrderItemArriveStatus;
import hotelfx.persistence.model.RefPurchaseOrderPaymentStatus;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.RefReturPaymentStatus;
import hotelfx.persistence.model.RefReturStatus;
import hotelfx.persistence.model.RefReturType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelReceivable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelReceivable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemExpiredDate;
import hotelfx.persistence.model.TblItemLocation;
import hotelfx.persistence.model.TblItemLocationItemExpiredDate;
import hotelfx.persistence.model.TblItemLocationPropertyBarcode;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblItemMutationHistoryItemExpiredDate;
import hotelfx.persistence.model.TblItemMutationHistoryPropertyBarcode;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailItemExpiredDate;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailPropertyBarcode;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailItemExpiredDate;
import hotelfx.persistence.model.TblReturDetailPropertyBarcode;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierBankAccount;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_retur.retur.ReturController;
import hotelfx.view.feature_retur.retur_purchasing.ReturPController;
import hotelfx.view.feature_retur.retur_storing.ReturWController;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FReturManagerImpl implements FReturManager {

    private Session session;

    private String errMsg;

    public FReturManagerImpl() {

    }

    //function was not used
    @Override
    public TblRetur insertDataRetur(TblRetur retur,
            List<ReturController.DetailLocation> detailLocations) {
        errMsg = "";
        TblRetur tblRetur = retur;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data retur
                tblRetur.setCodeRetur(ClassCoder.getCode("Retur", session));
                tblRetur.setReturDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setRefReturStatus(session.find(RefReturStatus.class, 0));  //Dibuat = '0'
                tblRetur.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 0));
                tblRetur.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRetur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRetur.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRetur);
                //data detail - location
                for (ReturController.DetailLocation detailLocation : detailLocations) {
                    //data retur detail
                    detailLocation.getTblDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    detailLocation.getTblDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    detailLocation.getTblDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    detailLocation.getTblDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    detailLocation.getTblDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    //increment item qunatity (if data was property barcode and has been saved)
                    if ((detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) //Property
                            && detailLocation.getTblDetail().getIddetailRetur() != 0L) {
                        detailLocation.getTblDetail().setItemQuantity(detailLocation.getTblDetail().getItemQuantity().add(new BigDecimal("1")));
                    }
                    session.saveOrUpdate(detailLocation.getTblDetail());
                    //data item mutation history (create)
                    TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
                    dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblSupplierItem().getTblItem());
                    dataMutationHistory.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
                    dataMutationHistory.setTblLocationByIdlocationOfSource(detailLocation.getTblDetail().getTblLocation());
                    dataMutationHistory.setTblLocationByIdlocationOfDestination(retur.getTblSupplier().getTblLocation());
//                    dataMutationHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationHistory.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationHistory.setRefItemMutationType(session.find(RefItemMutationType.class, 2)); //Rusak = '2'
                    dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataMutationHistory);
                    //data item location (source, udpate:minus)
                    TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfSource().getIdlocation(),
                            dataMutationHistory.getTblItem().getIditem());
                    if (tempItemSourceLocation == null) {
                        errMsg = "Barang (" + dataMutationHistory.getTblItem().getItemName() + ") tidak ditemukan pada Lokasi (yang dipilih)..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataMutationHistory.getItemQuantity()));
                    tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    if (tempItemSourceLocation.getItemQuantity()
                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                        errMsg = "Stok Barang (di gudang) tidak cukup untuk di-retur..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return null;
                    }
                    session.update(tempItemSourceLocation);
                    //data item mutation - property barcode
                    if (detailLocation.getTblDetailPropertyBarcode() != null) {
                        //data retur detail - property barcode
                        detailLocation.getTblDetailPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(detailLocation.getTblDetailPropertyBarcode());
                        //data item mutation - property barcode
                        TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                        itemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dataMutationHistory);
                        itemMutationHistoryPropertyBarcode.setTblPropertyBarcode(detailLocation.getTblDetailPropertyBarcode().getTblPropertyBarcode());
                        itemMutationHistoryPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        itemMutationHistoryPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        itemMutationHistoryPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        itemMutationHistoryPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        itemMutationHistoryPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(itemMutationHistoryPropertyBarcode);
                        //data item location property barcode (source:remove)
                        TblItemLocationPropertyBarcode tempItemSourceLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(tempItemSourceLocation.getIdrelation(),
                                detailLocation.getTblDetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                        if (tempItemSourceLocationPropertyBarcode != null) {
                            tempItemSourceLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemSourceLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            tempItemSourceLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                            session.update(tempItemSourceLocationPropertyBarcode);
                        } else {
                            errMsg = "Properti (" + detailLocation.getTblDetailPropertyBarcode().getTblPropertyBarcode().getCodeBarcode() + ") tidak ditemukan pada Lokasi (yang dipilih)..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                    }
                    //data retur detail - item expired date
                    if (detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getConsumable()) {   //consumable
                        for (ReturController.DetailItemExpiredDateSelected detailItemExpiredDateSelected : detailLocation.getDetailItemExpiredDateSelecteds()) {
                            if (detailItemExpiredDateSelected.isSelected()) {
                                //data retur detail - item expired date
                                detailItemExpiredDateSelected.getDataDetailItemExporedDate().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                detailItemExpiredDateSelected.getDataDetailItemExporedDate().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                detailItemExpiredDateSelected.getDataDetailItemExporedDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                detailItemExpiredDateSelected.getDataDetailItemExporedDate().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                detailItemExpiredDateSelected.getDataDetailItemExporedDate().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(detailItemExpiredDateSelected.getDataDetailItemExporedDate());
                                //data item expired date (update status)
//                                detailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().setRefItemExpiredDateStatus(session.find(RefItemExpiredDateStatus.class, 4));  //4 = 'Retur'
                                detailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                detailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate().setTblEmployeeByLastUdapteBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(detailItemExpiredDateSelected.getDataDetailItemExporedDate().getTblItemExpiredDate());
                            }
                        }
                    }
                }
//            //data detail - location
//            for (ReturController.DetailLocation detailLocation : detailLocations) {
//                //data retur detail (property) : item quantity (reset to total property number)
//                if(detailLocation.getTblDetail().getTblItem().getPropertyStatus()){   //Property
//                    double count = 0;
//                    for (ReturController.DetailLocation data : detailLocations) {
//                        if(detailLocation == data){
//                            count = count + 1;
//                        }
//                    }
//                    detailLocation.getTblDetail().setItemQuantity(new BigDecimal(count));
//                    session.update(detailLocation.getTblDetail());
//                }
//            }
                if (tblRetur.getRefReturType().getIdtype() == 1) {    //1 = 'Tukar Barang'
//                    //data PO
//                    TblPurchaseOrder dataPO = new TblPurchaseOrder();
//                    dataPO.setTblSupplier(tblRetur.getTblSupplier());
//                    dataPO.setTblRetur(tblRetur);
//                    dataPO.setNominalDiscount(new BigDecimal("0"));
//                    dataPO.setTaxPecentage(new BigDecimal("0"));
//                    dataPO.setDeliveryCost(new BigDecimal("0"));
//                    dataPO.setCodePo(ClassCoder.getCode("Purchase Order", session));
//                    dataPO.setPodate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 1));  //1 = 'Approved'
//                    dataPO.setRefPurchaseOrderItemArriveStatus(session.find(RefPurchaseOrderItemArriveStatus.class, 0));
//                    dataPO.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 0));
//                    dataPO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataPO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataPO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(dataPO);
//                    //data PO - detail
//                    BigDecimal totalPO = new BigDecimal("0");
//                    BigDecimal totalTax = new BigDecimal("0");
//                    for (ReturController.DetailLocation detailLocation : detailLocations) {
//                        TblPurchaseOrderDetail dataPODetail = new TblPurchaseOrderDetail();
//                        dataPODetail.setTblPurchaseOrder(dataPO);
////                        dataPODetail.setTblItem(detailLocation.getTblDetail().getTblItem());
//                        dataPODetail.setItemCost(detailLocation.getTblDetail().getItemCost());
//                        dataPODetail.setItemDiscount(detailLocation.getTblDetail().getItemDiscount());
//                        dataPODetail.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
//                        dataPODetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataPODetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        dataPODetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataPODetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        dataPODetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                        session.saveOrUpdate(dataPODetail);
//                        //calculation total PO n total tax percentage
//                        BigDecimal totalItemCost = (detailLocation.getTblDetail().getItemCost().subtract(detailLocation.getTblDetail().getItemDiscount()))
//                                .multiply(detailLocation.getTblDetail().getItemQuantity());
//                        totalPO = totalPO.add(totalItemCost);
//                        totalTax = totalTax.add(totalItemCost.multiply(detailLocation.getTblDetail().getItemTaxPercentage()));
//                    }
//                    //data hotel payable
//                    TblHotelPayable hotelPayable = new TblHotelPayable();
//                    hotelPayable.setHotelPayableNominal(new BigDecimal("0"));
//                    hotelPayable.setRefHotelPayableType(session.find(RefHotelPayableType.class, 1));   //Purchase Order = '1'                
//                    hotelPayable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 2));    //Sudah Dibayar = '2'
//                    hotelPayable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelPayable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelPayable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelPayable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelPayable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelPayable);
//                    //data hotel finnace transaction
//                    TblHotelFinanceTransaction hotelFinanceTransaction = new TblHotelFinanceTransaction();
////                    hotelFinanceTransaction.setTblHotelPayable(hotelPayable);1234567890---
//                    hotelFinanceTransaction.setRefFinanceTransactionType(session.find(RefFinanceTransactionType.class, 0));    //payable = 0'
//                    hotelFinanceTransaction.setTransactionNominal(new BigDecimal("0"));
//                    hotelFinanceTransaction.setTransactionRoundingValue(new BigDecimal("0"));
////                hotelFinanceTransaction.setSenderName();
////                hotelFinanceTransaction.setTblBankAccountBySenderBankAccount();
////                hotelFinanceTransaction.setReceiverName();
////                hotelFinanceTransaction.setTblBankAccountByReceiverBankAccount();
//                    hotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
//                    hotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelFinanceTransaction);
//                    //set n update data PO (tax percentage)
//                    dataPO.setTblHotelPayable(hotelPayable);
//                    dataPO.setTaxPecentage(totalTax.divide(totalPO));
//                    dataPO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    session.update(dataPO);
                } else {  //2 = 'Kembali Uang'
                    //data hotel invoice
                    TblHotelInvoice hotelInvoice = new TblHotelInvoice();
                    hotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
                    hotelInvoice.setTblPartner(null);
                    hotelInvoice.setTblSupplier(tblRetur.getTblSupplier());
                    hotelInvoice.setRefHotelInvoiceType(session.find(RefHotelInvoiceType.class, 1));    //Receivable = '1'
                    hotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hotelInvoice);
                    //data hotel receivable
                    TblHotelReceivable hotelReceivable = new TblHotelReceivable();
                    hotelReceivable.setTblHotelInvoice(hotelInvoice);
                    hotelReceivable.setHotelReceivableNominal(calculationTotalRetur(tblRetur,
                            detailLocations));
                    hotelReceivable.setRefHotelReceivableType(session.find(RefHotelReceivableType.class, 2));   //Retur = '2'
                    hotelReceivable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 0));    //Belum Dibayar = '0'
                    hotelReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hotelReceivable);
                    //data retur - (hotel receivable)
                    tblRetur.setTblHotelReceivable(hotelReceivable);
                    session.saveOrUpdate(tblRetur);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblRetur;
    }

    @Override
    public TblRetur insertDataReturWV2(TblRetur retur,
            List<ReturWController.DetailLocation> detailLocations) {
        errMsg = "";
        TblRetur tblRetur = retur;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data retur
                tblRetur.setCodeRetur(ClassCoder.getCode("Retur", session));
                tblRetur.setReturDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setRefReturType(session.find(RefReturType.class, 1));  //1 = 'Tukar Barang'
                tblRetur.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByCreatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRetur.setRefReturStatus(session.find(RefReturStatus.class, 0));  //Dibuat = '0'
                tblRetur.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 0));
                tblRetur.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRetur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRetur.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRetur);
                //data detail - location
                for (ReturWController.DetailLocation detailLocation : detailLocations) {
                    //data retur detail
                    detailLocation.getTblDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    detailLocation.getTblDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    detailLocation.getTblDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    detailLocation.getTblDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    detailLocation.getTblDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    //increment item qunatity (if data was property barcode and has been saved)
                    if ((detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) //Property
                            && detailLocation.getTblDetail().getIddetailRetur() != 0L) {
                        detailLocation.getTblDetail().setItemQuantity(detailLocation.getTblDetail().getItemQuantity().add(new BigDecimal("1")));
                    }
                    session.saveOrUpdate(detailLocation.getTblDetail());
//                    //data item mutation history (create)
//                    TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
//                    dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblSupplierItem().getTblItem());
//                    dataMutationHistory.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
//                    dataMutationHistory.setTblLocationByIdlocationOfSource(detailLocation.getTblDetail().getTblLocation());
//                    dataMutationHistory.setTblLocationByIdlocationOfDestination(retur.getTblSupplier().getTblLocation());
//                    dataMutationHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataMutationHistory.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataMutationHistory.setRefItemMutationType(session.find(RefItemMutationType.class, 2)); //Rusak = '2'
//                    dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(dataMutationHistory);
//                    //data item location (source, udpate:minus)
//                    TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfSource().getIdlocation(),
//                            dataMutationHistory.getTblItem().getIditem());
//                    if (tempItemSourceLocation == null) {
//                        errMsg = "Barang (" + dataMutationHistory.getTblItem().getItemName() + ") tidak ditemukan pada Lokasi (yang dipilih)..!";
//                        if (session.getTransaction().isActive()) {
//                            session.getTransaction().rollback();
//                        }
//                        return null;
//                    }
//                    tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataMutationHistory.getItemQuantity()));
//                    tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    if (tempItemSourceLocation.getItemQuantity()
//                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
//                        errMsg = "Stok Barang (di gudang) tidak cukup untuk di-retur..!";
//                        if (session.getTransaction().isActive()) {
//                            session.getTransaction().rollback();
//                        }
//                        return null;
//                    }
//                    session.update(tempItemSourceLocation);
                    //data item mutation - property barcode
                    if (detailLocation.getTblDetailPropertyBarcode() != null) {
                        //data retur detail - property barcode
                        detailLocation.getTblDetailPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(detailLocation.getTblDetailPropertyBarcode());
                    }
                    //data retur detail - item expired date
                    if (detailLocation.getTblDetailItemExpiredDate() != null) {   //consumable
                        //data retur detail - item expired date                        
                        detailLocation.getTblDetailItemExpiredDate().setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
                        detailLocation.getTblDetailItemExpiredDate().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailItemExpiredDate().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailItemExpiredDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailItemExpiredDate().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailItemExpiredDate().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(detailLocation.getTblDetailItemExpiredDate());
                    }
                }
//            //data detail - location
//            for (ReturController.DetailLocation detailLocation : detailLocations) {
//                //data retur detail (property) : item quantity (reset to total property number)
//                if(detailLocation.getTblDetail().getTblItem().getPropertyStatus()){   //Property
//                    double count = 0;
//                    for (ReturController.DetailLocation data : detailLocations) {
//                        if(detailLocation == data){
//                            count = count + 1;
//                        }
//                    }
//                    detailLocation.getTblDetail().setItemQuantity(new BigDecimal(count));
//                    session.update(detailLocation.getTblDetail());
//                }
//            }
                if (tblRetur.getRefReturType().getIdtype() == 1) {    //1 = 'Tukar Barang'
//                    //data PO
//                    TblPurchaseOrder dataPO = new TblPurchaseOrder();
//                    dataPO.setTblSupplier(tblRetur.getTblSupplier());
//                    dataPO.setTblRetur(tblRetur);
//                    dataPO.setNominalDiscount(new BigDecimal("0"));
//                    dataPO.setTaxPecentage(new BigDecimal("0"));
//                    dataPO.setDeliveryCost(new BigDecimal("0"));
//                    dataPO.setCodePo(ClassCoder.getCode("Purchase Order", session));
//                    dataPO.setPodate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 1));  //1 = 'Approved'
//                    dataPO.setRefPurchaseOrderItemArriveStatus(session.find(RefPurchaseOrderItemArriveStatus.class, 0));
//                    dataPO.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 0));
//                    dataPO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataPO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataPO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(dataPO);
//                    //data PO - detail
//                    BigDecimal totalPO = new BigDecimal("0");
//                    BigDecimal totalTax = new BigDecimal("0");
//                    for (ReturController.DetailLocation detailLocation : detailLocations) {
//                        TblPurchaseOrderDetail dataPODetail = new TblPurchaseOrderDetail();
//                        dataPODetail.setTblPurchaseOrder(dataPO);
////                        dataPODetail.setTblItem(detailLocation.getTblDetail().getTblItem());
//                        dataPODetail.setItemCost(detailLocation.getTblDetail().getItemCost());
//                        dataPODetail.setItemDiscount(detailLocation.getTblDetail().getItemDiscount());
//                        dataPODetail.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
//                        dataPODetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataPODetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        dataPODetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataPODetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        dataPODetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                        session.saveOrUpdate(dataPODetail);
//                        //calculation total PO n total tax percentage
//                        BigDecimal totalItemCost = (detailLocation.getTblDetail().getItemCost().subtract(detailLocation.getTblDetail().getItemDiscount()))
//                                .multiply(detailLocation.getTblDetail().getItemQuantity());
//                        totalPO = totalPO.add(totalItemCost);
//                        totalTax = totalTax.add(totalItemCost.multiply(detailLocation.getTblDetail().getItemTaxPercentage()));
//                    }
//                    //data hotel payable
//                    TblHotelPayable hotelPayable = new TblHotelPayable();
//                    hotelPayable.setHotelPayableNominal(new BigDecimal("0"));
//                    hotelPayable.setRefHotelPayableType(session.find(RefHotelPayableType.class, 1));   //Purchase Order = '1'                
//                    hotelPayable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 2));    //Sudah Dibayar = '2'
//                    hotelPayable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelPayable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelPayable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelPayable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelPayable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelPayable);
//                    //data hotel finnace transaction
//                    TblHotelFinanceTransaction hotelFinanceTransaction = new TblHotelFinanceTransaction();
////                    hotelFinanceTransaction.setTblHotelPayable(hotelPayable);1234567890---
//                    hotelFinanceTransaction.setRefFinanceTransactionType(session.find(RefFinanceTransactionType.class, 0));    //payable = 0'
//                    hotelFinanceTransaction.setTransactionNominal(new BigDecimal("0"));
//                    hotelFinanceTransaction.setTransactionRoundingValue(new BigDecimal("0"));
////                hotelFinanceTransaction.setSenderName();
////                hotelFinanceTransaction.setTblBankAccountBySenderBankAccount();
////                hotelFinanceTransaction.setReceiverName();
////                hotelFinanceTransaction.setTblBankAccountByReceiverBankAccount();
//                    hotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
//                    hotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelFinanceTransaction);
//                    //set n update data PO (tax percentage)
//                    dataPO.setTblHotelPayable(hotelPayable);
//                    dataPO.setTaxPecentage(totalTax.divide(totalPO));
//                    dataPO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    session.update(dataPO);
                } else {  //2 = 'Kembali Uang'
                    //data hotel invoice
                    TblHotelInvoice hotelInvoice = new TblHotelInvoice();
                    hotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
                    hotelInvoice.setTblPartner(null);
                    hotelInvoice.setTblSupplier(tblRetur.getTblSupplier());
                    hotelInvoice.setRefHotelInvoiceType(session.find(RefHotelInvoiceType.class, 1));    //Receivable = '1'
                    hotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hotelInvoice);
                    //data hotel receivable
                    TblHotelReceivable hotelReceivable = new TblHotelReceivable();
                    hotelReceivable.setTblHotelInvoice(hotelInvoice);
                    hotelReceivable.setHotelReceivableNominal(calculationTotalReturWV2(tblRetur,
                            detailLocations));
                    hotelReceivable.setRefHotelReceivableType(session.find(RefHotelReceivableType.class, 2));   //Retur = '2'
                    hotelReceivable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 0));    //Belum Dibayar = '0'
                    hotelReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hotelReceivable);
                    //data retur - (hotel receivable)
                    tblRetur.setTblHotelReceivable(hotelReceivable);
                    session.saveOrUpdate(tblRetur);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblRetur;
    }

    @Override
    public TblRetur insertDataReturPV2(TblRetur retur,
            List<ReturPController.DetailLocation> detailLocations) {
        errMsg = "";
        TblRetur tblRetur = retur;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data retur
                tblRetur.setCodeRetur(ClassCoder.getCode("Retur", session));
                tblRetur.setReturDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setRefReturType(session.find(RefReturType.class, 1));  //1 = 'Tukar Barang'
                tblRetur.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByCreatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRetur.setRefReturStatus(session.find(RefReturStatus.class, 0));  //Dibuat = '0'
                tblRetur.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 0));
                tblRetur.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRetur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblRetur.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblRetur);
                //data detail - location
                for (ReturPController.DetailLocation detailLocation : detailLocations) {
                    //data retur detail
                    detailLocation.getTblDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    detailLocation.getTblDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    detailLocation.getTblDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    detailLocation.getTblDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    detailLocation.getTblDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    //increment item qunatity (if data was property barcode and has been saved)
                    if ((detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getPropertyStatus()) //Property
                            && detailLocation.getTblDetail().getIddetailRetur() != 0L) {
                        detailLocation.getTblDetail().setItemQuantity(detailLocation.getTblDetail().getItemQuantity().add(new BigDecimal("1")));
                    }
                    session.saveOrUpdate(detailLocation.getTblDetail());
//                    //data item mutation history (create)
//                    TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
//                    dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblSupplierItem().getTblItem());
//                    dataMutationHistory.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
//                    dataMutationHistory.setTblLocationByIdlocationOfSource(detailLocation.getTblDetail().getTblLocation());
//                    dataMutationHistory.setTblLocationByIdlocationOfDestination(retur.getTblSupplier().getTblLocation());
//                    dataMutationHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataMutationHistory.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataMutationHistory.setRefItemMutationType(session.find(RefItemMutationType.class, 2)); //Rusak = '2'
//                    dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(dataMutationHistory);
//                    //data item location (source, udpate:minus)
//                    TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfSource().getIdlocation(),
//                            dataMutationHistory.getTblItem().getIditem());
//                    if (tempItemSourceLocation == null) {
//                        errMsg = "Barang (" + dataMutationHistory.getTblItem().getItemName() + ") tidak ditemukan pada Lokasi (yang dipilih)..!";
//                        if (session.getTransaction().isActive()) {
//                            session.getTransaction().rollback();
//                        }
//                        return null;
//                    }
//                    tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataMutationHistory.getItemQuantity()));
//                    tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    if (tempItemSourceLocation.getItemQuantity()
//                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
//                        errMsg = "Stok Barang (di gudang) tidak cukup untuk di-retur..!";
//                        if (session.getTransaction().isActive()) {
//                            session.getTransaction().rollback();
//                        }
//                        return null;
//                    }
//                    session.update(tempItemSourceLocation);
                    //data item mutation - property barcode
                    if (detailLocation.getTblDetailPropertyBarcode() != null) {     //property barcode
                        //data retur detail - property barcode
                        detailLocation.getTblDetailPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(detailLocation.getTblDetailPropertyBarcode());
                    }
                    //data retur detail - item expired date
                    if (detailLocation.getTblDetailItemExpiredDate() != null) {   //consumable
                        //data retur detail - item expired date
                        detailLocation.getTblDetailItemExpiredDate().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailItemExpiredDate().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailItemExpiredDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetailItemExpiredDate().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetailItemExpiredDate().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(detailLocation.getTblDetailItemExpiredDate());
                    }
                }
//            //data detail - location
//            for (ReturController.DetailLocation detailLocation : detailLocations) {
//                //data retur detail (property) : item quantity (reset to total property number)
//                if(detailLocation.getTblDetail().getTblItem().getPropertyStatus()){   //Property
//                    double count = 0;
//                    for (ReturController.DetailLocation data : detailLocations) {
//                        if(detailLocation == data){
//                            count = count + 1;
//                        }
//                    }
//                    detailLocation.getTblDetail().setItemQuantity(new BigDecimal(count));
//                    session.update(detailLocation.getTblDetail());
//                }
//            }
                if (tblRetur.getRefReturType().getIdtype() == 1) {    //1 = 'Tukar Barang'
//                    //data PO
//                    TblPurchaseOrder dataPO = new TblPurchaseOrder();
//                    dataPO.setTblSupplier(tblRetur.getTblSupplier());
//                    dataPO.setTblRetur(tblRetur);
//                    dataPO.setNominalDiscount(new BigDecimal("0"));
//                    dataPO.setTaxPecentage(new BigDecimal("0"));
//                    dataPO.setDeliveryCost(new BigDecimal("0"));
//                    dataPO.setCodePo(ClassCoder.getCode("Purchase Order", session));
//                    dataPO.setPodate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 1));  //1 = 'Approved'
//                    dataPO.setRefPurchaseOrderItemArriveStatus(session.find(RefPurchaseOrderItemArriveStatus.class, 0));
//                    dataPO.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 0));
//                    dataPO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataPO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    dataPO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(dataPO);
//                    //data PO - detail
//                    BigDecimal totalPO = new BigDecimal("0");
//                    BigDecimal totalTax = new BigDecimal("0");
//                    for (ReturController.DetailLocation detailLocation : detailLocations) {
//                        TblPurchaseOrderDetail dataPODetail = new TblPurchaseOrderDetail();
//                        dataPODetail.setTblPurchaseOrder(dataPO);
////                        dataPODetail.setTblItem(detailLocation.getTblDetail().getTblItem());
//                        dataPODetail.setItemCost(detailLocation.getTblDetail().getItemCost());
//                        dataPODetail.setItemDiscount(detailLocation.getTblDetail().getItemDiscount());
//                        dataPODetail.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
//                        dataPODetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataPODetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        dataPODetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        dataPODetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        dataPODetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                        session.saveOrUpdate(dataPODetail);
//                        //calculation total PO n total tax percentage
//                        BigDecimal totalItemCost = (detailLocation.getTblDetail().getItemCost().subtract(detailLocation.getTblDetail().getItemDiscount()))
//                                .multiply(detailLocation.getTblDetail().getItemQuantity());
//                        totalPO = totalPO.add(totalItemCost);
//                        totalTax = totalTax.add(totalItemCost.multiply(detailLocation.getTblDetail().getItemTaxPercentage()));
//                    }
//                    //data hotel payable
//                    TblHotelPayable hotelPayable = new TblHotelPayable();
//                    hotelPayable.setHotelPayableNominal(new BigDecimal("0"));
//                    hotelPayable.setRefHotelPayableType(session.find(RefHotelPayableType.class, 1));   //Purchase Order = '1'                
//                    hotelPayable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 2));    //Sudah Dibayar = '2'
//                    hotelPayable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelPayable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelPayable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelPayable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelPayable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelPayable);
//                    //data hotel finnace transaction
//                    TblHotelFinanceTransaction hotelFinanceTransaction = new TblHotelFinanceTransaction();
////                    hotelFinanceTransaction.setTblHotelPayable(hotelPayable);1234567890---
//                    hotelFinanceTransaction.setRefFinanceTransactionType(session.find(RefFinanceTransactionType.class, 0));    //payable = 0'
//                    hotelFinanceTransaction.setTransactionNominal(new BigDecimal("0"));
//                    hotelFinanceTransaction.setTransactionRoundingValue(new BigDecimal("0"));
////                hotelFinanceTransaction.setSenderName();
////                hotelFinanceTransaction.setTblBankAccountBySenderBankAccount();
////                hotelFinanceTransaction.setReceiverName();
////                hotelFinanceTransaction.setTblBankAccountByReceiverBankAccount();
//                    hotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
//                    hotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelFinanceTransaction);
//                    //set n update data PO (tax percentage)
//                    dataPO.setTblHotelPayable(hotelPayable);
//                    dataPO.setTaxPecentage(totalTax.divide(totalPO));
//                    dataPO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    dataPO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    session.update(dataPO);
                } else {  //2 = 'Kembali Uang'
                    //data hotel invoice
                    TblHotelInvoice hotelInvoice = new TblHotelInvoice();
                    hotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
                    hotelInvoice.setTblPartner(null);
                    hotelInvoice.setTblSupplier(tblRetur.getTblSupplier());
                    hotelInvoice.setRefHotelInvoiceType(session.find(RefHotelInvoiceType.class, 1));    //Receivable = '1'
                    hotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hotelInvoice);
                    //data hotel receivable
                    TblHotelReceivable hotelReceivable = new TblHotelReceivable();
                    hotelReceivable.setTblHotelInvoice(hotelInvoice);
                    hotelReceivable.setHotelReceivableNominal(calculationTotalReturPV2(tblRetur,
                            detailLocations));
                    hotelReceivable.setRefHotelReceivableType(session.find(RefHotelReceivableType.class, 2));   //Retur = '2'
                    hotelReceivable.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 0));    //Belum Dibayar = '0'
                    hotelReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hotelReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hotelReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hotelReceivable);
                    //data retur - (hotel receivable)
                    tblRetur.setTblHotelReceivable(hotelReceivable);
                    session.saveOrUpdate(tblRetur);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblRetur;
    }

    private TblItemLocation getItemLocationByIDLocationAndIDItem(long idLocation, long idItem) {
        List<TblItemLocation> list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                .setParameter("idLocation", idLocation)
                .setParameter("idItem", idItem)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblItemLocationPropertyBarcode getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(long idItemLocation, long idPropertyBarcode) {
        List<TblItemLocationPropertyBarcode> list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode")
                .setParameter("idrelation", idItemLocation)
                .setParameter("idproperty", idPropertyBarcode)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private BigDecimal calculationTotalRetur(
            TblRetur dataRetur,
            List<ReturController.DetailLocation> detailLocations) {
        BigDecimal result = new BigDecimal("0");
        for (ReturController.DetailLocation detailLocation : detailLocations) {
            result = result.add((((new BigDecimal("1")).add(detailLocation.getTblDetail().getItemTaxPercentage()))
                    .multiply((detailLocation.getTblDetail().getItemCost().subtract(detailLocation.getTblDetail().getItemDiscount()))
                            .multiply(detailLocation.getTblDetail().getItemQuantity())))
                    .add(dataRetur.getDeliveryCost()));
        }
        return result;
    }

    private BigDecimal calculationTotalReturWV2(
            TblRetur dataRetur,
            List<ReturWController.DetailLocation> detailLocations) {
        BigDecimal result = new BigDecimal("0");
        for (ReturWController.DetailLocation detailLocation : detailLocations) {
            result = result.add((((new BigDecimal("1")).add(detailLocation.getTblDetail().getItemTaxPercentage()))
                    .multiply((detailLocation.getTblDetail().getItemCost().subtract(detailLocation.getTblDetail().getItemDiscount()))
                            .multiply(detailLocation.getTblDetail().getItemQuantity())))
                    .add(dataRetur.getDeliveryCost()));
        }
        return result;
    }

    private BigDecimal calculationTotalReturPV2(
            TblRetur dataRetur,
            List<ReturPController.DetailLocation> detailLocations) {
        BigDecimal result = new BigDecimal("0");
        for (ReturPController.DetailLocation detailLocation : detailLocations) {
            result = result.add((((new BigDecimal("1")).add(detailLocation.getTblDetail().getItemTaxPercentage()))
                    .multiply((detailLocation.getTblDetail().getItemCost().subtract(detailLocation.getTblDetail().getItemDiscount()))
                            .multiply(detailLocation.getTblDetail().getItemQuantity())))
                    .add(dataRetur.getDeliveryCost()));
        }
        return result;
    }

    @Override
    public boolean updateDataRetur(TblRetur retur,
            List<ReturController.DetailLocation> detailLocations) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            //data retur
////            retur.setRefReturStatus(session.find(RefReturStatus.class, 0));
//            retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            session.update(retur);
//            //data retur detail
//            //delete all
//            Query rs = session.getNamedQuery("deleteAllTblReturDetailByIDRetur")
//                    .setParameter("idRetur", retur.getIdretur())
//                    .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
//            errMsg = (String) rs.uniqueResult();
//            if (!errMsg.equals("")) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                return false;
//            }
//            //data detail - location
//            for (ReturController.DetailLocation detailLocation : detailLocations) {
//                //save or update data retur detail
//                detailLocation.getTblDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now());
//                detailLocation.getTblDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                detailLocation.getTblDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                detailLocation.getTblDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                detailLocation.getTblDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(detailLocation.getTblDetail());
//                //data item mutation history (create)
//                TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
//                dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblItem());
//                dataMutationHistory.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
//                dataMutationHistory.setTblLocationByIdlocationOfSource(detailLocation.getTblDetail().getTblLocation());
//                dataMutationHistory.setTblLocationByIdlocationOfDestination(retur.getTblMemorandumInvoice().getTblPurchaseOrder().getTblSupplier().getTblLocation());
//                dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(dataMutationHistory);
//                //data item location (source, udpate:minus)
//                TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfSource().getIdlocation(),
//                        dataMutationHistory.getTblItem().getIditem());
//                tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataMutationHistory.getItemQuantity()));
//                tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                if (tempItemSourceLocation.getItemQuantity().compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
//                    errMsg = "Stock item (in warehouse) not enough for retur..!";
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    return false;
//                }
//                session.update(tempItemSourceLocation);
//            }
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//            return false;
//        } finally {
//            //session.close();
//        }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDataReturV2(TblRetur retur,
            List<ReturPController.DetailLocation> detailLocations) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            //data retur
////            retur.setRefReturStatus(session.find(RefReturStatus.class, 0));
//            retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            session.update(retur);
//            //data retur detail
//            //delete all
//            Query rs = session.getNamedQuery("deleteAllTblReturDetailByIDRetur")
//                    .setParameter("idRetur", retur.getIdretur())
//                    .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
//            errMsg = (String) rs.uniqueResult();
//            if (!errMsg.equals("")) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                return false;
//            }
//            //data detail - location
//            for (ReturController.DetailLocation detailLocation : detailLocations) {
//                //save or update data retur detail
//                detailLocation.getTblDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now());
//                detailLocation.getTblDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                detailLocation.getTblDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                detailLocation.getTblDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                detailLocation.getTblDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(detailLocation.getTblDetail());
//                //data item mutation history (create)
//                TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
//                dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblItem());
//                dataMutationHistory.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
//                dataMutationHistory.setTblLocationByIdlocationOfSource(detailLocation.getTblDetail().getTblLocation());
//                dataMutationHistory.setTblLocationByIdlocationOfDestination(retur.getTblMemorandumInvoice().getTblPurchaseOrder().getTblSupplier().getTblLocation());
//                dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(dataMutationHistory);
//                //data item location (source, udpate:minus)
//                TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfSource().getIdlocation(),
//                        dataMutationHistory.getTblItem().getIditem());
//                tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataMutationHistory.getItemQuantity()));
//                tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                if (tempItemSourceLocation.getItemQuantity().compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
//                    errMsg = "Stock item (in warehouse) not enough for retur..!";
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    return false;
//                }
//                session.update(tempItemSourceLocation);
//            }
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//            return false;
//        } finally {
//            //session.close();
//        }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean approveDataReturPV2(TblRetur retur) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data retur
                retur.setDeliveryNumber(ClassCoder.getCode("Surat Jalan", session));
                retur.setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
                retur.setTblEmployeeByApprovedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                retur.setRefReturStatus(session.find(RefReturStatus.class, 1)); //Disetujui = '1'
                retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(retur);
                //data retur detail
                List<TblReturDetail> returDetails = getAllReturDetailByIDRetur(retur.getIdretur());
                for (TblReturDetail returDetail : returDetails) {
                    //data retur detail
                    returDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    returDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(returDetail);
                    //data item mutation history (create)
                    TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
                    dataMutationHistory.setTblItem(returDetail.getTblSupplierItem().getTblItem());
                    dataMutationHistory.setItemQuantity(returDetail.getItemQuantity());
                    dataMutationHistory.setTblLocationByIdlocationOfSource(returDetail.getTblLocation());
                    dataMutationHistory.setTblLocationByIdlocationOfDestination(retur.getTblSupplier().getTblLocation());
//                    dataMutationHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationHistory.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationHistory.setRefItemMutationType(session.find(RefItemMutationType.class, 2)); //Rusak = '2'
                    dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(dataMutationHistory);
                    //data item location (source, udpate:minus)
                    TblItemLocation tempItemSourceLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfSource().getIdlocation(),
                            dataMutationHistory.getTblItem().getIditem());
                    if (tempItemSourceLocation == null) {
                        errMsg = "Barang (" + dataMutationHistory.getTblItem().getItemName() + ") tidak ditemukan pada Lokasi (yang dipilih)..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    tempItemSourceLocation.setItemQuantity(tempItemSourceLocation.getItemQuantity().subtract(dataMutationHistory.getItemQuantity()));
                    tempItemSourceLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tempItemSourceLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    if (tempItemSourceLocation.getItemQuantity()
                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                        errMsg = "Stok Barang (di gudang) tidak cukup untuk di-retur..!";
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                    session.update(tempItemSourceLocation);
                    //data item mutation - property barcode
                    if (returDetail.getTblSupplierItem().getTblItem().getPropertyStatus()) {  //Property
                        List<TblReturDetailPropertyBarcode> returDetailPBs = getAllReturDetailPropertyBarcodeByIDReturDetail(returDetail.getIddetailRetur());
                        for (TblReturDetailPropertyBarcode returDetailPB : returDetailPBs) {
                            //data retur detail - property barcode
                            returDetailPB.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            returDetailPB.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.saveOrUpdate(returDetailPB);
                            //data item mutation - property barcode
                            TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            itemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dataMutationHistory);
                            itemMutationHistoryPropertyBarcode.setTblPropertyBarcode(returDetailPB.getTblPropertyBarcode());
                            itemMutationHistoryPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemMutationHistoryPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            itemMutationHistoryPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemMutationHistoryPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            itemMutationHistoryPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(itemMutationHistoryPropertyBarcode);
                            //data item location property barcode (source:remove)
                            TblItemLocationPropertyBarcode tempItemSourceLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(tempItemSourceLocation.getIdrelation(),
                                    returDetailPB.getTblPropertyBarcode().getIdbarcode());
                            if (tempItemSourceLocationPropertyBarcode != null) {
                                tempItemSourceLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                tempItemSourceLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                tempItemSourceLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                session.update(tempItemSourceLocationPropertyBarcode);
                            } else {
                                errMsg = "Properti (" + returDetailPB.getTblPropertyBarcode().getCodeBarcode() + ") tidak ditemukan pada Lokasi (yang dipilih)..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return false;
                            }
                        }
                    }
                    //data retur detail - item expired date
                    if (returDetail.getTblSupplierItem().getTblItem().getConsumable()) {   //consumable
                        List<TblReturDetailItemExpiredDate> returDetailIEDs = getAllReturDetailItemExpiredDateByIDReturDetail(returDetail.getIddetailRetur());
                        for (TblReturDetailItemExpiredDate returDetailIED : returDetailIEDs) {
                            //data retur detail - item expired date
                            returDetailIED.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            returDetailIED.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.saveOrUpdate(returDetailIED);
                            //data item mutation - item expired date
                            TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                            itemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dataMutationHistory);
                            itemMutationHistoryItemExpiredDate.setTblItemExpiredDate(returDetailIED.getTblItemExpiredDate());
                            itemMutationHistoryItemExpiredDate.setItemQuantity(returDetailIED.getItemQuantity());
                            itemMutationHistoryItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemMutationHistoryItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            itemMutationHistoryItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemMutationHistoryItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            itemMutationHistoryItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(itemMutationHistoryItemExpiredDate);
                            //item location - source (-)
                            TblItemLocationItemExpiredDate iliedSource = getItemLocationItemExpiredDateByIDItemLocationAndItemExpiredDate(
                                    tempItemSourceLocation.getIdrelation(),
                                    returDetailIED.getTblItemExpiredDate().getIditemExpiredDate());
                            if (iliedSource == null) {
                                errMsg = "Barang (" + returDetailIED.getTblItemExpiredDate().getTblItem().getItemName()
                                        + " : " + ClassFormatter.dateFormate.format(returDetailIED.getTblItemExpiredDate().getItemExpiredDate())
                                        + ") tidak ditemukan pada Lokasi (yang dipilih)..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return false;
                            }
                            iliedSource.setItemQuantity(iliedSource.getItemQuantity().subtract(returDetailIED.getItemQuantity()));
                            iliedSource.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            iliedSource.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            if (iliedSource.getItemQuantity()
                                    .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                errMsg = "Stok Barang (di gudang) tidak cukup untuk di-retur..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return false;
                            }
                            session.update(iliedSource);
                        }
                    }
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            } finally {
                //session.close();
            }
        } else {
            return false;
        }
        return true;
    }

    private List<TblReturDetail> getAllReturDetailByIDRetur(long idRetur) {
        List<TblReturDetail> list = session.getNamedQuery("findAllTblReturDetailByIDRetur")
                .setParameter("idRetur", idRetur)
                .list();
        return list;
    }

    private List<TblReturDetailPropertyBarcode> getAllReturDetailPropertyBarcodeByIDReturDetail(long idReturDetail) {
        List<TblReturDetailPropertyBarcode> list = session.getNamedQuery("findAllTblReturDetailPropertyBarcodeByIDReturDetail")
                .setParameter("idReturDetail", idReturDetail)
                .list();
        return list;
    }

    private List<TblReturDetailItemExpiredDate> getAllReturDetailItemExpiredDateByIDReturDetail(long idReturDetail) {
        List<TblReturDetailItemExpiredDate> list = session.getNamedQuery("findAllTblReturDetailItemExpiredDateByIDReturDetail")
                .setParameter("idReturDetail", idReturDetail)
                .list();
        return list;
    }

    private TblItemLocationItemExpiredDate getItemLocationItemExpiredDateByIDItemLocationAndItemExpiredDate(
            long idItemLocation,
            long idItemExpiredDate) {
        List<TblItemLocationItemExpiredDate> list = session.getNamedQuery("findAllTblItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate")
                .setParameter("idItemLocation", idItemLocation)
                .setParameter("idItemExpiredDate", idItemExpiredDate)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }
    
    @Override
    public boolean deleteDataRetur(TblRetur retur) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (retur.getRefRecordStatus().getIdstatus() == 1) {
                    //data retur
                    retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    retur.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(retur);
                    //data retur detail
                    Query rs = session.getNamedQuery("deleteAllTblReturDetailByIDRetur")
                            .setParameter("idRetur", retur.getIdretur())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Data tidak dapat dihapus!!";
                    return false;
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            } finally {
                //session.close();
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataReturWV2(TblRetur retur) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (retur.getRefRecordStatus().getIdstatus() == 1) {
                    //data retur
                    retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    retur.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(retur);
                    //data retur detail
                    Query rs = session.getNamedQuery("deleteAllTblReturDetailByIDRetur")
                            .setParameter("idRetur", retur.getIdretur())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) rs.uniqueResult();
                    if (!errMsg.equals("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                        return false;
                    }
                } else {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    errMsg = "Data tidak dapat dihapus!!";
                    return false;
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            } finally {
                //session.close();
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataReturPV2(TblRetur retur) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data retur
                retur.setCanceledDate(Timestamp.valueOf(LocalDateTime.now()));
                retur.setTblEmployeeByCanceledBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                retur.setRefReturStatus(session.find(RefReturStatus.class, 2)); //Dibatalkan = '2'
                retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(retur);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return false;
            } finally {
                //session.close();
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public TblRetur getDataRetur(long id) {
        errMsg = "";
        TblRetur data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblRetur) session.find(TblRetur.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblRetur> getAllDataRetur() {
        errMsg = "";
        List<TblRetur> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRetur").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefReturType getDataReturType(int id) {
        errMsg = "";
        RefReturType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReturType) session.find(RefReturType.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<RefReturType> getAllDataReturType() {
        errMsg = "";
        List<RefReturType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefReturType").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefReturStatus getDataReturStatus(int id) {
        errMsg = "";
        RefReturStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReturStatus) session.find(RefReturStatus.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblReturDetail getDataReturDetail(long id) {
        errMsg = "";
        TblReturDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblReturDetail) session.find(TblReturDetail.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblReturDetail> getAllDataReturDetail() {
        errMsg = "";
        List<TblReturDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturDetail").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblReturDetail> getAllDataReturDetailByIDRetur(long idRetur) {
        errMsg = "";
        List<TblReturDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturDetailByIDRetur")
                        .setParameter("idRetur", idRetur)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblSupplier getDataSupplier(long id) {
        errMsg = "";
        TblSupplier data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSupplier) session.find(TblSupplier.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblSupplier> getAllDataSupplier() {
        errMsg = "";
        List<TblSupplier> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplier").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblMemorandumInvoice getDataMemorandumInvoice(long id) {
        errMsg = "";
        TblMemorandumInvoice data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblMemorandumInvoice) session.find(TblMemorandumInvoice.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblMemorandumInvoice> getAllDataMemorandumInvoice() {
        errMsg = "";
        List<TblMemorandumInvoice> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoice").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblMemorandumInvoice> getAllDataMemorandumInvoiceByIDPurchaseOrder(long idPO) {
        errMsg = "";
        List<TblMemorandumInvoice> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceByIDPurchaseOrder")
                        .setParameter("idPurchaseOrder", idPO)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(long idMemorandumInvoice) {
        errMsg = "";
        List<TblMemorandumInvoiceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailByIDMemorandumInvoice")
                        .setParameter("idMemorandumInvoice", idMemorandumInvoice)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblPurchaseOrder getDataPurchaseOrder(long id) {
        errMsg = "";
        TblPurchaseOrder data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPurchaseOrder) session.find(TblPurchaseOrder.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByIDSupplier(long idSupplier) {
        errMsg = "";
        List<TblPurchaseOrder> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderByIDSupplier")
                        .setParameter("idSupplier", idSupplier)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public RefPurchaseOrderStatus getDataPurchaseOrderStatus(int id) {
        errMsg = "";
        RefPurchaseOrderStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPurchaseOrderStatus) session.find(RefPurchaseOrderStatus.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public RefPurchaseOrderItemArriveStatus getDataPurchaseOrderItemArriveStatus(int id) {
        errMsg = "";
        RefPurchaseOrderItemArriveStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPurchaseOrderItemArriveStatus) session.find(RefPurchaseOrderItemArriveStatus.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public RefPurchaseOrderPaymentStatus getDataPurchaseOrderPaymentStatus(int id) {
        errMsg = "";
        RefPurchaseOrderPaymentStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPurchaseOrderPaymentStatus) session.find(RefPurchaseOrderPaymentStatus.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblItem getDataItem(long id) {
        errMsg = "";
        TblItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItem) session.find(TblItem.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public TblUnit getDataUnit(long id) {
        errMsg = "";
        TblUnit data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblUnit) session.find(TblUnit.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public RefItemType getDataItemType(int id) {
        errMsg = "";
        RefItemType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefItemType) session.find(RefItemType.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public RefItemGuestType getDataItemGuestType(int id) {
        errMsg = "";
        RefItemGuestType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefItemGuestType) session.find(RefItemGuestType.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<RefItemGuestType> getAllDataItemGuestType() {
        errMsg = "";
        List<RefItemGuestType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefItemGuestType").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblLocationOfWarehouse getWarehouse(long id) {
        errMsg = "";
        TblLocationOfWarehouse data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblLocationOfWarehouse) session.find(TblLocationOfWarehouse.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblLocationOfWarehouse> getAllDataWarehouse() {
        errMsg = "";
        List<TblLocationOfWarehouse> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfWarehouse").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfWarehouse> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfWarehouseByIdLocation")
                        .setParameter("id", idLocation)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblLocation getDataLocation(long id) {
        errMsg = "";
        TblLocation data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblLocation) session.find(TblLocation.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblMemorandumInvoiceDetail getDataMemorandumInvoiceDetailByIDMemorandumInvoiceAndIDSupplierItem(long idMI, long idSupplierItem) {
        errMsg = "";
        List<TblMemorandumInvoiceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailByIDMemorandumInvoiceAndIDSupplierItem")
                        .setParameter("idMemorandumInvoice", idMI)
                        .setParameter("idSupplierItem", idSupplierItem)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TblReturDetail> getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(long idMI, long idSupplierItem) {
        errMsg = "";
        List<TblReturDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturDetailByIDMemorandumInvoiceAndIDSupplierItem")
                        .setParameter("idMemorandumInvoice", idMI)
                        .setParameter("idSupplierItem", idSupplierItem)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblReturDetailPropertyBarcode getDataReturDetailPropertyBarcodeByIDPropertyBarcode(long idPropertyBarcode) {
        errMsg = "";
        List<TblReturDetailPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturDetailPropertyBarcodeByIDPropertyBarcode")
                        .setParameter("idPropertyBarcode", idPropertyBarcode)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public TblPurchaseOrderDetail getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(long idPO, long idSupplierItem) {
        errMsg = "";
        List<TblPurchaseOrderDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem")
                        .setParameter("idPurchaseOrder", idPO)
                        .setParameter("idSupplierItem", idSupplierItem)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public TblPropertyBarcode getDataPropertyBarcode(long id) {
        errMsg = "";
        TblPropertyBarcode data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPropertyBarcode) session.find(TblPropertyBarcode.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblMemorandumInvoiceDetailPropertyBarcode> getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(long idMIDetail) {
        errMsg = "";
        List<TblMemorandumInvoiceDetailPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail")
                        .setParameter("idMemorandumInvoiceDetail", idMIDetail)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefReturPaymentStatus getDataReturPaymentStatus(int id) {
        errMsg = "";
        RefReturPaymentStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefReturPaymentStatus) session.find(RefReturPaymentStatus.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblEmployee getDataEmployee(long id) {
        errMsg = "";
        TblEmployee data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblEmployee) session.find(TblEmployee.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblEmployee> getAllDataEmployee() {
        errMsg = "";
        List<TblEmployee> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblEmployee").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblCompanyBalance getDataCompanyBalance(long id) {
        errMsg = "";
        TblCompanyBalance data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblCompanyBalance) session.find(TblCompanyBalance.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblReturDetailPropertyBarcode> getAllDataReturDetailPropertyBarcodeByIDReturDetail(long idReturDetail) {
        errMsg = "";
        List<TblReturDetailPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturDetailPropertyBarcodeByIDReturDetail")
                        .setParameter("idReturDetail", idReturDetail)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblReturDetailItemExpiredDate> getAllDataReturDetailItemExpiredDateByIDReturDetail(long idReturDetail) {
        errMsg = "";
        List<TblReturDetailItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturDetailItemExpiredDateByIDReturDetail")
                        .setParameter("idReturDetail", idReturDetail)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblReturDetailItemExpiredDate> getAllDataReturDetailItemExpiredDateByIDReturDetailAndIDItemExpiredDate(
            long idReturDetail,
            long idItemExpiredDate) {
        errMsg = "";
        List<TblReturDetailItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturDetailItemExpiredDateByIDReturDetailAndIDItemExpiredDate")
                        .setParameter("idReturDetail", idReturDetail)
                        .setParameter("idItemExpiredDate", idItemExpiredDate)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblItemExpiredDate getDataItemExpiredDate(long id) {
        errMsg = "";
        TblItemExpiredDate data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemExpiredDate) session.find(TblItemExpiredDate.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblItemExpiredDate> getAllDataItemExpiredDate() {
        errMsg = "";
        List<TblItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemExpiredDate").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItem(long idItem) {
        errMsg = "";
        List<TblItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemExpiredDateByIDItem")
                        .setParameter("idItem", idItem)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblMemorandumInvoiceDetailItemExpiredDate> getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(long idMIDetail) {
        errMsg = "";
        List<TblMemorandumInvoiceDetailItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail")
                        .setParameter("idMemorandumInvoiceDetail", idMIDetail)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblHotelReceivable getDataHotelReceivable(long id) {
        errMsg = "";
        TblHotelReceivable data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelReceivable) session.find(TblHotelReceivable.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public RefHotelReceivableType getDataHotelReceivableType(int id) {
        errMsg = "";
        RefHotelReceivableType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefHotelReceivableType) session.find(RefHotelReceivableType.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public RefFinanceTransactionStatus getDataFinanceTransactionStatus(int id) {
        errMsg = "";
        RefFinanceTransactionStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefFinanceTransactionStatus) session.find(RefFinanceTransactionStatus.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblRetur retur) {
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
//                //@@@%%%
//                //data hotel receivale (finance transaction status : update)
//                tblHotelFinanceTransaction.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelFinanceTransaction.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(tblHotelFinanceTransaction.getTblHotelReceivable());
//                //data retur
//                retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(retur);
//                //data hotel finnace transaction
//                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Receivable - Payment", session));
//                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(tblHotelFinanceTransaction);
//                //@@@%%%
//                //data company balance (kas besar) : minus (updated)
//                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);
//                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
//                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(dataBalance);
//                //data company balance (kas besar) : nominal bank account : minus (updated)
//                TblCompanyBalanceBankAccount companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, hotelFinanceTransaction.getTblBankAccountByReceiverBankAccount().getIdbankAccount());    //hotel balance = '1'
//                companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
//                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(companyBalanceBankAccount);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblHotelFinanceTransaction;
    }

    private TblCompanyBalanceBankAccount getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(long idCompanyBalance, long idBankAccount) {
        List<TblCompanyBalanceBankAccount> list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount")
                .setParameter("idCompanyBalance", idCompanyBalance)
                .setParameter("idBankAccount", idBankAccount)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCash hotelFinanceTransactionWithCash,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs,
            String supplierName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Receivable - Payment", session));
                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelFinanceTransaction);
                //data hotel finance transaction - with transfer
                hotelFinanceTransactionWithCash.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithCash.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithCash.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithCash.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithCash.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(hotelFinanceTransactionWithCash);
                //data hotel finnace transaction - hotel receivable
                for (TblHotelFinanceTransactionHotelReceivable hfthr : hfthrs) {
                    hfthr.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthr);
                    //data hotel receivable
                    hfthr.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthr.getTblHotelReceivable());
                    //data reservation payment with guarantee payment...???
//                    TblReservationPaymentWithGuaranteePayment rpwgp = getReservationPaymentWithGuaranteePaymentByIDHotelReceivable(hfthr.getTblHotelReceivable().getIdhotelReceivable());
//                    if (rpwgp != null) {
//                        if(hfthr.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 1){ //Dibayar Sebagian = '1'
//                            rpwgp.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
//                        }else{  //Sudah Dibayar = '2'
//                            rpwgp.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 2));   //Dibayar Sebagian = '2'
//                        }
//                        rpwgp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                        rpwgp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                        session.saveOrUpdate(rpwgp);
//                    }
                }
                //@@@%%%
                //data company balance (kas) : plus/minus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, hotelFinanceTransactionWithCash.getTblCompanyBalance().getIdbalance());
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                } else {  //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran piutang kepada " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran piutang oleh " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblHotelFinanceTransaction;
    }

    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithTransfer hotelFinanceTransactionWithTransfer,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs,
            String supplierName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Receivable - Payment", session));
                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelFinanceTransaction);
                //data hotel finance transaction - with transfer
                hotelFinanceTransactionWithTransfer.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithTransfer.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithTransfer.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithTransfer.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithTransfer.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(hotelFinanceTransactionWithTransfer);
                //data hotel finnace transaction - hotel receivable
                for (TblHotelFinanceTransactionHotelReceivable hfthr : hfthrs) {
                    hfthr.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthr);
                    //data hotel receivable
                    hfthr.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthr.getTblHotelReceivable());
                    //data retur
                    TblRetur retur = getReturByIDHotelReceivable(hfthr.getTblHotelReceivable().getIdhotelReceivable());
                    if (retur != null) {
                        if (hfthr.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 1) { //Dibayar Sebagian = '1'
                            retur.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
                        } else {  //Sudah Dibayar = '2'
                            retur.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 2));   //Dibayar Sebagian = '2'
                        }
                        retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(retur);
                    }
                }
                //@@@%%%
                //data company balance (kas besar) : plus/minus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);    //Kas Besar = '1'
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (kas besar) : nominal bank account : plus (updated)
                TblCompanyBalanceBankAccount companyBalanceBankAccount;
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount().getIdbankAccount());
                } else {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                }
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //minus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //plus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(hotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount());
                } else {  //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(hotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran piutang kepada " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran piutang oleh " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblHotelFinanceTransaction;
    }

    @Override
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCekGiro hotelFinanceTransactionWithCekGiro,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs,
            String supplierName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Receivable - Payment", session));
                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelFinanceTransaction);
                //data hotel finance transaction - with cek/giro
                hotelFinanceTransactionWithCekGiro.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithCekGiro.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithCekGiro.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                hotelFinanceTransactionWithCekGiro.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                hotelFinanceTransactionWithCekGiro.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(hotelFinanceTransactionWithCekGiro);
                //data hotel finnace transaction - hotel receivable
                for (TblHotelFinanceTransactionHotelReceivable hfthr : hfthrs) {
                    hfthr.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthr.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthr);
                    //data hotel receivable
                    hfthr.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthr.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthr.getTblHotelReceivable());
                    //data retur
                    TblRetur retur = getReturByIDHotelReceivable(hfthr.getTblHotelReceivable().getIdhotelReceivable());
                    if (retur != null) {
                        if (hfthr.getTblHotelReceivable().getRefFinanceTransactionStatus().getIdstatus() == 1) { //Dibayar Sebagian = '1'
                            retur.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
                        } else {  //Sudah Dibayar = '2'
                            retur.setRefReturPaymentStatus(session.find(RefReturPaymentStatus.class, 2));   //Sudah Dibayar = '2'
                        }
                        retur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        retur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(retur);
                    }
                }
                //@@@%%%
                //data company balance (kas besar) : plus/minus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);    //Kas Besar = '1'
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (kas besar) : nominal bank account : plus/minus (updated)
                TblCompanyBalanceBankAccount companyBalanceBankAccount;
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount().getIdbankAccount());
                } else {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                }
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //minus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //plus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                }
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(hotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount());
                } else {  //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(hotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran piutang kepada " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran piutang oleh " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                }
                session.saveOrUpdate(logCompanyBalanceCashFlow);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblHotelFinanceTransaction;
    }

    private TblRetur getReturByIDHotelReceivable(long idHotelReceivable) {
        List<TblRetur> list = session.getNamedQuery("findAllTblReturByIDHotelReceivable")
                .setParameter("idHotelReceivable", idHotelReceivable)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TblHotelFinanceTransaction> getAllDataHotelFinanceTransactionByIDHotelReceivable(long idHotelReceivable) {
        errMsg = "";
        List<TblHotelFinanceTransaction> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionByIDHotelReceivable")
                        .setParameter("idHotelReceivable", idHotelReceivable)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public RefFinanceTransactionType getDataFinanceTransactionType(int id) {
        errMsg = "";
        RefFinanceTransactionType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefFinanceTransactionType) session.find(RefFinanceTransactionType.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblSupplierBankAccount insertDataSupplierBankAccount(TblSupplierBankAccount supplierBankAccount) {
        TblSupplierBankAccount tblSupplierBankAccount = supplierBankAccount;
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //save data bank account n supplier bank acccount
                //data bank account
                tblSupplierBankAccount.getTblBankAccount().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSupplierBankAccount.getTblBankAccount().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSupplierBankAccount.getTblBankAccount().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSupplierBankAccount.getTblBankAccount().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSupplierBankAccount.getTblBankAccount().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblSupplierBankAccount.getTblBankAccount());
                //data supplier - bank account
                tblSupplierBankAccount.setRefBankAccountHolderStatus(session.find(RefBankAccountHolderStatus.class, 0));
                tblSupplierBankAccount.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSupplierBankAccount.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSupplierBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblSupplierBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblSupplierBankAccount.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblSupplierBankAccount);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblSupplierBankAccount;
    }

    @Override
    public TblSupplierBankAccount getDataSupplierBankAccount(long id) {
        errMsg = "";
        TblSupplierBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSupplierBankAccount) session.find(TblSupplierBankAccount.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblSupplierBankAccount> getAllDataSupplierBankAccount() {
        errMsg = "";
        List<TblSupplierBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierBankAccount").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblSupplierBankAccount> getAllDataSupplierBankAccountByIDSupplier(long idSupplier) {
        errMsg = "";
        List<TblSupplierBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierBankAccountByIDSupplier")
                        .setParameter("idSupplier", idSupplier)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccount(long idCompanyBalance) {
        errMsg = "";
        List<TblCompanyBalanceBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalance")
                        .setParameter("idCompanyBalance", idCompanyBalance)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblBankAccount getBankAccount(long id) {
        errMsg = "";
        TblBankAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBankAccount) session.find(TblBankAccount.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblBankAccount> getAllDataBankAccount() {
        errMsg = "";
        List<TblBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBankAccount").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
//    @Override
//    public RefBankAccountHolderStatus getBankAccountHolderStatus(int id) {
//        errMsg = "";
//        RefBankAccountHolderStatus data = null;
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            data = (RefBankAccountHolderStatus) session.find(RefBankAccountHolderStatus.class, id);
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//        } finally {
//            //session.close();
//        }
//        return data;
//    }
//
//    @Override
//    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus() {
//        errMsg = "";
//        List<RefBankAccountHolderStatus> list = new ArrayList<>();
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            list = session.getNamedQuery("findAllRefBankAccountHolderStatus").list();
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            if (session.getTransaction().isActive()) {
//                session.getTransaction().rollback();
//            }
//            errMsg = e.getMessage();
//        } finally {
//            //session.close();
//        }
//        return list;
//    }
    //--------------------------------------------------------------------------
    @Override
    public TblBank getDataBank(long id) {
        errMsg = "";
        TblBank data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBank) session.find(TblBank.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblBank> getAllDataBank() {
        errMsg = "";
        List<TblBank> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblBank").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public SysDataHardCode getDataSysDataHardCode(long id) {
        errMsg = "";
        SysDataHardCode data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (SysDataHardCode) session.find(SysDataHardCode.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<SysDataHardCode> getAllDataSysDataHardCode() {
        errMsg = "";
        List<SysDataHardCode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllSysDataHardCode").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
//    @Override
//    public TblHotelInvoice insertDataHotelInvoice(TblHotelInvoice hotelInvoice,
//            List<TblHotelInvoiceReceivable> hotelInvoiceReceivables) {
//        errMsg = "";
//        TblHotelInvoice tblHotelInvoice = hotelInvoice;
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                //data hotel invoice
//                tblHotelInvoice.setCodeHotelInvoice(ClassCoder.getCode("Hotel Invoice", session));
//                tblHotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(tblHotelInvoice);
//                //insert all data hotel invoice receivable
//                for (TblHotelInvoiceReceivable hotelInvoiceReceivable : hotelInvoiceReceivables) {
//                    hotelInvoiceReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoiceReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoiceReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoiceReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoiceReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelInvoiceReceivable);
//                }
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//                return null;
//            } finally {
//                //session.close();
//            }
//        } else {
//            return null;
//        }
//        return tblHotelInvoice;
//    }
//    
//    @Override
//    public boolean updateDataHotelInvoice(TblHotelInvoice hotelInvoice,
//            List<TblHotelInvoiceReceivable> hotelInvoiceReceivables) {
//        errMsg = "";
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                //data hotel invoice
//                hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(hotelInvoice);
//                //delete all (data hotel invoice receivable)
//                Query rs = session.getNamedQuery("deleteAllTblHotelInvoiceReceivable")
//                        .setParameter("idHotelInvoice", hotelInvoice.getIdhotelInvoice())
//                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
//                errMsg = (String) rs.uniqueResult();
//                if (!errMsg.equals("")) {
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    return false;
//                }
//                //insert all data hotel invoice receivable
//                for (TblHotelInvoiceReceivable hotelInvoiceReceivable : hotelInvoiceReceivables) {
//                    hotelInvoiceReceivable.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoiceReceivable.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoiceReceivable.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoiceReceivable.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoiceReceivable.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(hotelInvoiceReceivable);
//                }
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//                return false;
//            } finally {
//                //session.close();
//            }
//        } else {
//            return false;
//        }
//        return true;
//    }
//    
//    @Override
//    public boolean deleteDataHotelInvoice(TblHotelInvoice hotelInvoice) {
//        errMsg = "";
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                if (hotelInvoice.getRefRecordStatus().getIdstatus() == 1) {
//                    hotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    hotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    hotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
//                    session.update(hotelInvoice);
//                    //delete all (data hotel invoice receivable)
//                    Query rs = session.getNamedQuery("deleteAllTblHotelInvoiceReceivable")
//                            .setParameter("idHotelInvoice", hotelInvoice.getIdhotelInvoice())
//                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
//                    errMsg = (String) rs.uniqueResult();
//                    if (!errMsg.equals("")) {
//                        if (session.getTransaction().isActive()) {
//                            session.getTransaction().rollback();
//                        }
//                        return false;
//                    }
//                } else {
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    errMsg = "Data tidak dapat dihapus!!";
//                    return false;
//                }
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//                return false;
//            } finally {
//                //session.close();
//            }
//        } else {
//            return false;
//        }
//        return true;
//    }
    @Override
    public TblHotelInvoice insertDataHotelInvoice(TblRetur dataRetur) {
        errMsg = "";
        TblRetur tblRetur = dataRetur;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel invoice (update)
                tblRetur.getTblHotelReceivable().getTblHotelInvoice().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.getTblHotelReceivable().getTblHotelInvoice().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.saveOrUpdate(tblRetur.getTblHotelReceivable().getTblHotelInvoice());
                //data hotel payable (update)
                tblRetur.getTblHotelReceivable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.getTblHotelReceivable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.saveOrUpdate(tblRetur.getTblHotelReceivable());
                //data retur (update)
                tblRetur.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblRetur.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(tblRetur);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
                return null;
            } finally {
                //session.close();
            }
        } else {
            return null;
        }
        return tblRetur.getTblHotelReceivable().getTblHotelInvoice();
    }

    @Override
    public TblHotelInvoice getDataHotelInvoice(long id) {
        errMsg = "";
        TblHotelInvoice data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelInvoice) session.find(TblHotelInvoice.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblHotelInvoice> getAllDataHotelInvoice() {
        errMsg = "";
        List<TblHotelInvoice> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelInvoice").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblHotelInvoice> getAllDataHotelInvoiceByIDSupplierNotNullAndIDHotelInvoiceType(int idHotelInvoiceType) {
        errMsg = "";
        List<TblHotelInvoice> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelInvoiceByIDSupplierNotNullAndIDHotelInvoiceType")
                        .setParameter("idHotelInvoiceType", idHotelInvoiceType)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefHotelInvoiceType getDataHotelInvoiceType(int id) {
        errMsg = "";
        RefHotelInvoiceType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefHotelInvoiceType) session.find(RefHotelInvoiceType.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<RefHotelInvoiceType> getAllDataHotelInvoiceType() {
        errMsg = "";
        List<RefHotelInvoiceType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefHotelInvoiceType").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblSupplierItem getDataSupplierItem(long id) {
        errMsg = "";
        TblSupplierItem data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSupplierItem) session.find(TblSupplierItem.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByMinPODueDate(Date poDueDate) {
        errMsg = "";
        List<TblPurchaseOrder> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderByMinPODueDate")
                        .setParameter("poDueDate", poDueDate)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------    
    @Override
    public TblHotelFinanceTransaction getDataHotelFinanceTransaction(long id) {
        errMsg = "";
        TblHotelFinanceTransaction data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelFinanceTransaction) session.find(TblHotelFinanceTransaction.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(long idHotelFinanceTransaction) {
        errMsg = "";
        List<TblHotelFinanceTransactionHotelReceivable> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction")
                        .setParameter("idHotelFinanceTransaction", idHotelFinanceTransaction)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(long idHotelReceivable) {
        errMsg = "";
        List<TblHotelFinanceTransactionHotelReceivable> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionHotelReceivableByIDHotelReceivable")
                        .setParameter("idHotelReceivable", idHotelReceivable)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblRetur getDataReturByIDHotelReceivable(long idHotelReceivable) {
        errMsg = "";
        List<TblRetur> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturByIDHotelReceivable")
                        .setParameter("idHotelReceivable", idHotelReceivable)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblRetur> getAllDataReturByIDSupplier(long idSupplier) {
        errMsg = "";
        List<TblRetur> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturByIDSupplier")
                        .setParameter("idSupplier", idSupplier)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public RefFinanceTransactionPaymentType getDataFinanceTransactionPaymentType(int id) {
        errMsg = "";
        RefFinanceTransactionPaymentType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefFinanceTransactionPaymentType) session.find(RefFinanceTransactionPaymentType.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return data;
    }

    @Override
    public List<RefFinanceTransactionPaymentType> getAllDataFinanceTransactionPaymentType() {
        errMsg = "";
        List<RefFinanceTransactionPaymentType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefFinanceTransactionPaymentType").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public List<TblCompanyBalance> getAllDataCompanyBalance() {
        errMsg = "";
        List<TblCompanyBalance> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblCompanyBalance").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public TblItemTypeHk getDataItemTypeHK(long id) {
        errMsg = "";
        TblItemTypeHk data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemTypeHk) session.find(TblItemTypeHk.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    @Override
    public List<TblItemTypeHk> getAllDataItemTypeHK() {
        errMsg = "";
        List<TblItemTypeHk> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemTypeHk")
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    @Override
    public TblItemTypeWh getDataItemTypeWH(long id) {
        errMsg = "";
        TblItemTypeWh data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblItemTypeWh) session.find(TblItemTypeWh.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }

        }
        return data;
    }

    @Override
    public List<TblItemTypeWh> getAllDataItemTypeWH() {
        errMsg = "";
        List<TblItemTypeWh> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemTypeWh")
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            } finally {
                //session.close();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public String getErrorMessage() {
        return errMsg;
    }

}
