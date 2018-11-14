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
public class ClassPrintPurchaseOrder {
    String kodePO;
    String tanggalPO;
    String tanggalKirim;
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
    String revisiLabel;
    String keteranganRevisi;
    String tipePembayaran;
    BigDecimal diskonTambahan;
    BigDecimal pajak;
    BigDecimal ongkosKirim;
    List<ClassPrintPurchaseOrderDetail>detailPurchaseOrder;
    
    public ClassPrintPurchaseOrder() {
    }

    public String getKodePO() {
        return kodePO;
    }

    public void setKodePO(String kodePO) {
        this.kodePO = kodePO;
    }

    public String getTanggalPO() {
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

    public List<ClassPrintPurchaseOrderDetail> getDetailPurchaseOrder() {
        return detailPurchaseOrder;
    }

    public void setDetailPurchaseOrder(List<ClassPrintPurchaseOrderDetail> detailPurchaseOrder) {
        this.detailPurchaseOrder = detailPurchaseOrder;
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

    public BigDecimal getDiskonTambahan() {
        return diskonTambahan;
    }

    public void setDiskonTambahan(BigDecimal diskonTambahan) {
        this.diskonTambahan = diskonTambahan;
    }

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

    public String getNamaApproval() {
        return namaApproval;
    }

    public void setNamaApproval(String namaApproval) {
        this.namaApproval = namaApproval;
    }

    public String getNoTeleponPIC() {
        return noTeleponPIC;
    }

    public void setNoTeleponPIC(String noTeleponPIC) {
        this.noTeleponPIC = noTeleponPIC;
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

    public String getRevisiLabel() {
        return revisiLabel;
    }

    public void setRevisiLabel(String revisiLabel) {
        this.revisiLabel = revisiLabel;
    }

    public String getKeteranganRevisi() {
        return keteranganRevisi;
    }

    public void setKeteranganRevisi(String keteranganRevisi) {
        this.keteranganRevisi = keteranganRevisi;
    }

    public String getTipePembayaran() {
        return tipePembayaran;
    }

    public void setTipePembayaran(String tipePembayaran) {
        this.tipePembayaran = tipePembayaran;
    }
}
