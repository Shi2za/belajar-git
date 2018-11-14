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
public class ClassPrintSchedule {
    String jadwalKerja;
    List<ClassPrintScheduleDetail>detailJadwal;

    public ClassPrintSchedule() {
    }

    public String getJadwalKerja() {
        return jadwalKerja;
    }

    public void setJadwalKerja(String jadwalKerja) {
        this.jadwalKerja = jadwalKerja;
    }

    public List<ClassPrintScheduleDetail> getDetailJadwal() {
        return detailJadwal;
    }

    public void setDetailJadwal(List<ClassPrintScheduleDetail> detailJadwal) {
        this.detailJadwal = detailJadwal;
    }
}
