/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.travel_agent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblPartner;
import hotelfx.persistence.model.TblRoomType;
import hotelfx.persistence.model.TblTravelAgent;
import hotelfx.persistence.model.TblTravelAgentRoomType;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class TravelAgentRoomTypeSettingDetailInputController implements Initializable {

    @FXML
    private AnchorPane ancFormTravelAgentRoomTypeSettingDetailInput;

    @FXML
    private GridPane gpFormDataTravelAgentRoomTypeSettingDetailInput;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtRoomType;

    @FXML
    private JFXTextField txtRoomAvailableNumber;

    @FXML
    private AnchorPane ancTableTravelAgentRoomTypeNumber;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataTravelAgentRoomTypeSettingDetailInput() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Ketersediaan Jumlah Kamar)"));
        btnSave.setOnAction((e) -> {
            dataTravelAgentRoomTypeSettingDetailInputSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataTravelAgentRoomTypeSettingDetailInputCancelHandle();
        });

        initImportantFieldColor();

    }

    private void initImportantFieldColor() {
//        ClassViewSetting.setImportantField(
//                );
    }

    private final IntegerProperty roomAvailableNumber = new SimpleIntegerProperty();

    private List<PartnerRoomTypeNumberHasBeenReserved> partnerRoomTypeNumberHasBeenReserveds;

    private List<TblTravelAgentRoomType> travelAgentRoomTypes;

    private List<TblTravelAgentRoomType> loadAllDataTravelAgentRoomType() {
        List<TblTravelAgentRoomType> list = new ArrayList<>();
        List<TblTravelAgent> tempTravelAgents = travelAgentRoomTypeSettingInputController.getParentController().getService().getAllDataTravelAgent();
        for (TblTravelAgent tempTravelAgent : tempTravelAgents) {
            TblTravelAgentRoomType data = travelAgentRoomTypeSettingInputController.getParentController().getService().getDataTravelAgentRoomTypeByIDRoomTypeAndIDPartnerAndAvailableDate(
                    travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getButtonValue().getIdroomType(),
                    tempTravelAgent.getTblPartner().getIdpartner(),
                    Date.valueOf(travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getDate()));
            if (data == null) {
                data = new TblTravelAgentRoomType();
                data.setTblPartner(travelAgentRoomTypeSettingInputController.getParentController().getService().getPartner(tempTravelAgent.getTblPartner().getIdpartner()));
                data.setTblRoomType(travelAgentRoomTypeSettingInputController.getParentController().getService().getDataRoomType(travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getButtonValue().getIdroomType()));
                data.setAvailableDate(Date.valueOf(travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getDate()));
                data.setRoomNumber(0);
            }
            list.add(data);
        }
        return list;
    }

    public TableView tableTravelAgentRoomTypeNumber;

    public void setDataTravelAgentRoomTypeNumber() {
        ancTableTravelAgentRoomTypeNumber.getChildren().clear();

        tableTravelAgentRoomTypeNumber = new TableView();
        tableTravelAgentRoomTypeNumber.setEditable(true);

        TableColumn<TblTravelAgentRoomType, String> partnerName = new TableColumn("Travel Agent");
        partnerName.setMinWidth(190);
        partnerName.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgentRoomType, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getTblPartner().getPartnerName(), param.getValue().tblPartnerProperty()));

        TableColumn<TblTravelAgentRoomType, String> roomNumber = new TableColumn("Jumlah Kamar");
        roomNumber.setMinWidth(180);
        roomNumber.setCellValueFactory((TableColumn.CellDataFeatures<TblTravelAgentRoomType, String> param)
                -> Bindings.createStringBinding(() -> String.valueOf(param.getValue().getRoomNumber()), param.getValue().roomNumberProperty()));
        Callback<TableColumn<TblTravelAgentRoomType, String>, TableCell<TblTravelAgentRoomType, String>> cellFactory
                = new Callback<TableColumn<TblTravelAgentRoomType, String>, TableCell<TblTravelAgentRoomType, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellRoomNumber(0, 1000);
                    }
                };
        roomNumber.setCellFactory(cellFactory);
        roomNumber.setEditable(true);

        tableTravelAgentRoomTypeNumber.getColumns().addAll(partnerName, roomNumber);

        tableTravelAgentRoomTypeNumber.setItems(FXCollections.observableArrayList(travelAgentRoomTypes));

        AnchorPane.setBottomAnchor(tableTravelAgentRoomTypeNumber, 0.0);
        AnchorPane.setLeftAnchor(tableTravelAgentRoomTypeNumber, 0.0);
        AnchorPane.setRightAnchor(tableTravelAgentRoomTypeNumber, 0.0);
        AnchorPane.setTopAnchor(tableTravelAgentRoomTypeNumber, 0.0);
        ancTableTravelAgentRoomTypeNumber.getChildren().add(tableTravelAgentRoomTypeNumber);
    }

    private boolean isListenerRunning = false;

    class EditingCellRoomNumber extends TableCell<TblTravelAgentRoomType, String> {

        private final int min;
        private final int max;

        private Spinner<Integer> spRoomNumber;

        public EditingCellRoomNumber(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void startEdit() {
            if (!isEmpty()
                    && this.getTableRow() != null) {
                super.startEdit();
                spRoomNumber = new Spinner<>();
                
                spRoomNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));
                spRoomNumber.setEditable(false);

                ClassViewSetting.setImportantField(
                        spRoomNumber);

                spRoomNumber.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                spRoomNumber.getValueFactory().setValue(((TblTravelAgentRoomType) this.getTableRow().getItem()).getRoomNumber());
                spRoomNumber.getValueFactory().valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (!isListenerRunning) {
                        isListenerRunning = true;
                        int change = newVal - oldVal;
                        int numberHasBeenReserved = 0;
                        for (PartnerRoomTypeNumberHasBeenReserved partnerRoomTypeNumberHasBeenReserved : partnerRoomTypeNumberHasBeenReserveds) {
                            if (partnerRoomTypeNumberHasBeenReserved.getDataPartner().getIdpartner()
                                    == ((TblTravelAgentRoomType) this.getTableRow().getItem()).getTblPartner().getIdpartner()) {
                                numberHasBeenReserved = partnerRoomTypeNumberHasBeenReserved.getDataReservedNumber();
                                break;
                            }
                        }
                        if (change > roomAvailableNumber.get()) {
                            spRoomNumber.getValueFactory().setValue(spRoomNumber.getValueFactory().getValue()
                                    - (change - roomAvailableNumber.get()));
                            change = roomAvailableNumber.get();
                        } else {
                            if (newVal
                                    < numberHasBeenReserved) {
                                spRoomNumber.getValueFactory().setValue(numberHasBeenReserved);
                                change = numberHasBeenReserved - oldVal;
                            } else {
                                if (newVal < min) {
                                    spRoomNumber.getValueFactory().setValue(min);
                                    change = min - oldVal;
                                } else {
                                    if (newVal > max) {
                                        spRoomNumber.getValueFactory().setValue(max);
                                        change = max - oldVal;
                                    }
                                }
                            }
                        }
                        ((TblTravelAgentRoomType) this.getTableRow().getItem()).setRoomNumber(spRoomNumber.getValueFactory().getValue());
                        roomAvailableNumber.set(roomAvailableNumber.get() - change);
                        isListenerRunning = false;
                    }
                });

                setText(null);
                setGraphic(spRoomNumber);

                //cell input color
                if (this.getTableRow() != null) {
                    if (this.getTableRow().getIndex() % 2 == 0) {
                        getStyleClass().remove("cell-input-even");
                        getStyleClass().add("cell-input-even-edit");
                    } else {
                        getStyleClass().remove("cell-input-odd");
                        getStyleClass().add("cell-input-odd-edit");
                    }
                }
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            spRoomNumber = null;

            setText((String) getItem());
            setGraphic(null);

            //cell input color
            if (this.getTableRow() != null) {
                if (this.getTableRow().getIndex() % 2 == 0) {
                    getStyleClass().remove("cell-input-even-edit");
                    getStyleClass().add("cell-input-even");
                } else {
                    getStyleClass().remove("cell-input-odd-edit");
                    getStyleClass().add("cell-input-odd");
                }
            }
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (!isEditing()) {
                    if (this.getTableRow() != null) {
                        Object data = this.getTableRow().getItem();
                        if (data != null) {
                            setText(String.valueOf(((TblTravelAgentRoomType) data).getRoomNumber()));
                            setGraphic(null);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                        //cell input color
                        if (this.getTableRow().getIndex() % 2 == 0) {
                            getStyleClass().add("cell-input-even");

                        } else {
                            getStyleClass().add("cell-input-odd");
                        }
                    } else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            }
        }

    }

    public class PartnerRoomTypeNumberHasBeenReserved {

        private final ObjectProperty<TblPartner> dataPartner = new SimpleObjectProperty<>();

        private final IntegerProperty dataReservedNumer = new SimpleIntegerProperty();

        public PartnerRoomTypeNumberHasBeenReserved(TblPartner dataPartner, int dataReservedNumer) {

            this.dataPartner.set(dataPartner);
            this.dataReservedNumer.set(dataReservedNumer);

        }

        public ObjectProperty<TblPartner> dataPartnerProperty() {
            return dataPartner;
        }

        public TblPartner getDataPartner() {
            return dataPartnerProperty().get();
        }

        public void setDataPartner(TblPartner dataPartner) {
            dataPartnerProperty().set(dataPartner);
        }

        public IntegerProperty dataReservedNumerProperty() {
            return dataReservedNumer;
        }

        public int getDataReservedNumber() {
            return dataReservedNumerProperty().get();
        }

        public void setDataReservedNumber(int dataReservedNumber) {
            dataReservedNumerProperty().set(dataReservedNumber);
        }

    }

    private void setSelectedDataToInputForm() {

        txtDate.setText(ClassFormatter.dateFormate.format(Date.valueOf(travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getDate())));
        txtRoomType.setText(travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getButtonValue().getRoomTypeName());

        travelAgentRoomTypes = loadAllDataTravelAgentRoomType();

        roomAvailableNumber.set(travelAgentRoomTypeSettingInputController.getRoomAvailableNumber(travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getButtonValue(),
                travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getDate(),
                null)
                - travelAgentRoomTypeSettingInputController.getRoomReservedNumber(travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getButtonValue(),
                        travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getDate(),
                        null));

        txtRoomAvailableNumber.setText(String.valueOf(roomAvailableNumber.get()));

        roomAvailableNumber.addListener((obs, oldVal, newVal) -> {
            txtRoomAvailableNumber.setText(String.valueOf(roomAvailableNumber.get()));
        });

        partnerRoomTypeNumberHasBeenReserveds = new ArrayList<>();
        for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
            TblTravelAgent dataTravelAgent = new TblTravelAgent();
            dataTravelAgent.setTblPartner(new TblPartner(travelAgentRoomType.getTblPartner()));
//            dataTravelAgent.setRoomTypeDiscountPercentage(new BigDecimal("0"));   //'BIGDECIMAL=NULL'
            partnerRoomTypeNumberHasBeenReserveds.add(new PartnerRoomTypeNumberHasBeenReserved(travelAgentRoomType.getTblPartner(),
                    travelAgentRoomTypeSettingInputController.getRoomReservedNumber(
                            travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getButtonValue(),
                            travelAgentRoomTypeSettingInputController.selectedDataCalendarRoomTypeAvailableButton.getDate(),
                            dataTravelAgent)));
        }

        setDataTravelAgentRoomTypeNumber();

    }

    private void dataTravelAgentRoomTypeSettingDetailInputSaveHandle() {
        if (checkDataInputDataTravelAgentRoomTypeSettingDetailInput()) {
            Alert alert = ClassMessage.showConfirmationSavingDataMessage("", travelAgentRoomTypeSettingInputController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                List<TblTravelAgentRoomType> dummyTravelAgentRoomTypes = new ArrayList<>();
                for (TblTravelAgentRoomType travelAgentRoomType : travelAgentRoomTypes) {
                    TblTravelAgentRoomType dummyTravelAgentRoomType = new TblTravelAgentRoomType(travelAgentRoomType);
                    dummyTravelAgentRoomType.setTblRoomType(new TblRoomType(dummyTravelAgentRoomType.getTblRoomType()));
                    dummyTravelAgentRoomType.setTblPartner(new TblPartner(dummyTravelAgentRoomType.getTblPartner()));
                    dummyTravelAgentRoomTypes.add(dummyTravelAgentRoomType);
                }
                if (travelAgentRoomTypeSettingInputController.getParentController().getService().updateDataTravelAgentRoomType(dummyTravelAgentRoomTypes)) {
                    ClassMessage.showSucceedUpdatingDataMessage("", travelAgentRoomTypeSettingInputController.dialogStage);
                    //refresh data from calendar - room type (available) & close form data tarts
                    travelAgentRoomTypeSettingInputController.selectedDate.set(LocalDate.now());
                    travelAgentRoomTypeSettingInputController.refreshCalendarRoomTypeAvailableContetnt();
                    //close dialog stage
                    travelAgentRoomTypeSettingInputController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(travelAgentRoomTypeSettingInputController.getParentController().getService().getErrorMessage(), travelAgentRoomTypeSettingInputController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, travelAgentRoomTypeSettingInputController.dialogStage);
        }
    }

    private void dataTravelAgentRoomTypeSettingDetailInputCancelHandle() {
        //refresh data from calendar - room type (available) & close form data tarts
        travelAgentRoomTypeSettingInputController.selectedDate.set(LocalDate.now());
        travelAgentRoomTypeSettingInputController.refreshCalendarRoomTypeAvailableContetnt();
        //close dialog stage
        travelAgentRoomTypeSettingInputController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataInputDataTravelAgentRoomTypeSettingDetailInput() {
        boolean dataInput = true;
        errDataInput = "";
//        if (txtRoomNumber.getText() == null && txtRoomNumber.getText().equals("")) {
//            dataInput = false;
//            errDataInput += "Jumlah Kamar : " + ClassMessage.defaultErrorNullValueMessage + " \n";
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
        //init form input
        initFormDataTravelAgentRoomTypeSettingDetailInput();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public TravelAgentRoomTypeSettingDetailInputController(TravelAgentRoomTypeSettingInputController parentController) {
        travelAgentRoomTypeSettingInputController = parentController;
    }

    private final TravelAgentRoomTypeSettingInputController travelAgentRoomTypeSettingInputController;

}
