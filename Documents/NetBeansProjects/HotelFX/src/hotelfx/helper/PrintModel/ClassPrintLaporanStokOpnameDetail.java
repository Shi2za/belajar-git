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
public class ClassPrintLaporanStokOpnameDetail {
    
  String tanggal;
  String kodeStokOpname;
  String gudang;
  String kodeBarang;
  String namaBarang;
  String satuan;
  String tanggalKadaluarsa;
  String kodeBarcode;
  BigDecimal qtySistem;
  BigDecimal qtyReal;
  BigDecimal balance;

    public ClassPrintLaporanStokOpnameDetail() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKodeStokOpname() {
        return kodeStokOpname;
    }

    public void setKodeStokOpname(String kodeStokOpname) {
        this.kodeStokOpname = kodeStokOpname;
    }

    public String getGudang() {
        return gudang;
    }

    public void setGudang(String gudang) {
        this.gudang = gudang;
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

    public String getTanggalKadaluarsa() {
        return tanggalKadaluarsa;
    }

    public void setTanggalKadaluarsa(String tanggalKadaluarsa) {
        this.tanggalKadaluarsa = tanggalKadaluarsa;
    }

    public String getKodeBarcode() {
        return kodeBarcode;
    }

    public void setKodeBarcode(String kodeBarcode) {
        this.kodeBarcode = kodeBarcode;
    }
    
    public BigDecimal getQtySistem() {
        return qtySistem;
    }

    public void setQtySistem(BigDecimal qtySistem) {
        this.qtySistem = qtySistem;
    }

    public BigDecimal getQtyReal() {
        return qtyReal;
    }

    public void setQtyReal(BigDecimal qtyReal) {
        this.qtyReal = qtyReal;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
