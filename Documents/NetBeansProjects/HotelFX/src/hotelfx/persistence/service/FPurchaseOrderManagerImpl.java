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
import hotelfx.persistence.model.RefHotelPayableType;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefMemorandumInvoiceStatus;
import hotelfx.persistence.model.RefPurchaseOrderItemArriveStatus;
import hotelfx.persistence.model.RefPurchaseOrderPaymentStatus;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.RefPurchaseRequestStatus;
import hotelfx.persistence.model.RefRecordStatus;
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
public class FPurchaseOrderManagerImpl implements FPurchaseOrderManager {

    private Session session;

    private String errMsg;

    public FPurchaseOrderManagerImpl() {

    }

    //--------------------------------------------------------------------------
    @Override
    public TblPurchaseOrder insertDataPurchaseOrder(TblPurchaseOrder purchaseOrder,
            List<TblPurchaseOrderDetail> purchaseOrderDetails) {
        errMsg = "";
        TblPurchaseOrder tblPurchaseOrder = purchaseOrder;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase order
                tblPurchaseOrder.setCodePo(ClassCoder.getCode("Purchase Order", session));
                tblPurchaseOrder.setPodate(Timestamp.valueOf(LocalDateTime.now()));
                tblPurchaseOrder.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPurchaseOrder.setTblEmployeeByCreatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                //check data needed to approve or not
                SysDataHardCode sdhMinPOTransactionForApproval = session.find(SysDataHardCode.class, (long) 26);   //ApprovalPO-MinTransactionNominal = '26'
                if (sdhMinPOTransactionForApproval != null
                        && sdhMinPOTransactionForApproval.getDataHardCodeValue() != null
                        && !sdhMinPOTransactionForApproval.getDataHardCodeValue().equals("")) {
                    BigDecimal minPOTransactionForApproval = new BigDecimal(sdhMinPOTransactionForApproval.getDataHardCodeValue());
                    if (calculationTotal(tblPurchaseOrder, purchaseOrderDetails)
                            .compareTo(minPOTransactionForApproval) != 1) {
                        tblPurchaseOrder.setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
                        tblPurchaseOrder.setTblEmployeeByApprovedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        tblPurchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 1)); //1 = 'Approved'
                    } else {
                        tblPurchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 0));  //0 = 'Created'
                    }
                } else {
                    tblPurchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 0));  //0 = 'Created'
                }
                tblPurchaseOrder.setRefPurchaseOrderItemArriveStatus(session.find(RefPurchaseOrderItemArriveStatus.class, 0));
                tblPurchaseOrder.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 0));
                tblPurchaseOrder.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPurchaseOrder.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPurchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPurchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPurchaseOrder.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblPurchaseOrder);
                //data purchase order detail
                for (TblPurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
                    purchaseOrderDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(purchaseOrderDetail);
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
        return tblPurchaseOrder;
    }

    private BigDecimal calculationTotalCost(TblPurchaseOrderDetail dataDetail) {
        return (dataDetail.getItemCost().subtract(dataDetail.getItemDiscount())).multiply(dataDetail.getItemQuantity());
    }

    private BigDecimal calculationSubTotal(List<TblPurchaseOrderDetail> dataDetails) {
        BigDecimal result = new BigDecimal("0");
        for (TblPurchaseOrderDetail dataDetail : dataDetails) {
            result = result.add(calculationTotalCost(dataDetail));
        }
        return result;
    }

    private BigDecimal calculationTotal(TblPurchaseOrder dataPO,
            List<TblPurchaseOrderDetail> dataDetails) {
        return (((new BigDecimal("1")).add(dataPO.getTaxPecentage())).multiply(calculationSubTotal(dataDetails).subtract(dataPO.getNominalDiscount())))
                .add(dataPO.getDeliveryCost());
    }

    @Override
    public boolean updateDataPurchaseOrder(TblPurchaseOrder purchaseOrder,
            List<TblPurchaseOrderDetail> purchaseOrderDetails) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase order
                purchaseOrder.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                //check data needed to approve or not
                SysDataHardCode sdhMinPOTransactionForApproval = session.find(SysDataHardCode.class, (long) 26);   //ApprovalPO-MinTransactionNominal = '26'
                if (sdhMinPOTransactionForApproval != null
                        && sdhMinPOTransactionForApproval.getDataHardCodeValue() != null
                        && !sdhMinPOTransactionForApproval.getDataHardCodeValue().equals("")) {
                    BigDecimal minPOTransactionForApproval = new BigDecimal(sdhMinPOTransactionForApproval.getDataHardCodeValue());
                    if (calculationTotal(purchaseOrder, purchaseOrderDetails)
                            .compareTo(minPOTransactionForApproval) != 1) {
                        purchaseOrder.setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
                        purchaseOrder.setTblEmployeeByApprovedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        purchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 1)); //1 = 'Approved'
                    } else {
//                        purchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 0));  //0 = 'Created'
                    }
                } else {
//                    purchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 0));  //0 = 'Created'
                }
                purchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrder);
                //data purchase order detail
                //delete all
                Query rs = session.getNamedQuery("deleteAllTblPurchaseOrderDetailByIDPurchaseOrder")
                        .setParameter("idPurchaseOrder", purchaseOrder.getIdpo())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data purchase order detail
                for (TblPurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
                    purchaseOrderDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(purchaseOrderDetail);
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
    public boolean updateDataPurchaseOrderRevision(
            TblPurchaseOrderRevisionHistory purchaseOrderRevisionHistory,
            List<TblPurchaseOrderDetail> purchaseOrderDetails) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase order
                if (purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().getIdpo() == 0L) {
//                    purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setCodePo(ClassCoder.getCode("Purchase Order", session));
                    purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                }
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 4));   //Revisi = '4'
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.saveOrUpdate(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew());
                //delete all (purchase order detail)
                Query rs = session.getNamedQuery("deleteAllTblPurchaseOrderDetailByIDPurchaseOrder")
                        .setParameter("idPurchaseOrder", purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().getIdpo())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data purchase order detail
                for (TblPurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
                    purchaseOrderDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(purchaseOrderDetail);
                }
                //data purchase order - revision history
                purchaseOrderRevisionHistory.setTblEmployeeByIdemployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseOrderRevisionHistory.setRevisionDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseOrderRevisionHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseOrderRevisionHistory.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(purchaseOrderRevisionHistory);
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
    public boolean updateApproveDataPurchaseOrder(TblPurchaseOrder purchaseOrder) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase order
                purchaseOrder.setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByApprovedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 1)); //1 = 'Approved'
                purchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrder);
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
    public boolean updateDataPurchaseOrderApproveRevision(
            TblPurchaseOrderRevisionHistory purchaseOrderRevisionHistory,
            List<TblPurchaseOrderDetail> purchaseOrderDetails) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase order (source)
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource().setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 3));   //Sudah Tidak Berlaku = '3'
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource());
                //data purchase order (new)
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 1));   //Disetujui = '1'
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew());
                //another data -> duplicated
                //1. Memorandum Invoice
                List<TblMemorandumInvoice> miSources = getAllMemorandumInvoiceByIDPurchaseOrder(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource().getIdpo());
                for (TblMemorandumInvoice miSource : miSources) {
                    TblMemorandumInvoice miNew = new TblMemorandumInvoice(miSource);
                    miNew.setIdmi(0L);
                    miNew.setCodeMi(ClassCoder.getCode("Receiving", session));
                    miNew.setTblPurchaseOrder(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew());
                    miNew.setRefMemorandumInvoiceStatus(new RefMemorandumInvoiceStatus(miNew.getRefMemorandumInvoiceStatus()));
                    session.saveOrUpdate(miNew);
                    List<TblMemorandumInvoiceDetail> midSources = getAllMemorandumInvoiceDetailByIDMemorandumInvoice(miSource.getIdmi());
                    for (TblMemorandumInvoiceDetail midSource : midSources) {
                        TblMemorandumInvoiceDetail midNew = new TblMemorandumInvoiceDetail(midSource);
                        midNew.setIddetail(0L);
                        midNew.setTblMemorandumInvoice(miNew);
                        session.saveOrUpdate(midNew);
                        //Retur
                        List<TblReturDetail> rdSources = getAllReturDetailByIDMemorandumInvoice(miSource.getIdmi());
                        for (TblReturDetail rdSource : rdSources) {
                            if (rdSource.getTblRetur().getRefReturStatus().getIdstatus() == 1) {    //Disetujui = '1'
                                //retur detail - (new)
                                TblReturDetail rdNew = new TblReturDetail(rdSource);
                                rdNew.setIddetailRetur(0L);
                                rdNew.setTblMemorandumInvoice(miNew);
                                session.saveOrUpdate(rdNew);
//                                //retur detail - (source)
//                                do something..
//                                session.update(rdSource);
                            }
                        }
                    }
                    miSource.setRefMemorandumInvoiceStatus(session.find(RefMemorandumInvoiceStatus.class, 3));  //Sudah Tidak Berlaku = '3'
                    session.update(miSource);
                }
                //2. Purchase Order - Hotel Payable
//                TblHotelPayablePurchaseOrder hppoSource = getHotelPayablePurchaseOrderByIDPurchaseOrder(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource().getIdpo());
//                if (hppoSource != null) {
//                    TblHotelPayable hpSource = session.find(TblHotelPayable.class, hppoSource.getTblHotelPayable().getIdhotelPayable());
//                    //hotel payable - (new)
//                    TblHotelPayable hpNew = new TblHotelPayable(hpSource);
//                    hpNew.setIdhotelPayable(0L);
//                    hpNew.setRefFinanceTransactionStatus(new RefFinanceTransactionStatus(hpNew.getRefFinanceTransactionStatus()));
//                    session.saveOrUpdate(hpNew);
//                    //hotel payable - (source)
//                    hpSource.setRefFinanceTransactionStatus(session.find(RefFinanceTransactionStatus.class, 3));  //Sudah Tidak Berlaku = '3'
//                    session.update(hpSource);
//                    //purchase order - hotel payable (new)
//                    TblHotelPayablePurchaseOrder hppoNew = new TblHotelPayablePurchaseOrder(hppoSource);
//                    hppoNew.setIdrelation(0L);
//                    hppoNew.setTblPurchaseOrder(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew());
//                    hppoNew.setTblHotelPayable(hpNew);
//                    session.saveOrUpdate(hppoNew);
//                    //hotel finnace transaction
//                    List<TblHotelFinanceTransaction> hftSources = getAllHotelFinanceTransactionByIDHotelPayable(hpSource.getIdhotelPayable());
//                    for (TblHotelFinanceTransaction hftSource : hftSources) {
//                        //hotel finnace transaction - (new)
//                        TblHotelFinanceTransaction hftNew = new TblHotelFinanceTransaction(hftSource);
//                        hftNew.setIdtransaction(0L);
//                        hftNew.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
//                        hftNew.setTblHotelPayable(hpNew);
//                        session.saveOrUpdate(hftNew);
//                    }
//                    //hotel invoice - hotel payable
//                    TblHotelInvoicePayable hipSource = getHotelInvoicePayableByIDHotelPayable(hpSource.getIdhotelPayable());
//                    if (hipSource != null) {
//                        TblHotelInvoice hiSouce = session.find(TblHotelInvoice.class, hipSource.getTblHotelInvoice().getIdhotelInvoice());
//                        //hotel invoice - (new)
//                        TblHotelInvoice hiNew = new TblHotelInvoice(hiSouce);
//                        hiNew.setIdhotelInvoice(0L);
//                        session.saveOrUpdate(hiNew);
//                        //hotel invoice - hotel payable (new)
//                        TblHotelInvoicePayable hipNew = new TblHotelInvoicePayable(hipSource);
//                        hipNew.setIdrelation(0L);
//                        hipNew.setTblHotelInvoice(hiNew);
//                        hipNew.setTblHotelPayable(hpNew);
//                        session.saveOrUpdate(hipNew);
//                    }
//                }
                //3. Retur...
                //save or update data purchase order detail
                for (TblPurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
                    purchaseOrderDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrderDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(purchaseOrderDetail);
                }
                //data purchase order - revision history              
                purchaseOrderRevisionHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrderRevisionHistory);
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

    private List<TblMemorandumInvoice> getAllMemorandumInvoiceByIDPurchaseOrder(long idPurchaseOrder) {
        List<TblMemorandumInvoice> list = session.getNamedQuery("findAllTblMemorandumInvoiceByIDPurchaseOrder")
                .setParameter("idPurchaseOrder", idPurchaseOrder)
                .list();
        return list;
    }

    private List<TblMemorandumInvoiceDetail> getAllMemorandumInvoiceDetailByIDMemorandumInvoice(long idMemorandumInvoice) {
        List<TblMemorandumInvoiceDetail> list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailByIDMemorandumInvoice")
                .setParameter("idMemorandumInvoice", idMemorandumInvoice)
                .list();
        return list;
    }

    private List<TblHotelFinanceTransaction> getAllHotelFinanceTransactionByIDHotelPayable(long idHotelPayable) {
        List<TblHotelFinanceTransaction> list = session.getNamedQuery("findAllTblHotelFinanceTransactionByIDHotelPayable")
                .setParameter("idHotelPayable", idHotelPayable)
                .list();
        return list;
    }

    private List<TblReturDetail> getAllReturDetailByIDMemorandumInvoice(long idMemorandumInvoice) {
        List<TblReturDetail> list = session.getNamedQuery("findAllTblReturDetailByIDMemorandumInvoice")
                .setParameter("idMemorandumInvoice", idMemorandumInvoice)
                .list();
        return list;
    }

    @Override
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
            List<TblHotelFinanceTransactionHotelPayable> hftHPSources,
            List<TblHotelFinanceTransactionHotelPayable> hftHPNews,
            List<TblHotelInvoice> hiSources,
            List<TblHotelInvoice> hiNews) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase order (source)
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource().setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 3));   //Sudah Tidak Berlaku = '3'
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdposource());
                //another data -> duplicated
                //1. Purchase Order - Hotel Payable
                //hotel invoice - (source)
                for (TblHotelInvoice hiSource : hiSources) {
                    session.update(hiSource);
                }
                //hotel invoice - (new)
                for (TblHotelInvoice hiNew : hiNews) {
                    session.saveOrUpdate(hiNew);
                }
                //hotel payable - (source)
                for (TblHotelPayable hpSource : hpSources) {
                    session.update(hpSource);
                }
                //hotel payable - (new)
                for (TblHotelPayable hpNew : hpNews) {
                    session.saveOrUpdate(hpNew);
                }
                //hotel finance transaction - hotel payable - (source)
                for (TblHotelFinanceTransactionHotelPayable hftHPSource : hftHPSources) {
                    session.update(hftHPSource);
                }
                //hotel finance transaction - hotel payable - (new)
                for (TblHotelFinanceTransactionHotelPayable hftHPNew : hftHPNews) {
//                    hftNew.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
                    session.saveOrUpdate(hftHPNew);
                }
                //data purchase order (new)
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 1));   //Disetujui = '1'
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrderRevisionHistory.getTblPurchaseOrderByIdponew());
                //2. Memorandum Invoice
                //memorandum invoice - (source)                
                for (TblMemorandumInvoice miSource : miSources) {
                    session.update(miSource);
                }
                //memorandum invoice - (new)                
                for (TblMemorandumInvoice miNew : miNews) {
//                    miNew.setCodeMi(ClassCoder.getCode("Receiving", session));
                    session.saveOrUpdate(miNew);
                }
                //memorandum invoice - detail (source)
                for (TblMemorandumInvoiceDetail miDetailSource : miDetailSources) {
                    session.update(miDetailSource);
                }
                //memorandum invoice - detail (new)                
                for (TblMemorandumInvoiceDetail miDetailNew : miDetailNews) {
                    session.saveOrUpdate(miDetailNew);
                }
                //retur - detail (source)                
                for (TblReturDetail returDetailSource : returDetailSources) {
                    session.update(returDetailSource);
                }
                //retur - detail (new)
                for (TblReturDetail returDetailNew : returDetailNews) {
                    session.saveOrUpdate(returDetailNew);
                }
                //save or update data purchase order detail
                for (TblPurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
                    purchaseOrderDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrderDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.update(purchaseOrderDetail);
                }
                //data purchase order - revision history              
                purchaseOrderRevisionHistory.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrderRevisionHistory.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrderRevisionHistory);
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
    public boolean updateOrderDataPurchaseOrder(TblPurchaseOrder purchaseOrder) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase order
                purchaseOrder.setOrderedDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByOrderedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 5)); //5 = 'Dipesan'
                purchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrder);
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
    public boolean updateClosingDataPurchaseOrder(
            TblPurchaseOrder purchaseOrder){
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase order
                purchaseOrder.setOrderedDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByOrderedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 6)); //6 = 'Selesai'
                purchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrder);
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
    public boolean deleteDataPurchaseOrder(TblPurchaseOrder purchaseOrder) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
//            if (purchaseOrder.getRefRecordStatus().getIdstatus() == 1) {
//                //data purchase order
//                purchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                purchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                purchaseOrder.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
//                session.update(purchaseOrder);
//                //data purchase order detail
//                Query rs = session.getNamedQuery("deleteAllTblPurchaseOrderDetailByIDPurchaseOrder")
//                        .setParameter("idPurchaseOrder", purchaseOrder.getIdpo())
//                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
//                errMsg = (String) rs.uniqueResult();
//                if (!errMsg.equals("")) {
//                    if (session.getTransaction().isActive()) {
//                        session.getTransaction().rollback();
//                    }
//                    return false;
//                }
//            } else {
//                if (session.getTransaction().isActive()) {
//                    session.getTransaction().rollback();
//                }
//                errMsg = "Data cant be delete!!";
//                return false;
//            }
                //data purchase order
                purchaseOrder.setCanceledDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByCanceledBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if(purchaseOrder.getRefPurchaseOrderStatus() != null
                        && purchaseOrder.getRefPurchaseOrderStatus().getIdstatus() == 4){   //Revisi = '4'
                    purchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 3)); //3 = 'Sudah Tidak Berlaku'
                }else{  //another
                    purchaseOrder.setRefPurchaseOrderStatus(session.find(RefPurchaseOrderStatus.class, 2)); //2 = 'Canceled'
                }
                purchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrder);
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
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByIDPurchaseRequest(long idPR) {
        errMsg = "";
        List<TblPurchaseOrder> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderByIDPurchaseRequest")
                        .setParameter("idPurchaseRequest", idPR)
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
    public TblPurchaseOrderDetail getDataPurchaseOrderDetail(long id) {
        errMsg = "";
        TblPurchaseOrderDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPurchaseOrderDetail) session.find(TblPurchaseOrderDetail.class, id);
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
    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetail() {
        errMsg = "";
        List<TblPurchaseOrderDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderDetail").list();
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
    public TblPurchaseRequest getDataPurchaseRequest(long id) {
        errMsg = "";
        TblPurchaseRequest data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPurchaseRequest) session.find(TblPurchaseRequest.class, id);
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
    public List<TblPurchaseRequest> getAllDataPurchaseRequest() {
        errMsg = "";
        List<TblPurchaseRequest> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseRequest").list();
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
    public RefPurchaseRequestStatus getDataPurchaseRequestStatus(int id) {
        errMsg = "";
        RefPurchaseRequestStatus data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefPurchaseRequestStatus) session.find(RefPurchaseRequestStatus.class, id);
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
    public List<RefPurchaseRequestStatus> getAllDataPurchaseRequestStatus() {
        errMsg = "";
        List<RefPurchaseRequestStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefPurchaseRequestStatus").list();
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
    public List<TblPurchaseRequestDetail> getAllDataPurchaseRequestDetailByIDPurchaseRequest(long idPurchaseRequest) {
        errMsg = "";
        List<TblPurchaseRequestDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseRequestDetailByIDPurchaseRequest")
                        .setParameter("idPurchaseRequest", idPurchaseRequest)
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

    @Override
    public List<RefPurchaseOrderPaymentStatus> getAllDataPurchaseOrderPaymentStatus() {
        errMsg = "";
        List<RefPurchaseOrderPaymentStatus> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllRefPurchaseOrderPaymentStatus").list();
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

    //--------------------------------------------------------------------------
    @Override
    public List<TblMemorandumInvoice> getAllDataMemorandumInvoiceByIDPurchaseOrder(long idPurchaseOrder) {
        errMsg = "";
        List<TblMemorandumInvoice> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceByIDPurchaseOrder")
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
    public RefHotelPayableType getDataHotelPayableType(int id) {
        errMsg = "";
        RefHotelPayableType data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (RefHotelPayableType) session.find(RefHotelPayableType.class, id);
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
            TblPurchaseOrder purchaseOrder) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
//                //data hotel payable (finance transaction status : insert or update)
//                if (tblHotelFinanceTransaction.getTblHotelPayable().getIdhotelPayable() == 0L) {
//                    tblHotelFinanceTransaction.getTblHotelPayable().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    tblHotelFinanceTransaction.getTblHotelPayable().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    tblHotelFinanceTransaction.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    tblHotelFinanceTransaction.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    tblHotelFinanceTransaction.getTblHotelPayable().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                    session.saveOrUpdate(tblHotelFinanceTransaction.getTblHotelPayable());
//                } else {
//                    //@@@%%%
//                    tblHotelFinanceTransaction.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                    tblHotelFinanceTransaction.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                    session.update(tblHotelFinanceTransaction.getTblHotelPayable());
//                }
//                //data purchase order
//                purchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                purchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(purchaseOrder);
//                //data hotel finnace transaction
//                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
//                tblHotelFinanceTransaction.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelFinanceTransaction.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelFinanceTransaction.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                tblHotelFinanceTransaction.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                tblHotelFinanceTransaction.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
//                session.saveOrUpdate(tblHotelFinanceTransaction);
//                //@@@%%%
//                //data company balance (kas besar) : minus (updated)
//                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);
//                dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
//                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                session.update(dataBalance);
//                //data company balance (kas besar) : nominal bank account : minus (updated)
//                TblCompanyBalanceBankAccount companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount((long) 1, hotelFinanceTransaction.getTblBankAccountBySenderBankAccount().getIdbankAccount());    //hotel balance = '1'
//                companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
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
            List<TblHotelFinanceTransactionHotelPayable> hfthps,
            String supplierName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
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
                //data hotel finnace transaction - hotel payable
                for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
                    hfthp.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthp);
                    //data hotel payable
                    hfthp.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthp.getTblHotelPayable());
                    //data purchase order
                    TblPurchaseOrder po = getPurchaseOrderByIDHotelPayable(hfthp.getTblHotelPayable().getIdhotelPayable());
                    if (po != null) {
                        if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 1) { //Dibayar Sebagian = '1'
                            po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
                        } else {
                            if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 2) { //Sudah Dibayar = '2'
                                po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 2));   //Sudah Dibayar = '2'
                            } else {
                                if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 5) { //Kelebihan Bayar = '5'
                                    po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 3));   //Kelebihan Bayar = '3'
                                } else {
                                    po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 0));   //Belum Dibayar = '0'
                                }
                            }
                        }
                        po.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        po.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(po);
                    }
                }
                //@@@%%%
                //data company balance (kas) : minus/plus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, hotelFinanceTransactionWithCash.getTblCompanyBalance().getIdbalance());
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                } else {  //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(null);
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(null);
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran utang oleh " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran utang kepada " + supplierName + ", \nsebesar : "
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
            List<TblHotelFinanceTransactionHotelPayable> hfthps,
            String supplierName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
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
                //data hotel finnace transaction - hotel payable
                for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
                    hfthp.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthp);
                    //data hotel payable
                    hfthp.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthp.getTblHotelPayable());
                    //data purchase order
                    TblPurchaseOrder po = getPurchaseOrderByIDHotelPayable(hfthp.getTblHotelPayable().getIdhotelPayable());
                    if (po != null) {
                        if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 1) { //Dibayar Sebagian = '1'
                            po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
                        } else {
                            if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 2) { //Sudah Dibayar = '2'
                                po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 2));   //Sudah Dibayar = '2'
                            } else {
                                if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 5) { //Kelebihan Bayar = '5'
                                    po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 3));   //Kelebihan Bayar = '3'
                                } else {
                                    po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 0));   //Belum Dibayar = '0'
                                }
                            }
                        }
                        po.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        po.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(po);
                    }
                }
                //@@@%%%
                //data company balance (kas besar) : minus/plus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);    //Kas Besar = '1'
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (kas besar) : nominal bank account : minus/plus (updated)
                TblCompanyBalanceBankAccount companyBalanceBankAccount;
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                } else {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount().getIdbankAccount());
                }
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(hotelFinanceTransactionWithTransfer.getTblBankAccountBySenderBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                } else {  //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(hotelFinanceTransactionWithTransfer.getTblBankAccountByReceiverBankAccount());
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran utang oleh " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran utang kepada " + supplierName + ", \nsebesar : "
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
            List<TblHotelFinanceTransactionHotelPayable> hfthps,
            String supplierName) {
        errMsg = "";
        TblHotelFinanceTransaction tblHotelFinanceTransaction = hotelFinanceTransaction;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel finnace transaction
                tblHotelFinanceTransaction.setCodeTransaction(ClassCoder.getCode("Payable - Payment", session));
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
                //data hotel finnace transaction - hotel payable
                for (TblHotelFinanceTransactionHotelPayable hfthp : hfthps) {
                    hfthp.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    hfthp.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(hfthp);
                    //data hotel payable
                    hfthp.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    hfthp.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(hfthp.getTblHotelPayable());
                    //data purchase order
                    TblPurchaseOrder po = getPurchaseOrderByIDHotelPayable(hfthp.getTblHotelPayable().getIdhotelPayable());
                    if (po != null) {
                        if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 1) { //Dibayar Sebagian = '1'
                            po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
                        } else {
                            if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 2) { //Sudah Dibayar = '2'
                                po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 2));   //Sudah Dibayar = '2'
                            } else {
                                if (hfthp.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 5) { //Kelebihan Bayar = '5'
                                    po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 3));   //Kelebihan Bayar = '3'
                                } else {
                                    po.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 0));   //Belum Dibayar = '0'
                                }
                            }
                        }
                        po.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                        po.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                        session.saveOrUpdate(po);
                    }
                }
                //@@@%%%
                //data company balance (kas besar) : minus/plus (updated)
                TblCompanyBalance dataBalance = session.find(TblCompanyBalance.class, (long) 1);    //Kas Besar = '1'
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    dataBalance.setBalanceNominal(dataBalance.getBalanceNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                dataBalance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                dataBalance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(dataBalance);
                //data company balance (kas besar) : nominal bank account : minus/plus (updated)
                TblCompanyBalanceBankAccount companyBalanceBankAccount;
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount().getIdbankAccount());
                } else {
                    companyBalanceBankAccount = getCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
                            (long) 1, //Kas Besar = '1'
                            hotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount().getIdbankAccount());
                }
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //plus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().add(hotelFinanceTransaction.getTransactionNominal()));
                } else {  //minus
                    companyBalanceBankAccount.setCompanyBalanceBankAccountNominal(companyBalanceBankAccount.getCompanyBalanceBankAccountNominal().subtract(hotelFinanceTransaction.getTransactionNominal()));
                }
                companyBalanceBankAccount.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                companyBalanceBankAccount.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(companyBalanceBankAccount);
                //data company balance (cash-flow log)
                LogCompanyBalanceCashFlow logCompanyBalanceCashFlow = new LogCompanyBalanceCashFlow();
                if (hotelFinanceTransaction.getIsReturnTransaction()) {   //in
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(hotelFinanceTransactionWithCekGiro.getTblBankAccountBySenderBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                } else {  //out
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdsenderCompanyBalance(dataBalance);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdsenderCbbankAccount(companyBalanceBankAccount.getTblBankAccount());
                    logCompanyBalanceCashFlow.setTblCompanyBalanceByIdreceiverCompanyBalance(null);
                    logCompanyBalanceCashFlow.setTblBankAccountByIdreceiverCbbankAccount(hotelFinanceTransactionWithCekGiro.getTblBankAccountByReceiverBankAccount());
                }
                logCompanyBalanceCashFlow.setTransferNominal(hotelFinanceTransaction.getTransactionNominal());
                logCompanyBalanceCashFlow.setHistoryDate(Timestamp.valueOf(LocalDateTime.now()));
                logCompanyBalanceCashFlow.setTblEmployee(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                if (hotelFinanceTransaction.getIsReturnTransaction()) {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pengembalian pembayaran utang oleh " + supplierName + ", \nsebesar : "
                            + ClassFormatter.currencyFormat.format(hotelFinanceTransaction.getTransactionNominal()) + " ("
                            + tblHotelFinanceTransaction.getRefFinanceTransactionPaymentType().getTypeName() + ")");
                } else {
                    logCompanyBalanceCashFlow.setHistoryStystemNote("Pembayaran utang kepada " + supplierName + ", \nsebesar : "
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

    private TblPurchaseOrder getPurchaseOrderByIDHotelPayable(long idHotelPayable) {
        List<TblPurchaseOrder> list = session.getNamedQuery("findAllTblPurchaseOrderByIDHotelPayable")
                .setParameter("idHotelPayable", idHotelPayable)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

//    @Override
//    public List<TblHotelFinanceTransaction> getAllDataHotelFinanceTransactionByIDHotelPayable(long idHotelPayable) {
//        errMsg = "";
//        List<TblHotelFinanceTransaction> list = new ArrayList<>();
//        if (ClassSession.checkUserSession()) {  //check user current session
//            try {
//                session = HibernateUtil.getSessionFactory().getCurrentSession();
//                session.beginTransaction();
//                list = session.getNamedQuery("findAllTblHotelFinanceTransactionByIDHotelPayable")
//                        .setParameter("idHotelPayable", idHotelPayable)
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
        errMsg = "";
        TblSupplierBankAccount tblSupplierBankAccount = supplierBankAccount;
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
    @Override
    public TblHotelInvoice insertDataHotelInvoice(TblHotelInvoice hotelInvoice,
            TblPurchaseOrder purchaseOrder) {
        errMsg = "";
        TblHotelInvoice tblHotelInvoice = hotelInvoice;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel invoice
                tblHotelInvoice.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelInvoice.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelInvoice.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblHotelInvoice.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblHotelInvoice.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblHotelInvoice);
                //data hotel payable (insert)
                if (purchaseOrder.getTblHotelPayable().getIdhotelPayable() == 0L) {
                    purchaseOrder.getTblHotelPayable().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrder.getTblHotelPayable().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrder.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrder.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseOrder.getTblHotelPayable().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(purchaseOrder.getTblHotelPayable());
                } else {
                    purchaseOrder.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseOrder.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(purchaseOrder.getTblHotelPayable());
                }
                //data purchase order (update)
                purchaseOrder.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseOrder.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseOrder);
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
        return tblHotelInvoice;
    }

    @Override
    public TblHotelInvoice insertDataHotelInvoice(TblPurchaseOrder dataPO) {
        errMsg = "";
        TblPurchaseOrder tblPO = dataPO;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data hotel invoice
                if (tblPO.getTblHotelPayable().getTblHotelInvoice().getIdhotelInvoice() == 0L) {
                    tblPO.getTblHotelPayable().getTblHotelInvoice().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblPO.getTblHotelPayable().getTblHotelInvoice().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblPO.getTblHotelPayable().getTblHotelInvoice().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblPO.getTblHotelPayable().getTblHotelInvoice().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblPO.getTblHotelPayable().getTblHotelInvoice().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(tblPO.getTblHotelPayable().getTblHotelInvoice());
                } else {
                    tblPO.getTblHotelPayable().getTblHotelInvoice().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblPO.getTblHotelPayable().getTblHotelInvoice().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(tblPO.getTblHotelPayable().getTblHotelInvoice());
                }
                //data hotel payable (insert)
                if (tblPO.getTblHotelPayable().getIdhotelPayable() == 0L) {
                    tblPO.getTblHotelPayable().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblPO.getTblHotelPayable().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblPO.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblPO.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    tblPO.getTblHotelPayable().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(tblPO.getTblHotelPayable());
                } else {
                    tblPO.getTblHotelPayable().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    tblPO.getTblHotelPayable().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(tblPO.getTblHotelPayable());
                }
                //data purchase order (update)
                if (tblPO.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 1) { //Dibayar Sebagian = '1'
                    tblPO.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 1));   //Dibayar Sebagian = '1'
                } else {
                    if (tblPO.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 2) { //Sudah Dibayar = '2'
                        tblPO.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 2));   //Sudah Dibayar = '2'
                    } else {
                        if (tblPO.getTblHotelPayable().getRefFinanceTransactionStatus().getIdstatus() == 5) { //Kelebihan Bayar = '5'
                            tblPO.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 3));   //Kelebihan Bayar = '3'
                        } else {
                            tblPO.setRefPurchaseOrderPaymentStatus(session.find(RefPurchaseOrderPaymentStatus.class, 0));   //Belum Dibayar = '0'
                        }
                    }
                }
                tblPO.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPO.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(tblPO);
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
        return tblPO.getTblHotelPayable().getTblHotelInvoice();
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

    //--------------------------------------------------------------------------
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

    //--------------------------------------------------------------------------
    @Override
    public TblPurchaseOrderRevisionHistory getDataPurchaseOrderRevisionHistory(long id) {
        errMsg = "";
        TblPurchaseOrderRevisionHistory data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPurchaseOrderRevisionHistory) session.find(TblPurchaseOrderRevisionHistory.class, id);
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
    public List<TblPurchaseOrderRevisionHistory> getAllDataPurchaseOrderRevisionHistory() {
        errMsg = "";
        List<TblPurchaseOrderRevisionHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderRevisionHistory").list();
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
    public List<TblPurchaseOrderRevisionHistory> getAllDataPurchaseOrderRevisionHistoryByIDPOSource(long idPOSource) {
        errMsg = "";
        List<TblPurchaseOrderRevisionHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderRevisionHistoryByIDPurchaseOrderSource")
                        .setParameter("idPOSource", idPOSource)
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
    public List<TblPurchaseOrderRevisionHistory> getAllDataPurchaseOrderRevisionHistoryByIDPONew(long idPONew) {
        errMsg = "";
        List<TblPurchaseOrderRevisionHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderRevisionHistoryByIDPurchaseOrderNew")
                        .setParameter("idPONew", idPONew)
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
    public TblPurchaseOrderRevisionHistory getDataPurchaseOrderRevisionHistoryByIDPONew(long idPONew) {
        errMsg = "";
        List<TblPurchaseOrderRevisionHistory> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderRevisionHistoryByIDPurchaseOrderNew")
                        .setParameter("idPONew", idPONew)
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

    @Override
    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetailByIDPurchaseRequestAndIDItem(
            long idPR, 
            long idItem){
        errMsg = "";
        List<TblPurchaseOrderDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderDetailByIDPurchaseRequestAndIDItem")
                        .setParameter("idPurchaseRequest", idPR)
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
    public List<TblReturDetail> getAllDataReturDetailByIDMemorandumInvoice(long idMI) {
        errMsg = "";
        List<TblReturDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblReturDetailByIDMemorandumInvoice")
                        .setParameter("idMemorandumInvoice", idMI)
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
    public List<TblMemorandumInvoiceDetailPropertyBarcode> getAllDataMemorandumInvoiceDetailPropertyBarcodeByIDMemorandumInvoiceDetail(long idMIDetail){
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
    public List<TblMemorandumInvoiceDetailItemExpiredDate> getAllDataMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail(long idMIDetail){
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
    public List<TblReturDetailPropertyBarcode> getAllDataReturDetailPropertyBarcodeByIDReturDetail(long idReturDetail){
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
    
    @Override
    public List<TblReturDetailItemExpiredDate> getAllDataReturDetailItemExpiredDateByIDReturDetail(long idReturDetail){
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
    public TblLocationOfWarehouse getDataWarehouseByIDLocation(long idLocation){
        errMsg = "";
        List<TblLocationOfWarehouse> list = new ArrayList();
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
    public List<TblHotelFinanceTransactionHotelPayable> getAllDataHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction(long idHotelFinanceTransaction) {
        errMsg = "";
        List<TblHotelFinanceTransactionHotelPayable> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionHotelPayableByIDHotelFinanceTransaction")
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
    public List<TblHotelFinanceTransactionHotelPayable> getAllDataHotelFinanceTransactionHotelPayableByIDHotelPayable(long idHotelPayable) {
        errMsg = "";
        List<TblHotelFinanceTransactionHotelPayable> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionHotelPayableByIDHotelPayable")
                        .setParameter("idHotelPayable", idHotelPayable)
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
    public TblPurchaseOrder getDataPurchaseOrderByIDHotelPayable(long idHotelPayable) {
        errMsg = "";
        List<TblPurchaseOrder> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderByIDHotelPayable")
                        .setParameter("idHotelPayable", idHotelPayable)
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
    public TblCompanyBalanceBankAccount getDataCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount(
            long idCompanyBalance,
            long idBankAccount) {
        errMsg = "";
        List<TblCompanyBalanceBankAccount> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblCompanyBalanceBankAccountByIDCompanyBalanceAndIDBankAccount")
                        .setParameter("idCompanyBalance", idCompanyBalance)
                        .setParameter("idBankAccount", idBankAccount)
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
    public TblHotelFinanceTransactionWithTransfer getDataHotelFinanceTransactionWithTransferByIDHotelFinanceTransaction(long idHotelFinanceTransaction) {
        errMsg = "";
        List<TblHotelFinanceTransactionWithTransfer> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionWithTransferByIDHotelFinanceTransactio")
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
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public TblHotelFinanceTransactionWithCekGiro getDataHotelFinanceTransactionWithCekGiroByIDHotelFinanceTransaction(long idHotelFinanceTransaction) {
        errMsg = "";
        List<TblHotelFinanceTransactionWithCekGiro> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblHotelFinanceTransactionWithCekGiroByIDHotelFinanceTransaction")
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
        return list.isEmpty() ? null : list.get(0);
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
    public TblItemTypeHk getDataItemTypeHK(long id){
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
    public List<TblItemTypeHk> getAllDataItemTypeHK(){
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
    public TblItemTypeWh getDataItemTypeWH(long id){
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
    public List<TblItemTypeWh> getAllDataItemTypeWH(){
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
