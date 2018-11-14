/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_log_book;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.model.TblSystemLogBook;
import hotelfx.persistence.model.TblSystemLogBookJob;
import hotelfx.persistence.service.FLogBookManager;
import hotelfx.persistence.service.FLogBookManagerImpl;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
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
    private Label lblHotelName;

    @FXML
    private HBox hbFunctionalButtonLayout;

    private ClassFilteringGridPane cfgp;

    private JFXButton btnCreateLogBook;

    @FXML
    private AnchorPane ancDataLogBookLayout;

    @FXML
    private ScrollPane spDataLogBookLayout;

    @FXML
    private VBox vbDetailDataLogBookLayout;

    private void initFormDataLogBook() {
        //set css
        ancDataLogBookLayout.getStyleClass().add("anc-layout-log-book");
        spDataLogBookLayout.getStyleClass().add("sp-layout-log-book");
        vbDetailDataLogBookLayout.getStyleClass().add("vb-layout-log-book");
        //header
        initHeaderContent();
        //body
        initBodyContent();
    }

    private void initHeaderContent() {
        hbFunctionalButtonLayout.getChildren().clear();
        hbFunctionalButtonLayout.setSpacing(10.0);

        //field searching
        cfgp = new ClassFilteringGridPane(logBooks);
        hbFunctionalButtonLayout.getChildren().add(cfgp);

        //button add
        btnCreateLogBook = new JFXButton(" ");
        btnCreateLogBook.setTooltip(new Tooltip("Tambah LohBook"));
        btnCreateLogBook.setButtonType(JFXButton.ButtonType.RAISED);
        btnCreateLogBook.getStyleClass().add("button-create-log-book");
        btnCreateLogBook.setPrefSize(25, 30);
        btnCreateLogBook.setOnMouseClicked((e) -> {
            //create handle
            dataLogBookCreateHandle();
        });
        hbFunctionalButtonLayout.getChildren().add(btnCreateLogBook);
    }

    public class ClassFilteringGridPane extends AnchorPane {

        private final HBox backgroundLayout = new HBox();

        private final JFXButton btnSearch = new JFXButton(" ");
        private final AnchorPane ancBtnSearchLayout = new AnchorPane();

        private final JFXTextField txtFilter = new JFXTextField("");
        private final AnchorPane ancTxtFilterLayout = new AnchorPane();

        private ObservableList<TblSystemLogBook> items = FXCollections.observableArrayList();

        public ClassFilteringGridPane(
                ObservableList<TblSystemLogBook> items) {
            this.getStyleClass().add(DEFAULT_STYLE_CLASS);

            this.items.setAll(items);

            txtFilter.setText("");
            txtFilter.setPromptText("Search");
            txtFilter.setLabelFloat(false);
            txtFilter.setPrefSize(200, 30);
            txtFilter.getStyleClass().add("text-filtering");
            txtFilter.setOnKeyReleased((e) -> {
                //set filter
                if (e.getCode() == KeyCode.ENTER) {
                    setLogBook(this.items);
                } else {
                    String filter = String.valueOf(txtFilter.getText());
                    if (filter == null || filter.length() == 0) {
                        setLogBook(this.items);
                    } else {
                        doFiltering(filter);
                    }
                }
            });
            AnchorPane.setBottomAnchor(txtFilter, 0.0);
            AnchorPane.setLeftAnchor(txtFilter, 0.0);
            AnchorPane.setRightAnchor(txtFilter, 0.0);
            AnchorPane.setTopAnchor(txtFilter, 0.0);
            ancTxtFilterLayout.setPrefSize(200, 30);
            ancTxtFilterLayout.getChildren().clear();
            ancTxtFilterLayout.getChildren().add(txtFilter);

            btnSearch.setPrefSize(15, 15);
            btnSearch.getStyleClass().add("button-search");
            btnSearch.setDisable(true);
            AnchorPane.setBottomAnchor(btnSearch, 7.5);
            AnchorPane.setLeftAnchor(btnSearch, 7.5);
            AnchorPane.setRightAnchor(btnSearch, 7.5);
            AnchorPane.setTopAnchor(btnSearch, 7.5);
            ancBtnSearchLayout.setPrefSize(30, 30);
            ancBtnSearchLayout.getChildren().clear();
            ancBtnSearchLayout.getChildren().add(btnSearch);

            backgroundLayout.setSpacing(5);
            backgroundLayout.setPrefSize(195, 30);
            backgroundLayout.getChildren().addAll(ancBtnSearchLayout, ancTxtFilterLayout);

            this.getChildren().add(backgroundLayout);
        }

        private void doFiltering(String filter) {
            FilteredList<TblSystemLogBook> fl = new FilteredList(this.items, i -> true);
            fl.setPredicate(item -> {
                String data;
                //subject
                data = ((TblSystemLogBook) item).getLogBookHeader();
                if (String.valueOf(data).toLowerCase()
                        .contains(filter.toLowerCase())) {
                    return true;
                }
                //from
                data = ((TblSystemLogBook) item).getTblEmployeeByIdemployee().getCodeEmployee()
                        + " - "
                        + ((TblSystemLogBook) item).getTblEmployeeByIdemployee().getTblPeople().getFullName();
                if (String.valueOf(data).toLowerCase()
                        .contains(filter.toLowerCase())) {
                    return true;
                }
                //to
                data = getDataTo(((TblSystemLogBook) item));
                if (String.valueOf(data).toLowerCase()
                        .contains(filter.toLowerCase())) {
                    return true;
                }
                //create date
                data = ClassFormatter.dateTimeFormate.format(((TblSystemLogBook) item).getLogBookDateTime());
                if (String.valueOf(data).toLowerCase()
                        .contains(filter.toLowerCase())) {
                    return true;
                }
                //reminder date
                data = ((TblSystemLogBook) item).getReminderDate() != null
                        ? ClassFormatter.dateFormate.format(((TblSystemLogBook) item).getReminderDate())
                        : "";
                if (!data.equals("")) {
                    if (String.valueOf(data).toLowerCase()
                            .contains(filter.toLowerCase())) {
                        return true;
                    }
                }
                //message
                data = ((TblSystemLogBook) item).getLogBookDetail();
                if (String.valueOf(data).toLowerCase()
                        .contains(filter.toLowerCase())) {
                    return true;
                }
                return false;
            });
            setLogBook(fl);
        }

        public void refreshFilterItems(ObservableList<TblSystemLogBook> items) {
            this.items.setAll(items);
            this.txtFilter.setText("");
        }

        /**
         * *************************************************************************
         *                                                                         *
         * Stylesheet Handling * *
         * ************************************************************************
         */
        /**
         * Initialize the style class to 'jfx-tool-bar'.
         *
         * This is the selector class from which CSS can be used to style this
         * control.
         */
        private static final String DEFAULT_STYLE_CLASS = "class-filtering-table";

    }

    private void initBodyContent() {
        //refresh data log book
        refreshDataLogBook();
    }

    private ObservableList<TblSystemLogBook> logBooks = FXCollections.observableArrayList(new ArrayList<>());

    public void setLogBook(ObservableList<TblSystemLogBook> dataLogBooks) {
        vbDetailDataLogBookLayout.getChildren().clear();
        vbDetailDataLogBookLayout.setSpacing(15.0);
        for (int i = 0; i < dataLogBooks.size(); i++) {
            LogBookGridPane gp = getLogBookGridPane(dataLogBooks.get(i));
            gp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
            vbDetailDataLogBookLayout.getChildren().add(gp);
        }
    }

    private LogBookGridPane getLogBookGridPane(TblSystemLogBook dataLogBook) {
        LogBookGridPane gp = new LogBookGridPane(dataLogBook);
        gp.getStyleClass().add("gp-log-book");
        return gp;
    }

    public class LogBookGridPane extends GridPane {

        private final ObjectProperty<TblSystemLogBook> dataLogBook = new SimpleObjectProperty<>();

        public LogBookGridPane(TblSystemLogBook dataLogBook) {
            //set gap
            setHgap(15.0);
            setVgap(10.0);
            /**
             * COLUMN
             */
            //left
            getColumnConstraints().add(new ColumnConstraints(5.0));
            //col 1 (label)
            getColumnConstraints().add(new ColumnConstraints(140.0));
            //col 2 (data)
            getColumnConstraints().add(new ColumnConstraints(250.0));
            //col 3 (empty space - 1)
            getColumnConstraints().add(new ColumnConstraints(0.0, 725.0, 725.0));
            //col 4 (funtional button 1 : 'udapte')
            getColumnConstraints().add(new ColumnConstraints(25.0));
            //col 5 (funtional button 2 : 'delete')
            getColumnConstraints().add(new ColumnConstraints(25.0));
            //right
            getColumnConstraints().add(new ColumnConstraints(5.0));
            /**
             * ROW
             */
            //top
            getRowConstraints().add(new RowConstraints(10.0));
            //subject
            getRowConstraints().add(new RowConstraints(30.0));
            //space
            getRowConstraints().add(new RowConstraints(5.0));
            //from
            getRowConstraints().add(new RowConstraints(30.0));
            //to
            getRowConstraints().add(new RowConstraints(30.0));
            //create date
            getRowConstraints().add(new RowConstraints(30.0));
            //reminder date
            getRowConstraints().add(new RowConstraints(30.0));
            //detail (+message)
            getRowConstraints().add(new RowConstraints(USE_COMPUTED_SIZE));
            //bottom
            getRowConstraints().add(new RowConstraints(25.0));
            /**
             * DATA
             */
            Label lblSubjectInput = new Label(dataLogBook.getLogBookHeader());
            lblSubjectInput.getStyleClass().add("lbl-title-subject-input");
            add(lblSubjectInput, 1, 1);
            JFXButton btnUpdate = new JFXButton(" ");
            btnUpdate.setTooltip(new Tooltip("Ubah Data"));
            btnUpdate.setButtonType(JFXButton.ButtonType.RAISED);
            btnUpdate.getStyleClass().add("button-update-log-book");
            btnUpdate.setPrefSize(25, 30);
            btnUpdate.setVisible(setAvailableUpdateDeleteButton(dataLogBook));
            btnUpdate.setOnMouseClicked((e) -> {
                //update listener
                dataLogBookUpdateHandle(dataLogBook);
            });
            add(btnUpdate, 4, 1);
            JFXButton btnDelete = new JFXButton(" ");
            btnDelete.setTooltip(new Tooltip("Hapus Data"));
            btnDelete.setButtonType(JFXButton.ButtonType.RAISED);
            btnDelete.getStyleClass().add("button-delete-log-book");
            btnDelete.setPrefSize(25, 30);
            btnDelete.setVisible(setAvailableUpdateDeleteButton(dataLogBook));
            btnDelete.setOnMouseClicked((e) -> {
                //delete listener
                dataLogBookDeleteHandle(dataLogBook);
            });
            add(btnDelete, 5, 1);

            Label lblFrom = new Label("From");
            lblFrom.getStyleClass().add("lbl-title");
            add(lblFrom, 1, 3);
            Label lblFromInput = new Label(dataLogBook.getTblEmployeeByIdemployee().getCodeEmployee()
                    + " - "
                    + dataLogBook.getTblEmployeeByIdemployee().getTblPeople().getFullName());
            lblFromInput.getStyleClass().add("lbl-title-input");
            add(lblFromInput, 2, 3);

            Label lblTo = new Label("To");
            lblTo.getStyleClass().add("lbl-title");
            add(lblTo, 1, 4);
            Label lblToInput = new Label(getDataTo(dataLogBook));
            lblToInput.getStyleClass().add("lbl-title-input");
            add(lblToInput, 2, 4, 2, 1);

            Label lblCreateDate = new Label("Create Date");
            lblCreateDate.getStyleClass().add("lbl-title");
            add(lblCreateDate, 1, 5);
            Label lblCreateDateInput = new Label(ClassFormatter.dateTimeFormate.format(dataLogBook.getLogBookDateTime()));
            lblCreateDateInput.getStyleClass().add("lbl-title-input");
            add(lblCreateDateInput, 2, 5);

            Label lblReminderDate = new Label("Reminder Date");
            lblReminderDate.getStyleClass().add("lbl-title");
            add(lblReminderDate, 1, 6);
            Label lblReminderDateInput = new Label(dataLogBook.getReminderDate() != null
                    ? ClassFormatter.dateFormate.format(dataLogBook.getReminderDate())
                    : "");
            lblReminderDateInput.getStyleClass().add("lbl-title-reminder-input");
            add(lblReminderDateInput, 2, 6);

            AnchorPane ancDetail = new AnchorPane();
            ancDetail.getStyleClass().add("anc-detail-log-book");

            Label lblMessageInput = new Label(dataLogBook.getLogBookDetail());
            lblMessageInput.getStyleClass().add("lbl-title-input");
            lblMessageInput.setAlignment(Pos.TOP_LEFT);
            AnchorPane.setBottomAnchor(lblMessageInput, 10.0);
            AnchorPane.setLeftAnchor(lblMessageInput, 10.0);
            AnchorPane.setRightAnchor(lblMessageInput, 10.0);
            AnchorPane.setTopAnchor(lblMessageInput, 10.0);
            ancDetail.getChildren().clear();
            ancDetail.getChildren().add(lblMessageInput);

            add(ancDetail, 3, 3, 3, 5);
        }

        public ObjectProperty<TblSystemLogBook> dataLogBookProperty() {
            return dataLogBook;
        }

        public TblSystemLogBook getDataLogBook() {
            return dataLogBookProperty().get();
        }

        public void setDataLogBook(TblSystemLogBook dataLogBook) {
            dataLogBookProperty().set(dataLogBook);
        }

    }

    private String getDataTo(TblSystemLogBook dataSystemLogBook) {
        String result = "";
        if (dataSystemLogBook != null) {
            List<TblSystemLogBookJob> dataLogBookJobs = fLogBooksManager.getAllDataSystemLogBookJobByIDSystemLogBook(dataSystemLogBook.getIdlogBook());
            for (int i = 0; i < dataLogBookJobs.size(); i++) {
                result += dataLogBookJobs.get(i).getTblJob().getJobName();
                if (i < dataLogBookJobs.size() - 1) {
                    result += ", ";
                }
            }
        }
        return result;
    }

    private boolean setAvailableUpdateDeleteButton(TblSystemLogBook dataLogBook) {
        //check current user with data log book
        return dataLogBook.getTblEmployeeByIdemployee().getIdemployee()
                == ClassSession.currentUser.getTblEmployeeByIdemployee().getIdemployee();
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

    private void dataLogBookUpdateHandle(TblSystemLogBook dataLogBook) {
        dataInputStatus = 1;
        selectedDataLogBook = new TblSystemLogBook(dataLogBook);
        //open form data log book input
        showDataLogBookInputDialog();
    }

    private void dataLogBookDeleteHandle(TblSystemLogBook dataLogBook) {
        Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
        if (alert.getResult() == ButtonType.OK) {
            //delete data system log
            if (fLogBooksManager.deleteDataSystemLogBook(new TblSystemLogBook(dataLogBook))) {
                ClassMessage.showSucceedDeletingDataMessage("", null);
                //refresh data log book
                refreshDataLogBook();
            } else {
                ClassMessage.showFailedDeletingDataMessage(fLogBooksManager.getErrorMessage(), null);
            }
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

    }

    public void refreshDataLogBook() {
        //get data log book
        logBooks = loadAllDataSystemLogBook();
        //refresh data log book
        setLogBook(logBooks);
        cfgp.refreshFilterItems(FXCollections.observableArrayList(logBooks));
        //refresh data form input
        setSelectedDataToInputForm();
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
