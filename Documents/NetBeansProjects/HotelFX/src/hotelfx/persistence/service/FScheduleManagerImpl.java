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
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblEmployeeSchedule;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Andreas
 */
public class FScheduleManagerImpl implements FScheduleManager{
   private String errMessage;
   private Session session;
   
   @Override
   public TblEmployeeSchedule insertDataSchedule(TblEmployeeSchedule schedule)
   {
     errMessage = "";
     TblEmployeeSchedule tblSchedule = schedule;
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       tblSchedule.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
       tblSchedule.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
       tblSchedule.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
       tblSchedule.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
       tblSchedule.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
       session.saveOrUpdate(tblSchedule);
       session.getTransaction().commit();
     }
     catch(Exception e)
     {
      if(session.getTransaction().isActive())
      {
        session.getTransaction().rollback();
      }
      errMessage = e.getMessage();
     }
     }else{
         return null;
     }
     return tblSchedule;
   }
   
   @Override
    public boolean updateDataSchedule(TblEmployeeSchedule schedule) {
        errMessage = "";
        if (ClassSession.checkUserSession()) {  //check user current session
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            schedule.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            schedule.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
            session.update(schedule);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
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
   public boolean deleteDataSchedule(TblEmployeeSchedule schedule)
   {
     errMessage = "";
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       if (schedule.getRefRecordStatus().getIdstatus() == 1) {
           schedule.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
           schedule.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
           schedule.setRefRecordStatus(session.find(RefRecordStatus.class, 2));
           session.update(schedule);
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
   public boolean insertDataEmployeeSchedule(List<TblCalendarEmployeeSchedule>dataEmployeeSchedule,long id)
   {
     errMessage = "";
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       
       Query q = session.getNamedQuery("deleteEmployeeSchedule").setParameter("idCalendar",id);
         errMessage = (String)q.uniqueResult();
         if(errMessage.equalsIgnoreCase("NOT DELETE") || errMessage.equalsIgnoreCase("DELETE")){
          for(TblCalendarEmployeeSchedule getDataEmployeeSchedule : dataEmployeeSchedule){
         getDataEmployeeSchedule.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
         getDataEmployeeSchedule.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         getDataEmployeeSchedule.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
         getDataEmployeeSchedule.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         getDataEmployeeSchedule.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
         session.save(getDataEmployeeSchedule);
         }
          
         /* Query q = session.getNamedQuery("deleteEmployeeSchedule").setParameter("idCalendar",getDataEmployeeSchedule.getSysCalendar().getIdcalendar())
                  .setParameter("idEmployee",getDataEmployeeSchedule.getTblEmployeeByIdemployee()).
                  setParameter("idSchedule",getDataEmployeeSchedule.getTblEmployeeSchedule().getIdschedule()).
                  setParameter("idUser",ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
         errMessage = (String)q.uniqueResult();
         
        /* getDataEmployeeSchedule.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
         getDataEmployeeSchedule.setTblEmployeeByCreateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         getDataEmployeeSchedule.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
         getDataEmployeeSchedule.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
         getDataEmployeeSchedule.setRefRecordStatus(session.find(RefRecordStatus.class, 1));
         session.save(getDataEmployeeSchedule);*/
         
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
   public List<TblEmployeeSchedule>getAllDataSchedule()
   {
     errMessage = "";
     List<TblEmployeeSchedule>list = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
      session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      list = session.getNamedQuery("findAllTblEmployeeSchedule").list();
      session.getTransaction().commit();
     }
     catch(Exception e)
     {
       if(session.getTransaction().isActive())
       {
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
     }
     }
     return list;
   }
   
   @Override
   public List<SysCalendar> getAllCalendar(Date startDate,Date endDate,String note){
     errMessage = "";
     List<SysCalendar>list = new ArrayList<>();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       //insert data calendar
       Query q = session.getNamedQuery("createCalendar").setParameter("dateStart",startDate)
                 .setParameter("dateEnd",endDate).setParameter("note",note).
                  setParameter("idEmployee",ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
       errMessage = (String)q.uniqueResult();
        if(!errMessage.equalsIgnoreCase(""))
        {
         if(session.getTransaction().isActive())
         {
           session.getTransaction().rollback();
         }
        }
        //get data calendar
        list = session.getNamedQuery("findAllSysCalendar").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
      session.getTransaction().commit();
     }
     catch(Exception e)
     {
       if(session.getTransaction().isActive())
       {
         session.getTransaction().rollback();
       }
     }
   }
     return list;
   }
   
   @Override
   public List<TblEmployee>getAllDataEmployee(long id){
     errMessage = "";
     List<TblEmployee>list = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
        if(id==3){
         list = session.getNamedQuery("findAllTblEmployee").list();
        }
        else{
         list = session.getNamedQuery("findAllTblEmployeeByTypeEmployee").setParameter("idType",id).list();
        }
       
       session.getTransaction().commit();
     }
     catch(Exception e){
       if(session.getTransaction().isActive())
       {
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
     }
     }
     return list;
   }
   
   @Override
   public List<TblCalendarEmployeeSchedule>getAllDataEmployeeSchedule(long id){
       errMessage = "";
     List<TblCalendarEmployeeSchedule>list = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByIdCalendar").setParameter("idCalendar",id).list();
       session.getTransaction().commit();
     }
     catch(Exception e){
       if(session.getTransaction().isActive())
       {
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
     }
     }
     return list; 
   }
   
   @Override
   public List<RefEmployeeType>getAllDataEmployeeType(){
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
     catch(Exception e){
       if(session.getTransaction().isActive())
       {
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
     }
     }
     return list; 
   }
   
   @Override
   public List<TblCalendarEmployeeSchedule>getAllEmployeeScheduleByTypeEmployeeAndCalendarOneMoreDay(Date startDate,Date endDate,RefEmployeeType employeeType){
       errMessage = "";
     List<TblCalendarEmployeeSchedule>list = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       if(employeeType.getIdtype()==3){
         list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleOneMoreDay").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
       }
       else{
           list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByEmployeeTypeOneMoreDay").setParameter("idType",employeeType.getIdtype()).setParameter("startDate",startDate).setParameter("endDate",endDate).list();
       }
        session.getTransaction().commit();
     }
     catch(Exception e){
       if(session.getTransaction().isActive())
       {
         session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
     }
     }
     return list; 
   }
   
   @Override
   public List<TblCalendarEmployeeSchedule>getAllEmployeeSchedulePrint(SysCalendar calendar,RefEmployeeType employeeType,TblEmployeeSchedule schedule){
     errMessage = "";
     List<TblCalendarEmployeeSchedule>list = new ArrayList();
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       if(employeeType.getIdtype()==3){
           if(schedule.getIdschedule()==0){
             list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByIdCalendar").setParameter("idCalendar",calendar.getIdcalendar()).list();
           }
           else{
             list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByEmployeeScheduleAndSysCalendar").setParameter("idSchedule",schedule.getIdschedule()).setParameter("idCalendar",calendar.getIdcalendar()).list();
           }
       }
       else if(employeeType.getIdtype()!=3){
           if(schedule.getIdschedule()==0){
             list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByTypeEmployeeAndSysCalendar")
                    .setParameter("idType",employeeType.getIdtype()).setParameter("idCalendar",calendar.getIdcalendar()).list();
           }
           else{
             list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByTypeEmployeeEmployeeScheduleAndSysCalendar").setParameter("idType",employeeType.getIdtype())
                    .setParameter("idSchedule",schedule.getIdschedule()).setParameter("idCalendar",calendar.getIdcalendar()).list();
           }
       }
       
       if(schedule.getIdschedule()==0){
           if(employeeType.getIdtype()==3){
             list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByIdCalendar").setParameter("idCalendar",calendar.getIdcalendar()).list();
            }
           else{
             list = list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByTypeEmployeeAndSysCalendar")
                    .setParameter("idType",employeeType.getIdtype()).setParameter("idCalendar",calendar.getIdcalendar()).list();
           }
       }
       else if(schedule.getIdschedule()!=0){
           if(employeeType.getIdtype()==3){
              list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByEmployeeScheduleAndSysCalendar")
                    .setParameter("idSchedule",schedule.getIdschedule()).setParameter("idCalendar",calendar.getIdcalendar()).list();
           }
           else{
               list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByTypeEmployeeEmployeeScheduleAndSysCalendar").setParameter("idType",employeeType.getIdtype())
                    .setParameter("idSchedule",schedule.getIdschedule()).setParameter("idCalendar",calendar.getIdcalendar()).list();
           }
       }
       
        session.getTransaction().commit();
     }
     catch(Exception e){
            if(session.getTransaction().isActive())
            {
              session.getTransaction().rollback();
            }
            errMessage = e.getMessage();
       }
     }
     return list; 
   }
   
   @Override
   public TblEmployeeSchedule getSchedule(long id)
   {
     errMessage = "";
     TblEmployeeSchedule data= null;
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       data = (TblEmployeeSchedule)session.find(TblEmployeeSchedule.class,id);
       session.getTransaction().commit();
     }
     catch(Exception e)
     {
       if(session.getTransaction().isActive())
       {
        session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
     }
     }
     return data;
   }
   
   @Override
   public TblCalendarEmployeeSchedule getEmployeeSchedule(long id){
     errMessage = "";
     TblCalendarEmployeeSchedule data= null;
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       data = (TblCalendarEmployeeSchedule)session.find(TblCalendarEmployeeSchedule.class,id);
       session.getTransaction().commit();
     }
     catch(Exception e)
     {
       if(session.getTransaction().isActive())
       {
        session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
     }
     }
     return data; 
   }
   
    @Override
   public TblEmployee getEmployee(long id)
   {
     errMessage = "";
     TblEmployee data= null;
     if (ClassSession.checkUserSession()) {  //check user current session
     try
     {
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       data = (TblEmployee)session.find(TblEmployee.class,id);
       session.getTransaction().commit();
     }
     catch(Exception e)
     {
       if(session.getTransaction().isActive())
       {
        session.getTransaction().rollback();
       }
       errMessage = e.getMessage();
     }
     }
     return data;
   }
   
   //---------------------------------------------------------------------------

    @Override
    public SysDataHardCode getDataSysDataHardCode(long id) {
        errMessage = "";
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
            errMessage = e.getMessage();
        } finally {
            //session.close();
        }
        }
        return data;
    }

    @Override
    public List<SysDataHardCode> getAllDataSysDataHardCode() {
        errMessage = "";
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
            errMessage = e.getMessage();
        } finally {
            //session.close();
        }
        }
        return list;
    }

    //--------------------------------------------------------------------------
   
   @Override
    public String getErrorMessage() {
        return errMessage;
    }
}
