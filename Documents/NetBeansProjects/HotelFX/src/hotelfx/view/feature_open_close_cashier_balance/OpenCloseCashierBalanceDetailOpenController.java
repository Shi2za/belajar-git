/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_open_close_cashier_balance;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.RefOpenCloseCashierBalanceStatus;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblOpenCloseCashierBalance;
import java.net.URL;
import java.util.List;
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
public class OpenCloseCashierBalanceDetailOpenController implements Initializable {

    @FXML
    private AnchorPane ancFormDetail;

    @FXML
    private GridPane gpFormDataDetail;

    @FXML
    private JFXTextField txtBeginBalanceNominal;

    @FXML
    private AnchorPane ancCashierLayout;
    private JFXCComboBoxTablePopup<TblEmployee> cbpCashier;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataDetail() {
        initDataDetailPopup();

        btnSave.setTooltip(new Tooltip("Buka Kas Kasir"));
        btnSave.setOnAction((e) -> {
            dataDetailSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDetailCancelHandle();
        });

        initImportantFieldColor();
        
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpCashier);
    }
    
    private void initDataDetailPopup() {
        //Employee
        TableView<TblEmployee> tableEmployee = new TableView<>();

        TableColumn<TblEmployee, String> codeEmployee = new TableColumn("ID Karyawan");
        codeEmployee.setCellValueFactory(cellData -> cellData.getValue().codeEmployeeProperty());
        codeEmployee.setMinWidth(140);

        TableColumn<TblEmployee, String> nameEmployee = new TableColumn("Nama Karyawan");
        nameEmployee.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPeople().getFullName(), param.getValue().getTblPeople().fullNameProperty()));
        nameEmployee.setMinWidth(140);

        TableColumn<TblEmployee, String> nameJob = new TableColumn("Jabatan");
        nameJob.setCellValueFactory((TableColumn.CellDataFeatures<TblEmployee, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblJob().getJobName(), param.getValue().tblJobProperty()));
        nameJob.setMinWidth(140);

        tableEmployee.getColumns().addAll(codeEmployee, nameEmployee, nameJob);

        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(loadAllDataEmployee());

        cbpCashier = new JFXCComboBoxTablePopup<>(
                TblEmployee.class, tableEmployee, employeeItems, "", "Kasir *", true, 440, 200
        );
        
        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpCashier, 0.0);
        AnchorPane.setLeftAnchor(cbpCashier, 0.0);
        AnchorPane.setRightAnchor(cbpCashier, 0.0);
        AnchorPane.setTopAnchor(cbpCashier, 0.0);
        ancCashierLayout.getChildren().clear();
        ancCashierLayout.getChildren().add(cbpCashier);
    }

    private List<TblEmployee> loadAllDataEmployee() {
        List<TblEmployee> list = openCloseCashierBalanceController.getFOpenCloseCashierBalanceManager().getAllDataEmployee();
        for (TblEmployee data : list) {
            //data people
            data.setTblPeople(openCloseCashierBalanceController.getFOpenCloseCashierBalanceManager().getDataPeople(data.getTblPeople().getIdpeople()));
            //data job
            data.setTblJob(openCloseCashierBalanceController.getFOpenCloseCashierBalanceManager().getDataJob(data.getTblJob().getIdjob()));
        }
        return list;
    }

    private void refreshDataPopup() {
        //Employee
        ObservableList<TblEmployee> employeeItems = FXCollections.observableArrayList(openCloseCashierBalanceController.getFOpenCloseCashierBalanceManager().getAllDataEmployee());
        cbpCashier.setItems(employeeItems);
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();
        
        txtBeginBalanceNominal.setText(ClassFormatter.currencyFormat.cFormat(openCloseCashierBalanceController.selectedData.getBeginBalanceNominal()));

        cbpCashier.valueProperty().bindBidirectional(openCloseCashierBalanceController.selectedData.tblEmployeeByIdcashierProperty());

        cbpCashier.hide();
    }

    private void dataDetailSaveHandle() {
        if (checkDataInputDataDetail()) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin membuka kas kasir?", "", openCloseCashierBalanceController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblOpenCloseCashierBalance dummyOpenCloseCashierBalance = new TblOpenCloseCashierBalance(openCloseCashierBalanceController.selectedData);
                dummyOpenCloseCashierBalance.setTblEmployeeByIdcashier(new TblEmployee(dummyOpenCloseCashierBalance.getTblEmployeeByIdcashier()));
                if(dummyOpenCloseCashierBalance.getRefOpenCloseCashierBalanceStatus() != null){
                    dummyOpenCloseCashierBalance.setRefOpenCloseCashierBalanceStatus(new RefOpenCloseCashierBalanceStatus(dummyOpenCloseCashierBalance.getRefOpenCloseCashierBalanceStatus()));
                }
                if (openCloseCashierBalanceController.getFOpenCloseCashierBalanceManager().insertOpenCloseCashierBalance(dummyOpenCloseCashierBalance) != null) {
                    HotelFX.showAlert(Alert.AlertType.INFORMATION, "BERHASIL", "Berhasil membuka kas kasir..!!", "", openCloseCashierBalanceController.dialogStage);
                    //refresh data
                    openCloseCashierBalanceController.refreshData();
                    //close form data detail
                    openCloseCashierBalanceController.dialogStage.close();
                } else {
                    HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Gagal membuka kas kasir..!!", openCloseCashierBalanceController.getFOpenCloseCashierBalanceManager().getErrorMessage(), openCloseCashierBalanceController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, openCloseCashierBalanceController.dialogStage);
        }
    }

    private void dataDetailCancelHandle() {
        //close form data detail
        openCloseCashierBalanceController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataDetail() {
        boolean dataInput = true;
        errDataInput = "";
        if (cbpCashier.getValue() == null) {
            dataInput = false;
            errDataInput += "Cashier : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataDetail();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public OpenCloseCashierBalanceDetailOpenController(OpenCloseCashierBalanceController parentController) {
        openCloseCashierBalanceController = parentController;
    }

    private final OpenCloseCashierBalanceController openCloseCashierBalanceController;

}
