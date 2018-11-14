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
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblEmployee;
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
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_laundry.laundry_in_coming.LaundryInComingController;
import hotelfx.view.feature_laundry.laundry_out_going.LaundryOutGoingController;
import hotelfx.view.feature_laundry.laundry_transfer_item.LaundryTransferItemController;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author Andreas
 */
public interface FLaundryManager {

    public TblLocationOfLaundry insertDataLaundry(TblLocationOfLaundry laundry);

    public boolean updateDataLaundry(TblLocationOfLaundry laundry);

    public boolean deleteDataLaundry(TblLocationOfLaundry laundry);

    public List<TblLocationOfLaundry> getAllDataLaundry();

    public TblLocationOfLaundry getLaundry(long id);
    
    public TblLocationOfLaundry getDataLaundryByIdLocation(long id);

    public TblLocation getLocation(long id);
    
    public TblLocation getLocationByIDLocationType(int id);

    public List<TblFloor> getAllDataFloor();

    public List<TblBuilding> getAllDataBuilding();

    public TblBuilding getDataBuilding(long id);

    public TblFloor getDataFloor(long id);
    
    public TblJob getJob(long id);

    public List<TblJob> getAllDataJob();
    
    public TblGroup getDataGroup(long id);

    public List<TblGroup> getAllDataGroup();

    //------------------------------------------------------
    public List<TblItemLocation> getAllDataItemLocation(long id);

    public RefLocationType getDataLocationType(int id);
    
    public List<RefLocationType> getAllDataTypeLocation();

    public List<TblLocation> getAllDataDestinationLocation(long id);

    public List<RefItemObsoleteBy> getAllDataObsoleteBy();

    //public boolean updateDataLaundryTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,ObservableList<TblPropertyBarcode>propertyBarcode);
    //public boolean updateDataLaundryTransferItem(TblItemLocation sourceItemLocation,TblItemMutationHistory mutation,ClassInputLocation destinationLocation,TblItemLocationPropertyBarcode propertyBarcode);
//     public boolean updateDataLaundryTransferItem(ClassDataTransferItem transferItem,TblItemMutationHistory mutation,ClassInputLocation destinationLocation);
    public boolean updateDataLaundryTransferItem(ClassDataTransferItem transferItem,
            TblItemMutationHistory mutation,
            ClassInputLocation destinationLocation,
            String status);

    public TblItemLocation getDataItemLocation(long id);

    public TblItem getDataItem(long id);
    
    public List<TblItem> getAllDataItem();

    public List<TblPropertyBarcode> getAllDataPropertyBarcodeByIdItem(long id);

    public List<TblRoom> getAllDataRoom();

    public List<TblLocationOfWarehouse> getAllDataWarehouse();

    public List<ClassDataTransferItem> getAllDataTransferItem(long idLoc);

    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcode(long id);

    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation);
    
    public TblPropertyBarcode getDataPropertyBarcode(long id);

    public boolean deleteDataItemLocationPropertyBarcode(TblItemLocationPropertyBarcode itemLocationPropertyBarcode);

    public List<ClassDataMutation> getAllDataMutationHistory();

    public List<TblLocationOfWarehouse> getWarehouseByIdLocation(long id);

    public List<TblRoom> getRoomByIdLocation(long id);

    public List<TblLocationOfLaundry> getLaundryByIdLocation(long id);

    public List<TblSupplier> getSupplierByIdLocation(long id);

    public List<TblLocationOfBin> getBinByIdLocation(long id);

    public List<TblSupplier> getAllDataSupplier();

    public List<TblLocationOfBin> getAllDataBin();

    public RefItemMutationType getMutationType(int id);
    //---------------------------------------------------------------------------

    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDate(long id);
    
    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation);
    
    public TblItemExpiredDate getDataItemExpiredDate(long id);
    
    public List<TblItemExpiredDate> getAllDataItemExpiredDateByIDItem(long idItem);

    //--------------------------------------------------------------------------
    public List<TblItemMutationHistory> getAllDataItemMutationHistory();
    
    public TblItemMutationHistoryPropertyBarcode getDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation);
    
    public List<TblItemMutationHistoryItemExpiredDate> getAllDataItemMutationHistoryItemExpiredDateByIDItemMutationHistory(long idItemMutation);
    
    //--------------------------------------------------------------------------
    
    public TblEmployee getDataEmployee(long id);
    
    public List<TblEmployee> getAllDataEmployee();
    
    public TblPeople getDataPeople(long id);
    
    //--------------------------------------------------------------------------
    
    public TblRoom getDataRoomByIDLocation(long idLocation);
    
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation);
    
    public TblLocationOfWarehouse getDataWarehouseByIdLocation(long id);
    
    //--------------------------------------------------------------------------
    
    public TblInComing insertDataInComing(TblInComing ic, 
            List<LaundryInComingController.ICDetailItemMutationHistory> icdps);
    
    public TblInComing getDataInComing(long id);
    
    public List<TblInComing> getAllDataInComing();
    
    //--------------------------------------------------------------------------
    
    public List<TblInComingDetail> getAllDataInComingDetailByIDInComing(long idInComing);
    
    public TblInComingDetailItemMutationHistory getDataInComingDetailItemMutationHistoryByIDInComingDetail(long idInComingDetail);
    
    //--------------------------------------------------------------------------
    
    public SysDataHardCode getDataSysDataHardCode(long id);
    
    public List<SysDataHardCode> getAllDataSysDataHardCode();
    
    //--------------------------------------------------------------------------
    
    public TblUnit getDataUnit(long id);
    
    public RefItemType getDataItemType(int id);
    
    public RefItemGuestType getDataItemGuestType(int id);
    
    //--------------------------------------------------------------------------
    
    public TblItemTypeHk getDataItemTypeHK(long id);
    
    public List<TblItemTypeHk> getAllDataItemTypeHK();
    
    public TblItemTypeWh getDataItemTypeWH(long id);
    
    public List<TblItemTypeWh> getAllDataItemTypeWH();
    
    //--------------------------------------------------------------------------
    
    public List<TblLocation> getAllDataLocationByIDLocationType(int idLocationType);
    
    //--------------------------------------------------------------------------
    
    public TblItemLocation getDataItemLocationByIDItemAndIDLocation(long idItem, long idLocation);
    
    public TblItemLocation getDataItemLocationByIDLocationAndIDItem(long idLocation, long idItem);
    
    public TblItemLocationPropertyBarcode getDataItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
            long idItemLocation, 
            long idPropertyBarcode);
    
    public TblItemLocationItemExpiredDate getDataItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
            long idItemLocation, 
            long idItemExpiredDate);
    
    //--------------------------------------------------------------------------
    
    public TblItemMutationHistory getDataItemMutationHistory(long id);
    
    public List<TblItemMutationHistoryPropertyBarcode> getAllDataItemMutationHistoryPropertyBarcodeByIDItemMutationHistory(long idItemMutation);
    
    //--------------------------------------------------------------------------
    
    public TblStoreRequest insertDataStoreRequest(TblStoreRequest sr, 
            List<TblStoreRequestDetail> srds);
    
    public boolean updateDataStoreRequest(TblStoreRequest sr, 
            List<TblStoreRequestDetail> srds);
    
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
    
    public TblOutGoing insertDataOutGoing(TblOutGoing og, 
            List<LaundryOutGoingController.OGDetailItemMutationHistory> ogdps);
    
    public TblOutGoing getDataOutGoing(long id);
    
    public List<TblOutGoing> getAllDataOutGoing();
    
    //--------------------------------------------------------------------------
    
    public List<TblOutGoingDetail> getAllDataOutGoingDetailByIDOutGoing(long idOutGoing);
    
    public TblOutGoingDetailItemMutationHistory getDataOutGoingDetailItemMutationHistoryByIDOutGoingDetail(long idOutGoingDetail);
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
}
