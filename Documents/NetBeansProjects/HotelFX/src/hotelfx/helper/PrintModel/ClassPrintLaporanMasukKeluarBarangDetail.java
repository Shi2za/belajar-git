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
public class ClassPrintLaporanMasukKeluarBarangDetail {
   String tanggal;
   String kodeBarang;
   String namaBarang;
   String satuan;
   BigDecimal stokAwal;
   BigDecimal stokMasuk;
   BigDecimal stokKeluar;
   BigDecimal stokAkhir;

    public ClassPrintLaporanMasukKeluarBarangDetail() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public BigDecimal getStokAwal() {
        return stokAwal;
    }

    public void setStokAwal(BigDecimal stokAwal) {
        this.stokAwal = stokAwal;
    }

    public BigDecimal getStokMasuk() {
        return stokMasuk;
    }

    public void setStokMasuk(BigDecimal stokMasuk) {
        this.stokMasuk = stokMasuk;
    }

    public BigDecimal getStokKeluar() {
        return stokKeluar;
    }

    public void setStokKeluar(BigDecimal stokKeluar) {
        this.stokKeluar = stokKeluar;
    }

    public BigDecimal getStokAkhir() {
        return stokAkhir;
    }

    public void setStokAkhir(BigDecimal stokAkhir) {
        this.stokAkhir = stokAkhir;
    }
   
}
