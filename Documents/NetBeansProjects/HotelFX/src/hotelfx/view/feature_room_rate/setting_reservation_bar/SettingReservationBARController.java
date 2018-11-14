/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_rate.setting_reservation_bar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblReservationBar;
import hotelfx.persistence.model.TblReservationCalendarBar;
import hotelfx.persistence.model.TblReservationDefaultBar;
import hotelfx.persistence.service.FRoomRateManager;
import hotelfx.view.feature_room_rate.FeatureRoomRateController;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.WEEKS;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class SettingReservationBARController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataCalendarBAR;

    private DoubleProperty dataCalendarBARFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane ancDataCalendarBARLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;
    
    private void setDataCalendarBARSplitpane() {
        spDataCalendarBAR.setDividerPositions(1);
        
        dataCalendarBARFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataCalendarBARFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataCalendarBAR.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataCalendarBAR.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataCalendarBARFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 0.0) {    //enable
                ancDataCalendarBARLayout.setDisable(false);
                ancDataCalendarBARLayoutDisableLayer.setDisable(true);
                ancDataCalendarBARLayout.toFront();
            }
            if (newVal.doubleValue() == 1) {  //disable
                ancDataCalendarBARLayout.setDisable(true);
                ancDataCalendarBARLayoutDisableLayer.setDisable(false);
                ancDataCalendarBARLayoutDisableLayer.toFront();
            }
        });

        dataCalendarBARFormShowStatus.set(0.0);

    }

    /**
     * CALENDAR BAR DATA
     */
    @FXML
    private AnchorPane ancDataCalendarBARLayout;

    private final ObjectProperty<YearMonth> month = new SimpleObjectProperty<>();
    private final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(Locale.getDefault());

    @FXML
    private Label lblSelectedMonth;

    @FXML
    private JFXButton btnPreviousMonth;

    @FXML
    private JFXButton btnNextMonth;

    @FXML
    private JFXButton btnSettingDefaultBAR;

    @FXML
    private GridPane gpDates;

    private final PseudoClass selectingPseudoClass = PseudoClass.getPseudoClass("selecting");
    private final PseudoClass beforePseudoClass = PseudoClass.getPseudoClass("before");
    private final PseudoClass afterPseudoClass = PseudoClass.getPseudoClass("after");

    private void initDataCalendarBARContent() {
        month.addListener((obs, oldMonth, newMonth) -> {
            rebuildDatesGrid();
        });

        btnPreviousMonth.setTooltip(new Tooltip("Sebelum (Bulan)"));
        btnPreviousMonth.setOnAction((e) -> previousMonth());
        btnNextMonth.setTooltip(new Tooltip("Setelah (Bulan)"));
        btnNextMonth.setOnAction((e) -> nextMonth());

        btnSettingDefaultBAR.setTooltip(new Tooltip("Pengaturan (Data Default BAR)"));
        btnSettingDefaultBAR.setOnMouseClicked((e) -> dataDefaultBARUpdateHandle());

        //set now
        month.set(YearMonth.from(LocalDate.now()));

        lblSelectedMonth.textProperty().bind(Bindings.createStringBinding(()
                -> ClassFormatter.convertMonthToIndonesian(month.get().getMonthValue()) + " " + month.get().format(DateTimeFormatter.ofPattern("yyyy")),
                month)
        );
    }

    private void nextMonth() {
        month.set(month.get().plusMonths(1));
    }

    private void previousMonth() {
        month.set(month.get().minusMonths(1));
    }

    private void rebuildDatesGrid() {
        gpDates.getChildren().clear();

        WeekFields weekFields = WeekFields.of(locale.get());

        LocalDate first = month.get().atDay(1);

        int dayOfWeekOfFirst = first.get(weekFields.dayOfWeek());

        LocalDate firstDisplayedDate = first.minusDays(dayOfWeekOfFirst - 1);
        LocalDate last = month.get().atEndOfMonth();
        int dayOfWeekOfLast = last.get(weekFields.dayOfWeek());
        LocalDate lastDisplayedDate = last.plusDays(7 - dayOfWeekOfLast);

        while (WEEKS.between(firstDisplayedDate, lastDisplayedDate) < 5) {
            lastDisplayedDate = lastDisplayedDate.plusDays(7);
        }

        PseudoClass weekendPseudoClass = PseudoClass.getPseudoClass("weekend");
        String cellClass = "month-cell";

        for (LocalDate date = firstDisplayedDate; !date.isAfter(lastDisplayedDate); date = date.plusDays(1)) {
            BorderPane cell = new BorderPane();
            cell.getStyleClass().add(cellClass);
            cell.setId(String.valueOf(date));
            GridPane.setFillHeight(cell, Boolean.TRUE);
            GridPane.setFillWidth(cell, Boolean.TRUE);

            cell.pseudoClassStateChanged(weekendPseudoClass,
                    date.get(weekFields.dayOfWeek()) == 7
                    || date.get(weekFields.dayOfWeek()) == 1
            );

//            cell.setOnMouseClicked((e) -> handleCell(e, cell));
            Label label = new Label(String.valueOf(date.getDayOfMonth()));
            label.getStyleClass().add("date");

            VBox vbox = new VBox();
            vbox.setSpacing(5.0);

            TblReservationCalendarBar reservationCalendarBar = parentController.getFRoomRateManager().getReservationCalendarBARByCalendarDate(Date.valueOf(date));
            if (reservationCalendarBar == null) {
                reservationCalendarBar = new TblReservationCalendarBar();
                reservationCalendarBar.setCalendarDate(Date.valueOf(date));
//                reservationCalendarBar.setTblReservationSeason(new TblReservationSeason());
                reservationCalendarBar.setTblReservationBar(parentController.getFRoomRateManager().getReservationBAR(
                        ((TblReservationDefaultBar) parentController.getFRoomRateManager().getReservationDefaultBARByDayOfWeek(date.getDayOfWeek().getValue())).getTblReservationBar().getIdbar()));
            } else {
                reservationCalendarBar.setTblReservationSeason(parentController.getFRoomRateManager().getReservationSeason(reservationCalendarBar.getTblReservationSeason().getIdseason()));
                reservationCalendarBar.setTblReservationBar(parentController.getFRoomRateManager().getReservationBAR(reservationCalendarBar.getTblReservationBar().getIdbar()));
            }

            CalendarBarButton cbb = new CalendarBarButton(reservationCalendarBar);
            cbb.setDisable(date.isBefore(first) || date.isAfter(last));
            cbb.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    dataReservationCalendarBARUpdateHandle(cbb);
                }
            });

//            CalendarRoomAvailableButton crab = new CalendarRoomAvailableButton();
//            crab.setDisable(date.isBefore(first) || date.isAfter(last));
//            crab.setOnMouseClicked((e) -> {
//                showCalendarRoomAvailableHandle();
//            });
            vbox.getChildren().addAll(cbb/*, crab*/);

            cell.pseudoClassStateChanged(beforePseudoClass, date.isBefore(first));
            cell.pseudoClassStateChanged(afterPseudoClass, date.isAfter(last));

            int dayOfWeek = date.get(weekFields.dayOfWeek());
            int daysSincefirstDisplayed = (int) firstDisplayedDate.until(date, ChronoUnit.DAYS);
            int weeksSinceFirstDisplayed = daysSincefirstDisplayed / 7;

            cell.setRight(label);
            BorderPane.setMargin(label, new Insets(4, 4, 4, 4));

            cell.setLeft(vbox);
            BorderPane.setMargin(vbox, new Insets(4, 0, 0, 0));

//            cell.pseudoClassStateChanged(selectingPseudoClass, cell.getId().equalsIgnoreCase(String.valueOf(selectedDate.get())));
            gpDates.add(cell, dayOfWeek - 1, weeksSinceFirstDisplayed);
        }
    }

    class CalendarBarButton extends JFXButton {

        private TblReservationCalendarBar calendarBarValue;

        public CalendarBarButton(TblReservationCalendarBar reservationCalendarBar) {
            super(reservationCalendarBar.getTblReservationBar().getBarname()
                    + " (" + ClassFormatter.decimalFormat.cFormat(reservationCalendarBar.getTblReservationBar().getBarpercentage()) + "x)");
            this.getStyleClass().add("button-note");
            calendarBarValue = reservationCalendarBar;
        }

        public TblReservationCalendarBar getCalendarBarValue() {
            return calendarBarValue;
        }

        public void setCalendarBarValue(TblReservationCalendarBar barValue) {
            this.calendarBarValue = barValue;
        }

    }

//    class CalendarRoomAvailableButton extends JFXButton {
//
//        public CalendarRoomAvailableButton() {
//            super("Test");
//        }
//
//    }
    /**
     * HANDLE DIALOG DATA INPUT (CALENDAR BAR)
     */
    public Stage dialogStage;

    public TblReservationCalendarBar selectedDataCalendarBAR;

    private void dataReservationCalendarBARUpdateHandle(CalendarBarButton cbb) {
        selectedDataCalendarBAR = cbb.getCalendarBarValue();
        //open dialog data calendar bar
        showReservationCalendarBARDialog();
    }

    private void showReservationCalendarBARDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_room_rate/setting_reservation_bar/SettingReservationBARInputDialog.fxml"));

            SettingReservationBARInputController controller = new SettingReservationBARInputController(this);
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

    public void refreshDataCalendarBAR() {
        rebuildDatesGrid();
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataDefaultBAR;

    @FXML
    private ScrollPane spFormDataDefaultBAR;

    private JFXCComboBoxTablePopup<TblReservationBar>[] arrCBPDays = new JFXCComboBoxTablePopup[7];

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private final TblReservationDefaultBar[] arrDataDefaultBAR = new TblReservationDefaultBar[7];

    private void initFormDataDefaultBAR() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataDefaultBAR.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataDefaultBAR.setOnScroll((ScrollEvent scroll) -> {
            isFormScroll.set(true);

            scrollCounter++;

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        if (scrollCounter == 1) {
                            //scroll end..!
                            isFormScroll.set(false);
                        }
                        scrollCounter--;
                    });
                } catch (Exception e) {
                    System.out.println("err " + e.getMessage());
                }

            });
            thread.setDaemon(true);
            thread.start();
        });

        initDataPopup();
        
        btnSave.setTooltip(new Tooltip("Simpan (Data Default BAR)"));
        btnSave.setOnAction((e) -> {
            dataDefaultBARSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataDefaultBARCancelHandle();
        });

        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                arrCBPDays);
    }

    private void initDataPopup() {
        for (int i = 0; i < arrCBPDays.length; i++) {
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

            ObservableList<TblReservationBar> reservationBARItems = FXCollections.observableArrayList(parentController.getFRoomRateManager().getAllDataReservationBAR());

            arrCBPDays[i] = new JFXCComboBoxTablePopup<>(
                    TblReservationBar.class, tableReservationBAR, reservationBARItems, "", "BAR *", true, 200, 200
            );
            
            arrCBPDays[i].valueProperty().addListener((obs, oldVal, newVal) -> {
                //set unsaving data input -> 'true'
                ClassSession.unSavingDataInput.set(true);
            });

            //attached to grid-pane
            gpFormDataDefaultBAR.add(arrCBPDays[i], 2, i + 2);
        }
    }

    private void refreshDataPopup() {
        for (int i = 0; i < arrCBPDays.length; i++) {
            ObservableList<TblReservationBar> barItems = FXCollections.observableArrayList(parentController.getFRoomRateManager().getAllDataReservationBAR());
            arrCBPDays[i].setItems(barItems);
        }
    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        for (int i = 0; i < arrCBPDays.length; i++) {
            arrCBPDays[i].valueProperty().bindBidirectional(arrDataDefaultBAR[i].tblReservationBarProperty());
            arrCBPDays[i].hide();
        }
    }

    /**
     * HANDLE FROM DATA INPUT (DEFAULT BAR)
     */
    private void dataDefaultBARUpdateHandle() {
        for (int i = 0; i < arrDataDefaultBAR.length; i++) {
            arrDataDefaultBAR[i] = parentController.getFRoomRateManager().getReservationDefaultBARByDayOfWeek(i + 1);
            arrDataDefaultBAR[i].setTblReservationBar(parentController.getFRoomRateManager().getReservationBAR(arrDataDefaultBAR[i].getTblReservationBar().getIdbar()));
        }
        setSelectedDataToInputForm();
        //open form data default bar
        dataCalendarBARFormShowStatus.set(1);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private void dataDefaultBARSaveHandle() {
        if (checkDataInputDataDefaultBAR()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblReservationDefaultBar[] dummyArrDataDefaultBAR = new TblReservationDefaultBar[arrDataDefaultBAR.length];
                for (int i = 0; i < dummyArrDataDefaultBAR.length; i++) {
                    TblReservationDefaultBar dummyDataDefaultBAR = new TblReservationDefaultBar(arrDataDefaultBAR[i]);
                    dummyDataDefaultBAR.setTblReservationBar(new TblReservationBar(dummyDataDefaultBAR.getTblReservationBar()));
                    dummyArrDataDefaultBAR[i] = dummyDataDefaultBAR;
                }
                if (parentController.getFRoomRateManager().updateDataReservationDefaultBAR(dummyArrDataDefaultBAR)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", null);
                    //refresh data calendar bar & close form data default bar
                    rebuildDatesGrid();
                    dataCalendarBARFormShowStatus.set(0);
                    //set unsaving data input -> 'false'
                    ClassSession.unSavingDataInput.set(false);
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(parentController.getFRoomRateManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataDefaultBARCancelHandle() {
        //refresh data calendar bar & close form data default bar
        rebuildDatesGrid();
        dataCalendarBARFormShowStatus.set(0);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    private String errDataInput;

    private boolean checkDataInputDataDefaultBAR() {
        boolean dataInput = true;
        errDataInput = "";
        for (int i = 0; i < arrCBPDays.length; i++) {
            if (arrCBPDays[i].getValue() == null) {
                dataInput = false;
                errDataInput += "Default Data(" + ClassFormatter.convertDayToIndonesian(i + 1) + ") : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            }
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
        //set splitpane
        setDataCalendarBARSplitpane();

        //init data content
        initDataCalendarBARContent();

        //init form
        initFormDataDefaultBAR();

        spDataCalendarBAR.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataCalendarBARFormShowStatus.set(0);
        });
    }

    public SettingReservationBARController(FeatureRoomRateController parentController) {
        this.parentController = parentController;
    }

    private final FeatureRoomRateController parentController;

    public FRoomRateManager getService() {
        return parentController.getFRoomRateManager();
    }

}
