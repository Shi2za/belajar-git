/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_report_front_office;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FReportManager;
import hotelfx.persistence.service.FReportManagerImpl;
import hotelfx.view.feature_report_front_office.report_additional_income.ReportAdditionalIncomeController;
import hotelfx.view.feature_report_front_office.report_checkin.ReportCheckInController;
import hotelfx.view.feature_report_front_office.report_checkout.ReportCheckOutController;
import hotelfx.view.feature_report_front_office.report_reservation.ReportReservationController;
import hotelfx.view.feature_report_front_office.report_revenue.ReportRevenueController;
import hotelfx.view.feature_report_front_office.report_room.ReportRoomController;
import hotelfx.view.feature_report_front_office.report_room_income.ReportRoomIncomeController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 *
 * @author Andreas
 */
public class FeatureReportFrontOfficeController implements Initializable{
     private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "report_reservation":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_front_office/report_reservation/ReportReservationView.fxml"));

                    ReportReservationController receivingController = new ReportReservationController(this);
                    loader.setController(receivingController);
                    break;
                
                case "report_revenue":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_front_office/report_revenue/ReportRevenueView.fxml"));
                    ReportRevenueController revenueController = new ReportRevenueController(this);
                    loader.setController(revenueController);
                    break;
                    
                case "report_checkin":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_front_office/report_checkin/ReportCheckInView.fxml"));
                    ReportCheckInController checkInController = new ReportCheckInController(this);
                    loader.setController(checkInController);
                    break;
                    
                case "report_checkout":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_front_office/report_checkout/ReportCheckOutView.fxml"));
                    ReportCheckOutController checkOutController = new ReportCheckOutController(this);
                    loader.setController(checkOutController);
                break;
                    
                case "report_room":
                    loader.setLocation(HotelFX.class.getResource("view/feature_report_front_office/report_room/ReportRoomView.fxml"));
                    ReportRoomController reportRoomController = new ReportRoomController(this);
                    loader.setController(reportRoomController);
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
     * Purchase Request
     */
    @FXML
    private JFXButton btnShowReportReservation;

    @FXML
    private AnchorPane btnShowReportReservationLayout;
    
    @FXML
    private JFXButton btnShowReportRoomIncome;

    @FXML
    private AnchorPane btnShowReportRoomIncomeLayout;
    
    @FXML
    private JFXButton btnShowReportAdditionalIncome;
    
    @FXML
    private AnchorPane btnShowReportAdditionalIncomeLayout;
    
    @FXML
    private JFXButton btnShowReportRevenue;
    
    @FXML
    private AnchorPane btnShowReportRevenueLayout;
    
    @FXML
    private JFXButton btnShowReportCheckIn;
    
    @FXML
    private AnchorPane btnShowReportCheckInLayout;
    
    @FXML
    private JFXButton btnShowReportCheckOut;
    
    @FXML
    private AnchorPane btnShowReportCheckOutLayout;
    
    @FXML
    private JFXButton btnShowReportRoom;
    
    @FXML
    private AnchorPane btnShowReportRoomLayout;
    
    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowReportReservationLayout.getStyleClass().add("sub-feature-layout");
        btnShowReportRevenueLayout.getStyleClass().add("sub-feature-layout");
        btnShowReportCheckInLayout.getStyleClass().add("sub-feature-layout");
        btnShowReportCheckOutLayout.getStyleClass().add("sub-feature-layout");
        btnShowReportRoomLayout.getStyleClass().add("sub-feature-layout");
        
        setButtonDataPseudoClass(false,false,false,false,false,false);
        
        btnShowReportReservation.setOnMouseClicked((e) -> {
           if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,true,false,false,false,false);
             selectedSubFeature.set("report_reservation");
           }
           else{
              Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult() == ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,true,false,false,false,false);
                 selectedSubFeature.set("report_reservation");
               }
           }
        
         /* btnShowReportReservationLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          btnShowReportRevenueLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));*/
//            btnShowReportRoomIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        //    btnShowReportAdditionalIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        //    selectedSubFeature.set("report_reservation");
        });
        
        btnShowReportRevenue.setOnMouseClicked((e) -> {
            if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,false,true,false,false,false);
             selectedSubFeature.set("report_revenue");
           }
           else{
              Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult() == ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,false,true,false,false,false);
                 selectedSubFeature.set("report_revenue");
               }
           }
         /*   btnShowReportReservationLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            btnShowReportRevenueLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
         // btnShowReportAdditionalIncomeLayout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            selectedSubFeature.set("report_revenue"); */
        });
        
        btnShowReportCheckIn.setOnMouseClicked((e)->{
           if(!ClassSession.unSavingDataInput.get()){
             setButtonDataPseudoClass(true,false,false,true,false,false);
             selectedSubFeature.set("report_checkin");
           }
           else{
             Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
             if(alert.getResult()==ButtonType.OK){
               ClassSession.unSavingDataInput.set(false);
               setButtonDataPseudoClass(true,false,false,true,false,false);
               selectedSubFeature.set("report_checkin");
             }
           }
        });
        
        btnShowReportCheckOut.setOnMouseClicked((e)->{
           if(!ClassSession.unSavingDataInput.get()){
              setButtonDataPseudoClass(true,false,false,false,true,false);
              selectedSubFeature.set("report_checkout");
           }
           else{
              Alert alert  = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
              if(alert.getResult()==ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,false,false,false,true,false);
                 selectedSubFeature.set("report_checkout");
              }
           }
        });
        
        btnShowReportRoom.setText("Laporan Tingkat \nHunian Kamar");
        btnShowReportRoom.setOnMouseClicked((e)->{
           if(!ClassSession.unSavingDataInput.get()){
              setButtonDataPseudoClass(true,false,false,false,false,true);
              selectedSubFeature.set("report_room");
           }
           else{
             Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage(null, null);
               if(alert.getResult()==ButtonType.OK){
                 ClassSession.unSavingDataInput.set(false);
                 setButtonDataPseudoClass(true,false,false,false,false,true);
                 selectedSubFeature.set("report_room");
               }
           }
        });
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
    
    private void setButtonDataPseudoClass(boolean sfc, boolean reportReservation,boolean reportRevenue,boolean reportCheckIn,boolean reportCheckOut,boolean reportRoom) {
      subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
      btnShowReportReservationLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportReservation);
      btnShowReportRevenueLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportRevenue);
      btnShowReportCheckInLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportCheckIn);
      btnShowReportCheckOutLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportCheckOut);
      btnShowReportRoomLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass,reportRoom);
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
