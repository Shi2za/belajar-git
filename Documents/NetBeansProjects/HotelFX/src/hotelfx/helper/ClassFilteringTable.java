/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.util.function.Predicate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author ANDRI
 */
public class ClassFilteringTable<T> extends AnchorPane {

    private Class<T> clazz;

    private final HBox backgroundLayout = new HBox();

    private final JFXButton btnSearch = new JFXButton(" ");
    private final AnchorPane ancBtnSearchLayout = new AnchorPane();

    private final JFXButton btnPosition = new JFXButton("A...");

    private final JFXButton btnIgnoreCase = new JFXButton("aA");

    /**
     * POSITION STATUS Start With : '1' Contain : '2' End With : '3'
     */
    private final IntegerProperty positionStatus = new SimpleIntegerProperty(1);

    private final BooleanProperty ignoreCaseStatus = new SimpleBooleanProperty(false);

    private final JFXTextField txtFilter = new JFXTextField("");
    private final AnchorPane ancTxtFilterLayout = new AnchorPane();

    private final BooleanProperty active = new SimpleBooleanProperty(true);

    private TableView<T> tableView;

    private ObservableList<T> items = FXCollections.observableArrayList();

    public ClassFilteringTable(
            Class<T> clazz,
            TableView<T> tableView,
            ObservableList<T> items) {
        super();
        initialize();

        this.clazz = clazz;
        this.tableView = tableView;
        this.items.setAll(items);

        txtFilter.setText("");
        txtFilter.setPromptText("Cari");
        txtFilter.setLabelFloat(false);
        txtFilter.setPrefSize(200, 25);
        txtFilter.getStyleClass().add("text-filtering");
        if (this.tableView != null) {
            txtFilter.setOnKeyReleased((e) -> {
                //set filter
                if (active.get()) {
                    if (e.getCode() == KeyCode.ENTER) {
                        this.tableView.setItems(FXCollections.observableArrayList(this.items));
                    } else {
                        String filter = String.valueOf(txtFilter.getText());
                        if (filter == null || filter.length() == 0) {
                            this.tableView.setItems(FXCollections.observableArrayList(this.items));
                        } else {
                            doFiltering(filter, positionStatus.get(), ignoreCaseStatus.get());
                        }
                    }
                }
            });
        }
        AnchorPane.setBottomAnchor(txtFilter, 0.0);
        AnchorPane.setLeftAnchor(txtFilter, 0.0);
        AnchorPane.setRightAnchor(txtFilter, 0.0);
        AnchorPane.setTopAnchor(txtFilter, 0.0);
        ancTxtFilterLayout.setPrefSize(200, 25);
        ancTxtFilterLayout.getChildren().clear();
        ancTxtFilterLayout.getChildren().add(txtFilter);

        btnSearch.setPrefSize(15, 10);
        btnSearch.getStyleClass().add("button-search");
        btnSearch.setDisable(true);
        AnchorPane.setBottomAnchor(btnSearch, 7.5);
        AnchorPane.setLeftAnchor(btnSearch, 7.5);
        AnchorPane.setRightAnchor(btnSearch, 7.5);
        AnchorPane.setTopAnchor(btnSearch, 7.5);
        ancBtnSearchLayout.setPrefSize(30, 25);
        ancBtnSearchLayout.getChildren().clear();
        ancBtnSearchLayout.getChildren().add(btnSearch);

        btnPosition.setPrefSize(50, 30);
        btnPosition.getStyleClass().add("button-position");
        btnPosition.setText("-A-");
        btnPosition.setTooltip(new Tooltip("Contain"));
        positionStatus.setValue(2);
        btnPosition.setOnMouseClicked((e) -> {
            switch (positionStatus.get()) {
                case 1:
                    btnPosition.setText("-A-");
                    btnPosition.setTooltip(new Tooltip("Contain"));
                    positionStatus.set(2);
                    break;
                case 2:
                    btnPosition.setText("...A");
                    btnPosition.setTooltip(new Tooltip("End With"));
                    positionStatus.set(3);
                    break;
                case 3:
                    btnPosition.setText("A...");
                    btnPosition.setTooltip(new Tooltip("Start With"));
                    positionStatus.set(1);
                    break;
                default:
                    break;
            }
            //set filter
            if (active.get()) {
                String filter = String.valueOf(txtFilter.getText());
                if (filter == null || filter.length() == 0) {
                    this.tableView.setItems(FXCollections.observableArrayList(this.items));
                } else {
                    doFiltering(filter, positionStatus.get(), ignoreCaseStatus.get());
                }
            }
        });

        btnIgnoreCase.setPrefSize(50, 30);
        btnIgnoreCase.getStyleClass().add("button-match-case");
        btnIgnoreCase.setTooltip(new Tooltip("Match Case"));
        ignoreCaseStatus.set(true);
        btnIgnoreCase.setOnMouseClicked((e) -> {
            ignoreCaseStatus.set(!ignoreCaseStatus.get());
            //set filter
            if (active.get()) {
                String filter = String.valueOf(txtFilter.getText());
                if (filter == null || filter.length() == 0) {
                    this.tableView.setItems(FXCollections.observableArrayList(this.items));
                } else {
                    doFiltering(filter, positionStatus.get(), ignoreCaseStatus.get());
                }
            }
        });

        backgroundLayout.setSpacing(5);
        backgroundLayout.setPrefSize(195, 25);
        backgroundLayout.getChildren().addAll(ancBtnSearchLayout, ancTxtFilterLayout);
//        backgroundLayout.setPrefSize(285, 30);
//        backgroundLayout.getChildren().addAll(ancBtnSearchLayout, txtFilter, btnPosition, btnIgnoreCase);

        this.getChildren().add(backgroundLayout);
    }

    private void doFiltering(String filter, int position, boolean ignoreCase) {
        FilteredList<T> fl = new FilteredList(this.items, i -> true);
        fl.setPredicate((Predicate<? super T>) item -> {
            for (int i = 0; i < tableView.getColumns().size(); i++) {
                if(checkInheritanceColumn(filter, position, ignoreCase, item, (TableColumn<T, ?>)tableView.getColumns().get(i))){
                    return true;
                }
//                if (ignoreCase) {
//                    switch (position) {
//                        case 1:
//                            if (String.valueOf(tableView.getColumns().get(i).getCellData(item)).toLowerCase()
//                                    .startsWith(filter.toLowerCase())) {
//                                return true;
//                            }
//                            break;
//                        case 2:
//                            if (String.valueOf(tableView.getColumns().get(i).getCellData(item)).toLowerCase()
//                                    .contains(filter.toLowerCase())) {
//                                return true;
//                            }
//                            break;
//                        case 3:
//                            if (String.valueOf(tableView.getColumns().get(i).getCellData(item)).toLowerCase()
//                                    .endsWith(filter.toLowerCase())) {
//                                return true;
//                            }
//                            break;
//                        default:
//                            break;
//                    }
//                } else {
//                    switch (position) {
//                        case 1:
//                            if (String.valueOf(tableView.getColumns().get(i).getCellData(item))
//                                    .startsWith(filter)) {
//                                return true;
//                            }
//                            break;
//                        case 2:
//                            if (String.valueOf(tableView.getColumns().get(i).getCellData(item))
//                                    .contains(filter)) {
//                                return true;
//                            }
//                            break;
//                        case 3:
//                            if (String.valueOf(tableView.getColumns().get(i).getCellData(item))
//                                    .endsWith(filter)) {
//                                return true;
//                            }
//                            break;
//                        default:
//                            break;
//                    }
//                }
            }
            return false;
        });
        this.tableView.setItems(FXCollections.observableArrayList(fl));
    }

    private boolean checkInheritanceColumn(String filter, int position, boolean ignoreCase, T item, TableColumn<T, ?> tableColumn) {
        if (!tableColumn.getColumns().isEmpty()) {
            for (int i = 0; i < tableColumn.getColumns().size(); i++) {
                if(checkInheritanceColumn(filter, position, ignoreCase, item, (TableColumn<T, String>)tableColumn.getColumns().get(i))){
                    return true;
                }
            }
        } else {
            if (ignoreCase) {
                switch (position) {
                    case 1:
                        if (String.valueOf(tableColumn.getCellData(item)).toLowerCase()
                                .startsWith(filter.toLowerCase())) {
                            return true;
                        }
                        break;
                    case 2:
                        if (String.valueOf(tableColumn.getCellData(item)).toLowerCase()
                                .contains(filter.toLowerCase())) {
                            return true;
                        }
                        break;
                    case 3:
                        if (String.valueOf(tableColumn.getCellData(item)).toLowerCase()
                                .endsWith(filter.toLowerCase())) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            } else {
                switch (position) {
                    case 1:
                        if (String.valueOf(tableColumn.getCellData(item))
                                .startsWith(filter)) {
                            return true;
                        }
                        break;
                    case 2:
                        if (String.valueOf(tableColumn.getCellData(item))
                                .contains(filter)) {
                            return true;
                        }
                        break;
                    case 3:
                        if (String.valueOf(tableColumn.getCellData(item))
                                .endsWith(filter)) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return false;
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public boolean isActive() {
        return activeProperty().get();
    }

    public void setIsActive(boolean active) {
        activeProperty().set(active);
    }

//    public String getFilter(){
//        return txtFilter.getText();
//    }
//    
//    public void setFilter(String filter){
//        txtFilter.setText(filter);
//    }
    public void refreshFilterItems(ObservableList<T> items) {
        this.items.setAll(items);
        this.txtFilter.setText("");
    }

    public String getTextFilter(){
        return txtFilter.getText();
    }
    
    public void setTextFilter(String textFilter){
        txtFilter.setText(textFilter);
        doFiltering(txtFilter.getText(), 2, true);
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
    private static final String DEFAULT_STYLE_CLASS = "class-filtering-table";

    private void initialize() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

}
