/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.HotelFX;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author ANDRI
 */
public class ClassMessage {
    
    public static String defaultErrorNullValueMessage = "data tidak boleh kosong";
    
    public static String defaultErrorZeroValueMessage = "data harus lebih besar dari pada '0'";
    
    //--------------------------------------------------------------------------
    
    public static void showWarningSelectedDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK ADA YANG DIPILIH", "Silahkan pilih data terlebih dahulu..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.WARNING, "TIDAK ADA YANG DIPILIH", "Silahkan pilih data terlebih dahulu..!!", content, dialogStage);
        }
    }
    
    public static void showWarningInputDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan periksa data masukan..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.WARNING, "DATA MASUKAN TIDAK SESUAI", "Silahkan periksa data masukan..!!", content, dialogStage);
        }
    }
    
    public static Alert showConfirmationSavingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menyimpan data ini?", content);
        }else{
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menyimpan data ini?", content, dialogStage);
        }
    }
    
    public static void showFailedInsertingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal disimpan..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal disimpan..!!", content, dialogStage);
        }
    }
    
    public static void showSucceedInsertingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil disimpan..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil disimpan..!!", content, dialogStage);
        }
    }
    
    public static void showFailedUpdatingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal diubah..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal diubah..!!", content, dialogStage);
        }
    }
    
    public static void showSucceedUpdatingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil diubah..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil diubah..!!", content, dialogStage);
        }
    }
    
    public static Alert showConfirmationDeletingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menghapus data ini?", content);
        }else{
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menghapus data ini?", content, dialogStage);
        }
    }
    
    public static void showFailedDeletingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dihapus..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dihapus..!!", content, dialogStage);
        }
    }
    
    public static void showSucceedDeletingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dihapus..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dihapus..!!", content, dialogStage);
        }
    }
    
    public static void showFailedAddingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal ditambahkan..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal ditambahkan..!!", content, dialogStage);
        }
    }
    
    public static void showSucceedAddingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil ditambahkan..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil ditambahkan..!!", content, dialogStage);
        }
    }
    
    public static Alert showConfirmationRemovingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin membuang data ini?", content);
        }else{
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin membuang data ini?", content, dialogStage);
        }
    }
    
    public static void showFailedRemovingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dibuang..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dibuang..!!", content, dialogStage);
        }
    }
    
    public static void showSucceedRemovingDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dibuang..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dibuang..!!", content, dialogStage);
        }
    }
    
     public static void showFailedSendingEmailDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dibuang..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Data gagal dibuang..!!", content, dialogStage);
        }
    }
    
    public static void showSucceedSendingEmailDataMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dikirim..!!", content);
        }else{
            HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Data berhasil dikirim..!!", content, dialogStage);
        }
    }
     
    public static void showWarningSelectedDataFromDateNow(String content,Stage dialogStage){
       if(dialogStage == null){
          HotelFX.showAlert(Alert.AlertType.WARNING,"DATA YANG DIPILIH SALAH","Silahkan pilih data mulai dari tanggal hari ini..!!", content);
       }
       else{
         HotelFX.showAlert(Alert.AlertType.WARNING,"DATA YANG DIPILIH SALAH","Silahkan pilih data mulai dari tanggal hari ini..!!", content,dialogStage);
       }
    }
    
    public static Alert showConfirmationUnSavingDataInputMessage(String content, Stage dialogStage){
        if(dialogStage == null){
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Data belum tersimpan, apakah anda nyakin untuk keluar dari halaman ini?", content);
        }else{
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Data belum tersimpan, apakah anda nyakin untuk keluar dari halaman ini?", content, dialogStage);
        }
    }
    
    public static Alert showConfirmationSendingEmail(String content, Stage dialogStage){
        if(dialogStage == null){
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk mengirim data ini?", content);
        }else{
            return HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk mengirim data ini?", content,dialogStage);
        }
    }
}
