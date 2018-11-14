/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefItemGuestType;
import hotelfx.persistence.model.RefItemType;
import hotelfx.persistence.model.RefPurchaseRequestStatus;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblEmployee;
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
public class FPurchaseRequestManagerImpl implements FPurchaseRequestManager {

    private Session session;

    private String errMsg;

    public FPurchaseRequestManagerImpl() {

    }

    //--------------------------------------------------------------------------
    @Override
    public TblPurchaseRequest insertDataPurchaseRequest(TblPurchaseRequest purchaseRequest,
            List<TblPurchaseRequestDetail> purchaseRequestDetails) {
        errMsg = "";
        TblPurchaseRequest tblPurchaseRequest = purchaseRequest;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase request
                tblPurchaseRequest.setCodePr(ClassCoder.getCode("Purchase Request", session));
                tblPurchaseRequest.setPrdate(Timestamp.valueOf(LocalDateTime.now()));
                tblPurchaseRequest.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPurchaseRequest.setTblEmployeeByCreatedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPurchaseRequest.setRefPurchaseRequestStatus(session.find(RefPurchaseRequestStatus.class, 0));    //0 = 'Created'
                tblPurchaseRequest.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPurchaseRequest.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPurchaseRequest.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                tblPurchaseRequest.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                tblPurchaseRequest.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                session.saveOrUpdate(tblPurchaseRequest);
                //data purchase request detail
                for (TblPurchaseRequestDetail purchaseRequestDetail : purchaseRequestDetails) {
                    purchaseRequestDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseRequestDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseRequestDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseRequestDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseRequestDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(purchaseRequestDetail);
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
        return tblPurchaseRequest;
    }

    @Override
    public boolean updateDataPurchaseRequest(TblPurchaseRequest purchaseRequest,
            List<TblPurchaseRequestDetail> purchaseRequestDetails) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase request
                purchaseRequest.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseRequest.setRefPurchaseRequestStatus(session.find(RefPurchaseRequestStatus.class, 0)); //need approve again, 0 = 'Created'
                purchaseRequest.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseRequest.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseRequest);
                //data purchase request detail
                //delete all
                Query rs = session.getNamedQuery("deleteAllTblPurchaseRequestDetailByIDPurchaseRequest")
                        .setParameter("idPurchaseRequest", purchaseRequest.getIdpr())
                        .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                errMsg = (String) rs.uniqueResult();
                if (!errMsg.equals("")) {
                    if (session.getTransaction().isActive()) {
                        session.getTransaction().rollback();
                    }
                    return false;
                }
                //save or update data purchase request detail
                for (TblPurchaseRequestDetail purchaseRequestDetail : purchaseRequestDetails) {
                    purchaseRequestDetail.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseRequestDetail.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseRequestDetail.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    purchaseRequestDetail.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    purchaseRequestDetail.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
                    session.saveOrUpdate(purchaseRequestDetail);
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
    public boolean updateApproveDataPurchaseRequest(TblPurchaseRequest purchaseRequest) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase request
                purchaseRequest.setApprovedDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseRequest.setTblEmployeeByApprovedBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseRequest.setRefPurchaseRequestStatus(session.find(RefPurchaseRequestStatus.class, 1)); //1 = 'Approved'
                purchaseRequest.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseRequest.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseRequest);
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
    public boolean updateClosingDataPurchaseRequest(
            TblPurchaseRequest purchaseRequest) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //data purchase request
                purchaseRequest.setClosingDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseRequest.setTblEmployeeByClosingBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseRequest.setRefPurchaseRequestStatus(session.find(RefPurchaseRequestStatus.class, 4)); //4 = 'Selesai'
                purchaseRequest.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseRequest.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseRequest);
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

    ;
    
    @Override
    public boolean deleteDataPurchaseRequest(TblPurchaseRequest purchaseRequest) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
//            if (purchaseRequest.getRefRecordStatus().getIdstatus() == 1) {
//                //data purchase request
//                purchaseRequest.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
//                purchaseRequest.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
//                purchaseRequest.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
//                session.update(purchaseRequest);
//                //data purchase request detail
//                Query rs = session.getNamedQuery("deleteAllTblPurchaseRequestDetailByIDPurchaseRequest")
//                        .setParameter("idPurchaseRequest", purchaseRequest.getIdpr())
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
                //data purchase request
                purchaseRequest.setCanceledDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseRequest.setTblEmployeeByCanceledBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                purchaseRequest.setRefPurchaseRequestStatus(session.find(RefPurchaseRequestStatus.class, 2)); //2 = 'Canceled'
                purchaseRequest.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                purchaseRequest.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                session.update(purchaseRequest);
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

    //--------------------------------------------------------------------------
    @Override
    public TblPurchaseRequestDetail getDataPurchaseRequestDetail(long id) {
        errMsg = "";
        TblPurchaseRequestDetail data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblPurchaseRequestDetail) session.find(TblPurchaseRequestDetail.class, id);
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
    public List<TblPurchaseRequestDetail> getAllDataPurchaseRequestDetail() {
        errMsg = "";
        List<TblPurchaseRequestDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseRequestDetail").list();
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

    //--------------------------------------------------------------------------
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
    public List<TblPurchaseOrder> getAllDataPurchaseOrderByIDPurchaseRequest(long idPurchaseRequest) {
        errMsg = "";
        List<TblPurchaseOrder> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblPurchaseOrderByIDPurchaseRequest")
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
    public List<TblPurchaseOrderDetail> getAllDataPurchaseOrderDetailByIDPurchaseRequestAndIDItem(
            long idPR,
            long idItem) {
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
    public List<TblMemorandumInvoiceDetail> getAllDataMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem(
            long idPO,
            long idSupplierItem) {
        errMsg = "";
        List<TblMemorandumInvoiceDetail> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailByIDPurchaseOrderAndIDSupplierItem")
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
    public List<TblReturDetail> getAllDataReturDetailByIDMemorandumInvoiceAndIDSupplierItem(
            long idMI,
            long idSupplierItem) {
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
