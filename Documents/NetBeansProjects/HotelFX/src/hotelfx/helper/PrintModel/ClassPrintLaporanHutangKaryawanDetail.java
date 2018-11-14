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
public class ClassPrintLaporanHutangKaryawanDetail {
   String tanggal;
   String idKaryawan;
   String namaKaryawan;
   String tipeKaryawan;
   String jabatanKaryawan;
   BigDecimal nominalPinjaman;
   String statusPinjaman;

    public ClassPrintLaporanHutangKaryawanDetail() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getTipeKaryawan() {
        return tipeKaryawan;
    }

    public void setTipeKaryawan(String tipeKaryawan) {
        this.tipeKaryawan = tipeKaryawan;
    }

    public String getJabatanKaryawan() {
        return jabatanKaryawan;
    }

    public void setJabatanKaryawan(String jabatanKaryawan) {
        this.jabatanKaryawan = jabatanKaryawan;
    }

    public BigDecimal getNominalPinjaman() {
        return nominalPinjaman;
    }

    public void setNominalPinjaman(BigDecimal nominalPinjaman) {
        this.nominalPinjaman = nominalPinjaman;
    }

    public String getStatusPinjaman() {
        return statusPinjaman;
    }

    public void setStatusPinjaman(String statusPinjaman) {
        this.statusPinjaman = statusPinjaman;
    }   
}
