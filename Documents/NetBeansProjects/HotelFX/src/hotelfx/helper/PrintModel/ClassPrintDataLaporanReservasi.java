/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.PrintModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Andreas
 */
public class ClassPrintDataLaporanReservasi {
    String createdBy;
    List<ClassPrintDataLaporanReservasiDetail>reservasiDetail;

    public ClassPrintDataLaporanReservasi() {
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<ClassPrintDataLaporanReservasiDetail> getReservasiDetail() {
        return reservasiDetail;
    }

    public void setReservasiDetail(List<ClassPrintDataLaporanReservasiDetail> reservasiDetail) {
        this.reservasiDetail = reservasiDetail;
    }
}
