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
public class ClassPrintInvoiceReturDetail {
    String kodePo;
    String kodeBarang;
    String namaBarang;
    BigDecimal harga;
    BigDecimal jumlah;
    String satuan;
    BigDecimal diskon;
    BigDecimal pajak;
    BigDecimal total;

    public ClassPrintInvoiceReturDetail() {
    }

    public String getKodePo() {
        return kodePo;
    }

    public void setKodePo(String kodePo) {
        this.kodePo = kodePo;
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

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public BigDecimal getJumlah() {
        return jumlah;
    }

    public void setJumlah(BigDecimal jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
    
    public BigDecimal getDiskon() {
        return diskon;
    }

    public void setDiskon(BigDecimal diskon) {
        this.diskon = diskon;
    }

    public BigDecimal getPajak() {
        return pajak;
    }

    public void setPajak(BigDecimal pajak) {
        this.pajak = pajak;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    
}
