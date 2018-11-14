/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import com.jfoenix.controls.JFXTextField;
import com.lowagie.text.Anchor;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

/**
 *
 * @author ANDRI
 * @param <T>
 */
public class ClassTableWithDetail<T> extends AnchorPane {

    private final TableView<T> tableView;

    private final BorderPane tableWithDetailLayout;

    private AnchorPane titleTable;

    private AnchorPane contentTable;

    private TableView<T> tempTableView;

    private final ScrollPane scpContentTable = new ScrollPane();

    public ClassTableWithDetail(TableView tableView) {
        super();

        //init table view
        this.tableView = tableView;

        //init title table
        createTitleTable();

        //init content table
        createContentTable();

        //set to layout
        if(contentTable.getWidth() > titleTable.getWidth()){
            titleTable.setMinWidth(contentTable.getWidth());
            titleTable.setMaxWidth(contentTable.getWidth());
        }else{
            contentTable.setMinWidth(titleTable.getWidth());
            contentTable.setMaxWidth(titleTable.getWidth());
        }
        
        tableWithDetailLayout = new BorderPane();

        tableWithDetailLayout.setCenter(contentTable);
        tableWithDetailLayout.setTop(titleTable);

        titleTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.doubleValue() > contentTable.getWidth()){
                contentTable.setMinWidth(newVal.doubleValue());
                contentTable.setMaxWidth(newVal.doubleValue());
            }
            ScrollBar hScrollBar = findScrollBar(tableView, Orientation.HORIZONTAL);
            if (hScrollBar != null) {
                hScrollBar.valueProperty().bind(scpContentTable.hvalueProperty());
                System.out.println("?????????");
            } else {
                System.out.println("NULL");
            }
            System.out.println("- he : " + titleTable.getWidth());
            System.out.println("- co : " + contentTable.getWidth());
        });
        contentTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.doubleValue() > titleTable.getWidth()){
                titleTable.setMinWidth(newVal.doubleValue());
                titleTable.setMaxWidth(newVal.doubleValue());
            }
            System.out.println("-- he : " + titleTable.getWidth());
            System.out.println("-- co : " + contentTable.getWidth());
        });
        
        AnchorPane.setBottomAnchor(tableWithDetailLayout, 0.0);
        AnchorPane.setLeftAnchor(tableWithDetailLayout, 0.0);
        AnchorPane.setRightAnchor(tableWithDetailLayout, 0.0);
        AnchorPane.setTopAnchor(tableWithDetailLayout, 0.0);
        getChildren().add(tableWithDetailLayout);
    }

    private void createTitleTable() {
        titleTable = new AnchorPane();

        if (tableView != null) {
            tempTableView = new TableView();

            double columnMaxHeight = 0;
            double tempColumnMaxHeight = 0;

            for (int i = 0; i < tableView.getColumns().size(); i++) {
                tempColumnMaxHeight = 20;
                TableColumn<T, ?> tempTableColumn = new TableColumn(((TableColumn<T, ?>) tableView.getColumns().get(i)).getText());
                tempTableColumn.setMinWidth(((TableColumn<T, ?>) tableView.getColumns().get(i)).getMinWidth());
                tempTableColumn.setMaxWidth(((TableColumn<T, ?>) tableView.getColumns().get(i)).getMinWidth());
                createInheritanceTableColomnHeader((TableColumn<T, ?>) tempTableColumn, (TableColumn<T, ?>) tableView.getColumns().get(i), tempColumnMaxHeight);
                tempTableView.getColumns().add(tempTableColumn);
                if (tempColumnMaxHeight > columnMaxHeight) {
                    columnMaxHeight = tempColumnMaxHeight;
                }
            }
            tempTableView.setItems(FXCollections.observableArrayList());

            AnchorPane placeHolder = new AnchorPane();
            placeHolder.setPrefHeight(0.0);
            tempTableView.setPlaceholder(placeHolder);

            tempTableView.setPrefHeight(columnMaxHeight);

            AnchorPane.setLeftAnchor(tempTableView, 0.0);
            AnchorPane.setRightAnchor(tempTableView, 0.0);
            AnchorPane.setTopAnchor(tempTableView, 0.0);
            titleTable.getChildren().add(tempTableView);
        }
    }

    private double createInheritanceTableColomnHeader(TableColumn<T, ?> tempTableColumn, TableColumn<T, ?> tableColumn, double tempColumnMaxHeight) {
        double result = tempColumnMaxHeight;
        if (!tableColumn.getColumns().isEmpty()) {
            for (int i = 0; i < tableColumn.getColumns().size(); i++) {
                TableColumn<T, ?> nextTempTableColumn = new TableColumn(((TableColumn<T, ?>) tableColumn.getColumns().get(i)).getText());
                nextTempTableColumn.setMinWidth(((TableColumn<T, ?>) tableColumn.getColumns().get(i)).getMinWidth());
                nextTempTableColumn.setMaxWidth(((TableColumn<T, ?>) tableColumn.getColumns().get(i)).getMinWidth());
                result = createInheritanceTableColomnHeader((TableColumn<T, ?>) nextTempTableColumn, (TableColumn<T, ?>) tableColumn.getColumns().get(i), tempColumnMaxHeight + 20);
                tempTableColumn.getColumns().add(nextTempTableColumn);
            }
        }
        return result;
    }

    private ScrollBar findScrollBar(TableView tableView, Orientation orientation) {
        return tableView.lookupAll(".scroll-bar")
                .stream()
                .filter(n -> n instanceof ScrollBar)
                .map(n -> (ScrollBar) n)
                .filter(sb -> sb.getOrientation() == orientation)
                .findFirst()
                .orElse(null);
    }

    private void createContentTable() {
        contentTable = new AnchorPane();

        contentTable.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"), CornerRadii.EMPTY, Insets.EMPTY)));

        contentTable.getChildren().clear();
        scpContentTable.setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), CornerRadii.EMPTY, Insets.EMPTY)));
        AnchorPane.setBottomAnchor(scpContentTable, 0.0);
        AnchorPane.setLeftAnchor(scpContentTable, 0.0);
        AnchorPane.setRightAnchor(scpContentTable, 0.0);
        AnchorPane.setTopAnchor(scpContentTable, 0.0);
        contentTable.getChildren().add(scpContentTable);

        AnchorPane a = new AnchorPane();
        a.setBackground(new Background(new BackgroundFill(Paint.valueOf("yellow"), CornerRadii.EMPTY, Insets.EMPTY)));
        a.setMinWidth(2000);
        a.setMaxWidth(2000);
        scpContentTable.setContent(a);
    }

}
