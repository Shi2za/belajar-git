/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.tid_r;

import com.jfoenix.controls.JFXCComboBoxTablePopup;
import com.jfoenix.controls.JFXTextField;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblCompanyBalance;
import hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.HotelFinanceTransactionReturnInputController;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class TIDRCashController implements Initializable {

    @FXML
    private GridPane gpTIDRCash;
    
    @FXML
    private JFXTextField txtRoundingValueNominal;
    
    @FXML
    private JFXTextField txtNominalAfterRoundingValue;
    
    @FXML
    private AnchorPane ancCompanyBalanceLayout;
    private JFXCComboBoxTablePopup<TblCompanyBalance> cbpCompanyBalance;
    
    @FXML
    private JFXTextField txtBackOfficeBalance;
    
    private void initFormDataTIDRCash() {
        initDataTIDRCashPopup();
        
        txtRoundingValueNominal.setDisable(true);
        txtNominalAfterRoundingValue.setDisable(true);
        
        ancCompanyBalanceLayout.setVisible(false);
        
        initImportantFieldColor();
    }
    
    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpCompanyBalance);
    }
    
    private void initDataTIDRCashPopup() {
        //Company Balance
        TableView<TblCompanyBalance> tableCompanyBalance = new TableView<>();
        
        TableColumn<TblCompanyBalance, String> companyBalanceName = new TableColumn<>("Kas");
        companyBalanceName.setCellValueFactory(cellData -> cellData.getValue().balanceNameProperty());
        companyBalanceName.setMinWidth(140);
        
        TableColumn<TblCompanyBalance, String> companyBalanceNominal = new TableColumn<>("Saldo");
        companyBalanceNominal.setCellValueFactory((TableColumn.CellDataFeatures<TblCompanyBalance, String> param)
                -> Bindings.createStringBinding(() -> ClassFormatter.currencyFormat.format(param.getValue().getBalanceNominal()),
                        param.getValue().balanceNameProperty()));
        companyBalanceNominal.setMinWidth(140);
        
        tableCompanyBalance.getColumns().addAll(companyBalanceName, companyBalanceNominal);
        
        ObservableList<TblCompanyBalance> companyBalanceItems = FXCollections.observableArrayList(loadAllDataCompanyBalance());
        
        cbpCompanyBalance = new JFXCComboBoxTablePopup<>(
                TblCompanyBalance.class, tableCompanyBalance, companyBalanceItems, "", "Kas *", true, 300, 200
        );

        //attached to grid-pane
        AnchorPane.setBottomAnchor(cbpCompanyBalance, 0.0);
        AnchorPane.setLeftAnchor(cbpCompanyBalance, 0.0);
        AnchorPane.setRightAnchor(cbpCompanyBalance, 0.0);
        AnchorPane.setTopAnchor(cbpCompanyBalance, 0.0);
        ancCompanyBalanceLayout.getChildren().clear();
        ancCompanyBalanceLayout.getChildren().add(cbpCompanyBalance);
    }
    
    private List<TblCompanyBalance> loadAllDataCompanyBalance() {
        List<TblCompanyBalance> list = hotelFinanceTransactionReturnInputController.getParentController().getService().getAllDataCompanyBalance();
        for(int i=list.size()-1; i>-1; i--){
            if(list.get(i).getIdbalance() != 2  //Kas Back-Ofifce = '2'
                    && list.get(i).getIdbalance() != 3){    //Kas Kasir = '3'
                list.remove(i);
            }
        }
        return list;
    }
    
    private void refreshDataPopup() {
        //company balance
        ObservableList<TblCompanyBalance> companyBalanceItems = FXCollections.observableArrayList(loadAllDataCompanyBalance());
        cbpCompanyBalance.setItems(companyBalanceItems);
    }
    
    private final ObjectProperty<BigDecimal> roundingValue = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> nominalAfterRoundingValue = new SimpleObjectProperty<>();
    
    private void setSelectedDataToInputForm() {
        refreshDataPopup();
        
        roundingValue.addListener((obs, oldValue, newValue) -> {
            if(newValue != null){
                txtRoundingValueNominal.setText(ClassFormatter.currencyFormat.format(newValue));
            }else{
                txtRoundingValueNominal.setText(ClassFormatter.currencyFormat.format(new BigDecimal("0")));
            }
        });
        nominalAfterRoundingValue.addListener((obs, oldValue, newValue) -> {
            if(newValue != null){
                txtNominalAfterRoundingValue.setText(ClassFormatter.currencyFormat.format(newValue));
            }else{
                txtNominalAfterRoundingValue.setText(ClassFormatter.currencyFormat.format(new BigDecimal("0")));
            }
        });
        
        roundingValue.bindBidirectional(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFT.transactionRoundingValueProperty());
        nominalAfterRoundingValue.bindBidirectional(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFT.transactionNominalProperty());
        
        cbpCompanyBalance.valueProperty().bindBidirectional(hotelFinanceTransactionReturnInputController.getParentController().selectedDataHFTWithCash.tblCompanyBalanceProperty());
        cbpCompanyBalance.setValue(hotelFinanceTransactionReturnInputController.getParentController().getService().getDataCompanyBalance(2));    //Kas Back-Ofiice = '2'
        
        cbpCompanyBalance.hide();
        
        txtBackOfficeBalance.setText(ClassFormatter.currencyFormat.format(cbpCompanyBalance.getValue() != null 
                        ? cbpCompanyBalance.getValue().getBalanceNominal() : new BigDecimal("0")));
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init form input
        initFormDataTIDRCash();
        //refresh data form input
        setSelectedDataToInputForm();
    }    
    
    public TIDRCashController(HotelFinanceTransactionReturnInputController parentController) {
        hotelFinanceTransactionReturnInputController = parentController;
    }
    
    private final HotelFinanceTransactionReturnInputController hotelFinanceTransactionReturnInputController;
    
}
