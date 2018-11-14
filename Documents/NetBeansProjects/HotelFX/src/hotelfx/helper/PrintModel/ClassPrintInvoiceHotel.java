/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.PrintModel;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Andreas
 */
public class ClassPrintInvoiceHotel {
   String kodeInvoice;
   String namaPerusahaan;
   String alamatPerusahaan;
   String tanggalFaktur;
   String tanggalJatuhTempo;
   String subject;
   String kodeReservasi;
   String keterangan;
   BigDecimal totalPembayaran;
   List<ClassPrintInvoiceHotelDetail>listInvoiceHotelDetail;
//   List<ClassPrintInvoiceReturDetail>listInvoiceReturDetail;
   
    public ClassPrintInvoiceHotel() {
    }

    public String getKodeInvoice() {
        return kodeInvoice;
    }

    public void setKodeInvoice(String kodeInvoice) {
        this.kodeInvoice = kodeInvoice;
    }

    public String getNamaPerusahaan() {
        return namaPerusahaan;
    }

    public void setNamaPerusahaan(String namaPerusahaan) {
        this.namaPerusahaan = namaPerusahaan;
    }

    public String getAlamatPerusahaan() {
        return alamatPerusahaan;
    }

    public void setAlamatPerusahaan(String alamatPerusahaan) {
        this.alamatPerusahaan = alamatPerusahaan;
    }

    public String getTanggalFaktur() {
        return tanggalFaktur;
    }

    public void setTanggalFaktur(String tanggalFaktur) {
        this.tanggalFaktur = tanggalFaktur;
    }

    public String getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public void setTanggalJatuhTempo(String tanggalJatuhTempo) {
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }

    public String getKodeReservasi() {
        return kodeReservasi;
    }

    public void setKodeReservasi(String kodeReservasi) {
        this.kodeReservasi = kodeReservasi;
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public BigDecimal getTotalPembayaran() {
        return totalPembayaran;
    }

    public void setTotalPembayaran(BigDecimal totalPembayaran) {
        this.totalPembayaran = totalPembayaran;
    }
    
    public List<ClassPrintInvoiceHotelDetail> getListInvoiceHotelDetail() {
        return listInvoiceHotelDetail;
    }

    public void setListInvoiceHotelDetail(List<ClassPrintInvoiceHotelDetail> listInvoiceHotelDetail) {
        this.listInvoiceHotelDetail = listInvoiceHotelDetail;
    }
    
    

  /*  public List<ClassPrintInvoiceReturDetail> getListInvoiceReturDetail() {
        return listInvoiceReturDetail;
    }

    public void setListInvoiceReturDetail(List<ClassPrintInvoiceReturDetail> listInvoiceReturDetail) {
        this.listInvoiceReturDetail = listInvoiceReturDetail;
    } */
}
