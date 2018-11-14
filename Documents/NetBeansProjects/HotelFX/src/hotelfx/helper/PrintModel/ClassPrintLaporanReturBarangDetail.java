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
public class ClassPrintLaporanReturBarangDetail {
   String tanggal;
   String noPO;
   String noSuratJalan;
   String noRetur;
   String supplier;
   String kodeBarang;
   String namaBarang;
   BigDecimal qty;
   String unit;
   BigDecimal harga;
   BigDecimal total;

    public ClassPrintLaporanReturBarangDetail() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNoPO() {
        return noPO;
    }

    public void setNoPO(String noPO) {
        this.noPO = noPO;
    }

    public String getNoRetur() {
        return noRetur;
    }

    public void setNoRetur(String noRetur) {
        this.noRetur = noRetur;
    }
    
    public String getNoSuratJalan() {
        return noSuratJalan;
    }

    public void setNoSuratJalan(String noSuratJalan) {
        this.noSuratJalan = noSuratJalan;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
