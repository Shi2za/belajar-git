/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.RefItemExpiredDateStatus;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefMemorandumInvoiceStatus;
import hotelfx.persistence.model.RefPurchaseOrderItemArriveStatus;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
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
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_receiving.receiving.ReceivingController;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FReceivingManager {
    
    public TblMemorandumInvoice insertDataMemorandumInvoice(
            TblMemorandumInvoice memorandumInvoice,
            List<ReceivingController.DetailLocation> detailLocations
    );
    
    public boolean updateDataMemorandumInvoice(
            TblMemorandumInvoice memorandumInvoice,
            List<ReceivingController.DetailLocation> detailLocations
    );
    
//    public boolean updateApproveDataMemorandumInvoice(
//            TblMemorandumInvoice memorandumInvoice
//    );
    
//    public boolean deleteDataMemorandumInvoice(TblMemorandumInvoice memorandumInvoice);
    
    public TblMemorandumInvoice getDataMemorandumInvoice(long id);
    
    public List<TblMemorandumInvoice> getAllDataMemorandumInvoice();
    
    public List<TblMemorandumInvoice> getAllDataMemorandumInvoiceByIDPurchaseOrder(long idPO);
    
    //--------------------------------------------------------------------------
    
    public TblMemorandumInvoiceDetail getDataMemorandumInvoiceDetail(long id);
    
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetail();
    
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDMemorandumInvoice(long idMemorandumInvoice);
    
    //--------------------------------------------------------------------------
    
    public RefMemorandumInvoiceStatus getDataMemorandumInvoiceStatus(int id);
    
    //--------------------------------------------------------------------------
    
    public TblPurchaseOrder getDataPurchaseOrder(long id);
    
    public List<TblPurchaseOrder> getAllDataPurchaseOrder();
    
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByMinPODueDate(Date poDueDate);
    
    public RefPurchaseOrderStatus getDataPurchaseOrderStatus(int id);
    
    public List<RefPurchaseOrderStatus> getAllDataPurchaseOrderStatus();
    
    public RefPurchaseOrderItemArriveStatus getDataPurchaseOrderItemArriveStatus(int id);
    
    public List<RefPurchaseOrderItemArriveStatus> getAllDataPurchaseOrderItemArriveStatus();
    
    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetailByIDPurchaseOrder(long idPurchaseOrder);
    
    //--------------------------------------------------------------------------
    
    public TblSupplier getDataSupplier(long id);
    
    public List<TblSupplier> getAllDataSupplier();
    
    //--------------------------------------------------------------------------
    
    public TblSupplierItem getDataSupplierItem(long id);
    
    public List<TblSupplierItem> getAllDataSupplierItemByIDSupplier(long idSupplier);
    
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
    
    public TblLocationOfWarehouse getWarehouse(long id);

    public List<TblLocationOfWarehouse> getAllDataWarehouse();
    
    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation);
    
    //--------------------------------------------------------------------------
    
    public TblLocation getDataLocation(long id);

    public TblBuilding getDataBuilding(long id);

    public TblFloor getDataFloor(long id);
    
    //--------------------------------------------------------------------------
    
//    public TblPurchaseOrderDetail getDataPurchaseOrderDetailByIDPurchaseOrderAndIDItem(long idPO, long idItem);
    
    public TblPurchaseOrderDetail getDataPurchaseOrderDetailByIDPurchaseOrderAndIDSupplierItem(long idPO, long idSupplierItem);
    
//    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDItem(long  idPO, long idItem);
    
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(long  idPO, long idSupplierItem);
    
    public List<TblReturDetail> getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(long idMI, long idSupplierItem);
    
    //--------------------------------------------------------------------------
    
    public List<TblMemorandumInvoiceDetailPropertyBarcode>  getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(long idMIDetail);
    
    public TblPropertyBarcode getDataPropertyBarcode(long id);
    
    //--------------------------------------------------------------------------
    
    public List<TblMemorandumInvoiceDetailItemExpiredDate>  getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(long idMIDetail);
    
    public TblItemExpiredDate getDataItemExpiredDate(long id);
    
    public RefItemExpiredDateStatus getDataItemExpiredDateStatus(int id);
    
    //--------------------------------------------------------------------------
    public TblHotelPayable getDataHotelPayable(long id);
    
    public TblHotelInvoice getDataHotelInvoice(long id);
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
