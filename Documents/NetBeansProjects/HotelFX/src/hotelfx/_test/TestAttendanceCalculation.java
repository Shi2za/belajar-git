/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx._test;

import hotelfx._test.DataFromOutSource.AttendanceTypeFromOutSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author ANDRI
 */
public class TestAttendanceCalculation {

    private final String zoneID = "Asia/Ho_Chi_Minh";

    public TestAttendanceCalculation() {

    }

    private Employee getEmployee(String idEmployee) {
        if (idEmployee != null) {
            return new Employee(idEmployee, "Nama", "S01");
        }
        return null;
    }

    private ArrayList<DayOfWeek> getSchedule(String idSchedule) {
        if (idSchedule != null) {
            ArrayList<DayOfWeek> schedule = new ArrayList<>();
            for (int i = 1; i < 6; i++) {
                schedule.add(new DayOfWeek(
                        idSchedule,
                        i,
                        LocalTime.parse("09:00", DateTimeFormatter.ISO_TIME),
                        LocalTime.parse("17:00", DateTimeFormatter.ISO_TIME)));
            }
            return schedule;
        }
        return new ArrayList<>();
    }

    private void createDataAttendaceForBeginRegular(
            DataFromOutSource dataOutSource,
            ArrayList<DayOfWeek> schedule,
            EmployeeAttendance attendance) {
        int dayOfWeek = dataOutSource.getDataDateTime().getDayOfWeek().getValue();
        LocalDate temptDate;
        double realTimeMillis;
        double oldValueTimemillis;
        double newValueTimeMillis;
        //set begin real
        attendance.setBeginReal(dataOutSource.getDataDateTime());
        //set date attendance and begin schedule
        realTimeMillis = dataOutSource.getDataDateTime().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
        for (DayOfWeek sc : schedule) {
            if (dayOfWeek
                    != sc.getIdDayOfWeek()) {
                //one day before
                if (((dayOfWeek - 1) < 0
                        ? (dayOfWeek - 1) + 7
                        : dayOfWeek - 1)
                        == sc.getIdDayOfWeek()) {
                    temptDate = dataOutSource.getDataDateTime().toLocalDate().minusDays(1);
                    if (attendance.getBeginSchedule() != null) {
                        oldValueTimemillis = (LocalDateTime.of(attendance.getAttendanceDate(), attendance.getBeginSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                        newValueTimeMillis = (LocalDateTime.of(temptDate, sc.getBeginSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                        //better? --> replace old data with new value
                        if (Math.abs(newValueTimeMillis - realTimeMillis)
                                < Math.abs(oldValueTimemillis - realTimeMillis)) {
                            attendance.setAttendanceDate(temptDate);
                            attendance.setBeginSchedule(sc.getBeginSchedule());
                            attendance.setEndSchedule(sc.getEndSchedule());
                        }
                    } else {
                        attendance.setAttendanceDate(temptDate);
                        attendance.setBeginSchedule(sc.getBeginSchedule());
                        attendance.setEndSchedule(sc.getEndSchedule());
                    }
                } else {
                    //one day after
                    if (((dayOfWeek + 1) == 7
                            ? (dayOfWeek + 1) - 7
                            : dayOfWeek + 1)
                            == sc.getIdDayOfWeek()) {
                        temptDate = dataOutSource.getDataDateTime().toLocalDate().plusDays(1);
                        if (attendance.getBeginSchedule() != null) {
                            oldValueTimemillis = (LocalDateTime.of(attendance.getAttendanceDate(), attendance.getBeginSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                            newValueTimeMillis = (LocalDateTime.of(temptDate, sc.getBeginSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                            //better? --> replace old data with new value
                            if (Math.abs(newValueTimeMillis - realTimeMillis)
                                    < Math.abs(oldValueTimemillis - realTimeMillis)) {
                                attendance.setAttendanceDate(temptDate);
                                attendance.setBeginSchedule(sc.getBeginSchedule());
                                attendance.setEndSchedule(sc.getEndSchedule());
                            }
                        } else {
                            attendance.setAttendanceDate(temptDate);
                            attendance.setBeginSchedule(sc.getBeginSchedule());
                            attendance.setEndSchedule(sc.getEndSchedule());
                        }
                    }
                }
            } else {
                //same day
                temptDate = dataOutSource.getDataDateTime().toLocalDate();
                if (attendance.getBeginSchedule() != null) {
                    oldValueTimemillis = (LocalDateTime.of(attendance.getAttendanceDate(), attendance.getBeginSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                    newValueTimeMillis = (LocalDateTime.of(temptDate, sc.getBeginSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                    //better? --> replace old data with new value
                    if (Math.abs(newValueTimeMillis - realTimeMillis)
                            < Math.abs(oldValueTimemillis - realTimeMillis)) {
                        attendance.setAttendanceDate(temptDate);
                        attendance.setBeginSchedule(sc.getBeginSchedule());
                        attendance.setEndSchedule(sc.getEndSchedule());
                    }
                } else {
                    attendance.setAttendanceDate(temptDate);
                    attendance.setBeginSchedule(sc.getBeginSchedule());
                    attendance.setEndSchedule(sc.getEndSchedule());
                }
            }
        }
        //set attendance status
        attendance.setAttendanceStatus(AttendanceStatus.Regular);
    }

    private void createDataAttendaceForEndRegular(
            DataFromOutSource dataOutSource,
            ArrayList<DayOfWeek> schedule,
            EmployeeAttendance attendance) {
        int dayOfWeek = dataOutSource.getDataDateTime().getDayOfWeek().getValue();
        LocalDate temptDate;
        double realTimeMillis;
        double oldValueTimemillis;
        double newValueTimeMillis;
        //set end real
        attendance.setEndReal(dataOutSource.getDataDateTime());
        //set date attendance and end schedule
        realTimeMillis = dataOutSource.getDataDateTime().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
        for (DayOfWeek sc : schedule) {
            //if end > begin, must + one day
            int passDay = (sc.getBeginSchedule().isAfter(sc.getEndSchedule()) ? 1 : 0);
            int sdow = sc.getIdDayOfWeek()
                    + passDay;
            sdow = sdow == 7 ? 0 : sdow;
            if (dayOfWeek
                    != sdow) {
                //one day before
                if (((dayOfWeek - 1) < 0
                        ? (dayOfWeek - 1) + 7
                        : dayOfWeek - 1)
                        == sdow) {
                    temptDate = dataOutSource.getDataDateTime().toLocalDate().minusDays(1);
                    if (attendance.getEndSchedule() != null) {
                        oldValueTimemillis = (LocalDateTime.of(attendance.getAttendanceDate(), attendance.getEndSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                        newValueTimeMillis = (LocalDateTime.of(temptDate, sc.getEndSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                        //better? --> replace old data with new value
                        if (Math.abs(newValueTimeMillis - realTimeMillis)
                                < Math.abs(oldValueTimemillis - realTimeMillis)) {
                            attendance.setAttendanceDate(temptDate.minusDays(passDay));
                            attendance.setBeginSchedule(sc.getBeginSchedule());
                            attendance.setEndSchedule(sc.getEndSchedule());
                        }
                    } else {
                        attendance.setAttendanceDate(temptDate.minusDays(passDay));
                        attendance.setBeginSchedule(sc.getBeginSchedule());
                        attendance.setEndSchedule(sc.getEndSchedule());
                    }
                } else {
                    //one day after
                    if (((dayOfWeek + 1) == 7
                            ? (dayOfWeek + 1) - 7
                            : dayOfWeek + 1)
                            == sdow) {
                        temptDate = dataOutSource.getDataDateTime().toLocalDate().plusDays(1);

                        if (attendance.getEndSchedule() != null) {

                            oldValueTimemillis = (LocalDateTime.of(attendance.getAttendanceDate(), attendance.getEndSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                            newValueTimeMillis = (LocalDateTime.of(temptDate, sc.getEndSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                            //better? --> replace old data with new value
                            if (Math.abs(newValueTimeMillis - realTimeMillis)
                                    < Math.abs(oldValueTimemillis - realTimeMillis)) {
                                attendance.setAttendanceDate(temptDate.minusDays(passDay));
                                attendance.setBeginSchedule(sc.getBeginSchedule());
                                attendance.setEndSchedule(sc.getEndSchedule());
                            }
                        } else {
                            attendance.setAttendanceDate(temptDate.minusDays(passDay));
                            attendance.setBeginSchedule(sc.getBeginSchedule());
                            attendance.setEndSchedule(sc.getEndSchedule());
                        }
                    }
                }
            } else {
                //same day
                temptDate = dataOutSource.getDataDateTime().toLocalDate();
                if (attendance.getEndSchedule() != null) {
                    oldValueTimemillis = (LocalDateTime.of(attendance.getAttendanceDate(), attendance.getEndSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                    newValueTimeMillis = (LocalDateTime.of(temptDate, sc.getEndSchedule())).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant().toEpochMilli();
                    //better? --> replace old data with new value
                    if (Math.abs(newValueTimeMillis - realTimeMillis)
                            < Math.abs(oldValueTimemillis - realTimeMillis)) {
                        attendance.setAttendanceDate(temptDate.minusDays(passDay));
                        attendance.setBeginSchedule(sc.getBeginSchedule());
                        attendance.setEndSchedule(sc.getEndSchedule());
                    }
                } else {
                    attendance.setAttendanceDate(temptDate.minusDays(passDay));
                    attendance.setBeginSchedule(sc.getBeginSchedule());
                    attendance.setEndSchedule(sc.getEndSchedule());
                }
            }
        }
        //set attendance status
        attendance.setAttendanceStatus(AttendanceStatus.Regular);
    }

    private EmployeeAttendance createDataAttendanceFromOutsource(
            DataFromOutSource dataOutSource) {
        if (dataOutSource.getDataDateTime() != null) {
            Employee employee = getEmployee(dataOutSource.getIdEmployee());
            ArrayList<DayOfWeek> schedule = getSchedule(employee != null
                    ? employee.getIdSchedule() : null);
            if (!schedule.isEmpty()) {
                EmployeeAttendance attendance = new EmployeeAttendance(employee.getIdEmployee(),
                        null, null, null, null, null, null);
                switch (AttendanceTypeFromOutSource.valueOf(0)) {
//                    switch (dataOutSource.getAttendanceType()) {
                    case BeginRegular:
                        createDataAttendaceForBeginRegular(dataOutSource, schedule, attendance);
                        break;
                    case EndRegular:
                        createDataAttendaceForEndRegular(dataOutSource, schedule, attendance);
                        break;
                    case BeginOvertime:
                        break;
                    case EndOvertime:
                        break;
                    default :
                        attendance = null;
                        break;
                }
                return attendance;
            }
        }
        return null;
    }

    public void insertNewData(DataFromOutSource dataOutsource) {
        EmployeeAttendance attendance = createDataAttendanceFromOutsource(dataOutsource);
        if (attendance == null) {
            System.out.println("NULL");
        } else {
            System.out.println("IDEmployee : " + attendance.getIdEmployee());
            System.out.println("IDDate : " + attendance.getAttendanceDate());
            System.out.println("BeginSchedule : " + attendance.getBeginSchedule());
            System.out.println("EndSchedule : " + attendance.getEndSchedule());
            System.out.println("BeginReal : " + attendance.getBeginReal());
            System.out.println("EndReal : " + attendance.getEndReal());
            System.out.println("Status : " + attendance.getAttendanceStatus());
        }
    }

    class Employee {

        private String idEmployee;
        private String employeeName;

        private String idSchedule;

        public Employee(String idEmployee, String employeeName,
                String idScheddule) {
            this.idEmployee = idEmployee;
            this.employeeName = employeeName;
            this.idSchedule = idScheddule;
        }

        public String getIdEmployee() {
            return idEmployee;
        }

        public void setIdEmployee(String idEmployee) {
            this.idEmployee = idEmployee;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getIdSchedule() {
            return idSchedule;
        }

        public void setIdSchedule(String idSchedule) {
            this.idSchedule = idSchedule;
        }

    }

    class DayOfWeek {

        private String idSchedule;
        private int idDayOfWeek;

        private LocalTime beginSchedule;
        private LocalTime endSchedule;

        public DayOfWeek(String idSchedule, int idDayOfWeek,
                LocalTime beginSchedule, LocalTime endSchedule) {
            this.idSchedule = idSchedule;
            this.idDayOfWeek = idDayOfWeek;
            this.beginSchedule = beginSchedule;
            this.endSchedule = endSchedule;
        }

        public int getIdDayOfWeek() {
            return idDayOfWeek;
        }

        public void setIdDayOfWeek(int idDayOfWeek) {
            this.idDayOfWeek = idDayOfWeek;
        }

        public LocalTime getBeginSchedule() {
            return beginSchedule;
        }

        public void setBeginSchedule(LocalTime beginSchedule) {
            this.beginSchedule = beginSchedule;
        }

        public LocalTime getEndSchedule() {
            return endSchedule;
        }

        public void setEndSchedule(LocalTime endSchedule) {
            this.endSchedule = endSchedule;
        }

        public String getIdSchedule() {
            return idSchedule;
        }

        public void setIdSchedule(String idSchedule) {
            this.idSchedule = idSchedule;
        }

    }

    class EmployeeAttendance {

        private String idEmployee;
        private LocalDate attendanceDate;

        private LocalTime beginSchedule;
        private LocalTime endSchedule;

        private LocalDateTime beginReal;
        private LocalDateTime endReal;

        private AttendanceStatus attendanceStatus;

        public EmployeeAttendance(String idEmployee, LocalDate attendanceDate,
                LocalTime beginSchedule, LocalTime endSchedule,
                LocalDateTime beginReal, LocalDateTime endReal,
                AttendanceStatus attendanceStatus) {
            this.idEmployee = idEmployee;
            this.attendanceDate = attendanceDate;
            this.beginSchedule = beginSchedule;
            this.endSchedule = endSchedule;
            this.beginReal = beginReal;
            this.endReal = endReal;
            this.attendanceStatus = attendanceStatus;
        }

        public String getIdEmployee() {
            return idEmployee;
        }

        public void setIdEmployee(String idEmployee) {
            this.idEmployee = idEmployee;
        }

        public LocalDate getAttendanceDate() {
            return attendanceDate;
        }

        public void setAttendanceDate(LocalDate attendanceDate) {
            this.attendanceDate = attendanceDate;
        }

        public LocalTime getBeginSchedule() {
            return beginSchedule;
        }

        public void setBeginSchedule(LocalTime beginSchedule) {
            this.beginSchedule = beginSchedule;
        }

        public LocalTime getEndSchedule() {
            return endSchedule;
        }

        public void setEndSchedule(LocalTime endSchedule) {
            this.endSchedule = endSchedule;
        }

        public LocalDateTime getBeginReal() {
            return beginReal;
        }

        public void setBeginReal(LocalDateTime beginReal) {
            this.beginReal = beginReal;
        }

        public LocalDateTime getEndReal() {
            return endReal;
        }

        public void setEndReal(LocalDateTime endReal) {
            this.endReal = endReal;
        }

        public AttendanceStatus getAttendanceStatus() {
            return attendanceStatus;
        }

        public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
            this.attendanceStatus = attendanceStatus;
        }

    }

    public enum scheduleType {

        ScheduleOfWeek, ScheduleOfDay;
    }

    public enum AttendanceStatus {

        Annual_Leave, Conditional_Leave,
        Regular, Regular_Full,
        TranShift, TranShift_Full,
        Overtime, Overtime_Full,
        DayOff, Absent;
    }

    //in minutes
    private static long beginEarlyTolerance = 30;
    private static long beginLateTolerance = 15;
    private static long endEarlyTolerance = 15;
    private static long endLateTolerance = 30;

//    public static void main(String[] args) {
//        TestAttendanceCalculation calculation = new TestAttendanceCalculation();
//        DataFromOutSource dataOutsource = new DataFromOutSource(
//                "Hana",
//                DataFromOutSource.AttendanceTypeFromOutSource.BeginRegular,
//                LocalDateTime.of(2017, Month.APRIL, 11, 22, 0));
//        calculation.insertNewData(dataOutsource);
//    }

}

class DataFromOutSource {

    private String idEmployee;
    private AttendanceTypeFromOutSource attendanceType;

    private LocalDateTime dataDateTime;

    public DataFromOutSource(String idEmployee,
            AttendanceTypeFromOutSource attendanceType,
            LocalDateTime dataDateTime) {
        this.idEmployee = idEmployee;
        this.attendanceType = attendanceType;
        this.dataDateTime = dataDateTime;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public AttendanceTypeFromOutSource getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceTypeFromOutSource attendanceType) {
        this.attendanceType = attendanceType;
    }

    public LocalDateTime getDataDateTime() {
        return dataDateTime;
    }

    public void setDataDateTime(LocalDateTime dataDateTime) {
        this.dataDateTime = dataDateTime;
    }

    public enum AttendanceTypeFromOutSource {

        BeginRegular(0), EndRegular(1), BeginOvertime(2), EndOvertime(3);
        
        private final int value;
        
        private AttendanceTypeFromOutSource(int value){
            this.value = value;
        }
        
        public int getValue(){
            return this.value;
        }
        
        public static AttendanceTypeFromOutSource valueOf(int value){
            for(AttendanceTypeFromOutSource data : AttendanceTypeFromOutSource.values()){
                if(data.getValue() == value){
                    return data;
                }
            }
            return null;
        }
        
    }

}
