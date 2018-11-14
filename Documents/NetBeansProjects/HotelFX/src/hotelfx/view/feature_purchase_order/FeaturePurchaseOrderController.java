/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_purchase_order;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FPurchaseOrderManager;
import hotelfx.persistence.service.FPurchaseOrderManagerImpl;
import hotelfx.view.feature_purchase_order.purchase_order.PurchaseOrderController;
import hotelfx.view.feature_purchase_order.purchase_order_closing.PurchaseOrderClosingController;
import hotelfx.view.feature_purchase_order.purchase_order_payable_and_payment.PurchaseOrderPayableAndPaymentController;
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
public class FeaturePurchaseOrderController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "purchase_order":
                    loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order/PurchaseOrderView.fxml"));

                    PurchaseOrderController poController = new PurchaseOrderController(this);
                    loader.setController(poController);
                    break;
                case "purchase_order_closing":
                    loader.setLocation(HotelFX.class.getResource("view/feature_purchase_order/purchase_order_closing/PurchaseOrderClosingView.fxml"));

                    PurchaseOrderClosingController pocController = new PurchaseOrderClosingController(this);
                    loader.setController(pocController);
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

            //set 'sub feature content' into the center of 'feature_puchase_order' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Purchase Order
     */
    @FXML
    private JFXButton btnShowPO;

    @FXML
    private AnchorPane btnShowPOLayout;

    /**
     * Purchase Order Closing
     */
    @FXML
    private JFXButton btnShowPOClosing;

    @FXML
    private AnchorPane btnShowPOClosingLayout;
    
    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowPOLayout.getStyleClass().add("sub-feature-layout");
        btnShowPOClosingLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false);

        btnShowPO.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false);

                selectedSubFeature.set("purchase_order");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false);

                    selectedSubFeature.set("purchase_order");
                }
            }
        });

        btnShowPOClosing.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true);

                selectedSubFeature.set("purchase_order_closing");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true);

                    selectedSubFeature.set("purchase_order_closing");
                }
            }
        });
        
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean purchaseOrder, boolean purchaseOrderClosing) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowPOLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, purchaseOrder);
        btnShowPOClosingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, purchaseOrderClosing);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FPurchaseOrderManager fPurchaseOrderManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fPurchaseOrderManager = new FPurchaseOrderManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FPurchaseOrderManager getFPurchaseOrderManager() {
        return this.fPurchaseOrderManager;
    }

}
