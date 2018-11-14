/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_purchasing;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.ButtonType;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FReportManager;
import hotelfx.persistence.service.FReportManagerImpl;
import hotelfx.view.feature_report_purchasing.report_purchasing.ReportPurchasingController;
import hotelfx.view.feature_report_purchasing.report_receiving.ReportReceivingController;
import hotelfx.view.feature_report_purchasing.report_receiving_po.ReportReceivingPOController;
import hotelfx.view.feature_report_purchasing.report_retur.ReportReturBarangController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Andreas
 */
public class FeatureReportPurchasingController implements Initializable {
   private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "report_purchasing":
                    System.out.println("yyy");
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_purchasing/report_purchasing/ReportPurchasingView.fxml"));

                    ReportPurchasingController reportPurchasingController = new ReportPurchasingController(this);
                    loader.setController(reportPurchasingController);
                break;
                
                case "report_receiving":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_purchasing/report_receiving/ReportReceivingView.fxml"));
                    
                    ReportReceivingController reportReceivingController = new ReportReceivingController(this);
                    loader.setController(reportReceivingController);
                break;
                
                 case "report_receiving_po":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_purchasing/report_receiving_po/ReportReceivingPOView.fxml"));
                    
                    ReportReceivingPOController reportReceivingPOController = new ReportReceivingPOController(this);
                    loader.setController(reportReceivingPOController);
                break;
                     
                case "report_retur":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_purchasing/report_retur/ReportReturBarangView.fxml"));
                    ReportReturBarangController reportReturController = new ReportReturBarangController(this);
                    loader.setController(reportReturController);
                break;
               /*   case "report_room_income":
                    System.out.println("masuk!!");
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_front_office/report_room_income/ReportRoomIncomeView.fxml"));

                    ReportRoomIncomeController reportRoomIncomeController = new ReportRoomIncomeController(this);
                    loader.setController(reportRoomIncomeController);
                    break;
                  
                  case "report_additional_income":
                      loader.setLocation(HotelFX.class.getResource("view/feature_report_front_office/report_additional_income/ReportAdditionalIncomeView.fxml"));
                      ReportAdditionalIncomeController reportAdditionalIncomeController = new ReportAdditionalIncomeController(this);
                      loader.setController(reportAdditionalIncomeController);
                  break; */
                      
                default:
                    loader.setLocation(HotelFX.class.getResource(""));
                    break;
            }

            //load loader to node
            Node subContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);

            //set 'sub feature content' into the center of 'feature_puchase_request' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Report Warehouse
     */
    @FXML
    private JFXButton btnShowReportPurchasing;

    @FXML
    private AnchorPane btnShowReportPurchasingLayout;
   
    @FXML
    private JFXButton btnShowReportReceiving;
    @FXML
    private AnchorPane btnShowReportReceivingLayout;
    
    @FXML
    private JFXButton btnShowReportReceivingPO;
    @FXML
    private AnchorPane btnShowReportReceivingPOLayout;
    
    @FXML
    private JFXButton btnShowReportRetur;
    @FXML
    private AnchorPane btnShowReportReturLayout;
    
    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowReportPurchasingLayout.getStyleClass().add("sub-feature-layout");
        btnShowReportReceivingPOLayout.getStyleClass().add("sub-feature-layout");
        btnShowReportReceivingLayout.getStyleClass().add("sub-feature-layout");
        btnShowReportReturLayout.getStyleClass().add("sub-feature-layout");
        
        setButtonDataPseudoClass(false,false,false,false,false);
        
        btnShowReportPurchasing.setText("Laporan Pembelian \n Barang");
        btnShowReportPurchasing.setOnMouseClicked((e) -> {
           if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,true,false,false,false);
             selectedSubFeature.set("report_purchasing");
           }
           else{
              Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult()== ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,true,false,false,false);
                 selectedSubFeature.set("report_purchasing");
               }
           }
         
          
         /* btnShowReportReservationLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          btnShowReportRevenueLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));*/
//            btnShowReportRoomIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        //    btnShowReportAdditionalIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        //    selectedSubFeature.set("report_reservation");
        });
        
        btnShowReportReceivingPO.setText("Laporan Penerimaan \n PO");
        btnShowReportReceivingPO.setOnMouseClicked((e)->{
           if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,false,true,false,false);
             selectedSubFeature.set("report_receiving_po");
           }
           else{
              Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult()== ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,false,true,false,false);
                 selectedSubFeature.set("report_receiving_po");
               }
           } 
        });
         
        btnShowReportReceiving.setText("Laporan Terima \n Barang");
        btnShowReportReceiving.setOnMouseClicked((e) -> {
           if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,false,false,true,false);
             selectedSubFeature.set("report_receiving");
           }
           else{
              Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult()== ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,false,false,true,false);
                 selectedSubFeature.set("report_receiving");
               }
           }
        });
        
        btnShowReportRetur.setOnMouseClicked((e)->{
           if(!ClassSession.unSavingDataInput.get()){
              setButtonDataPseudoClass(true,false,false,false,true);
              selectedSubFeature.set("report_retur");
           }
           else{
             Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult()==ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,false,false,false,true);
                 selectedSubFeature.set("report_retur");
               }
           }
        });
        
    /*    btnShowReportRevenue.setOnMouseClicked((e) -> {
            if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,false,true);
             selectedSubFeature.set("report_revenue");
           }
           else{
              Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult() == ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,false,true);
                 selectedSubFeature.set("report_revenue");
               }
           }
         /*   btnShowReportReservationLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            btnShowReportRevenueLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
         // btnShowReportAdditionalIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            selectedSubFeature.set("report_revenue"); */
     //   }); 
     /* btnShowReportRoomIncome.setText("Laporan Penjualan\n Kamar");
        btnShowReportRoomIncome.setOnMouseClicked((e)->{
            btnShowReportReservationLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            btnShowReportRoomIncomeLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            btnShowReportAdditionalIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            selectedSubFeature.set("report_room_income");
        });
        
        btnShowReportAdditionalIncome.setText("Laporan Penjualan \nLayanan & Barang");
        btnShowReportAdditionalIncome.setOnMouseClicked((e)->{
            btnShowReportReservationLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            btnShowReportRoomIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            btnShowReportAdditionalIncomeLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            selectedSubFeature.set("report_additional_income");
        }); */
    }
    
    private void setButtonDataPseudoClass(boolean sfc, boolean reportPurchasing,boolean reportReceivingPO,boolean reportReceiving,boolean reportRetur) {
      subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
      btnShowReportPurchasingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportPurchasing);
      btnShowReportReceivingPOLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportReceivingPO);
      btnShowReportReceivingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportReceiving);
      btnShowReportReturLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportRetur);
     // btnShowReportRevenueLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportRevenue);
    }
    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FReportManager fReportManager;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        System.out.println("aaaaa");
         //set service manager
        fReportManager = new FReportManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     
    public FReportManager getFReportManager() {
        return this.fReportManager;
    }
}
