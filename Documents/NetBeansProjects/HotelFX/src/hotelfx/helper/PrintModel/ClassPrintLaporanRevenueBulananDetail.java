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
public class ClassPrintLaporanRevenueBulananDetail {
    String tanggal;
    BigDecimal totalHarga;
    BigDecimal diskon;
    BigDecimal compliment;
    BigDecimal serviceCharge;
    BigDecimal pajak;
    BigDecimal total;

    public ClassPrintLaporanRevenueBulananDetail() {
        this.tanggal = tanggal;
        this.totalHarga = totalHarga;
        this.diskon = diskon;
        this.serviceCharge = serviceCharge;
        this.pajak = pajak;
        this.total = total;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public BigDecimal getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(BigDecimal totalHarga) {
        this.totalHarga = totalHarga;
    }

    public BigDecimal getDiskon() {
        return diskon;
    }

    public void setDiskon(BigDecimal diskon) {
        this.diskon = diskon;
    }

    public BigDecimal getCompliment() {
        return compliment;
    }

    public void setCompliment(BigDecimal compliment) {
        this.compliment = compliment;
    }
    
    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
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
