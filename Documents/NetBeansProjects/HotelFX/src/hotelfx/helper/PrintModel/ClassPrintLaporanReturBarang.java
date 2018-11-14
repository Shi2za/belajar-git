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
public class ClassPrintLaporanReturBarang {
   String periode;
   String createdBy;
   List<ClassPrintLaporanReturBarangDetail>listLaporanReturBarangDetail;

    public ClassPrintLaporanReturBarang() {
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<ClassPrintLaporanReturBarangDetail> getListLaporanReturBarangDetail() {
        return listLaporanReturBarangDetail;
    }

    public void setListLaporanReturBarangDetail(List<ClassPrintLaporanReturBarangDetail> listLaporanReturBarangDetail) {
        this.listLaporanReturBarangDetail = listLaporanReturBarangDetail;
    }
   
}
