/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_receivable_and_payment;

import hotelfx.HotelFX;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FReturManager;
import hotelfx.view.feature_retur.FeatureReturController;
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
public class ReturReceivableAndPaymentController implements Initializable {

    @FXML
    private AnchorPane ancReturReceivable;
    
    @FXML
    private AnchorPane ancReturPayment;
    
    private final BooleanProperty unSavingDataInput = new SimpleBooleanProperty();
    
    @FXML
    private TabPane tabPaneLayout;
    
    private void setDataRRAPTabpane() {
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
        //retur receivable
        setReturReceivableContent();
        //retur payment
        setReturPaymentContent();
    }
    
    private void setReturReceivableContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_receivable_and_payment/ReturReceivableView.fxml"));

            //set controller
            ReturReceivableController returReceivableController = new ReturReceivableController(this);
            loader.setController(returReceivableController);

            Node poPayableDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(poPayableDataContent, 0.0);
            AnchorPane.setLeftAnchor(poPayableDataContent, 0.0);
            AnchorPane.setRightAnchor(poPayableDataContent, 0.0);
            AnchorPane.setBottomAnchor(poPayableDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancReturReceivable.getChildren().clear();
            ancReturReceivable.getChildren().add(poPayableDataContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    private void setReturPaymentContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_receivable_and_payment/ReturPaymentView.fxml"));

            //set controller
            ReturPaymentController returPaymentController = new ReturPaymentController(this);
            loader.setController(returPaymentController);

            Node poPaymentDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setLeftAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setRightAnchor(poPaymentDataContent, 0.0);
            AnchorPane.setBottomAnchor(poPaymentDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancReturPayment.getChildren().clear();
            ancReturPayment.getChildren().add(poPaymentDataContent);

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
        setDataRRAPTabpane();
        
        //set data content
        setDataContent();
        
        //set tab-pane listener
        tabPaneLayout.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                if(newVal.getText().equals("Data Piutang")){
                    setReturReceivableContent();
                }else{
                    setReturPaymentContent();
                }
            }
        });
    }    
    
    public ReturReceivableAndPaymentController(FeatureReturController parentController) {
        this.parentController = parentController;
    }

    private final FeatureReturController parentController;

    public FReturManager getService() {
        return parentController.getFReturManager();
    }
    
}
