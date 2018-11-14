/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

//import hotelfx.helper.ClassDataSchedule;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.RefEmployeeAttendanceEndStatus;
import hotelfx.persistence.model.RefEmployeeAttendanceStartStatus;
import hotelfx.persistence.model.RefEmployeeAttendanceStatus;
import hotelfx.persistence.model.RefEmployeeType;
import hotelfx.persistence.model.RefRecordStatus;
import hotelfx.persistence.model.SysCalendar;
import hotelfx.persistence.model.TblCalendarEmployeeAttendance;
import hotelfx.persistence.model.TblCalendarEmployeeLeave;
import hotelfx.persistence.model.TblCalendarEmployeeOvertime;
import hotelfx.persistence.model.TblCalendarEmployeeSchedule;
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
public class FAttendanceManagerImpl implements FAttendanceManager{
    private Session session ;
    private String errMessage;
    
    @Override
    public boolean updateCodeFinger(TblEmployee employee){
      errMessage = "";
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        //customer.getTblPeople().setImageUrl(customer.getTblPeople().getIdpeople() + "." + imgExtention);    
        employee.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        employee.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class, ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
        session.update(employee);
        session.getTransaction().commit();
      }
      catch(Exception e){
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
    public boolean insertDataAttendance(List<TblCalendarEmployeeAttendance>listAttendance){
      errMessage = "";
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        for(TblCalendarEmployeeAttendance getAttendance : listAttendance){
            List<TblCalendarEmployeeAttendance>listEmployeeAttendance = session.getNamedQuery("findAllTblCalendarEmployeeAttendanceForDataExists")
                                                                        .setParameter("idCalendar",getAttendance.getSysCalendar().getIdcalendar()).setParameter("idEmployee",getAttendance.getTblEmployeeByIdemployee().getIdemployee())
                                                                        .setParameter("codeFinger",getAttendance.getCodeFingerPrint()).setParameter("isOvertime",getAttendance.getIsOvertime()).setParameter("note",getAttendance.getNote())
                                                                        .setParameter("idStatus",getAttendance.getRefEmployeeAttendanceStatus().getIdstatus()).list();
            boolean found = false;
            for(TblCalendarEmployeeAttendance getEmployeeAttendance : listEmployeeAttendance){
              if(getEmployeeAttendance.getBeginSchedule().equals(getAttendance.getBeginSchedule()) && 
                 getEmployeeAttendance.getEndSchedule().equals(getAttendance.getEndSchedule()) && 
                 getEmployeeAttendance.getBeginReal().equals(getAttendance.getBeginReal()) && 
                 getEmployeeAttendance.getEndReal().equals(getAttendance.getEndReal())){
                 found = true;
              }
            }
            
            if(!found){
              getAttendance.setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              getAttendance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
              getAttendance.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
              getAttendance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
              getAttendance.setRefRecordStatus(session.find(RefRecordStatus.class,1));
              session.saveOrUpdate(getAttendance);
            }
             
         /*Query q = session.getNamedQuery("createDataAttendance").setParameter("beginSchedule",getAttendance.getBeginSchedule())
                    .setParameter("endSchedule",getAttendance.getEndSchedule());
         errMessage = (String)q.uniqueResult();
          /*Query q = session.getNamedQuery("createDataAttendance").setParameter("sysCalendar",getAttendance.getSysCalendar().getIdcalendar())
                    .setParameter("idEmployee",getAttendance.getTblEmployeeByIdemployee().getIdemployee())
                    .setParameter("codeFinger",getAttendance.getCodeFingerPrint()).setParameter("beginSchedule",getAttendance.getBeginSchedule())
                    .setParameter("endSchedule",getAttendance.getEndSchedule()).setParameter("beginReal",getAttendance.getBeginReal()).setParameter("endReal",getAttendance.getEndReal())
                    .setParameter("isOvertime",getAttendance.getIsOvertime()).setParameter("note",getAttendance.getNote()).setParameter("attendanceStatus",getAttendance.getRefEmployeeAttendanceStatus().getIdstatus())
                    .setParameter("user",ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee());
          errMessage = (String)q.uniqueResult();
         /*if(errMessage=="no"){
             getAttendance.setTblEmployeeByCreateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             getAttendance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
             getAttendance.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
             getAttendance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
             getAttendance.setRefRecordStatus(session.find(RefRecordStatus.class,1));
             session.saveOrUpdate(getAttendance);
          //}*/
        }
        session.getTransaction().commit();
      }
      catch(Exception e){
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
    public boolean updateDataAttendance(TblCalendarEmployeeAttendance employeeAttendance){
      errMessage = "";
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        employeeAttendance.setTblEmployeeByLastUpdateBy(session.find(TblEmployee.class,ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee()));
        employeeAttendance.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        session.update(employeeAttendance);
        session.getTransaction().commit();
      }
      catch(Exception e){
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
    public List<TblEmployee>getAllEmployee(){
      errMessage = "";
      List<TblEmployee>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();   
       list = session.getNamedQuery("findAllTblEmployee").list();
       session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
          session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
    }
    
    @Override
    public List<RefEmployeeType>getTypeEmployee(){
      errMessage = "";
      List<RefEmployeeType>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();   
       list = session.getNamedQuery("findAllRefEmployeeType").list();
       session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
          session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
    }
    
    @Override
    public List<SysCalendar>getSysCalendarByDate(Date startPeriode,Date endPeriode,Date date,String status){
      errMessage = "";
      List<SysCalendar>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
       session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction(); 
       if(status.equalsIgnoreCase("excel")){
           System.out.println("hsl date:"+date);
          list = session.getNamedQuery("findAllSysCalendarByDate").setParameter("date",date).list();
       }
       else{
         list = session.getNamedQuery("findAllSysCalendar").setParameter("startDate",startPeriode).setParameter("endDate",endPeriode).list();
       }
       
       session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
          session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
    }
    
    @Override
    public List<TblEmployee>getAllEmployeeByFingerPrint(String codeFingerPrint){
       errMessage = "";
       List<TblEmployee>list = new ArrayList();
       if (ClassSession.checkUserSession()) {  //check user current session
       try{
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         list = session.getNamedQuery("findAllTblEmployeeByFingerPrint").setParameter("code",codeFingerPrint).list();
         session.getTransaction().commit();
       }
       catch(Exception e){
         if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
         }
         errMessage = e.getMessage();
       }
       }
       return list;
    }
    
    /*public List<ClassDataSchedule>getAllDataScheduleByPeriode(Date startDate,Date endDate){
      List<ClassDataSchedule>list = new ArrayList();
      errMessage = "";
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<TblCalendarEmployeeSchedule>listSchedule = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByPeriode").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
        List<TblCalendarEmployeeOvertime>listOvertime = session.getNamedQuery("findAllTblCalendarEmployeeOvertimeByPeriode").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
        for(TblCalendarEmployeeSchedule getSchedule : listSchedule){
          ClassDataSchedule dataEmployeeSchedule = new ClassDataSchedule();
          dataEmployeeSchedule.setTblSchedule(getSchedule);
          list.add(dataEmployeeSchedule);
        }
        
        if(listOvertime.size()!=0){
          for(ClassDataSchedule getEmployeeSchedule : list){
            for(TblCalendarEmployeeOvertime getOvertime : listOvertime){
               getEmployeeSchedule.setTblOvertime(getOvertime);
            }
          }
        }
        session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
        return null;
      }
      return list;
    }*/
    
    @Override
     public List<TblCalendarEmployeeSchedule>getAllDataScheduleByPeriode(Date startDate,Date endDate){
      errMessage = "";
      List<TblCalendarEmployeeSchedule>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        list = session.getNamedQuery("findAllTblCalendarEmployeeScheduleByPeriode").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
        session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
    }
     
    @Override
     public List<TblCalendarEmployeeOvertime>getAllDataOvertimeByPeriode(Date startDate,Date endDate){
      errMessage = "";
      List<TblCalendarEmployeeOvertime>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        list = session.getNamedQuery("findAllTblCalendarEmployeeOvertimeByPeriode").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
        session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
    }
    
    @Override
     public List<RefEmployeeAttendanceStatus>getAllAttendanceStatus(){
      errMessage = "";
      List<RefEmployeeAttendanceStatus>list = new ArrayList();      
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        list = session.getNamedQuery("findAllRefEmployeeAttendanceStatus").list();
        session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
     }
     
    @Override
      public List<RefEmployeeAttendanceStartStatus>getAllAttendanceStartStatus(){
      errMessage = "";
      List<RefEmployeeAttendanceStartStatus>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        list = session.getNamedQuery("findAllRefEmployeeAttendanceStartStatus").list();
        session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
     }
     
    @Override
      public List<RefEmployeeAttendanceEndStatus>getAllAttendanceEndStatus(){
      errMessage = "";
      List<RefEmployeeAttendanceEndStatus>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        list = session.getNamedQuery("findAllRefEmployeeAttendanceEndStatus").list();
        session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
     }
     
    @Override
     public List<TblCalendarEmployeeLeave>getAllEmployeeLeaveByDateAndIdEmployee(Date date,long id){
      errMessage = "";
      List<TblCalendarEmployeeLeave>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        list = session.getNamedQuery("findAllTblCalendarEmployeeLeaveBySysCalendarAndIdEmployee").setParameter("date",date).setParameter("idEmployee", id).list();
        session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
     }
     
    @Override
     public List<TblCalendarEmployeeAttendance>getAllEmployeeAttendance(int id,int idList,Date startDate,Date endDate){
      errMessage = "";
      List<TblCalendarEmployeeAttendance>list = new ArrayList();
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        if(id==3){
            if(idList==1){
              list = session.getNamedQuery("findAllTblCalendarEmployeeAttendancePerPeriode").setParameter("startDate",startDate).setParameter("endDate",endDate).list();
            }
            else if(idList==2){
             list = session.getNamedQuery("findAllTblCalendarEmployeeAttendanceOvertimePerPeriode").setParameter("overtime",Boolean.FALSE).setParameter("startDate",startDate).setParameter("endDate",endDate).list();
            }
            else{
             list = session.getNamedQuery("findAllTblCalendarEmployeeAttendanceOvertimePerPeriode").setParameter("overtime",Boolean.TRUE).setParameter("startDate",startDate).setParameter("endDate",endDate).list();
            }
        }
        else{
            if(idList==1){
              list = session.getNamedQuery("findAllTblCalendarEmployeeAttendanceByEmployeeTypePerPeriode").setParameter("idType",id).setParameter("startDate",startDate).setParameter("endDate",endDate).list();
            }
            else if(idList==2){
             list = session.getNamedQuery("findAllTblCalendarEmployeeAttendanceByEmployeeTypeAndOvertimePerPeriode").setParameter("idType",id).setParameter("overtime",Boolean.FALSE).setParameter("startDate",startDate).setParameter("endDate",endDate).list();
            }
            else{
             list = session.getNamedQuery("findAllTblCalendarEmployeeAttendanceByEmployeeTypeAndOvertimePerPeriode").setParameter("idType",id).setParameter("overtime",Boolean.TRUE).setParameter("startDate",startDate).setParameter("endDate",endDate).list();
            }
        }
        

        session.getTransaction().commit();
      }
      catch(Exception e){
        if(session.getTransaction().isActive()){
           session.getTransaction().rollback();
        }
        errMessage = e.getMessage();
      }
      }
      return list;
     }
     
    @Override
    public TblEmployee getEmployee(long id){
      errMessage = "";
      TblEmployee data = null;
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        data = (TblEmployee)session.find(TblEmployee.class, id);
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
     public RefEmployeeAttendanceStatus getAttendanceStatus(int id){
      errMessage = "";
      RefEmployeeAttendanceStatus data = null;
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        data = (RefEmployeeAttendanceStatus)session.find(RefEmployeeAttendanceStatus.class, id);
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
    public RefEmployeeAttendanceStartStatus getAttendanceStartStatus(int id){
      errMessage = "";
      RefEmployeeAttendanceStartStatus data = null;
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        data = (RefEmployeeAttendanceStartStatus)session.find(RefEmployeeAttendanceStartStatus.class, id);
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
    public RefEmployeeAttendanceEndStatus getAttendanceEndStatus(int id){
      errMessage = "";
      RefEmployeeAttendanceEndStatus data = null;
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        data = (RefEmployeeAttendanceEndStatus)session.find(RefEmployeeAttendanceEndStatus.class, id);
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
    public TblCalendarEmployeeAttendance getEmployeeAttendance(long id){
      errMessage = "";
      TblCalendarEmployeeAttendance data = null;
      if (ClassSession.checkUserSession()) {  //check user current session
      try{
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        data = (TblCalendarEmployeeAttendance)session.find(TblCalendarEmployeeAttendance.class, id);
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
    public String getErrorMessage(){
       return errMessage;
    }
    
   /* public static void main(String[] args) {
        System.out.println(getAttendanceStartStatus(1).getStatusName());
    }*/
}
