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
public class ClassPrintLaporanPendapatanTambahan {
   String createdBy;
   Date tanggalReservasi;
   List<ClassPrintLaporanPendapatanTambahanDetail>laporanPendapatanDetail;

    public ClassPrintLaporanPendapatanTambahan() {
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getTanggalReservasi() {
        return tanggalReservasi;
    }

    public void setTanggalReservasi(Date tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }

    public List<ClassPrintLaporanPendapatanTambahanDetail> getLaporanPendapatanDetail() {
        return laporanPendapatanDetail;
    }

    public void setLaporanPendapatanDetail(List<ClassPrintLaporanPendapatanTambahanDetail> laporanPendapatanDetail) {
        this.laporanPendapatanDetail = laporanPendapatanDetail;
    }
   
   
}
