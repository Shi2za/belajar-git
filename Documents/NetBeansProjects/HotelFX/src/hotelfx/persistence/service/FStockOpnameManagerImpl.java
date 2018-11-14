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
import hotelfx.persistence.model.RefItemMutationType;
import hotelfx.persistence.model.RefItemObsoleteBy;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefLocationType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysAccount;
import hotelfx.persistence.model.TblEmployee;
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
import hotelfx.persistence.model.TblLocationOfBin;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.model.TblPropertyBarcode;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblStockOpname;
import hotelfx.persistence.model.TblStockOpnameDetail;
import hotelfx.persistence.model.TblStockOpnameDetailItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameDetailPropertyBarcode;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblUnit;
import hotelfx.view.feature_laundry.laundry_stock_opname.LaundryStockOpnameController;
import hotelfx.view.feature_stock_opname.StockOpnameController;
import hotelfx.view.feature_warehouse.warehouse_stock_opname.WarehouseStockOpnameController;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author ANDRI
 */
public class FStockOpnameManagerImpl implements FStockOpnameManager {

    private Session session;

    private String errMsg;

    public FStockOpnameManagerImpl() {

    }

    @Override
    public TblStockOpname insertDataStockOpname(TblStockOpname stockOpname,
            List<StockOpnameController.ClassSODetail> classSODetails) {
        errMsg = "";
        TblStockOpname tblStockOpname = stockOpname;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data stock opname
                tblStockOpname.setCodeStockOpname(ClassCoder.getCode("Stock Opname", session));
//                tblStockOpname.setStockOpanameDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblStockOpname);
                //data stock-opname (location)
                TblLocation dataLSO = getFirstDataLocationStockOpname();
                if (dataLSO == null) {    //Location of 'StockOpname' is not found...
                    errMsg = "Terjadi kesalahan pada lokasi (StockOpname)..!";
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return null;
                }
                for (StockOpnameController.ClassSODetail classSODetail : classSODetails) {
                    //data stock opname - detail
                    classSODetail.getDataSODetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    classSODetail.getDataSODetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    classSODetail.getDataSODetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    classSODetail.getDataSODetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    classSODetail.getDataSODetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(classSODetail.getDataSODetail());
                    //data details
                    if (classSODetail.getDataSODetailPropertyBarcode() != null) { //property
                        //data stock opname - detail - property barcode
                        classSODetail.getDataSODetailPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        classSODetail.getDataSODetailPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        classSODetail.getDataSODetailPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        classSODetail.getDataSODetailPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        classSODetail.getDataSODetailPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(classSODetail.getDataSODetailPropertyBarcode());
                    } else {
                        if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                            //data stock opname - detail - item expired date
                            classSODetail.getDataSODetailItemExpiredDate().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            classSODetail.getDataSODetailItemExpiredDate().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            classSODetail.getDataSODetailItemExpiredDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            classSODetail.getDataSODetailItemExpiredDate().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            classSODetail.getDataSODetailItemExpiredDate().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(classSODetail.getDataSODetailItemExpiredDate());
                        }
                    }
                    //data different stock
                    BigDecimal diffrenceStock = classSODetail.getDataSODetail().getItemQuantityReal().subtract(classSODetail.getDataSODetail().getItemQuantitySystem());
                    if (diffrenceStock.compareTo(new BigDecimal("0")) == 1) { //+stock
                        //data item mutation history
                        TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                        dataMutation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                        dataMutation.setTblLocationByIdlocationOfSource(dataLSO);
                        dataMutation.setTblLocationByIdlocationOfDestination(tblStockOpname.getTblLocation());
                        dataMutation.setItemQuantity(diffrenceStock.abs());
                        dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 0));    //Dipindahkan = '0'
                        dataMutation.setRefItemObsoleteBy(null);
                        dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataMutation);
                        //data item location (+)
                        TblItemLocation dataItemLocation = getItemLocationByIDLocationAndIDItem(
                                dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                classSODetail.getDataSODetail().getTblItem().getIditem());
                        if (dataItemLocation != null) {
                            dataItemLocation.setItemQuantity(dataItemLocation.getItemQuantity().add(dataMutation.getItemQuantity()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataItemLocation);
                        } else {
                            dataItemLocation = new TblItemLocation();
                            dataItemLocation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                            dataItemLocation.setTblLocation(dataMutation.getTblLocationByIdlocationOfDestination());
                            dataItemLocation.setItemQuantity(dataMutation.getItemQuantity());
                            dataItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataItemLocation);
                        }
                        //data item location (stockopname, -)
                        TblItemLocation dataItemLocationSO = getItemLocationByIDLocationAndIDItem(
                                dataMutation.getTblLocationByIdlocationOfSource().getIdlocation(),
                                classSODetail.getDataSODetail().getTblItem().getIditem());
                        if (dataItemLocationSO == null) {
                            errMsg = "Barang (" + dataMutation.getTblItem().getItemName() + ") tidak ditemukan..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        dataItemLocationSO.setItemQuantity(dataItemLocationSO.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                        dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        if (dataItemLocationSO.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                            errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName() + ") tidak mencukupi..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        session.update(dataItemLocationSO);
                        //data details
                        if (classSODetail.getDataSODetailPropertyBarcode() != null) {
                            //data item mutation history - property barcode
                            TblItemMutationHistoryPropertyBarcode dataMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            dataMutationPropertyBarcode.setTblItemMutationHistory(dataMutation);
                            dataMutationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                            dataMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutationPropertyBarcode);
                            //data item location - property barcode (add)
                            TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                    dataItemLocation.getIdrelation(),
                                    classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (dataItemLocationPropertyBarcode != null) {
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.update(dataItemLocationPropertyBarcode);
                            } else {
                                dataItemLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                dataItemLocationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                dataItemLocationPropertyBarcode.setTblItemLocation(dataItemLocationSO);
                                dataItemLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataItemLocationPropertyBarcode);
                            }
                            //data item location - property barcode (stockopname, remove)
                            TblItemLocationPropertyBarcode dataItemLocationPropertyBarcodeSO = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                    dataItemLocationSO.getIdrelation(),
                                    classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());

                            if (dataItemLocationPropertyBarcodeSO == null) {
                                errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                        + " - " + dataMutationPropertyBarcode.getTblPropertyBarcode().getCodeBarcode()
                                        + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                            session.update(dataItemLocationPropertyBarcodeSO);
                        } else {
                            if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                                //data item mutation history - item expired date
                                TblItemMutationHistoryItemExpiredDate dataMutationItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                                dataMutationItemExpiredDate.setTblItemMutationHistory(dataMutation);
                                dataMutationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                dataMutationItemExpiredDate.setItemQuantity(diffrenceStock.abs());
                                dataMutationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataMutationItemExpiredDate);
                                //data item location - item expired date (+)
                                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                        dataItemLocation.getIdrelation(),
                                        classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                if (dataItemLocationItemExpiredDate != null) {
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataItemLocationItemExpiredDate.getItemQuantity().add((dataMutationItemExpiredDate.getItemQuantity())));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    session.update(dataItemLocationItemExpiredDate);
                                } else {
                                    dataItemLocationItemExpiredDate = new TblItemLocationItemExpiredDate();
                                    dataItemLocationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                    dataItemLocationItemExpiredDate.setTblItemLocation(dataItemLocationSO);
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataMutationItemExpiredDate.getItemQuantity());
                                    dataItemLocationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataItemLocationItemExpiredDate);
                                }
                                //data item location - item expired date (stockopname, +)
                                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDateSO = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                        dataItemLocationSO.getIdrelation(),
                                        classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());

                                if (dataItemLocationItemExpiredDateSO == null) {
                                    errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                            + ") tidak ditemukan..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                dataItemLocationItemExpiredDateSO.setItemQuantity(dataItemLocationItemExpiredDateSO.getItemQuantity().subtract((dataMutationItemExpiredDate.getItemQuantity())));
                                dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                if (dataItemLocationItemExpiredDateSO.getItemQuantity()
                                        .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                    errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                            + ") tidak mencukupi..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                session.update(dataItemLocationItemExpiredDateSO);
                            }
                        }
                    } else {
                        if (diffrenceStock.compareTo(new BigDecimal("0")) == -1) {    //-stock
                            //data item mutation history
                            TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                            dataMutation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                            dataMutation.setTblLocationByIdlocationOfSource(tblStockOpname.getTblLocation());
                            dataMutation.setTblLocationByIdlocationOfDestination(dataLSO);
                            dataMutation.setItemQuantity(diffrenceStock.abs());
                            dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));    //Rusak = '2'
                            dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 3));    //Lainnya = '3'
                            dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutation);
                            //data item location (-)
                            TblItemLocation dataItemLocation = getItemLocationByIDLocationAndIDItem(
                                    dataMutation.getTblLocationByIdlocationOfSource().getIdlocation(),
                                    classSODetail.getDataSODetail().getTblItem().getIditem());
                            if (dataItemLocation == null) {
                                errMsg = "Barang (" + dataMutation.getTblItem().getItemName() + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            dataItemLocation.setItemQuantity(dataItemLocation.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            if (dataItemLocation.getItemQuantity()
                                    .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName() + ") tidak mencukupi..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            session.update(dataItemLocation);
                            //data item location (stockopanme, +)
                            TblItemLocation dataItemLocationSO = getItemLocationByIDLocationAndIDItem(
                                    dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                    classSODetail.getDataSODetail().getTblItem().getIditem());
                            if (dataItemLocationSO != null) {
                                dataItemLocationSO.setItemQuantity(dataItemLocationSO.getItemQuantity().add(dataMutation.getItemQuantity()));
                                dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(dataItemLocationSO);
                            } else {
                                dataItemLocationSO = new TblItemLocation();
                                dataItemLocationSO.setTblItem(classSODetail.getDataSODetail().getTblItem());
                                dataItemLocationSO.setTblLocation(dataMutation.getTblLocationByIdlocationOfDestination());
                                dataItemLocationSO.setItemQuantity(dataMutation.getItemQuantity());
                                dataItemLocationSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataItemLocationSO);
                            }
                            //data details
                            if (classSODetail.getDataSODetailPropertyBarcode() != null) {
                                //data item mutation history - property barcode
                                TblItemMutationHistoryPropertyBarcode dataMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                                dataMutationPropertyBarcode.setTblItemMutationHistory(dataMutation);
                                dataMutationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                dataMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataMutationPropertyBarcode);
                                //data item location - property barcode (remove)
                                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                        dataItemLocation.getIdrelation(),
                                        classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                                if (dataItemLocationPropertyBarcode == null) {
                                    errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + dataMutationPropertyBarcode.getTblPropertyBarcode().getCodeBarcode()
                                            + ") tidak ditemukan..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                session.update(dataItemLocationPropertyBarcode);
                                //data item location - property barcode (stockopname, add)
                                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcodeSO = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                        dataItemLocationSO.getIdrelation(),
                                        classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                                if (dataItemLocationPropertyBarcodeSO != null) {
                                    dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.update(dataItemLocationPropertyBarcodeSO);
                                } else {
                                    dataItemLocationPropertyBarcodeSO = new TblItemLocationPropertyBarcode();
                                    dataItemLocationPropertyBarcodeSO.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                    dataItemLocationPropertyBarcodeSO.setTblItemLocation(dataItemLocationSO);
                                    dataItemLocationPropertyBarcodeSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataItemLocationPropertyBarcodeSO);
                                }
                            } else {
                                if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                                    //data item mutation history - item expired date
                                    TblItemMutationHistoryItemExpiredDate dataMutationItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                                    dataMutationItemExpiredDate.setTblItemMutationHistory(dataMutation);
                                    dataMutationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                    dataMutationItemExpiredDate.setItemQuantity(diffrenceStock.abs());
                                    dataMutationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataMutationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataMutationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataMutationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataMutationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataMutationItemExpiredDate);
                                    //data item location - item expired date (-)
                                    TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                            dataItemLocation.getIdrelation(),
                                            classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                    if (dataItemLocationItemExpiredDate == null) {
                                        errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                                + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                                + ") tidak ditemukan..!";
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        return null;
                                    }
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataItemLocationItemExpiredDate.getItemQuantity().subtract((dataMutationItemExpiredDate.getItemQuantity())));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    if (dataItemLocationItemExpiredDate.getItemQuantity()
                                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                        errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName()
                                                + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                                + ") tidak mencukupi..!";
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        return null;
                                    }
                                    session.update(dataItemLocationItemExpiredDate);
                                    //data item location - item expired date (stockopname, +)
                                    TblItemLocationItemExpiredDate dataItemLocationItemExpiredDateSO = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                            dataItemLocationSO.getIdrelation(),
                                            classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                    if (dataItemLocationItemExpiredDateSO != null) {
                                        dataItemLocationItemExpiredDateSO.setItemQuantity(dataItemLocationItemExpiredDateSO.getItemQuantity().add((dataMutationItemExpiredDate.getItemQuantity())));
                                        dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        session.update(dataItemLocationItemExpiredDateSO);
                                    } else {
                                        dataItemLocationItemExpiredDateSO = new TblItemLocationItemExpiredDate();
                                        dataItemLocationItemExpiredDateSO.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                        dataItemLocationItemExpiredDateSO.setTblItemLocation(dataItemLocationSO);
                                        dataItemLocationItemExpiredDateSO.setItemQuantity(dataMutationItemExpiredDate.getItemQuantity());
                                        dataItemLocationItemExpiredDateSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataItemLocationItemExpiredDateSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(dataItemLocationItemExpiredDateSO);
                                    }
                                }
                            }
                        } else {
                            //do nothing...
                        }
                    }
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
        return tblStockOpname;
    }

    @Override
    public TblStockOpname insertDataStockOpnameWarehouse(TblStockOpname stockOpname,
            List<WarehouseStockOpnameController.ClassSODetail> classSODetails) {
        errMsg = "";
        TblStockOpname tblStockOpname = stockOpname;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data stock opname
                tblStockOpname.setCodeStockOpname(ClassCoder.getCode("Stock Opname", session));
//                tblStockOpname.setStockOpanameDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblStockOpname);
                //data stock-opname (location)
                TblLocation dataLSO = getFirstDataLocationStockOpname();
                if (dataLSO == null) {    //Location of 'StockOpname' is not found...
                    errMsg = "Terjadi kesalahan pada lokasi (StockOpname)..!";
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return null;
                }
                for (WarehouseStockOpnameController.ClassSODetail classSODetail : classSODetails) {
                    //data stock opname - detail
                    classSODetail.getDataSODetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    classSODetail.getDataSODetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    classSODetail.getDataSODetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    classSODetail.getDataSODetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    classSODetail.getDataSODetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(classSODetail.getDataSODetail());
                    //data details
                    if (classSODetail.getDataSODetailPropertyBarcode() != null) { //property
                        //data stock opname - detail - property barcode
                        classSODetail.getDataSODetailPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        classSODetail.getDataSODetailPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        classSODetail.getDataSODetailPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        classSODetail.getDataSODetailPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        classSODetail.getDataSODetailPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(classSODetail.getDataSODetailPropertyBarcode());
                    } else {
                        if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                            //data stock opname - detail - item expired date
                            classSODetail.getDataSODetailItemExpiredDate().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            classSODetail.getDataSODetailItemExpiredDate().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            classSODetail.getDataSODetailItemExpiredDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            classSODetail.getDataSODetailItemExpiredDate().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            classSODetail.getDataSODetailItemExpiredDate().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(classSODetail.getDataSODetailItemExpiredDate());
                        }
                    }
                    //data different stock
                    BigDecimal diffrenceStock = classSODetail.getDataSODetail().getItemQuantityReal().subtract(classSODetail.getDataSODetail().getItemQuantitySystem());
                    if (diffrenceStock.compareTo(new BigDecimal("0")) == 1) { //+stock
                        System.out.println("ooiii");
                        //data item mutation history
                        TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                        dataMutation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                        dataMutation.setTblLocationByIdlocationOfSource(dataLSO);
                        dataMutation.setTblLocationByIdlocationOfDestination(tblStockOpname.getTblLocation());
                        dataMutation.setItemQuantity(diffrenceStock.abs());
                        dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 0));    //Dipindahkan = '0'
                        dataMutation.setRefItemObsoleteBy(null);
                        dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataMutation);
                        //data item location (+)
                        TblItemLocation dataItemLocation = getItemLocationByIDLocationAndIDItem(
                                dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                classSODetail.getDataSODetail().getTblItem().getIditem());
                        if (dataItemLocation != null) {
                            dataItemLocation.setItemQuantity(dataItemLocation.getItemQuantity().add(dataMutation.getItemQuantity()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataItemLocation);
                        } else {
                            dataItemLocation = new TblItemLocation();
                            dataItemLocation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                            dataItemLocation.setTblLocation(dataMutation.getTblLocationByIdlocationOfDestination());
                            dataItemLocation.setItemQuantity(dataMutation.getItemQuantity());
                            dataItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataItemLocation);
                        }
                        //data item location (stockopname, -)
                        TblItemLocation dataItemLocationSO = getItemLocationByIDLocationAndIDItem(
                                dataMutation.getTblLocationByIdlocationOfSource().getIdlocation(),
                                classSODetail.getDataSODetail().getTblItem().getIditem());
                        if (dataItemLocationSO == null) {
                            errMsg = "Barang (" + dataMutation.getTblItem().getItemName() + ") tidak ditemukan..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        dataItemLocationSO.setItemQuantity(dataItemLocationSO.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                        dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        if (dataItemLocationSO.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                            errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName() + ") tidak mencukupi..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        session.update(dataItemLocationSO);
                        //data details
                        if (classSODetail.getDataSODetailPropertyBarcode() != null) {
                            //data item mutation history - property barcode
                            TblItemMutationHistoryPropertyBarcode dataMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            dataMutationPropertyBarcode.setTblItemMutationHistory(dataMutation);
                            dataMutationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                            dataMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutationPropertyBarcode);
                            //data item location - property barcode (add)
                            TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                    dataItemLocation.getIdrelation(),
                                    classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (dataItemLocationPropertyBarcode != null) {
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.update(dataItemLocationPropertyBarcode);
                            } else {
                                dataItemLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                dataItemLocationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                dataItemLocationPropertyBarcode.setTblItemLocation(dataItemLocationSO);
                                dataItemLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataItemLocationPropertyBarcode);
                            }
                            //data item location - property barcode (stockopname, remove)
                            TblItemLocationPropertyBarcode dataItemLocationPropertyBarcodeSO = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                    dataItemLocationSO.getIdrelation(),
                                    classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());

                            if (dataItemLocationPropertyBarcodeSO == null) {
                                errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                        + " - " + dataMutationPropertyBarcode.getTblPropertyBarcode().getCodeBarcode()
                                        + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                            session.update(dataItemLocationPropertyBarcodeSO);
                        } else {
                            if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                                //data item mutation history - item expired date
                                TblItemMutationHistoryItemExpiredDate dataMutationItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                                dataMutationItemExpiredDate.setTblItemMutationHistory(dataMutation);
                                dataMutationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                dataMutationItemExpiredDate.setItemQuantity(diffrenceStock.abs());
                                dataMutationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataMutationItemExpiredDate);
                                //data item location - item expired date (+)
                                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                        dataItemLocation.getIdrelation(),
                                        classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                if (dataItemLocationItemExpiredDate != null) {
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataItemLocationItemExpiredDate.getItemQuantity().add((dataMutationItemExpiredDate.getItemQuantity())));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    session.update(dataItemLocationItemExpiredDate);
                                } else {
                                    dataItemLocationItemExpiredDate = new TblItemLocationItemExpiredDate();
                                    dataItemLocationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                    dataItemLocationItemExpiredDate.setTblItemLocation(dataItemLocationSO);
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataMutationItemExpiredDate.getItemQuantity());
                                    dataItemLocationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataItemLocationItemExpiredDate);
                                }
                                //data item location - item expired date (stockopname, +)
                                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDateSO = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                        dataItemLocationSO.getIdrelation(),
                                        classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());

                                if (dataItemLocationItemExpiredDateSO == null) {
                                    errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                            + ") tidak ditemukan..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                dataItemLocationItemExpiredDateSO.setItemQuantity(dataItemLocationItemExpiredDateSO.getItemQuantity().subtract((dataMutationItemExpiredDate.getItemQuantity())));
                                dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                if (dataItemLocationItemExpiredDateSO.getItemQuantity()
                                        .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                    errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                            + ") tidak mencukupi..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                session.update(dataItemLocationItemExpiredDateSO);
                            }
                        }
                    } else {
                        if (diffrenceStock.compareTo(new BigDecimal("0")) == -1) {    //-stock
                            //data item mutation history
                            TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                            dataMutation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                            dataMutation.setTblLocationByIdlocationOfSource(tblStockOpname.getTblLocation());
                            dataMutation.setTblLocationByIdlocationOfDestination(dataLSO);
                            dataMutation.setItemQuantity(diffrenceStock.abs());
                            dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));    //Rusak = '2'
                            dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 3));    //Lainnya = '3'
                            dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutation);
                            //data item location (-)
                            TblItemLocation dataItemLocation = getItemLocationByIDLocationAndIDItem(
                                    dataMutation.getTblLocationByIdlocationOfSource().getIdlocation(),
                                    classSODetail.getDataSODetail().getTblItem().getIditem());
                            if (dataItemLocation == null) {
                                errMsg = "Barang (" + dataMutation.getTblItem().getItemName() + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            dataItemLocation.setItemQuantity(dataItemLocation.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            if (dataItemLocation.getItemQuantity()
                                    .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName() + ") tidak mencukupi..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            session.update(dataItemLocation);
                            //data item location (stockopanme, +)
                            TblItemLocation dataItemLocationSO = getItemLocationByIDLocationAndIDItem(
                                    dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                    classSODetail.getDataSODetail().getTblItem().getIditem());
                            if (dataItemLocationSO != null) {
                                dataItemLocationSO.setItemQuantity(dataItemLocationSO.getItemQuantity().add(dataMutation.getItemQuantity()));
                                dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(dataItemLocationSO);
                            } else {
                                dataItemLocationSO = new TblItemLocation();
                                dataItemLocationSO.setTblItem(classSODetail.getDataSODetail().getTblItem());
                                dataItemLocationSO.setTblLocation(dataMutation.getTblLocationByIdlocationOfDestination());
                                dataItemLocationSO.setItemQuantity(dataMutation.getItemQuantity());
                                dataItemLocationSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataItemLocationSO);
                            }
                            //data details
                            if (classSODetail.getDataSODetailPropertyBarcode() != null) {
                                //data item mutation history - property barcode
                                TblItemMutationHistoryPropertyBarcode dataMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                                dataMutationPropertyBarcode.setTblItemMutationHistory(dataMutation);
                                dataMutationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                dataMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataMutationPropertyBarcode);
                                //data item location - property barcode (remove)
                                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                        dataItemLocation.getIdrelation(),
                                        classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                                if (dataItemLocationPropertyBarcode == null) {
                                    errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + dataMutationPropertyBarcode.getTblPropertyBarcode().getCodeBarcode()
                                            + ") tidak ditemukan..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                session.update(dataItemLocationPropertyBarcode);
                                //data item location - property barcode (stockopname, add)
                                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcodeSO = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                        dataItemLocationSO.getIdrelation(),
                                        classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                                if (dataItemLocationPropertyBarcodeSO != null) {
                                    dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.update(dataItemLocationPropertyBarcodeSO);
                                } else {
                                    dataItemLocationPropertyBarcodeSO = new TblItemLocationPropertyBarcode();
                                    dataItemLocationPropertyBarcodeSO.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                    dataItemLocationPropertyBarcodeSO.setTblItemLocation(dataItemLocationSO);
                                    dataItemLocationPropertyBarcodeSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataItemLocationPropertyBarcodeSO);
                                }
                            } else {
                                if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                                    //data item mutation history - item expired date
                                    TblItemMutationHistoryItemExpiredDate dataMutationItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                                    dataMutationItemExpiredDate.setTblItemMutationHistory(dataMutation);
                                    dataMutationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                    dataMutationItemExpiredDate.setItemQuantity(diffrenceStock.abs());
                                    dataMutationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataMutationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataMutationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataMutationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataMutationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataMutationItemExpiredDate);
                                    //data item location - item expired date (-)
                                    TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                            dataItemLocation.getIdrelation(),
                                            classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                    if (dataItemLocationItemExpiredDate == null) {
                                        errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                                + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                                + ") tidak ditemukan..!";
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        return null;
                                    }
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataItemLocationItemExpiredDate.getItemQuantity().subtract((dataMutationItemExpiredDate.getItemQuantity())));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    if (dataItemLocationItemExpiredDate.getItemQuantity()
                                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                        errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName()
                                                + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                                + ") tidak mencukupi..!";
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        return null;
                                    }
                                    session.update(dataItemLocationItemExpiredDate);
                                    //data item location - item expired date (stockopname, +)
                                    TblItemLocationItemExpiredDate dataItemLocationItemExpiredDateSO = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                            dataItemLocationSO.getIdrelation(),
                                            classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                    if (dataItemLocationItemExpiredDateSO != null) {
                                        dataItemLocationItemExpiredDateSO.setItemQuantity(dataItemLocationItemExpiredDateSO.getItemQuantity().add((dataMutationItemExpiredDate.getItemQuantity())));
                                        dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        session.update(dataItemLocationItemExpiredDateSO);
                                    } else {
                                        dataItemLocationItemExpiredDateSO = new TblItemLocationItemExpiredDate();
                                        dataItemLocationItemExpiredDateSO.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                        dataItemLocationItemExpiredDateSO.setTblItemLocation(dataItemLocationSO);
                                        dataItemLocationItemExpiredDateSO.setItemQuantity(dataMutationItemExpiredDate.getItemQuantity());
                                        dataItemLocationItemExpiredDateSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataItemLocationItemExpiredDateSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(dataItemLocationItemExpiredDateSO);
                                    }
                                }
                            }
                        } else {
                            //do nothing...
                        }
                    }
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
        return tblStockOpname;
    }

    @Override
    public TblStockOpname insertDataStockOpnameLaundry(TblStockOpname stockOpname,
            List<LaundryStockOpnameController.ClassSODetail> classSODetails) {
        errMsg = "";
        TblStockOpname tblStockOpname = stockOpname;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data stock opname
                tblStockOpname.setCodeStockOpname(ClassCoder.getCode("Stock Opname", session));
//                tblStockOpname.setStockOpanameDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblStockOpname.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblStockOpname.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblStockOpname);
                //data stock-opname (location)
                TblLocation dataLSO = getFirstDataLocationStockOpname();
                if (dataLSO == null) {    //Location of 'StockOpname' is not found...
                    errMsg = "Terjadi kesalahan pada lokasi (StockOpname)..!";
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return null;
                }
                for (LaundryStockOpnameController.ClassSODetail classSODetail : classSODetails) {
                    //data stock opname - detail
                    classSODetail.getDataSODetail().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    classSODetail.getDataSODetail().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    classSODetail.getDataSODetail().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    classSODetail.getDataSODetail().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    classSODetail.getDataSODetail().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(classSODetail.getDataSODetail());
                    //data details
                    if (classSODetail.getDataSODetailPropertyBarcode() != null) { //property
                        //data stock opname - detail - property barcode
                        classSODetail.getDataSODetailPropertyBarcode().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        classSODetail.getDataSODetailPropertyBarcode().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        classSODetail.getDataSODetailPropertyBarcode().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        classSODetail.getDataSODetailPropertyBarcode().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        classSODetail.getDataSODetailPropertyBarcode().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(classSODetail.getDataSODetailPropertyBarcode());
                    } else {
                        if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                            //data stock opname - detail - item expired date
                            classSODetail.getDataSODetailItemExpiredDate().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            classSODetail.getDataSODetailItemExpiredDate().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            classSODetail.getDataSODetailItemExpiredDate().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            classSODetail.getDataSODetailItemExpiredDate().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            classSODetail.getDataSODetailItemExpiredDate().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(classSODetail.getDataSODetailItemExpiredDate());
                        }
                    }
                    //data different stock
                    BigDecimal diffrenceStock = classSODetail.getDataSODetail().getItemQuantityReal().subtract(classSODetail.getDataSODetail().getItemQuantitySystem());
                    if (diffrenceStock.compareTo(new BigDecimal("0")) == 1) { //+stock
                        //data item mutation history
                        TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                        dataMutation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                        dataMutation.setTblLocationByIdlocationOfSource(dataLSO);
                        dataMutation.setTblLocationByIdlocationOfDestination(tblStockOpname.getTblLocation());
                        dataMutation.setItemQuantity(diffrenceStock.abs());
                        dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 0));    //Dipindahkan = '0'
                        dataMutation.setRefItemObsoleteBy(null);
                        dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                        session.saveOrUpdate(dataMutation);
                        //data item location (+)
                        TblItemLocation dataItemLocation = getItemLocationByIDLocationAndIDItem(
                                dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                classSODetail.getDataSODetail().getTblItem().getIditem());
                        if (dataItemLocation != null) {
                            dataItemLocation.setItemQuantity(dataItemLocation.getItemQuantity().add(dataMutation.getItemQuantity()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            session.update(dataItemLocation);
                        } else {
                            dataItemLocation = new TblItemLocation();
                            dataItemLocation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                            dataItemLocation.setTblLocation(dataMutation.getTblLocationByIdlocationOfDestination());
                            dataItemLocation.setItemQuantity(dataMutation.getItemQuantity());
                            dataItemLocation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataItemLocation);
                        }
                        //data item location (stockopname, -)
                        TblItemLocation dataItemLocationSO = getItemLocationByIDLocationAndIDItem(
                                dataMutation.getTblLocationByIdlocationOfSource().getIdlocation(),
                                classSODetail.getDataSODetail().getTblItem().getIditem());
                        if (dataItemLocationSO == null) {
                            errMsg = "Barang (" + dataMutation.getTblItem().getItemName() + ") tidak ditemukan..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        dataItemLocationSO.setItemQuantity(dataItemLocationSO.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                        dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        if (dataItemLocationSO.getItemQuantity()
                                .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                            errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName() + ") tidak mencukupi..!";
                            if (session.getTransaction().isActive()) {
                                session.getTransaction().rollback();
                            }
                            return null;
                        }
                        session.update(dataItemLocationSO);
                        //data details
                        if (classSODetail.getDataSODetailPropertyBarcode() != null) {
                            //data item mutation history - property barcode
                            TblItemMutationHistoryPropertyBarcode dataMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                            dataMutationPropertyBarcode.setTblItemMutationHistory(dataMutation);
                            dataMutationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                            dataMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutationPropertyBarcode);
                            //data item location - property barcode (add)
                            TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                    dataItemLocation.getIdrelation(),
                                    classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                            if (dataItemLocationPropertyBarcode != null) {
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.update(dataItemLocationPropertyBarcode);
                            } else {
                                dataItemLocationPropertyBarcode = new TblItemLocationPropertyBarcode();
                                dataItemLocationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                dataItemLocationPropertyBarcode.setTblItemLocation(dataItemLocationSO);
                                dataItemLocationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataItemLocationPropertyBarcode);
                            }
                            //data item location - property barcode (stockopname, remove)
                            TblItemLocationPropertyBarcode dataItemLocationPropertyBarcodeSO = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                    dataItemLocationSO.getIdrelation(),
                                    classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());

                            if (dataItemLocationPropertyBarcodeSO == null) {
                                errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                        + " - " + dataMutationPropertyBarcode.getTblPropertyBarcode().getCodeBarcode()
                                        + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                            session.update(dataItemLocationPropertyBarcodeSO);
                        } else {
                            if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                                //data item mutation history - item expired date
                                TblItemMutationHistoryItemExpiredDate dataMutationItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                                dataMutationItemExpiredDate.setTblItemMutationHistory(dataMutation);
                                dataMutationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                dataMutationItemExpiredDate.setItemQuantity(diffrenceStock.abs());
                                dataMutationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataMutationItemExpiredDate);
                                //data item location - item expired date (+)
                                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                        dataItemLocation.getIdrelation(),
                                        classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                if (dataItemLocationItemExpiredDate != null) {
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataItemLocationItemExpiredDate.getItemQuantity().add((dataMutationItemExpiredDate.getItemQuantity())));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    session.update(dataItemLocationItemExpiredDate);
                                } else {
                                    dataItemLocationItemExpiredDate = new TblItemLocationItemExpiredDate();
                                    dataItemLocationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                    dataItemLocationItemExpiredDate.setTblItemLocation(dataItemLocationSO);
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataMutationItemExpiredDate.getItemQuantity());
                                    dataItemLocationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataItemLocationItemExpiredDate);
                                }
                                //data item location - item expired date (stockopname, +)
                                TblItemLocationItemExpiredDate dataItemLocationItemExpiredDateSO = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                        dataItemLocationSO.getIdrelation(),
                                        classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());

                                if (dataItemLocationItemExpiredDateSO == null) {
                                    errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                            + ") tidak ditemukan..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                dataItemLocationItemExpiredDateSO.setItemQuantity(dataItemLocationItemExpiredDateSO.getItemQuantity().subtract((dataMutationItemExpiredDate.getItemQuantity())));
                                dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                if (dataItemLocationItemExpiredDateSO.getItemQuantity()
                                        .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                    errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                            + ") tidak mencukupi..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                session.update(dataItemLocationItemExpiredDateSO);
                            }
                        }
                    } else {
                        if (diffrenceStock.compareTo(new BigDecimal("0")) == -1) {    //-stock
                            //data item mutation history
                            TblItemMutationHistory dataMutation = new TblItemMutationHistory();
                            dataMutation.setTblItem(classSODetail.getDataSODetail().getTblItem());
                            dataMutation.setTblLocationByIdlocationOfSource(tblStockOpname.getTblLocation());
                            dataMutation.setTblLocationByIdlocationOfDestination(dataLSO);
                            dataMutation.setItemQuantity(diffrenceStock.abs());
                            dataMutation.setRefItemMutationType(session.find(RefItemMutationType.class, 2));    //Rusak = '2'
                            dataMutation.setRefItemObsoleteBy(session.find(RefItemObsoleteBy.class, 3));    //Lainnya = '3'
                            dataMutation.setMutationDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataMutation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            dataMutation.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                            session.saveOrUpdate(dataMutation);
                            //data item location (-)
                            TblItemLocation dataItemLocation = getItemLocationByIDLocationAndIDItem(
                                    dataMutation.getTblLocationByIdlocationOfSource().getIdlocation(),
                                    classSODetail.getDataSODetail().getTblItem().getIditem());
                            if (dataItemLocation == null) {
                                errMsg = "Barang (" + dataMutation.getTblItem().getItemName() + ") tidak ditemukan..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            dataItemLocation.setItemQuantity(dataItemLocation.getItemQuantity().subtract(dataMutation.getItemQuantity()));
                            dataItemLocation.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                            dataItemLocation.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                            if (dataItemLocation.getItemQuantity()
                                    .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName() + ") tidak mencukupi..!";
                                if (session.getTransaction().isActive()) {
                                    session.getTransaction().rollback();
                                }
                                return null;
                            }
                            session.update(dataItemLocation);
                            //data item location (stockopanme, +)
                            TblItemLocation dataItemLocationSO = getItemLocationByIDLocationAndIDItem(
                                    dataMutation.getTblLocationByIdlocationOfDestination().getIdlocation(),
                                    classSODetail.getDataSODetail().getTblItem().getIditem());
                            if (dataItemLocationSO != null) {
                                dataItemLocationSO.setItemQuantity(dataItemLocationSO.getItemQuantity().add(dataMutation.getItemQuantity()));
                                dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                session.update(dataItemLocationSO);
                            } else {
                                dataItemLocationSO = new TblItemLocation();
                                dataItemLocationSO.setTblItem(classSODetail.getDataSODetail().getTblItem());
                                dataItemLocationSO.setTblLocation(dataMutation.getTblLocationByIdlocationOfDestination());
                                dataItemLocationSO.setItemQuantity(dataMutation.getItemQuantity());
                                dataItemLocationSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataItemLocationSO);
                            }
                            //data details
                            if (classSODetail.getDataSODetailPropertyBarcode() != null) {
                                //data item mutation history - property barcode
                                TblItemMutationHistoryPropertyBarcode dataMutationPropertyBarcode = new TblItemMutationHistoryPropertyBarcode();
                                dataMutationPropertyBarcode.setTblItemMutationHistory(dataMutation);
                                dataMutationPropertyBarcode.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                dataMutationPropertyBarcode.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationPropertyBarcode.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataMutationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataMutationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                session.saveOrUpdate(dataMutationPropertyBarcode);
                                //data item location - property barcode (remove)
                                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcode = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                        dataItemLocation.getIdrelation(),
                                        classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                                if (dataItemLocationPropertyBarcode == null) {
                                    errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                            + " - " + dataMutationPropertyBarcode.getTblPropertyBarcode().getCodeBarcode()
                                            + ") tidak ditemukan..!";
                                    if (session.getTransaction().isActive()) {
                                        session.getTransaction().rollback();
                                    }
                                    return null;
                                }
                                dataItemLocationPropertyBarcode.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                dataItemLocationPropertyBarcode.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                dataItemLocationPropertyBarcode.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                                session.update(dataItemLocationPropertyBarcode);
                                //data item location - property barcode (stockopname, add)
                                TblItemLocationPropertyBarcode dataItemLocationPropertyBarcodeSO = getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
                                        dataItemLocationSO.getIdrelation(),
                                        classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode().getIdbarcode());
                                if (dataItemLocationPropertyBarcodeSO != null) {
                                    dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.update(dataItemLocationPropertyBarcodeSO);
                                } else {
                                    dataItemLocationPropertyBarcodeSO = new TblItemLocationPropertyBarcode();
                                    dataItemLocationPropertyBarcodeSO.setTblPropertyBarcode(classSODetail.getDataSODetailPropertyBarcode().getTblPropertyBarcode());
                                    dataItemLocationPropertyBarcodeSO.setTblItemLocation(dataItemLocationSO);
                                    dataItemLocationPropertyBarcodeSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationPropertyBarcodeSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataItemLocationPropertyBarcodeSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataItemLocationPropertyBarcodeSO);
                                }
                            } else {
                                if (classSODetail.getDataSODetailItemExpiredDate() != null) { //item expired date
                                    //data item mutation history - item expired date
                                    TblItemMutationHistoryItemExpiredDate dataMutationItemExpiredDate = new TblItemMutationHistoryItemExpiredDate();
                                    dataMutationItemExpiredDate.setTblItemMutationHistory(dataMutation);
                                    dataMutationItemExpiredDate.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                    dataMutationItemExpiredDate.setItemQuantity(diffrenceStock.abs());
                                    dataMutationItemExpiredDate.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataMutationItemExpiredDate.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataMutationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataMutationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    dataMutationItemExpiredDate.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                    session.saveOrUpdate(dataMutationItemExpiredDate);
                                    //data item location - item expired date (-)
                                    TblItemLocationItemExpiredDate dataItemLocationItemExpiredDate = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                            dataItemLocation.getIdrelation(),
                                            classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                    if (dataItemLocationItemExpiredDate == null) {
                                        errMsg = "Barang (" + dataMutation.getTblItem().getItemName()
                                                + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                                + ") tidak ditemukan..!";
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        return null;
                                    }
                                    dataItemLocationItemExpiredDate.setItemQuantity(dataItemLocationItemExpiredDate.getItemQuantity().subtract((dataMutationItemExpiredDate.getItemQuantity())));
                                    dataItemLocationItemExpiredDate.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                    dataItemLocationItemExpiredDate.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                    if (dataItemLocationItemExpiredDate.getItemQuantity()
                                            .compareTo(new BigDecimal("0")) == -1) {   //data item quantity < 0
                                        errMsg = "Stok Barang (" + dataMutation.getTblItem().getItemName()
                                                + " - " + ClassFormatter.dateFormate.format(dataMutationItemExpiredDate.getTblItemExpiredDate().getItemExpiredDate())
                                                + ") tidak mencukupi..!";
                                        if (session.getTransaction().isActive()) {
                                            session.getTransaction().rollback();
                                        }
                                        return null;
                                    }
                                    session.update(dataItemLocationItemExpiredDate);
                                    //data item location - item expired date (stockopname, +)
                                    TblItemLocationItemExpiredDate dataItemLocationItemExpiredDateSO = getItemLocationItemExpiredDateByIDItemLocationAndIDItemExpiredDate(
                                            dataItemLocationSO.getIdrelation(),
                                            classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate().getIditemExpiredDate());
                                    if (dataItemLocationItemExpiredDateSO != null) {
                                        dataItemLocationItemExpiredDateSO.setItemQuantity(dataItemLocationItemExpiredDateSO.getItemQuantity().add((dataMutationItemExpiredDate.getItemQuantity())));
                                        dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        session.update(dataItemLocationItemExpiredDateSO);
                                    } else {
                                        dataItemLocationItemExpiredDateSO = new TblItemLocationItemExpiredDate();
                                        dataItemLocationItemExpiredDateSO.setTblItemExpiredDate(classSODetail.getDataSODetailItemExpiredDate().getTblItemExpiredDate());
                                        dataItemLocationItemExpiredDateSO.setTblItemLocation(dataItemLocationSO);
                                        dataItemLocationItemExpiredDateSO.setItemQuantity(dataMutationItemExpiredDate.getItemQuantity());
                                        dataItemLocationItemExpiredDateSO.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataItemLocationItemExpiredDateSO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                                        dataItemLocationItemExpiredDateSO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                                        dataItemLocationItemExpiredDateSO.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                                        session.saveOrUpdate(dataItemLocationItemExpiredDateSO);
                                    }
                                }
                            }
                        } else {
                            //do nothing...
                        }
                    }
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
        return tblStockOpname;
    }

    private TblItemLocation getItemLocationByIDLocationAndIDItem(
            long idLocation,
            long idItem) {
        List<TblItemLocation> list = session.getNamedQuery("findAllItemLocationByIdLocationAndIdItem")
                .setParameter("idLocation", idLocation)
                .setParameter("idItem", idItem)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblLocation getFirstDataLocationStockOpname() {
        List<TblLocation> list = session.getNamedQuery("findAllTblLocationByIDLocationType")
                .setParameter("idLocationType", 5) //StockOpname = '5'
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    private TblItemLocationPropertyBarcode getItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode(
            long idItemLocation,
            long idPropertyBarcode) {
        List<TblItemLocationPropertyBarcode> list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIDItemLocationAndIDPropertyBarcode")
                .setParameter("idItemLocation", idItemLocation)
                .setParameter("idPropertyBarcode", idPropertyBarcode)
                .list();
        return list.isEmpty() ? null : list.get(0);
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

    //not used ..!!!
    @Override
    public boolean updateDataStockOpname(TblStockOpname stockOpname,
            List<TblStockOpnameDetail> stockOpnameDetails,
            List<TblStockOpnameDetailItemExpiredDate> stockOpnameDetailItemExpiredDates) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data stock opname
                stockOpname.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                stockOpname.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(stockOpname);
                //delete all (data stock opname detail)
                Query rs = session.getNamedQuery("deleteAllTblStockOpnameDetail")
                        .setParameter("idStockOpname", stockOpname.getIdstockOpname())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //insert all data stock opname detail
                for (TblStockOpnameDetail stockOpnameDetail : stockOpnameDetails) {
                    stockOpnameDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    stockOpnameDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    stockOpnameDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    stockOpnameDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    stockOpnameDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(stockOpnameDetail);
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

    //not used ..!!!
    @Override
    public boolean deleteDataStockOpname(TblStockOpname stockOpname) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if (stockOpname.getRefRecordStatus().getIdstatus() == 1) {
                    //data stock opname
                    stockOpname.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    stockOpname.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    stockOpname.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
                    session.update(stockOpname);
                    //delete all (data stock opname detail)
                    Query rs = session.getNamedQuery("deleteAllTblStockOpnameDetail")
                            .setParameter("idStockOpname", stockOpname.getIdstockOpname())
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
    public TblStockOpname getDataStockOpname(long id) {
        errMsg = "";
        TblStockOpname data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblStockOpname) session.find(TblStockOpname.class, id);
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
    public List<TblStockOpname> getAllDataStockOpname() {
        errMsg = "";
        List<TblStockOpname> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStockOpname").list();
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
    public TblStockOpnameDetail getDataStockOpnameDetail(long id) {
        errMsg = "";
        TblStockOpnameDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblStockOpnameDetail) session.find(TblStockOpnameDetail.class, id);
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
    public List<TblStockOpnameDetail> getAllDataStockOpnameDetailByIDStockOpname(long idStockOpname) {
        errMsg = "";
        List<TblStockOpnameDetail> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStockOpnameDetailByIDStockOpname")
                        .setParameter("idStockOpname", idStockOpname)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list;
    }

    //--------------------------------------------------------------------------
    @Override
    public SysAccount getDataAccount(long id) {
        errMsg = "";
        SysAccount data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (SysAccount) session.find(SysAccount.class, id);
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
    public List<SysAccount> getAllDataAccount() {
        errMsg = "";
        List<SysAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllSysAccount").list();
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
    public List<TblLocation> getAllDataLocation() {
        errMsg = "";
        List<TblLocation> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocation").list();
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
    public RefLocationType getDataLocationType(int id) {
        errMsg = "";
        RefLocationType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefLocationType) session.find(RefLocationType.class, id);
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
    public List<RefLocationType> getAllDataLocationType() {
        errMsg = "";
        List<RefLocationType> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefLocationType").list();
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
    public List<TblLocationOfLaundry> getAllDataLaundry() {
        errMsg = "";
        List<TblLocationOfLaundry> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfLaundry").list();
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
    public TblRoom getDataRoomByIDLocation(long idLocation) {
        errMsg = "";
        List<TblRoom> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblRoomByIdLocation")
                        .setParameter("id", idLocation)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfWarehouse> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfWarehouseByIdLocation").
                        setParameter("id", idLocation)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblLocationOfLaundry getDataLaundryByIDLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfLaundry> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfLaundryByIdLocation")
                        .setParameter("id", idLocation)
                        .list();

                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblSupplier getDataSupplierByIDLocation(long idLocation) {
        errMsg = "";
        List<TblSupplier> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSupplierByIdLocation")
                        .setParameter("id", idLocation)
                        .list();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblLocationOfBin getDataBinByIDLocation(long idLocation) {
        errMsg = "";
        List<TblLocationOfBin> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblLocationOfBinByIdLocation")
                        .setParameter("id", idLocation)
                        .list();

                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
            }
        }
        return list.isEmpty() ? null : list.get(0);
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
    public TblPeople getDataPeople(long id) {
        errMsg = "";
        TblPeople data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPeople) session.find(TblPeople.class, id);
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
    public List<TblItemLocation> getAllDataItemLocationByIDLocation(long idLocation) {
        errMsg = "";
        List<TblItemLocation> list = new ArrayList();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationByIdLocation")
                        .setParameter("idLocation", idLocation)
                        .list();

                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMsg = e.getMessage();
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
    public List<TblItemLocationPropertyBarcode> getAllDataItemLocationPropertyBarcodeByIDItemLocation(long idItemLocation) {
        errMsg = "";
        List<TblItemLocationPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationPropertyBarcodeByIdItemLocation")
                        .setParameter("idItemLocation", idItemLocation)
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
    public List<TblStockOpnameDetailPropertyBarcode> getAllDataStockOpnameDetailPropertyBarcodeByIDStockOpnameDetail(long idStockOpnameDetail) {
        errMsg = "";
        List<TblStockOpnameDetailPropertyBarcode> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStockOpnameDetailPropertyBarcodeByIDStockOpnameDetail")
                        .setParameter("idStockOpnameDetail", idStockOpnameDetail)
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
    public List<TblItemLocationItemExpiredDate> getAllDataItemLocationItemExpiredDateByIDItemLocation(long idItemLocation) {
        errMsg = "";
        List<TblItemLocationItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblItemLocationItemExpiredDateByIDItemLocation")
                        .setParameter("idItemLocation", idItemLocation)
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
    public List<TblStockOpnameDetailItemExpiredDate> getAllDataStockOpnameDetailItemExpiredDateByIDStockOpnameDetail(long idStockOpnameDetail) {
        errMsg = "";
        List<TblStockOpnameDetailItemExpiredDate> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblStockOpnameDetailItemExpiredDateByIDStockOpnameDetail")
                        .setParameter("idStockOpnameDetail", idStockOpnameDetail)
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
