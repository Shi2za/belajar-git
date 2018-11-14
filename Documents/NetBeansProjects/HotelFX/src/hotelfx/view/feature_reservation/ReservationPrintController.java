/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReservationPrintController implements Initializable {

    @FXML
    private AnchorPane ancFormPrintLaunguage;

    @FXML
    private GridPane gpFormDataPrintLanguage;

    private ToggleGroup tggPrintLaunguageGroup;

    @FXML
    private JFXRadioButton rdbEnglish;

    @FXML
    private JFXRadioButton rdbIndonesian;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataPrintLanguage() {

        btnPrint.setTooltip(new Tooltip("Cetak"));
        btnPrint.setOnAction((e) -> {
            dataPrintHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCancelHandle();
        });

        tggPrintLaunguageGroup = new ToggleGroup();
        rdbEnglish.setToggleGroup(tggPrintLaunguageGroup);
        rdbIndonesian.setToggleGroup(tggPrintLaunguageGroup);
        
        rdbEnglish.setSelected(true);
        rdbIndonesian.setSelected(false);
        
        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                rdbEnglish, 
                rdbIndonesian);
    }

    private void setSelectedDataToInputForm() {
        
    }

    private void dataPrintHandle() {
        if (checkDataInputDataPrintLanguage()) {
            //do printing
            reservationController.printHandle(rdbEnglish.isSelected() ? 0 : 1);
            //close form data print language input
            reservationController.dialogStage.close();
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataCancelHandle() {
        //close form data print language input
        reservationController.dialogStage.close();
    }

    private String errDataInput;
    
    private boolean checkDataInputDataPrintLanguage() {
        boolean dataInput = true;
        errDataInput = "";
        if (!rdbEnglish.isSelected() && !rdbIndonesian.isSelected()) {
            dataInput = false;
            errDataInput += "Bahasa : Belum ditentukan..!!!\n";
        }
        return dataInput;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataPrintLanguage();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public ReservationPrintController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;
    
}
