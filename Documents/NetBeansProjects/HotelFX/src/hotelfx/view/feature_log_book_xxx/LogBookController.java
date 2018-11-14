/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_log_book_xxx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.model.TblSystemLogBookJob;
import hotelfx.persistence.service.FLogBookManager;
import hotelfx.persistence.service.FLogBookManagerImpl;
import hotelfx.view.DashboardController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
public class LogBookController implements Initializable {

    @FXML
    private AnchorPane ancFormLogBook;

    @FXML
    private GridPane gpFormDataLogBook;

    @FXML
    private AnchorPane ancTableLogBookLayout;

    @FXML
    private JFXTextField txtFrom;

    @FXML
    private JFXTextArea txtTo;
    
    @FXML
    private JFXTextField txtCreatedDate;

    @FXML
    private JFXTextField txtReminderDate;

    @FXML
    private JFXTextArea txtDetailLog;

    private void initFormDataLogBook() {

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
        logDateTime.setMinWidth(150);
        
        TableColumn<TblSystemLogBook, String> headerLog = new TableColumn("Subject");
        headerLog.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getLogBookHeader(),
                        param.getValue().logBookHeaderProperty()));
        headerLog.setMinWidth(180);
        
        TableColumn<TblSystemLogBook, String> from = new TableColumn("From");
        from.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getCodeEmployee() 
                        + " - "
                        + param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName(),
                        param.getValue().tblEmployeeByIdemployeeProperty()));
        from.setMinWidth(220);

        TableColumn<TblSystemLogBook, String> to = new TableColumn("To");
        to.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> getDataToV1(param.getValue()),
                        param.getValue().idlogBookProperty()));
        to.setMinWidth(180);
        
        tableView.getColumns().addAll(logDateTime, headerLog, from, to);

        tableView.setItems(loadAllDataSystemLogBook());

        tableView.setRowFactory(tv -> {
            TableRow<TblSystemLogBook> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (tableView.getSelectionModel().getSelectedItems().size() == 1) {
                    TblSystemLogBook dataRow = (TblSystemLogBook) tableView.getSelectionModel().getSelectedItem();
                    txtFrom.setText(dataRow.getTblEmployeeByIdemployee().getCodeEmployee()
                            + " - "
                            + dataRow.getTblEmployeeByIdemployee().getTblPeople().getFullName());
                    txtTo.setText(getDataToV2(dataRow));
                    txtCreatedDate.setText(ClassFormatter.dateTimeFormate.format(dataRow.getLogBookDateTime()));
                    txtReminderDate.setText(dataRow.getReminderDate() != null
                            ? ClassFormatter.dateFormate.format(dataRow.getReminderDate())
                            : "");
                    txtDetailLog.setText(dataRow.getLogBookDetail());
                } else {
                    txtFrom.setText("");
                    txtTo.setText("");
                    txtCreatedDate.setText("");
                    txtReminderDate.setText("");
                    txtDetailLog.setText("");
                }
            });
            return row;
        });

        tableDataSystemLogBook = new ClassTableWithControl(tableView);

    }

    private String getDataToV1(TblSystemLogBook dataSystemLogBook){
        String result = "";
        if(dataSystemLogBook != null){
            List<TblSystemLogBookJob> dataLogBookJobs = fLogBooksManager.getAllDataSystemLogBookJobByIDSystemLogBook(dataSystemLogBook.getIdlogBook());
            for(int i=0; i<dataLogBookJobs.size(); i++){
                result += dataLogBookJobs.get(i).getTblJob().getJobName();
                if(i < dataLogBookJobs.size()-1){
                    result += "\n";
                }
            }
        }
        return result;
    }
    
    private String getDataToV2(TblSystemLogBook dataSystemLogBook){
        String result = "";
        if(dataSystemLogBook != null){
            List<TblSystemLogBookJob> dataLogBookJobs = fLogBooksManager.getAllDataSystemLogBookJobByIDSystemLogBook(dataSystemLogBook.getIdlogBook());
            for(int i=0; i<dataLogBookJobs.size(); i++){
                result += dataLogBookJobs.get(i).getTblJob().getJobName();
                if(i < dataLogBookJobs.size()-1){
                    result += ", ";
                }
            }
        }
        return result;
    }
    
    private void setTableControlDataSystemLogBook() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataLogBookCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataLogBookUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
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
        List<TblSystemLogBook> list = new ArrayList<>();
        if (ClassSession.currentUser.getTblEmployeeByIdemployee().getTblJob() == null) {
            list = fLogBooksManager.getAllDataSystemLogBook();
            for (TblSystemLogBook data : list) {
                //data user
                data.setTblSystemUser(fLogBooksManager.getDataUser(data.getTblSystemUser().getIduser()));
                //data employee
                data.setTblEmployeeByIdemployee(fLogBooksManager.getDataEmployee(data.getTblEmployeeByIdemployee().getIdemployee()));
                //data people
                data.getTblEmployeeByIdemployee().setTblPeople(fLogBooksManager.getDataPeople(data.getTblEmployeeByIdemployee().getTblPeople().getIdpeople()));
            }
        } else {
            List<TblSystemLogBookJob> systemLogBookJobs = fLogBooksManager.getAllDataSystemLogBookJobByIDJob(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblJob().getIdjob());
            for (TblSystemLogBookJob systemLogBookJob : systemLogBookJobs) {
                TblSystemLogBook data = fLogBooksManager.getDataSystemLogBook(systemLogBookJob.getTblSystemLogBook().getIdlogBook());
                //data user
                data.setTblSystemUser(fLogBooksManager.getDataUser(data.getTblSystemUser().getIduser()));
                //data employee
                data.setTblEmployeeByIdemployee(fLogBooksManager.getDataEmployee(data.getTblEmployeeByIdemployee().getIdemployee()));
                //data people
                data.getTblEmployeeByIdemployee().setTblPeople(fLogBooksManager.getDataPeople(data.getTblEmployeeByIdemployee().getTblPeople().getIdpeople()));
                list.add(data);
            }
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
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataLogBookDeleteHandle() {
        if (tableDataSystemLogBook.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //delete data system log
                if (fLogBooksManager.deleteDataSystemLogBook(new TblSystemLogBook((TblSystemLogBook) tableDataSystemLogBook.getTableView().getSelectionModel().getSelectedItem()))) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table sytem log
                    tableDataSystemLogBook.getTableView().setItems(loadAllDataSystemLogBook());
                    //refresh data form input
                    setSelectedDataToInputForm();
                } else {
                    ClassMessage.showFailedDeletingDataMessage(fLogBooksManager.getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void showDataLogBookInputDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_log_book/LogBookInputDialog.fxml"));

            LogBookInputController controller = new LogBookInputController(this);
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

    public void setSelectedDataToInputForm() {
        txtFrom.setText("");
        txtTo.setText("");
        txtCreatedDate.setText("");
        txtReminderDate.setText("");
        txtDetailLog.setText("");
        txtDetailLog.setText("");
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FLogBookManager fLogBooksManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service
        fLogBooksManager = new FLogBookManagerImpl();
        //init form input
        initFormDataLogBook();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public FLogBookManager getFLogBookManager() {
        return fLogBooksManager;
    }

}
