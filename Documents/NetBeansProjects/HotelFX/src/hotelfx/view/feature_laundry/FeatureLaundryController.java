/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_laundry;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FLaundryManager;
import hotelfx.persistence.service.FLaundryManagerImpl;
import hotelfx.persistence.service.FStockOpnameManager;
import hotelfx.persistence.service.FStockOpnameManagerImpl;
import hotelfx.view.feature_laundry.laundry_in_coming.LaundryInComingController;
import hotelfx.view.feature_laundry.laundry_mutation_history.LaundryMutationHistoryController;
import hotelfx.view.feature_laundry.laundry_out_going.LaundryOutGoingController;
import hotelfx.view.feature_laundry.laundry_stock_opname.LaundryStockOpnameController;
import hotelfx.view.feature_laundry.laundry_transfer_item.LaundryTransferItemController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FeatureLaundryController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "Laundry_Transfer_Item":
                    loader.setLocation(HotelFX.class.getResource("view/feature_laundry/laundry_transfer_item/LaundryTransferItemView.fxml"));
                    LaundryTransferItemController laundryTransferItemController = new LaundryTransferItemController(this);
                    loader.setController(laundryTransferItemController);
                    break;

                case "in_coming":
                    loader.setLocation(HotelFX.class.getResource("view/feature_laundry/laundry_in_coming/LaundryInComingView.fxml"));

                    LaundryInComingController laundryInComingController = new LaundryInComingController(this);
                    loader.setController(laundryInComingController);
                    break;

                case "out_going":
                    loader.setLocation(HotelFX.class.getResource("view/feature_laundry/laundry_out_going/LaundryOutGoingView.fxml"));

                    LaundryOutGoingController laundryOutGoingController = new LaundryOutGoingController(this);
                    loader.setController(laundryOutGoingController);
                    break;

                case "stock_opname":
                    loader.setLocation(HotelFX.class.getResource("view/feature_laundry/laundry_stock_opname/LaundryStockOpnameView.fxml"));

                    LaundryStockOpnameController laundryStockOpnameController = new LaundryStockOpnameController(this);
                    loader.setController(laundryStockOpnameController);
                    break;

                case "Laundry_Mutation_History":
                    loader.setLocation(HotelFX.class.getResource("view/feature_laundry/laundry_mutation_history/LaundryMutationHistoryView.fxml"));
                    LaundryMutationHistoryController laundryMutationHistoryController = new LaundryMutationHistoryController(this);
                    loader.setController(laundryMutationHistoryController);
                    break;

                default:
                    loader.setLocation(HotelFX.class.getResource(""));
                    break;
            }

            Node subContent = loader.load();

            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);

            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);
        } catch (IOException ex) {
            Logger.getLogger(FeatureLaundryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private JFXButton btnShowLaundryTransferItem;

    @FXML
    private AnchorPane btnShowLaundryTransferItemLayout;

    @FXML
    private JFXButton btnShowLaundryInComing;

    @FXML
    private AnchorPane btnShowLaundryInComingLayout;

    @FXML
    private JFXButton btnShowLaundryOutGoing;

    @FXML
    private AnchorPane btnShowLaundryOutGoingLayout;

    @FXML
    private JFXButton btnShowLaundryStockOpname;

    @FXML
    private AnchorPane btnShowLaundryStockOpnameLayout;

    @FXML
    private JFXButton btnShowLaundryMutationHistory;

    @FXML
    private AnchorPane btnShowLaundryMutationHistoryLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowLaundryTransferItemLayout.getStyleClass().add("sub-feature-layout");
        btnShowLaundryInComingLayout.getStyleClass().add("sub-feature-layout");
        btnShowLaundryOutGoingLayout.getStyleClass().add("sub-feature-layout");
        btnShowLaundryStockOpnameLayout.getStyleClass().add("sub-feature-layout");
        btnShowLaundryMutationHistoryLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false, false, false, false);

        btnShowLaundryTransferItem.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false, false, false, false);

                selectedSubFeature.set("Laundry_Transfer_Item");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false, false, false, false);

                    selectedSubFeature.set("Laundry_Transfer_Item");
                }
            }
        });

        btnShowLaundryInComing.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true, false, false, false);

                selectedSubFeature.set("in_coming");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true, false, false, false);

                    selectedSubFeature.set("in_coming");
                }
            }
        });

        btnShowLaundryOutGoing.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, true, false, false);

                selectedSubFeature.set("out_going");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, true, false, false);

                    selectedSubFeature.set("out_going");
                }
            }
        });

        btnShowLaundryStockOpname.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, true, false);

                selectedSubFeature.set("stock_opname");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, true, false);

                    selectedSubFeature.set("stock_opname");
                }
            }
        });

        btnShowLaundryMutationHistory.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, false, false, false, true);

                selectedSubFeature.set("Laundry_Mutation_History");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, false, false, false, true);

                    selectedSubFeature.set("Laundry_Mutation_History");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean laundryTransferItem, boolean laundryInComing, boolean outGoing, boolean stockOpname, boolean laundryMutationHistory) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowLaundryTransferItemLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, laundryTransferItem);
        btnShowLaundryInComingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, laundryInComing);
        btnShowLaundryOutGoingLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, outGoing);
        btnShowLaundryStockOpnameLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, stockOpname);
        btnShowLaundryMutationHistoryLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, laundryMutationHistory);
    }

    /**
     * SERVICE (FOR ACCESS TO DATABASE)
     */
    private FLaundryManager fLaundryManager;

    private FStockOpnameManager fStockOpnameManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fLaundryManager = new FLaundryManagerImpl();
        fStockOpnameManager = new FStockOpnameManagerImpl();

        setButtonDataShow();

        selectedSubFeature.addListener((obs, oldVal, newVal) -> {
            //set sub feature content
            setSubFeatureContent(newVal);
        });
    }

    public FLaundryManager getFLaundryManager() {
        return this.fLaundryManager;
    }

    public FStockOpnameManager getFStockOpnameManager() {
        return this.fStockOpnameManager;
    }

}
