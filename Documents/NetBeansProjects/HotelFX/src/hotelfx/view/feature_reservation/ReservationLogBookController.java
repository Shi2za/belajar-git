/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.service.FReservationManager;
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
import javafx.scene.control.Tooltip;
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
public class ReservationLogBookController implements Initializable {

    @FXML
    private AnchorPane ancFormLogBook;

    @FXML
    private GridPane gpFormDataLogBook;

    @FXML
    private AnchorPane ancTableLogBookLayout;

    @FXML
    private JFXTextArea txtDetailLog;

    @FXML
    private JFXButton btnBack;

    private void initFormDataLogBook() {

        btnBack.setTooltip(new Tooltip("kembali ke halaman awal"));
        btnBack.setOnMouseClicked((e) -> {
            dataLogBookBackHandle();
        });

        txtDetailLog.setText("");

        initTableDataSystemLogBook();

    }

    public ClassTableWithControl tableDataSystemLogBook;

    private void initTableDataSystemLogBook() {
        //set table
        setTableDataSystemLogBook();
        //set control
        setTableControlDataSystemLogBook();
        //set table-control to content-view
        ancTableLogBookLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableDataSystemLogBook, 0.0);
        AnchorPane.setLeftAnchor(tableDataSystemLogBook, 0.0);
        AnchorPane.setBottomAnchor(tableDataSystemLogBook, 0.0);
        AnchorPane.setRightAnchor(tableDataSystemLogBook, 0.0);

        ancTableLogBookLayout.getChildren().add(tableDataSystemLogBook);
    }

    private void setTableDataSystemLogBook() {
        TableView<TblSystemLogBook> tableView = new TableView();

        TableColumn<TblSystemLogBook, String> logDateTime = new TableColumn("Tanggal Log");
        logDateTime.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getLogBookDateTime()),
                        param.getValue().logBookDateTimeProperty()));
        logDateTime.setMinWidth(160);

        TableColumn<TblSystemLogBook, String> userName = new TableColumn("Username");
        userName.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblSystemUser().getCodeUser(),
                        param.getValue().tblSystemUserProperty()));
        userName.setMinWidth(140);

        TableColumn<TblSystemLogBook, String> employeeName = new TableColumn("Karyawan");
        employeeName.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(),
                        param.getValue().tblEmployeeByIdemployeeProperty()));
        employeeName.setMinWidth(140);

        TableColumn<TblSystemLogBook, String> headerLog = new TableColumn("Log");
        headerLog.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getLogBookHeader(),
                        param.getValue().logBookHeaderProperty()));
        headerLog.setMinWidth(200);

        tableView.getColumns().addAll(logDateTime, userName, employeeName, headerLog);

        tableView.setItems(loadAllDataSystemLogBook());

        tableView.setRowFactory(tv -> {
            TableRow<TblSystemLogBook> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (tableView.getSelectionModel().getSelectedItems().size() == 1) {
                    txtDetailLog.setText(((TblSystemLogBook) tableView.getSelectionModel().getSelectedItem()).getLogBookDetail());
                } else {
                    txtDetailLog.setText("");
                }
            });
            return row;
        });

        tableDataSystemLogBook = new ClassTableWithControl(tableView);

    }

    private void setTableControlDataSystemLogBook() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataLogBookCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataLogBookUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (true) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataLogBookDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableDataSystemLogBook.addButtonControl(buttonControls);
    }

    public ObservableList<TblSystemLogBook> loadAllDataSystemLogBook() {
//        List<TblSystemLog> list = reservationController.getFReservationManager().getAllDataSystemLog();
//        for (TblSystemLog data : list) {
//            //data system log by
//            data.setRefSystemLogBy(reservationController.getFReservationManager().getDataSystemLogBy(data.getRefSystemLogBy().getIdtype()));
//            //data user
//            data.setTblSystemUser(reservationController.getFReservationManager().getDataUser(data.getTblSystemUser().getIduser()));
//            //data employee
//            data.setTblEmployeeByIdemployee(reservationController.getFReservationManager().getDataEmployee(data.getTblEmployeeByIdemployee().getIdemployee()));
//            //data people
//            data.getTblEmployeeByIdemployee().setTblPeople(reservationController.getFReservationManager().getPeople(data.getTblEmployeeByIdemployee().getTblPeople().getIdpeople()));
//        }
//        return FXCollections.observableArrayList(list);
        return FXCollections.observableArrayList();
    }

    /**
     * HANDLE FOR DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    public int dataInputStatus = 0;

    public Stage dialogStage;

    public TblSystemLogBook selectedDataLogBook;

    private void dataLogBookCreateHandle() {
        dataInputStatus = 0;
        selectedDataLogBook = new TblSystemLogBook();
        //open form data log book input
        showDataLogBookInputDialog();
    }

    private void dataLogBookUpdateHandle() {
        if (tableDataSystemLogBook.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedDataLogBook = new TblSystemLogBook((TblSystemLogBook) tableDataSystemLogBook.getTableView().getSelectionModel().getSelectedItem());
            //open form data log book input
            showDataLogBookInputDialog();
        } else {
            ClassMessage.showWarningSelectedDataMessage("", reservationController.dialogStage);
        }
    }

    private void dataLogBookDeleteHandle() {
        if (tableDataSystemLogBook.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", reservationController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //delete data system log
//                if (reservationController.getFReservationManager().deleteDataSystemLog(new TblSystemLog((TblSystemLog) tableDataSystemLog.getTableView().getSelectionModel().getSelectedItem()))) {
                if(true){
                    ClassMessage.showSucceedDeletingDataMessage("", reservationController.dialogStage);
                    //refresh data from table sytem log
                    tableDataSystemLogBook.getTableView().setItems(loadAllDataSystemLogBook());
                } else {
                    ClassMessage.showFailedDeletingDataMessage(reservationController.getFReservationManager().getErrorMessage(), reservationController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", reservationController.dialogStage);
        }
    }

    private void showDataLogBookInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_reservation/ReservationLogBookInputDialog.fxml"));

            ReservationLogBookInputController controller = new ReservationLogBookInputController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    private void setSelectedDataToInputForm() {
        txtDetailLog.setText("");
    }

    private void dataLogBookBackHandle() {
//        //refresh data
//        reservationController.refreshDataSelectedReservation();
        //close form data log book
        reservationController.dialogStage.close();
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
        initFormDataLogBook();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public ReservationLogBookController(ReservationController parentController) {
        reservationController = parentController;
    }

    private final ReservationController reservationController;

    public FReservationManager getService() {
        return reservationController.getFReservationManager();
    }

}
