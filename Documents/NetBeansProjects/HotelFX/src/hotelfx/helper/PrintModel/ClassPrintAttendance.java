/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.PrintModel;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Andreas
 */
public class ClassPrintAttendance {
    private Date tanggalAbsen;
    private String idKaryawan;
    private String namaKaryawan;
    private String tipeKaryawan;
    private Time jamMasukKerja;
    private Time jamKeluarKerja;
    private Time jamMasukLembur;
    private Time jamKeluarLembur;
    private Date jamMasukReal;
    private Date jamKeluarReal;
    private String statusMasuk;
    private String statusKeluar;
    private String statusKehadiran;

    public ClassPrintAttendance() {
    }
    
    public Date getTanggalAbsen() {
        return tanggalAbsen;
    }

    public void setTanggalAbsen(Date tanggalAbsen) {
        this.tanggalAbsen = tanggalAbsen;
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

    public Time getJamMasukKerja() {
        return jamMasukKerja;
    }

    public void setJamMasukKerja(Time jamMasukKerja) {
        this.jamMasukKerja = jamMasukKerja;
    }

    public Time getJamKeluarKerja() {
        return jamKeluarKerja;
    }

    public void setJamKeluarKerja(Time jamKeluarKerja) {
        this.jamKeluarKerja = jamKeluarKerja;
    }

    public Time getJamMasukLembur() {
        return jamMasukLembur;
    }

    public void setJamMasukLembur(Time jamMasukLembur) {
        this.jamMasukLembur = jamMasukLembur;
    }

    public Time getJamKeluarLembur() {
        return jamKeluarLembur;
    }

    public void setJamKeluarLembur(Time jamKeluarLembur) {
        this.jamKeluarLembur = jamKeluarLembur;
    }

    public Date getJamMasukReal() {
        return jamMasukReal;
    }

    public void setJamMasukReal(Date jamMasukReal) {
        this.jamMasukReal = jamMasukReal;
    }

    public Date getJamKeluarReal() {
        return jamKeluarReal;
    }

    public void setJamKeluarReal(Date jamKeluarReal) {
        this.jamKeluarReal = jamKeluarReal;
    }

    public String getStatusMasuk() {
        return statusMasuk;
    }

    public void setStatusMasuk(String statusMasuk) {
        this.statusMasuk = statusMasuk;
    }

    public String getStatusKeluar() {
        return statusKeluar;
    }

    public void setStatusKeluar(String statusKeluar) {
        this.statusKeluar = statusKeluar;
    }

    public String getStatusKehadiran() {
        return statusKehadiran;
    }

    public void setStatusKehadiran(String statusKehadiran) {
        this.statusKehadiran = statusKehadiran;
    }
    
    
}
