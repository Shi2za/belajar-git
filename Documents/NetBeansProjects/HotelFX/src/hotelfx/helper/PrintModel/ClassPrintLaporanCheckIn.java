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
public class ClassPrintLaporanCheckIn {
   String periode;
   String createdBy;
   List<ClassPrintLaporanCheckInDetail>listLaporanCheckInDetail;

    public ClassPrintLaporanCheckIn() {
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

    public List<ClassPrintLaporanCheckInDetail> getListLaporanCheckInDetail() {
        return listLaporanCheckInDetail;
    }

    public void setListLaporanCheckInDetail(List<ClassPrintLaporanCheckInDetail> listLaporanCheckInDetail) {
        this.listLaporanCheckInDetail = listLaporanCheckInDetail;
    }
   
}
