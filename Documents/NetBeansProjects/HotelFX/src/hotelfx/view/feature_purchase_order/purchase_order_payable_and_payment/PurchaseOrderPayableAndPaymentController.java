/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment;

import hotelfx.HotelFX;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FPurchaseOrderManager;
import hotelfx.view.feature_finance_transaction.FeatureFinanceTransactionController;
import hotelfx.view.feature_purchase_order.FeaturePurchaseOrderController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PurchaseOrderPayableAndPaymentController implements Initializable {

    @FXML
    private AnchorPane ancPOPayable;
    
    @FXML
    private AnchorPane ancPOPayment;
    
    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();
    
    @FXML
    private TabPane tabPaneLayout;
    
    private void setDataPOPAPTabpane() {
        unSavingDataInput.bind(ClassSession.unSavingDataInput);

        unSavingDataInput.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                for (Tab tab : tabPaneLayout.getTabs()) {
                    if(tabPaneLayout.getSelectionModel().getSelectedItem() != null
                            && tabPaneLayout.getSelectionModel().getSelectedItem().equals(tab)){
                        tab.setDisable(false);
                    }else{
                        tab.setDisable(true);
                    }
                }
            } else {
                for (Tab tab : tabPaneLayout.getTabs()) {
                    tab.setDisable(false);
                }
            }
        });
    }
    
    private void setDataContent(){
        //po payable
        setPOPayableContent();
        //po payment
        setPOPaymentContent();
    }
    
    private void setPOPayableContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/PurchaseOrderPayableView.fxml"));

            //set controller
            PurchaseOrderPayableController purchaseOrderPayableController = new PurchaseOrderPayableController(this);
            loader.setController(purchaseOrderPayableController);

            Node poPayableDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(poPayableDataContent, 0.0);
            AnchorPane.setLeftAnchor(poPayableDataContent, 0.0);
            AnchorPane.setRightAnchor(poPayableDataContent, 0.0);
            AnchorPane.setBottomAnchor(poPayableDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancPOPayable.getChildren().clear();
            ancPOPayable.getChildren().add(poPayableDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    private void setPOPaymentContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_payable_and_payment/PurchaseOrderPaymentView.fxml"));

            //set controller
            PurchaseOrderPaymentController purchaseOrderPaymentController = new PurchaseOrderPaymentController(this);
            loader.setController(purchaseOrderPaymentController);

            Node poPaymentDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setLeftAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setRightAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setBottomAnchor(poPaymentDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancPOPayment.getChildren().clear();
            ancPOPayment.getChildren().add(poPaymentDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    public void refreshAllContent(){
        setDataContent();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set tab pane
        setDataPOPAPTabpane();
        
        //set data content
        setDataContent();
        
        //set tab-pane listener
        tabPaneLayout.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                if(newVal.getText().equals("Data Hutang")){
                    setPOPayableContent();
                }else{
                    setPOPaymentContent();
                }
            }
        });
    }    
    
    public PurchaseOrderPayableAndPaymentController(FeatureFinanceTransactionController parentController) {
        this.parentController = parentController;
    }

    private final FeatureFinanceTransactionController parentController;

    public FPurchaseOrderManager getService() {
        return parentController.getFPurchaseOrderManager();
    }
    
}
