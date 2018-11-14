/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.partner_receivable_and_payment;

import hotelfx.HotelFX;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FPartnerManager;
import hotelfx.view.feature_finance_transaction.FeatureFinanceTransactionController;
import hotelfx.view.feature_partner.FeaturePartnerController;
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
public class PartnerReceivableAndPaymentController implements Initializable {

    @FXML
    private AnchorPane ancPartnerReceivable;
    
    @FXML
    private AnchorPane ancPartnerPayment;
    
    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();
    
    @FXML
    private TabPane tabPaneLayout;
    
    private void setDataPRAPTabpane() {
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
        //partner receivable
        setPartnerReceivableContent();
        //partner payment
        setPartnerPaymentContent();
    }
    
    private void setPartnerReceivableContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/partner_receivable_and_payment/PartnerReceivableView.fxml"));

            //set controller
            PartnerReceivableController partnerReceivableController = new PartnerReceivableController(this);
            loader.setController(partnerReceivableController);

            Node poPayableDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(poPayableDataContent, 0.0);
            AnchorPane.setLeftAnchor(poPayableDataContent, 0.0);
            AnchorPane.setRightAnchor(poPayableDataContent, 0.0);
            AnchorPane.setBottomAnchor(poPayableDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancPartnerReceivable.getChildren().clear();
            ancPartnerReceivable.getChildren().add(poPayableDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    private void setPartnerPaymentContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/partner_receivable_and_payment/PartnerPaymentView.fxml"));

            //set controller
            PartnerPaymentController partnerPaymentController = new PartnerPaymentController(this);
            loader.setController(partnerPaymentController);

            Node poPaymentDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setLeftAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setRightAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setBottomAnchor(poPaymentDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancPartnerPayment.getChildren().clear();
            ancPartnerPayment.getChildren().add(poPaymentDataContent);

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
        setDataPRAPTabpane();
        
        //set data content
        setDataContent();
        
        //set tab-pane listener
        tabPaneLayout.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                if(newVal.getText().equals("Data Piutang")){
                    setPartnerReceivableContent();
                }else{
                    setPartnerPaymentContent();
                }
            }
        });
    }    
    
    public PartnerReceivableAndPaymentController(FeatureFinanceTransactionController parentController) {
        this.parentController = parentController;
    }

    private final FeatureFinanceTransactionController parentController;

    public FPartnerManager getService() {
        return parentController.getFPartnerManager();
    }
    
}
