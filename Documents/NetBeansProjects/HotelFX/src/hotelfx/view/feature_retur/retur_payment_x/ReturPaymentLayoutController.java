/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_retur.retur_payment_x;

import hotelfx.HotelFX;
import hotelfx.persistence.service.FReturManager;
import hotelfx.view.feature_retur.FeatureReturController;
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
public class ReturPaymentLayoutController implements Initializable {

    @FXML
    private AnchorPane ancReturPayment;
    
    @FXML
    private AnchorPane ancReturPaymentInvoice;
    
    @FXML
    private TabPane tabPaneLayout;
    
    private void setDataContent(){
        //retur payment
        setReturPaymentContent();
        //retur payment inovice
        setReturPaymentInvoiceContent();
    }
    
    private void setReturPaymentContent() {
//        try {
//            //loader data (path)
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_payment/ReturPaymentView.fxml"));
//
//            //set controller
//            ReturPaymentController returPaymentController = new ReturPaymentController(this);
//            loader.setController(returPaymentController);
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
//            ancReturPayment.getChildren().clear();
//            ancReturPayment.getChildren().add(roomChekcDataContent);
//
//        } catch (Exception e) {
//            System.out.println("err >> " + e.getMessage());
//        }
    }
    
    private void setReturPaymentInvoiceContent() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/feature_retur/retur_payment/ReturPaymentInvoiceView.fxml"));

            //set controller
            ReturPaymentInvoiceController returPaymentInvoiceController = new ReturPaymentInvoiceController(this);
            loader.setController(returPaymentInvoiceController);

            Node employeeAttendanceDataContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(employeeAttendanceDataContent, 0.0);
            AnchorPane.setLeftAnchor(employeeAttendanceDataContent, 0.0);
            AnchorPane.setRightAnchor(employeeAttendanceDataContent, 0.0);
            AnchorPane.setBottomAnchor(employeeAttendanceDataContent, 0.0);

            //set 'data' into the center of dashboard.
            ancReturPaymentInvoice.getChildren().clear();
            ancReturPaymentInvoice.getChildren().add(employeeAttendanceDataContent);

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
                    setReturPaymentContent();
                }else{
                    setReturPaymentInvoiceContent();
                }
            }
        });
    }    
    
    public ReturPaymentLayoutController(FeatureReturController parentController) {
        this.parentController = parentController;
    }

    private final FeatureReturController parentController;

    public FReturManager getService() {
        return parentController.getFReturManager();
    }
    
}
