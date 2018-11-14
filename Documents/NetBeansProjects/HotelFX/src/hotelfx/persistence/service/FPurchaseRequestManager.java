/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefPurchaseRequestStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemTypeHk;
import hotelfx.persistence.model.TblItemTypeWh;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblPurchaseRequest;
import hotelfx.persistence.model.TblPurchaseRequestDetail;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FPurchaseRequestManager {
    
    public TblPurchaseRequest insertDataPurchaseRequest(
            TblPurchaseRequest purchaseRequest,
            List<TblPurchaseRequestDetail> purchaseRequestDetails
    );
    
    public boolean updateDataPurchaseRequest(
            TblPurchaseRequest purchaseRequest,
            List<TblPurchaseRequestDetail> purchaseRequestDetails
    );
    
    public boolean updateApproveDataPurchaseRequest(
            TblPurchaseRequest purchaseRequest
    );
    
    public boolean updateClosingDataPurchaseRequest(
            TblPurchaseRequest purchaseRequest
    );
    
    public boolean deleteDataPurchaseRequest(TblPurchaseRequest purchaseRequest);
    
    public TblPurchaseRequest getDataPurchaseRequest(long id);
    
    public List<TblPurchaseRequest> getAllDataPurchaseRequest();
    
    //--------------------------------------------------------------------------
    
    public TblPurchaseRequestDetail getDataPurchaseRequestDetail(long id);
    
    public List<TblPurchaseRequestDetail> getAllDataPurchaseRequestDetail();
    
    public List<TblPurchaseRequestDetail> getAllDataPurchaseRequestDetailByIDPurchaseRequest(long idPurchaseRequest);
    
    //--------------------------------------------------------------------------
    
    public TblSupplier getDataSupplier(long id);
    
    public List<TblSupplier> getAllDataSupplier();
    
    //--------------------------------------------------------------------------
    
    public RefPurchaseRequestStatus getDataPurchaseRequestStatus(int id);
    
    public List<RefPurchaseRequestStatus> getAllDataPurchaseRequestStatus();
    
    //--------------------------------------------------------------------------
    
    public TblItem getDataItem(long id);
    
    public List<TblItem> getAllDataItem();
    
    public TblUnit getDataUnit(long id);
    
    public List<TblUnit> getAllDataUnit();
    
    public RefItemType getDataItemType(int id);
    
    public List<RefItemType> getAllDataItemType();
    
    public RefItemGuestType getDataItemGuestType(int id);
    
    public List<RefItemGuestType> getAllDataItemGuestType();
    
    //--------------------------------------------------------------------------
    
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByIDPurchaseRequest(long idPurchaseRequest);
    
    //--------------------------------------------------------------------------
    
    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetailByIDPurchaseRequestAndIDItem(long idPR, long idItem);
    
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(long idPO, long idSupplierItem);
    
    public List<TblReturDetail> getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(long idMI, long idSupplierItem);
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
