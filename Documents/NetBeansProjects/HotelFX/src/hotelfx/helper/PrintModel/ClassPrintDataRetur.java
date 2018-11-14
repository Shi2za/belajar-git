/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.PrintModel;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Andreas
 */
public class ClassPrintDataRetur {
    String kodePO;
    String kodeRetur;
    String kodeSuratJalan;
//    String tanggalKirim;
    String namaSupplier;
    String alamatSupplier;
    String noTeleponSupplier;
    String keterangan;
    String namaPembuat;
    String jabatanPembuat;
    String namaApproval;
    String jabatanApproval;
    String namaPIC;
    String noTeleponPIC;
//    BigDecimal diskonTambahan;
    BigDecimal pajak;
    BigDecimal ongkosKirim;
    List<ClassPrintDataReturDetail>listReturDetail;

    public ClassPrintDataRetur() {
    }

    public String getKodePO() {
        return kodePO;
    }

    public void setKodePO(String kodePO) {
        this.kodePO = kodePO;
    }

    public String getKodeRetur() {
        return kodeRetur;
    }

    public void setKodeRetur(String kodeRetur) {
        this.kodeRetur = kodeRetur;
    }

    public String getKodeSuratJalan() {
        return kodeSuratJalan;
    }

    public void setKodeSuratJalan(String kodeSuratJalan) {
        this.kodeSuratJalan = kodeSuratJalan;
    }

/*    public String getTanggalRetur() {
        return tanggalRetur;
    }

    public void setTanggalRetur(String tanggalRetur) {
        this.tanggalRetur = tanggalRetur;
    }
    
    
 /*   public String getTanggalPO() {
        return tanggalPO;
    }

    public void setTanggalPO(String tanggalPO) {
        this.tanggalPO = tanggalPO;
    }

    public String getTanggalKirim() {
        return tanggalKirim;
    }

    public void setTanggalKirim(String tanggalKirim) {
        this.tanggalKirim = tanggalKirim;
    } */

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

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNamaPembuat() {
        return namaPembuat;
    }

    public void setNamaPembuat(String namaPembuat) {
        this.namaPembuat = namaPembuat;
    }

    public String getJabatanPembuat() {
        return jabatanPembuat;
    }

    public void setJabatanPembuat(String jabatanPembuat) {
        this.jabatanPembuat = jabatanPembuat;
    }

    public String getNamaApproval() {
        return namaApproval;
    }

    public void setNamaApproval(String namaApproval) {
        this.namaApproval = namaApproval;
    }

    public String getJabatanApproval() {
        return jabatanApproval;
    }

    public void setJabatanApproval(String jabatanApproval) {
        this.jabatanApproval = jabatanApproval;
    }

    public String getNamaPIC() {
        return namaPIC;
    }

    public void setNamaPIC(String namaPIC) {
        this.namaPIC = namaPIC;
    }

    public String getNoTeleponPIC() {
        return noTeleponPIC;
    }

    public void setNoTeleponPIC(String noTeleponPIC) {
        this.noTeleponPIC = noTeleponPIC;
    }

/*    public BigDecimal getDiskonTambahan() {
        return diskonTambahan;
    }

    public void setDiskonTambahan(BigDecimal diskonTambahan) {
        this.diskonTambahan = diskonTambahan;
    } */

    public BigDecimal getPajak() {
        return pajak;
    }

    public void setPajak(BigDecimal pajak) {
        this.pajak = pajak;
    }

    public BigDecimal getOngkosKirim() {
        return ongkosKirim;
    }

    public void setOngkosKirim(BigDecimal ongkosKirim) {
        this.ongkosKirim = ongkosKirim;
    }

    public List<ClassPrintDataReturDetail> getListReturDetail() {
        return listReturDetail;
    }

    public void setListReturDetail(List<ClassPrintDataReturDetail> listReturDetail) {
        this.listReturDetail = listReturDetail;
    }
}
