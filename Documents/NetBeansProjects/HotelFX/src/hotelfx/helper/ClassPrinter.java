/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

//import hotelfx.persistence.HibernateUtil;
import hotelfx.helper.PrintModel.ClassPrintAttendance;
import hotelfx.helper.PrintModel.ClassPrintBill;
import hotelfx.helper.PrintModel.ClassPrintCustomer;
import hotelfx.helper.PrintModel.ClassPrintDataCodeSetting;
import hotelfx.helper.PrintModel.ClassPrintDataDefault;
import hotelfx.helper.PrintModel.ClassPrintDataGedung;
import hotelfx.helper.PrintModel.ClassPrintDataGroup;
import hotelfx.helper.PrintModel.ClassPrintDataHutangKaryawan;
import hotelfx.helper.PrintModel.ClassPrintDataJabatan;
import hotelfx.helper.PrintModel.ClassPrintDataLantai;
import hotelfx.helper.PrintModel.ClassPrintDataLaporanReservasi;
import hotelfx.helper.PrintModel.ClassPrintDataMaterialRequest;
import hotelfx.helper.PrintModel.ClassPrintDataRetur;
import hotelfx.helper.PrintModel.ClassPrintInvoiceHotel;
import hotelfx.helper.PrintModel.ClassPrintInvoiceRetur;
import hotelfx.helper.PrintModel.ClassPrintKuitansiPembayaranInvoiceHotel;
import hotelfx.helper.PrintModel.ClassPrintLaporanBarangHilang;
import hotelfx.helper.PrintModel.ClassPrintLaporanCheckIn;
import hotelfx.helper.PrintModel.ClassPrintLaporanCheckOut;
import hotelfx.helper.PrintModel.ClassPrintLaporanHutangKaryawan;
import hotelfx.helper.PrintModel.ClassPrintLaporanMasukKeluarBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanPembelianBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanPendapatanKamar;
import hotelfx.helper.PrintModel.ClassPrintLaporanPendapatanTambahan;
import hotelfx.helper.PrintModel.ClassPrintLaporanOccupancyKamar;
import hotelfx.helper.PrintModel.ClassPrintLaporanPenerimaanBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanPenerimaanPO;
import hotelfx.helper.PrintModel.ClassPrintLaporanPengeluaran;
import hotelfx.helper.PrintModel.ClassPrintLaporanReturBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueBulanan;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueHarian;
import hotelfx.helper.PrintModel.ClassPrintLaporanRevenueTahunan;
import hotelfx.helper.PrintModel.ClassPrintLaporanRusakBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanStokOpname;
import hotelfx.helper.PrintModel.ClassPrintLaporanTemuanBarang;
import hotelfx.helper.PrintModel.ClassPrintLaporanTransaksiArusKas;
import hotelfx.helper.PrintModel.ClassPrintPurchaseOrder;
import hotelfx.helper.PrintModel.ClassPrintReceiving;
import hotelfx.helper.PrintModel.ClassPrintSchedule;
import hotelfx.helper.PrintModel.ClassPrintStoreRequest;
import hotelfx.helper.PrintModel.ClassPrintSuratJalan;
import hotelfx.helper.PrintModel.ClassPrintWorkSheetHouseKeeping;
import hotelfx.persistence.DBConnectionSetting;
import hotelfx.persistence.model.RefCountry;
import hotelfx.persistence.model.RefCustomerType;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblCustomer;
import hotelfx.persistence.model.TblPurchaseOrder;
import hotelfx.persistence.model.TblPurchaseRequest;
import hotelfx.persistence.model.TblReservation;
import hotelfx.persistence.model.TblReservationBill;
import hotelfx.persistence.model.TblRetur;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingNode;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author ANDRI
 */
public class ClassPrinter {

    private static Map parameter = new HashMap();

    //--------------------------------------------------------------------------
    public static void printReport() {
        String fileName = "ReportName";
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();

//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
//        parameter.put("parameterName", "Value");
//        try {
//            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//
//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);
//            //PDF
//            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + fileName + ".pdf";
//            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
//        } catch (JRException e) {
//            System.out.println(e.toString());
//        }
        //show print preview data report
        showPrintPreView(fileName);
    }

    public static void printBon(String hotelName,
            String hotelAddress,
            String hotelPhoneNumber,
            String hotelEmail,
            String hotelLogoName,
            TblReservationBill dataReservationBill,
            int language) {
        String fileName = "BonReservasiRevisi";
        String pdfFileName = "Bill " + dataReservationBill.getRefReservationBillType().getTypeName() + " - "
                + ClassFormatter.dateFormate.format(dataReservationBill.getTblReservation().getReservationDate()) + " - "
                + dataReservationBill.getTblReservation().getTblCustomer().getCodeCustomer();

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
        parameter.put("NamaHotel", hotelName);
        parameter.put("AlamatHotel", hotelAddress);
        parameter.put("NoTeleponHotel", hotelPhoneNumber);
        parameter.put("emailHotel", hotelEmail);
//        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName + ".jpg");
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
        parameter.put("idReservation", dataReservationBill.getTblReservation().getIdreservation());
        parameter.put("billType", dataReservationBill.getRefReservationBillType().getIdtype());
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");
        parameter.put("idLanguage", language);

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connectionDB());

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    }
    
    public static String nameFileBillReservasi(List<ClassPrintBill>listPrintBill,
            TblReservationBill dataReservationBill) {
        String fileName = "BonReservasiRevisi";
        String result = "";
        String pdfFileName = "Invoice#"+dataReservationBill.getCodeBill();

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listPrintBill);
        getDataHotel(parameter);
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
       
//        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName + ".jpg");
        
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");
      

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            
           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);
           // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connectionDB());

            //PDF
            
      
            File pdfFileExist = new File(ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf");
            if(pdfFileExist.exists()){
               result= pdfFileExist.getPath();
            }
            else{
             String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
             JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
             
             result = pdfFile;
            }
           // JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        return result;
    }
     
    public static void printReceipt(String hotelName,
            String hotelAddress,
            String hotelPhoneNumber,
            String hotelLogoName,
            TblReservationBill dataReservationBill,
            int language) {
        String fileName = "KuitansiHotel";
        String pdfFileName = "Kuitansi"
                + dataReservationBill.getRefReservationBillType().getTypeName()
                + " - "
                + ClassFormatter.dateFormate.format(dataReservationBill.getTblReservation().getReservationDate()) + " - "
                + dataReservationBill.getTblReservation().getTblCustomer().getCodeCustomer();

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
        parameter.put("namaHotel", hotelName);
        parameter.put("alamatHotel", hotelAddress);
        parameter.put("noTeleponHotel", hotelPhoneNumber);
//        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName + ".jpg");
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
        parameter.put("idReservation", dataReservationBill.getTblReservation().getIdreservation());
        parameter.put("billType", dataReservationBill.getRefReservationBillType().getIdtype());
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");
        parameter.put("idLanguage", language);

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connectionDB());

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
//        showPrintPreView(pdfFileName);
    }

     public static void printBillReservasi(List<ClassPrintBill>listPrintBill,
            TblReservationBill dataReservationBill) {
        String fileName = "BonReservasiRevisi";
        String pdfFileName = "Invoice#"+dataReservationBill.getCodeBill();

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listPrintBill);
        getDataHotel(parameter);
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
       
//        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName + ".jpg");
        
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");
      

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            
           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);
           // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connectionDB());

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    }
     
   
    public static void printCanceledReceipt(String hotelName,
            String hotelAddress,
            String hotelPhoneNumber,
            String hotelLogoName,
            TblReservation dataReservation,
            int language) {
        String fileName = "KuitansiCancelHotel";
        String pdfFileName = "Kuitansi-Pembatalan"
                + " - "
                + ClassFormatter.dateFormate.format(dataReservation.getReservationDate()) + " - "
                + dataReservation.getTblCustomer().getCodeCustomer();

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
        parameter.put("namaHotel", hotelName);
        parameter.put("alamatHotel", hotelAddress);
        parameter.put("noTeleponHotel", hotelPhoneNumber);

//        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName + ".jpg");
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
        parameter.put("idReservation", dataReservation.getIdreservation());
        parameter.put("billType", 0);   //...
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");
        parameter.put("idLanguage", language);

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connectionDB());

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    }

 /*   public static void printPR(String hotelName,
            String hotelAddress,
            String hotelPhoneNumber,
            String hotelLogoName,
            TblPurchaseRequest dataPR) {
        String fileName = "PrintPurchaseRequest";
        String pdfFileName = "PurchaseRequest -"
                + ClassFormatter.dateFormate.format(dataPR.getPrdate()) + " - "
                + dataPR.getIdpr();

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
        parameter.put("namaHotel", hotelName);
        parameter.put("alamatHotel", hotelAddress);
        parameter.put("noTeleponHotel", hotelPhoneNumber);
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
        parameter.put("idPR", dataPR.getIdpr());
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connectionDB());

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    } */
    
      public static void printPR(List<ClassPrintDataMaterialRequest>listMaterialRequest,String codePR) {
        String fileName = "PrintPurchaseRequest";
        String pdfFileName =codePR;

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listMaterialRequest);
        getDataHotel(parameter);
        parameter.put("listMaterialRequest", itemJRBean);
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
     /*   parameter.put("namaHotel", hotelName);
        parameter.put("alamatHotel", hotelAddress);
        parameter.put("noTeleponHotel", hotelPhoneNumber);
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
        parameter.put("idPR", dataPR.getIdpr()); */
        
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    }
      
    public static void printPO(List<ClassPrintPurchaseOrder>listPurchaseOrder,TblPurchaseOrder dataPO,String revisi) {
        String fileName = "PrintPurchaseOrder";
        String pdfFileName = dataPO.getCodePo()+(revisi.equals("")?"":"-"+revisi);

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listPurchaseOrder);
        getDataHotel(parameter);
        parameter.put("listPurchaseOrder", itemJRBean);
         parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            System.out.println(ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper");
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    }

    public static void printRetur(List<ClassPrintDataRetur>listRetur,String kodeRetur) {
        String fileName = "PrintRetur";
        String pdfFileName =kodeRetur;

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listRetur);
        getDataHotel(parameter);
        parameter.put("listRetur", itemJRBean);
         parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
      /*  parameter.put("namaHotel", hotelName);
        parameter.put("alamatHotel", hotelAddress);
        parameter.put("noTeleponHotel", hotelPhoneNumber);
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
        parameter.put("idRetur", dataRetur.getIdretur());
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/"); */

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            System.out.println(ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper");
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    }

    /*  public static void printInvoiceHotel(String logoHotelName,
     TblHotelInvoice dataInvoice) {
     String fileName = "InvoiceHotel";
     String pdfFileName = "Invoice-Partner -"
     + ClassFormatter.dateFormate.format(dataInvoice.getCreateDate()) + " - "
     + dataInvoice.getTblPartner().getPartnerName() + " - "
     + dataInvoice.getIdhotelInvoice();

     parameter = new HashMap();

     parameter.put("idInvoice", dataInvoice.getIdhotelInvoice());
     parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + logoHotelName);
     parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

     String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
     try {
     JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);
     jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
     "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
     JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
     "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

     JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connectionDB());

     String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
     JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
     } catch (JRException ex) {
     Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
     }

     showPrintPreView(pdfFileName);
     } */
    
     public static void printDeliveryNumber(List<ClassPrintSuratJalan>listSuratJalan,String kodeSuratJalan) {
        String fileName = "PrintSuratJalan";
        String pdfFileName =kodeSuratJalan;

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listSuratJalan);
        getDataHotel(parameter);
        parameter.put("listRetur", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
      /*  parameter.put("namaHotel", hotelName);
        parameter.put("alamatHotel", hotelAddress);
        parameter.put("noTeleponHotel", hotelPhoneNumber);
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
        parameter.put("idRetur", dataRetur.getIdretur());
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/"); */

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            System.out.println(ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper");
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    }
    
     public static void printStoreRequest(List<ClassPrintStoreRequest>listStoreRequest,String kodeStoreRequest) {
        String fileName = "PrintStoreRequest";
        String pdfFileName =kodeStoreRequest;

//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //set data parameter
        parameter = new HashMap();
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listStoreRequest);
        getDataHotel(parameter);
        parameter.put("listStoreRequest", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");
//        parameter.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);
      /*  parameter.put("namaHotel", hotelName);
        parameter.put("alamatHotel", hotelAddress);
        parameter.put("noTeleponHotel", hotelPhoneNumber);
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
        parameter.put("idRetur", dataRetur.getIdretur());
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/"); */

        try {
            //create report
//            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jrxml";
            System.out.println(ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper");
            String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
//            String reportDest = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

            JasperReport jasperReport = (JasperReport)JRLoader.loadObjectFromFile(reportSource);

//            JasperReport jasperReport = JasperCompileManager.compileReport((reportSource));
            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);

            //PDF
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException e) {
            System.out.println(e.toString());
        }
        //show print preview data report
        showPrintPreView(pdfFileName);
    }
     
    public static void printInvoiceHotel(List<ClassPrintInvoiceHotel> listHotelInvoice, String codeInvoice, String partner, String dateInvoice) {
        String fileName = "InvoiceHotel";
        String pdfFileName = "Invoice-Partner -" + codeInvoice + "-" + partner + "-" + dateInvoice;

        parameter = new HashMap();
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listHotelInvoice);
        parameter = new HashMap();
        getDataHotel(parameter);
        parameter.put("listInvoiceHotel", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        showPrintPreView(pdfFileName);
    }

    public static void printInvoiceRetur(List<ClassPrintInvoiceRetur> listInvoiceRetur, String supplier, String codeInvoice, String invoiceDate) {
        String fileName = "InvoiceRetur";
        String pdfFileName = "Invoice-Retur -" + codeInvoice + "-" + supplier + "-" + invoiceDate;

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listInvoiceRetur);
        parameter = new HashMap();
        getDataHotel(parameter);
        parameter.put("listInvoiceRetur", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);

            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        showPrintPreView(pdfFileName);
    }

    public static void printKuitansiPartner(List<ClassPrintKuitansiPembayaranInvoiceHotel> listPembayaranInvoiceHotel, String kodeTransaksi, String partner, String tanggalBayar) {
        String fileName = "KuitansiPembayaranInvoice";
        String pdfFileName = "Kuitansi-Invoice-Partner -"
                + kodeTransaksi + " - " + partner + " - " + tanggalBayar;

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listPembayaranInvoiceHotel);
        parameter = new HashMap();
        getDataHotel(parameter);
        parameter.put("listPembayaranInvoiceHotel", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);

            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        showPrintPreView(pdfFileName);
    }

    public static void printKuitansiRetur(String logoHotelName,
            TblRetur dataReceipt) {
        String fileName = "KuitansiInvoiceRetur";
        String pdfFileName = "Kuitansi-Invoice-Retur -"
                + ClassFormatter.dateFormate.format(dataReceipt.getCreateDate()) + " - "
                + dataReceipt.getTblSupplier().getSupplierName() + " - "
                + dataReceipt.getIdretur();

        parameter = new HashMap();
        parameter.put("idRetur", dataReceipt.getIdretur());
        parameter.put("logoImage", ClassFolderManager.imageSystemRootPath + "/" + logoHotelName);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, connectionDB());

            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        showPrintPreView(pdfFileName);
    }

    public static void printAttendance(List<ClassPrintAttendance> printAttendance, Date startDate, Date endDate) {
        String fileName = "EmployeeAttendance";
        String pdfFileName = "Absensi Karyawan Periode " + new SimpleDateFormat("dd MMM yyyy").format(startDate) + " - " + new SimpleDateFormat("dd MMM yyyy").format(endDate);

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printAttendance);
        parameter = new HashMap();
        parameter.put(fileName, fileName);

        getDataHotel(parameter);
        parameter.put("ListAttendance", itemJRBean);

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            /* jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
             "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
             JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
             "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");*/
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, new JREmptyDataSource());

            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        showPrintPreView(pdfFileName);
    }

    public static SwingNode printSchedule(Date startDate, Date endDate, List<ClassPrintSchedule> listSchedule) {
        String fileName = "EmployeeScheduleUpdate";
        String pdfFileName = "Jadwal Karyawan Periode" + startDate + "-" + endDate;
        //JasperPrint jasperPrint = new JasperPrint();
        SwingNode swingNode = new SwingNode();

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listSchedule);
        parameter = new HashMap();
        if (endDate != null) {
            parameter.put("periodeJadwal", new SimpleDateFormat("dd MMMM yyyy").format(startDate) + " - " + new SimpleDateFormat("dd MMMM yyyy").format(endDate));
        } else {
            parameter.put("periodeJadwal", new SimpleDateFormat("dd MMMM yyyy").format(startDate));
        }

        //parameter.put("endDate", endDate);
        //parameter.put("tipeEmployee", employeeType.getIdtype());
        getDataHotel(parameter);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            JRViewer jview = new JRViewer(jasperPrint);
            //jview.setVisible(true);
            swingNode.setContent(jview);
            // String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            // JasperExportManager.exportReportToPdfFile(jasperPrint,pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        //showPrintPreView(pdfFileName);
        return swingNode;
    }

    public static SwingNode printCustomer(List<ClassPrintCustomer> printCustomer, Date startDate, Date endDate, RefCustomerType customerType, TblCustomer customer, RefCountry country) {
        String fileName = "LaporanCustomerUpdate";
        String pdfFileName = "Laporan Customer Periode " + new SimpleDateFormat("dd MMM yyyy").format(startDate) + " - " + new SimpleDateFormat("dd MMM yyyy").format(endDate);
        SwingNode swingNode = new SwingNode();
        String periodeRegistrasi = new SimpleDateFormat("dd MMMM yyyy").format(startDate) + " - " + new SimpleDateFormat("dd MMMM yyyy").format(endDate);

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printCustomer);
        parameter = new HashMap();
        parameter.put(fileName, fileName);

        getDataHotel(parameter);
        parameter.put("ListCustomer", itemJRBean);
        parameter.put("periodeRegistrasi", periodeRegistrasi);
        parameter.put("tipeCustomer", customerType == null ? "-" : customerType.getTypeName());
        parameter.put("namaCustomer", customer == null ? "-" : customer.getTblPeople().getFullName());
        parameter.put("negara", country == null ? "-" : country.getCountryName());
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            /* jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
             "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
             JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
             "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");*/
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);

            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            //String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            //JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return swingNode;
        //showPrintPreView(pdfFileName);
    }

    public static void printGedung(List<ClassPrintDataGedung> listGedung) {
        String fileName = "DataGedung";
        String pdfFileName = "DataGedung";
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listGedung);
        parameter = new HashMap();
        getDataHotel(parameter);

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";
        SwingNode swingNode = new SwingNode();
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);

            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
            //JRViewer jrView = new JRViewer(jasperPrint);
            //swingNode.setContent(jrView);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }

    public static void printDataLantai(List<ClassPrintDataLantai> listLantai) {
        String fileName = "DataLantai";
        String pdfFileName = "DataLantai";
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listLantai);
        parameter = new HashMap();
        getDataHotel(parameter);

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + fileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        showPrintPreView(pdfFileName);

    }

    public static void printDataCodeSetting(List<ClassPrintDataCodeSetting> listCodeSetting) {
        String fileName = "DataCodeSetting";
        String pdfFileName = "DataCodeSetting";

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listCodeSetting);
        parameter = new HashMap();
        getDataHotel(parameter);

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + fileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }

    public static void printDataDefault(List<ClassPrintDataDefault> listDataDefault) {
        String fileName = "DataDefault";
        String pdfFileName = "DataDefault";

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listDataDefault);
        parameter = new HashMap();
        getDataHotel(parameter);

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + fileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }

    public static void printDataJabatan(List<ClassPrintDataJabatan> listDataJabatan) {
        String fileName = "DataJabatan";
        String pdfFileName = "DataJabatan";

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listDataJabatan);
        parameter = new HashMap();
        getDataHotel(parameter);
        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }

    public static void printDataGroup(List<ClassPrintDataGroup> listDataGroup) {
        String fileName = "DataGroup";
        String pdfFileName = "DataGroup";

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(listDataGroup);
        parameter = new HashMap();
        getDataHotel(parameter);
        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);
        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }

    public static SwingNode printReportReservation(List<ClassPrintDataLaporanReservasi> printReservasi, String periode) {
        String fileName = "LaporanReservasi";
        String pdfFileName = "Laporan Reservasi Periode " + periode;
        SwingNode swingNode = new SwingNode();

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printReservasi);

        getDataHotel(parameter);
        parameter.put("periodeReservasi", periode);
        parameter.put("ListLaporanReservasi", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            //String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            // JasperExportManager.exportReportToPdfFile(jasperPrint,pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return swingNode;
        //  showPrintPreView(pdfFileName);
    }

    public static SwingNode printReportRoomIncome(List<ClassPrintLaporanPendapatanKamar> printLaporanPendapatanKamar,
            String periode, BigDecimal totalHargaKamar, BigDecimal totalDiskon,
            BigDecimal totalCompliment, BigDecimal totalServiceCharge, BigDecimal totalPajak) {
        String fileName = "LaporanPendapatanKamar";
        String pdfFileName = "Laporan Pendapatan Kamar periode - " + periode;
        SwingNode swingNode = new SwingNode();

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanPendapatanKamar);

            getDataHotel(parameter);
            parameter.put("ListLaporanPendapatanKamar", itemJRBean);
            parameter.put("periode", periode);
            parameter.put("totalHargaKamar", totalHargaKamar);
            parameter.put("totalDiskon", totalDiskon);
            parameter.put("totalCompliment", totalCompliment);
            parameter.put("totalServiceCharge", totalServiceCharge);
            parameter.put("totalPajak", totalPajak);
            parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
         //  String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            //  JasperExportManager.exportReportToPdfFile(jasperPrint,pdfFile);

            //  JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return swingNode;
        //  showPrintPreView(pdfFileName);
    }

    public static SwingNode printReportAdditionalIncome(List<ClassPrintLaporanPendapatanTambahan> printLaporanPendapatanTambahan,
            String periode, BigDecimal totalHarga, BigDecimal totalDiskon, BigDecimal totalServiceCharge, BigDecimal totalPajak) {
        String fileName = "LaporanPendapatanTambahanLayanandanBarang";
        String pdfFileName = "Laporan Pendapatan Tambahan periode - " + periode;
        SwingNode swingNode = new SwingNode();

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanPendapatanTambahan);

            getDataHotel(parameter);
            parameter.put("listPendapatanTambahan", itemJRBean);
            //    parameter.put("totalHargaKamar", totalHargaKamar);
            parameter.put("periode", periode);
            parameter.put("totalHarga", totalHarga);
            parameter.put("totalDiskon", totalDiskon);
            //    parameter.put("totalCompliment", totalCompliment);
            parameter.put("totalServiceCharge", totalServiceCharge);
            parameter.put("totalPajak", totalPajak);
            parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
           // String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            // JasperExportManager.exportReportToPdfFile(jasperPrint,pdfFile);

            //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return swingNode;
    }
    
      public static SwingNode printReportRevenueDaily(List<ClassPrintLaporanRevenueHarian> printLaporanRevenueHarian,String tanggal) {
        String fileName = "LaporanRevenueHarian";
        String pdfFileName = "Laporan Revenue " + tanggal;
        SwingNode swingNode = new SwingNode();

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanRevenueHarian);

            getDataHotel(parameter);
            parameter.put("listRevenueHarian", itemJRBean);
            //    parameter.put("totalHargaKamar", totalHargaKamar);
            parameter.put("tanggal",tanggal);
            parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        //    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
        //    String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
        //    JasperExportManager.exportReportToPdfFile(jasperPrint,pdfFile);

         JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);
         JRViewer jrView = new JRViewer(jasperPrint);
         swingNode.setContent(jrView);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return swingNode;
     //   showPrintPreView(pdfFileName);
    }
      
     public static SwingNode printReportRevenueMonthly(List<ClassPrintLaporanRevenueBulanan> printLaporanRevenueBulanan,String month) {
        String fileName = "LaporanRevenueBulanan";
        String pdfFileName = "Laporan Revenue Bulanan" + month;
        SwingNode swingNode = new SwingNode();

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanRevenueBulanan);

            getDataHotel(parameter);
            parameter.put("listRevenueBulanan", itemJRBean);
            //    parameter.put("totalHargaKamar", totalHargaKamar);
            parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        //    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
        //    String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
        //    JasperExportManager.exportReportToPdfFile(jasperPrint,pdfFile);

           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);
           JRViewer jrView = new JRViewer(jasperPrint);
           swingNode.setContent(jrView);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return swingNode;
       // showPrintPreView(pdfFileName);
    }
    
    public static SwingNode printReportRevenueYearly(List<ClassPrintLaporanRevenueTahunan> printLaporanRevenueTahunan,String year) {
        String fileName = "LaporanRevenueTahunan";
        String pdfFileName = "Laporan Revenue Tahun "+year;
        SwingNode swingNode = new SwingNode();

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanRevenueTahunan);

            getDataHotel(parameter);
            parameter.put("listRevenueTahunan", itemJRBean);
            //    parameter.put("totalHargaKamar", totalHargaKamar);
            parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

         //  JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
         //  String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
        //   JasperExportManager.exportReportToPdfFile(jasperPrint,pdfFile);

         JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter,itemJRBean);
         JRViewer jrView = new JRViewer(jasperPrint);
         swingNode.setContent(jrView);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return swingNode;
      //  showPrintPreView(pdfFileName);
    }
       
    public static void printReceiving(List<ClassPrintReceiving> printReceiving, String kodeReceiving) {
        String fileName = "PrintMemorandumInvoice";
        String pdfFileName = "Receiving - " + kodeReceiving;

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printReceiving);

        getDataHotel(parameter);
        parameter.put("ListReceiving", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }
    
        public static void printWorkSheetHouseKeeping(List<ClassPrintWorkSheetHouseKeeping> printWorkSheet,String houseKeeping) {
        String fileName = "WorkSheetHouseKeeping";
        String pdfFileName = "Lembar Kerja - " + houseKeeping;

        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printWorkSheet);

        getDataHotel(parameter);
        parameter.put("ListWorkSheetHouseKeeping", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }
    
    public static void printLaporanTransaksiArusKas(List<ClassPrintLaporanTransaksiArusKas>printTransaksiArusKas,String periode,String kas){
       String fileName = "LaporanTransaksiArusKas";
       String pdfFileName = "Laporan Arus Kas tanggal "+periode+" "+kas;
       
       
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printTransaksiArusKas);

        getDataHotel(parameter);
        parameter.put("ListLaporanTransaksiArusKas", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }
    
      public static SwingNode printLaporanMasukKeluarBarang(List<ClassPrintLaporanMasukKeluarBarang>printLaporanMasukKeluarBarang,ClassPrintLaporanMasukKeluarBarang laporanMasukKeluarBarang){
       String fileName = "LaporanMasukKeluarBarang";
       String pdfFileName = "Laporan Masuk Keluar Barang tanggal "+laporanMasukKeluarBarang.getPeriode()+"-"+laporanMasukKeluarBarang.getGudang();
       SwingNode swingNode = new SwingNode();
       
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanMasukKeluarBarang);

        getDataHotel(parameter);
        parameter.put("ListLaporanMasukKeluarBarang", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
         //   String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
         //   JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return swingNode;
       // showPrintPreView(pdfFileName);
    }
    
     
    public static void printDataHutangKaryawan(List<ClassPrintDataHutangKaryawan>printDataHutangKaryawan){
       String fileName = "DataHutangKaryawan";
       String pdfFileName = "Data Hutang Karyawan ";
       
       
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printDataHutangKaryawan);

        getDataHotel(parameter);
        parameter.put("ListDataHutangKaryawan", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        showPrintPreView(pdfFileName);
    }
    
     public static SwingNode printLaporanHutangKaryawan(List<ClassPrintLaporanHutangKaryawan>printLaporanHutangKaryawan){
       String fileName = "LaporanHutangKaryawan";
       String pdfFileName = "Laporan Hutang Karyawan ";
       SwingNode swingNode = new SwingNode();
       
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanHutangKaryawan);

        getDataHotel(parameter);
        parameter.put("ListDataHutangKaryawan", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
         //   String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
         //   JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return swingNode;
      //  showPrintPreView(pdfFileName);
    }
    
    public static SwingNode printLaporanPembelianBarang(List<ClassPrintLaporanPembelianBarang>printLaporanPembelianBarang){
       String fileName = "LaporanPembelianBarang";
       String pdfFileName = "Laporan Pembelian Barang ";
       SwingNode swingNode = new SwingNode();
       
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanPembelianBarang);

        getDataHotel(parameter);
        parameter.put("ListLaporanPembelianBarang", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
           // JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return swingNode;
      //  showPrintPreView(pdfFileName);
    }
    
    public static SwingNode printLaporanCheckIn(List<ClassPrintLaporanCheckIn>printLaporanCheckIn){
      String fileName = "LaporanCheckIn";
      String pdfFileName = "Laporan CheckIn";
      SwingNode swingNode = new SwingNode();
      
        JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanCheckIn);

        getDataHotel(parameter);
        parameter.put("ListLaporanPembelianBarang", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return swingNode;
     // showPrintPreView(pdfFileName);
    }
    
     public static SwingNode printLaporanBarangHilang(List<ClassPrintLaporanBarangHilang>printLaporanBarangHilang){
      String fileName = "LaporanBarangHilang";
      String pdfFileName = "Laporan Barang Hilang";
      SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanBarangHilang);

        getDataHotel(parameter);
        parameter.put("ListLaporanBarangHilang", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return swingNode;
      //  showPrintPreView(pdfFileName);
    }
    
     
     public static SwingNode printLaporanTemuanBarang(List<ClassPrintLaporanTemuanBarang>printLaporanTemuanBarang){
      String fileName = "LaporanTemuanBarang";
      String pdfFileName = "Laporan Temuan Barang";
      SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanTemuanBarang);

        getDataHotel(parameter);
        parameter.put("ListLaporanTemuanBarang", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return swingNode;
      //  showPrintPreView(pdfFileName);
    }
    
     
     public static SwingNode printLaporanCheckOut(List<ClassPrintLaporanCheckOut>printLaporanCheckOut){
      String fileName = "LaporanCheckOut";
      String pdfFileName = "Laporan CheckOut";
      SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanCheckOut);

        getDataHotel(parameter);
        parameter.put("ListLaporanCheckOut", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
         //  JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return swingNode;
      // showPrintPreView(pdfFileName);
    }
    
     public static SwingNode printLaporanOccupancyTipeKamar(List<ClassPrintLaporanOccupancyKamar>printLaporanRekapitulasiKamar){
      String fileName = "LaporanOccupancyTipeKamar";
      String pdfFileName = "Laporan Occupancy Tipe Kamar";
      SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanRekapitulasiKamar);

        getDataHotel(parameter);
        parameter.put("ListLaporanOccupanyTipeKamar", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
           JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return swingNode;
       // showPrintPreView(pdfFileName);
    }
    
    public static SwingNode printLaporanOccupancyKamar(List<ClassPrintLaporanOccupancyKamar>printLaporanOccupancyKamar){
      String fileName = "LaporanOccupancyKamar";
      String pdfFileName = "Laporan Occupancy Kamar";
      SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanOccupancyKamar);

        getDataHotel(parameter);
        parameter.put("ListLaporanOccupanyKamar", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
           JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return swingNode;
     //  showPrintPreView(pdfFileName);
    }
    
    public static SwingNode printLaporanPenerimaanBarang(List<ClassPrintLaporanPenerimaanBarang>printLaporanPenerimaanBarang){
      String fileName = "LaporanTerimaBarang";
      String pdfFileName = "Laporan Terima Barang";
      SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanPenerimaanBarang);

        getDataHotel(parameter);
        parameter.put("ListPenerimaanBarang", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
        //  String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
         //  JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
       return swingNode;
     // showPrintPreView(pdfFileName);
    }
    
      public static SwingNode printLaporanReturBarang(List<ClassPrintLaporanReturBarang>printLaporanReturBarang){
      String fileName = "LaporanReturBarang";
      String pdfFileName = "Laporan Retur Barang";
      SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanReturBarang);

        getDataHotel(parameter);
        parameter.put("ListReturBarang", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
          //  JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
       return swingNode;
      //showPrintPreView(pdfFileName);
    }
     
    public static SwingNode printLaporanPenerimaanPO(List<ClassPrintLaporanPenerimaanPO>printLaporanPenerimaanPO){
       String fileName = "LaporanPenerimaanPO";
       String pdfFileName = "Laporan Penerimaan PO";
       SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanPenerimaanPO);

        getDataHotel(parameter);
        parameter.put("ListPenerimaanPO", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
      return swingNode;
     // showPrintPreView(pdfFileName);
    }
    
    public static SwingNode printLaporanStokOpname(List<ClassPrintLaporanStokOpname>printLaporanStokOpname){
       String fileName = "LaporanStokOpname";
       String pdfFileName = "Laporan Stok Opname";
       SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanStokOpname);

        getDataHotel(parameter);
        parameter.put("ListStokOpname", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
       return swingNode;
      //showPrintPreView(pdfFileName);
    }
    
     public static SwingNode printLaporanRusakBarang(List<ClassPrintLaporanRusakBarang>printLaporanRusakBarang){
       String fileName = "LaporanRusakBarang";
       String pdfFileName = "Laporan Rusak Barang";
       SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanRusakBarang);

        getDataHotel(parameter);
        parameter.put("ListStokOpname", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
       return swingNode;
      //showPrintPreView(pdfFileName);
    }
    
       public static SwingNode printLaporanPengeluaran(List<ClassPrintLaporanPengeluaran>printLaporanPengeluaran){
       String fileName = "LaporanPengeluaran";
       String pdfFileName = "Laporan Pengeluaran";
       SwingNode swingNode = new SwingNode();
      
       JRBeanCollectionDataSource itemJRBean = new JRBeanCollectionDataSource(printLaporanPengeluaran);

        getDataHotel(parameter);
        parameter.put("ListStokOpname", itemJRBean);
        parameter.put("SUBREPORT_DIR", ClassFolderManager.reportSystemRootPath + "/");

        String reportSource = ClassFolderManager.reportSystemRootPath + "/" + fileName + ".jasper";

       try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportSource);

            jasperReport.setProperty("net.sf.jasperreports.query.executer.factory.plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql",
                    "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, itemJRBean);
            String pdfFile = ClassFolderManager.reportClientRootPath + "/" + pdfFileName + ".pdf";
            JRViewer jrView = new JRViewer(jasperPrint);
            swingNode.setContent(jrView);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile);

        } catch (JRException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
     return swingNode;
     // showPrintPreView(pdfFileName);
    }
    
    
    //--------------------------------------------------------------------------
    public static Connection connectionDB() {
        Connection conn = null;
        try {
            DBConnectionSetting dBConnectionSetting = new DBConnectionSetting();
            dBConnectionSetting.loadDataConnection();
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(dBConnectionSetting.getURL(), dBConnectionSetting.getDbUsername(), dBConnectionSetting.getDbPassword());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClassPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    //--------------------------------------------------------------------------
    public static boolean showPrintPreView(String fileName) {
        boolean status = true;
        try {
            File pdfFile = new File(ClassFolderManager.reportClientRootPath + "/" + fileName + ".pdf");
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                    //Desktop.getDesktop().print(pdfFile);
                } else {
                    status = false;
                    System.out.println("Awt Desktop is not supported!");
                }
            } else {
                status = false;
                System.out.println("File is not exists!");
            }
        } catch (IOException ex) {
            status = false;
            System.out.println(ex);
        }
        return status;
    }

    public static void getDataHotel(Map parameter) {
        //service
        FLoginManager fLoginManager = new FLoginManagerImpl();

        String hotelName = "";
        String hotelAddress = "";
        String hotelPhone = "";
        String hotelLogoName = "";
        SysDataHardCode sdhHotelName = fLoginManager.getDataSysDataHardCode((long) 12);
        SysDataHardCode sdhHotelAddress = fLoginManager.getDataSysDataHardCode((long) 13);
        SysDataHardCode sdhHotelPhone = fLoginManager.getDataSysDataHardCode((long) 14);
        SysDataHardCode sdhHotelLogoName = fLoginManager.getDataSysDataHardCode((long) 15);

        if (sdhHotelName != null && sdhHotelName.getDataHardCodeValue() != null) {
            hotelName = sdhHotelName.getDataHardCodeValue();
        }

        if (sdhHotelAddress != null && sdhHotelAddress.getDataHardCodeValue() != null) {
            hotelAddress = sdhHotelAddress.getDataHardCodeValue();
        }

        if (sdhHotelPhone != null && sdhHotelPhone.getDataHardCodeValue() != null) {
            hotelPhone = sdhHotelPhone.getDataHardCodeValue();
        }

        if (sdhHotelLogoName != null && sdhHotelLogoName.getDataHardCodeValue() != null) {
            hotelLogoName = sdhHotelLogoName.getDataHardCodeValue();
        }
        parameter.put("NAMA_HOTEL", hotelName);
        parameter.put("ALAMAT_HOTEL", hotelAddress);
        parameter.put("TELEPON_HOTEL", hotelPhone);
        parameter.put("LOGO_IMAGE", ClassFolderManager.imageSystemRootPath + "/" + hotelLogoName);
    }

}
