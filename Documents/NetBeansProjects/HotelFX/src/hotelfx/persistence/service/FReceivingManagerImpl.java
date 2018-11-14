/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefItemExpiredDateStatus;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefMemorandumInvoiceStatus;
import hotelfx.persistence.model.RefPurchaseOrderItemArriveStatus;
//import hotelfx.persistence.model.RefMemorandumInvoiceStatus;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblHotelInvoice;
import hotelfx.persistence.model.TblHotelPayable;
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
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblSupplierItem;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_receiving.receiving.ReceivingController;
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
public class FReceivingManagerImpl implements FReceivingManager {

    private Session session;

    private String errMsg;

    public FReceivingManagerImpl() {

    }

    @Override
    public TblMemorandumInvoice insertDataMemorandumInvoice(TblMemorandumInvoice memorandumInvoice,
            List<ReceivingController.DetailLocation> detailLocations) {
        errMsg = "";
        TblMemorandumInvoice tblMemorandumInvoice = memorandumInvoice;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data memorandum invoice
                tblMemorandumInvoice.setCodeMi(ClassCoder.getCode("Receiving", session));
                tblMemorandumInvoice.setReceivingDate(Timestamp.valueOf(LocalDateTime.now()));
                tblMemorandumInvoice.setRefMemorandumInvoiceStatus(session.find(RefMemorandumInvoiceStatus.class, 0));  //Dibuat = '0'
                tblMemorandumInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblMemorandumInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblMemorandumInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblMemorandumInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblMemorandumInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblMemorandumInvoice);
                //data detail - location
                for (ReceivingController.DetailLocation detailLocation : detailLocations) {
                    if (detailLocation.getTblDetail().getItemQuantity().compareTo(new BigDecimal("0")) == 1) {  //if item quanttiy > 0, insert data to database
                        //data memorandum invoice detail
                        detailLocation.getTblDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(detailLocation.getTblDetail());
                        //data item (cost of goods sold)
                        if (detailLocation.getTblDetail().getTblItem() == null) {    //not bonus
                            TblPurchaseOrderDetail data = getPurchaseOrderDetailByIDPurchaseOrderAndIDItem(detailLocation.getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                                    detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getIditem());
                            if (data != null) { //data found
                                detailLocation.getTblDetail().getTblSupplierItem().getTblItem().setItemCostOfGoodsSold(data.getItemCost());
                                detailLocation.getTblDetail().getTblSupplierItem().getTblItem().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                detailLocation.getTblDetail().getTblSupplierItem().getTblItem().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(detailLocation.getTblDetail().getTblSupplierItem().getTblItem());
                                session.evict(data);    //remove data (object) from session cache
                            }
                        }
                        //data item mutation history (create)
                        TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
                        if (detailLocation.getTblDetail().getTblItem() == null) {    //not bonus
                            dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblSupplierItem().getTblItem());
                        } else {  //bonus
                            dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblItem());
                        }
                        dataMutationHistory.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
                        dataMutationHistory.setTblLocationByIdlocationOfSource(tblMemorandumInvoice.getTblPurchaseOrder().getTblSupplier().getTblLocation());
                        dataMutationHistory.setTblLocationByIdlocationOfDestination(detailLocation.getTblDetail().getTblLocation());
                        dataMutationHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutationHistory.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutationHistory.setRefItemMutationType(session.find(RefItemMutationType.class, 0));
                        dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataMutationHistory);
                        //data item location (destination, udpate:plus)
                        TblItemLocation tempItemDestinationLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                dataMutationHistory.getTblItem().getIditem());
                        if (tempItemDestinationLocation != null) {    //update
                            tempItemDestinationLocation.setItemQuantity(tempItemDestinationLocation.getItemQuantity().add(dataMutationHistory.getItemQuantity()));
                            tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(tempItemDestinationLocation);
                        } else {  //create
                            tempItemDestinationLocation = new TblItemLocation();
                            tempItemDestinationLocation.setTblItem(session.find(TblItem.class, dataMutationHistory.getTblItem().getIditem()));
                            tempItemDestinationLocation.setTblLocation(session.find(TblLocation.class, dataMutationHistory.getTblLocationByIdlocationOfDestination().getIdlocation()));
                            tempItemDestinationLocation.setItemQuantity(dataMutationHistory.getItemQuantity());
                            tempItemDestinationLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemDestinationLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            tempItemDestinationLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(tempItemDestinationLocation);
                        }
                        //data memorandum invoice detail - property barcode
                        for (TblMemorandumInvoiceDetailPropertyBarcode midPB : detailLocation.getTblMemorandumInvoiceDetailPropertyBarcodes()) {
                            //data fixed tangible asset
                            midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().setCodeAsset(ClassCoder.getCode("Asset", session));
                            midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            midPB.getTblPropertyBarcode().getTblFixedTangibleAsset().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(midPB.getTblPropertyBarcode().getTblFixedTangibleAsset());
                            //data property barcode
                            //check data barcode has been used or not
//                        TblPropertyBarcode dataBarcode = getPropertyBarcodeByCodeBarcode(midPB.getTblPropertyBarcode().getCodeBarcode());
//                        if (dataBarcode != null) {
//                            session.evict(dataBarcode);
//                            errMsg = "Duplicate Barcode Number..!!";
//                            if (session.getTransaction().isActive()) {
//                                session.getTransaction().rollback();
//                            }
//                            return null;
//                        }
                            midPB.getTblPropertyBarcode().setCodeBarcode(ClassCoder.getCode("Property Barcode", session));
                            midPB.getTblPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            midPB.getTblPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            midPB.getTblPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            midPB.getTblPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            midPB.getTblPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(midPB.getTblPropertyBarcode());
                            //data memorandum invoice detail - property barcode
                            midPB.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            midPB.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            midPB.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            midPB.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            midPB.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(midPB);
                            //data item mutation - property barcode
                            TblItemMutationHistoryPropertyBarcode itemMutationHistoryPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            itemMutationHistoryPropertyBarcode.setTblItemMutationHistory(dataMutationHistory);
                            itemMutationHistoryPropertyBarcode.setTblPropertyBarcode(midPB.getTblPropertyBarcode());
                            itemMutationHistoryPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemMutationHistoryPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            itemMutationHistoryPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemMutationHistoryPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            itemMutationHistoryPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(itemMutationHistoryPropertyBarcode);
                            //data item location property barcode (destination:insert)
                            TblItemLocationPropertyBarcode tempItemDestinationLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                            tempItemDestinationLocationPropertyBarcode.setTblItemLocation(tempItemDestinationLocation);
                            tempItemDestinationLocationPropertyBarcode.setTblPropertyBarcode(itemMutationHistoryPropertyBarcode.getTblPropertyBarcode());
                            tempItemDestinationLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemDestinationLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            tempItemDestinationLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemDestinationLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            tempItemDestinationLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(tempItemDestinationLocationPropertyBarcode);
                        }
                        //sorting data detail item expired date quantity
                        bubbleSort(detailLocation.getDetailItemExpiredDateQuantities());
                        for (int i = 0; i < detailLocation.getDetailItemExpiredDateQuantities().size(); i++) {
                            //data item expired date
                            TblItemExpiredDate itemExpiredDate = getItemExpiredDateByIDItemAndItemExpiredDate(
                                    detailLocation.getDetailItemExpiredDateQuantities().get(i).getDataDetailItemExporedDate().getTblItemExpiredDate().getTblItem().getIditem(),
                                    detailLocation.getDetailItemExpiredDateQuantities().get(i).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate());
                            if (itemExpiredDate == null) {
                                itemExpiredDate = new TblItemExpiredDate(detailLocation.getDetailItemExpiredDateQuantities().get(i).getDataDetailItemExporedDate().getTblItemExpiredDate());
                                itemExpiredDate.setCodeItemExpiredDate(ClassCoder.getCode("Item Expired Date", session));
                                itemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                itemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                itemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                itemExpiredDate.setTblEmployeeByLastUdapteBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                itemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(itemExpiredDate);
                            }
                            //data mi detail - item expired date
                            TblMemorandumInvoiceDetailItemExpiredDate miDetailItemExpiredDate = new TblMemorandumInvoiceDetailItemExpiredDate(detailLocation.getDetailItemExpiredDateQuantities().get(i).getDataDetailItemExporedDate());
                            miDetailItemExpiredDate.setTblItemExpiredDate(itemExpiredDate);
                            miDetailItemExpiredDate.setItemQuantity(detailLocation.getDetailItemExpiredDateQuantities().get(i).getItemQuantity());
                            miDetailItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            miDetailItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            miDetailItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            miDetailItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            miDetailItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(miDetailItemExpiredDate);
                            //data item mutation - item expired date
                            TblItemMutationHistoryItemExpiredDate itemMutationHistoryItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                            itemMutationHistoryItemExpiredDate.setTblItemMutationHistory(dataMutationHistory);
                            itemMutationHistoryItemExpiredDate.setTblItemExpiredDate(itemExpiredDate);
                            itemMutationHistoryItemExpiredDate.setItemQuantity(miDetailItemExpiredDate.getItemQuantity());
                            itemMutationHistoryItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemMutationHistoryItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            itemMutationHistoryItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            itemMutationHistoryItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            itemMutationHistoryItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(itemMutationHistoryItemExpiredDate);
                            //data item location - item expired date (destination, +)
                            TblItemLocationItemExpiredDate itemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                    tempItemDestinationLocation.getIdrelation(),
                                    itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getIditemExpiredDate()
                            );
                            if (itemLocationItemExpiredDate != null) {
                                itemLocationItemExpiredDate.setItemQuantity(itemLocationItemExpiredDate.getItemQuantity().add(itemMutationHistoryItemExpiredDate.getItemQuantity()));
                                itemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                itemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.saveOrUpdate(itemLocationItemExpiredDate);
                            } else {
                                //data item location - item expired date (destination:insert => all location)
                                List<TblLocation> dataLocations = getAllLocation();
                                for (TblLocation dataLocation : dataLocations) {
                                    TblItemLocation dataItemLocation = getItemLocationByIDItemAndIDLocation(
                                            itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getTblItem().getIditem(),
                                            dataLocation.getIdlocation()
                                    );
                                    if (dataItemLocation == null) {
                                        dataItemLocation = new TblItemLocation();
                                        dataItemLocation.setTblItem(itemMutationHistoryItemExpiredDate.getTblItemExpiredDate().getTblItem());
                                        dataItemLocation.setTblLocation(dataLocation);
                                        dataItemLocation.setItemQuantity(new BigDecimal("0"));
                                        dataItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(dataItemLocation);
                                    }
                                    itemLocationItemExpiredDate = new TblItemLocationItemExpiredDate();
                                    itemLocationItemExpiredDate.setTblItemLocation(dataItemLocation);
                                    if (dataItemLocation.getIdrelation() == tempItemDestinationLocation.getIdrelation()) {
                                        itemLocationItemExpiredDate.setItemQuantity(itemMutationHistoryItemExpiredDate.getItemQuantity());  //current data
                                    } else {
                                        itemLocationItemExpiredDate.setItemQuantity(new BigDecimal("0"));   //another data
                                    }
                                    itemLocationItemExpiredDate.setTblItemExpiredDate(itemMutationHistoryItemExpiredDate.getTblItemExpiredDate());
                                    itemLocationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    itemLocationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    itemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    itemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    itemLocationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(itemLocationItemExpiredDate);
                                }
                            }
                        }
                    }
                }
                //data purchase order (item-arrive status : updated)
                tblMemorandumInvoice.getTblPurchaseOrder().setRefPurchaseOrderItemArriveStatus(session.find(RefPurchaseOrderItemArriveStatus.class, 1));    //Diterima Sebagian = '1', sudah terdapat penerimaan barang
                tblMemorandumInvoice.getTblPurchaseOrder().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblMemorandumInvoice.getTblPurchaseOrder().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.saveOrUpdate(tblMemorandumInvoice.getTblPurchaseOrder());
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
        return tblMemorandumInvoice;
    }

    private TblItemLocation getItemLocationByIDLocationAndIDItem(long idLocation, long idItem) {
        List<TblItemLocation> list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                .setParameter("idLocation", idLocation)
                .setParameter("idItem", idItem)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    public TblPurchaseOrderDetail getPurchaseOrderDetailByIDPurchaseOrderAndIDItem(long idPO, long idItem) {
        List<TblPurchaseOrderDetail> list = session.getNamedQuery("findAllTblPurchaseOrderDetailByIDPurchaseOrderAndIDItem")
                .setParameter("idPurchaseOrder", idPO)
                .setParameter("idItem", idItem)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    public TblPropertyBarcode getPropertyBarcodeByCodeBarcode(String codeBarcode) {
        List<TblPropertyBarcode> list = session.getNamedQuery("findAllTblPropertyBarcodeByCodeBarcode")
                .setParameter("codeBarcode", codeBarcode)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblItemExpiredDate getItemExpiredDateByIDItemAndItemExpiredDate(
            long idItem,
            Date itemExpiredDate) {
        List<TblItemExpiredDate> list = session.getNamedQuery("findAllTblItemExpiredDateByIDItemAndItemExpiredDate")
                .setParameter("idItem", idItem)
                .setParameter("itemExpiredDate", itemExpiredDate)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private void bubbleSort(List<ReceivingController.DetailItemExpiredDateQuantity> arr) {
        boolean swapped = true;
        int j = 0;
        ReceivingController.DetailItemExpiredDateQuantity tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < arr.size() - j; i++) {
                if (arr.get(i).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate().after(
                        arr.get(i + 1).getDataDetailItemExporedDate().getTblItemExpiredDate().getItemExpiredDate())) {
                    tmp = arr.get(i);
                    arr.set(i, arr.get(i + 1));
                    arr.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }

    private TblItemLocationItemExpiredDate getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
            long idItemLocation,
            long idItemExpiredDate) {
        List<TblItemLocationItemExpiredDate> list = session.getNamedQuery("findAllTblItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate")
                .setParameter("idItemLocation", idItemLocation)
                .setParameter("idItemExpiredDate", idItemExpiredDate)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private List<TblLocation> getAllLocation() {
        List<TblLocation> list = session.getNamedQuery("findAllTblLocation").list();
        return list;
    }

    private TblItemLocation getItemLocationByIDItemAndIDLocation(
            long idItem,
            long idLocation) {
        List<TblItemLocation> list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                .setParameter("idLocation", idLocation)
                .setParameter("idItem", idItem)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    //functional still cant be used
    @Override
    public boolean updateDataMemorandumInvoice(TblMemorandumInvoice memorandumInvoice,
            List<ReceivingController.DetailLocation> detailLocations) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data memorandum invoice
//            memorandumInvoice.setRefMemorandumInvoiceStatus(session.find(RefMemorandumInvoiceStatus.class, 0)); //need approve again
                memorandumInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                memorandumInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(memorandumInvoice);
                //data memorandum invoice detail
                //delete all
                Query rs = session.getNamedQuery("deleteAllTblMemorandumInvoiceDetailByIDMemorandumInvoice")
                        .setParameter("idMemorandumInvoice", memorandumInvoice.getIdmi())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //data detail - location
                for (ReceivingController.DetailLocation detailLocation : detailLocations) {
                    if (detailLocation.getTblDetail().getItemQuantity().compareTo(new BigDecimal("0")) == 1) {  //if item quantity > 0, insert data to database
                        //save or update data memorandum invoice detail
                        detailLocation.getTblDetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        detailLocation.getTblDetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        detailLocation.getTblDetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(detailLocation.getTblDetail());
                        //data item (cost of goods sold)
                        if (detailLocation.getTblDetail().getTblItem() == null) {    //not bonus
                            TblPurchaseOrderDetail data = getPurchaseOrderDetailByIDPurchaseOrderAndIDItem(detailLocation.getTblDetail().getTblMemorandumInvoice().getTblPurchaseOrder().getIdpo(),
                                    detailLocation.getTblDetail().getTblSupplierItem().getTblItem().getIditem());
                            if (data != null) { //data found
                                detailLocation.getTblDetail().getTblSupplierItem().getTblItem().setItemCostOfGoodsSold(data.getItemCost());
                                detailLocation.getTblDetail().getTblSupplierItem().getTblItem().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                detailLocation.getTblDetail().getTblSupplierItem().getTblItem().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(detailLocation.getTblDetail().getTblSupplierItem().getTblItem());
                            }
                            session.evict(data);    //remove data (object) from session cache
                        }
                        //data item mutation history (create)
                        TblItemMutationHistory dataMutationHistory = new TblItemMutationHistory();
                        if (detailLocation.getTblDetail().getTblItem() == null) {    //not bonus
                            dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblSupplierItem().getTblItem());
                        } else {  //bonus
                            dataMutationHistory.setTblItem(detailLocation.getTblDetail().getTblItem());
                        }
                        dataMutationHistory.setItemQuantity(detailLocation.getTblDetail().getItemQuantity());
                        dataMutationHistory.setTblLocationByIdlocationOfSource(memorandumInvoice.getTblPurchaseOrder().getTblSupplier().getTblLocation());
                        dataMutationHistory.setTblLocationByIdlocationOfDestination(detailLocation.getTblDetail().getTblLocation());
                        dataMutationHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutationHistory.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutationHistory.setRefItemMutationType(session.find(RefItemMutationType.class, 0));
                        dataMutationHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutationHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutationHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutationHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutationHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataMutationHistory);
                        //data item location (destination, udpate:plus)
                        TblItemLocation tempItemDestinationLocation = getItemLocationByIDLocationAndIDItem(dataMutationHistory.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                dataMutationHistory.getTblItem().getIditem());
                        if (tempItemDestinationLocation != null) {    //update
                            tempItemDestinationLocation.setItemQuantity(tempItemDestinationLocation.getItemQuantity().add(dataMutationHistory.getItemQuantity()));
                            tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(tempItemDestinationLocation);
                        } else {  //create
                            tempItemDestinationLocation = new TblItemLocation();
                            tempItemDestinationLocation.setTblItem(session.find(TblItem.class, dataMutationHistory.getTblItem().getIditem()));
                            tempItemDestinationLocation.setTblLocation(session.find(TblLocation.class, dataMutationHistory.getTblLocationByIdlocationOfDestination().getIdlocation()));
                            tempItemDestinationLocation.setItemQuantity(dataMutationHistory.getItemQuantity());
                            tempItemDestinationLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemDestinationLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            tempItemDestinationLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            tempItemDestinationLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            tempItemDestinationLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(tempItemDestinationLocation);
                        }
                    }
                }
                //data purchase order (item-arrive status : updated)
                memorandumInvoice.getTblPurchaseOrder().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                memorandumInvoice.getTblPurchaseOrder().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.saveOrUpdate(memorandumInvoice.getTblPurchaseOrder());
//                rs = session.getNamedQuery("updateTblPurchaseOrderItemArriveStatusByIDPurchaseOrder")
//                        .setParameter("idPurchaseOrder", memorandumInvoice.getTblPurchaseOrder().getIdpo());
//                errMsg = (String) rs.uniqueResult();
//                if (!errMsg.equals("")) {
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    return false;
//                }
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

//    @Override
//    public boolean updateApproveDataMemorandumInvoice(TblMemorandumInvoice memorandumInvoice) {
//        errMsg = "";
//        try {
//            session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            //data memorandum invoice
//            memorandumInvoice.setRefMemorandumInvoiceStatus(session.find(RefMemorandumInvoiceStatus.class, 1)); //approved
//            memorandumInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//            memorandumInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//            session.update(memorandumInvoice);
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
//        return true;
//    }
//    @Override
//    public boolean deleteDataMemorandumInvoice(TblMemorandumInvoice memorandumInvoice) {
//        errMsg = "";
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                if (memorandumInvoice.getRefRecordStatus().getIdstatus() == 1) {
//                    //data memorandum invoice
//                    memorandumInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    memorandumInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    memorandumInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
//                    session.update(memorandumInvoice);
//                    //data memorandum invoice detail
//                    Query rs = session.getNamedQuery("deleteAllTblMemorandumInvoiceDetailByIDMemorandumInvoice")
//                            .setParameter("idMemorandumInvoice", memorandumInvoice.getIdmi())
//                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
//                    errMsg = (String) rs.uniqueResult();
//                    if (!errMsg.equals("")) {
//                        if (session.getTransaction().isActive()) {
//                            session.getTransaction().rollback();
//                        }
//                        return false;
//                    }
//                    //data purchase order (item-arrive status : updated) .. --> can be make error data
//                    memorandumInvoice.getTblPurchaseOrder().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    memorandumInvoice.getTblPurchaseOrder().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    session.saveOrUpdate(memorandumInvoice.getTblPurchaseOrder());
////                    rs = session.getNamedQuery("updateTblPurchaseOrderItemArriveStatusByIDPurchaseOrder")
////                            .setParameter("idPurchaseOrder", memorandumInvoice.getTblPurchaseOrder().getIdpo());
////                    errMsg = (String) rs.uniqueResult();
////                    if (!errMsg.equals("")) {
////                        if (session.getTransaction().isActive()) {
////                            session.getTransaction().rollback();
////                        }
////                        return false;
////                    }
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

    //--------------------------------------------------------------------------
    @Override
    public TblMemorandumInvoiceDetail getDataMemorandumInvoiceDetail(long id) {
        errMsg = "";
        TblMemorandumInvoiceDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblMemorandumInvoiceDetail) session.find(TblMemorandumInvoiceDetail.class, id);
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
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetail() {
        errMsg = "";
        List<TblMemorandumInvoiceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceDetail").list();
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
    public RefMemorandumInvoiceStatus getDataMemorandumInvoiceStatus(int id) {
        errMsg = "";
        RefMemorandumInvoiceStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefMemorandumInvoiceStatus) session.find(RefMemorandumInvoiceStatus.class, id);
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
    public List<TblPurchaseOrder> getAllDataPurchaseOrder() {
        errMsg = "";
        List<TblPurchaseOrder> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrder").list();
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
    public List<RefPurchaseOrderStatus> getAllDataPurchaseOrderStatus() {
        errMsg = "";
        List<RefPurchaseOrderStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefPurchaseOrderStatus").list();
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
    public List<RefPurchaseOrderItemArriveStatus> getAllDataPurchaseOrderItemArriveStatus() {
        errMsg = "";
        List<RefPurchaseOrderItemArriveStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefPurchaseOrderItemArriveStatus").list();
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
    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetailByIDPurchaseOrder(long idPurchaseOrder) {
        errMsg = "";
        List<TblPurchaseOrderDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderDetailByIDPurchaseOrder")
                        .setParameter("idPurchaseOrder", idPurchaseOrder)
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

    @Override
    public List<TblSupplierItem> getAllDataSupplierItemByIDSupplier(long idSupplier) {
        errMsg = "";
        List<TblSupplierItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierItemByIDSupplier")
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
    public List<TblItem> getAllDataItem() {
        errMsg = "";
        List<TblItem> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItem").list();
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
    public List<TblUnit> getAllDataUnit() {
        errMsg = "";
        List<TblUnit> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblUnit").list();
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
    public List<RefItemType> getAllDataItemType() {
        errMsg = "";
        List<RefItemType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefItemType").list();
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

    //--------------------------------------------------------------------------
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

    @Override
    public TblBuilding getDataBuilding(long id) {
        errMsg = "";
        TblBuilding data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblBuilding) session.find(TblBuilding.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return data;
    }

    @Override
    public TblFloor getDataFloor(long id) {
        errMsg = "";
        TblFloor data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblFloor) session.find(TblFloor.class, id);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return data;
    }

    //--------------------------------------------------------------------------
//    @Override
//    public TblPurchaseOrderDetail getDataPurchaseOrderDetailByIDPurchaseOrderAndIDItem(long idPO, long idItem) {
//        errMsg = "";
//        List<TblPurchaseOrderDetail> list = new ArrayList<>();
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                list = session.getNamedQuery("findAllTblPurchaseOrderDetailByIDPurchaseOrderAndIDItem")
//                        .setParameter("idPurchaseOrder", idPO)
//                        .setParameter("idItem", idItem)
//                        .list();
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//            } finally {
//                //session.close();
//            }
//        }
//        return list.isEmpty() ? null : list.get(0);
//    }
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

//    @Override
//    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDItem(long idPO, long idItem) {
//        errMsg = "";
//        List<TblMemorandumInvoiceDetail> list = new ArrayList<>();
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailByIDPurchaseOrderAndIDItemAndIsNotBonus")
//                        .setParameter("idPurchaseOrder", idPO)
//                        .setParameter("idItem", idItem)
//                        .list();
//                session.getTransaction().commit();
//            } catch (Exception e) {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = e.getMessage();
//            } finally {
//                //session.close();
//            }
//        }
//        return list;
//    }
    @Override
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(long idPO, long idSupplierItem) {
        errMsg = "";
        List<TblMemorandumInvoiceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItemAndIsNotBonus")
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
        return list;
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

    //--------------------------------------------------------------------------
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

    //--------------------------------------------------------------------------
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
    public RefItemExpiredDateStatus getDataItemExpiredDateStatus(int id) {
        errMsg = "";
        RefItemExpiredDateStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefItemExpiredDateStatus) session.find(RefItemExpiredDateStatus.class, id);
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
    public TblHotelPayable getDataHotelPayable(long id) {
        errMsg = "";
        TblHotelPayable data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblHotelPayable) session.find(TblHotelPayable.class, id);
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
