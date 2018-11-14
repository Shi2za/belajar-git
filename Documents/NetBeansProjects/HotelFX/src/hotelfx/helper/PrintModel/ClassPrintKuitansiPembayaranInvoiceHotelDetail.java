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
public class ClassPrintKuitansiPembayaranInvoiceHotelDetail {
   String kodeInvoice;
   BigDecimal nominalPembayaran;

    public ClassPrintKuitansiPembayaranInvoiceHotelDetail() {
    }

    public String getKodeInvoice() {
        return kodeInvoice;
    }

    public void setKodeInvoice(String kodeInvoice) {
        this.kodeInvoice = kodeInvoice;
    }

    public BigDecimal getNominalPembayaran() {
        return nominalPembayaran;
    }

    public void setNominalPembayaran(BigDecimal nominalPembayaran) {
        this.nominalPembayaran = nominalPembayaran;
    }
   
   
}
