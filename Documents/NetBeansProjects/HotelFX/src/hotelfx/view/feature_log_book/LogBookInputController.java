/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_log_book;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.helper.tagbar.BaseModel;
import hotelfx.helper.tagbar.DummyJobModel;
import hotelfx.helper.tagbar.MYFXOptMenu;
import hotelfx.helper.tagbar.MYFXTagsBar;
import hotelfx.persistence.model.TblEmployee;
import hotelfx.persistence.model.TblJob;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.model.TblSystemLogBookJob;
import hotelfx.persistence.model.TblSystemUser;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class LogBookInputController implements Initializable {

    @FXML
    private AnchorPane ancFormLogBookInput;

    @FXML
    private GridPane gpFormDataLogBookInput;

    @FXML
    private JFXTextField txtHeaderLog;

    @FXML
    private JFXTextArea txtDataLog;

    @FXML
    private FlowPane fpToJobTagbarLayout;
    
    @FXML
    private JFXCheckBox chbReminder;

    @FXML
    private JFXDatePicker dtpReminderDate;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataLogBookInput() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Log Book)"));
        btnSave.setOnAction((e) -> {
            dataSystemLogBookSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataSystemLogBookCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtHeaderLog,
                txtDataLog, 
                dtpReminderDate);
    }

    private void setSelectedDataToInputForm() {

        txtHeaderLog.textProperty().bindBidirectional(logBookController.selectedDataLogBook.logBookHeaderProperty());

        txtDataLog.textProperty().bindBidirectional(logBookController.selectedDataLogBook.logBookDetailProperty());
        
        chbReminder.selectedProperty().addListener((obs, oldVal, newVal) -> {
            dtpReminderDate.setVisible(newVal);
        });
        chbReminder.selectedProperty().bindBidirectional(logBookController.selectedDataLogBook.isReminderProperty());
        
        if(logBookController.selectedDataLogBook.getReminderDate() != null){
            dtpReminderDate.setValue(logBookController.selectedDataLogBook.getReminderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }else{
            dtpReminderDate.setValue(null);
        }
        dtpReminderDate.setVisible(logBookController.selectedDataLogBook.getIsReminder());

        initToJobTagbar();
        
    }

    private MYFXTagsBar toJobTagsBar;
    
    private void initToJobTagbar(){
        ObservableList<BaseModel> items = FXCollections.observableArrayList();
        
        List<TblJob> listJob = loadAllDataJob();
        
        for(TblJob dataJob : listJob){
            items.add(new DummyJobModel(dataJob));
        }
        
        MYFXOptMenu myfxom = new MYFXOptMenu(DummyJobModel.class, "To : ", items);
        toJobTagsBar = new MYFXTagsBar(DummyJobModel.class, myfxom, "Jabatan");
        
        if(logBookController.dataInputStatus == 1){ //update
            setDataToJob(items);
        }
        
//        toJobTagsBar.setDisable(logBookController.dataInputStatus == 1);    //update
        
        fpToJobTagbarLayout.getChildren().add(toJobTagsBar);
    }
    
    private List<TblJob> loadAllDataJob(){
//        List<TblJob> list = new ArrayList<>();
//        if(logBookController.dataInputStatus == 0){ //insert
//            list = logBookController.getFLogBookManager().getAllDataJob();
//        }else{  //update
//            List<TblSystemLogBookJob> systemLogBookJobs = logBookController.getFLogBookManager().getAllDataSystemLogBookJobByIDSystemLogBook(logBookController.selectedDataLogBook.getIdlogBook());
//            for(TblSystemLogBookJob systemLogBookJob : systemLogBookJobs){
//                list.add(logBookController.getFLogBookManager().getDataJob(systemLogBookJob.getTblJob().getIdjob()));
//            }
//        }
        List<TblJob> list = logBookController.getFLogBookManager().getAllDataJob();
        return list;
    }
    
    private void setDataToJob(ObservableList<BaseModel> items){
        List<TblSystemLogBookJob> systemLogBookJobs = logBookController.getFLogBookManager().getAllDataSystemLogBookJobByIDSystemLogBook(logBookController.selectedDataLogBook.getIdlogBook());
        for(TblSystemLogBookJob systemLogBookJob : systemLogBookJobs){
            for(BaseModel item : items){
                if(((DummyJobModel)item).getDataJob().getIdjob() == systemLogBookJob.getTblJob().getIdjob()){
                    toJobTagsBar.getSelectedItems().add(item);
                    break;
                }
            }
        }
    }
    
    private void dataSystemLogBookSaveHandle() {
        if (checkDataInputDataLogBookInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", logBookController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                if(logBookController.selectedDataLogBook.getIsReminder()){
                    logBookController.selectedDataLogBook.setReminderDate(Date.from(dtpReminderDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }else{
                    logBookController.selectedDataLogBook.setReminderDate(null);
                }
                //dummy entry
                TblSystemLogBook dummySelectedData = new TblSystemLogBook(logBookController.selectedDataLogBook);
                if (dummySelectedData.getTblSystemUser() != null) {
                    dummySelectedData.setTblSystemUser(new TblSystemUser(dummySelectedData.getTblSystemUser()));
                }
                if (dummySelectedData.getTblEmployeeByIdemployee() != null) {
                    dummySelectedData.setTblEmployeeByIdemployee(new TblEmployee(dummySelectedData.getTblEmployeeByIdemployee()));
                }
                List<TblSystemLogBookJob> dummySystemLogBookJobs = new ArrayList<>();
                for(DummyJobModel dummyJobModel : (List<DummyJobModel>)toJobTagsBar.getSelectedItems()){
                    TblSystemLogBookJob dummySystemLogBookJob = new TblSystemLogBookJob();
                    dummySystemLogBookJob.setTblSystemLogBook(dummySelectedData);
                    dummySystemLogBookJob.setTblJob(new TblJob(dummyJobModel.getDataJob()));
                    dummySystemLogBookJobs.add(dummySystemLogBookJob);
                }
                switch (logBookController.dataInputStatus) {
                    case 0: //insert
                        if (logBookController.getFLogBookManager().insertDataSystemLogBook(dummySelectedData, 
                                dummySystemLogBookJobs) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", logBookController.dialogStage);
                            //refresh data log book
                            logBookController.refreshDataLogBook();
                            //close form data log book input
                            logBookController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(logBookController.getFLogBookManager().getErrorMessage(), logBookController.dialogStage);
                        }
                        break;
                    case 1: //update
                        if (logBookController.getFLogBookManager().updateDataSystemLogBook(dummySelectedData, 
                                dummySystemLogBookJobs)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", logBookController.dialogStage);
                            //refresh data log book
                            logBookController.refreshDataLogBook();
                            //close form data log book input
                            logBookController.dialogStage.close();
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(logBookController.getFLogBookManager().getErrorMessage(), logBookController.dialogStage);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, logBookController.dialogStage);
        }
    }

    private void dataSystemLogBookCancelHandle() {
        //refresh data log book
        logBookController.refreshDataLogBook();
        //close form data log book input
        logBookController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataLogBookInput() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtHeaderLog.getText() == null || txtHeaderLog.getText().equals("")) {
            dataInput = false;
            errDataInput += "Subject : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (txtDataLog.getText() == null || txtDataLog.getText().equals("")) {
            dataInput = false;
            errDataInput += "Message : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (chbReminder.isSelected()) {
            if (dtpReminderDate.getValue() == null) {
                dataInput = false;
                errDataInput += "Tanggal (Reminder) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
        }
        if(toJobTagsBar.getSelectedItems().isEmpty()){
            dataInput = false;
            errDataInput += "To (Tujuan) : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        initFormDataLogBookInput();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public LogBookInputController(LogBookController parentController) {
        logBookController = parentController;
    }

    private final LogBookController logBookController;
    
}
