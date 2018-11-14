/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_bank.bank_network_card;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class TestAjaController implements Initializable {

    @FXML
    private AnchorPane ancDataLayout;

    private TreeView listMenu;

    public class DataHeaderDetail extends AnchorPane{
        
        public DataHeaderDetail(JFXButton a){
            getChildren().add(a);
        }
        
        public DataHeaderDetail(JFXTextArea a){
            getChildren().add(a);
        }
        
    }
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TreeItem<DataHeaderDetail> root = new TreeItem<>();
        
        JFXButton a1 = new JFXButton("a1");
        TreeItem<DataHeaderDetail> tiA1 = new TreeItem<>(new DataHeaderDetail(a1));
        root.getChildren().add(tiA1);
        
        tiA1.expandedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){
                for(TreeItem<DataHeaderDetail> data : root.getChildren()){
                    if(!data.equals(tiA1)){
                        data.setExpanded(false);
                    }
                }
            }
        });
        
        JFXTextArea a11 = new JFXTextArea("a11");
        a11.setPrefSize(100, 100);
        tiA1.getChildren().add(new TreeItem<>(new DataHeaderDetail(a11)));
        
        JFXTextArea a12 = new JFXTextArea("a12");
        a12.setPrefSize(100, 100);
        tiA1.getChildren().add(new TreeItem<>(new DataHeaderDetail(a12)));
        
        JFXTextArea a13 = new JFXTextArea("a13");
        a13.setPrefSize(100, 100);
        tiA1.getChildren().add(new TreeItem<>(new DataHeaderDetail(a13)));
        
        JFXButton a2 = new JFXButton("a2");
        TreeItem<DataHeaderDetail> tiA2 = new TreeItem<>(new DataHeaderDetail(a2));
        root.getChildren().add(tiA2);
        
        JFXTextArea a21 = new JFXTextArea("a21");
        a21.setPrefSize(100, 100);
        tiA2.getChildren().add(new TreeItem<>(new DataHeaderDetail(a21)));
        
        JFXTextArea a22 = new JFXTextArea("a22");
        a22.setPrefSize(100, 100);
        tiA2.getChildren().add(new TreeItem<>(new DataHeaderDetail(a22)));
        
        tiA2.expandedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){
                for(TreeItem<DataHeaderDetail> data : root.getChildren()){
                    if(!data.equals(tiA2)){
                        data.setExpanded(false);
                    }
                }
            }
        });
        
        listMenu = new TreeView(root);
        listMenu.setShowRoot(false);

//        listMenu.setPrefHeight(500);
//        listMenu.setMaxHeight(Double.MAX_VALUE);
//        listMenu.setPrefWidth(200);

//        listMenu.setCellFactory(tv -> {
//            TreeCell<NameItems> cell = new TreeCell<NameItems>() {
//
//                @Override
//                public void updateItem(NameItems item, boolean empty) {
//                    super.updateItem(item, empty);
//                    setText(null);
//                    setGraphic(null);
//                    setOnMouseClicked(null);
//
//                    if (!empty) {
//                        setText(item.getName());
//
//                        setOnMouseClicked((e) -> {
//                            if (e.getClickCount() == 2) {
//                                if (!feature.isFeatureParent(item.getRoleFeatureValue().getTblSystemFeature())) {
//                                    feature.setSelectedRoleFeature(feature.getFeatureParent(item.getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature()).getFeatureChild(item.getRoleFeatureValue().getTblSystemFeature().getIdfeature()).getRoleFeatureInstance());
//                                    setTitleFeature(item.getRoleFeatureValue().getTblSystemFeature().getFeatureName());
//                                    setDashboardContent(item.getRoleFeatureValue().getTblSystemFeature().getFxmlpath());
//                                    status.set(false);
//                                }
//                                /*if(item.getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature()!=null){
//                                 feature.setSelectedRoleFeature(feature.getFeatureParent(item.getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature()).getFeatureChild(item.getRoleFeatureValue().getTblSystemFeature().getIdfeature()).getRoleFeatureInstance());
//                                 setTitleFeature(item.getRoleFeatureValue().getTblSystemFeature().getFeatureName());
//                                 setDashboardContent(item.getRoleFeatureValue().getTblSystemFeature().getFxmlpath());
//                                 status.set(false);
//                                 }*/
//
//                                System.out.println(">>" + item.getRoleFeatureValue().getTblSystemFeature().getFeatureName());
//                            }
//                        });
//                    }
//
//                }
//
//            };
//
//            //((ActiveCellMenu)cell).activeProperty().set(false);
//            cell.treeItemProperty().addListener((obs, oldItem, newItem) -> {
//                cell.pseudoClassStateChanged(subMenuPseudoClass, newItem != null && newItem.getParent() != cell.getTreeView().getRoot());
//                cell.pseudoClassStateChanged(mainMenuPseudoClass, newItem != null && newItem.getParent() == cell.getTreeView().getRoot());
//            });
//            return cell;
//            //return null;
//        });

        ancDataLayout.getChildren().add(listMenu);
    }

}
