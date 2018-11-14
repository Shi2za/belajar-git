/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
//import xhotelfx.persistence.model._TblFinanceTransactionPaymentType;
import hotelfx.view.ReservationController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class ReservationTransactionDialogController implements Initializable {

//    private ReservationController reservationController;
//
//    public void setReservationController(ReservationController reservationController) {
//        this.reservationController = reservationController;
//    }
//
//    @FXML
//    JFXComboBox<_TblFinanceTransactionPaymentType> cbPaymentType;
//    
//    @FXML
//    JFXTextField txtUnitCost;
//    
//    @FXML
//    JFXTextField txtUnitNumber;
//    
//    @FXML
//    JFXButton btnSave;
//    
//    @FXML
//    JFXButton btncCancel;
//    
//    @FXML
//    JFXButton btnGetRestOfBill;
//    
//    private void initComboBoxPaymnetType(){
//        ObservableList<_TblFinanceTransactionPaymentType> list = FXCollections.observableArrayList();
//        list.add(new _TblFinanceTransactionPaymentType("01", "Tunai", "AAA"));
//        list.add(new _TblFinanceTransactionPaymentType("02", "Transfer", "BBB"));
//        list.add(new _TblFinanceTransactionPaymentType("03", "Debit", "CCC"));
//        list.add(new _TblFinanceTransactionPaymentType("04", "Kredit", "CCC"));
//        list.add(new _TblFinanceTransactionPaymentType("05", "Cek", "CCC"));
//        list.add(new _TblFinanceTransactionPaymentType("06", "Giro", "CCC"));
//        list.add(new _TblFinanceTransactionPaymentType("07", "Travel Agent", "CCC"));
//        list.add(new _TblFinanceTransactionPaymentType("08", "Corporate", "CCC"));
//        list.add(new _TblFinanceTransactionPaymentType("09", "Goverment", "CCC"));
//        list.add(new _TblFinanceTransactionPaymentType("10", "Voucher", "CCC"));
//        list.add(new _TblFinanceTransactionPaymentType("11", "Draw Deposit", "CCC"));
//        cbPaymentType.setItems(list);
//
//        cbPaymentType.setCellFactory(new Callback<ListView<_TblFinanceTransactionPaymentType>, 
//                ListCell<_TblFinanceTransactionPaymentType>>(){
//            @Override
//            public ListCell<_TblFinanceTransactionPaymentType> call(ListView<_TblFinanceTransactionPaymentType> param) {
//                ListCell<_TblFinanceTransactionPaymentType> lc = new ListCell<_TblFinanceTransactionPaymentType>(){
//                    @Override
//                    protected void updateItem(_TblFinanceTransactionPaymentType data, boolean b){
//                        super.updateItem(data, b);
//                        if(data != null){
//                            setGraphic(new Label(data.getName()));
//                        }else{
//                            setGraphic(null);
//                        }
//                    }
//                    
//                };
//                return lc;
//            }
//            
//        });
//        
//        cbPaymentType.setConverter(new StringConverter<_TblFinanceTransactionPaymentType>() {
//
//            @Override
//            public String toString(_TblFinanceTransactionPaymentType object) {
//                if(object == null){
//                    return "[none]";
//                }else{
//                    return object.getName();
//                }
//            }
//
//            @Override
//            public _TblFinanceTransactionPaymentType fromString(String string) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
//        
//        cbPaymentType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
//            refreshDataInputForm();
//        });
//        
//    }
//    
//    private void initButtonFunction(){
//        //save
//        btnSave.setOnMouseClicked((e) -> {
//            
//            reservationController.closeDialogStage();
//        });
//        btnSave.ripplerFillProperty().set(Color.GOLD);
//        //cancel
//        btncCancel.setOnMouseClicked((e) -> {
//            
//            reservationController.closeDialogStage();
//        });
//        btncCancel.ripplerFillProperty().set(Color.GOLD);
//        //get rest of bill
//        btnGetRestOfBill.setOnMouseClicked((e) -> {
//            txtUnitCost.setText(String.valueOf(reservationController.getRestOfBill()));
//            txtUnitNumber.setText("1");
//        });
//        btnGetRestOfBill.setGraphic(new ImageView(new Image("file:resources/Icon/Basic/get-bill-g-128.png", 25, 25, true, true)));
//        btnGetRestOfBill.setText("");
//        btnGetRestOfBill.ripplerFillProperty().set(Color.GOLD);
//    }
//    
//    @FXML
//    AnchorPane apCash;
//    
//    @FXML
//    AnchorPane apTransfer;
//    
//    @FXML
//    AnchorPane apDebitCard;
//    
//    @FXML
//    AnchorPane apCreditCard;
//    
//    @FXML
//    AnchorPane apCek;
//    
//    @FXML
//    AnchorPane apGiro;
//    
//    @FXML
//    AnchorPane apTravelAgent;
//    
//    @FXML
//    AnchorPane apCorporate;
//    
//    @FXML
//    AnchorPane apGoverment;
//    
//    @FXML
//    AnchorPane apVoucher;
//    
//    @FXML
//    AnchorPane apDrawDeposit;
//    
//    private void refreshDataInputForm(){
//        apCash.setVisible(false);
//        apTransfer.setVisible(false);
//        apDebitCard.setVisible(false);
//        apCreditCard.setVisible(false);
//        apCek.setVisible(false);
//        apGiro.setVisible(false);
//        apTravelAgent.setVisible(false);
//        apCorporate.setVisible(false);
//        apGoverment.setVisible(false);
//        apVoucher.setVisible(false);
//        apDrawDeposit.setVisible(false);
//        switch(cbPaymentType.getSelectionModel().getSelectedItem().getName()){
//            case "Tunai":
//                apCash.setVisible(true);
//                break;
//            case "Transfer":
//                apTransfer.setVisible(true);
//                break;
//            case "Debit":
//                apDebitCard.setVisible(true);
//                break;
//            case "Kredit":
//                apCreditCard.setVisible(true);
//                break;
//            case "Cek":
//                apCek.setVisible(true);
//                break;
//            case "Giro":
//                apGiro.setVisible(true);
//                break;
//            case "Travel Agent":
//                apTravelAgent.setVisible(true);
//                break;
//            case "Corporate":
//                apCorporate.setVisible(true);
//                break;
//            case "Goverment":
//                apGoverment.setVisible(true);
//                break;
//            case "Voucher":
//                apVoucher.setVisible(true);
//                break;
//            case "Draw Deposit":
//                apDrawDeposit.setVisible(true);
//                break;
//            default :
//                break;
//        }
//    }
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        //set combobox - transaction-type
//        initComboBoxPaymnetType();
//        //set functiononality of button
//        initButtonFunction();
    }

}
