/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import com.jfoenix.controls.JFXButton;
import hotelfx.persistence.model.TblSystemFeature;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author ANDRI
 */
public class ClassFeature {

    private FeatureParent firstFeatureParent;
    private FeatureParent tailFeatureParent;

    private FeatureParent selectedFeatureParent;

    private TblSystemRoleSystemFeature selectedRoleFeature;

    public ClassFeature() {

    }

    public FeatureParent getFirstFeatureParent() {
        return firstFeatureParent;
    }

    public FeatureParent getTailFeatureParent() {
        return tailFeatureParent;
    }

    public void addFeatureParent(TblSystemRoleSystemFeature feature) {
        if (getFeatureParent(feature.getTblSystemFeature().getIdfeature()) == null) {
            FeatureParent addedFeatureParent = new FeatureParent(feature);
            if (firstFeatureParent == null) {
                addedFeatureParent.setPrev(addedFeatureParent);
                addedFeatureParent.setNext(addedFeatureParent);
                firstFeatureParent = addedFeatureParent;
                tailFeatureParent = addedFeatureParent;
            } else if (firstFeatureParent == tailFeatureParent) {
                tailFeatureParent = addedFeatureParent;
                firstFeatureParent.setNext(tailFeatureParent);
                firstFeatureParent.setPrev(tailFeatureParent);
                tailFeatureParent.setNext(firstFeatureParent);
                tailFeatureParent.setPrev(firstFeatureParent);
            } else {
                tailFeatureParent.setNext(addedFeatureParent);
                addedFeatureParent.setPrev(tailFeatureParent);
                addedFeatureParent.setNext(firstFeatureParent);
                tailFeatureParent = tailFeatureParent.getNext();
                firstFeatureParent.setPrev(tailFeatureParent);
            }
        }
    }

    public FeatureParent getFeatureParent(long featureID) {
        if (featureID != -1) {
            FeatureParent attempt = firstFeatureParent;
            if (attempt != null) {
                do {
                    if (attempt.getRoleFeatureInstance().getTblSystemFeature().getIdfeature() == featureID) {
                        return attempt;
                    }
                    attempt = attempt.getNext();
                } while (!attempt.equals(firstFeatureParent));
            }
        }
        return null;
    }

    public ObservableList<FeatureParent> getFeatureParents() {
        ObservableList<FeatureParent> featureParents = FXCollections.observableArrayList();
        FeatureParent attempt = firstFeatureParent;
        do {
            featureParents.add(attempt);
            attempt = attempt.getNext();
        } while (!attempt.equals(firstFeatureParent));

        return featureParents;
    }

    public FeatureParent getSelectedFeatureParent() {
        return selectedFeatureParent;
    }

    public void setSelectedFeatureParent(FeatureParent selectedFeatureParent) {
        this.selectedFeatureParent = selectedFeatureParent;
        this.selectedFeatureParent.setVisible(true);
    }

    public void toPrevious() {
        selectedFeatureParent.setVisible(false);
        setSelectedFeatureParent(selectedFeatureParent.getPrev());
    }

    public void toNext() {
        selectedFeatureParent.setVisible(false);
        setSelectedFeatureParent(selectedFeatureParent.getNext());
    }

    public boolean addFeatureChild(TblSystemRoleSystemFeature feature) {
        if (getFeatureParent((feature.getTblSystemFeature().getTblSystemFeature() != null)
                ? feature.getTblSystemFeature().getTblSystemFeature().getIdfeature()
                : -1) != null) {
            getFeatureParent(feature.getTblSystemFeature().getTblSystemFeature().getIdfeature()).addFeatureChild(new FeatureChild(feature));
        } else {
            addFeatureParent(feature);
            getFeatureParent(feature.getTblSystemFeature().getTblSystemFeature().getIdfeature()).addFeatureChild(new FeatureChild(feature));
        }
        return false;
    }

    public void addFeature(TblSystemRoleSystemFeature feature) {
        if (isFeatureParent(feature.getTblSystemFeature())) {
            addFeatureParent(feature);
        } else {
            addFeatureChild(feature);
        }
    }

    public boolean isFeatureParent(TblSystemFeature feature) {
        return feature.getTblSystemFeature() == null;
    }

    public TblSystemRoleSystemFeature getSelectedRoleFeature() {
        return selectedRoleFeature;
    }

    public void setSelectedRoleFeature(TblSystemRoleSystemFeature selectedRoleFeature) {
        this.selectedRoleFeature = selectedRoleFeature;
    }

    public class FeatureParent extends BorderPane {

        private final TblSystemRoleSystemFeature roleFeatureInstance;

        private FeatureParent next;
        private FeatureParent prev;

        private final double BASE_WIDTH = 1200;
        private final double BASE_HEIGHT = 590;

        private AnchorPane header;

        private Label label;

        private AnchorPane content;

        private FlowPane flowPane;

        private ObservableList<FeatureChild> featureChilds;

        public FeatureParent(TblSystemRoleSystemFeature roleFeatureInstance) {

            //set role feature instance
            this.roleFeatureInstance = roleFeatureInstance;

            //set style class
            getStyleClass().add("feature");

            //set min and max sise (border-pane)
            setMinSize(BASE_WIDTH, BASE_HEIGHT);
            setPrefSize(BASE_WIDTH, BASE_HEIGHT);
            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            //set visible(false)
            setVisible(false);

            //set observale-list feature
            featureChilds = FXCollections.observableArrayList();
            featureChilds.addListener((ListChangeListener.Change<? extends Object> change) -> {
                while (change.next()) {
                    if (change.wasRemoved()) {
                        flowPane.getChildren().removeAll(change.getRemoved());
                    }
                    if (change.wasAdded()) {
                        flowPane.getChildren().addAll(change.getAddedSubList().stream()
                                .filter(FeatureChild.class::isInstance)
                                .map(FeatureChild.class::cast)
                                .collect(Collectors.toList()));
                    }
                }
            });

            //header
            header = new AnchorPane();
            header.setMinSize(1200, 85);
            header.setPrefSize(1200, 85);
            header.setMaxSize(Double.MAX_VALUE, 85);

            setTop(header);

            //label
            label = new Label(this.roleFeatureInstance.getTblSystemFeature().getFeatureName());
            label.setFont(new Font("System", 30));

            AnchorPane.setTopAnchor(label, 35.0);
            AnchorPane.setLeftAnchor(label, 35.0);
            AnchorPane.setBottomAnchor(label, 10.0);

            header.getChildren().add(label);

            //content
            content = new AnchorPane();
            content.setMinSize(1200, 505);
            content.setPrefSize(1200, 505);
            content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            setCenter(content);

            //flow-pane
            flowPane = new FlowPane();

            flowPane.setPadding(new Insets(15));
            flowPane.setHgap(32);
            flowPane.setVgap(32);

            AnchorPane.setTopAnchor(flowPane, 20.0);
            AnchorPane.setRightAnchor(flowPane, 20.0);
            AnchorPane.setBottomAnchor(flowPane, 20.0);
            AnchorPane.setLeftAnchor(flowPane, 20.0);

            content.getChildren().add(flowPane);
        }

        public FeatureChild getFeatureChild(long featureID) {
            for (FeatureChild featureChild : featureChilds) {
                if (featureChild.getRoleFeatureInstance().getTblSystemFeature().getIdfeature() == featureID) {
                    return featureChild;
                }
            }
            return null;
        }

        public ObservableList<FeatureChild> getFeatureChilds() {
            return featureChilds;
        }

        public void addFeatureChild(FeatureChild featureChild) {
            featureChilds.add(featureChild);
        }

        public void setNext(FeatureParent next) {
            this.next = next;
        }

        public void setPrev(FeatureParent prev) {
            this.prev = prev;
        }

        public FeatureParent getNext() {
            return this.next;
        }

        public FeatureParent getPrev() {
            return this.prev;
        }

        public TblSystemRoleSystemFeature getRoleFeatureInstance() {
            return roleFeatureInstance;
        }

    }

    public class FeatureChild extends JFXButton {

        private final TblSystemRoleSystemFeature roleFeatureInstance;

        private final double BASE_WIDTH = 232;
        private final double BASE_HEIGHT = 100;

        public FeatureChild(TblSystemRoleSystemFeature roleFeatureInstance) {
            super();

            //set role feature instance
            this.roleFeatureInstance = roleFeatureInstance;

            //set rippler fill
            ripplerFillProperty().set(Color.GOLD);

            //set min and max size
            setMinSize(BASE_WIDTH, BASE_HEIGHT);
            setMaxSize(BASE_WIDTH, BASE_HEIGHT);

            //set padding and alignment
            setPadding(new Insets(12, 16, 12, 16));
            setAlignment(Pos.CENTER_LEFT);

            //set feature name
            setText(this.roleFeatureInstance.getTblSystemFeature().getFeatureName());

            //set feature icon
            setGraphic(new ImageView(new Image(roleFeatureInstance.getTblSystemFeature().getImageIconPath(), 64, 64, true, true)));
            setGraphicTextGap(30);

            //set button type
            setButtonType(ButtonType.RAISED);

            //set visible(true)
            setVisible(true);

        }

        public TblSystemRoleSystemFeature getRoleFeatureInstance() {
            return roleFeatureInstance;
        }

    }

}
