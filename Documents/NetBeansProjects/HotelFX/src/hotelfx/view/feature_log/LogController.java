/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_log;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.persistence.model.LogSystem;
import hotelfx.persistence.service.FLogManager;
import hotelfx.persistence.service.FLogManagerImpl;
import hotelfx.view.DashboardController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class LogController implements Initializable {

    @FXML
    private AnchorPane ancFormLog;

    @FXML
    private GridPane gpFormDataLog;

    @FXML
    private AnchorPane ancTableLogLayout;

    @FXML
    private JFXTextArea txtDetailLog;

    private void initFormDataLog() {

        txtDetailLog.setText("");

        initTableDataLog();

    }

    public ClassTableWithControl tableDataLog;

    private void initTableDataLog() {
        //set table
        setTableDataLog();
        //set control
        setTableControlDataLog();
        //set table-control to content-view
        ancTableLogLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataLog, 0.0);
        AnchorPane.setLeftAnchor(tableDataLog, 0.0);
        AnchorPane.setBottomAnchor(tableDataLog, 0.0);
        AnchorPane.setRightAnchor(tableDataLog, 0.0);

        ancTableLogLayout.getChildren().add(tableDataLog);
    }

    private void setTableDataLog() {
        TableView<LogSystem> tableView = new TableView();

        TableColumn<LogSystem, String> logDateTime = new TableColumn("Tanggal");
        logDateTime.setCellValueFactory((TableColumn.CellDataFeatures<LogSystem, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getLogDateTime()),
                        param.getValue().logDateTimeProperty()));
        logDateTime.setMinWidth(160);

        TableColumn<LogSystem, String> userName = new TableColumn("Username");
        userName.setCellValueFactory((TableColumn.CellDataFeatures<LogSystem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSystemUser().getCodeUser(),
                        param.getValue().tblSystemUserProperty()));
        userName.setMinWidth(140);

        TableColumn<LogSystem, String> employeeName = new TableColumn("Karyawan");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<LogSystem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployee().getTblPeople().getFullName(),
                        param.getValue().tblEmployeeProperty()));
        employeeName.setMinWidth(140);

        TableColumn<LogSystem, String> headerLog = new TableColumn("Log");
        headerLog.setCellValueFactory((TableColumn.CellDataFeatures<LogSystem, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getLogHeader(),
                        param.getValue().logHeaderProperty()));
        headerLog.setMinWidth(250);

        tableView.getColumns().addAll(logDateTime, userName, employeeName, headerLog);

        tableView.setItems(loadAllDataLog());

        tableView.setRowFactory(tv -> {
            TableRow<LogSystem> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (tableView.getSelectionModel().getSelectedItems().size() == 1) {
                    txtDetailLog.setText(((LogSystem) tableView.getSelectionModel().getSelectedItem()).getLogDetail());
                } else {
                    txtDetailLog.setText("");
                }
            });
            return row;
        });

        tableDataLog = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataLog() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
//        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {            
//            buttonControl = new JFXButton();
//            buttonControl.setText("Tambah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener add
//                dataLogCreateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {            
//            buttonControl = new JFXButton();
//            buttonControl.setText("Ubah");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener update
//                dataLogUpdateHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
//        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Hapus");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener delete
//                dataLogDeleteHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Cetak");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                dataLogPrintHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataLog.addButtonControl(buttonControls);
    }

    public ObservableList<LogSystem> loadAllDataLog() {
        List<LogSystem> list = fLogManager.getAllDataLog();
        for (LogSystem data : list) {
            //data user
            data.setTblSystemUser(fLogManager.getDataUser(data.getTblSystemUser().getIduser()));
            //data employee
            data.setTblEmployee(fLogManager.getDataEmployee(data.getTblEmployee().getIdemployee()));
            //data people
            data.getTblEmployee().setTblPeople(fLogManager.getDataPeople(data.getTblEmployee().getTblPeople().getIdpeople()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputStatus = 0;

    public Stage dialogStage;

    public LogSystem selecteddDataLog;

//    private void dataLogCreateHandle() {
//        dataInputStatus = 0;
//        selecteddDataLog = new LogSystem();
//        //open form data log input
//        showDataLogInputDialog();
//    }
//
//    private void dataLogUpdateHandle() {
//        if (tableDataLog.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            dataInputStatus = 1;
//            selecteddDataLog = new LogSystem((LogSystem) tableDataLog.getTableView().getSelectionModel().getSelectedItem());
//            //open form data log input
//            showDataLogInputDialog();
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }
//
//    private void dataLogDeleteHandle() {
//        if (tableDataLog.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
//            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
//            if (alert.getResult() == ButtonType.OK) {
//                //delete data system log
//                if (fLogManager.deleteDataLog(new LogSystem((LogSystem) tableDataLog.getTableView().getSelectionModel().getSelectedItem()))) {
//                    ClassMessage.showSucceedDeletingDataMessage("", null);
//                    //refresh data from table sytem log
//                    tableDataLog.getTableView().setItems(loadAllDataLog());
//                    //refresh data form input
//                    setSelectedDataToInputForm();
//                } else {
//                    ClassMessage.showFailedDeletingDataMessage(fLogManager.getErrorMessage(), null);
//                }
//            }
//        } else {
//            ClassMessage.showWarningSelectedDataMessage("", null);
//        }
//    }

    private void dataLogPrintHandle() {
        if (tableDataLog.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //print data system log
                //...
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    } 
    
//    private void showDataLogInputDialog() {
//        try {
//            // Load the fxml file and create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_log/LogInputDialog.fxml"));
//
//            LogInputController controller = new LogInputController(this);
//            loader.setController(controller);
//
//            Region page = loader.load();
//
//            // Create the dialog Stage.
//            dialogStage = new Stage();
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(HotelFX.primaryStage);
//
//            //undecorated
//            Undecorator undecorator = new Undecorator(dialogStage, page);
//            undecorator.getStylesheets().add("skin/undecorator.css");
//            undecorator.getMenuButton().setVisible(false);
//            undecorator.getMaximizeButton().setVisible(false);
//            undecorator.getMinimizeButton().setVisible(false);
//            undecorator.getFullScreenButton().setVisible(false);
//            undecorator.getCloseButton().setVisible(false);
//
//            Scene scene = new Scene(undecorator);
//            scene.setFill(Color.TRANSPARENT);
//
//            dialogStage.initStyle(StageStyle.TRANSPARENT);
//            dialogStage.setScene(scene);
//            dialogStage.setResizable(false);
//
//            // Show the dialog and wait until the user closes it
//            dialogStage.showAndWait();
//        } catch (IOException e) {
//            System.out.println("Err >> " + e.toString());
//        }
//    }

    public void setSelectedDataToInputForm() {
        txtDetailLog.setText("");
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FLogManager fLogManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service
        fLogManager = new FLogManagerImpl();
        //init form input
        initFormDataLog();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public FLogManager getFLogManager() {
        return fLogManager;
    }

}
