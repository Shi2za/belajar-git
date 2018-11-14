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
public class ClassPrintLaporanPendapatanKamar {
   String createdBy;
   String tanggalReservasi;
   List<ClassPrintLaporanPendapatanKamarDetail>laporanPendapatanDetail;

    public ClassPrintLaporanPendapatanKamar() {
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTanggalReservasi() {
        return tanggalReservasi;
    }

    public void setTanggalReservasi(String tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }

    public List<ClassPrintLaporanPendapatanKamarDetail> getLaporanPendapatanDetail() {
        return laporanPendapatanDetail;
    }

    public void setLaporanPendapatanDetail(List<ClassPrintLaporanPendapatanKamarDetail> laporanPendapatanDetail) {
        this.laporanPendapatanDetail = laporanPendapatanDetail;
    }
    
    
}
