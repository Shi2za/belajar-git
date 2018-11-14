/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassDataMutation;
import hotelfx.helper.ClassDataTransferItem;
import hotelfx.helper.ClassInputLocation;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefStoreRequestStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFixedTangibleAsset;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblInComing;
import hotelfx.persistence.model.TblInComingDetail;
import hotelfx.persistence.model.TblInComingDetailItemMutationHistory;
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
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblOutGoing;
import hotelfx.persistence.model.TblOutGoingDetail;
import hotelfx.persistence.model.TblOutGoingDetailItemMutationHistory;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblStoreRequest;
import hotelfx.persistence.model.TblStoreRequestDetail;
import hotelfx.persistence.model.TblStoreRequestDetailItemMutationHistory;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_warehouse.warehouse_in_coming.WarehouseInComingController;
import hotelfx.view.feature_warehouse.warehouse_out_going.WarehouseOutGoingController;
import hotelfx.view.feature_warehouse.warehouse_out_going.WarehouseOutGoingV2Controller;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FWarehouseManager {

    public TblLocationOfWarehouse insertDataWarehouse(TblLocationOfWarehouse warehouse);

    public boolean updateDataWarehouse(TblLocationOfWarehouse warehouse);

    public boolean deleteDataWarehouse(TblLocationOfWarehouse warehouse);

    public TblLocationOfWarehouse getWarehouse(long id);

    public List<TblLocationOfWarehouse> getAllDataWarehouse();

    public TblLocationOfWarehouse getDataWarehouseByIdLocation(long id);

    //--------------------------------------------------------------------------

    public TblLocation getLocation(long id);
    
    public TblLocation getLocationByIDLocationType(int id);

    public List<TblLocation> getAllDataLocation();
    
    public List<TblLocation> getAllDataLocationByIDLocationType(int idLocationType);

    public List<TblFloor> getAllDataFloor();

    public List<TblBuilding> getAllDataBuilding();

    public TblBuilding getDataBuilding(long id);

    public TblFloor getDataFloor(long id);
    
    public TblJob getDataJob(long id);
    
    public List<TblJob> getAllDataJob();
    
    public TblGroup getDataGroup(long id);
    
    public List<TblGroup> getAllDataGroup();
    
    //--------------------------------------------------------------------

    public List<TblItemLocation> getAllDataItemLocation(long id);

    public RefLocationType getDataLocationType(int id);
    
    public List<RefLocationType> getAllDataTypeLocation();

   //public List<TblLocation>getAllDataDestinationLocation(long id);
    public TblItemLocation getDataItemLocation(long id);

    public TblItem getDataItem(long id);

   //public boolean updateDataWarehouseTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,ObservableList<TblPropertyBarcode> propertyBarcode);
    //public boolean updateDataWarehouseTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,TblItemLocationPropertyBarcode propertyBarcode);
    //public boolean updateDataWarehouseTransferItem(ClassDataTransferItem transferItem,TblItemMutationHistory mutation,ClassInputLocation destinationLocation);
    public boolean updateDataWarehouseTransferItem(ClassDataTransferItem transferItem, 
            TblItemMutationHistory mutation, 
            ClassInputLocation destinationLocation,
            String status);

    public boolean deleteDataItemLocationPropertyBarcode(TblItemLocationPropertyBarcode itemLocationPropertyBarcode);

    public TblItemLocation getDataItemLocationByIDItemAndIDLocation(long idItem, long idLocation);

    public List<RefItemObsoleteBy> getAllDataObsoleteBy();
//--------------------------------------------------------------------------

    public List<TblPropertyBarcode> getAllDataPropertyBarcodeByIdItem(long id);

    public List<TblRoom> getAllDataRoom();

    public List<TblLocationOfLaundry> getAllDataLaundry();

    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcode(long id);

    public TblPropertyBarcode getDataPropertyBarcode(long id);

     //public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcode(long id);
    public List<ClassDataTransferItem> getAllDataTransferItem(long id);

    public List<ClassDataMutation> getAllDataMutationHistory();

    public TblRoom getDataRoom(long id);

    public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id);

    public List<TblRoom> getRoomByIdLocation(long id);

    public List<TblLocationOfLaundry> getLaundryByIdLocation(long id);

    public List<TblSupplier> getSupplierByIdLocation(long id);

    public List<TblLocationOfBin> getBinByIdLocation(long id);
    
    public List<TblSupplier> getAllDataSupplier();
    
    public List<TblLocationOfBin> getAllDataBin();
    
    public RefItemMutationType getMutationType(int id);
     // public TblLocationOfLaundry getDataLaundry(long id);
    // public TblItemLocationPropertyBarcode getDataItemPropertyBarcode(long id);
    //public List<TblItemLocationPropertyBarcode> getDataItemLocationPropertyBarcodeByItemLocationAndPropertyBarcode(long idItemLoc,long idProperty);
    //public boolean updateDataWarehouseTransferItemBarcode(TblItemLocationPropertyBarcode sourceItemLocation,TblItemMutationHistoryPropertyBarcode mutation);
//--------------------------------------------------------------------------
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDate(long id);
    
    public TblItemExpiredDate getDataItemExpiredDate(long id);
    
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItem(long idItem);
     
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
    public TblStoreRequest insertDataStoreRequest(TblStoreRequest sr, 
            List<TblStoreRequestDetail> srds);
    
    public boolean updateDataStoreRequest(TblStoreRequest sr, 
            List<TblStoreRequestDetail> srds);
    
//    //Out Going
//    public boolean updateApprovedDataStoreRequest(TblStoreRequest sr, 
//            List<WarehouseOutGoingController.SRDetailProperty> srdps);
    
    //Out Going
    public boolean updateApprovedOGDataStoreRequest(TblStoreRequest sr, 
            List<WarehouseOutGoingV2Controller.SRDetailItemMutationHistory> srdimhs);
    
    public boolean deleteDataStoreRequest(TblStoreRequest sr);
    
    public TblStoreRequest getDataStoreRequest(long id);
    
    public List<TblStoreRequest> getAllDataStoreRequest();
    
    public List<TblStoreRequest> getAllDataStoreRequestByIDStoreRequestStatus(int idStoreRequestStatus);
    
    //--------------------------------------------------------------------------
    
    public TblStoreRequestDetail getDataStoreRequestDetail(long id);
    
    public TblStoreRequestDetail getDataStoreRequestDetailByIDStoreRequestAndIDItem(long idSR, long idItem);
    
    public List<TblStoreRequestDetail> getAllDataStoreRequestDetail();
    
    public List<TblStoreRequestDetail> getAllDataStoreRequestDetailByIDStoreRequest(long idSR);
    
    //--------------------------------------------------------------------------
    
    public RefStoreRequestStatus getDataStoreRequestStatus(int id);
    
    public List<TblItem> getAllDataItem();
    
    public TblUnit getDataUnit(long id);
    
    public RefItemType getDataItemType(int id);
    
    public RefItemGuestType getDataItemGuestType(int id);
    
    //--------------------------------------------------------------------------
    
    public TblEmployee getDataEmployee(long id);
    
    public List<TblEmployee> getAllDataEmployee();
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    
    public TblFixedTangibleAsset getDataFixedTangibleAsset(long id);
    
    public List<TblFixedTangibleAsset> getAllDataFixedTangibleAsset();
    
    //--------------------------------------------------------------------------
    public TblItemLocation getDataItemLocationByIDLocationAndIDItem(long idLocation, long idItem);
    
    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation);
    
    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
            long idItemLocation, 
            long idPropertyBarcode);
    
    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation);
    
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
            long idItemLocation, 
            long idItemExpiredDate);
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    public TblStoreRequestDetailItemMutationHistory getDataStoreRequestDetailItemMutationHistoryByIDStoreRequestDetail(long idStoreRequestDetail);
    
    public TblItemMutationHistory getDataItemMutationHistory(long id);
    
    public List<TblItemMutationHistory> getAllDataItemMutationHistory();
    
    public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation);
    
    public List<TblItemMutationHistoryPropertyBarcode> getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation);
    
    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(long idItemMutation);
    
    //--------------------------------------------------------------------------
    
    public TblRoom getDataRoomByIDLocation(long idLocation);
    
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation);
    
    //--------------------------------------------------------------------------
    
    public TblOutGoing insertDataOutGoing(TblOutGoing og, 
            List<WarehouseOutGoingController.OGDetailItemMutationHistory> ogdps);
    
    public TblOutGoing getDataOutGoing(long id);
    
    public List<TblOutGoing> getAllDataOutGoing();
    
    //--------------------------------------------------------------------------
    
    public List<TblOutGoingDetail> getAllDataOutGoingDetailByIDOutGoing(long idOutGoing);
    
    public TblOutGoingDetailItemMutationHistory getDataOutGoingDetailItemMutationHistoryByIDOutGoingDetail(long idOutGoingDetail);
    
    //--------------------------------------------------------------------------
    
    public TblInComing insertDataInComing(TblInComing ic, 
            List<WarehouseInComingController.ICDetailItemMutationHistory> icdps);
    
    public TblInComing getDataInComing(long id);
    
    public List<TblInComing> getAllDataInComing();
    
    //--------------------------------------------------------------------------
    
    public List<TblInComingDetail> getAllDataInComingDetailByIDInComing(long idInComing);
    
    public TblInComingDetailItemMutationHistory getDataInComingDetailItemMutationHistoryByIDInComingDetail(long idInComingDetail);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();

}
