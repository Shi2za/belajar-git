/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCDatePicker;
import com.jfoenix.controls.JFXCustomToolBar;
import com.jfoenix.controls.JFXTabPane;
import hotelfx.helper.ClassTableWithControl;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.naming.Binding;
import hotelfx.HotelFX;
import hotelfx.view.dialog.ReservationTransactionDialogController;
import insidefx.undecorator.Undecorator;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReservationController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init table content
        initTableContent();
        //init calendar content
        initCalendarContent();
        //init basic button
        initButtonSaveCacel();
        initButtonBillForm();
    }

    /**
     * Content
     */
    @FXML
    JFXTabPane tabPane;

    /**
     * Table
     */
    @FXML
    Tab tabTable;

    @FXML
    SplitPane spH;

    @FXML
    SplitPane spV;

    @FXML
    SplitPane spH1;

    @FXML
    SplitPane spH2;

    @FXML
    AnchorPane apReservationDetail;

    @FXML
    AnchorPane apReservation;

    @FXML
    AnchorPane apReservationRoomDetail;

    @FXML
    AnchorPane apReservationPAGDetail;

    @FXML
    AnchorPane apReservationFABDetail;

    @FXML
    AnchorPane apReservationAmenityDetail;

    @FXML
    AnchorPane apReservationPropertyBarcodeDetail;

    @FXML
    AnchorPane apReservationServiceDetail;
    
    @FXML
    AnchorPane apReservationTransaction;
    
    @FXML
    AnchorPane apReservationCheckInOut;

    @FXML
    JFXButton btnSave;
    
    @FXML
    JFXButton btnCancel;
    
    @FXML
    JFXButton btnChargeInfo;
    
    @FXML
    JFXButton btnRestoDebtInfo;
    
    @FXML
    JFXButton btnPrintBill;
    
    @FXML
    JFXButton btnPrintTransactionPayment;
    
    @FXML
    JFXButton btnReservaionPaymentStatus;
    
    private void initButtonSaveCacel(){
        btnSave.setGraphic(new ImageView(new Image("file:resources/Icon/Basic/save-g-128.png", 25, 25, true, true)));
        btnSave.setText("");
        btnSave.ripplerFillProperty().set(Color.GOLD);
        btnCancel.setGraphic(new ImageView(new Image("file:resources/Icon/Basic/cancel-g-128.png", 25, 25, true, true)));
        btnCancel.setText("");
        btnCancel.ripplerFillProperty().set(Color.GOLD);
    }
    
    private void initButtonBillForm(){
        btnChargeInfo.setGraphic(new ImageView(new Image("file:resources/Icon/Basic/information-g-128.png", 25, 25, true, true)));
        btnChargeInfo.setText("");
        btnChargeInfo.ripplerFillProperty().set(Color.GOLD);
        btnRestoDebtInfo.setGraphic(new ImageView(new Image("file:resources/Icon/Basic/information-g-128.png", 25, 25, true, true)));
        btnRestoDebtInfo.setText("");
        btnRestoDebtInfo.ripplerFillProperty().set(Color.GOLD);
        btnPrintBill.setGraphic(new ImageView(new Image("file:resources/Icon/Basic/print-g-128.png", 25, 25, true, true)));
        btnPrintBill.setText("");
        btnPrintBill.ripplerFillProperty().set(Color.GOLD);
        btnPrintTransactionPayment.setGraphic(new ImageView(new Image("file:resources/Icon/Basic/print-g-128.png", 25, 25, true, true)));
        btnPrintTransactionPayment.setText("");
        btnPrintTransactionPayment.ripplerFillProperty().set(Color.GOLD);
        btnReservaionPaymentStatus.setGraphic(new ImageView(new Image("file:resources/Icon/Basic/warning-r-128.png", 25, 25, true, true)));
        btnReservaionPaymentStatus.setText("");
        btnReservaionPaymentStatus.ripplerFillProperty().set(Color.GOLD);
    }
    
    private void initTableContent() {

        initReservationContent();
        initReservationDetailContent();
        initReservationRoomDetailContent();
        initReservationPAGDetailContent();
        
        initReservationTransaction();
        initReservationCheckInOut();

//        arrayOfDivider = new double[]{0.0f, 1.0f, 1.0f};
        arrayOfDivider = new double[]{0.25f, 0.45f, 0.65f};
        spH.setDividerPositions(arrayOfDivider);

        for (int i = 0; i < arrayOfDivider.length; i++) {
            final int idx = i;
            SplitPane.Divider div = spH.getDividers().get(i);
            div.positionProperty().addListener((obs, oldValue, newValue) -> {
                div.setPosition(arrayOfDivider[idx]);
            });
        }

        spV.setDividerPositions(0.5f, 1.0f);
        spH1.setDividerPositions(0.5f, 1.0f);
        spH2.setDividerPositions(0.5f, 1.0f);

        //give time for resize anchor(0.01 sec)
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1);
                Platform.runLater(() -> {
                    show2();
                });
            } catch (Exception e) {
                System.out.println("err " + e.getMessage());
            }

        });
        thread.setDaemon(true);
        thread.start();
    }

    private ClassTableWithControl tcReservation;

    private void initReservationContent() {
        //set table
        setReservationTable();
        //set control
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton btnAddR = new JFXButton("Tambah");
        btnAddR.setOnMouseClicked((e) -> show134());
        buttonControls.add(btnAddR);
        JFXButton btnUpdateR = new JFXButton("Ubah");
        btnUpdateR.setOnMouseClicked((e) -> show134());
        buttonControls.add(btnUpdateR);
        JFXButton btnDeleteR = new JFXButton("Cancel");
        buttonControls.add(btnDeleteR);
        tcReservation.addButtonControl(buttonControls);
        //set table-control in content
        AnchorPane.setBottomAnchor(tcReservation, 10.0);
        AnchorPane.setLeftAnchor(tcReservation, 10.0);
        AnchorPane.setRightAnchor(tcReservation, 10.0);
        AnchorPane.setTopAnchor(tcReservation, 10.0);
        apReservation.getChildren().add(tcReservation);
    }

    private void setReservationTable() {
        TableView tableView = new TableView();
        tcReservation = new ClassTableWithControl(tableView);
    }

    private void initReservationDetailContent() {

    }

    private ClassTableWithControl tcReservationRoomDetail;

    private void initReservationRoomDetailContent() {
        //set table
        setTableReservationRoomDetail();
        //set control
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton btnAddRRD = new JFXButton("Tambah");
        buttonControls.add(btnAddRRD);
        btnAddRRD.setOnMouseClicked((e) -> show134());
        JFXButton btnUpdateRRD = new JFXButton("Ubah");
//        btnUpdateRRD.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnUpdateRRD);
        JFXButton btnDeleteRRD = new JFXButton("Hapus");
        buttonControls.add(btnDeleteRRD);
        tcReservationRoomDetail.addButtonControl(buttonControls);
        //set table-control in content
        AnchorPane.setBottomAnchor(tcReservationRoomDetail, 10.0);
        AnchorPane.setLeftAnchor(tcReservationRoomDetail, 10.0);
        AnchorPane.setRightAnchor(tcReservationRoomDetail, 10.0);
        AnchorPane.setTopAnchor(tcReservationRoomDetail, 50.0);
        apReservationRoomDetail.getChildren().add(tcReservationRoomDetail);
    }

    private void setTableReservationRoomDetail() {
        TableView tableView = new TableView();
        tcReservationRoomDetail = new ClassTableWithControl(tableView);
    }

    private void initReservationPAGDetailContent() {
        initReservationFABDetail();
        initReservationAmenityDetail();
        initReservationPropertyBarcodeDetail();
        initReservationServiceDetail();
    }

    private ClassTableWithControl tcReservationFABDetail;

    private void initReservationFABDetail() {
        //set table
        setTableReservationFABDetail();
        //set control
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton btnAddRPAG = new JFXButton("Tambah");
//        btnAddRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnAddRPAG);
        JFXButton btnUpdateRPAG = new JFXButton("Ubah");
//        btnUpdateRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnUpdateRPAG);
        JFXButton btnDeleteRPAG = new JFXButton("Hapus");
        buttonControls.add(btnDeleteRPAG);
        tcReservationFABDetail.addButtonControl(buttonControls);
        //set table-control in content
        AnchorPane.setBottomAnchor(tcReservationFABDetail, 5.0);
        AnchorPane.setLeftAnchor(tcReservationFABDetail, 5.0);
        AnchorPane.setRightAnchor(tcReservationFABDetail, 5.0);
        AnchorPane.setTopAnchor(tcReservationFABDetail, 5.0);
        apReservationFABDetail.getChildren().add(tcReservationFABDetail);
    }

    private void setTableReservationFABDetail() {
        TableView tableView = new TableView();
        tcReservationFABDetail = new ClassTableWithControl(tableView);
    }

    private ClassTableWithControl tcReservationAmenityDetail;

    private void initReservationAmenityDetail() {
        //set table
        setTableReservationAmenityDetail();
        //set control
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton btnAddRPAG = new JFXButton("Tambah");
//        btnAddRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnAddRPAG);
        JFXButton btnUpdateRPAG = new JFXButton("Ubah");
//        btnUpdateRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnUpdateRPAG);
        JFXButton btnDeleteRPAG = new JFXButton("Hapus");
        buttonControls.add(btnDeleteRPAG);
        tcReservationAmenityDetail.addButtonControl(buttonControls);
        //set table-control in content
        AnchorPane.setBottomAnchor(tcReservationAmenityDetail, 5.0);
        AnchorPane.setLeftAnchor(tcReservationAmenityDetail, 5.0);
        AnchorPane.setRightAnchor(tcReservationAmenityDetail, 5.0);
        AnchorPane.setTopAnchor(tcReservationAmenityDetail, 5.0);
        apReservationAmenityDetail.getChildren().add(tcReservationAmenityDetail);
    }

    private void setTableReservationAmenityDetail() {
        TableView tableView = new TableView();
        tcReservationAmenityDetail = new ClassTableWithControl(tableView);
    }

    private ClassTableWithControl tcReservationPropertyBarcodeDetail;

    private void initReservationPropertyBarcodeDetail() {
        //set table
        setTableReservationPropertyBarcodeDetail();
        //set control
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton btnAddRPAG = new JFXButton("Tambah");
//        btnAddRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnAddRPAG);
        JFXButton btnUpdateRPAG = new JFXButton("Ubah");
//        btnUpdateRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnUpdateRPAG);
        JFXButton btnDeleteRPAG = new JFXButton("Hapus");
        buttonControls.add(btnDeleteRPAG);
        tcReservationPropertyBarcodeDetail.addButtonControl(buttonControls);
        //set table-control in content
        AnchorPane.setBottomAnchor(tcReservationPropertyBarcodeDetail, 5.0);
        AnchorPane.setLeftAnchor(tcReservationPropertyBarcodeDetail, 5.0);
        AnchorPane.setRightAnchor(tcReservationPropertyBarcodeDetail, 5.0);
        AnchorPane.setTopAnchor(tcReservationPropertyBarcodeDetail, 5.0);
        apReservationPropertyBarcodeDetail.getChildren().add(tcReservationPropertyBarcodeDetail);
    }

    private void setTableReservationPropertyBarcodeDetail() {
        TableView tableView = new TableView();
        tcReservationPropertyBarcodeDetail = new ClassTableWithControl(tableView);
    }

    private ClassTableWithControl tcReservationServiceDetail;

    private void initReservationServiceDetail() {
        //set table
        setTableReservationServiceDetail();
        //set control
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton btnAddRPAG = new JFXButton("Tambah");
//        btnAddRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnAddRPAG);
        JFXButton btnUpdateRPAG = new JFXButton("Ubah");
//        btnUpdateRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnUpdateRPAG);
        JFXButton btnDeleteRPAG = new JFXButton("Hapus");
        buttonControls.add(btnDeleteRPAG);
        tcReservationServiceDetail.addButtonControl(buttonControls);
        //set table-control in content
        AnchorPane.setBottomAnchor(tcReservationServiceDetail, 5.0);
        AnchorPane.setLeftAnchor(tcReservationServiceDetail, 5.0);
        AnchorPane.setRightAnchor(tcReservationServiceDetail, 5.0);
        AnchorPane.setTopAnchor(tcReservationServiceDetail, 5.0);
        apReservationServiceDetail.getChildren().add(tcReservationServiceDetail);
    }

    private void setTableReservationServiceDetail() {
        TableView tableView = new TableView();
        tcReservationServiceDetail = new ClassTableWithControl(tableView);
    }

    private ClassTableWithControl tcReservationTransaction;

    private void initReservationTransaction() {
        //set table
        setTableReservationTransaction();
        //set control
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton btnAddRT= new JFXButton("Tambah");
        btnAddRT.setOnMouseClicked((e) -> showReservationTransactionDialog());
        buttonControls.add(btnAddRT);
        JFXButton btnUpdateRT = new JFXButton("Ubah");
//        btnUpdateRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnUpdateRT);
        JFXButton btnDeleteRT = new JFXButton("Hapus");
        buttonControls.add(btnDeleteRT);
        tcReservationTransaction.addButtonControl(buttonControls);
        //set table-control in content
        AnchorPane.setBottomAnchor(tcReservationTransaction, 5.0);
        AnchorPane.setLeftAnchor(tcReservationTransaction, 5.0);
        AnchorPane.setRightAnchor(tcReservationTransaction, 5.0);
        AnchorPane.setTopAnchor(tcReservationTransaction, 5.0);
        apReservationTransaction.getChildren().add(tcReservationTransaction);
    }

    private void setTableReservationTransaction() {
        TableView tableView = new TableView();
        tcReservationTransaction = new ClassTableWithControl(tableView);
    }
    
    private ClassTableWithControl tcReservationCheckInOut;

    private void initReservationCheckInOut() {
        //set table
        setTableReservationCheckInOut();
        //set control
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton btnAddRPAG = new JFXButton("Tambah");
//        btnAddRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnAddRPAG);
        JFXButton btnUpdateRPAG = new JFXButton("Ubah");
//        btnUpdateRFAB.setOnMouseClicked((e) -> show13());
        buttonControls.add(btnUpdateRPAG);
        JFXButton btnDeleteRPAG = new JFXButton("Hapus");
        buttonControls.add(btnDeleteRPAG);
        tcReservationCheckInOut.addButtonControl(buttonControls);
        //set table-control in content
        AnchorPane.setBottomAnchor(tcReservationCheckInOut, 5.0);
        AnchorPane.setLeftAnchor(tcReservationCheckInOut, 5.0);
        AnchorPane.setRightAnchor(tcReservationCheckInOut, 5.0);
        AnchorPane.setTopAnchor(tcReservationCheckInOut, 5.0);
        apReservationCheckInOut.getChildren().add(tcReservationCheckInOut);
    }

    private void setTableReservationCheckInOut() {
        TableView tableView = new TableView();
        tcReservationCheckInOut = new ClassTableWithControl(tableView);
    }
    
    private double[] arrayOfDivider;
    
    @FXML
    private void show2() {
        apReservation.setVisible(true);

        arrayOfDivider = new double[]{0.0f, 1.0f, 1.0f};
        spH.setDividerPositions(arrayOfDivider);

        apReservationDetail.setVisible(false);
        apReservationRoomDetail.setVisible(false);
        apReservationPAGDetail.setVisible(false);
    }

//    @FXML
//    private void show23() {
//        apReservation.setVisible(true);
//        apReservationRoomDetail.setVisible(true);
//
//        arrayOfDivider = new double[]{0.0f, 0.5f, 1.0f};
//        spH.setDividerPositions(arrayOfDivider);
//
//        apReservationDetail.setVisible(false);
//        apReservationPAGDetail.setVisible(false);
//    }

    @FXML
    private void show134() {
        apReservationDetail.setVisible(true);
        apReservationRoomDetail.setVisible(true);
        apReservationPAGDetail.setVisible(true);

        arrayOfDivider = new double[]{0.3f, 0.3f, 0.6f};
        spH.setDividerPositions(arrayOfDivider);

        spV.setDividerPositions(0.5f, 1.0f);
        spH1.setDividerPositions(0.5f, 1.0f);
        spH2.setDividerPositions(0.5f, 1.0f);
        
        apReservation.setVisible(false);
//        apReservationPAGDetail.setVisible(false);
    }

//    @FXML
//    private void show34() {
//        apReservationDetail.setVisible(true);
//        apReservationRoomDetail.setVisible(true);
//        apReservationPAGDetail.setVisible(true);
//
////        arrayOfDivider = new double[]{0.0f, 0.0f, 0.5f};
//        arrayOfDivider = new double[]{0.3f, 0.3f, 6.5f};
//        spH.setDividerPositions(arrayOfDivider);
//
//        spV.setDividerPositions(0.5f, 1.0f);
//        spH1.setDividerPositions(0.5f, 1.0f);
//        spH2.setDividerPositions(0.5f, 1.0f);
//
////        apReservationDetail.setVisible(false);
//        apReservation.setVisible(false);
//    }
    
    private Stage dialogStage;
    
    private void showRoomDetailDialog(){
        
    }
    
    private void showFABDialog(){
        
    }
    
    private void showAmenityDialog(){
        
    }
    
    private void showPropertyBarcodeDialog(){
        
    }
    
    private void showServiceDialog(){
        
    }
    
    private void showReservationTransactionDialog(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/dialog/ReservationTransactionDialog.fxml"));
            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
//            dialogStage.setTitle("Reservation Transaction..");
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

            // Set this reservation-controller into the controller.
//            ReservationTransactionDialogController controller = loader.getController();
//            controller.setReservationController(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showReservationCheckInOutDialog(){
        
    }
    
    private void showDialog(String fxmlPath){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/PersonEditDialog.fxml"));
            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

//            // Set the person into the controller.
//            PersonEditDialogController controller = loader.getController();
//            controller.setDialogStage(dialogStage);
//            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calendar
     */
    @FXML
    Tab tabCalendar;

    @FXML
    ScrollPane scrollPane;

    private void initCalendarContent() {

        setCalendarTitle();

        setRoom();

        setCalendarContent();

    }

    @FXML
    JFXCDatePicker btnGoToDate;

    @FXML
    JFXButton btnGotToCurrentDatePosition;

    @FXML
    AnchorPane apCalendarTitle;

    @FXML
    GridPane gpCalendarTitle;

    @FXML
    Label lblCalendarTitleMin3;

    @FXML
    Label lblCalendarTitleMin2;

    @FXML
    Label lblCalendarTitleMin1;

    @FXML
    Label lblCalendarTitle;

    @FXML
    Label lblCalendarTitlePlus1;

    @FXML
    Label lblCalendarTitlePlus2;

    @FXML
    Label lblCalendarTitlePlus3;

    @FXML
    Label lblCalendarTitlePlus4;

    private ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>(LocalDate.now());

    private void setCalendarTitle() {
        //set button go to date
        JFXButton arrowButtonContent = new JFXButton();
        arrowButtonContent.setPrefSize(185, 25);
        arrowButtonContent.setButtonType(JFXButton.ButtonType.RAISED);
        arrowButtonContent.setOnMouseClicked((e) -> btnGoToDate.show());
        arrowButtonContent.textProperty().bind(Bindings.createStringBinding(()
                -> (btnGoToDate.valueProperty().get() != null)
                        ? btnGoToDate.valueProperty().get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "", btnGoToDate.valueProperty()));
        btnGoToDate.setArrowButtonContent(arrowButtonContent);
        btnGoToDate.valueProperty().bindBidirectional(selectedDate);
        //set current date button
//        btnGotToCurrentDatePosition.textProperty().bind(Bindings.createStringBinding(() -> currentDate.get().format(DateTimeFormatter.ofPattern("HH:mm:ss")), currentDate));
        //set calendar title
        lblCalendarTitleMin3.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().minusDays(3)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitleMin2.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().minusDays(2)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitleMin1.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().minusDays(1)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitle.textProperty().bind(Bindings.createStringBinding(() -> selectedDate.get().format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitlePlus1.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().plusDays(1)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitlePlus2.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().plusDays(2)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitlePlus3.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().plusDays(3)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
        lblCalendarTitlePlus4.textProperty().bind(Bindings.createStringBinding(() -> (selectedDate.get().plusDays(4)).format(DateTimeFormatter.ofPattern("EEE, dd")), selectedDate));
    }

    @FXML
    private void setNow() {
        selectedDate.set(LocalDate.now());
    }

    @FXML
    private void minusDay() {
        selectedDate.set(selectedDate.get().minusDays(1));
    }

    @FXML
    private void plusDay() {
        selectedDate.set(selectedDate.get().plusDays(1));
    }

    @FXML
    AnchorPane mainSP;

    private int[] rooms = new int[50];

    @FXML
    GridPane gpRooms;

    private void setRoom() {
        gpRooms.getRowConstraints().clear();
        for (int i = 0; i < rooms.length; i++) {
            gpRooms.getRowConstraints().add(new RowConstraints());
            Label label = getRoomContent("Room " + i);
            gpRooms.add(label, 0, i);
        }
    }

    @FXML
    GridPane gpCalendars;

    private void setCalendarContent() {
        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 15) {
                GridPane gpCalendar = new GridPane();
                gpCalendar.setGridLinesVisible(true);
                for (int j = 0; j < rooms.length; j++) {
                    gpCalendar.getRowConstraints().add(new RowConstraints());
                    Label label = getCalendarContent("C" + j, "test" + ((j % 4) + 1));
                    label.setOpacity(0.5);
                    gpCalendar.add(label, 0, j);
                }
                gpCalendars.add(gpCalendar, i, 0);
            } else {
                if (i % 2 != 0) {
                    GridPane gpCalendar = new GridPane();
                    gpCalendar.setGridLinesVisible(true);
                    for (int j = 0; j < rooms.length; j++) {
                        gpCalendar.getRowConstraints().add(new RowConstraints());
                        Label label = getCalendarContent("C" + j, "test" + ((j % 4) + 1));
                        gpCalendar.add(label, 0, j);
                    }
                    gpCalendars.add(gpCalendar, i, 0, 2, 1);
                }
            }
        }

        gpCalendars.widthProperty().addListener((obs, oldValue, newValue) -> {
            ScrollBar sb = (ScrollBar) scrollPane.lookup(".scroll-bar:vertical");
            if (sb != null) {
                if (sb.isVisible()) {
                    AnchorPane.setRightAnchor(gpCalendarTitle, 18.0);
                } else {
                    AnchorPane.setRightAnchor(gpCalendarTitle, 5.0);
                }
            } else {
                System.out.println("null");
            }
        });
    }

    private void setDataReservation() {

    }

    private Label getCalendarContent(String id, String style) {
        Label label = new Label(id);
        label.setPrefWidth(1000000);
        label.setPrefHeight(80);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add(style);
        return label;
    }

    private Label getRoomContent(String id) {
        Label label = new Label(id);
        label.setPrefWidth(100);
        label.setPrefHeight(80);
        label.setAlignment(Pos.CENTER);
        return label;
    }
    
    /**
     * Function for Dialog
     */
    public void closeDialogStage(){
        dialogStage.close();
    }
    
    public double getRestOfBill(){
        return 0;
    }

}
