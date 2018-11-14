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
public class ClassPrintLaporanTransaksiArusKas {
    String printedBy;
    String periode;
    String tipeKas;
    List<ClassPrintLaporanTransaksiArusKasDetail>listLaporanTransaksiArusKasDetail;

    public ClassPrintLaporanTransaksiArusKas() {
    }

    public String getPrintedBy() {
        return printedBy;
    }

    public void setPrintedBy(String printedBy) {
        this.printedBy = printedBy;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getTipeKas() {
        return tipeKas;
    }

    public void setTipeKas(String tipeKas) {
        this.tipeKas = tipeKas;
    }
    
    public List<ClassPrintLaporanTransaksiArusKasDetail> getListLaporanTransaksiArusKasDetail() {
        return listLaporanTransaksiArusKasDetail;
    }

    public void setListLaporanTransaksiArusKasDetail(List<ClassPrintLaporanTransaksiArusKasDetail> listLaporanTransaksiArusKasDetail) {
        this.listLaporanTransaksiArusKasDetail = listLaporanTransaksiArusKasDetail;
    }
    
    
}
