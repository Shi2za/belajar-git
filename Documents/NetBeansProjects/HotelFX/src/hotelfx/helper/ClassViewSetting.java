/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxPopup;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.skins.JFXDatePickerContent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

/**
 *
 * @author ANDRI
 */
public class ClassViewSetting {

    //KEY FRAME DURATION FOR TIMELINE (SPLITPANE : TABLE-FORM) => in mili seconds
    public static double keyFrameDuration = 200;
    
    //SET FORM INPUT------------------------------------------------------------
    public static void setDisableForAllInputNode(Parent root, boolean disable, Node... hasBeenSettingNodes) {
        setDisableForAllInputNode(getAllNodes(root), disable, hasBeenSettingNodes);
    }

    public static void setDisableForAllInputNode(List<Node> dataNodes, boolean disable, Node... hasBeenSettingNodes) {
        for (Node dataNode : dataNodes) {
            if (dataNode instanceof JFXButton) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXButton) {
                        if (((JFXButton) dataNode).equals((JFXButton) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXButton) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXButton) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof Button) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof Button) {
                        if (((Button) dataNode).equals((Button) hasBeenSettingNode)) {
                            isBaseDisablNode = ((Button) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((Button) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof JFXTextField) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXTextField) {
                        if (((JFXTextField) dataNode).equals((JFXTextField) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXTextField) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXTextField) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof TextField) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof TextField) {
                        if (((TextField) dataNode).equals((TextField) hasBeenSettingNode)) {
                            isBaseDisablNode = ((TextField) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((TextField) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof JFXTextArea) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXTextArea) {
                        if (((JFXTextArea) dataNode).equals((JFXTextArea) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXTextArea) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXTextArea) dataNode).setDisable(disable);
                }
            }
//            if (dataNode instanceof TextArea) {
//                boolean isBaseDisablNode = false;
//                for(Node baseDisableNode : baseDisableNodes){
//                    if(baseDisableNode instanceof TextArea){
//                        if(((TextArea)dataNode).equals((TextArea)baseDisableNode)){
//                            isBaseDisablNode = ((TextArea)baseDisableNode).isDisable();
//                            break;
//                        }
//                    }
//                }
//                if(!isBaseDisablNode){
//                    ((TextArea) dataNode).setDisable(disable);
//                }
//            }
            if (dataNode instanceof JFXDatePicker) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXDatePicker) {
                        if (((JFXDatePicker) dataNode).equals((JFXDatePicker) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXDatePicker) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXDatePicker) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof DatePicker) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof DatePicker) {
                        if (((DatePicker) dataNode).equals((DatePicker) hasBeenSettingNode)) {
                            isBaseDisablNode = ((DatePicker) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((DatePicker) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof JFXTimePicker) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXTimePicker) {
                        if (((JFXTimePicker) dataNode).equals((JFXTimePicker) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXTimePicker) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXTimePicker) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof JFXRadioButton) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXRadioButton) {
                        if (((JFXRadioButton) dataNode).equals((JFXRadioButton) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXRadioButton) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXRadioButton) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof RadioButton) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof RadioButton) {
                        if (((RadioButton) dataNode).equals((RadioButton) hasBeenSettingNode)) {
                            isBaseDisablNode = ((RadioButton) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((RadioButton) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof JFXCheckBox) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXCheckBox) {
                        if (((JFXCheckBox) dataNode).equals((JFXCheckBox) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXCheckBox) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXCheckBox) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof CheckBox) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof CheckBox) {
                        if (((CheckBox) dataNode).equals((CheckBox) hasBeenSettingNode)) {
                            isBaseDisablNode = ((CheckBox) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((CheckBox) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof Spinner) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof Spinner) {
                        if (((Spinner) dataNode).equals((Spinner) hasBeenSettingNode)) {
                            isBaseDisablNode = ((Spinner) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((Spinner) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof JFXComboBox) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXComboBox) {
                        if (((JFXComboBox) dataNode).equals((JFXComboBox) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXComboBox) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXComboBox) dataNode).setDisable(disable);
                }
            }
            if (dataNode instanceof JFXCComboBoxPopup) {
                boolean isBaseDisablNode = false;
                for (Node hasBeenSettingNode : hasBeenSettingNodes) {
                    if (hasBeenSettingNode instanceof JFXCComboBoxPopup) {
                        if (((JFXCComboBoxPopup) dataNode).equals((JFXCComboBoxPopup) hasBeenSettingNode)) {
                            isBaseDisablNode = ((JFXCComboBoxPopup) hasBeenSettingNode).isDisable();
                            break;
                        }
                    }
                }
                if (!isBaseDisablNode) {
                    ((JFXCComboBoxPopup) dataNode).setDisable(disable);
                }
            }
        }
    }

    public static void setDisableForAllInputNode(Parent root, boolean disable) {
        setDisableForAllInputNode(getAllNodes(root), disable);
    }

    public static void setDisableForAllInputNode(List<Node> dataNodes, boolean disable) {
        for (Node dataNode : dataNodes) {
            if (dataNode instanceof JFXButton) {
//                if(!disable 
//                        && ((JFXButton)dataNode).isDisabled()){
//                    ((JFXButton)dataNode).setDisable(true);
//                }else{
                ((JFXButton) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof Button) {
//                if(!disable 
//                        && ((Button)dataNode).isDisabled()){
//                    ((Button)dataNode).setDisable(true);
//                }else{
                ((Button) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof JFXTextField) {
//                if(!disable 
//                        && ((JFXTextField)dataNode).isDisabled()){
//                    ((JFXTextField)dataNode).setDisable(true);
//                }else{
                ((JFXTextField) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof TextField) {
//                if(!disable 
//                        && ((TextField)dataNode).isDisabled()){
//                    ((TextField)dataNode).setDisable(true);
//                }else{
                ((TextField) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof JFXTextArea) {
//                if(!disable 
//                        && ((JFXTextArea)dataNode).isDisabled()){
//                    ((JFXTextArea)dataNode).setDisable(true);
//                }else{
                ((JFXTextArea) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof JFXDatePicker) {
//                if(!disable 
//                        && ((JFXDatePicker)dataNode).isDisabled()){
//                    ((JFXDatePicker)dataNode).setDisable(true);
//                }else{
                ((JFXDatePicker) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof DatePicker) {
//                if(!disable 
//                        && ((DatePicker)dataNode).isDisabled()){
//                    ((DatePicker)dataNode).setDisable(true);
//                }else{
                ((DatePicker) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof JFXTimePicker) {
//                if(!disable 
//                        && ((JFXTimePicker)dataNode).isDisabled()){
//                    ((JFXTimePicker)dataNode).setDisable(true);
//                }else{
                ((JFXTimePicker) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof JFXRadioButton) {
//                if(!disable 
//                        && ((JFXRadioButton)dataNode).isDisabled()){
//                    ((JFXRadioButton)dataNode).setDisable(true);
//                }else{
                ((JFXRadioButton) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof RadioButton) {
//                if(!disable 
//                        && ((RadioButton)dataNode).isDisabled()){
//                    ((RadioButton)dataNode).setDisable(true);
//                }else{
                ((RadioButton) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof JFXCheckBox) {
//                if(!disable 
//                        && ((JFXCheckBox)dataNode).isDisabled()){
//                    ((JFXCheckBox)dataNode).setDisable(true);
//                }else{
                ((JFXCheckBox) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof CheckBox) {
//                if(!disable 
//                        && ((CheckBox)dataNode).isDisabled()){
//                    ((CheckBox)dataNode).setDisable(true);
//                }else{
                ((CheckBox) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof Spinner) {
//                if(!disable 
//                        && ((Spinner)dataNode).isDisabled()){
//                    ((Spinner)dataNode).setDisable(true);
//                }else{
                ((Spinner) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof JFXComboBox) {
//                if(!disable 
//                        && ((JFXComboBox)dataNode).isDisabled()){
//                    ((JFXComboBox)dataNode).setDisable(true);
//                }else{
                ((JFXComboBox) dataNode).setDisable(disable);
//                }
            }
            if (dataNode instanceof JFXCComboBoxPopup) {
//                if(!disable 
//                        && ((JFXCComboBoxPopup)dataNode).isDisabled()){
//                    ((JFXCComboBoxPopup)dataNode).setDisable(true);
//                }else{
                ((JFXCComboBoxPopup) dataNode).setDisable(disable);
//                }
            }
        }
    }

    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDecendents(root, nodes);
        return nodes;
    }

    private static void addAllDecendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent) {
                addAllDecendents((Parent) node, nodes);
            }
        }
    }

    //NOTNULL PSEUDO CLASS - STATUS
    public static void setNotNullField(Node... notNullFieldNodes){
        for(Node notNullFieldNode : notNullFieldNodes){
            setNotNullPseudoClassStatus(notNullFieldNode);
        }
    }
    
    /**
     * true, if nonull-field in condition must be show to user false, if
     * nonull-field in condition not needed be show to user
     *
     * @param dataNode
     * @return
     */
    private static boolean setNotNullPseudoClassStatus(Node dataNode) {
        boolean show = false;
        if (dataNode instanceof JFXButton) {
            show = false;
        }
        if (dataNode instanceof Button) {
            show = false;
        }
        if (dataNode instanceof JFXTextField) {
            ((JFXTextField) dataNode).textProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null
                    || newVal.equals("")){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((JFXTextField) dataNode).getText() == null
                    || ((JFXTextField) dataNode).getText().equals("");
        }
        if (dataNode instanceof TextField) {
            ((TextField) dataNode).textProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null
                    || newVal.equals("")){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((TextField) dataNode).getText() == null
                    || ((TextField) dataNode).getText().equals("");
        }
        if (dataNode instanceof JFXTextArea) {
            ((JFXTextArea) dataNode).textProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null
                    || newVal.equals("")){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((JFXTextArea) dataNode).getText() == null
                    || ((JFXTextArea) dataNode).getText().equals("");
        }
//        if (dataNode instanceof TextArea) {
//            show = ((TextArea) dataNode).getText() == null
//                    || ((TextArea) dataNode).getText().equals("");
//        }
        if (dataNode instanceof JFXDatePicker) {
            ((JFXDatePicker) dataNode).valueProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((JFXDatePicker) dataNode).getValue() == null;
        }
        if (dataNode instanceof DatePicker) {
            ((DatePicker) dataNode).valueProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((DatePicker) dataNode).getValue() == null;
        }
        if (dataNode instanceof JFXTimePicker) {
            ((JFXTimePicker) dataNode).valueProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((JFXTimePicker) dataNode).getValue() == null;
        }
        if (dataNode instanceof JFXRadioButton) {
            show = true;
        }
        if (dataNode instanceof RadioButton) {
            show = true;
        }
        if (dataNode instanceof JFXCheckBox) {
            show = true;
        }
        if (dataNode instanceof CheckBox) {
            show = true;
        }
        if (dataNode instanceof Spinner) {
            ((Spinner) dataNode).valueProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((Spinner) dataNode).getValue() == null;
        }
        if (dataNode instanceof JFXComboBox) {
            ((JFXComboBox) dataNode).valueProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((JFXComboBox) dataNode).getValue() == null;
        }
        if (dataNode instanceof JFXCComboBoxPopup) {
            ((JFXCComboBoxPopup) dataNode).valueProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal == null){
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, false);
                }else{
                    dataNode.pseudoClassStateChanged(ClassPseudoClassCSS.notNullFieldPseudoClass, true);
                }
            });
            show = ((JFXCComboBoxPopup) dataNode).getValue() == null;
        }
        return true;
    }
    
    //IMPORTANT PSEUDO CLASS - STATUS
    public static void setImportantField(Node... importantFieldNodes){
        for(Node importantFieldNode : importantFieldNodes){
            importantFieldNode.pseudoClassStateChanged(ClassPseudoClassCSS.importantFieldPseudoClass, ClassViewSetting.setImportantPseudoClassStatus(importantFieldNode));
        }
    }
    
    /**
     * true, if important-field in condition must be show to user false, if
     * important-field in condition not needed be show to user
     *
     * @param dataNode
     * @return
     */
    private static boolean setImportantPseudoClassStatus(Node dataNode) {
        boolean show = false;
        if (dataNode instanceof JFXButton) {
            show = false;
        }
        if (dataNode instanceof Button) {
            show = false;
        }
        if (dataNode instanceof JFXTextField) {
            show = ((JFXTextField) dataNode).getText() == null
                    || ((JFXTextField) dataNode).getText().equals("");
        }
        if (dataNode instanceof TextField) {
            show = ((TextField) dataNode).getText() == null
                    || ((TextField) dataNode).getText().equals("");
        }
        if (dataNode instanceof JFXTextArea) {
            show = ((JFXTextArea) dataNode).getText() == null
                    || ((JFXTextArea) dataNode).getText().equals("");
        }
//        if (dataNode instanceof TextArea) {
//            show = ((TextArea) dataNode).getText() == null
//                    || ((TextArea) dataNode).getText().equals("");
//        }
        if (dataNode instanceof JFXDatePicker) {
            show = ((JFXDatePicker) dataNode).getValue() == null;
        }
        if (dataNode instanceof DatePicker) {
            show = ((DatePicker) dataNode).getValue() == null;
        }
        if (dataNode instanceof JFXTimePicker) {
            show = ((JFXTimePicker) dataNode).getValue() == null;
        }
        if (dataNode instanceof JFXRadioButton) {
            show = true;
        }
        if (dataNode instanceof RadioButton) {
            show = true;
        }
        if (dataNode instanceof JFXCheckBox) {
            show = true;
        }
        if (dataNode instanceof CheckBox) {
            show = true;
        }
        if (dataNode instanceof Spinner) {
            show = ((Spinner) dataNode).getValue() == null;
        }
        if (dataNode instanceof JFXComboBox) {
            show = ((JFXComboBox) dataNode).getValue() == null;
        }
        if (dataNode instanceof JFXCComboBoxPopup) {
            show = ((JFXCComboBoxPopup) dataNode).getValue() == null;
        }
        return true;
    }
}
