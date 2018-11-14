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
public class ClassPrintLaporanOccupancyKamarDetail {
   
   String tanggal;
   String tipeKamar;
   long jumlahKamar;
   long jumlahTerjual;
   BigDecimal occupancy;
   
    public ClassPrintLaporanOccupancyKamarDetail() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public void setTipeKamar(String tipeKamar) {
        this.tipeKamar = tipeKamar;
    }

    public long getJumlahKamar() {
        return jumlahKamar;
    }

    public void setJumlahKamar(long jumlahKamar) {
        this.jumlahKamar = jumlahKamar;
    }

    public long getJumlahTerjual() {
        return jumlahTerjual;
    }

    public void setJumlahTerjual(long jumlahTerjual) {
        this.jumlahTerjual = jumlahTerjual;
    }

    public BigDecimal getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(BigDecimal occupancy) {
        this.occupancy = occupancy;
    }
}
