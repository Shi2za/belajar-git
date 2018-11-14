/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.test;

import java.awt.Color;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class TableDetailTestController implements Initializable {

    @FXML
    private ScrollPane spnTest;

    private void createTree() {
        TreeItem<ItemTest> root = new TreeItem<>();

        TreeItem<ItemTest> itemParent1 = new TreeItem(new ItemTest(true, createThisLayoutAsParent()));
        TreeItem<ItemTest> itemChild1 = new TreeItem(new ItemTest(false, createThisLayoutAsChild()));
        itemParent1.getChildren().add(itemChild1);

        TreeItem<ItemTest> itemParent2 = new TreeItem(new ItemTest(true, createThisLayoutAsParent()));
        TreeItem<ItemTest> itemChild2 = new TreeItem(new ItemTest(false, createThisLayoutAsChild()));
        itemParent2.getChildren().add(itemChild2);

        root.getChildren().add(itemParent1);
        root.getChildren().add(itemParent2);

        TreeView listMenu = new TreeView(root);
        listMenu.setShowRoot(false);

        spnTest.setContent(listMenu);
        spnTest.setFitToWidth(true);
        spnTest.setFitToHeight(true);
    }

    private AnchorPane createThisLayoutAsParent() {
        AnchorPane content = new AnchorPane();
        content.setPrefSize(USE_COMPUTED_SIZE, 30);
        
        GridPane gp = new GridPane();
        gp.getRowConstraints().add(new RowConstraints(30));
        
        gp.getColumnConstraints().add(new ColumnConstraints(140));
        gp.add(new Label("0001"), 0, 0);
        
        gp.getColumnConstraints().add(new ColumnConstraints(140));
        gp.add(new Label("Steven"), 1, 0);
        
        gp.getColumnConstraints().add(new ColumnConstraints(140));
        gp.add(new Label("CheckOut"), 2, 0);
        
        gp.getColumnConstraints().add(new ColumnConstraints(USE_COMPUTED_SIZE));
        
        AnchorPane.setBottomAnchor(gp, 0.0);
        AnchorPane.setLeftAnchor(gp, 0.0);
        AnchorPane.setRightAnchor(gp, 0.0);
        AnchorPane.setTopAnchor(gp, 0.0);

        content.getChildren().add(gp);
        
        return content;
    }

    private AnchorPane createThisLayoutAsChild() {
        AnchorPane content = new AnchorPane();
        content.setPrefSize(USE_COMPUTED_SIZE, 200);
        
        AnchorPane contentData = new AnchorPane();

        contentData.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        AnchorPane.setBottomAnchor(contentData, 0.0);
        AnchorPane.setLeftAnchor(contentData, 0.0);
        AnchorPane.setRightAnchor(contentData, 30.0);
        AnchorPane.setTopAnchor(contentData, 0.0);
        
        content.getChildren().add(contentData);
        
        return content;
    }

    public class ItemTest extends AnchorPane {

        private final boolean isParent;

        private final AnchorPane content;

        public ItemTest(boolean isParent, AnchorPane content) {
            this.isParent = isParent;
            this.content = content;

            setContent();
        }

        private void setContent() {
            setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            AnchorPane.setTopAnchor(content, 0.0);

            getChildren().add(content);
        }

        public boolean getIsParent() {
            return isParent;
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
        createTree();
    }

}
