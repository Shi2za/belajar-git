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
public class ClassPrintLaporanRevenueTahunanDetail {
    String bulan;
    BigDecimal totalHarga;
    BigDecimal diskon;
    BigDecimal compliment;
    BigDecimal serviceCharge;
    BigDecimal pajak;
    BigDecimal total;

    public ClassPrintLaporanRevenueTahunanDetail() {
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
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
