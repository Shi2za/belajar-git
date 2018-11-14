/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCDatePicker;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class ReminderPageController implements Initializable {

    @FXML
    private AnchorPane ancFormReminder;

    @FXML
    private GridPane gpFormDataReminder;

    @FXML
    private JFXCDatePicker cdtpRemainderDate;

    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();

    private void initFormDataReminder() {
        //set current date
        selectedDate.set(LocalDate.now());
        //init table data reminder
        initTableDataReminder();
        //set selected date - listener
        selectedDate.addListener((obs, oldVal, newVal) -> {
            tableDataReminder.getItems().setAll(FXCollections.observableArrayList(loadAllDataReminder(newVal)));
            cft.refreshFilterItems(tableDataReminder.getItems());
        });
        //set binding - custom date picker
        JFXButton arrowButtonContent = new JFXButton();
        arrowButtonContent.setPrefSize(185, 25);
        arrowButtonContent.setButtonType(JFXButton.ButtonType.RAISED);
        arrowButtonContent.setTooltip(new Tooltip("Pilih Tanggal.."));
        arrowButtonContent.setOnMouseClicked((e) -> cdtpRemainderDate.show());
        arrowButtonContent.textProperty().bind(Bindings.createStringBinding(()
                -> (cdtpRemainderDate.valueProperty().get() != null)
                        ? cdtpRemainderDate.valueProperty().get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "", cdtpRemainderDate.valueProperty()));
        cdtpRemainderDate.setArrowButtonContent(arrowButtonContent);
        cdtpRemainderDate.valueProperty().bindBidirectional(selectedDate);
    }

    private ClassFilteringTable<TblSystemLogBook> cft;

    @FXML
    private AnchorPane tableDataReminderLayout;
    private TableView tableDataReminder;

    private void initTableDataReminder() {
        setTableDataReminder();

        tableDataReminderLayout.getChildren().clear();

        AnchorPane.setTopAnchor(tableDataReminder, 0.0);
        AnchorPane.setLeftAnchor(tableDataReminder, 0.0);
        AnchorPane.setBottomAnchor(tableDataReminder, 0.0);
        AnchorPane.setRightAnchor(tableDataReminder, 0.0);

        tableDataReminderLayout.getChildren().add(tableDataReminder);
    }

    private void setTableDataReminder() {
        TableView<TblSystemLogBook> tableView = new TableView();

        TableColumn<TblSystemLogBook, String> logDateTime = new TableColumn("Tanggal Buat");
        logDateTime.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.dateTimeFormate.format(param.getValue().getLogBookDateTime()),
                        param.getValue().logBookDateTimeProperty()));
        logDateTime.setMinWidth(200);

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

        TableColumn<TblSystemLogBook, String> by = new TableColumn("Dibuat Oleh");
        by.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblEmployeeByIdemployee().getTblPeople().getFullName()
                        + " (" + (param.getValue().getTblEmployeeByIdemployee().getTblJob() != null ? param.getValue().getTblEmployeeByIdemployee().getTblJob().getJobName() : "-") + ")",
                        param.getValue().tblEmployeeByIdemployeeProperty()));
        by.setMinWidth(300);

        TableColumn<TblSystemLogBook, String> reminder = new TableColumn("Message");
        reminder.setCellValueFactory((TableColumn.CellDataFeatures<TblSystemLogBook, String> param)
                -> Bindings.createStringBinding(()
                        -> "Subject : " + param.getValue().getLogBookHeader() + " \n\n"
                        + "Message : \n" + param.getValue().getLogBookDetail(),
                        param.getValue().logBookHeaderProperty()));
        reminder.setMinWidth(800);

        tableView.getColumns().addAll(logDateTime, by, reminder);

        tableView.setItems(FXCollections.observableArrayList(loadAllDataReminder(selectedDate.get())));
        
        tableDataReminder = tableView;
        
        //set filter
        cft = new ClassFilteringTable<>(
                TblSystemLogBook.class,
                tableDataReminder,
                tableDataReminder.getItems());
        
        gpFormDataReminder.add(cft, 3, 1);
    }

    private List<TblSystemLogBook> loadAllDataReminder(LocalDate date) {
        List<TblSystemLogBook> list = new ArrayList<>();
        if (date != null) {
            list = fLoginManager.getAllDataCurrentReminderBySelectedDate(ClassSession.currentUser, date);
            for (TblSystemLogBook data : list) {
                //data user
                data.setTblSystemUser(fLoginManager.getDataUserAccount(data.getTblSystemUser().getIduser()));
                //data employee
                data.setTblEmployeeByIdemployee(fLoginManager.getDataEmployee(data.getTblEmployeeByIdemployee().getIdemployee()));
                //data job
                if (data.getTblEmployeeByIdemployee().getTblJob() != null) {
                    data.getTblEmployeeByIdemployee().setTblJob(fLoginManager.getDataJob(data.getTblEmployeeByIdemployee().getTblJob().getIdjob()));
                }
                //data people
                data.getTblEmployeeByIdemployee().setTblPeople(fLoginManager.getDataPeople(data.getTblEmployeeByIdemployee().getTblPeople().getIdpeople()));
            }
        }
        return list;
    }

    private void setSelectedDataToInputForm() {

    }

    /**
     * Service
     */
    private FLoginManager fLoginManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service
        fLoginManager = new FLoginManagerImpl();
        //init form input
        initFormDataReminder();
        //refresh data form input
        setSelectedDataToInputForm();
    }

}
