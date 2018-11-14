/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_finance;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FReportManager;
import hotelfx.persistence.service.FReportManagerImpl;
import hotelfx.view.feature_report_finance.report_finance_transaction.ReportFinanceTransactionController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class FeatureReportFinanceController implements Initializable{
    
    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
       try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
               case "report_finance_transaction":
                 loader.setLocation(HotelFX.class.getResource("view/feature_report_finance/report_finance_transaction/ReportFinanceTransactionView.fxml"));
                 ReportFinanceTransactionController reportFinanceTransactionController = new ReportFinanceTransactionController(this);
                 loader.setController(reportFinanceTransactionController);
               break;
            }
            
            Node subContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);

            //set 'sub feature content' into the center of 'feature_puchase_request' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);
        }
       catch(Exception e){
          System.out.println("err >> " + e.getMessage());
       }
    }
    
    @FXML
    private JFXButton btnShowReportFinanceTransaction;
    @FXML
    private AnchorPane btnShowReportFinanceTransactionLayout;
    
    private void setButtonDataShow() {
     subFeatureContent.getStyleClass().add("sub-feature-layout");
     btnShowReportFinanceTransactionLayout.getStyleClass().add("sub-feature-layout");
    // btnShowReportRevenueLayout.getStyleClass().add("sub-feature-layout");
        
        setButtonDataPseudoClass(false,false);
        
        btnShowReportFinanceTransaction.setText("Laporan \nPengeluaran");
        btnShowReportFinanceTransaction.setOnMouseClicked((e) -> {
           if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,true);
             selectedSubFeature.set("report_finance_transaction");
           }
           else{
             Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult() == ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,false);
                 selectedSubFeature.set("report_finance_transaction");
               }
           }
        });
    }
    
     private void setButtonDataPseudoClass(boolean sfc, boolean reportFinanceTransaction) {
      subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
      btnShowReportFinanceTransactionLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportFinanceTransaction);
     // btnShowReportRevenueLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportRevenue);
    }
     
    private FReportManager fReportManager;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      fReportManager = new FReportManagerImpl();
      setButtonDataShow();
      
      selectedSubFeature.addListener((obs,oldVal,newVal)->{
         setSubFeatureContent(newVal);
      });
      //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public FReportManager getFReportManager(){
       return this.fReportManager;
    }
    
}
