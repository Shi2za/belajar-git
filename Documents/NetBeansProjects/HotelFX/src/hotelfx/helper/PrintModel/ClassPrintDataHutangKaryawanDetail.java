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
public class ClassPrintDataHutangKaryawanDetail {
   String idKaryawan;
   String namaKaryawan;
   String jabatan;
   String tipeKaryawan;
   BigDecimal totalPinjaman;
   BigDecimal totalBayar;
   BigDecimal sisaPinjaman;

    public ClassPrintDataHutangKaryawanDetail() {
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

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getTipeKaryawan() {
        return tipeKaryawan;
    }

    public void setTipeKaryawan(String tipeKaryawan) {
        this.tipeKaryawan = tipeKaryawan;
    }
    
    public BigDecimal getTotalPinjaman() {
        return totalPinjaman;
    }

    public void setTotalPinjaman(BigDecimal totalPinjaman) {
        this.totalPinjaman = totalPinjaman;
    }

    public BigDecimal getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(BigDecimal totalBayar) {
        this.totalBayar = totalBayar;
    }

    public BigDecimal getSisaPinjaman() {
        return sisaPinjaman;
    }

    public void setSisaPinjaman(BigDecimal sisaPinjaman) {
        this.sisaPinjaman = sisaPinjaman;
    }
}
