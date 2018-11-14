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
public class ClassPrintLaporanMasukKeluarBarang {
   String periode;
   String gudang;
   String createdBy;
   List<ClassPrintLaporanMasukKeluarBarangDetail>listLaporanMasukKeluarBarangDetail;

    public ClassPrintLaporanMasukKeluarBarang() {
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getGudang() {
        return gudang;
    }

    public void setGudang(String gudang) {
        this.gudang = gudang;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<ClassPrintLaporanMasukKeluarBarangDetail> getListLaporanMasukKeluarBarangDetail() {
        return listLaporanMasukKeluarBarangDetail;
    }

    public void setListLaporanMasukKeluarBarangDetail(List<ClassPrintLaporanMasukKeluarBarangDetail> listLaporanMasukKeluarBarangDetail) {
        this.listLaporanMasukKeluarBarangDetail = listLaporanMasukKeluarBarangDetail;
    }
   
   
}
