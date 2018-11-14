/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room.room;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFilteringTable;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassTableWithControl;
import hotelfx.helper.ClassViewSetting;
//import hotelfx.persistence.model.RefRoomCleanStatus;
import hotelfx.persistence.model.RefRoomStatus;
import hotelfx.persistence.model.TblBuilding;
import hotelfx.persistence.model.TblFloor;
import hotelfx.persistence.model.TblGroup;
import hotelfx.persistence.model.TblLocation;
import hotelfx.persistence.model.TblRoom;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.view.DashboardController;
import hotelfx.view.feature_room_and_service_md.FeatureRoomAndServiceMDController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RoomController implements Initializable {

    /**
     * SPLITPANE BETWEEN TABLE AND FORM, CONTENT LAYOUT
     */
    @FXML
    private SplitPane spDataRoom;

    private DoubleProperty dataRoomFormShowStatus;

    @FXML
    private AnchorPane contentLayout;

    @FXML
    private AnchorPane tableDataRoomLayoutDisableLayer;

    private boolean isTimeLinePlaying = false;

    private void setDataRoomSplitpane() {
        spDataRoom.setDividerPositions(1);

        dataRoomFormShowStatus = new SimpleDoubleProperty(1.0);

        DoubleProperty divPosition = new SimpleDoubleProperty();

        divPosition.bind(new SimpleDoubleProperty(1.0)
                .subtract(dataRoomFormShowStatus)
        );

        divPosition.addListener((obs, oldVal, newVal) -> {
            isTimeLinePlaying = true;
            KeyValue keyValue = new KeyValue(spDataRoom.getDividers().get(0).positionProperty(), newVal);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(ClassViewSetting.keyFrameDuration), keyValue));
            timeline.play();
            timeline.setOnFinished((e) -> {
                isTimeLinePlaying = false;
            });
        });

        SplitPane.Divider div = spDataRoom.getDividers().get(0);
        div.positionProperty().addListener((obs, oldValue, newValue) -> {
            if (!isTimeLinePlaying) {
                div.setPosition(divPosition.get());
            }
        });

        dataRoomFormShowStatus.addListener((obs, oldVal, newVal) -> {
            if (dataInputStatus != 3) {
                if (newVal.doubleValue() == 0.0) {    //enable
                    tableDataRoomLayout.setDisable(false);
                    tableDataRoomLayoutDisableLayer.setDisable(true);
                    tableDataRoomLayout.toFront();
                }
                if (newVal.doubleValue() == 1) {  //disable
                    tableDataRoomLayout.setDisable(true);
                    tableDataRoomLayoutDisableLayer.setDisable(false);
                    tableDataRoomLayoutDisableLayer.toFront();
                }
            }
        });

        dataRoomFormShowStatus.set(0.0);
    }

    /**
     * TABLE DATA
     */
    @FXML
    private AnchorPane tableDataRoomLayout;

    private ClassFilteringTable<TblRoom> cft;

    @FXML
    private AnchorPane ancHeaderLayout;

    @FXML
    private AnchorPane ancBodyLayout;

    private ClassTableWithControl tableDataRoom;

    private void initTableDataRoom() {
        //set table
        setTableDataRoom();
        //set control
        setTableControlDataRoom();
        //set table-control to content-view
        ancBodyLayout.getChildren().clear();
        AnchorPane.setBottomAnchor(tableDataRoom, 15.0);
        AnchorPane.setLeftAnchor(tableDataRoom, 15.0);
        AnchorPane.setRightAnchor(tableDataRoom, 15.0);
        AnchorPane.setTopAnchor(tableDataRoom, 15.0);
        ancBodyLayout.getChildren().add(tableDataRoom);
    }

    private void setTableDataRoom() {
        TableView<TblRoom> tableView = new TableView();
//        tableView.setEditable(true);
        TableColumn<TblRoom, String> roomName = new TableColumn("Kamar");
        roomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        roomName.setMinWidth(140);

        TableColumn<TblRoom, String> roomNote = new TableColumn("Keterangan");
        roomNote.setCellValueFactory(cellData -> cellData.getValue().roomNoteProperty());
        roomNote.setMinWidth(200);

        TableColumn<TblRoom, String> roomType = new TableColumn("Tipe Kamar");
        roomType.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblRoomType().getRoomTypeName(), param.getValue().tblRoomTypeProperty()));
        roomType.setMinWidth(140);

        TableColumn<TblRoom, String> smokingStatus = new TableColumn("Jenis Kamar");
        smokingStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getSmokingStatus() == 0 ? "Non Smoking" : "Smoking", param.getValue().smokingStatusProperty()));
        smokingStatus.setMinWidth(140);

        TableColumn<TblRoom, String> roomStatus = new TableColumn("Status Kamar");
        roomStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getRefRoomStatus().getStatusName(), param.getValue().refRoomStatusProperty()));
        roomStatus.setMinWidth(140);

        TableColumn<TblRoom, String> buildingName = new TableColumn("Gedung");
        buildingName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblBuilding().getBuildingName(), param.getValue().getTblLocation().tblBuildingProperty()));
        buildingName.setMinWidth(140);

        TableColumn<TblRoom, String> floorName = new TableColumn("Lantai");
        floorName.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblLocation().getTblFloor().getFloorName(), param.getValue().getTblLocation().tblFloorProperty()));
        floorName.setMinWidth(140);

//        TableColumn<TblRoom, String> roomCleanStatus = new TableColumn("Status Kebersihan");
//        roomCleanStatus.setCellValueFactory((TableColumn.CellDataFeatures<TblRoom, String> param)
//                -> Bindings.createStringBinding(() -> param.getValue().getRefRoomCleanStatus().getStatusName(), param.getValue().refRoomCleanStatusProperty()));
//        roomCleanStatus.setMinWidth(140);
        tableView.getColumns().addAll(roomName, roomType, smokingStatus, roomStatus, buildingName, floorName, roomNote);
        tableView.setItems(loadAllDataRoom());

        tableView.setRowFactory(tv -> {
            TableRow<TblRoom> row = new TableRow<>();
            row.setOnMouseClicked((e) -> {
                if (e.getClickCount() == 2) {
                    if (isShowStatus.get()) {
                        dataRoomUnshowHandle();
                    } else {
                        if ((!row.isEmpty())) {
                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
                                //listener update
                                dataRoomUpdateHandle();
                            } else {
                                dataRoomShowHandle();
                            }
                        }
                    }
//                } else {
//                    if (!row.isEmpty()) {
//                        if (isShowStatus.get()) {
//                            if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
//                                //listener update
//                                dataRoomUpdateHandle();
//                            } else {
//                                dataRoomShowHandle();
//                            }
//                        }
//                    }
                }
            });
            return row;
        });

        tableDataRoom = new ClassTableWithControl(tableView);

        //set filter
        cft = new ClassFilteringTable<>(
                TblRoom.class,
                tableDataRoom.getTableView(),
                tableDataRoom.getTableView().getItems());

        AnchorPane.setBottomAnchor(cft, 12.5);
//        AnchorPane.setLeftAnchor(cft, 15.0);
        AnchorPane.setRightAnchor(cft, 15.0);
        AnchorPane.setTopAnchor(cft, 12.5);
//        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cft);
    }

    private void setTableControlDataRoom() {
        //set control from feature
        ObservableList<Node> buttonControls = FXCollections.observableArrayList();
        JFXButton buttonControl;
        if (DashboardController.feature.getSelectedRoleFeature().getCreateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Tambah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener add
                dataRoomCreateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getUpdateData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Ubah");
            buttonControl.setOnMouseClicked((e) -> {
                //listener update
                dataRoomUpdateHandle();
            });
            buttonControls.add(buttonControl);
        }
        if (DashboardController.feature.getSelectedRoleFeature().getDeleteData()) {
            buttonControl = new JFXButton();
            buttonControl.setText("Hapus");
            buttonControl.setOnMouseClicked((e) -> {
                //listener delete
                dataRoomDeleteHandle();
            });
            buttonControls.add(buttonControl);
        }
//        if (DashboardController.feature.getSelectedRoleFeature().getPrintData()) {
//            buttonControl = new JFXButton();
//            buttonControl.setText("Cetak");
//            buttonControl.setOnMouseClicked((e) -> {
//                //listener print
//                dataRoomPrintHandle();
//            });
//            buttonControls.add(buttonControl);
//        }
        tableDataRoom.addButtonControl(buttonControls);
    }

    private ObservableList<TblRoom> loadAllDataRoom() {
        List<TblRoom> list = parentController.getFRoomManager().getAllDataRoom();
        for (TblRoom data : list) {
            //data room type
            data.setTblRoomType(parentController.getFRoomManager().getRoomType(data.getTblRoomType().getIdroomType()));
            //data location
            data.setTblLocation(parentController.getFRoomManager().getLocation(data.getTblLocation().getIdlocation()));
            //data building
            data.getTblLocation().setTblBuilding(parentController.getFRoomManager().getBuilding(data.getTblLocation().getTblBuilding().getIdbuilding()));
            //data floor
            data.getTblLocation().setTblFloor(parentController.getFRoomManager().getFloor(data.getTblLocation().getTblFloor().getIdfloor()));
        }
        return FXCollections.observableArrayList(list);
    }

    /**
     * FORM INPUT
     */
    @FXML
    private AnchorPane formAnchor;

    @FXML
    private GridPane gpFormDataRoom;

    @FXML
    private ScrollPane spFormDataRoom;

    @FXML
    private JFXTextField txtRoomName;

    @FXML
    private JFXTextArea txtRoomNote;

    private final JFXCComboBoxPopup<RefRoomStatus> cbpRoomStatusHeader = new JFXCComboBoxPopup<>(RefRoomStatus.class);

    @FXML
    private AnchorPane buildingLayout;
    private JFXCComboBoxTablePopup<TblBuilding> cbpBuilding;

    @FXML
    private AnchorPane floorLayout;
    private JFXCComboBoxTablePopup<TblFloor> cbpFloor;

    @FXML
    private JFXCheckBox chbJM;
    
    @FXML
    private AnchorPane ancGroupLayout;
    private JFXCComboBoxTablePopup<TblGroup> cbpGroup;
    
    @FXML
    private AnchorPane typeRoomLayout;
    private JFXCComboBoxTablePopup<TblRoomType> cbpType;

    @FXML
    private JFXRadioButton radbSmokingStatus;

    @FXML
    private AnchorPane roomStatusLayout;
    private JFXCComboBoxTablePopup<RefRoomStatus> cbpRoomStatus;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private TblRoom selectedData;

    private void initFormDataRoom() {
        isFormScroll.addListener((obs, wasSroll, nowScroll) -> {
            //@@@
            spFormDataRoom.pseudoClassStateChanged(onScrollPseudoClass, nowScroll);
        });

        //@@@
        gpFormDataRoom.setOnScroll((ScrollEvent scroll) -> {
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

        btnSave.setTooltip(new Tooltip("Simpan (Data Kamar)"));
        btnSave.setOnAction((e) -> {
            dataRoomSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataRoomCancelHandle();
        });

        chbJM.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){
                cbpGroup.setValue(null);
                cbpGroup.setVisible(true);
            }else{
                cbpGroup.setValue(null);
                cbpGroup.setVisible(false);
            }
        });
        chbJM.setSelected(false);
        cbpGroup.setVisible(false);
        
        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtRoomName,
                cbpBuilding,
                cbpFloor,
                cbpGroup,
                cbpType);
    }

    private void initDataPopup() {
        //Building
        TableView<TblBuilding> tableBuilding = new TableView<>();

        TableColumn<TblBuilding, String> codeBuilding = new TableColumn<>("ID");
        codeBuilding.setCellValueFactory(cellData -> cellData.getValue().codeBuildingProperty());
        codeBuilding.setMinWidth(120);

        TableColumn<TblBuilding, String> buildingName = new TableColumn<>("Gedung");
        buildingName.setCellValueFactory(cellData -> cellData.getValue().buildingNameProperty());
        buildingName.setMinWidth(140);

        tableBuilding.getColumns().addAll(codeBuilding, buildingName);

        ObservableList<TblBuilding> buildingItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataBuilding());

        cbpBuilding = new JFXCComboBoxTablePopup<>(
                TblBuilding.class, tableBuilding, buildingItems, "", "Gedung *", true, 270, 200
        );

        //Floor
        TableView<TblFloor> tableFloor = new TableView<>();
        TableColumn<TblFloor, String> floorName = new TableColumn<>("Lantai");
        floorName.setCellValueFactory(cellData -> cellData.getValue().floorNameProperty());
        floorName.setMinWidth(140);

        tableFloor.getColumns().addAll(floorName);

        ObservableList<TblFloor> floorItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataFloor());

        cbpFloor = new JFXCComboBoxTablePopup<>(
                TblFloor.class, tableFloor, floorItems, "", "Lantai *", true, 200, 200
        );

        //Group
        TableView<TblGroup> tableGroup = new TableView<>();
        TableColumn<TblGroup, String> groupName = new TableColumn<>("Department");
        groupName.setCellValueFactory(cellData -> cellData.getValue().groupNameProperty());
        groupName.setMinWidth(140);

        tableGroup.getColumns().addAll(groupName);

        ObservableList<TblGroup> groupItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataGroup());

        cbpGroup = new JFXCComboBoxTablePopup<>(
                TblGroup.class, tableGroup, groupItems, "", "Department *", true, 200, 200
        );
        
        //Type
        TableView<TblRoomType> tableType = new TableView<>();

        TableColumn<TblRoomType, String> typeName = new TableColumn<>("Room Type");
        typeName.setCellValueFactory(cellData -> cellData.getValue().roomTypeNameProperty());
        typeName.setMinWidth(140);

        TableColumn<TblRoomType, Integer> adultNum = new TableColumn<>("Adult");
        adultNum.setCellValueFactory(cellData -> cellData.getValue().adultNumberProperty().asObject());
        adultNum.setMinWidth(140);

        TableColumn<TblRoomType, Integer> childNum = new TableColumn<>("Child");
        childNum.setCellValueFactory(cellData -> cellData.getValue().childNumberProperty().asObject());
        childNum.setMinWidth(140);

        tableType.getColumns().addAll(typeName, adultNum, childNum);

        ObservableList<TblRoomType> typeItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomType());

        cbpType = new JFXCComboBoxTablePopup<>(
                TblRoomType.class, tableType, typeItems, "", "Tipe Kamar *", true, 430, 250
        );

        //Room Status
        TableView<RefRoomStatus> tableRoomStatus = new TableView<>();

        TableColumn<RefRoomStatus, String> roomStatusName = new TableColumn<>("Status");
        roomStatusName.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        roomStatusName.setMinWidth(140);

        tableRoomStatus.getColumns().addAll(roomStatusName);

        ObservableList<RefRoomStatus> roomStatusItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomStatus());

        cbpRoomStatus = new JFXCComboBoxTablePopup<>(
                RefRoomStatus.class, tableRoomStatus, roomStatusItems, "", "Status Kamar *", true, 200, 200
        );

        //Room Status - Header
        TableView<RefRoomStatus> tableRoomStatusHeader = new TableView<>();

        TableColumn<RefRoomStatus, String> roomStatusNameHeader = new TableColumn<>("Status");
        roomStatusNameHeader.setCellValueFactory(cellData -> cellData.getValue().statusNameProperty());
        roomStatusNameHeader.setMinWidth(140);

        TableColumn<RefRoomStatus, String> roomStatusNoteHeader = new TableColumn<>("Keterangan");
        roomStatusNoteHeader.setCellValueFactory(cellData -> cellData.getValue().statusNoteProperty());
        roomStatusNoteHeader.setMinWidth(180);

        tableRoomStatusHeader.getColumns().addAll(roomStatusNameHeader, roomStatusNoteHeader);

        ObservableList<RefRoomStatus> roomStatusHeaderItems = FXCollections.observableArrayList(loadAllDataRoomStatusHeader());

        setFunctionPopupRoomStatusHeader(cbpRoomStatusHeader, tableRoomStatusHeader, roomStatusHeaderItems, "", "", 330, 200);

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpRoomStatusHeader, 10.0);
        AnchorPane.setLeftAnchor(cbpRoomStatusHeader, 15.0);
//        AnchorPane.setRightAnchor(cbpRoomStatusHeader, 10.0);
        AnchorPane.setTopAnchor(cbpRoomStatusHeader, 10.0);
//        ancHeaderLayout.getChildren().clear();
        ancHeaderLayout.getChildren().add(cbpRoomStatusHeader);
        
        AnchorPane.setBottomAnchor(cbpBuilding, 0.0);
        AnchorPane.setLeftAnchor(cbpBuilding, 0.0);
        AnchorPane.setRightAnchor(cbpBuilding, 0.0);
        AnchorPane.setTopAnchor(cbpBuilding, 0.0);
        buildingLayout.getChildren().clear();
        buildingLayout.getChildren().add(cbpBuilding);

        AnchorPane.setBottomAnchor(cbpFloor, 0.0);
        AnchorPane.setLeftAnchor(cbpFloor, 0.0);
        AnchorPane.setRightAnchor(cbpFloor, 0.0);
        AnchorPane.setTopAnchor(cbpFloor, 0.0);
        floorLayout.getChildren().clear();
        floorLayout.getChildren().add(cbpFloor);

        AnchorPane.setBottomAnchor(cbpGroup, 0.0);
        AnchorPane.setLeftAnchor(cbpGroup, 0.0);
        AnchorPane.setRightAnchor(cbpGroup, 0.0);
        AnchorPane.setTopAnchor(cbpGroup, 0.0);
        ancGroupLayout.getChildren().clear();
        ancGroupLayout.getChildren().add(cbpGroup);
        
        AnchorPane.setBottomAnchor(cbpType, 0.0);
        AnchorPane.setLeftAnchor(cbpType, 0.0);
        AnchorPane.setRightAnchor(cbpType, 0.0);
        AnchorPane.setTopAnchor(cbpType, 0.0);
        typeRoomLayout.getChildren().clear();
        typeRoomLayout.getChildren().add(cbpType);

        AnchorPane.setBottomAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setLeftAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setRightAnchor(cbpRoomStatus, 0.0);
        AnchorPane.setTopAnchor(cbpRoomStatus, 0.0);
        roomStatusLayout.getChildren().clear();
        roomStatusLayout.getChildren().add(cbpRoomStatus);
    }

    private ObservableList<RefRoomStatus> loadAllDataRoomStatusHeader() {
        List<RefRoomStatus> list = parentController.getFRoomManager().getAllDataRoomStatus();
        return FXCollections.observableArrayList(list);
    }

    private void refreshDataPopup() {
        //Building
        ObservableList<TblBuilding> buildingItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataBuilding());
        cbpBuilding.setItems(buildingItems);

        //Floor
        ObservableList<TblFloor> floorItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataFloor());
        cbpFloor.setItems(floorItems);

        //Group
        ObservableList<TblGroup> groupItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataGroup());
        cbpGroup.setItems(groupItems);
        
        //Type
        ObservableList<TblRoomType> typeItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomType());
        cbpType.setItems(typeItems);

        //Room Status
        ObservableList<RefRoomStatus> roomStatusItems = FXCollections.observableArrayList(parentController.getFRoomManager().getAllDataRoomStatus());
        cbpRoomStatus.setItems(roomStatusItems);

        //Room Status - Header
        ObservableList<RefRoomStatus> roomStatusHeaderItems = FXCollections.observableArrayList(loadAllDataRoomStatusHeader());
        cbpRoomStatusHeader.setItems(roomStatusHeaderItems);
    }

    private void setFunctionPopupRoomStatusHeader(JFXCComboBoxPopup cbp,
            TableView table,
            ObservableList items,
            String nameFiltered,
            String promptText,
            double prefWidth,
            double prefHeight) {
        table.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() != -1) {
                        cbp.valueProperty().set(table.getItems().get(newValue.intValue()));
                    }
                    cbp.hide();
                });

        cbp.setPropertyNameForFiltered(nameFiltered);

        cbp.setItems(items);

        cbp.setOnShowing((e) -> {
            table.getSelectionModel().clearSelection();
        });

        // Add observable list data to the table
        table.itemsProperty().bind(cbp.filteredItemsProperty());

        //popup button
        JFXButton button = new JFXButton("Infomasi Status Kamar");
        button.setOnMouseClicked((e) -> cbp.show());
        button.setPrefSize(180, 40);

        //popup content
        BorderPane content = new BorderPane(new JFXButton("SHOW"));
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setPrefSize(prefWidth, prefHeight);

        content.setCenter(table);

        cbp.setPopupEditor(false);
        cbp.promptTextProperty().set(promptText);
        cbp.setLabelFloat(true);
        cbp.setPopupButton(button);
        cbp.settArrowButton(true);
        cbp.setPopupContent(content);

        cbp.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        cbp.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

    }

    private void setSelectedDataToInputForm() {

        refreshDataPopup();

        txtRoomName.textProperty().bindBidirectional(selectedData.roomNameProperty());
        txtRoomNote.textProperty().bindBidirectional(selectedData.roomNoteProperty());

        radbSmokingStatus.setSelected(selectedData.getSmokingStatus() == 1);
        radbSmokingStatus.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                selectedData.setSmokingStatus(1);
            } else {
                selectedData.setSmokingStatus(0);
            }
        });

        chbJM.setSelected(selectedData.getTblLocation().getTblGroup() != null);
        
        cbpBuilding.valueProperty().bindBidirectional(selectedData.getTblLocation().tblBuildingProperty());
        cbpFloor.valueProperty().bindBidirectional(selectedData.getTblLocation().tblFloorProperty());
        cbpGroup.valueProperty().bindBidirectional(selectedData.getTblLocation().tblGroupProperty());
        cbpType.valueProperty().bindBidirectional(selectedData.tblRoomTypeProperty());
        cbpRoomStatus.valueProperty().bindBidirectional(selectedData.refRoomStatusProperty());

        cbpBuilding.hide();
        cbpFloor.hide();
        cbpGroup.hide();
        cbpType.hide();
        cbpRoomStatus.hide();

        setSelectedDataToInputFormFunctionalComponent();
    }

    private void setSelectedDataToInputFormFunctionalComponent() {
        cbpRoomStatus.setDisable(true);
        ClassViewSetting.setDisableForAllInputNode(gpFormDataRoom, dataInputStatus == 3, cbpRoomStatus);

        btnSave.setVisible(dataInputStatus != 3);
        btnCancel.setVisible(dataInputStatus != 3);
    }

    /**
     * HANDLE FROM DATA INPUT
     */
    //0 = 'insert'
    //1 = 'update'
    private int dataInputStatus = 0;

    private void dataRoomCreateHandle() {
        dataInputStatus = 0;
        selectedData = new TblRoom();
        selectedData.setTblLocation(new TblLocation());
//        selectedData.getTblLocation().setTblBuilding(new TblBuilding());
//        selectedData.getTblLocation().setTblFloor(new TblFloor());
        selectedData.setRefRoomStatus(parentController.getFRoomManager().getRoomStatus(6)); //Out Of Order = '6'
        setSelectedDataToInputForm();
        //open form data room
        dataRoomFormShowStatus.set(0);
        dataRoomFormShowStatus.set(1);
        //set unsaving data input -> 'true'
        ClassSession.unSavingDataInput.set(true);
    }

    private void dataRoomUpdateHandle() {
        if (tableDataRoom.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 1;
            selectedData = parentController.getFRoomManager().getRoom(((TblRoom) tableDataRoom.getTableView().getSelectionModel().getSelectedItem()).getIdroom());
            selectedData.setTblLocation(parentController.getFRoomManager().getLocation(selectedData.getTblLocation().getIdlocation()));
            selectedData.getTblLocation().setTblBuilding(parentController.getFRoomManager().getBuilding(selectedData.getTblLocation().getTblBuilding().getIdbuilding()));
            selectedData.getTblLocation().setTblFloor(parentController.getFRoomManager().getFloor(selectedData.getTblLocation().getTblFloor().getIdfloor()));
            setSelectedDataToInputForm();
            //open form data room
            dataRoomFormShowStatus.set(0);
            dataRoomFormShowStatus.set(1);
            //set unsaving data input -> 'true'
            ClassSession.unSavingDataInput.set(true);
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private final BooleanProperty isShowStatus = new SimpleBooleanProperty(false);

    private void dataRoomShowHandle() {
        if (tableDataRoom.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            dataInputStatus = 3;
            selectedData = parentController.getFRoomManager().getRoom(((TblRoom) tableDataRoom.getTableView().getSelectionModel().getSelectedItem()).getIdroom());
            selectedData.setTblLocation(parentController.getFRoomManager().getLocation(selectedData.getTblLocation().getIdlocation()));
            selectedData.getTblLocation().setTblBuilding(parentController.getFRoomManager().getBuilding(selectedData.getTblLocation().getTblBuilding().getIdbuilding()));
            selectedData.getTblLocation().setTblFloor(parentController.getFRoomManager().getFloor(selectedData.getTblLocation().getTblFloor().getIdfloor()));
            setSelectedDataToInputForm();
            dataRoomFormShowStatus.set(1.0);
            isShowStatus.set(true);
        }
    }

    private void dataRoomUnshowHandle() {
        refreshDataTableRoom();
        dataRoomFormShowStatus.set(0);
        isShowStatus.set(false);
    }

    private void dataRoomDeleteHandle() {
        if (tableDataRoom.getTableView().getSelectionModel().getSelectedItems().size() == 1) {
            Alert alert = ClassMessage.showConfirmationDeletingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                if (parentController.getFRoomManager().deleteDataRoom((TblRoom) tableDataRoom.getTableView().getSelectionModel().getSelectedItem())) {
                    ClassMessage.showSucceedDeletingDataMessage("", null);
                    //refresh data from table & close form data room
                    refreshDataTableRoom();
                    dataRoomFormShowStatus.set(0);
                } else {
                    ClassMessage.showFailedDeletingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
                }
            }
        } else {
            ClassMessage.showWarningSelectedDataMessage("", null);
        }
    }

    private void dataRoomPrintHandle() {

    }

    private void dataRoomSaveHandle() {
        if (checkDataInputDataRoom()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblRoom dummySelectedData = new TblRoom(selectedData);
                dummySelectedData.setTblLocation(new TblLocation(dummySelectedData.getTblLocation()));
                dummySelectedData.setTblRoomType(new TblRoomType(dummySelectedData.getTblRoomType()));
                switch (dataInputStatus) {
                    case 0:
                        if (parentController.getFRoomManager().insertDataRoom(dummySelectedData) != null) {
                            ClassMessage.showSucceedInsertingDataMessage("", null);
                            //refresh data from table & close form data room
                            refreshDataTableRoom();
                            dataRoomFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedInsertingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
                        }
                        break;
                    case 1:
                        if (parentController.getFRoomManager().updateDataRoom(dummySelectedData)) {
                            ClassMessage.showSucceedUpdatingDataMessage("", null);
                            //refresh data from table & close form data room
                            refreshDataTableRoom();
                            dataRoomFormShowStatus.set(0);
                            //set unsaving data input -> 'false'
                            ClassSession.unSavingDataInput.set(false);
                        } else {
                            ClassMessage.showFailedUpdatingDataMessage(parentController.getFRoomManager().getErrorMessage(), null);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, null);
        }
    }

    private void dataRoomCancelHandle() {
        //refresh data from table & close form data room
        refreshDataTableRoom();
        dataRoomFormShowStatus.set(0);
        isShowStatus.set(false);
        //set unsaving data input -> 'false'
        ClassSession.unSavingDataInput.set(false);
    }

    public void refreshDataTableRoom() {
        tableDataRoom.getTableView().setItems(loadAllDataRoom());
        cft.refreshFilterItems(tableDataRoom.getTableView().getItems());
    }

    private String errDataInput;

    private boolean checkDataInputDataRoom() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtRoomName.getText() == null || txtRoomName.getText().equals("")) {
            dataInput = false;
            errDataInput += "Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpBuilding.getValue() == null) {
            dataInput = false;
            errDataInput += "Gedung : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpFloor.getValue() == null) {
            dataInput = false;
            errDataInput += "Lantai : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if(chbJM.isSelected()
                && cbpGroup.getValue() == null){
            dataInput = false;
            errDataInput += "Department : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (cbpType.getValue() == null) {
            dataInput = false;
            errDataInput += "Tipe Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
//        if (cbpRoomStatus.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Status Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
//        if (cbpRoomCleanStatus.getValue() == null) {
//            dataInput = false;
//            errDataInput += "Status Kebersihan: " + ClassMessage.defaultErrorNullValueMessage + " \n";
//        }
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
        setDataRoomSplitpane();

        //init table
        initTableDataRoom();

        //init form
        initFormDataRoom();

        spDataRoom.widthProperty().addListener((obs, oldVal, newVal) -> {
            dataRoomFormShowStatus.set(0);
        });
    }

    public RoomController(FeatureRoomAndServiceMDController parentController) {
        this.parentController = parentController;
    }

    private final FeatureRoomAndServiceMDController parentController;

}
