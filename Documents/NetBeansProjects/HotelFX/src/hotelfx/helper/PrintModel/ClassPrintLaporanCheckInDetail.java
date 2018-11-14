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
public class ClassPrintLaporanCheckInDetail {
   
   String noKamar;
   String namaTamu;
   String noReservasi;
   String tanggalCheckIn;
   String tanggalCheckOut;
   int jumlahDewasa;
   int jumlahAnak;
   long lamaHari;
   String alamat;
   String kota;
   String negara;

    public ClassPrintLaporanCheckInDetail() {
    }

    public String getNoKamar() {
        return noKamar;
    }

    public void setNoKamar(String noKamar) {
        this.noKamar = noKamar;
    }

    public String getNamaTamu() {
        return namaTamu;
    }

    public void setNamaTamu(String namaTamu) {
        this.namaTamu = namaTamu;
    }

    public String getNoReservasi() {
        return noReservasi;
    }

    public void setNoReservasi(String noReservasi) {
        this.noReservasi = noReservasi;
    }

    public String getTanggalCheckIn() {
        return tanggalCheckIn;
    }

    public void setTanggalCheckIn(String tanggalCheckIn) {
        this.tanggalCheckIn = tanggalCheckIn;
    }

    public String getTanggalCheckOut() {
        return tanggalCheckOut;
    }

    public void setTanggalCheckOut(String tanggalCheckOut) {
        this.tanggalCheckOut = tanggalCheckOut;
    }

    public long getLamaHari() {
        return lamaHari;
    }

    public void setLamaHari(long lamaHari) {
        this.lamaHari = lamaHari;
    }
    
    
    public int getJumlahDewasa() {
        return jumlahDewasa;
    }

    public void setJumlahDewasa(int jumlahDewasa) {
        this.jumlahDewasa = jumlahDewasa;
    }

    public int getJumlahAnak() {
        return jumlahAnak;
    }

    public void setJumlahAnak(int jumlahAnak) {
        this.jumlahAnak = jumlahAnak;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }
    
}
