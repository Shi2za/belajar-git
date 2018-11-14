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
public class ClassPrintLaporanPengeluaranDetail {
   String tanggal;
   String deskripsi;
   BigDecimal nominalPembayaran;
   String tipePembayaran;
   String keterangan;

    public ClassPrintLaporanPengeluaranDetail() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public BigDecimal getNominalPembayaran() {
        return nominalPembayaran;
    }

    public void setNominalPembayaran(BigDecimal nominalPembayaran) {
        this.nominalPembayaran = nominalPembayaran;
    }

    public String getTipePembayaran() {
        return tipePembayaran;
    }

    public void setTipePembayaran(String tipePembayaran) {
        this.tipePembayaran = tipePembayaran;
    }
    
    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
