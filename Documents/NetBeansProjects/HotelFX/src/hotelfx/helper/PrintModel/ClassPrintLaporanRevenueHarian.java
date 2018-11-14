/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.PrintModel;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Andreas
 */
public class ClassPrintLaporanRevenueHarian {
  String tanggal;
  String createdBy;
  List<ClassPrintLaporanRevenueHarianDetail>laporanPendapatanDetail;

    public ClassPrintLaporanRevenueHarian() {
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<ClassPrintLaporanRevenueHarianDetail> getLaporanPendapatanDetail() {
        return laporanPendapatanDetail;
    }

    public void setLaporanPendapatanDetail(List<ClassPrintLaporanRevenueHarianDetail> laporanPendapatanDetail) {
        this.laporanPendapatanDetail = laporanPendapatanDetail;
    }
  
  
}
