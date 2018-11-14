/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassDataUserAccess;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblSystemFeature;
import hotelfx.persistence.model.TblSystemRole;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
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
public class FUserAccessManagerImpl implements FUserAccessManager {

    private Session session;

    private String errMsg;

    public FUserAccessManagerImpl() {

    }

    @Override
    public boolean updateDataRoleFeature(TblSystemRole role,
            List<TblSystemRoleSystemFeature> roleFeatures) {
        errMsg = "";
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();

            //delete all
            /*Query rs = session.getNamedQuery("deleteAllTblSystemRoleSystemFeatureByIdRole")
                 .setParameter("idRole", role.getIdrole())
                 .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                 errMsg = (String) rs.uniqueResult();
                 if (!errMsg.equals("")) {
                 if (session.getTransaction().isActive()) {
                 session.getTransaction().rollback();
                 }
                 return false;
                 }*/
                //save or update data
                for (TblSystemRoleSystemFeature rf : roleFeatures) {

                    rf.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rf.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    rf.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
                    rf.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    //rf.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
                    session.saveOrUpdate(rf);
                    Query q = session.getNamedQuery("updateDataRoleFeatureParent").setParameter("idRole", rf.getTblSystemRole().getIdrole())
                            .setParameter("idFeature", rf.getTblSystemFeature().getIdfeature()).
                            setParameter("idFeatureParent", rf.getTblSystemFeature().getTblSystemFeature().getIdfeature())
                            .setParameter("idEmployee", ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
                    errMsg = (String) q.uniqueResult();
                    if (!errMsg.equalsIgnoreCase("")) {
                        if (session.getTransaction().isActive()) {
                            session.getTransaction().rollback();
                        }
                    }
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
    public TblSystemRoleSystemFeature getRoleFeature(long idRoleFeature) {
        errMsg = "";
        TblSystemRoleSystemFeature data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSystemRoleSystemFeature) session.find(TblSystemRoleSystemFeature.class, idRoleFeature);
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
    public List<TblSystemRoleSystemFeature> getAllDataRoleFeature() {
        errMsg = "";
        List<TblSystemRoleSystemFeature> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemRoleSystemFeature")
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
    public List<ClassDataUserAccess> getAllDataRoleFeatureByIdRole(long idRole) {
        errMsg = "";
        //List<TblSystemRoleSystemFeature> list = new ArrayList<>();
        List<ClassDataUserAccess> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                List<TblSystemRoleSystemFeature> listRoleFeature = session.getNamedQuery("findAllTblSystemRoleSystemFeatureByIdRole")
                        .setParameter("idRole", idRole)
                        .list();
                for (TblSystemRoleSystemFeature getRoleFeature : listRoleFeature) {
                    ClassDataUserAccess userAccess = new ClassDataUserAccess();
                    userAccess.setSystemRoleFeature(getRoleFeature);
                    userAccess.setIsCreate(getRoleFeature.getCreateData());
                    userAccess.setIsUpdate(getRoleFeature.getUpdateData());
                    userAccess.setIsApprove(getRoleFeature.getApproveData());
                    userAccess.setIsDelete(getRoleFeature.getDeleteData());
                    userAccess.setIsPrint(getRoleFeature.getPrintData());
                    list.add(userAccess);
                }
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
    public TblSystemFeature getFeature(long id) {
        errMsg = "";
        TblSystemFeature data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSystemFeature) session.find(TblSystemFeature.class, id);
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
    public List<TblSystemFeature> getAllDataFeature() {
        errMsg = "";
        List<TblSystemFeature> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemFeture")
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
    public TblSystemRole getRole(long id) {
        errMsg = "";
        TblSystemRole data = null;
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                data = (TblSystemRole) session.find(TblSystemRole.class, id);
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
    public List<TblSystemRole> getAllDataRole() {
        errMsg = "";
        List<TblSystemRole> list = new ArrayList<>();
        if (ClassSession.checkUserSession()) {  //check user current session
            try {
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                list = session.getNamedQuery("findAllTblSystemRole")
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
