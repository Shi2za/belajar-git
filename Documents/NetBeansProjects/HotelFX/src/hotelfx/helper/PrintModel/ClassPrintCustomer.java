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
public class ClassPrintCustomer {
   long idCustomer;
   String infoCustomer;
   String tipeCustomer;
   String tanggalRegistrasi;
   String noIdentitas;
   String tempatTanggalLahir;
   String jenisKelamin;
   String agama;
   String statusPerkawinan;
   String alamat;
   String negara;
   String noTelepon;
   String email;
   List<ClassPrintRekeningCustomer>rekeningCustomer;
   //List<ClassPrintDetailCustomer>DetailCustomer;
  

    public ClassPrintCustomer() {
    }

    public long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getInfoCustomer() {
        return infoCustomer;
    }

    public void setInfoCustomer(String infoCustomer) {
        this.infoCustomer = infoCustomer;
    }

    public String getTipeCustomer() {
        return tipeCustomer;
    }

    public void setTipeCustomer(String tipeCustomer) {
        this.tipeCustomer = tipeCustomer;
    }

    public String getTanggalRegistrasi() {
        return tanggalRegistrasi;
    }

    public void setTanggalRegistrasi(String tanggalRegistrasi) {
        this.tanggalRegistrasi = tanggalRegistrasi;
    }

    public String getNoIdentitas() {
        return noIdentitas;
    }

    public void setNoIdentitas(String noIdentitas) {
        this.noIdentitas = noIdentitas;
    }

    public String getTempatTanggalLahir() {
        return tempatTanggalLahir;
    }

    public void setTempatTanggalLahir(String tempatTanggalLahir) {
        this.tempatTanggalLahir = tempatTanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }

    public String getStatusPerkawinan() {
        return statusPerkawinan;
    }

    public void setStatusPerkawinan(String statusPerkawinan) {
        this.statusPerkawinan = statusPerkawinan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ClassPrintRekeningCustomer> getRekeningCustomer() {
        return rekeningCustomer;
    }

    public void setRekeningCustomer(List<ClassPrintRekeningCustomer> rekeningCustomer) {
        this.rekeningCustomer = rekeningCustomer;
    }

    
    
}
