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
public class ClassPrintLaporanPendapatanKamarDetail {
    String kodeReservasi;
    String tipeKamar;
    String namaCustomer;
    String travelAgent;
    BigDecimal hargaKamar;
    BigDecimal diskon;
    BigDecimal compliment;
    BigDecimal serviceCharge;
    BigDecimal pajak;

    public ClassPrintLaporanPendapatanKamarDetail(String kodeReservasi, String tipeKamar, String namaCustomer, String travelAgent, BigDecimal hargaKamar, BigDecimal diskon, BigDecimal compliment, BigDecimal serviceCharge, BigDecimal pajak) {
        this.kodeReservasi = kodeReservasi;
        this.tipeKamar = tipeKamar;
        this.namaCustomer = namaCustomer;
        this.travelAgent = travelAgent;
        this.hargaKamar = hargaKamar;
        this.diskon = diskon;
        this.compliment = compliment;
        this.serviceCharge = serviceCharge;
        this.pajak = pajak;
    }

    public String getKodeReservasi() {
        return kodeReservasi;
    }

    public void setKodeReservasi(String kodeReservasi) {
        this.kodeReservasi = kodeReservasi;
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public void setTipeKamar(String tipeKamar) {
        this.tipeKamar = tipeKamar;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getTravelAgent() {
        return travelAgent;
    }

    public void setTravelAgent(String travelAgent) {
        this.travelAgent = travelAgent;
    }

    public BigDecimal getHargaKamar() {
        return hargaKamar;
    }

    public void setHargaKamar(BigDecimal hargaKamar) {
        this.hargaKamar = hargaKamar;
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
}
