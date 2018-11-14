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
public class ClassPrintLaporanPenerimaanPO {
   String periode;
   String createdBy;
   List<ClassPrintLaporanPenerimaanPODetail>listLaporanPenerimaanPODetail;

    public ClassPrintLaporanPenerimaanPO() {
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

    public List<ClassPrintLaporanPenerimaanPODetail> getListLaporanPenerimaanPODetail() {
        return listLaporanPenerimaanPODetail;
    }

    public void setListLaporanPenerimaanPODetail(List<ClassPrintLaporanPenerimaanPODetail> listLaporanPenerimaanPODetail) {
        this.listLaporanPenerimaanPODetail = listLaporanPenerimaanPODetail;
    }
   
}
