/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_partner.partner_payment_x;

import hotelfx.HotelFX;
import hotelfx.persistence.service.FPartnerManager;
import hotelfx.view.feature_partner.FeaturePartnerController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class PartnerPaymentLayoutController implements Initializable {

    @FXML
    private AnchorPane ancPartnerPayment;
    
    @FXML
    private AnchorPane ancPartnerPaymentInvoice;
    
    @FXML
    private TabPane tabPaneLayout;
    
    private void setDataContent(){
        //partner payment
        setPartnerPaymentContent();
        //partner payment inovice
        setPartnerPaymentInvoiceContent();
    }
    
    private void setPartnerPaymentContent() {
//        try {
//            //loader data (path)
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_partner/partner_payment/PartnerPaymentView.fxml"));
//
//            //set controller
//            PartnerPaymentController partnerPaymentController = new PartnerPaymentController(this);
//            loader.setController(partnerPaymentController);
//
//            Node roomChekcDataContent = loader.load();
//
//            //set anchor position
//            AnchorPane.setTopAnchor(roomChekcDataContent, 0.0);
//            AnchorPane.setLeftAnchor(roomChekcDataContent, 0.0);
//            AnchorPane.setRightAnchor(roomChekcDataContent, 0.0);
//            AnchorPane.setBottomAnchor(roomChekcDataContent, 0.0);
//
//            //set 'data' into the center of dashboard.
//            ancPartnerPayment.getChildren().clear();
//            ancPartnerPayment.getChildren().add(roomChekcDataContent);
//
//        } catch (Exception e) {
//            System.out.println("err >> " + e.getMessage());
//        }
    }
    
    private void setPartnerPaymentInvoiceContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_partner/partner_payment/PartnerPaymentInvoiceView.fxml"));

            //set controller
            PartnerPaymentInvoiceController partnerPaymentInvoiceController = new PartnerPaymentInvoiceController(this);
            loader.setController(partnerPaymentInvoiceController);

            Node employeeAttendanceDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(employeeAttendanceDataContent, 0.0);
            AnchorPane.setLeftAnchor(employeeAttendanceDataContent, 0.0);
            AnchorPane.setRightAnchor(employeeAttendanceDataContent, 0.0);
            AnchorPane.setBottomAnchor(employeeAttendanceDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancPartnerPaymentInvoice.getChildren().clear();
            ancPartnerPaymentInvoice.getChildren().add(employeeAttendanceDataContent);

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
        //set data content
        setDataContent();
        
        //set tab-pane listener
        tabPaneLayout.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null){
                if(newVal.getText().equals("Data Piutang")){
                    setPartnerPaymentContent();
                }else{
                    setPartnerPaymentInvoiceContent();
                }
            }
        });
    }    
    
    public PartnerPaymentLayoutController(FeaturePartnerController parentController) {
        this.parentController = parentController;
    }

    private final FeaturePartnerController parentController;

    public FPartnerManager getService() {
        return parentController.getFPartnerManager();
    }
    
}
