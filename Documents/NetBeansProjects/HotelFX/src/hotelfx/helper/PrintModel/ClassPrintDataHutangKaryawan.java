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
public class ClassPrintDataHutangKaryawan {
    
   String printedBy;
   List<ClassPrintDataHutangKaryawanDetail>listDataHutangKaryawanDetail;

    public ClassPrintDataHutangKaryawan() {
    }

    public String getPrintedBy() {
        return printedBy;
    }

    public void setPrintedBy(String printedBy) {
        this.printedBy = printedBy;
    }

    public List<ClassPrintDataHutangKaryawanDetail> getListDataHutangKaryawanDetail() {
        return listDataHutangKaryawanDetail;
    }

    public void setListDataHutangKaryawanDetail(List<ClassPrintDataHutangKaryawanDetail> listDataHutangKaryawanDetail) {
        this.listDataHutangKaryawanDetail = listDataHutangKaryawanDetail;
    }
}
