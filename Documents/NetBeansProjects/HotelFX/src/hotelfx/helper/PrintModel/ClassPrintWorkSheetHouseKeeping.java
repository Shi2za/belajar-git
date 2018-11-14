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
public class ClassPrintWorkSheetHouseKeeping {
    String namaChecker;
    List<ClassPrintWorkSheetHouseKeepingDetail>listWorkSheetHouseKeepingDetail;
    
    public ClassPrintWorkSheetHouseKeeping() {
    }

    public String getNamaChecker() {
        return namaChecker;
    }

    public void setNamaChecker(String namaChecker) {
        this.namaChecker = namaChecker;
    }

    public List<ClassPrintWorkSheetHouseKeepingDetail> getListWorkSheetHouseKeepingDetail() {
        return listWorkSheetHouseKeepingDetail;
    }

    public void setListWorkSheetHouseKeepingDetail(List<ClassPrintWorkSheetHouseKeepingDetail> listWorkSheetHouseKeepingDetail) {
        this.listWorkSheetHouseKeepingDetail = listWorkSheetHouseKeepingDetail;
    }
}
