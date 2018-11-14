/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblCalendarEmployeeOvertime;
import hotelfx.persistence.model.TblEmployee;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Andreas
 */
public class FOvertimeManagerImpl implements FOvertimeManager {
  Session session;
  String errMessage;
  
  @Override
  public TblCalendarEmployeeOvertime insertDataOvertime(TblCalendarEmployeeOvertime overtime,Date date){
    errMessage = "";
    TblCalendarEmployeeOvertime tblOvertime = overtime;
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      List<SysCalendar>listSysCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date",date).list();
      if(!listSysCalendar.isEmpty()){
        for(SysCalendar getSysCalendar : listSysCalendar){
          overtime.setSysCalendar(getSysCalendar);
        }
      }
      else
      {
        overtime.getSysCalendar().setCalendarDate(date);
        overtime.getSysCalendar().setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        overtime.getSysCalendar().setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
        overtime.getSysCalendar().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        overtime.getSysCalendar().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
        overtime.getSysCalendar().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
        session.saveOrUpdate(overtime.getSysCalendar());
      }
      tblOvertime.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
      tblOvertime.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
      tblOvertime.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
      tblOvertime.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
      tblOvertime.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
      session.saveOrUpdate(tblOvertime);
      session.getTransaction().commit();
    }
    catch(Exception e){
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
      return null;
    }  
    }else{
        return null;
    }
    return tblOvertime;
  }
  
  @Override
  public boolean updateDataOvertime(TblCalendarEmployeeOvertime overtime,Date date){
    errMessage = "";
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      List<SysCalendar>listSysCalendar = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date",date).list();
      if(!listSysCalendar.isEmpty()){
        for(SysCalendar getSysCalendar : listSysCalendar){
          overtime.setSysCalendar(getSysCalendar);
        }
      }
      else
      {
        overtime.getSysCalendar().setCalendarDate(date);
        overtime.getSysCalendar().setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        overtime.getSysCalendar().setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
        overtime.getSysCalendar().setRefRecordStatus(session.find(RefRecordStatus.class, 1));
        session.saveOrUpdate(overtime.getSysCalendar());
      }
      overtime.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
      overtime.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
      overtime.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
      session.update(overtime);
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
      return false;
    }
    }else{
        return false;
    }
    return true;
  }
  
  @Override
  public boolean deleteDataOvertime(TblCalendarEmployeeOvertime overtime){
    errMessage = "";
    if (ClassSession.checkUserSession()) {  //check user current session
    try{
      session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       if (overtime.getRefRecordStatus().getIdstatus() == 1) {
           overtime.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
           overtime.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
           overtime.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
           session.update(overtime);
            } 
       else {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                errMessage = "Data tidak dapat dihapus!!";
       return false;
     }
       session.getTransaction().commit();
     }
     catch(Exception e)
     {
       if(session.getTransaction().isActive())
       {
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
       return false;
     }
    }else{
        return false;
    }
     return true;
    
  }
  
  @Override
  public List<TblCalendarEmployeeOvertime>getAllDataOvertime(){
    errMessage = "";
    List<TblCalendarEmployeeOvertime>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      list = session.getNamedQuery("findAllTblCalendarEmployeeOvertime").list();
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
  @Override
  public List<RefEmployeeType>getAllDataTypeEmployee(){
    errMessage = "";
    List<RefEmployeeType>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      list = session.getNamedQuery("findAllRefEmployeeType").list();
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
  @Override
  public List<TblEmployee>getAllDataEmployee(RefEmployeeType employeeType){
    errMessage = "";
    List<TblEmployee>list = new ArrayList();
    if (ClassSession.checkUserSession()) {  //check user current session
    try
    {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      if(employeeType.getIdtype()==3){
        list = session.getNamedQuery("findAllTblEmployee").list();
      }
      else{
       list = session.getNamedQuery("findAllTblEmployeeByTypeEmployee").setParameter("idType",employeeType.getIdtype()).list();
      }
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if(session.getTransaction().isActive()){
       session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return list;
  }
  
  @Override
  public TblCalendarEmployeeOvertime getDataOvertime(long id){
    errMessage = "";
    TblCalendarEmployeeOvertime data = null;
    if (ClassSession.checkUserSession()) {  //check user current session
    try{
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      data = (TblCalendarEmployeeOvertime)session.find(TblCalendarEmployeeOvertime.class,id);
      session.getTransaction().commit();
    }
    catch(Exception e){
      if(session.getTransaction().isActive()){
        session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
    }
    }
    return data;
  }
  
  @Override
    public String getErrorMessage() {
        return errMessage;
    }
}
