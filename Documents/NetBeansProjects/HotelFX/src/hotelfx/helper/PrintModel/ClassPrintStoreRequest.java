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
public class ClassPrintStoreRequest {
   String kodeStoreRequest;
   String tanggal;
   String gudangSumber;
   String gudangTujuan;
   String namaPembuat;
   String jabatanPembuat;
   String namaApproval;
   String jabatanApproval;
   String namaPenerima;
   String jabatanPenerima;
   List<ClassPrintStoreRequestDetail>listStoreRequestDetail;

    public ClassPrintStoreRequest() {
    }

    public String getKodeStoreRequest() {
        return kodeStoreRequest;
    }

    public void setKodeStoreRequest(String kodeStoreRequest) {
        this.kodeStoreRequest = kodeStoreRequest;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getGudangSumber() {
        return gudangSumber;
    }

    public void setGudangSumber(String gudangSumber) {
        this.gudangSumber = gudangSumber;
    }

    public String getGudangTujuan() {
        return gudangTujuan;
    }

    public void setGudangTujuan(String gudangTujuan) {
        this.gudangTujuan = gudangTujuan;
    }

    public List<ClassPrintStoreRequestDetail> getListStoreRequestDetail() {
        return listStoreRequestDetail;
    }

    public void setListStoreRequestDetail(List<ClassPrintStoreRequestDetail> listStoreRequestDetail) {
        this.listStoreRequestDetail = listStoreRequestDetail;
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

    public String getNamaPenerima() {
        return namaPenerima;
    }

    public void setNamaPenerima(String namaPenerima) {
        this.namaPenerima = namaPenerima;
    }

    public String getJabatanPenerima() {
        return jabatanPenerima;
    }

    public void setJabatanPenerima(String jabatanPenerima) {
        this.jabatanPenerima = jabatanPenerima;
    }
}
