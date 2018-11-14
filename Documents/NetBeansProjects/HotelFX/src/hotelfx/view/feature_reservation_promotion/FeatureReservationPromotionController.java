/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation_promotion;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FReservationPromotionManager;
import hotelfx.persistence.service.FReservationPromotionManagerImpl;
import hotelfx.view.feature_reservation_promotion.card_event.CardEventController;
import hotelfx.view.feature_reservation_promotion.discountable_setting.DiscountableSettingController;
import hotelfx.view.feature_reservation_promotion.hotel_event.HotelEventController;
import hotelfx.view.feature_reservation_promotion.reservation_voucher.ReservationVoucherController;
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
public class FeatureReservationPromotionController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "voucher":
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation_promotion/reservation_voucher/ReservationVoucherView.fxml"));

                    ReservationVoucherController reservationVoucherController = new ReservationVoucherController(this);
                    loader.setController(reservationVoucherController);
                    break;
                case "hotel_event":
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation_promotion/hotel_event/HotelEventView.fxml"));

                    HotelEventController hotelEventController = new HotelEventController(this);
                    loader.setController(hotelEventController);
                    break;
                case "card_event":
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation_promotion/card_event/CardEventView.fxml"));

                    CardEventController cardEventController = new CardEventController(this);
                    loader.setController(cardEventController);
                    break;
                case "discountable_setting":
                    loader.setLocation(HotelFX.class.getResource("view/feature_reservation_promotion/discountable_setting/DiscountableSettingView.fxml"));

                    DiscountableSettingController discountableSettingController = new DiscountableSettingController(this);
                    loader.setController(discountableSettingController);
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

            //set 'sub feature content' into the center of 'feature_reservation_promotion' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * ReservationPromotion (Voucher)
     */
    @FXML
    private JFXButton btnShowReservationPromotion;

    @FXML
    private AnchorPane btnShowReservationPromotionLayout;

    /**
     * Hotel Event
     */
    @FXML
    private JFXButton btnShowHotelEvent;

    @FXML
    private AnchorPane btnShowHotelEventLayout;

    /**
     * Card Event
     */
    @FXML
    private JFXButton btnShowCardEvent;

    @FXML
    private AnchorPane btnShowCardEventLayout;

    /**
     * Discount-able Setting
     */
    @FXML
    private JFXButton btnShowDiscountableSetting;

    @FXML
    private AnchorPane btnShowDiscountableSettingLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowReservationPromotionLayout.getStyleClass().add("sub-feature-layout");
        btnShowHotelEventLayout.getStyleClass().add("sub-feature-layout");
        btnShowCardEventLayout.getStyleClass().add("sub-feature-layout");
        btnShowDiscountableSettingLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false, false);

        btnShowReservationPromotion.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false, false);

                selectedSubFeature.set("voucher");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false, false);

                    selectedSubFeature.set("voucher");
                }
            }
        });

        btnShowHotelEvent.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false, false);

                selectedSubFeature.set("hotel_event");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false, false);

                    selectedSubFeature.set("hotel_event");
                }
            }
        });

        btnShowCardEvent.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true, false);

                selectedSubFeature.set("card_event");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true, false);

                    selectedSubFeature.set("card_event");                    
                }
            }
        });

        btnShowDiscountableSetting.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, true);

                selectedSubFeature.set("discountable_setting");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, true);

                    selectedSubFeature.set("discountable_setting");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean voucher, boolean hotelEvent, boolean cardEvent, boolean discountableSetting) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowReservationPromotionLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, voucher);
        btnShowHotelEventLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, hotelEvent);
        btnShowCardEventLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, cardEvent);
        btnShowDiscountableSettingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, discountableSetting);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FReservationPromotionManager fReservationPromotionManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fReservationPromotionManager = new FReservationPromotionManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FReservationPromotionManager getFReservationPromotionManager() {
        return this.fReservationPromotionManager;
    }

}
