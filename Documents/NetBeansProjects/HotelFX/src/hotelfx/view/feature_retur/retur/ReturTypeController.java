/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur;

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
public class ReturTypeController implements Initializable {

    @FXML
    private AnchorPane ancFormReturType;

    @FXML
    private GridPane gpFormDataReturType;

    private ToggleGroup tggReturTypeGroup;
    
    @FXML
    private JFXRadioButton rbtChangeWithItem;

    @FXML
    private JFXRadioButton rbtChangeWithMoney;
    
    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetail() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Retur)"));
        btnSave.setOnAction((e) -> {
            dataReturSaveHandle();
        });
        
        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReturCancelHandle();
        });
        
        initImportantFieldColor();
        
        tggReturTypeGroup = new ToggleGroup();
        rbtChangeWithItem.setToggleGroup(tggReturTypeGroup);
        rbtChangeWithMoney.setToggleGroup(tggReturTypeGroup);

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                rbtChangeWithItem, 
                rbtChangeWithMoney);
    }
    
    private void setSelectedDataToInputForm() {

    }

    private void dataReturSaveHandle() {
        if (checkDataInputDataReturType()) {
            //save data retur by retur type to database
            if(rbtChangeWithItem.isSelected()){ //set retut type -> 1 = 'Tukar Barang', create data PO
                returController.selectedData.setRefReturType(returController.getService().getDataReturType(1));
            }else{  //set retut type -> 2 = 'Kembali Uang', create data Hotel-Receivable
                returController.selectedData.setRefReturType(returController.getService().getDataReturType(2));
            }
            //save data retur
            returController.dataReturSaveHandle();
            //close form data detail
            returController.dialogStageDetal.close();
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, returController.dialogStageDetal);
        }
    }

    private void dataReturCancelHandle() {
        //close form data retur type
        returController.dialogStageDetal.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataReturType() {
        boolean dataInput = true;
        errDataInput = "";
        if (!rbtChangeWithItem.isSelected() && !rbtChangeWithMoney.isSelected()) {
            dataInput = false;
            errDataInput += "Jenis Retur : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public ReturTypeController(ReturController parentController) {
        returController = parentController;
    }

    private final ReturController returController;
    
}
