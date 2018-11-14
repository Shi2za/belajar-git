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
import hotelfx.helper.ClassSession;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.model.LogRoomTypeHistory;
import hotelfx.persistence.model.RefFinanceTransactionPaymentType;
import hotelfx.persistence.model.RefLostFoundStatus;
import hotelfx.persistence.model.RefPurchaseOrderStatus;
import hotelfx.persistence.model.RefReservationOrderByType;
import hotelfx.persistence.model.RefReservationStatus;
import hotelfx.persistence.model.TblBankAccount;
import hotelfx.persistence.model.TblCalendarEmployeeDebt;
import hotelfx.persistence.model.TblCompanyBalance;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Andreas
 */
public class FReportManagerImpl implements FReportManager {
    
    Session session;
    String errMessage;
    
    //---------------Report Reservation -----------------------
   public List<TblReservation>getAllDataReservation(){
      List<TblReservation>list = new ArrayList();
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblReservation").list();
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
  
   public List<TblReservation>getAllDataReservationByPeriode(Date startDate,Date endDate,
                                                             RefReservationStatus reservationStatus,
                                                             TblTravelAgent travelAgent,
                                                             RefReservationOrderByType reservationType,
                                                             TblReservation reservation){
       List<TblReservation>list = new ArrayList();
       if(ClassSession.checkUserSession()){
           String queryAll = "from TblReservation t where ";
           String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by convert(date,reservationDate),t.idreservation asc";
           String hslQuery  = "";
           String reservationStatusClause = "";
           String travelAgentClause = "" ;
           String dateClause = "";
           String reservationClause = "";
              //System.out.println("hsl query >> "+hslQuery);
             
           Query query = null;
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               
               if(startDate!=null && endDate!=null){
                  dateClause = "convert(date,reservationDate) between '"+startDate+"' and '"+endDate+"' and ";
               }
                
               if(reservationStatus!=null){
                  reservationStatusClause = "t.refReservationStatus.idstatus="+reservationStatus.getIdstatus()+" and ";           
                }
               
               if(reservationType!=null){
                   if(reservationType.getIdtype()==0){
                      travelAgentClause = "t.tblPartner is null and ";
                   }
                   else{
                     travelAgentClause = "t.tblPartner is not null and ";
                   }
               }
               
               if(travelAgent!=null){
                 travelAgentClause = "t.tblPartner.idpartner="+travelAgent.getTblPartner().getIdpartner()+" and ";
                }
              
               if(reservation!=null){
                  reservationClause = "t.idreservation ="+reservation.getIdreservation()+" and ";
               }
               
              hslQuery = queryAll+dateClause+reservationStatusClause+travelAgentClause+reservationClause+recordStatusClause;
              query = session.createQuery(hslQuery);  
              list = (List<TblReservation>)query.list();
              session.getTransaction().commit();
            }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               } 
           }
       }
     return list;
   }
   
  public List<TblReservationRoomTypeDetail>getAllDataReservationRoomByIDReservation(long idReservation){
      List<TblReservationRoomTypeDetail>list = new ArrayList();
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblReservationRoomTypeDetailByIDReservation").
                     setParameter("idReservation",idReservation).list();
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
  
  public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailByIDReservation(long id){
      List<TblReservationRoomTypeDetailRoomPriceDetail>list = new ArrayList();
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByIdReservation").
                     setParameter("idReservation",id).list();
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
  
  public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailByDate(Date date){
      List<TblReservationRoomTypeDetailRoomPriceDetail>list = new ArrayList();
        String queryAll = "from TblReservationRoomTypeDetailRoomPriceDetail t where ";
        String recordStatusClause = "t.tblReservationRoomPriceDetail.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.tblReservationRoomPriceDetail.detailDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
        String hslQuery  = "";
        String roomTypeClause = "";
        String travelAgentClause = "" ;
        String dateClause = "";
        String reservationTypeClause = "";
        Query query = null;
        
        if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               
               if(date!=null){
                 dateClause = "t.tblReservationRoomPriceDetail.detailDate = '"+date+"' and ";
               }
              
             /*  if(travelAgent!=null){
                 travelAgentClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner.idpartner="+travelAgent.getTblPartner().getIdpartner()+" and "; 
                }
               
               if(roomType!=null){
                  roomTypeClause = "t.tblReservationRoomTypeDetail.tblRoomType.idroomType="+roomType.getIdroomType()+" and ";
                }
               
               if(reservationType!=null){
                   if(reservationType.getIdtype() == 0){
                     reservationTypeClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner is null and "; 
                    }
                   else{
                     reservationTypeClause = travelAgentClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner is not null and "; 
                    }
               }*/
                           
              hslQuery = queryAll+dateClause+reservationTypeClause+travelAgentClause+roomTypeClause+recordStatusClause;
              query = session.createQuery(hslQuery);
              list = (List<TblReservationRoomTypeDetailRoomPriceDetail>)query.list();
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
  
    public List<TblReservationAdditionalItem>getAllDataReservationAdditionalItemDetailByIDReservation(long idReservation){
      List<TblReservationAdditionalItem>list = new ArrayList();
      errMessage = ""; 
      if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblReservationAdditionalItemByIdReservation").
                     setParameter("idReservation",idReservation).list();
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
    
   public List<TblReservationAdditionalService>getAllDataReservationAdditionalServiceByIDReservation(long id){
      List<TblReservationAdditionalService>list = new ArrayList();
      errMessage = "";
      
      if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblReservationAdditionalServiceByIdReservation").setParameter("idReservation",id).list();
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
   
   public List<TblReservationBill> getAllDataReservationBill(long id,int idType){
     errMessage = "";
     List<TblReservationBill>list = new ArrayList();
     
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblReservationBillByIDReservationAndIDReservationBillType").
                     setParameter("idReservation",id).setParameter("idType",idType).list();
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
   
   public List<TblReservationPayment>getAllDataReservationPayment(long id,int idType){
       errMessage = "";
     List<TblReservationPayment>list = new ArrayList();
     
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblReservationPaymentByIDReservationAndIDReservationBillType").
                     setParameter("idReservation",id).setParameter("idReservationBillType",idType).list();
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
   
   public List<TblReservationBrokenItem>getAllDataBrokenItem(long id){
     errMessage = "";
     List<TblReservationBrokenItem>list = new ArrayList();
     
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              list = session.getNamedQuery("findAllTblReservationBrokenItemByIdReservation").setParameter("idReservation",id).list();
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
   
   public List<RefReservationStatus>getAllDataReservationStatus(){
      errMessage = "";
      List<RefReservationStatus>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllRefReservationStatus").list();
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
   
   public List<TblTravelAgent>getAllDataTravelAgent(){
      errMessage = "";
      List<TblTravelAgent>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblTravelAgent").list();
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
   
    public List<TblRoomType>getAllDataRoomType(){
      errMessage = "";
      List<TblRoomType>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblRoomType").list();
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
   
   public List<TblRoomService>getAllDataRoomService(){
      List<TblRoomService>list = new ArrayList();
      errMessage = "";
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblRoomService").list();
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
    
    public List<TblItem>getAllDataAdditionalItemByGuestStatus(boolean guestStatus){
       List<TblItem>list = new ArrayList();
       errMessage = "";
      
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblItemByGuestStatus")
                     .setParameter("guestStatus",guestStatus).list();
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
    
    public List<RefReservationOrderByType>getAllDataReservationType(){
      List<RefReservationOrderByType>list = new ArrayList();
      errMessage = "";
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllRefReservationOrderByType").list();
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
    
    public List<TblReservationAdditionalService>getAllDataAdditionalServiceOtherByDate(Date date){
       List<TblReservationAdditionalService>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalService t where ";
       String recordStatusClause = "t.tblRoomService.idroomService not in (1) and t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String dateClause = "";
       String roomServiceClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(date!=null){
                  dateClause = "t.additionalDate ='"+date+"' and ";
               }
               
            /*   if(roomService!=null){
                  roomServiceClause = "t.tblRoomService.idroomService="+roomService.getIdroomService()+" and ";
               }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==1){
                     additionalTypeClause = "t.tblRoomService is not null and "; 
                   } 
               } */
              
              hslQuery = queryAll+dateClause+additionalTypeClause+roomServiceClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalService>)query.list();
              session.getTransaction().commit();
              System.out.println("hsl>>"+list.size());
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }
    
      public List<TblReservationAdditionalService>getAllDataAdditionalServiceBreakfastByDate(Date date){
       List<TblReservationAdditionalService>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalService t where ";
       String recordStatusClause = "t.tblRoomService.idroomService = 1 and t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String dateClause = "";
       String roomServiceClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(date!=null){
                  dateClause = "t.additionalDate='"+date+"' and ";
               }
               
            /*   if(roomService!=null){
                  roomServiceClause = "t.tblRoomService.idroomService="+roomService.getIdroomService()+" and ";
               }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==1){
                     additionalTypeClause = "t.tblRoomService is not null and "; 
                   } 
               } */
              
              hslQuery = queryAll+dateClause+additionalTypeClause+roomServiceClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalService>)query.list();
              session.getTransaction().commit();
              System.out.println("hsl>>"+list.size());
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }
      
    public List<TblReservationAdditionalItem>getAllDataAdditionalItemByDate(Date date){
       List<TblReservationAdditionalItem>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalItem t where ";
       String recordStatusClause = "t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String dateClause = "";
       String itemClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(date!=null){
                 dateClause = "t.additionalDate = '"+date+"' and ";
               }
               
            /*   if(tblItem!=null){
                 itemClause = "t.tblItem.iditem ="+tblItem.getIditem()+ " and ";  
                }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==2){
                     additionalTypeClause = "t.tblItem is not null and ";
                   }
               } */
              
              hslQuery = queryAll+dateClause+additionalTypeClause+itemClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalItem>)query.list();
              session.getTransaction().commit();
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }

//REPORT REVENUE MONTH
     public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailByMonth(String month){
      List<TblReservationRoomTypeDetailRoomPriceDetail>list = new ArrayList();
        String queryAll = "from TblReservationRoomTypeDetailRoomPriceDetail t where ";
        String recordStatusClause = "t.tblReservationRoomPriceDetail.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.tblReservationRoomPriceDetail.detailDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
        String hslQuery  = "";
        String roomTypeClause = "";
        String travelAgentClause = "" ;
        String monthClause = "";
        String reservationTypeClause = "";
        Query query = null;
        
        if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               
               if(month!=null){
                 monthClause = "t.tblReservationRoomPriceDetail.detailDate like '"+month+"%' and ";
               }
              
             /*  if(travelAgent!=null){
                 travelAgentClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner.idpartner="+travelAgent.getTblPartner().getIdpartner()+" and "; 
                }
               
               if(roomType!=null){
                  roomTypeClause = "t.tblReservationRoomTypeDetail.tblRoomType.idroomType="+roomType.getIdroomType()+" and ";
                }
               
               if(reservationType!=null){
                   if(reservationType.getIdtype() == 0){
                     reservationTypeClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner is null and "; 
                    }
                   else{
                     reservationTypeClause = travelAgentClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner is not null and "; 
                    }
               }*/
                           
              hslQuery = queryAll+monthClause+reservationTypeClause+travelAgentClause+roomTypeClause+recordStatusClause;
              query = session.createQuery(hslQuery);
              list = (List<TblReservationRoomTypeDetailRoomPriceDetail>)query.list();
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
     
    public List<TblReservationAdditionalService>getAllDataAdditionalServiceOtherByMonth(String month){
       List<TblReservationAdditionalService>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalService t where ";
       String recordStatusClause = "t.tblRoomService not in (1) and t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String monthClause = "";
       String roomServiceClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(month!=null){
                 monthClause = "t.additionalDate like '"+month+"%' and ";
               }
               
            /*   if(roomService!=null){
                  roomServiceClause = "t.tblRoomService.idroomService="+roomService.getIdroomService()+" and ";
               }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==1){
                     additionalTypeClause = "t.tblRoomService is not null and "; 
                   } 
               } */
              
              hslQuery = queryAll+monthClause+additionalTypeClause+roomServiceClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalService>)query.list();
              session.getTransaction().commit();
              System.out.println("hsl>>"+list.size());
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }
    
      public List<TblReservationAdditionalService>getAllDataAdditionalServiceBreakfastByMonth(String month){
       List<TblReservationAdditionalService>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalService t where ";
       String recordStatusClause = "t.tblRoomService = 1 and t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String monthClause = "";
       String roomServiceClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(month!=null){
                  monthClause = "t.additionalDate like '"+month+"%' and ";
               }
               
            /*   if(roomService!=null){
                  roomServiceClause = "t.tblRoomService.idroomService="+roomService.getIdroomService()+" and ";
               }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==1){
                     additionalTypeClause = "t.tblRoomService is not null and "; 
                   } 
               } */
              
              hslQuery = queryAll+monthClause+additionalTypeClause+roomServiceClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalService>)query.list();
              session.getTransaction().commit();
              System.out.println("hsl>>"+list.size());
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }
      
    public List<TblReservationAdditionalItem>getAllDataAdditionalItemByMonth(String month){
       List<TblReservationAdditionalItem>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalItem t where ";
       String recordStatusClause = "t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String monthClause = "";
       String itemClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
      
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(month!=null){
                 monthClause = "t.additionalDate like '"+month+"%' and ";
               }
               
            /*   if(tblItem!=null){
                 itemClause = "t.tblItem.iditem ="+tblItem.getIditem()+ " and ";  
                }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==2){
                     additionalTypeClause = "t.tblItem is not null and ";
                   }
               } */
              
              hslQuery = queryAll+monthClause+additionalTypeClause+itemClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalItem>)query.list();
              session.getTransaction().commit();
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }
    
//REPORT REVENUE YEARLY
    public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllDataReservationRoomPriceDetailByYear(int year){
      List<TblReservationRoomTypeDetailRoomPriceDetail>list = new ArrayList();
      String queryAll = "from TblReservationRoomTypeDetailRoomPriceDetail t where ";
      String recordStatusClause = "t.tblReservationRoomPriceDetail.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.tblReservationRoomPriceDetail.detailDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
      String hslQuery  = "";
      String roomTypeClause = "";
      String travelAgentClause = "" ;
      String yearClause = "";
      String reservationTypeClause = "";
      Query query = null;
        
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               
               if(year!=0){
                 yearClause = "t.tblReservationRoomPriceDetail.detailDate like '"+year+"%' and ";
               }
              
             /*  if(travelAgent!=null){
                 travelAgentClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner.idpartner="+travelAgent.getTblPartner().getIdpartner()+" and "; 
                }
               
               if(roomType!=null){
                  roomTypeClause = "t.tblReservationRoomTypeDetail.tblRoomType.idroomType="+roomType.getIdroomType()+" and ";
                }
               
               if(reservationType!=null){
                   if(reservationType.getIdtype() == 0){
                     reservationTypeClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner is null and "; 
                    }
                   else{
                     reservationTypeClause = travelAgentClause = "t.tblReservationRoomTypeDetail.tblReservation.tblPartner is not null and "; 
                    }
               }*/
                           
              hslQuery = queryAll+yearClause+reservationTypeClause+travelAgentClause+roomTypeClause+recordStatusClause;
              query = session.createQuery(hslQuery);
              list = (List<TblReservationRoomTypeDetailRoomPriceDetail>)query.list();
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
     
    public List<TblReservationAdditionalService>getAllDataAdditionalServiceOtherByYear(int year){
       List<TblReservationAdditionalService>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalService t where ";
       String recordStatusClause = "t.tblRoomService not in (1) and t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String yearClause = "";
       String roomServiceClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(year!=0){
                 yearClause = "t.additionalDate like '"+year+"%' and ";
               }
               
            /*   if(roomService!=null){
                  roomServiceClause = "t.tblRoomService.idroomService="+roomService.getIdroomService()+" and ";
               }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==1){
                     additionalTypeClause = "t.tblRoomService is not null and "; 
                   } 
               } */
              
              hslQuery = queryAll+yearClause+additionalTypeClause+roomServiceClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalService>)query.list();
              session.getTransaction().commit();
              System.out.println("hsl>>"+list.size());
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }
    
      public List<TblReservationAdditionalService>getAllDataAdditionalServiceBreakfastByYear(int year){
       List<TblReservationAdditionalService>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalService t where ";
       String recordStatusClause = "t.tblRoomService = 1 and t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String yearClause = "";
       String roomServiceClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
       
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(year!=0){
                  yearClause = "t.additionalDate like '"+year+"%' and ";
               }
               
            /*   if(roomService!=null){
                  roomServiceClause = "t.tblRoomService.idroomService="+roomService.getIdroomService()+" and ";
               }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==1){
                     additionalTypeClause = "t.tblRoomService is not null and "; 
                   } 
               } */
              
              hslQuery = queryAll+yearClause+additionalTypeClause+roomServiceClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalService>)query.list();
              session.getTransaction().commit();
              System.out.println("hsl>>"+list.size());
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }
      
    public List<TblReservationAdditionalItem>getAllDataAdditionalItemByYear(int year){
       List<TblReservationAdditionalItem>list = new ArrayList();
       errMessage = "";
       String queryAll = "from TblReservationAdditionalItem t where ";
       String recordStatusClause = "t.refReservationBillType.idtype not in (4) and t.refEndOfDayDataStatus.idstatus = 1 and (t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by t.additionalDate,t.tblReservationRoomTypeDetail.tblReservation.idreservation asc";
       String yearClause = "";
       String itemClause = "";
       String additionalTypeClause = "";
       String hslQuery = "";
       Query query = null;
      
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               
               if(year!=0){
                 yearClause = "t.additionalDate like '"+year+"%' and ";
               }
               
            /*   if(tblItem!=null){
                 itemClause = "t.tblItem.iditem ="+tblItem.getIditem()+ " and ";  
                }
               
               if(additionalType!=null){
                   if(additionalType.getIdType()==2){
                     additionalTypeClause = "t.tblItem is not null and ";
                   }
               } */
              
              hslQuery = queryAll+yearClause+additionalTypeClause+itemClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationAdditionalItem>)query.list();
              session.getTransaction().commit();
            }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
                 errMessage = e.getMessage();
               } 
           }
       }
     return list;
    }
    
   public List<TblLocationOfWarehouse>getAllDataWarehouse(){
       errMessage = "";
       List<TblLocationOfWarehouse>list = new ArrayList();
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblLocationOfWarehouse").list();
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
   
   public List<TblItemMutationHistory>getAllDataItemMutationHistoryByIDWarehouse(TblLocation location,Date startDate,Date endDate){
      errMessage = "";
      List<TblItemMutationHistory>list = new ArrayList();
      String queryAll = "from TblItemMutationHistory t where ";
      String recordStatusClause ="(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) order by convert(date,t.mutationDate) asc";
      String locationClause = "";
      String dateClause = "";
      Query query = null;
      String hslQuery = "";
      
      if(ClassSession.checkUserSession()){
         try{
           session = HibernateUtil.getSessionFactory().getCurrentSession();
           session.beginTransaction();
           if(location!=null){
             locationClause = "(t.tblLocationByIdlocationOfDestination.idlocation = "+location.getIdlocation()+" or t.tblLocationByIdlocationOfSource.idlocation="+location.getIdlocation()+") and ";
           }
           
           if(startDate!=null && endDate!=null){
             dateClause = "convert(date,t.mutationDate) between '"+startDate+"' and '"+endDate+"' and ";
           }
           
           hslQuery = queryAll+dateClause+locationClause+recordStatusClause;
           System.out.println("hsl :"+hslQuery);
           query = session.createQuery(hslQuery);
           list = (List<TblItemMutationHistory>)query.list();
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
   
     public BigDecimal getStock(TblLocation location,TblItem item,Date startDate){
       errMessage = "";
       String stock = "";
       Query query = null;
        
       if(ClassSession.checkUserSession()){
          try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             query = session.getNamedQuery("getStock").setParameter("idLocation",location.getIdlocation()).setParameter("idItem",item.getIditem()).setParameter("date",startDate);             
             stock = (String)query.uniqueResult();
             System.out.println("hsl stock :"+stock);
             session.getTransaction().commit();
           }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
              errMessage = e.getMessage();
           }
        }
       return new BigDecimal(stock);
    }
     
     //REPORT EMPLOYEE
     
    public List<TblEmployee>getAllDataEmployee(){
       errMessage = "";
       List<TblEmployee>list = new ArrayList();
       if(ClassSession.checkUserSession()){
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
    
    public List<TblCalendarEmployeeDebt>getAllDataDebt(TblEmployee employee,Date startDate,Date endDate,ClassDataDebtStatus debtStatus){
       errMessage = "";
       String queryAll = "from TblCalendarEmployeeDebt t where ";
       String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3)";
       String dateClause = "";
       String employeeClause = "";
       String debtStatusClause = "";
       String hslQuery = "";
       Query query = null;
       List<TblCalendarEmployeeDebt>list = new ArrayList();
       
       if(ClassSession.checkUserSession()){
          try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               if(employee!=null){
                 employeeClause = " t.tblEmployeeByIdemployee.idemployee="+employee.getIdemployee()+" and ";
               }
               
               if(startDate!=null && endDate!=null){
                  dateClause = "convert(date,t.createdDate) between '"+startDate+"' and '"+endDate+"' and "; 
               }
               
               if(debtStatus!=null){
                  debtStatusClause = "t.employeeDebtStatus='"+debtStatus.getStatusName()+"' and ";
               }
               hslQuery = queryAll+employeeClause+dateClause+debtStatusClause+recordStatusClause;
               
               System.out.println("hsl :"+hslQuery);
               query = session.createQuery(hslQuery);
               list = (List<TblCalendarEmployeeDebt>)query.list();
               session.getTransaction().commit();
           }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               } 
           }
       }
       return list;
    }
    
 //LAPORAN PEMBELIAN BARANG
    public List<TblPurchaseOrderDetail>getAllDataPurchaseOrderDetail(TblSupplier supplier,TblPurchaseOrder po,RefPurchaseOrderStatus poStatus,TblItem item,Date startDate,Date endDate,TblPurchaseRequest mr){
      errMessage = "";
      List<TblPurchaseOrderDetail>list = new ArrayList();
      String queryAll = "from TblPurchaseOrderDetail t where ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3)";
      String dateClause = "";
      String supplierClause = "";
      String statusPOClause = "";
      String itemClause = "";
      String codePOClause = "";
      String mrClause = "";
      String hslQuery = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               if(startDate!=null && endDate!=null){
                 dateClause = "convert(date,t.tblPurchaseOrder.podate) between '"+startDate +"' and '"+endDate+"' and "; 
               }
               
               if(supplier!=null){
                 supplierClause = "t.tblPurchaseOrder.tblSupplier.idsupplier = "+supplier.getIdsupplier()+" and ";
               }
               
               if(poStatus!=null){
                 statusPOClause = "t.tblPurchaseOrder.refPurchaseOrderStatus.idstatus = "+poStatus.getIdstatus()+" and ";
               }
               
               if(item!=null){
                 itemClause = "t.tblSupplierItem.tblItem.iditem = "+item.getIditem()+" and ";
               }
               
               if(po!=null){
                 codePOClause = "t.tblPurchaseOrder.idpo = "+po.getIdpo()+" and ";
               }
               
               if(mr!=null){
                 mrClause = "t.tblPurchaseOrder.tblPurchaseRequest.idpr = "+mr.getIdpr()+" and ";
               }
               
             hslQuery = queryAll+dateClause+supplierClause+statusPOClause+itemClause+codePOClause+mrClause+recordStatusClause;
             System.out.println("hsl :"+hslQuery);
             query = session.createQuery(hslQuery);
             list = (List<TblPurchaseOrderDetail>)query.list();
             System.out.println("hsl list : "+list.size());
             session.getTransaction().commit();
           }
          catch(Exception e){
             if(session.getTransaction().isActive()){
               session.getTransaction().rollback();
             } 
          }
       }
      return list;
    }
    
    public List<TblPurchaseOrder>getAllDataPurchaseOrder(){
       errMessage = "";
       List<TblPurchaseOrder>list = new ArrayList();
       
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblPurchaseOrderByIDPOStatus").list();
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
    
    public List<RefPurchaseOrderStatus>getAllPurchasingStatus(){
       List<RefPurchaseOrderStatus>list = new ArrayList();
       errMessage = "";
       if(ClassSession.checkUserSession()){
          try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            list = session.getNamedQuery("findAllRefPurchaseOrderStatus").list();
            session.getTransaction().commit();
          }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               } 
           }
       }
      return list;
    }
    
    public List<TblItem>getAllDataItem(){
       errMessage = "";
       List<TblItem>list = new ArrayList();
       
       if(ClassSession.checkUserSession()){
          try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            list = session.getNamedQuery("findAllTblItem").list();
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
    
    public List<TblSupplier>getAllDataSupplier(){
       errMessage = "";
       List<TblSupplier>list = new ArrayList();
       
       if(ClassSession.checkUserSession()){
          try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            list = session.getNamedQuery("findAllTblSupplier").list();
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
    
    public List<TblPurchaseRequest>getAllTblPurchaseRequest(){
      errMessage = "";
      List<TblPurchaseRequest>list = new ArrayList();
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblPurchaseRequest").list();
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
    
    public List<TblReservationRoomTypeDetail>getAllDataRoomTypeDetailByPeriode(Date startDate,Date endDate){
       errMessage = "";
       String queryAll = "from TblReservationRoomTypeDetail t where t.tblReservationCheckInOut is not null and (t.tblReservationCheckInOut.refReservationCheckInOutStatus.idstatus = 1 or t.tblReservationCheckInOut.refReservationCheckInOutStatus.idstatus = 2) and ";
       String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3)";
       String dateClause = "";
       String hslQuery = "";
       Query query = null;
       List<TblReservationRoomTypeDetail>list = new ArrayList();
       
       if(ClassSession.checkUserSession()){
          try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              
               if(startDate!=null && endDate!=null){
                  dateClause = "convert(date,t.tblReservationCheckInOut.checkInDateTime) between '"+startDate+"' and '"+endDate+"' and "; 
               }
               
               hslQuery = queryAll+dateClause+recordStatusClause;
               
               System.out.println("hsl :"+hslQuery);
               query = session.createQuery(hslQuery);
               list = (List<TblReservationRoomTypeDetail>)query.list();
               session.getTransaction().commit();
           }
          catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               } 
           }
       }
       return list;
    }
   
   public List<RefLostFoundStatus>getAllLostFoundStatus(){
      errMessage = "";
      List<RefLostFoundStatus>list = new ArrayList();
      if(ClassSession.checkUserSession()){
         try{
           session = HibernateUtil.getSessionFactory().getCurrentSession();
           session.beginTransaction();
           list = session.getNamedQuery("findAllRefLostFoundStatus").list();
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
   
   public List<TblLostInformation>getAllDataLostInformation(Date startDate,Date endDate,RefLostFoundStatus lostStatus){
     errMessage = "";
     List<TblLostInformation>list = new ArrayList();
     String queryAll  = "from TblLostInformation t where ";
     String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3)";
     String dateClause = "";
     String hslQuery = "";
     String lostStatusClause = "";
     Query query = null;
     
     if(ClassSession.checkUserSession()){
       try{
           session = HibernateUtil.getSessionFactory().getCurrentSession();
           session.beginTransaction();
           if(startDate!=null && endDate!=null){
             dateClause = "t.lostDate between '"+startDate+"' and '"+endDate+"' and ";
           }
           
           if(lostStatus!=null){
             lostStatusClause = "t.refLostFoundStatus.idstatus = "+lostStatus.getIdstatus()+" and ";
           }
           
          hslQuery = queryAll+dateClause+lostStatusClause+recordStatusClause;
          query = session.createQuery(hslQuery);
          list = (List<TblLostInformation>)query.list();
          session.getTransaction().commit();
        }
       catch(Exception e){
           if(session.getTransaction().isActive()){
             session.getTransaction().rollback();
            }
        }
     }
     return list;
   }
   
    public List<TblFoundInformation>getAllDataFoundInformation(Date startDate,Date endDate,RefLostFoundStatus foundStatus){
     errMessage = "";
     List<TblFoundInformation>list = new ArrayList();
     String queryAll  = "from TblFoundInformation t where ";
     String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3)";
     String dateClause = "";
     String hslQuery = "";
     String foundStatusClause = "";
     Query query = null;
     
     if(ClassSession.checkUserSession()){
       try{
           session = HibernateUtil.getSessionFactory().getCurrentSession();
           session.beginTransaction();
           if(startDate!=null && endDate!=null){
             dateClause = "t.foundDate between '"+startDate+"' and '"+endDate+"' and ";
           }
           
           if(foundStatus!=null){
             foundStatusClause = "t.refLostFoundStatus.idstatus = "+foundStatus.getIdstatus()+" and ";
           }
           
          hslQuery = queryAll+dateClause+foundStatusClause+recordStatusClause;
          query = session.createQuery(hslQuery);
          list = (List<TblFoundInformation>)query.list();
          session.getTransaction().commit();
        }
       catch(Exception e){
           if(session.getTransaction().isActive()){
             session.getTransaction().rollback();
            }
        }
     }
     return list;
   }
    
    @Override
   public List<TblLostFoundInformationDetail>getAllDataReturnInformation(TblLostInformation lostInformation,TblFoundInformation foundInformation){
      errMessage = "";
      List<TblLostFoundInformationDetail>list = new ArrayList();
    //System.out.println("hsl lost :"+lostInformation.getCodeLost());
      
      if(ClassSession.checkUserSession()){
           try{
               session = HibernateUtil.getSessionFactory().getCurrentSession();
               session.beginTransaction();
               if(lostInformation!=null){
                 list = session.getNamedQuery("findAllTblLostFoundInformationDetailByIdLost").setParameter("idLost",lostInformation.getIdlost()).list();
               }
               
               if(foundInformation!=null){
                 list = session.getNamedQuery("findAllTblLostFoundInformationDetailByIdFound").setParameter("idFound",foundInformation.getIdfound()).list();
               }
            
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
           }
       }
      return list;
   }
   
   public List<TblReservationRoomTypeDetail>getAllReservationCheckOut(Date startDate,Date endDate){
      List<TblReservationRoomTypeDetail>list = new ArrayList();
      errMessage = "";
      String queryAll = "from TblReservationRoomTypeDetail t where t.tblReservationCheckInOut is not null and t.tblReservationCheckInOut.tblRoom is not null and t.tblReservationCheckInOut.refReservationCheckInOutStatus.idstatus = 3 and ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3)";
      String hslQuery = "";
      String dateClause = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             if(startDate!=null && endDate!=null){
                 dateClause = "convert(date,t.tblReservationCheckInOut.checkOutDateTime) between '"+startDate+"' and '"+endDate+"' and ";
              }
              hslQuery = queryAll+dateClause+recordStatusClause;
              System.out.println("hsl :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblReservationRoomTypeDetail>)query.list();
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
   
   // LAPORAN REKAP KAMAR
   
   public List<LogRoomTypeHistory>getAllRoomTypeHistory(TblRoomType roomType){
      errMessage = "";
      List<LogRoomTypeHistory>list = new ArrayList();
      
      if(ClassSession.checkUserSession()){
         try{
           session = HibernateUtil.getSessionFactory().getCurrentSession();
           session.beginTransaction();
           list = session.getNamedQuery("findAllLogRoomTypeHistoryByIDRoomType").setParameter("idRoomType",roomType.getIdroomType()).list();
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
   public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllRoomTypeReservation(Date date,TblRoomType roomType){
      errMessage = "";
      List<TblReservationRoomTypeDetailRoomPriceDetail>list = new ArrayList();
      
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblReservationRoomTypeDetailRoomPriceDetailByDateAndIdRoomType").setParameter("idRoomType",roomType.getIdroomType()).setParameter("date",date).list();
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
   
  /* public List<TblReservationCheckInOut>getAllRoomUsed(Date date,TblRoomType roomType){
      errMessage = "";
      List<TblReservationCheckInOut>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblReservationCheckInOutByDateAndIDRoomType").setParameter("date",date).setParameter("idRoomType",roomType.getIdroomType()).list();
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
   } */
   
   public List<TblReservationRoomTypeDetailRoomPriceDetail>getAllRoomSold(Date date,TblRoomType roomType){
     errMessage = "";
     List<TblReservationRoomTypeDetailRoomPriceDetail>list = new ArrayList();
     String queryAll = "from TblReservationRoomTypeDetailRoomPriceDetail t where t.tblReservationRoomPriceDetail.refEndOfDayDataStatus.idstatus = 1 and ";
     String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3)";
     String dateClause = "";
     String roomTypeClause = "";
     String hslQuery = "";
     Query query = null;
     
       if(ClassSession.checkUserSession()){
           try{
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if(date!=null){
                  dateClause = "t.tblReservationRoomPriceDetail.detailDate = '"+date+"' and ";
                }

                if(roomType!=null){
                  roomTypeClause = "t.tblReservationRoomTypeDetail.tblRoomType.idroomType = "+roomType.getIdroomType()+" and ";
                }
                hslQuery = queryAll+dateClause+roomTypeClause+recordStatusClause;
                query = session.createQuery(hslQuery);
                list = (List<TblReservationRoomTypeDetailRoomPriceDetail>)query.list();
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
   
// LAPORAN PENERIMAAN BARANG
   public List<TblMemorandumInvoiceDetail>getAllMemorandumInvoiceDetail(Date startDate,Date endDate,TblPurchaseOrder po,TblMemorandumInvoice mi,TblItem item,ClassMemorandumInvoiceBonusType bonusType){
     errMessage = "";
     List<TblMemorandumInvoiceDetail>list = new ArrayList();
     String queryAll = "from TblMemorandumInvoiceDetail t where ";
     String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
     String dateClause = "";
     String poClause = "";
     String miClause = "";
     String bonusTypeClause = "";
     String itemClause = "";
   //  String groupByClause = "group by t.iddetail";
     String hslQuery = "";
     Query query = null;
     
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               if(startDate!=null && endDate!=null){
                 dateClause = "(convert(date,t.tblMemorandumInvoice.receivingDate) between '"+startDate+"' and '"+endDate+"') and "; 
                }
               
               if(po!=null){
                 poClause = "t.tblMemorandumInvoice.tblPurchaseOrder.idpo = "+po.getIdpo()+" and ";
               }
       
               if(mi!=null){
                 miClause = "t.tblMemorandumInvoice.idmi = "+mi.getIdmi()+" and ";
               }
       
              if(bonusType !=null){
                   if(bonusType.getIdtype()==1){ // bonus
                     bonusTypeClause = "t.tblItem is not null and ";
                   }
           
                   if(bonusType.getIdtype()==2){
                     bonusTypeClause = "t.tblItem is null and ";  
                   }
               }
       
             /* if(item!=null){
                 itemClause = "((t.tblItem is not null and t.tblItem.iditem = "+item.getIditem()+") or (t.tblSupplierItem is not null and t.tblSupplierItem.tblItem.iditem ="+item.getIditem() +")) and ";
               } */
              
              hslQuery = queryAll+dateClause+poClause+bonusTypeClause+itemClause+miClause+recordStatusClause;
              System.out.println("hsl : "+hslQuery);
              query = session.createQuery(hslQuery);
              
               System.out.println("-------> " + query.list().size());
              
              list = (List<TblMemorandumInvoiceDetail>)query.list();
              List<TblMemorandumInvoiceDetail>listItem = new ArrayList();
              
               if(item!=null){
                   for(int i = list.size()-1; i>=0;i--){
                       TblMemorandumInvoiceDetail getMemorandumInvoiceDetail = list.get(i);
                       if(getMemorandumInvoiceDetail.getTblItem()!=null && getMemorandumInvoiceDetail.getTblItem().getIditem()!=item.getIditem()){
                          list.remove(getMemorandumInvoiceDetail);
                       }

                       if(getMemorandumInvoiceDetail.getTblSupplierItem()!=null && getMemorandumInvoiceDetail.getTblSupplierItem().getTblItem().getIditem()!=item.getIditem()){
                          list.remove(getMemorandumInvoiceDetail);
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
           }
       }
      return list;
    }
   
   public List<TblMemorandumInvoiceDetailItemExpiredDate>getDataExpiredDate(long detail){
      errMessage = "";
      List<TblMemorandumInvoiceDetailItemExpiredDate>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblMemorandumInvoiceDetailItemExpiredDateByIDMemorandumInvoiceDetail").setParameter("idMemorandumInvoiceDetail",detail).list();
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
   
   public List<TblMemorandumInvoice>getAllDataMemorandumInvoice(){
     errMessage = "";
     List<TblMemorandumInvoice>list = new ArrayList();
     
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblMemorandumInvoice").list();
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
   
   //REPORT RETUR BARANG
   public List<TblReturDetail>getAllReturDetail(Date startDate,Date endDate,TblSupplier supplier,TblItem item,TblPurchaseOrder po,TblRetur retur){
      errMessage = "";
      List<TblReturDetail>list = new ArrayList();
      String queryAll = "from TblReturDetail t where ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
      String dateClause = "";
      String returClause = "";
      String purchaseOrderClause = "";
      String itemClause = "";
      String supplierClause = "";
      String hslQuery = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
               session = HibernateUtil.getSessionFactory().getCurrentSession();
               session.beginTransaction();
               if(startDate!=null && endDate!=null){
                  dateClause = "t.tblRetur.returDate between '"+startDate+"' and '"+endDate+"' and "; 
               }
               
                if(retur!=null){
                  returClause = "t.tblRetur.idretur = "+retur.getIdretur()+" and ";
               }

               if(po!=null){
                  purchaseOrderClause = "t.tblMemorandumInvoice.tblPurchaseOrder.idpo ="+po.getIdpo()+" and ";
               }
               
               if(item!=null){
                  itemClause = "t.tblSupplierItem.tblItem.iditem = "+item.getIditem()+" and ";
               }
               
               if(supplier !=null){
                  supplierClause = "t.tblRetur.tblSupplier.idsupplier = "+supplier.getIdsupplier()+" and ";
               }
               
               hslQuery = queryAll+dateClause+returClause+purchaseOrderClause+itemClause+supplierClause+recordStatusClause;
               
               System.out.println("hsl :"+hslQuery);
               query = session.createQuery(hslQuery);
               list = query.list();
               
               session.getTransaction().commit();
             /*  if(retur!=null){
                  returClause = "t.tblRetur.idretur = "+retur.getIdretur()+" and ";
               }

               if(po!=null){
                  purchaseOrderClause = "t.tblMemorandumInvoice.tblPurchaseOrder.idpo ="+po.getIdpo()+" and ";
               }
               
               if(item!=null){
                  itemClause = "t.tblSupplierItem.tblItem.iditem = "+item.getIditem()+" and ";
               } */
               
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
   
   public List<TblRetur>getAllDataRetur(){
     List<TblRetur>list = new ArrayList();
     if(ClassSession.checkUserSession()){
       try{
          session = HibernateUtil.getSessionFactory().getCurrentSession();
          session.beginTransaction();
          list = session.getNamedQuery("findAllTblRetur").list();
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
   
   //LAPORAN PENERIMAAN PO
   public List<TblPurchaseOrder>getAllDataPurchaseOrder(Date startDate,Date endDate,TblPurchaseOrder po,TblSupplier supplier){
      errMessage = "";
      List<TblPurchaseOrder>list = new ArrayList();
      String queryAll = "from TblPurchaseOrder t where ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
      String dateClause = "";
      String poClause = "";
      String supplierClause = "";
      String hslQuery = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
              
               if(startDate!=null && endDate!=null){
                  dateClause = "convert(date,t.podate) between '"+startDate+"' and '"+endDate+"' and ";
               }
               
               if(po!=null){
                 poClause="t.idpo = "+po.getIdpo()+" and ";
               }
               
               if(supplier!=null){
                 supplierClause = "t.tblSupplier.idsupplier = "+supplier.getIdsupplier()+" and ";
               } 
               
              hslQuery = queryAll+dateClause+poClause+supplierClause+recordStatusClause;
              System.out.println("hsl:"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblPurchaseOrder>)query.list();
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
   
   public List<TblPurchaseOrderDetail>getAllDataPODetail(TblPurchaseOrder po,TblItem item){
     errMessage = "";
     List<TblPurchaseOrderDetail>list = new ArrayList();
     String queryAll = "from TblPurchaseOrderDetail t where t.tblPurchaseOrder.refPurchaseOrderStatus.idstatus IN (5,6) and ";
     String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
     String poClause = "";
     String itemClause = "";
     String hslQuery = "";
     Query query = null;
     
       if(ClassSession.checkUserSession()){
            try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               if(po!=null){
                 poClause = "t.tblPurchaseOrder.idpo ="+po.getIdpo()+" and ";
               }

               if(item!=null){
                 itemClause = "t.tblSupplierItem.tblItem.iditem = "+item.getIditem()+" and ";
               }

              hslQuery = queryAll+poClause+itemClause+recordStatusClause;
              System.out.println("hsl :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblPurchaseOrderDetail>)query.list();
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
   
    public List<TblMemorandumInvoiceDetail>getAllDataMIDetail(TblPurchaseOrder po,TblItem item){
     errMessage = "";
     List<TblMemorandumInvoiceDetail>list = new ArrayList();
     String queryAll = "from TblMemorandumInvoiceDetail t where ";
     String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
     String poClause = "";
     String itemClause = "";
     String hslQuery = "";
     Query query = null;
     
       if(ClassSession.checkUserSession()){
            try{
              session = HibernateUtil.getSessionFactory().getCurrentSession();
              session.beginTransaction();
               if(po!=null){
                 poClause = "t.tblMemorandumInvoice.tblPurchaseOrder.idpo ="+po.getIdpo()+" and ";
               }

               if(item!=null){
                 itemClause = "t.tblItem.iditem = "+item.getIditem()+" and ";
               }

              hslQuery = queryAll+poClause+itemClause+recordStatusClause;
              System.out.println("hsl :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblMemorandumInvoiceDetail>)query.list();
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
   
    public List<TblReturDetail>getAllDataReturDetail(TblItem item,TblPurchaseOrder po){
      errMessage = "";
      List<TblReturDetail>list = new ArrayList();
      String queryAll = "from TblReturDetail t where ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
      String purchaseOrderClause = "";
      String itemClause = "";
      String hslQuery = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
               session = HibernateUtil.getSessionFactory().getCurrentSession();
               session.beginTransaction();
             
               if(po!=null){
                  purchaseOrderClause = "t.tblMemorandumInvoice.tblPurchaseOrder.idpo ="+po.getIdpo()+" and ";
               }
               
               if(item!=null){
                  itemClause = "t.tblSupplierItem.tblItem.iditem = "+item.getIditem()+" and ";
               }
               
             
               
               hslQuery = queryAll+purchaseOrderClause+itemClause+recordStatusClause;
               
               System.out.println("hsl :"+hslQuery);
               query = session.createQuery(hslQuery);
               list = query.list();
               
               session.getTransaction().commit();
             /*  if(retur!=null){
                  returClause = "t.tblRetur.idretur = "+retur.getIdretur()+" and ";
               }

               if(po!=null){
                  purchaseOrderClause = "t.tblMemorandumInvoice.tblPurchaseOrder.idpo ="+po.getIdpo()+" and ";
               }
               
               if(item!=null){
                  itemClause = "t.tblSupplierItem.tblItem.iditem = "+item.getIditem()+" and ";
               } */
               
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
 //------------------------------REPORT STOKOPNAME----------------------
   public List<TblStockOpnameDetail>getAllDataStockOpnameForReport(Date startDate,Date endDate,TblStockOpname stockOpname,TblItem item,TblLocation location){
     List<TblStockOpnameDetail>list = new ArrayList();
     errMessage = "";
     String queryAll = "from TblStockOpnameDetail t where ";
     String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
     String dateClause = "";
     String stockOpnameClause = "";
     String itemClause = "";
     String locationClause = "";
     String hslQuery = "";
     Query query = null;
     
       if(ClassSession.checkUserSession()){
           try{
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                if(startDate!=null && endDate!=null){
                  dateClause = "convert(date,t.tblStockOpname.stockOpanameDate) between '"+startDate+"' and '"+endDate+"' and ";
                }
                
                if(stockOpname!=null){
                  stockOpnameClause = "t.tblStockOpname.idstockOpname = "+stockOpname.getIdstockOpname()+" and ";
                }
                
                if(item!=null){
                  itemClause = "t.tblItem.iditem = "+item.getIditem()+" and ";
                }
                
                if(location!=null){
                  locationClause = "t.tblStockOpname.tblLocation.idlocation = "+location.getIdlocation()+" and ";
                }
                
               hslQuery = queryAll+dateClause+stockOpnameClause+itemClause+locationClause+recordStatusClause;
               System.out.println("hsl query :"+hslQuery);
               query = session.createQuery(hslQuery);
               list = (List<TblStockOpnameDetail>)query.list();
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
   
   public List<TblStockOpnameDetailItemExpiredDate>getAllDataStockOpnameItemExp(TblItem item){
      errMessage = "";
      List<TblStockOpnameDetailItemExpiredDate>list = new ArrayList();
      String queryAll = "from TblStockOpnameDetailItemExpiredDate t where ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
      String itemClause = "";
      String hslQuery = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               if(item!=null){
                 itemClause = "t.tblItemExpiredDate.tblItem.iditem = "+item.getIditem()+" and ";
                }
              hslQuery = queryAll+itemClause+recordStatusClause;
              query = session.createQuery(hslQuery);
              list = (List<TblStockOpnameDetailItemExpiredDate>)query.list();
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
   
   public List<TblStockOpnameDetailPropertyBarcode>getAllDataStockOpnameProperty(TblItem item){
      errMessage = "";
      List<TblStockOpnameDetailPropertyBarcode>list = new ArrayList();
      String queryAll = "from TblStockOpnameDetailPropertyBarcode t where ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
      String itemClause = "";
      String hslQuery = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               if(item!=null){
                 itemClause = "t.tblStockOpnameDetail.tblItem.iditem = "+item.getIditem()+" and ";
                }
              hslQuery = queryAll+itemClause+recordStatusClause;
              query = session.createQuery(hslQuery);
              list = (List<TblStockOpnameDetailPropertyBarcode>)query.list();
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
   
   public List<TblLocationOfWarehouse>getAllDataWareHouseByIdLocation(long id){
      List<TblLocationOfWarehouse>list = new ArrayList();
      errMessage = "";
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblLocationOfWarehouseByIdLocation").setParameter("id",id).list();
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
   
  public List<TblLocationOfLaundry>getAllDataLaundryByIdLocation(long id){
      List<TblLocationOfLaundry>list = new ArrayList();
      errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblLocationOfLaundryByIdLocation").setParameter("id",id).list();
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
  
    public List<TblStockOpname>getAllDataStockOpname(){
      List<TblStockOpname>list = new ArrayList();
      errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblStockOpname").list();
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
    
   public List<TblLocationOfLaundry>getAllDataLaundry(){
       List<TblLocationOfLaundry>list = new ArrayList();
       errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblLocationOfLaundry").list();
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
   
    public List<TblRoom>getAllDataRoomByIdLocation(long id){
      List<TblRoom>list = new ArrayList();
      errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblRoomByIdLocation").setParameter("id",id).list();
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
    
   // LAPORAN RUSAK BARANG
    
   public List<TblItemMutationHistory>getAllDataItemMutationHistoryForReport(Date startDate,Date endDate,TblItem item,TblLocation location){
      errMessage = "";
      List<TblItemMutationHistory>list = new ArrayList();
      String queryAll = "from TblItemMutationHistory t where t.refItemMutationType.idtype = 2 and ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
      String dateClause = "";
      String itemClause = "";
      String locationClause = "";
      String hslQuery = "";
      Query query = null;
      
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
               if(startDate!=null && endDate!=null){
                 dateClause = "convert(date,t.mutationDate) between '"+startDate+"' and '"+endDate+"' and ";
               }
               
               if(item!=null){
                 itemClause = "t.tblItem.iditem = "+item.getIditem()+" and ";
               }
               
               if(location!=null){
                 locationClause = "t.tblLocationByIdlocationOfSource.idlocation = "+location.getIdlocation()+" and ";
               }
               
              hslQuery = queryAll+dateClause+itemClause+locationClause+recordStatusClause;
              System.out.println("hsl query :"+hslQuery);
              query = session.createQuery(hslQuery);
              list = (List<TblItemMutationHistory>)query.list();
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
   
   public List<TblRoom>getAllDataRoom(){
      errMessage = "";
      List<TblRoom>list = new ArrayList();
      
      if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblRoom").list();
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
   
// LAPORAN PENGELUARAN
   public List<TblHotelFinanceTransactionHotelPayable>getAllFinanceTransaction(Date startDate,Date endDate,RefFinanceTransactionPaymentType financeType){
      errMessage= "";
      List<TblHotelFinanceTransactionHotelPayable>list = new ArrayList();
      String queryAll = "from TblHotelFinanceTransactionHotelPayable t where ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
      String financeTypeClause = "";
      String dateClause = "";
      String hslQuery = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
           
               if(startDate!=null && endDate!=null){
                 dateClause = "convert(date,t.tblHotelFinanceTransaction.createDate) between '"+startDate+"' and '"+endDate+"' and "; 
                }
               
               if(financeType!=null){
                 financeTypeClause = "t.tblHotelFinanceTransaction.refFinanceTransactionPaymentType.idtype="+financeType.getIdtype()+" and ";
               }
               
             hslQuery = queryAll+dateClause+financeTypeClause+recordStatusClause;
             System.out.println("hsl query : "+hslQuery);
             query = session.createQuery(hslQuery);
             list = (List<TblHotelFinanceTransactionHotelPayable>)query.getResultList();
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
   
   public List<TblHotelExpenseTransactionDetail>getAllDataExpense(Date startDate,Date endDate,RefFinanceTransactionPaymentType financeType){
     errMessage= "";
      List<TblHotelExpenseTransactionDetail>list = new ArrayList();
      String queryAll = "from TblHotelExpenseTransactionDetail t where ";
      String recordStatusClause = "(t.refRecordStatus.idstatus=1 or t.refRecordStatus.idstatus=3) ";
      String dateClause = "";
      String financeTypeClause = "";
      String hslQuery = "";
      Query query = null;
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
           
               if(startDate!=null && endDate!=null){
                 dateClause = "convert(date,t.tblHotelExpenseTransaction.expenseTransactionDate) between '"+startDate+"' and '"+endDate+"' and "; 
                }
               
               if(financeType!=null){
                 financeTypeClause = "t.tblHotelExpenseTransaction.refFinanceTransactionPaymentType.idtype="+financeType.getIdtype()+" and ";  
               }
               
             hslQuery = queryAll+dateClause+financeTypeClause+recordStatusClause;
             System.out.println("hsl query expense :"+hslQuery);
             query = session.createQuery(hslQuery);
             list = (List<TblHotelExpenseTransactionDetail>)query.getResultList();
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
   
   public List<TblHotelFinanceTransactionWithTransfer>getAllDataTransfer(TblHotelFinanceTransaction financeTransaction){
      List<TblHotelFinanceTransactionWithTransfer>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblHotelFinanceTransactionWithTransferByIDHotelFinanceTransaction").setParameter("idHotelFinanceTransaction",financeTransaction.getIdtransaction()).list();
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
           }
        }
      return list;
    }
   
    public List<TblHotelFinanceTransactionWithCekGiro>getAllDataCekGiro(TblHotelFinanceTransaction financeTransaction){
      List<TblHotelFinanceTransactionWithCekGiro>list = new ArrayList();
      
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllTblHotelFinanceTransactionWithCekGiroByIDHotelFinanceTransaction").setParameter("idHotelFinanceTransaction",financeTransaction.getIdtransaction()).list();
             session.getTransaction().commit();
           }
           catch(Exception e){
               if(session.getTransaction().isActive()){
                 session.getTransaction().rollback();
               }
           }
        }
      return list;
    }
    
    public List<RefFinanceTransactionPaymentType>getAllDataPaymentType(){
       List<RefFinanceTransactionPaymentType>list = new ArrayList();
       errMessage = "";
       if(ClassSession.checkUserSession()){
           try{
             session = HibernateUtil.getSessionFactory().getCurrentSession();
             session.beginTransaction();
             list = session.getNamedQuery("findAllRefFinanceTransactionPaymentType").list();
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
}
