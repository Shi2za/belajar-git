/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassAdditionalType;
import hotelfx.helper.ClassDataDebtStatus;
import hotelfx.helper.ClassMemorandumInvoiceBonusType;
import hotelfx.helper.ClassReservationType;
import hotelfx.persistence.model.LogRoomTypeHistory;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefLostFoundStatus;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblFoundInformation;
import hotelfx.persistence.model.TblHotelExpenseTransactionDetail;
import hotelfx.persistence.model.TblHotelFinanceTransaction;
import hotelfx.persistence.model.TblHotelFinanceTransactionHotelPayable;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithCekGiro;
import hotelfx.persistence.model.TblHotelFinanceTransactionWithTransfer;
import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblItemMutationHistory;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblLocationOfLaundry;
import hotelfx.persistence.model.TblLocationOfWarehouse;
import hotelfx.persistence.model.TblLostFoundInformationDetail;
import hotelfx.persistence.model.TblLostInformation;
import hotelfx.persistence.model.TblMemorandumInvoice;
import hotelfx.persistence.model.TblMemorandumInvoiceDetail;
import hotelfx.persistence.model.TblMemorandumInvoiceDetailItemExpiredDate;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseOrderDetail;
import hotelfx.persistence.model.TblPurchaseRequest;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblReservationAdditionalService;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblReservationBrokenItem;
import hotelfx.persistence.model.TblReservationCheckInOut;
import hotelfx.persistence.model.TblReservationPayment;
import hotelfx.persistence.model.TblReservationRoomTypeDetail;
import hotelfx.persistence.model.TblReservationRoomTypeDetailRoomPriceDetail;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.model.TblReturDetail;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomService;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblStockOpname;
import hotelfx.persistence.model.TblStockOpnameDetail;
import hotelfx.persistence.model.TblStockOpnameDetailItemExpiredDate;
import hotelfx.persistence.model.TblStockOpnameDetailPropertyBarcode;
import hotelfx.persistence.model.TblSupplier;
import hotelfx.persistence.model.TblTravelAgent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Andreas
 */
public interface FReportManager {
  // REPORT RESERVASI
  public List<TblReservation>getAllDataReservation();
  //public List<TblReservation>getAllDataReservationByPeriode(Date startDate,Date endDate);
 public List<TblReservation>getAllDataReservationByPeriode(Date startDate,Date endDate,
                                                             RefReservationStatus reservationStatus,
                                                             TblTravelAgent travelAgent,
                                                             RefReservationOrderByType reservationType,
                                                             TblReservation reservation);
  public List<TblReservationRoomTypeDetail>getAllDataReservationRoomByIDReservation(long idReservation);
  //public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetail(Date startDate,Date endDate);
  public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailByIDReservation(long id);
 // public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailPeriode(Date startDate,Date endDate);
  public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailByDate(Date date);
  public List<TblReservationAdditionalItem>getAllDataReservationAdditionalItemDetailByIDReservation(long idReservation);
  public List<TblReservationAdditionalService>getAllDataReservationAdditionalServiceByIDReservation(long id);
  public List<TblReservationBill> getAllDataReservationBill(long id,int idType);
  public List<TblReservationPayment>getAllDataReservationPayment(long id,int idType);
  public List<TblReservationBrokenItem>getAllDataBrokenItem(long id);
  public List<RefReservationStatus>getAllDataReservationStatus();
  public List<TblTravelAgent>getAllDataTravelAgent();
  public List<TblRoomType>getAllDataRoomType();
  public List<TblRoomService>getAllDataRoomService();
  public List<TblItem>getAllDataAdditionalItemByGuestStatus(boolean guestStatus);
//  public List<TblReservationAdditionalService>getAllDataAdditionalServiceByDate(Date startDate);
  public List<TblReservationAdditionalService>getAllDataAdditionalServiceOtherByDate(Date date);
  public List<TblReservationAdditionalService>getAllDataAdditionalServiceBreakfastByDate(Date date);
  public List<TblReservationAdditionalItem>getAllDataAdditionalItemByDate(Date startDatete);
  public List<RefReservationOrderByType>getAllDataReservationType();
  
  public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailByMonth(String month);
  public List<TblReservationAdditionalService>getAllDataAdditionalServiceOtherByMonth(String month);
  public List<TblReservationAdditionalService>getAllDataAdditionalServiceBreakfastByMonth(String month);
  public List<TblReservationAdditionalItem>getAllDataAdditionalItemByMonth(String month); 
  
  public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailByYear(int year);
  public List<TblReservationAdditionalService>getAllDataAdditionalServiceOtherByYear(int year);
  public List<TblReservationAdditionalService>getAllDataAdditionalServiceBreakfastByYear(int year);
  public List<TblReservationAdditionalItem>getAllDataAdditionalItemByYear(int yeat); 
  
  public List<TblLocationOfWarehouse>getAllDataWarehouse();
  public List<TblItemMutationHistory>getAllDataItemMutationHistoryByIDWarehouse(TblLocation location,Date startDate,Date endDate);
  public BigDecimal getStock(TblLocation location,TblItem item,Date startDate);
 // public List<TblItemMutationHistory>getAllDataItemMutationHistoryByIDWarehouse(TblLocation location);
  
  public List<TblEmployee>getAllDataEmployee();
  public List<TblCalendarEmployeeDebt>getAllDataDebt(TblEmployee employee,Date startDate,Date endDate,ClassDataDebtStatus debtStatus);
    
  //LAPORAN PEMBELIAN BARANG
//  public List<TblPurchaseOrderDetail>getAllDataPurchaseOrderDetail(TblSupplier supplier,TblPurchaseOrder po,RefPurchaseOrderStatus poStatus,TblItem item,Date startDate,Date endDate);
  public List<TblPurchaseOrderDetail>getAllDataPurchaseOrderDetail(TblSupplier supplier,TblPurchaseOrder po,RefPurchaseOrderStatus poStatus,TblItem item,Date startDate,Date endDate,TblPurchaseRequest mr);
  public List<TblPurchaseOrder>getAllDataPurchaseOrder();
  public List<RefPurchaseOrderStatus>getAllPurchasingStatus();
  public List<TblItem>getAllDataItem();
  public List<TblSupplier>getAllDataSupplier();
  public List<TblPurchaseRequest>getAllTblPurchaseRequest();
  
  public List<TblReservationRoomTypeDetail>getAllDataRoomTypeDetailByPeriode(Date startDate,Date endDate);
  
  public List<RefLostFoundStatus>getAllLostFoundStatus();
  
  public List<TblLostInformation>getAllDataLostInformation(Date startDate,Date endDate,RefLostFoundStatus lostStatus);
  public List<TblFoundInformation>getAllDataFoundInformation(Date startDate,Date endDate,RefLostFoundStatus foundStatus);
  public List<TblLostFoundInformationDetail>getAllDataReturnInformation(TblLostInformation lostInformation,TblFoundInformation foundInformation);
  
  public List<TblReservationRoomTypeDetail>getAllReservationCheckOut(Date startDate,Date endDate);
  
  // LAPORAN REKAP KAMAR
  
//  public List<LogRoomTypeHistory>getAllRoomTypeHistory(Date date,TblRoomType roomType);
  public List<LogRoomTypeHistory>getAllRoomTypeHistory(TblRoomType roomType);
  public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllRoomTypeReservation(Date date,TblRoomType roomType);
  public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllRoomSold(Date date,TblRoomType roomType);
//  public List<TblReservationCheckInOut>getAllRoomUsed(Date date,TblRoomType roomType);
//  public List<TblLostFoundInformationDetail>getAllDataReturnInformation(TblLostInformation lostInformation);
  
  // LAPORAN PENERIMAAN BARANG
 // public List<TblMemorandumInvoiceDetail>getAllMemorandumInvoiceDetail(Date startDate,Date endDate);
  public List<TblMemorandumInvoiceDetail>getAllMemorandumInvoiceDetail(Date startDate,Date endDate,TblPurchaseOrder po,TblMemorandumInvoice mi,TblItem item,ClassMemorandumInvoiceBonusType bonusType);
  public List<TblMemorandumInvoiceDetailItemExpiredDate>getDataExpiredDate(long item);
  public List<TblMemorandumInvoice>getAllDataMemorandumInvoice();
   
 //LAPORAN RETUR BARANG
  public List<TblReturDetail>getAllReturDetail(Date startDate,Date endDate,TblSupplier supplier,TblItem item,TblPurchaseOrder po,TblRetur retur);
// public List<TblReturDetail>getAllReturDetail(Date startDate,Date endDate,TblSupplier supplier,TblItem item,TblPurchaseOrder po,TblRetur retur);
 public List<TblRetur>getAllDataRetur();
 // public List<TblReturDetail>getAllReturDetail(Date startDate,Date endDate);
 
  //LAPORAN PENERIMAAN PO
 // public List<TblPurchaseOrder>getAllDataPurchaseOrder(Date startDate,Date endDate);
  public List<TblPurchaseOrder>getAllDataPurchaseOrder(Date startDate,Date endDate,TblPurchaseOrder po,TblSupplier supplier);
  public List<TblPurchaseOrderDetail>getAllDataPODetail(TblPurchaseOrder po,TblItem item);
  public List<TblMemorandumInvoiceDetail>getAllDataMIDetail(TblPurchaseOrder po,TblItem item);
  public List<TblReturDetail>getAllDataReturDetail(TblItem item,TblPurchaseOrder po);
  
   //------------------------------REPORT STOKOPNAME----------------------
   public List<TblStockOpnameDetail>getAllDataStockOpnameForReport(Date startDate,Date endDate,TblStockOpname stockOpname,TblItem item,TblLocation location);
 // public List<TblStockOpnameDetail>getAllDataStockOpnameForReport(Date startDate,Date endDate);
  public List<TblStockOpnameDetailItemExpiredDate>getAllDataStockOpnameItemExp(TblItem item);
  public List<TblStockOpnameDetailPropertyBarcode>getAllDataStockOpnameProperty(TblItem item);
  public List<TblLocationOfWarehouse>getAllDataWareHouseByIdLocation(long id);
  public List<TblLocationOfLaundry>getAllDataLaundryByIdLocation(long id); 
  public List<TblStockOpname>getAllDataStockOpname();
  
  public List<TblLocationOfLaundry>getAllDataLaundry();
  public List<TblRoom>getAllDataRoomByIdLocation(long id);
  
  //--------------REPORT ITEM BROKEN--------------------------------------------
  public List<TblItemMutationHistory>getAllDataItemMutationHistoryForReport(Date startDate,Date endDate,TblItem item,TblLocation location);
  public List<TblRoom>getAllDataRoom();
  
  //-------------REPORT FINANCE TRANSACTION-------------------------------------
    public List<TblHotelFinanceTransactionHotelPayable>getAllFinanceTransaction(Date startDate,Date endDate,RefFinanceTransactionPaymentType financeType);
    public List<TblHotelExpenseTransactionDetail>getAllDataExpense(Date startDate,Date endDate,RefFinanceTransactionPaymentType financeType);
    public List<TblHotelFinanceTransactionWithTransfer>getAllDataTransfer(TblHotelFinanceTransaction financeTransaction);
    public List<TblHotelFinanceTransactionWithCekGiro>getAllDataCekGiro(TblHotelFinanceTransaction financeTransaction);
    public List<RefFinanceTransactionPaymentType>getAllDataPaymentType();
//  public List<TblItemMutationHistory>getAllDataItemMutationHistoryForReport(Date startDate,Date endDate);

}
