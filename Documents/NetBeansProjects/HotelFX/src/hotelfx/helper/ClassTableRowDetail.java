/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author ANDRI
 * @param <T>
 */
public class ClassTableRowDetail<T> extends TableRow {

    private Node detailsPane = new BorderPane();

    private boolean show = false;

    public ClassTableRowDetail() {
        selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (!isNowSelected) {
                hide();
            }
        });
    }

    @Override
    protected double computePrefHeight(double width) {
        if (isSelected() && show) {
            return super.computePrefHeight(width) + detailsPane.prefHeight(getWidth());
        } else {
            return super.computePrefHeight(width);
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (isSelected() && show) {
            double width = getWidth();
            double paneHeight = detailsPane.prefHeight(width);
            detailsPane.resizeRelocate(0, getHeight() - paneHeight, width, paneHeight);
        }
    }

    public boolean isShow(){
        return show;
    }
    
    public void show(Node node) {
        hide();
        show = true;
        detailsPane = node;
        getChildren().add(detailsPane);
        this.requestLayout();
    }

    public void hide() {
        show = false;
//        detailsPane = new BorderPane();
        getChildren().remove(detailsPane);
        this.requestLayout();
    }

}
