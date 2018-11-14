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
public class ClassPrintLaporanTransaksiArusKasDetail {
    String tanggalTransaksi;
    String deskripsi;
    BigDecimal nominalMasuk;
    BigDecimal nominalKeluar;
    BigDecimal saldo;
    String keterangan;

    public ClassPrintLaporanTransaksiArusKasDetail() {
    }

    public String getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(String tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public BigDecimal getNominalMasuk() {
        return nominalMasuk;
    }

    public void setNominalMasuk(BigDecimal nominalMasuk) {
        this.nominalMasuk = nominalMasuk;
    }

    public BigDecimal getNominalKeluar() {
        return nominalKeluar;
    }

    public void setNominalKeluar(BigDecimal nominalKeluar) {
        this.nominalKeluar = nominalKeluar;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
    
    
}
