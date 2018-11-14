/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_rate.setting_reservation_bar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationSeason;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class SettingReservationBARInputController implements Initializable {

    @FXML
    private AnchorPane ancFormReservationCalendarBARInput;

    @FXML
    private GridPane gpFormDataReservationCalendarBARInput;

    @FXML
    private JFXTextField txtCalendarBARDate;

    private JFXCComboBoxTablePopup<TblReservationBar> cbpReservationBAR;

    private JFXCComboBoxTablePopup<TblReservationSeason> cbpReservationSeason;

    @FXML
    private JFXButton btnSaveCalendarBAR;

    @FXML
    private JFXButton btnCancelCalendarBAR;

    private void initFormDataReservationCalendarBARInput() {
        initDataPopup();

        btnSaveCalendarBAR.setTooltip(new Tooltip("Simpan (Data BAR)"));
        btnSaveCalendarBAR.setOnAction((e) -> {
            dataReservationCalendarBARInputSaveHandle();
        });

        btnCancelCalendarBAR.setTooltip(new Tooltip("Batal"));
        btnCancelCalendarBAR.setOnAction((e) -> {
            dataReservationCalendarBARInputCancelHandle();
        });

        initImportantFieldColor();
        
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpReservationBAR, 
                cbpReservationSeason);
    }
    
    private void initDataPopup() {
        //Reservation BAR
        TableView<TblReservationBar> tableReservationBAR = new TableView<>();

        TableColumn<TblReservationBar, String> reservationBARName = new TableColumn<>("BAR");
        reservationBARName.setCellValueFactory(cellData -> cellData.getValue().barnameProperty());
        reservationBARName.setMinWidth(140);

        TableColumn<TblReservationBar, String> reservationBARPercentage = new TableColumn<>("Multiple");
        reservationBARPercentage.setCellValueFactory((TableColumn.CellDataFeatures<TblReservationBar, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.decimalFormat.cFormat(param.getValue().getBarpercentage()), param.getValue().barpercentageProperty()));
        reservationBARPercentage.setMinWidth(120);

        tableReservationBAR.getColumns().addAll(reservationBARName, reservationBARPercentage);

        ObservableList<TblReservationBar> reservationBARItems = FXCollections.observableArrayList(settingReservationBARController.getService().getAllDataReservationBAR());

        cbpReservationBAR = new JFXCComboBoxTablePopup<>(
                TblReservationBar.class, tableReservationBAR, reservationBARItems, "", "BAR *", true, 160, 200
        );
        
        //Reservation Season
        TableView<TblReservationSeason> tableReservationSeason = new TableView<>();
        TableColumn<TblReservationSeason, String> reservationSeasonName = new TableColumn<>("Season");
        reservationSeasonName.setCellValueFactory(cellData -> cellData.getValue().seasonNameProperty());
        reservationSeasonName.setMinWidth(140);

        tableReservationSeason.getColumns().addAll(reservationSeasonName);

        ObservableList<TblReservationSeason> reservationSeasonItems = FXCollections.observableArrayList(settingReservationBARController.getService().getAllDataReservationSeason());

        cbpReservationSeason = new JFXCComboBoxTablePopup<>(
                TblReservationSeason.class, tableReservationSeason, reservationSeasonItems, "", "Season *", true, 160, 200
        );

        //attached to grid-pane
        gpFormDataReservationCalendarBARInput.add(cbpReservationBAR, 0, 1);
        gpFormDataReservationCalendarBARInput.add(cbpReservationSeason, 1, 1);
    }

    private void refreshDataPopup() {
        //Reservation BAR
        ObservableList<TblReservationBar> barItems = FXCollections.observableArrayList(settingReservationBARController.getService().getAllDataReservationBAR());
        cbpReservationBAR.setItems(barItems);
        //Reservation Season
        ObservableList<TblReservationSeason> seasonItems = FXCollections.observableArrayList(settingReservationBARController.getService().getAllDataReservationSeason());
        cbpReservationSeason.setItems(seasonItems);
    }
    
    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtCalendarBARDate.textProperty().bind(Bindings.createStringBinding(() -> ClassFormatter.dateFormate.format(settingReservationBARController.selectedDataCalendarBAR.getCalendarDate()),
                settingReservationBARController.selectedDataCalendarBAR.calendarDateProperty()));

        cbpReservationBAR.valueProperty().bindBidirectional(settingReservationBARController.selectedDataCalendarBAR.tblReservationBarProperty());
        cbpReservationSeason.valueProperty().bindBidirectional(settingReservationBARController.selectedDataCalendarBAR.tblReservationSeasonProperty());

        //close
        cbpReservationBAR.hide();
        cbpReservationSeason.hide();

    }

    private void dataReservationCalendarBARInputSaveHandle() {
        if (checkDataInputDataEmployeeScheduleOfDateInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", settingReservationBARController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblReservationCalendarBar dummySelectedDataCalendarBAR = new TblReservationCalendarBar(settingReservationBARController.selectedDataCalendarBAR);
                dummySelectedDataCalendarBAR.setTblReservationBar(new TblReservationBar(dummySelectedDataCalendarBAR.getTblReservationBar()));
                dummySelectedDataCalendarBAR.setTblReservationSeason(new TblReservationSeason(dummySelectedDataCalendarBAR.getTblReservationSeason()));
                if (settingReservationBARController.getService().updateDataReservationCalendarBAR(dummySelectedDataCalendarBAR)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", settingReservationBARController.dialogStage);
                    settingReservationBARController.refreshDataCalendarBAR();
                    settingReservationBARController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(settingReservationBARController.getService().getErrorMessage(), settingReservationBARController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, settingReservationBARController.dialogStage);
        }
    }

    private void dataReservationCalendarBARInputCancelHandle() {
        settingReservationBARController.refreshDataCalendarBAR();
        settingReservationBARController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataEmployeeScheduleOfDateInput() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpReservationBAR.getValue() == null) {
            dataInput = false;
            errDataInput += "BAR : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpReservationSeason.getValue() == null) {
            dataInput = false;
            errDataInput += "Season : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        return dataInput;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataReservationCalendarBARInput();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public SettingReservationBARInputController(SettingReservationBARController parentController) {
        settingReservationBARController = parentController;
    }

    private final SettingReservationBARController settingReservationBARController;

}
