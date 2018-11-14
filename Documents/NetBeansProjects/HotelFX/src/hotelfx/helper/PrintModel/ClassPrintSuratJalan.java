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
public class ClassPrintSuratJalan {
    String kodeSuratJalan;
    String namaSupplier;
    String alamatSupplier;
    String noTeleponSupplier;
    String keterangan;
    String namaPembuat;
    String jabatanPembuat;
    String namaPIC;
    String noTeleponPIC;
    List<ClassPrintSuratJalanDetail>detailSuratJalan;

    public ClassPrintSuratJalan() {
    }

    public String getKodeSuratJalan() {
        return kodeSuratJalan;
    }

    public void setKodeSuratJalan(String kodeSuratJalan) {
        this.kodeSuratJalan = kodeSuratJalan;
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

    public List<ClassPrintSuratJalanDetail> getDetailSuratJalan() {
        return detailSuratJalan;
    }

    public void setDetailSuratJalan(List<ClassPrintSuratJalanDetail> detailSuratJalan) {
        this.detailSuratJalan = detailSuratJalan;
    }
}
