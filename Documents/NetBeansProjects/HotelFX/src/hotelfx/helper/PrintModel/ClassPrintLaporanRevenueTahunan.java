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
public class ClassPrintLaporanRevenueTahunan {
  String tahun;
  String createdBy;
  List<ClassPrintLaporanRevenueTahunanDetail>laporanRevenueTahunanDetail;

    public ClassPrintLaporanRevenueTahunan() {
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<ClassPrintLaporanRevenueTahunanDetail> getLaporanRevenueTahunanDetail() {
        return laporanRevenueTahunanDetail;
    }

    public void setLaporanRevenueTahunanDetail(List<ClassPrintLaporanRevenueTahunanDetail> laporanRevenueTahunanDetail) {
        this.laporanRevenueTahunanDetail = laporanRevenueTahunanDetail;
    }
  
  
}
