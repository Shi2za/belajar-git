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
public class ClassPrintStoreRequestDetail {
   String kodeBarang;
   String namaBarang;
   BigDecimal jumlahBarang;
   String satuan;

    public ClassPrintStoreRequestDetail() {
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

    public BigDecimal getJumlahBarang() {
        return jumlahBarang;
    }

    public void setJumlahBarang(BigDecimal jumlahBarang) {
        this.jumlahBarang = jumlahBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
   
}
