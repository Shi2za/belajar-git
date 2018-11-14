/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_room_rate;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FRoomRateManager;
import hotelfx.persistence.service.FRoomRateManagerImpl;
import hotelfx.view.feature_room_rate.reservation_bar.ReservationBARController;
import hotelfx.view.feature_room_rate.reservation_season.ReservationSeasonController;
import hotelfx.view.feature_room_rate.setting_reservation_bar.SettingReservationBARController;
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
 * FXML Controller class
 *
 * @author ANDRI
 */
public class FeatureRoomRateController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "reservation_bar":
                    loader.setLocation(HotelFX.class.getResource("view/feature_room_rate/reservation_bar/ReservationBARView.fxml"));

                    ReservationBARController reservationBARController = new ReservationBARController(this);
                    loader.setController(reservationBARController);
                    break;
                case "reservation_season":
                    loader.setLocation(HotelFX.class.getResource("view/feature_room_rate/reservation_season/ReservationSeasonView.fxml"));

                    ReservationSeasonController reservationSeasonController = new ReservationSeasonController(this);
                    loader.setController(reservationSeasonController);
                    break;
                case "reservation_setting_bar":
                    loader.setLocation(HotelFX.class.getResource("view/feature_room_rate/setting_reservation_bar/SettingReservationBARView.fxml"));

                    SettingReservationBARController settingReservationBARController = new SettingReservationBARController(this);
                    loader.setController(settingReservationBARController);
                    break;
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

            //set 'sub feature content' into the center of 'feature_room_rate' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Reservation BAR
     */
    @FXML
    private JFXButton btnShowReservationBAR;

    @FXML
    private AnchorPane btnShowReservationBARLayout;

    /**
     * Reservation Season
     */
    @FXML
    private JFXButton btnShowReservationSeason;

    @FXML
    private AnchorPane btnShowReservationSeasonLayout;

    /**
     * Reservation Setting BAR
     */
    @FXML
    private JFXButton btnShowReservationSettingBAR;

    @FXML
    private AnchorPane btnShowReservationSettingBARLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowReservationBARLayout.getStyleClass().add("sub-feature-layout");
        btnShowReservationSeasonLayout.getStyleClass().add("sub-feature-layout");
        btnShowReservationSettingBARLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false);

        btnShowReservationBAR.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false);

                selectedSubFeature.set("reservation_bar");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false);

                    selectedSubFeature.set("reservation_bar");
                }
            }
        });

        btnShowReservationSeason.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false);

                selectedSubFeature.set("reservation_season");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false);

                    selectedSubFeature.set("reservation_season");
                }
            }
        });

        btnShowReservationSettingBAR.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true);

                selectedSubFeature.set("reservation_setting_bar");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true);

                    selectedSubFeature.set("reservation_setting_bar");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean bar, boolean season, boolean settingBAR) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowReservationBARLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, bar);
        btnShowReservationSeasonLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, season);
        btnShowReservationSettingBARLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, settingBAR);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FRoomRateManager fRoomRateManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fRoomRateManager = new FRoomRateManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public void setFRoomRateManager(FRoomRateManager fRoomRateManager) {
        this.fRoomRateManager = fRoomRateManager;
    }

    public FRoomRateManager getFRoomRateManager() {
        return this.fRoomRateManager;
    }

}
