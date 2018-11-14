/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_v2;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXRadioButton;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblTravelAgent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReservationRoomTypeDetailReservationTypeInputController implements Initializable {

    @FXML
    private AnchorPane ancFormReservationType;

    @FXML
    private GridPane gpFormDataReservationType;

    private ToggleGroup tggReservationTypeGroup;

    @FXML
    private JFXRadioButton rdbCustomer;

    @FXML
    private JFXRadioButton rdbTravelAgent;

    @FXML
    private AnchorPane ancTravelAgentLayout;

    private JFXCComboBoxTablePopup<TblTravelAgent> cbpTravelAgent;

    @FXML
    private JFXButton btnNext;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataReservationType() {
        initDataReservationTypePopup();

        btnNext.setTooltip(new Tooltip("Proses Selanjutnya.."));
        btnNext.setOnAction((e) -> {
            dataReservationTypeNextHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataReservationTypeCancelHandle();
        });

        tggReservationTypeGroup = new ToggleGroup();
        rdbCustomer.setToggleGroup(tggReservationTypeGroup);
        rdbTravelAgent.setToggleGroup(tggReservationTypeGroup);
        
        rdbTravelAgent.selectedProperty().addListener((obs, oldVal, newVal) -> {
            setSelectedDataToInputForm();
            ancTravelAgentLayout.setVisible(newVal);
        });
        
        rdbCustomer.setSelected(true);
        rdbTravelAgent.setSelected(false);
        
        ancTravelAgentLayout.setVisible(false);
        
        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                rdbCustomer, 
                rdbTravelAgent, 
                cbpTravelAgent);
    }
    
    private void initDataReservationTypePopup() {
        //Travel Agent
        TableView<TblTravelAgent> tableTravelAgent = new TableView<>();
        
        TableColumn<TblTravelAgent, String> codeTravelAgent = new TableColumn("ID");
        codeTravelAgent.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getCodePartner(), param.getValue().tblPartnerProperty()));
        codeTravelAgent.setMinWidth(120);
        
        TableColumn<TblTravelAgent, String> travelAgentName = new TableColumn("Travel Agent");
        travelAgentName.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgent, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPartnerName(), param.getValue().tblPartnerProperty()));
        travelAgentName.setMinWidth(140);
        
        tableTravelAgent.getColumns().addAll(codeTravelAgent, travelAgentName);

        ObservableList<TblTravelAgent> travelAgentItems = FXCollections.observableArrayList(loadAllDataTravelAgent());

        cbpTravelAgent = new JFXCComboBoxTablePopup<>(
                TblTravelAgent.class, tableTravelAgent, travelAgentItems, "", "Travel Agent *", false, 200, 250
        );
        
        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpTravelAgent, 0.0);
        AnchorPane.setLeftAnchor(cbpTravelAgent, 0.0);
        AnchorPane.setRightAnchor(cbpTravelAgent, 0.0);
        AnchorPane.setTopAnchor(cbpTravelAgent, 0.0);
        ancTravelAgentLayout.getChildren().add(cbpTravelAgent);
    }

    private List<TblTravelAgent> loadAllDataTravelAgent(){
        List<TblTravelAgent> list = reservationController.getFReservationManager().getAllDataTravelAgent();
        for(TblTravelAgent ta : list){
            ta.setTblPartner(reservationController.getFReservationManager().getPartner(ta.getTblPartner().getIdpartner()));
        }
        return list;
    }
    
    private void refreshDataPopup() {
        //Travel Agent
        ObservableList<TblTravelAgent> travelAgentItems = FXCollections.observableArrayList(loadAllDataTravelAgent());
        cbpTravelAgent.setItems(travelAgentItems);
    }

    private void setSelectedDataToInputForm() {
        refreshDataPopup();

        cbpTravelAgent.hide();
    }

    private void dataReservationTypeNextHandle() {
        if (checkDataInputDataReservationType()) {
            if (rdbCustomer.isSelected()) {   //Customer
                //reservation type : customer
                reservationController.reservationType = reservationController.getFReservationManager().getReservationOrderByType(0);
            } else {//Travel Agent
                //reservation type : travel agent
                reservationController.reservationType = reservationController.getFReservationManager().getReservationOrderByType(1);
                //data travel agent
                reservationController.currentTravelAgent = cbpTravelAgent.getValue();
            }
            //close and load room type detail input
//            reservationController.dataReservationRoomTypeDetailCreateHandle();
            reservationController.dataReservationCreateHandle();
            reservationController.dialogStage.close();
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, reservationController.dialogStage);
        }
    }

    private void dataReservationTypeCancelHandle() {
        //close form data customer input
        reservationController.dialogStage.close();
    }

    private String errDataInput;
    
    private boolean checkDataInputDataReservationType() {
        boolean dataInput = true;
        errDataInput = "";
        if (!rdbCustomer.isSelected() && !rdbTravelAgent.isSelected()) {
            dataInput = false;
            errDataInput += "Reservasi Oleh : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (rdbTravelAgent.isSelected()) {
            if (cbpTravelAgent.getValue() == null) {
                dataInput = false;
                errDataInput += "Travel Agent : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
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
        initFormDataReservationType();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public ReservationRoomTypeDetailReservationTypeInputController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;
    
}
