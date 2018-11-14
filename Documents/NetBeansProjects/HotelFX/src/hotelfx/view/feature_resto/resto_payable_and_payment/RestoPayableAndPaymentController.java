/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_resto.resto_payable_and_payment;

import hotelfx.HotelFX;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FRestoManager;
import hotelfx.view.feature_finance_transaction.FeatureFinanceTransactionController;
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
 * @author ABC-Programmer
 */
public class RestoPayableAndPaymentController implements Initializable {

    @FXML
    private AnchorPane ancRestoPayable;
    
    @FXML
    private AnchorPane ancRestoPayment;
    
    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();
    
    @FXML
    private TabPane tabPaneLayout;
    
    private void setDataRestoPAPTabpane() {
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
        //resto payable
        setRestoPayableContent();
        //restu payment
        setRestoPaymentContent();
    }
    
    private void setRestoPayableContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_resto/resto_payable_and_payment/RestoPayableView.fxml"));

            //set controller
            RestoPayableController restoPayableController = new RestoPayableController(this);
            loader.setController(restoPayableController);

            Node restoPayableDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(restoPayableDataContent, 0.0);
            AnchorPane.setLeftAnchor(restoPayableDataContent, 0.0);
            AnchorPane.setRightAnchor(restoPayableDataContent, 0.0);
            AnchorPane.setBottomAnchor(restoPayableDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancRestoPayable.getChildren().clear();
            ancRestoPayable.getChildren().add(restoPayableDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    private void setRestoPaymentContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_resto/resto_payable_and_payment/RestoPaymentView.fxml"));

            //set controller
            RestoPaymentController restoPaymentController = new RestoPaymentController(this);
            loader.setController(restoPaymentController);

            Node restoPaymentDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(restoPaymentDataContent, 0.0);
            AnchorPane.setLeftAnchor(restoPaymentDataContent, 0.0);
            AnchorPane.setRightAnchor(restoPaymentDataContent, 0.0);
            AnchorPane.setBottomAnchor(restoPaymentDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancRestoPayment.getChildren().clear();
            ancRestoPayment.getChildren().add(restoPaymentDataContent);

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
        setDataRestoPAPTabpane();
        
        //set data content
        setDataContent();
        
        //set tab-pane listener
        tabPaneLayout.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                if(newVal.getText().equals("Data Hutang")){
                    setRestoPayableContent();
                }else{
                    setRestoPaymentContent();
                }
            }
        });
    }    
    
    public RestoPayableAndPaymentController(FeatureFinanceTransactionController parentController) {
        this.parentController = parentController;
    }

    private final FeatureFinanceTransactionController parentController;

    public FRestoManager getService() {
        return parentController.getFRestoManager();
    }
    
}
