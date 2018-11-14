/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_house_keeping;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FReportManager;
import hotelfx.persistence.service.FReportManagerImpl;
import hotelfx.view.feature_report_house_keeping.report_found_information.ReportFoundInformationController;
import hotelfx.view.feature_report_house_keeping.report_lost_information.ReportLostInformationController;
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
public class FeatureReportHouseKeepingController implements Initializable{
   private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "report_lost_information":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_house_keeping/report_lost_information/ReportLostInformationView.fxml"));
                    ReportLostInformationController reportLostInformationController = new ReportLostInformationController(this);
                    loader.setController(reportLostInformationController);
                    break;
                
                 case "report_found_information":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_house_keeping/report_found_information/ReportFoundInformationView.fxml"));
                    ReportFoundInformationController reportFoundInformationController = new ReportFoundInformationController(this);
                    loader.setController(reportFoundInformationController);
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
    private JFXButton btnShowReportLostInformation;

    @FXML
    private AnchorPane btnShowReportLostInformationLayout;
   
    @FXML
    private JFXButton btnShowReportFoundInformation;
    
    @FXML
    private AnchorPane btnShowReportFoundInformationLayout;
    
    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowReportLostInformationLayout.getStyleClass().add("sub-feature-layout");
        btnShowReportFoundInformationLayout.getStyleClass().add("sub-feature-layout");
      //  btnShowReportItemLayout.getStyleClass().add("sub-feature-layout");
        
        setButtonDataPseudoClass(false,false,false);
        
        btnShowReportLostInformation.setText("Laporan Barang \n Hilang");
        btnShowReportLostInformation.setOnMouseClicked((e) -> {
           if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,true,false);
             selectedSubFeature.set("report_lost_information");
           }
           else{
              Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult() == ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,true,false);
                 selectedSubFeature.set("report_lost_information");
               }
           }
         
         
         /* btnShowReportReservationLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          btnShowReportRevenueLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));*/
//            btnShowReportRoomIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        //    btnShowReportAdditionalIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        //    selectedSubFeature.set("report_reservation");
        });
        
         btnShowReportFoundInformation.setText("Laporan Temuan \n Barang");
          btnShowReportFoundInformation.setOnMouseClicked((e)->{
               if(!ClassSession.unSavingDataInput.get()){
                 setButtonDataPseudoClass(true,false,true);
                 selectedSubFeature.set("report_found_information");
               }
               else{
                   Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
                   if(alert.getResult() == ButtonType.OK){
                      ClassSession.unSavingDataInput.set(false);
                      setButtonDataPseudoClass(true,false,true);
                      selectedSubFeature.set("report_found_information");
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
    
    private void setButtonDataPseudoClass(boolean sfc, boolean reportLostInformation,boolean reportFoundInformation) {
      subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
      btnShowReportLostInformationLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportLostInformation);
      btnShowReportFoundInformationLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportFoundInformation);
     // btnShowReportRevenueLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportRevenue);
    }
    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FReportManager fReportManager;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */

   @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
    }    
    
    public FReportManager getFReportManager() {
        return this.fReportManager;
    }
}
