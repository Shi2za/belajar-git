/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author ANDRI
 */
public class ClassTableWithControl extends StackPane {

    private final TableView tableView;

    private final ToolbarControl toolbarControl;

    public ClassTableWithControl(TableView tableView) {
        super();
        
        //int table view
        this.tableView = tableView;

        //int toolbarControl
        toolbarControl = new ToolbarControl();

        toolbarControl.setMinHeight(35);
        toolbarControl.setMaxHeight(35);
        toolbarControl.setPrefHeight(35);

        setAlignment(toolbarControl, Pos.BOTTOM_LEFT);

        //set content to stack-pane
        getChildren().add(this.tableView);
        getChildren().add(toolbarControl);

        //add column number
        TableColumn columnNumber = new TableColumn();
        columnNumber.setMinWidth(35);
        columnNumber.setMaxWidth(35);
        columnNumber.setSortable(false);
        this.tableView.getColumns().add(0, columnNumber);
        
        //create listener to block header-reordering
        this.tableView.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

//        this.tableView.getColumns().addListener((ListChangeListener.Change change) -> {
//            PseudoClass firstColumnPseudoClass = PseudoClass.getPseudoClass("first-column");
//            while(change.next()){
//                System.out.println("change");
//                System.out.println(" "+tableView.getColumns());
//                System.out.println(change.wasPermutated() ? "permutated"
//                        : change.wasAdded() ? "added"
//                                : change.wasRemoved() ? "removed"
//                                        : change.wasUpdated() ? "updated"
//                                                : change.wasReplaced() ? "replaced" : "???");
//                if(change.wasAdded()){
//                    this.tableView.getColumns().stream()
//                            .filter(TableColumn.class::isInstance)
//                            .map(TableColumn.class::cast)
//                            .forEach(tc -> {
//                                ((TableCell)(((TableColumn)tc).getCellFactory().call(tc))).setPadding(new Insets(60));
//
//                                System.out.println(" >"+((TableCell)(((TableColumn)tc).getCellFactory().call(tc))).getPseudoClassStates());
//                                ((TableCell)(((TableColumn)tc).getCellFactory().call(tc))).pseudoClassStateChanged(firstColumnPseudoClass, false);
//                                //((TableCell)tc.getCellFactory().call(tc)).pseudoClassStateChanged(firstColumnPseudoClass, false);
//                            });
//                    ((TableCell)((TableColumn)this.tableView.getColumns().get(0)).getCellFactory().call((TableColumn)this.tableView.getColumns().get(0))).pseudoClassStateChanged(firstColumnPseudoClass, true);
//                    System.out.println(((TableCell)((TableColumn)this.tableView.getColumns().get(0)).getCellFactory().call((TableColumn)this.tableView.getColumns().get(0))).getPseudoClassStates());
//                }
//            }
//        });
    }

    public void addButtonControl(ObservableList<Node> buttons) {
        toolbarControl.setItems(buttons);
    }

    public TableView getTableView(){
        return tableView;
    }
    
    public ToolbarControl getToolbarControl(){
        return toolbarControl;
    }
    
    public class ToolbarControl extends AnchorPane {

        private final JFXButton showButton = new JFXButton(">");

        private final HBox toolbarButton = new HBox();

        public ToolbarControl() {
            super();
            initialize();

            //toolbar button
            toolbarButton.setVisible(false);
            AnchorPane.setTopAnchor(toolbarButton, 0.0);
            AnchorPane.setLeftAnchor(toolbarButton, 40.0);
            AnchorPane.setBottomAnchor(toolbarButton, 0.0);
            AnchorPane.setRightAnchor(toolbarButton, 0.0);

//            toolbarButton.getChildren().stream().map(Region.class::cast).forEach(n -> {
//                System.out.println(n.widthProperty());
//            });
//            toolbarButton.getChildren().addListener((ListChangeListener.Change<? extends Object> change) -> {
//                System.out.println("Change!!!");
//                while (change.next()) {
//                    if (change.wasAdded()) {
//                        change.getAddedSubList().stream()
//                                .filter(Button.class::isInstance)
//                                .map(Button.class::cast)
//                                .forEach(b -> {
//                                    toolbarButton.setPrefWidth(toolbarButton.getPrefWidth() + b.getWidth());
//                                    System.out.println(" " + b.getWidth());
//                                });
//                    } else if (change.wasRemoved()) {
//                        change.getRemoved().stream()
//                                .filter(Button.class::isInstance)
//                                .map(Button.class::cast)
//                                .forEach(b -> {
//                                    toolbarButton.setPrefWidth(toolbarButton.getPrefWidth() - b.getWidth());
//                                });
//                    }
//                }
//            });
            getChildren().add(toolbarButton);

            //show button
            showButton.setPrefSize(35, 30);
            showButton.setOnMouseClicked((e) -> showButton(e));
            AnchorPane.setTopAnchor(showButton, 0.0);
            AnchorPane.setLeftAnchor(showButton, 0.0);
            AnchorPane.setBottomAnchor(showButton, 0.0);

            getChildren().add(showButton);

            //give time for resize anchor(0.5 sec)
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(500);
                    Platform.runLater(() -> {
                        setMinWidth(40);
                        setMaxWidth(40);
                    });
                } catch (Exception e) {
                    System.out.println("err " + e.getMessage());
                }

            });
            thread.setDaemon(true);
            thread.start();

        }

        private void showButton(MouseEvent e) {
            if (toolbarButton.isVisible()) {
                showButton.setText(">");
                showButton.setTooltip(new Tooltip("Show"));
                toolbarButton.setVisible(false);
                setMinWidth(40);
                setMaxWidth(40);
            } else {
                showButton.setText("<");
                showButton.setTooltip(new Tooltip("Hide"));
                toolbarButton.setVisible(true);
//                toolbarButton.getChildren().stream().map(Button.class::cast).forEach(b -> {
//                    setMinWidth(getMinWidth() + b.getWidth());
//                    setMaxWidth(getMaxWidth() + b.getWidth());
//                });
                setMinWidth(toolbarButton.getPrefWidth() + 45);
                setMaxWidth(toolbarButton.getPrefWidth() + 45);

            }
        }

        public void setItems(ObservableList<Node> nodes) {
            toolbarButton.getChildren().setAll(nodes);
            for (Node node : nodes) {
                ((Button) node).widthProperty().addListener((obs, oldWidth, newWidth) -> {
                    if (oldWidth.doubleValue() <= 0.0 && oldWidth.doubleValue() < newWidth.doubleValue()) {
                        setPrefWidth(newWidth.doubleValue());
                        toolbarButton.prefWidthProperty().set(toolbarButton.getPrefWidth() - oldWidth.doubleValue() + newWidth.doubleValue());
                    }
                });
            }
        }

        public ObservableList<Node> getItems() {
            return this.toolbarButton.getChildren();
        }

        /**
         * *************************************************************************
         *                                                                         *
         * Stylesheet Handling * *
         * ************************************************************************
         */
        /**
         * Initialize the style class to 'jfx-tool-bar'.
         *
         * This is the selector class from which CSS can be used to style this
         * control.
         */
        private static final String DEFAULT_STYLE_CLASS = "jfx-custom-tool-bar";

        private void initialize() {
            this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        }
    }

}
