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
public class ClassPrintLaporanRevenueBulanan {
   String monthly;
   String createdBy;
   List<ClassPrintLaporanRevenueBulananDetail>listLaporanRevenueBulananDetail;

    public ClassPrintLaporanRevenueBulanan() {
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public List<ClassPrintLaporanRevenueBulananDetail> getListLaporanRevenueBulananDetail() {
        return listLaporanRevenueBulananDetail;
    }

    public void setListLaporanRevenueBulananDetail(List<ClassPrintLaporanRevenueBulananDetail> listLaporanRevenueBulananDetail) {
        this.listLaporanRevenueBulananDetail = listLaporanRevenueBulananDetail;
    }
}
