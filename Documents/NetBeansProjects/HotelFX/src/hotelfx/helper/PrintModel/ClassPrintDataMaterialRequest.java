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
public class ClassPrintDataMaterialRequest {
    
    String noMR;
    String tanggal;
    String namaPembuat;
    String jabatanPembuat;
    String namaPersetujuan;
    String jabatanPersetujuan;
    List<ClassPrintDataMaterialRequestDetail>listMaterialRequestDetail;

    public ClassPrintDataMaterialRequest() {
    }

    public String getNoMR() {
        return noMR;
    }

    public void setNoMR(String noMR) {
        this.noMR = noMR;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public String getNamaPersetujuan() {
        return namaPersetujuan;
    }

    public void setNamaPersetujuan(String namaPersetujuan) {
        this.namaPersetujuan = namaPersetujuan;
    }

    public String getJabatanPersetujuan() {
        return jabatanPersetujuan;
    }

    public void setJabatanPersetujuan(String jabatanPersetujuan) {
        this.jabatanPersetujuan = jabatanPersetujuan;
    }

    public List<ClassPrintDataMaterialRequestDetail> getListMaterialRequestDetail() {
        return listMaterialRequestDetail;
    }

    public void setListMaterialRequestDetail(List<ClassPrintDataMaterialRequestDetail> listMaterialRequestDetail) {
        this.listMaterialRequestDetail = listMaterialRequestDetail;
    }
    
}
