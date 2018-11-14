/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefFinanceTransactionStatus;
import hotelfx.persistence.model.RefFinanceTransactionType;
import hotelfx.persistence.model.RefHotelInvoiceType;
import hotelfx.persistence.model.RefHotelReceivableType;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefPurchaseOrderItemArriveStatus;
import hotelfx.persistence.model.RefPurchaseOrderPaymentStatus;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
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
import java.util.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FReturManager {
    
    public TblRetur insertDataRetur(
            TblRetur retur,
            List<ReturController.DetailLocation> detailLocations
    );
    
    public TblRetur insertDataReturWV2(
            TblRetur retur,
            List<ReturWController.DetailLocation> detailLocations
    );
    
    public TblRetur insertDataReturPV2(
            TblRetur retur,
            List<ReturPController.DetailLocation> detailLocations
    );
    
    public boolean updateDataRetur(
            TblRetur retur,
            List<ReturController.DetailLocation> detailLocations
    );
    
    public boolean updateDataReturV2(
            TblRetur retur,
            List<ReturPController.DetailLocation> detailLocations
    );
    
    public boolean approveDataReturPV2(TblRetur retur);
    
    public boolean deleteDataRetur(TblRetur retur);
    
    public boolean deleteDataReturWV2(TblRetur retur);
    
    public boolean deleteDataReturPV2(TblRetur retur);
    
    public TblRetur getDataRetur(long id);
    
    public List<TblRetur> getAllDataRetur();
    
    //--------------------------------------------------------------------------
    
    public RefReturType getDataReturType(int id);
    
    public List<RefReturType> getAllDataReturType();
    
    //--------------------------------------------------------------------------
    
    public RefReturStatus getDataReturStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblReturDetail getDataReturDetail(long id);
    
    public List<TblReturDetail> getAllDataReturDetail();
    
    public List<TblReturDetail> getAllDataReturDetailByIDRetur(long idRetur);
    
    //--------------------------------------------------------------------------
    
    public TblSupplier getDataSupplier(long id);
    
    public List<TblSupplier> getAllDataSupplier();
    
    //--------------------------------------------------------------------------
    
    public TblMemorandumInvoice getDataMemorandumInvoice(long id);
    
    public List<TblMemorandumInvoice> getAllDataMemorandumInvoice();
    
    public List<TblMemorandumInvoice> getAllDataMemorandumInvoiceByIDPurchaseOrder(long idPO);
    
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(long idMemorandumInvoice);
    
    //--------------------------------------------------------------------------
    
    public TblPurchaseOrder getDataPurchaseOrder(long id);
    
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByIDSupplier(long idSupplier);
    
    public RefPurchaseOrderStatus getDataPurchaseOrderStatus(int id);
    
    public RefPurchaseOrderItemArriveStatus getDataPurchaseOrderItemArriveStatus(int id);
    
    public RefPurchaseOrderPaymentStatus getDataPurchaseOrderPaymentStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblItem getDataItem(long id);
    
    public TblUnit getDataUnit(long id);
    
    public RefItemType getDataItemType(int id);
    
    public RefItemGuestType getDataItemGuestType(int id);
    
    public List<RefItemGuestType> getAllDataItemGuestType();
    
    //--------------------------------------------------------------------------
    
    public TblLocationOfWarehouse getWarehouse(long id);

    public List<TblLocationOfWarehouse> getAllDataWarehouse();
    
    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation);
    
    public TblLocation getDataLocation(long id);
    
    //--------------------------------------------------------------------------
    
    public TblMemorandumInvoiceDetail getDataMemorandumInvoiceDetailByIDMemorandumInvoiceAndIDSupplierItem(long  idMI, long idSupplierItem);
    
    public List<TblReturDetail> getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(long  idMI, long idSupplierItem);
    
    public TblReturDetailPropertyBarcode getDataReturDetailPropertyBarcodeByIDPropertyBarcode(long idPropertyBarcode);
    
    //--------------------------------------------------------------------------
    
    public TblPurchaseOrderDetail getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(long idPO, long idSupplierItem);
    
    //--------------------------------------------------------------------------
    
    public TblPropertyBarcode getDataPropertyBarcode(long id);
    
    public List<TblMemorandumInvoiceDetailPropertyBarcode> getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(long idMIDetail);
    
    //--------------------------------------------------------------------------
    
    public RefReturPaymentStatus getDataReturPaymentStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblEmployee getDataEmployee(long id);
    
    public List<TblEmployee> getAllDataEmployee();
    
    //--------------------------------------------------------------------------
    
    public TblCompanyBalance getDataCompanyBalance(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblReturDetailPropertyBarcode> getAllDataReturDetailPropertyBarcodeByIDReturDetail(long idReturDetail);
    
     //--------------------------------------------------------------------------
    
    public List<TblReturDetailItemExpiredDate> getAllDataReturDetailItemExpiredDateByIDReturDetail(long idReturDetail);
    
    public List<TblReturDetailItemExpiredDate> getAllDataReturDetailItemExpiredDateByIDReturDetailAndIDItemExpiredDate(long idReturDetail, long idItemExpiredDate);
    
     //--------------------------------------------------------------------------
    
    public TblItemExpiredDate getDataItemExpiredDate(long id);
    
    public List<TblItemExpiredDate> getAllDataItemExpiredDate();
    
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItem(long idItem);
    
    public List<TblMemorandumInvoiceDetailItemExpiredDate> getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(long idMIDetail);
    
    //--------------------------------------------------------------------------
            
    public TblHotelReceivable getDataHotelReceivable(long id);
    
    public RefHotelReceivableType getDataHotelReceivableType(int id);
    
    public RefFinanceTransactionStatus getDataFinanceTransactionStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction, 
            TblRetur retur);
    
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCash hotelFinanceTransactionWithCash,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String supplierName);
    
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithTransfer hotelFinanceTransactionWithTransfer,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String supplierName);

    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCekGiro hotelFinanceTransactionWithCekGiro,
            List<TblHotelFinanceTransactionHotelReceivable> hfthrs, 
            String supplierName);
    
    public List<TblHotelFinanceTransaction> getAllDataHotelFinanceTransactionByIDHotelReceivable(long idHotelReceivable);
    
    public RefFinanceTransactionType getDataFinanceTransactionType(int id);
    
    //--------------------------------------------------------------------------
    
    public TblSupplierBankAccount insertDataSupplierBankAccount(TblSupplierBankAccount supplierBankAccount);
    
    public TblSupplierBankAccount getDataSupplierBankAccount(long id);
    
    public List<TblSupplierBankAccount> getAllDataSupplierBankAccount();
    
    public List<TblSupplierBankAccount> getAllDataSupplierBankAccountByIDSupplier(long idSupplier);
    
    //--------------------------------------------------------------------------
    
    public List<TblCompanyBalanceBankAccount> getAllDataCompanyBalanceBankAccount(long idCompanyBalance);
    
    //--------------------------------------------------------------------------
    
    public TblBankAccount getBankAccount(long id);
    
    public List<TblBankAccount> getAllDataBankAccount();
    
    //--------------------------------------------------------------------------
    
//    public RefBankAccountHolderStatus getBankAccountHolderStatus(int id);
//    
//    public List<RefBankAccountHolderStatus> getAllDataBankAccountHolderStatus();
    
    //--------------------------------------------------------------------------
    
    public TblBank getDataBank(long id);
    
    public List<TblBank> getAllDataBank();
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
//    public TblHotelInvoice insertDataHotelInvoice(TblHotelInvoice hotelInvoice, 
//            List<TblHotelInvoiceReceivable> hotelInvoiceReceivables);
//    
//    public boolean updateDataHotelInvoice(TblHotelInvoice hotelInvoice, 
//            List<TblHotelInvoiceReceivable> hotelInvoiceReceivables);
//    
//    public boolean deleteDataHotelInvoice(TblHotelInvoice hotelInvoice);
    
    public TblHotelInvoice insertDataHotelInvoice(TblRetur dataRetur);
    
    public TblHotelInvoice getDataHotelInvoice(long id);
    
    public List<TblHotelInvoice> getAllDataHotelInvoice();
    
    public List<TblHotelInvoice> getAllDataHotelInvoiceByIDSupplierNotNullAndIDHotelInvoiceType(int idHotelInvoiceType);
    
    //--------------------------------------------------------------------------
    
    public RefHotelInvoiceType getDataHotelInvoiceType(int id);
    
    public List<RefHotelInvoiceType> getAllDataHotelInvoiceType();
    
    //--------------------------------------------------------------------------
    
    public TblSupplierItem getDataSupplierItem(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByMinPODueDate(Date poDueDate);
    
    //--------------------------------------------------------------------------
    
    public TblHotelFinanceTransaction getDataHotelFinanceTransaction(long id);
    
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelFinanceTransaction(long idHotelFinanceTransaction);
    
    public List<TblHotelFinanceTransactionHotelReceivable> getAllDataHotelFinanceTransactionHotelReceivableByIDHotelReceivable(long idHotelReceivable);
    
    //--------------------------------------------------------------------------
    
    public TblRetur getDataReturByIDHotelReceivable(long idHotelReceivable);
    
    //--------------------------------------------------------------------------
    
    public List<TblRetur> getAllDataReturByIDSupplier(long idSupplier);
    
    //--------------------------------------------------------------------------
    public RefFinanceTransactionPaymentType getDataFinanceTransactionPaymentType(int id);

    public List<RefFinanceTransactionPaymentType> getAllDataFinanceTransactionPaymentType();
    
    //--------------------------------------------------------------------------
    
    public List<TblCompanyBalance> getAllDataCompanyBalance();
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
