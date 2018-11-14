/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.PrintModel;

import java.util.List;

/**
 *
 * @author Andreas
 */
public class ClassPrintReceiving {
   String kodeReceiving;
   String kodePO;
   String namaSupplier;
   String alamatSupplier;
   String noTeleponSupplier;
   List<ClassPrintReceivingDetail>receivingDetail;

    public ClassPrintReceiving() {
    }

    public String getKodeReceiving(){
        return kodeReceiving;
    }

    public void setKodeReceiving(String kodeReceiving) {
        this.kodeReceiving = kodeReceiving;
    }

    public String getKodePO() {
        return kodePO;
    }

    public void setKodePO(String kodePO) {
        this.kodePO = kodePO;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public void setNamaSupplier(String namaSupplier) {
        this.namaSupplier = namaSupplier;
    }

    public String getAlamatSupplier() {
        return alamatSupplier;
    }

    public void setAlamatSupplier(String alamatSupplier) {
        this.alamatSupplier = alamatSupplier;
    }

    public String getNoTeleponSupplier() {
        return noTeleponSupplier;
    }

    public void setNoTeleponSupplier(String noTeleponSupplier) {
        this.noTeleponSupplier = noTeleponSupplier;
    }

    public List<ClassPrintReceivingDetail> getReceivingDetail() {
        return receivingDetail;
    }

    public void setReceivingDetail(List<ClassPrintReceivingDetail> receivingDetail) {
        this.receivingDetail = receivingDetail;
    }
   
}
