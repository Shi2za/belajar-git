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
public class ClassPrintLaporanPenerimaanPODetail {
   String tanggalPO;
   String kodePO;
   String kodeSupplier;
   String namaSupplier;
   String kodeBarang;
   String namaBarang;
   String satuan;
   BigDecimal qtyPO;
   BigDecimal qtyTerima;
   BigDecimal qtyRetur;
   BigDecimal balance;
   BigDecimal balancePercent;
   String status;

    public ClassPrintLaporanPenerimaanPODetail() {
    }

    public String getTanggalPO() {
        return tanggalPO;
    }

    public void setTanggalPO(String tanggalPO) {
        this.tanggalPO = tanggalPO;
    }

    public String getKodePO() {
        return kodePO;
    }

    public void setKodePO(String kodePO) {
        this.kodePO = kodePO;
    }

    public String getKodeSupplier() {
        return kodeSupplier;
    }

    public void setKodeSupplier(String kodeSupplier) {
        this.kodeSupplier = kodeSupplier;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public void setNamaSupplier(String namaSupplier) {
        this.namaSupplier = namaSupplier;
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

    public BigDecimal getQtyPO() {
        return qtyPO;
    }

    public void setQtyPO(BigDecimal qtyPO) {
        this.qtyPO = qtyPO;
    }

    public BigDecimal getQtyTerima() {
        return qtyTerima;
    }

    public void setQtyTerima(BigDecimal qtyTerima) {
        this.qtyTerima = qtyTerima;
    }

    public BigDecimal getQtyRetur() {
        return qtyRetur;
    }

    public void setQtyRetur(BigDecimal qtyRetur) {
        this.qtyRetur = qtyRetur;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalancePercent() {
        return balancePercent;
    }

    public void setBalancePercent(BigDecimal balancePercent) {
        this.balancePercent = balancePercent;
    }

    public String getStatus() {
       return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   
}
