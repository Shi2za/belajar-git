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
public class ClassPrintLaporanPenerimaanBarangDetail {
    
   String tanggal;
   String kodePenerimaan;
   String kodePembelian;
   String noSuratJalan;
   String kodeBarang;
   String namaBarang;
   String satuan;
   String tanggalExp;
   BigDecimal qty;
   String bonus;
  
    public ClassPrintLaporanPenerimaanBarangDetail() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKodePenerimaan() {
        return kodePenerimaan;
    }

    public void setKodePenerimaan(String kodePenerimaan) {
        this.kodePenerimaan = kodePenerimaan;
    }

    public String getKodePembelian() {
        return kodePembelian;
    }

    public void setKodePembelian(String kodePembelian) {
        this.kodePembelian = kodePembelian;
    }

    public String getNoSuratJalan() {
        return noSuratJalan;
    }

    public void setNoSuratJalan(String noSuratJalan) {
        this.noSuratJalan = noSuratJalan;
    }
    
    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getTanggalExp() {
        return tanggalExp;
    }

    public void setTanggalExp(String tanggalExp) {
        this.tanggalExp = tanggalExp;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
