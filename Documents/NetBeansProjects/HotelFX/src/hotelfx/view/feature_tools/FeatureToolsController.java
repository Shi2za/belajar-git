/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_tools;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassPseudoClassCSS;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.service.FToolsManager;
import hotelfx.persistence.service.FToolsManagerImpl;
import hotelfx.view.feature_tools.tools.ToolsController;
import hotelfx.view.feature_tools.tools_stock.ToolsStockController;
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
public class FeatureToolsController implements Initializable {

    private final StringProperty selectedSubFeature = new SimpleStringProperty("");

    @FXML
    private AnchorPane subFeatureContent;

    private void setSubFeatureContent(String subFeature) {
        try {
            FXMLLoader loader = new FXMLLoader();
            switch (subFeature) {
                case "tools":
                    loader.setLocation(HotelFX.class.getResource("view/feature_tools/tools/ToolsView.fxml"));
                    ToolsController toolsController = new ToolsController(this);
                    loader.setController(toolsController);
                    break;

                case "tools_stock":
                    loader.setLocation(HotelFX.class.getResource("view/feature_tools/tools_stock/ToolsStockView.fxml"));
                    ToolsStockController toolsStockController = new ToolsStockController(this);
                    loader.setController(toolsStockController);
                    break;
                default:
                    loader.setLocation(HotelFX.class.getResource(""));
                    break;
            }
            Node subContent = loader.load();

            AnchorPane.setTopAnchor(subContent, 0.0);
            AnchorPane.setBottomAnchor(subContent, 0.0);
            AnchorPane.setLeftAnchor(subContent, 0.0);
            AnchorPane.setRightAnchor(subContent, 0.0);

            subFeatureContent.getChildren().clear();
            subFeatureContent.getChildren().add(subContent);

        } catch (IOException ex) {
            Logger.getLogger(FeatureToolsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private JFXButton btnShowTools;

    @FXML
    private AnchorPane btnShowToolsLayout;

    @FXML
    private JFXButton btnShowStockTools;

    @FXML
    private AnchorPane btnShowStockToolsLayout;

    private void setButtonDataShow() {
        subFeatureContent.getStyleClass().add("sub-feature-layout");
        btnShowToolsLayout.getStyleClass().add("sub-feature-layout");
        btnShowStockToolsLayout.getStyleClass().add("sub-feature-layout");

        setButtonDataPseudoClass(false, false, false);

        btnShowTools.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, true, false);

                selectedSubFeature.set("tools");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, true, false);

                    selectedSubFeature.set("tools");
                }
            }
        });

        btnShowStockTools.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                setButtonDataPseudoClass(true, false, true);

                selectedSubFeature.set("tools_stock");
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);

                    setButtonDataPseudoClass(true, false, true);

                    selectedSubFeature.set("tools_stock");
                }
            }
        });
    }

    private void setButtonDataPseudoClass(boolean sfc, boolean tools, boolean toolsStock) {
        subFeatureContent.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, sfc);
        btnShowToolsLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, tools);
                    btnShowStockToolsLayout.pseudoClassStateChanged(ClassPseudoClassCSS.selectedSubFeaturePseudoClass, toolsStock);
    }
    
    private FToolsManager fToolsManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fToolsManager = new FToolsManagerImpl();

        setButtonDataShow();

        selectedSubFeature.addListener((obs, oldVal, newVal) -> {
            //set sub feature content
            setSubFeatureContent(newVal);
        });

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public FToolsManager getFToolsManager() {
        return this.fToolsManager;
    }

}
