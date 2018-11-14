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
import hotelfx.persistence.model.RefHotelPayableType;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefMemorandumInvoiceStatus;
import hotelfx.persistence.model.RefPurchaseOrderItemArriveStatus;
import hotelfx.persistence.model.RefPurchaseOrderPaymentStatus;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.RefPurchaseRequestStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBank;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.persistence.model.TblCompanyBalanceBankAccount;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCash;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailItemExpiredDate;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailPropertyBarcode;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblPurchaseOrderRevisionHistory;
import hotelfx.persistence.model.TblPurchaseRequest;
import hotelfx.persistence.model.TblPurchaseRequestDetail;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblReturDetailItemExpiredDate;
import hotelfx.persistence.model.TblReturDetailPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierBankAccount;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.model.TblUnit;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FPurchaseOrderManager {

    public TblPurchaseOrder insertDataPurchaseOrder(
            TblPurchaseOrder purchaseOrder,
            List<TblPurchaseOrderDetail> purchaseOrderDetails
    );

    public boolean updateDataPurchaseOrder(
            TblPurchaseOrder purchaseOrder,
            List<TblPurchaseOrderDetail> purchaseOrderDetails
    );

    public boolean updateApproveDataPurchaseOrder(
            TblPurchaseOrder purchaseOrder
    );

    public boolean updateDataPurchaseOrderRevision(
            TblPurchaseOrderRevisionHistory purchaseOrderRevisionHistory,
            List<TblPurchaseOrderDetail> purchaseOrderDetails
    );

    public boolean updateDataPurchaseOrderApproveRevision(
            TblPurchaseOrderRevisionHistory purchaseOrderRevisionHistory,
            List<TblPurchaseOrderDetail> purchaseOrderDetails
    );

    public boolean updateDataPurchaseOrderApproveRevision(
            TblPurchaseOrderRevisionHistory purchaseOrderRevisionHistory,
            List<TblPurchaseOrderDetail> purchaseOrderDetails,
            List<TblMemorandumInvoice> miSources,
            List<TblMemorandumInvoiceDetail> miDetailSources,
            List<TblMemorandumInvoice> miNews,
            List<TblMemorandumInvoiceDetail> miDetailNews,
            List<TblReturDetail> returDetailSources,
            List<TblReturDetail> returDetailNews,
            List<TblHotelPayable> hpSources,
            List<TblHotelPayable> hpNews,
            List<TblHotelFinanceTransactionHotelPayable> hfthpSources,
            List<TblHotelFinanceTransactionHotelPayable> hfthpNews,
            List<TblHotelInvoice> hiSources,
            List<TblHotelInvoice> hiNews
    );

    public boolean updateOrderDataPurchaseOrder(
            TblPurchaseOrder purchaseOrder
    );

    public boolean updateClosingDataPurchaseOrder(
            TblPurchaseOrder purchaseOrder);

    public boolean deleteDataPurchaseOrder(TblPurchaseOrder purchaseOrder);

    public TblPurchaseOrder getDataPurchaseOrder(long id);

    public List<TblPurchaseOrder> getAllDataPurchaseOrder();

    public List<TblPurchaseOrder> getAllDataPurchaseOrderByIDPurchaseRequest(long idPR);

    //--------------------------------------------------------------------------
    public TblPurchaseOrderDetail getDataPurchaseOrderDetail(long id);

    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetail();

    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetailByIDPurchaseOrder(long idPurchaseOrder);

    //--------------------------------------------------------------------------
    public TblPurchaseRequest getDataPurchaseRequest(long id);

    public List<TblPurchaseRequest> getAllDataPurchaseRequest();

    public RefPurchaseRequestStatus getDataPurchaseRequestStatus(int id);

    public List<RefPurchaseRequestStatus> getAllDataPurchaseRequestStatus();

    public List<TblPurchaseRequestDetail> getAllDataPurchaseRequestDetailByIDPurchaseRequest(long idPurchaseRequest);

    //--------------------------------------------------------------------------
    public TblSupplier getDataSupplier(long id);

    public List<TblSupplier> getAllDataSupplier();

    public List<TblSupplierItem> getAllDataSupplierItemByIDSupplier(long idSupplier);

    //--------------------------------------------------------------------------
    public RefPurchaseOrderStatus getDataPurchaseOrderStatus(int id);

    public List<RefPurchaseOrderStatus> getAllDataPurchaseOrderStatus();

    public RefPurchaseOrderItemArriveStatus getDataPurchaseOrderItemArriveStatus(int id);

    public List<RefPurchaseOrderItemArriveStatus> getAllDataPurchaseOrderItemArriveStatus();

    public RefPurchaseOrderPaymentStatus getDataPurchaseOrderPaymentStatus(int id);

    public List<RefPurchaseOrderPaymentStatus> getAllDataPurchaseOrderPaymentStatus();

    //--------------------------------------------------------------------------
    public TblSupplierItem getDataSupplierItem(long id);

    public TblItem getDataItem(long id);

    public List<TblItem> getAllDataItem();

    public TblUnit getDataUnit(long id);

    public List<TblUnit> getAllDataUnit();

    //--------------------------------------------------------------------------
    public List<TblMemorandumInvoice> getAllDataMemorandumInvoiceByIDPurchaseOrder(long idPurchaseOrder);

    //--------------------------------------------------------------------------
    public TblEmployee getDataEmployee(long id);

    public List<TblEmployee> getAllDataEmployee();

    //--------------------------------------------------------------------------
    public TblCompanyBalance getDataCompanyBalance(long id);

    //--------------------------------------------------------------------------
    public TblHotelPayable getDataHotelPayable(long id);

    public RefHotelPayableType getDataHotelPayableType(int id);

    public RefFinanceTransactionStatus getDataFinanceTransactionStatus(int id);

    //--------------------------------------------------------------------------
    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblPurchaseOrder purchaseOrder);

    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCash hotelFinanceTransactionWithCash,
            List<TblHotelFinanceTransactionHotelPayable> hfthps,
            String supplierName);

    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithTransfer hotelFinanceTransactionWithTransfer,
            List<TblHotelFinanceTransactionHotelPayable> hfthps,
            String supplierName);

    public TblHotelFinanceTransaction insertDataHotelFinanceTransaction(TblHotelFinanceTransaction hotelFinanceTransaction,
            TblHotelFinanceTransactionWithCekGiro hotelFinanceTransactionWithCekGiro,
            List<TblHotelFinanceTransactionHotelPayable> hfthps,
            String supplierName);

//    public List<TblHotelFinanceTransaction> getAllDataHotelFinanceTransactionByIDHotelPayable(long idHotelPayable);
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
    public RefItemType getDataItemType(int id);

    public List<RefItemType> getAllDataItemType();

    public RefItemGuestType getDataItemGuestType(int id);

    public List<RefItemGuestType> getAllDataItemGuestType();

    //--------------------------------------------------------------------------
    public SysDataHardCode getDataSysDataHardCode(long id);

    public List<SysDataHardCode> getAllDataSysDataHardCode();

    //--------------------------------------------------------------------------
    public TblHotelInvoice insertDataHotelInvoice(TblHotelInvoice hotelInvoice,
            TblPurchaseOrder purchaseOrder);

    public TblHotelInvoice insertDataHotelInvoice(TblPurchaseOrder dataPO);

    public TblHotelInvoice getDataHotelInvoice(long id);

    public RefHotelInvoiceType getDataHotelInvoiceType(int id);

    //--------------------------------------------------------------------------
    public TblRetur getDataRetur(long id);

    //--------------------------------------------------------------------------
    public TblPurchaseOrderRevisionHistory getDataPurchaseOrderRevisionHistory(long id);

    public List<TblPurchaseOrderRevisionHistory> getAllDataPurchaseOrderRevisionHistory();

    public List<TblPurchaseOrderRevisionHistory> getAllDataPurchaseOrderRevisionHistoryByIDPOSource(long idPOSource);

    public List<TblPurchaseOrderRevisionHistory> getAllDataPurchaseOrderRevisionHistoryByIDPONew(long idPONew);

    public TblPurchaseOrderRevisionHistory getDataPurchaseOrderRevisionHistoryByIDPONew(long idPONew);

    //--------------------------------------------------------------------------
    public TblPurchaseOrderDetail getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(long idPO, long idSupplierItem);

    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetailByIDPurchaseRequestAndIDItem(long idPR, long idItem);
    
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(long idPO, long idSupplierItem);

    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(long idMemorandumInvoice);
    
    public List<TblReturDetail> getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(long idMI, long idSupplierItem);

    //--------------------------------------------------------------------------
    public List<TblReturDetail> getAllDataReturDetailByIDMemorandumInvoice(long idMI);

    public RefMemorandumInvoiceStatus getDataMemorandumInvoiceStatus(int id);

    //--------------------------------------------------------------------------
    
    public List<TblMemorandumInvoiceDetailPropertyBarcode> getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(long idMIDetail);
    
    public List<TblMemorandumInvoiceDetailItemExpiredDate> getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(long idMIDetail);
    
    public List<TblReturDetailPropertyBarcode> getAllDataReturDetailPropertyBarcodeByIDReturDetail(long idReturDetail);
    
    public List<TblReturDetailItemExpiredDate> getAllDataReturDetailItemExpiredDateByIDReturDetail(long idReturDetail);
    
    //--------------------------------------------------------------------------
    public TblRoom getDataRoomByIDLocation(long idLocation);
    
    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation);
    
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation);

    public TblSupplier getDataSupplierByIDLocation(long idLocation);

    public TblLocationOfBin getDataBinByIDLocation(long idLocation);
    
    //--------------------------------------------------------------------------
    
    public TblHotelFinanceTransaction getDataHotelFinanceTransaction(long id);

    public List<TblHotelFinanceTransactionHotelPayable> getAllDataHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction(long idHotelFinanceTransaction);

    public List<TblHotelFinanceTransactionHotelPayable> getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(long idHotelPayable);

    //--------------------------------------------------------------------------
    public TblPurchaseOrder getDataPurchaseOrderByIDHotelPayable(long idHotelPayable);

    //--------------------------------------------------------------------------
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByIDSupplier(long idSupplier);

    //--------------------------------------------------------------------------
    public RefFinanceTransactionPaymentType getDataFinanceTransactionPaymentType(int id);

    public List<RefFinanceTransactionPaymentType> getAllDataFinanceTransactionPaymentType();

    //--------------------------------------------------------------------------
    public TblCompanyBalanceBankAccount getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
            long idCompanyBalance,
            long idBankAccount);

    //--------------------------------------------------------------------------
    public TblHotelFinanceTransactionWithTransfer getDataHotelFinanceTransactionWithTransferByIDHotelFinanceTransaction(long idHotelFinanceTransaction);

    public TblHotelFinanceTransactionWithCekGiro getDataHotelFinanceTransactionWithCekGiroByIDHotelFinanceTransaction(long idHotelFinanceTransaction);

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
