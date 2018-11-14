/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view;

import com.jfoenix.controls.JFXButton;
import hotelfx.helper.ClassTableWithControl;
//import xhotelfx.persistence.model.TblTest;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RegistrationController implements Initializable {

    @FXML
    private AnchorPane tableLayout;

    private ClassTableWithControl tableControl;

    private void initTableDataRegistration() {
        //set table
        setTableDataRegistration();
        //set control
        setControlDataRegistration();
        //set table-control to content-view
        AnchorPane.setBottomAnchor(tableControl, 15.0);
        AnchorPane.setLeftAnchor(tableControl, 15.0);
        AnchorPane.setRightAnchor(tableControl, 15.0);
        AnchorPane.setTopAnchor(tableControl, 15.0);
        tableLayout.getChildren().add(tableControl);
    }

    private void setTableDataRegistration() {
//        TableView<TblTest> tableView = new TableView();
//        TableColumn<TblTest, String> idTest = new TableColumn("ID");
//        idTest.setCellValueFactory(cellData -> cellData.getValue().idTestProperty());
//        TableColumn<TblTest, String> testName = new TableColumn("NAME");
//        testName.setCellValueFactory(cellData -> cellData.getValue().testNameProperty());
//        TableColumn<TblTest, String> testNote = new TableColumn("NOTE");
//        testNote.setCellValueFactory(cellData -> cellData.getValue().testNoteProperty());
//        tableView.getColumns().addAll(idTest, testName, testNote);
//        ObservableList<TblTest> testData = FXCollections.observableArrayList();
//        for (int i = 0; i < 100; i++) {
//            testData.add(new TblTest("ID-" + i, "Name", "Note"));
//        }
//        tableView.setItems(testData);
//        tableControl = new ClassTableWithControl(tableView);
    }

    private void setControlDataRegistration() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                createHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                updateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                deleteHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Print");
            buttonControl.setOnMouseClicked((e) -> {
                //listener print
                printHandle();
            });
            buttonControls.add(buttonControl);
        }
        tableControl.addButtonControl(buttonControls);
    }

    @FXML
    private GridPane gpFormData;

    @FXML
    private ScrollPane spFormData;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");
    
    private BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private void initFormDataRegistration() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            spFormData.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        gpFormData.setOnScroll((ScrollEvent scroll) -> {
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
    }

    @FXML
    private SplitPane spDataRegistration;

    private IntegerProperty dataFormShowStatus;

    private void setSplitpane() {
        dataFormShowStatus = new SimpleIntegerProperty(0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract((new SimpleDoubleProperty(360.0).divide(spDataRegistration.widthProperty()))
                        .multiply(dataFormShowStatus)
                )
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            spDataRegistration.setDividerPositions(newVal.doubleValue());
        });

        SplitPane.Divider div = spDataRegistration.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            div.setPosition(divPosition.get());
        });

    }

    /*
     * HANDLES
     */
    public void createHandle() {

        //open form data registration
        dataFormShowStatus.set(1);
    }

    public void updateHandle() {
        if (tableControl.getTableView().getSelectionModel().getSelectedItems().size() == 1) {

            //open form data registration
            dataFormShowStatus.set(1);
        } else {

        }
    }

    public void deleteHandle() {

    }

    public void printHandle() {

    }

    public void loadFileHandle() {

    }

    public void saveHandle() {

        //close form data registration
        dataFormShowStatus.set(0);
    }

    public void cancelHandle() {

        //close form data registration
        dataFormShowStatus.set(0);
    }

    public boolean checkDataInput() {
        boolean result = true;
        return result;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init table (data registration)
        initTableDataRegistration();
        //init form (data registration)
        initFormDataRegistration();
        //set split divinder for contetnt
        setSplitpane();
    }

}
