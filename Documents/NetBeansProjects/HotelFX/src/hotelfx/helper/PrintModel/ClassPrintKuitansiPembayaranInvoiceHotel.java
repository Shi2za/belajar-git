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
public class ClassPrintKuitansiPembayaranInvoiceHotel {
  String kodeTransaksi;
  String namaPerusahaan;
  String alamatPerusahaan;
  String tanggalBayar;
  String tipePembayaran;
  BigDecimal pembulatan;
  List<ClassPrintKuitansiPembayaranInvoiceHotelDetail>listKuitansiPembayaranInvoiceHotelDetail;
  
    public ClassPrintKuitansiPembayaranInvoiceHotel() {
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
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

    public String getTanggalBayar() {
        return tanggalBayar;
    }

    public void setTanggalBayar(String tanggalBayar) {
        this.tanggalBayar = tanggalBayar;
    }

    public String getTipePembayaran() {
        return tipePembayaran;
    }

    public void setTipePembayaran(String tipePembayaran) {
        this.tipePembayaran = tipePembayaran;
    }

    public BigDecimal getPembulatan() {
        return pembulatan;
    }

    public void setPembulatan(BigDecimal pembulatan) {
        this.pembulatan = pembulatan;
    }

    public List<ClassPrintKuitansiPembayaranInvoiceHotelDetail> getListKuitansiPembayaranInvoiceHotelDetail() {
        return listKuitansiPembayaranInvoiceHotelDetail;
    }

    public void setListKuitansiPembayaranInvoiceHotelDetail(List<ClassPrintKuitansiPembayaranInvoiceHotelDetail> listKuitansiPembayaranInvoiceHotelDetail) {
        this.listKuitansiPembayaranInvoiceHotelDetail = listKuitansiPembayaranInvoiceHotelDetail;
    }
  
}
