/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.PrintModel;

import java.math.BigDecimal;

/**
 *
 * @author Andreas
 */
public class ClassPrintLaporanRevenueHarianDetail {
    String kodeReservasi;
    String deskripsi;
    String statusReservasi;
    BigDecimal hargaBarang;
    BigDecimal jumlah;
    String satuan;
    BigDecimal totalHarga;
    BigDecimal diskon;
    BigDecimal compliment;
    BigDecimal pajak;
    BigDecimal serviceCharge;
    BigDecimal total;

    public ClassPrintLaporanRevenueHarianDetail(String kodeReservasi, String deskripsi, String statusReservasi, BigDecimal hargaBarang, BigDecimal jumlah, String satuan, BigDecimal totalHarga, BigDecimal diskon, BigDecimal compliment, BigDecimal pajak, BigDecimal serviceCharge, BigDecimal total) {
        this.kodeReservasi = kodeReservasi;
        this.deskripsi = deskripsi;
        this.statusReservasi = statusReservasi;
        this.hargaBarang = hargaBarang;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.totalHarga = totalHarga;
        this.diskon = diskon;
        this.compliment = compliment;
        this.pajak = pajak;
        this.serviceCharge = serviceCharge;
        this.total = total;
    }

    public String getKodeReservasi() {
        return kodeReservasi;
    }

    public void setKodeReservasi(String kodeReservasi) {
        this.kodeReservasi = kodeReservasi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStatusReservasi() {
        return statusReservasi;
    }

    public void setStatusReservasi(String statusReservasi) {
        this.statusReservasi = statusReservasi;
    }
    
    public BigDecimal getCompliment() {
        return compliment;
    }

    public void setCompliment(BigDecimal compliment) {
        this.compliment = compliment;
    }

    public BigDecimal getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(BigDecimal hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public BigDecimal getJumlah() {
        return jumlah;
    }

    public void setJumlah(BigDecimal jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public BigDecimal getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(BigDecimal totalHarga) {
        this.totalHarga = totalHarga;
    }

    public BigDecimal getDiskon() {
        return diskon;
    }

    public void setDiskon(BigDecimal diskon) {
        this.diskon = diskon;
    }

    public BigDecimal getPajak() {
        return pajak;
    }

    public void setPajak(BigDecimal pajak) {
        this.pajak = pajak;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
}
