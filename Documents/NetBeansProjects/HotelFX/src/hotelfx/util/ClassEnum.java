/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.util;

/**
 *
 * @author ANDRI
 */
public class ClassEnum {
    
    public enum dataInputStatus{
        
        DoNothing, DoAdd, DoUpdate, DoDelete;
    }
    
    public enum ApplicantStatus {

        Applicant, Employee;
    }

    public enum PeopleStatus {

        Menikah, Belum_Menikah;
    }

    public enum PeopleGender {

        Pria, Wanita;
    }

    public enum PeopleReligion {

        Islam, Kristen_Protestan, Katolik, Hindu, Buddha, Kong_Hu_Cu;
    }

    public enum EmployeeType {

        Tetap, Kontrak;
    }

    public enum SalaryChangeBy {

        Nominal, Percentase;
    }

    public enum LeaveType {

        Potong_Gaji,        //Potong Gaji
        Potong_Cuti,        //Potong Cuti, Tidak Potong Gaji
        Tidak_Ada_Potongan; //Tidak Dipotong Gaji maupun Cuti
    }

    public enum DayStatus {

        Kerja, Libur;
    }

    public enum DayOfWeek {

        Senin, Selasa, Rabu, Kamis, Jumat, Sabtu, Minggu;
    }

    //prioritas!
    public enum AttendanceStatus {

        Cuti, Regular, Regular_Full, TranShift, TranShift_Full, Lembur, Lembur_Full, Libur, Absent;
    }

    public enum PayrollMonthType {

        Bulan_Sebelum, Bulan_Penggajian;
    }

    public enum PayrollType {

        Payroll, Resign;
    }

    public enum ResignType {

        Pesiun, Mengundurkan_Diri, Diberhentikan;
    }

    public enum ApprovalStatus {

        Belum_Disetujui, Disetujui, Kadar_Luarsa, Ditolak;
    }

    public enum ItemCategory {

        Food_And_Beverage, Property_Barcode, Property_NonBarcode, UnKnown;   //property nonbarcode = barang
    }

    public enum ItemType {

        Habis_Pakai, Tidak_Habis_Pakai;
    }

    public enum CardType {

        NotYetPaid, Cash, Transfer, Debit, Credit, CekGiro;
    }

    public enum BrokenCase {

        Customer, Karyawan, Konsumsi, Expired;
    }

    public enum CleanStatus {

        CleanReady, Dirty, ReClean;
    }

    public enum RoomStatus {

        Vacant, Occupied, Do_Not_Disturb, OutOfService, OutOfOrder;
    }

    public enum RoomRateStatus {

        Available, NonAvailable;
    }

    public enum VoucherStatus {

        Available, NonAvailable;
    }

    public enum PaymentType {

        Tunai, Transfer, Kartu_Debit, Kartu_Kredit, Cek, Giro, Partner_Agent,
        Voucher, Draw_Deposit, OpenCash, OpenCard, CloseCash, CloseCard, 
        Retur, CancelingFee, ReturByCashier, ReturByFinance;
    }

    public enum TransactionType {

        Payable, Receivable, Moving, Nothing;    //Hutang dan Piutang, Perpindahan
    }

    public enum SettlementStatus {

        Unsettle, Settled;
    }

    public enum TransactionCreateBy {

        AnotherTransaction,
        ReservationTransaction, MITransaction, ReturTransacion,
        EmpDebtTransaction, EmpPayrollTransaction, EmpTHRTransaction,
        EmpRefundLeaveTransaction, EmpRefundResignTransaction,
        EDCChargeTransaction, EventCardTransaction, TravelAgentTransaction,
        ServiceChargeTransaction, PPNTransaction, PBBTransaction, PPHTransaction,
        CashierFisrtBalance, CashierMovingBalance,
        EDCFirstBalance, EDCMovingBalance,
        CompanyInjectionBalance, CompanyMovingBalance, POTransaction,
        RestoBreakfast, RestoFABOrder;
    }

    public enum ReservationPaymentStatus {

        Unconfirmed, Tentative, Confirmed, Skipped;
//        Unconfirmed, Tentative, Confirmed, Partner_Agent_Guarantee, Company_Guarantee, Compliment, Settled, Skiped;
    }

    public enum CheckInOutStatus {

        CheckIn, CheckOut, To_CheckIn, To_CheckOut;
    }

    //sorted by priority
    public enum ReservationStatus {

        Waiting_List, Reserved, Booked, Arrived, CheckIn, CheckOut, Canceled, Expired;
    }

    public enum ReservationDetailStatus {

        Reservation, Income, CancelingFee, CancelingWithOutFee;
    }

    //except settlement status
    public enum EDCTransactionStatus {

        Sale, Void, CardVer;
    }

    public enum EventBankCardType {

        Percentage, Nominal;
    }

    public enum TravelAgentCommissionType {

        Percentage, Nominal;
    }

    public enum RFIDCardStatus {

        Available, Used, NotAvailable;
    }

    public enum AccountStatus {
        //0, 1
        Plus, Minus;
    }

    public enum AccountJournalType{
        
        General, Adjustment, Closing, Revershing;
    }
    
    public enum BreakfastType {

        Buffet, Ala_carte;
    }

    public enum BreakfastVoucherType {

        IncludedRoomService, Additional;
    }

    public enum BreakfastVoucherStatus {

        //ready to issue : ready to print
        //ready to use : has been printed
        //used : has been used
        ReadyToIssue, ReadyToUse, Used;
    }

    public enum AssetType{
        
        Current_Asset, Fixed_Tangible_Asset, Fixed_Untangible_Asset, 
        Investation_Asset, Aonther_Asset;
    }
    
    public enum  FixedAssetDepreciationType{
        
        Depreciation_Plant, Undepreciated_Plant;
    }
    
    public enum JDCComponentType {

        INPUT, INFORMATION;
    }

    public enum JSComponentType {

        INPUT, INPUT_DATETIMEHm,
        INPUT_TIMEHm, INPUT_LIST,
        INPUT_NUMBER_INT, INPUT_NUMBER_DOUBLE,
        INPUT_NUMBER_DOUBLE_CURRENCY,
        INFORMATION, INFORMATION_DATETIMEHm,
        INFORMATION_TIMEHm, INFORMATION_LIST,
        INFORMATION_NUMBER_INT, INFORMATION_NUMBER_DOUBLE,
        INFORMATION_NUMBER_DOUBLE_CURRENCY;
    }

    public enum JTFComponentType {

        INPUT, INFORMATION;
    }
    
    public enum UserLockStatus{
        
        Locked, Unlocked;
    }
    
    public enum UserLoginStatus{
        
        Login, Logout;
    }
    
    public enum EmployeeStatus{
        
        Active, Inactive;
    }
    
    public enum RoomSmokingStatus{
        
        SmokingRoom, NoSmokingRoom;
    }
    
    public enum RecruitmentShowStatus{
        
        Show, DontShow;
    }
    
    public enum UpdateStockStatus{
        
        UpdateStock, NotUpdateStock;
    }
    
}
