/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.persistence.service;

import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationDefaultBar;
import hotelfx.persistence.model.TblReservationSeason;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author ANDRI
 */
public interface FRoomRateManager {
    
    public TblReservationBar insertDataReservationBAR(TblReservationBar bar);
    
    public boolean updateDataReservationBAR(TblReservationBar bar);
    
    public boolean deleteDataReservationBAR(TblReservationBar bar);
    
    public TblReservationBar getReservationBAR(long id);
    
    public List<TblReservationBar> getAllDataReservationBAR();
    
    //--------------------------------------------------------------------------
    
    public TblReservationSeason insertDataReservationSeason(TblReservationSeason season);
    
    public boolean updateDataReservationSeason(TblReservationSeason season);
    
    public boolean deleteDataReservationSeason(TblReservationSeason season);
    
    public TblReservationSeason getReservationSeason(long id);
    
    public List<TblReservationSeason> getAllDataReservationSeason();
    
    //--------------------------------------------------------------------------
    
    public TblReservationCalendarBar insertDataReservationCalendarBAR(TblReservationCalendarBar calendarBar);
    
    public boolean updateDataReservationCalendarBAR(TblReservationCalendarBar calendarBar);
    
    public boolean deleteDataReservationCalendarBAR(TblReservationCalendarBar calendarBar);
    
    public TblReservationCalendarBar getReservationCalendarBAR(long id);
    
    public TblReservationCalendarBar getReservationCalendarBARByCalendarDate(Date calendarDate);
    
    public List<TblReservationCalendarBar> getAllDataReservationCalendarBAR();
    
    //--------------------------------------------------------------------------
    
    public TblReservationDefaultBar insertDataReservationDefaultBAR(TblReservationDefaultBar defaultBar);
    
    public boolean updateDataReservationDefaultBAR(TblReservationDefaultBar defaultBar);
    
    public boolean updateDataReservationDefaultBAR(TblReservationDefaultBar[] defaultBar);
    
    public boolean deleteDataReservationDefaultBAR(TblReservationDefaultBar defaultBar);
    
    public TblReservationDefaultBar getReservationDefaultBAR(long id);
    
    public TblReservationDefaultBar getReservationDefaultBARByDayOfWeek(int dayOfWeek);
    
    public List<TblReservationDefaultBar> getAllDataReservationDefaultBAR();
    
    //--------------------------------------------------------------------------
    
    public String getErrorMessage();
    
}
