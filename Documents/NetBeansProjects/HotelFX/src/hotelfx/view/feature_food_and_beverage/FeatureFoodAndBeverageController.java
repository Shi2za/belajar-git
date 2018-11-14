/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_food_and_beverage;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FFoodAndBeverageManager;
import hotelfx.persistence.service.FFoodAndBeverageManagerImpl;
import hotelfx.view.feature_food_and_beverage.food_and_beverage.FoodAndBeverageController;
import hotelfx.view.feature_food_and_beverage.food_and_beverage_expired_date.FoodAndBeverageExpiredDateController;
import hotelfx.view.feature_food_and_beverage.food_and_beverage_stock.FoodAndBeverageStockController;
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
public class FeatureFoodAndBeverageController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            //loader data (path) & set controller
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "food_and_beverage":
                    loader.setLocation(HotelFX.class.getResource("view/feature_food_and_beverage/food_and_beverage/FoodAndBeverageView.fxml"));

                    FoodAndBeverageController foodAndBeverageController = new FoodAndBeverageController(this);
                    loader.setController(foodAndBeverageController);
                    break;

                case "food_and_beverage_stock":
                    loader.setLocation(HotelFX.class.getResource("view/feature_food_and_beverage/food_and_beverage_stock/FoodAndBeverageStockView.fxml"));

                    FoodAndBeverageStockController foodAndBeverageStockController = new FoodAndBeverageStockController(this);
                    loader.setController(foodAndBeverageStockController);
                    break;

                case "food_and_beverage_expired_date":
                    loader.setLocation(HotelFX.class.getResource("view/feature_food_and_beverage/food_and_beverage_expired_date/FoodAndBeverageExpiredDateView.fxml"));

                    FoodAndBeverageExpiredDateController foodAndBeverageExpiredDateController = new FoodAndBeverageExpiredDateController(this);
                    loader.setController(foodAndBeverageExpiredDateController);
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

            //set 'sub feature content' into the center of 'feature_food_and_beverage' contetnt.
            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }

    /**
     * Food And Beverage
     */
    @FXML
    private JFXButton btnShowFoodAndBeverage;

    @FXML
    private AnchorPane btnShowFoodAndBeverageLayout;

    /**
     * Food And Beverage
     */
    @FXML
    private JFXButton btnShowFoodAndBeverageStock;

    @FXML
    private AnchorPane btnShowFoodAndBeverageStockLayout;

    /**
     * Food And Beverage
     */
    @FXML
    private JFXButton btnShowFoodAndBeverageExpiredDate;

    @FXML
    private AnchorPane btnShowFoodAndBeverageExpiredDateLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowFoodAndBeverageLayout.getStyleClass().add("sub-feature-layout");
        btnShowFoodAndBeverageStockLayout.getStyleClass().add("sub-feature-layout");
        btnShowFoodAndBeverageExpiredDateLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false);

        btnShowFoodAndBeverage.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false);

                selectedSubFeature.set("food_and_beverage");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false);

                    selectedSubFeature.set("food_and_beverage");
                }
            }
        });

        btnShowFoodAndBeverageStock.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false);

                selectedSubFeature.set("food_and_beverage_stock");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false);

                    selectedSubFeature.set("food_and_beverage_stock");
                }
            }
        });

        btnShowFoodAndBeverageExpiredDate.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true);

                selectedSubFeature.set("food_and_beverage_expired_date");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true);

                    selectedSubFeature.set("food_and_beverage_expired_date");
                }
            }
        });

    }

    private void setButtonDataPseudoClass(boolean sfc, boolean fab, boolean stockFAB, boolean fabExpiredDate) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowFoodAndBeverageLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, fab);
        btnShowFoodAndBeverageStockLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, stockFAB);
        btnShowFoodAndBeverageExpiredDateLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, fabExpiredDate);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FFoodAndBeverageManager fFoodAndBeverageManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service manager
        fFoodAndBeverageManager = new FFoodAndBeverageManagerImpl();

        //set button event
        setButtonDataShow();

        //set selected sub feature
        selectedSubFeature.addListener((obs, oldValue, newValue) -> {
            //set sub feature content
            setSubFeatureContent(newValue);
        });
    }

    public FFoodAndBeverageManager getFFoodAndBeverageManager() {
        return this.fFoodAndBeverageManager;
    }

}
