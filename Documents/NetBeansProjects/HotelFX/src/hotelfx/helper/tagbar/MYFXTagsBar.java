/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.tagbar;

import hotelfx.HotelFX;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPopup;
import com.sun.javafx.property.PropertyReference;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 *
 * @author Andreas
 */
public class MYFXTagsBar<S> extends FlowPane{
    
    private final ObservableList<S> selectedItems; /*selectedItems : tags*/
    private final TextField inputTextField;
    
    private Tag addTag, promptTag;
    
    private MYFXBaseOptMenu optMenu;
    
    private final JFXPopup popup;
    private DoubleProperty popupOffsetX;
    private DoubleProperty popupOffsetY;
    private ObjectProperty<Point2D> popupOffset;
    private JFXDialog dialog;
    private Region opMenu;
    
    private Class<S> clazz;
    
    private String prompt;
    
    public ObservableList<S> getSelectedItems() {
        return selectedItems;
    }
    
    public MYFXTagsBar(Class<S> clazz, MYFXBaseOptMenu<S> optMenu, String prompt) {
        getStylesheets().add(HotelFX.class.getResource("view/root.css").toExternalForm());
        getStyleClass().add("myfx-tags-bar");
        
        this.clazz = clazz;
        
        inputTextField = new TextField();
        selectedItems = FXCollections.observableArrayList();
        popup = new JFXPopup(optMenu);
        //popup.show(this); popup.hide();
        optMenu.getItemList().getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if(optMenu.getItemList().getSelectionModel().getSelectedItems().size() == 1){
                optMenu.selectedItemProperty().set((S)optMenu.getItemList().getSelectionModel().getSelectedItem());
            }
        });
        optMenu.selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem!=null && !selectedItems.contains(newItem)){
                selectedItems.add((S)newItem);
                optMenu.selectedItemProperty().set(null);
            }
        });
        popup.setOnHiding((e) -> {
            optMenu.initState();
            optMenu.selectedItemProperty().set(null);
        });
        
        popup.setOnShowing((e) -> {
//            System.out.println("anchorX, anchorY  : "+popup.getAnchorX() + ", "+ popup.getAnchorY());
//            System.out.println("x, y              : "+popup.getX() + ", "+ popup.getY());
//            System.out.println("   width x height : "+popup.getWidth()+ " x " +popup.getHeight());
//            System.out.println("   window         : "+popup.getScene().getWindow().getX()+ ", "+popup.getScene().getWindow().getY());
//            System.out.println("   primaryStage   : "+RR_Trial_2.primaryStage.getX()+", "+RR_Trial_2.primaryStage.getY());
//            System.out.println("                  : "+RR_Trial_2.primaryStage.getWidth()+" x "+RR_Trial_2.primaryStage.getHeight());
        });
        
        
        
        //tags = optMenu.getSelectedItems();
        selectedItems.addListener((ListChangeListener.Change<? extends Object> change) -> {
            while(change.next()) {
                if(change.wasPermutated()){
                    ArrayList<Node> newSublist = new ArrayList<>(change.getTo() - change.getFrom());
                    for(int i = change.getFrom(), end = change.getTo(); i < end; i++){
                        newSublist.add(null);
                    }
                    for(int i = change.getFrom(), end = change.getTo(); i < end; i++){
                        newSublist.set(change.getPermutation(i), getChildren().get(i));
                    }
                    getChildren().subList(change.getFrom(), change.getTo()).clear();
                    getChildren().addAll(change.getFrom(), newSublist);
                }else {
                    if(change.wasRemoved()){
                        getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                        if(change.getList().size()==0){
                            getChildren().add(change.getFrom(), getPromptTag());
                        }
                    }
                    if(change.wasAdded()){
                        if(change.getList().size()==1){
                            getChildren().subList(change.getFrom(), 1).clear();
                        }
                        getChildren().addAll(change.getFrom(), change.getAddedSubList().stream()
                                .filter(BaseModel.class::isInstance)
                                .map(BaseModel.class::cast)
                                .map(Tag::new).collect(Collectors.toList()));
                        //System.out.println("before hide "+popup.isShowing());
                        if(popup.isShowing()){
                           popup.hide(); 
                        }
                        //System.out.println("after hide "+popup.isShowing());
                    }
                }
            }
        });
        
        this.prompt = prompt;
        promptTag = new Tag(MYFXTagsBar.TagType.PROMPT);
        addTag = new Tag(MYFXTagsBar.TagType.ADD);
        
        getChildren().addAll(promptTag, addTag);
        
        //initPopupOffset();
    }
    
    private void calculatePopupOffset(){
        popupOffsetX.set(HotelFX.primaryStage.getX()
                        + HotelFX.primaryStage.getWidth()
                        - popup.getX()
                        - popup.getWidth());
        
        popupOffsetY.set(HotelFX.primaryStage.getY()
                        + HotelFX.primaryStage.getHeight()
                        - popup.getY()
                        - popup.getHeight());
    }
    
    private void calculatePopupOffset(Node container){
        System.out.println("container layout : "+container.getLayoutX()+", "+container.getLayoutY());
        System.out.println("container scene  : "+container.getScene().getX()+", "+container.getScene().getY());
        popupOffsetX.set(HotelFX.primaryStage.getX()
                        + HotelFX.primaryStage.getWidth()
                        - container.getLayoutX()
                        - popup.getPrefWidth());
        
        popupOffsetY.set(HotelFX.primaryStage.getY()
                        + HotelFX.primaryStage.getHeight()
                        - container.getLayoutY()
                        - popup.getPrefHeight());
    }
    
    private void initPopupOffset(){
        popupOffsetX = new SimpleDoubleProperty();
        popupOffsetY = new SimpleDoubleProperty();

        popupOffsetX.addListener((e) -> {
            if(popupOffsetX.get() > 0.0){
                popupOffsetX.set(0.0);
            }
        });
        
        popupOffsetY.addListener((e) -> {
            if(popupOffsetY.get() > 0.0){
                popupOffsetY.set(0.0);
            }
        });
        
    }
    
    private Tag getPromptTag(){
        return this.promptTag;
    }
    
    //    public JFXCTagsBar(Boolean dumb, String promptText, JFXCBaseOptMenu optMenu) {
//        tags = FXCollections.observableArrayList();
//        inputTextField = new TextField();
//        
//        this.optMenu = optMenu;
//        promptTag = new Tag(promptText);
//        
//        addTag = new Tag(true, "+");
//        addTag.onMouseClickedProperty().addListener((e) -> {
//            dialog.show();
//        });
//        
//        tags.addListener((ListChangeListener.Change<? extends String> change) -> {
//            while (change.next()) {
//                if (change.wasPermutated()) {
//                    ArrayList<Node> newSublist = new ArrayList<>(change.getTo() - change.getFrom());
//                    for(int i = change.getFrom(), end = change.getTo(); i < end; i++){
//                        newSublist.add(null);
//                    }
//                    for(int i = change.getFrom(), end = change.getTo(); i < end; i++){
//                        newSublist.set(change.getPermutation(i), getChildren().get(i));
//                    }
//                    getChildren().subList(change.getFrom(), change.getTo()).clear();
//                    getChildren().addAll(change.getFrom(), newSublist);
//                } else {
//                    if (change.wasRemoved()){
//                        getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
//                    }
//                    if (change.wasAdded()){
//                        getChildren().addAll(change.getFrom(), change.getAddedSubList().stream().map(Tag::new).collect(Collectors.toList()));
//                    }
//                }
//            }
//        });
//        
//        getChildren().addAll(promptTag, addTag);
//    }
    
    private class Tag extends HBox {
        
        //public Tag(){
        //    this("tag", MYFXTagsBar.TagType.TAG);
        //}
        
        public Tag(BaseModel tag){
            this(tag, MYFXTagsBar.TagType.TAG);
        }
        
        public Tag(MYFXTagsBar.TagType tagType){
            this(null, tagType);
        }
        
        public Tag(BaseModel tag, MYFXTagsBar.TagType tagType){
            Text text;      
            setAlignment(Pos.CENTER);
            getStyleClass().add("tag");
            if(null != tagType) switch (tagType) {
                case TAG:
                    text = new Text(tag.toString(BaseModel.StringType.NAME_QTY));
                    HBox.setMargin(text, new Insets(0, 0, 0, 5));
                    Button tagCancel = new Button();
//                    ImageView cancelIcon = new ImageView(
//                            new Image(getClass().getResourceAsStream(
//                                    "/rr_trial_2/resources/icon/149629-essential-compilation/png/multiply-red.png")));
                    ImageView cancelIcon = new ImageView(
                            new Image("file:resources/Icon/TagBar/multiply-red.png"));
                    cancelIcon.setFitHeight(10.0);
                    cancelIcon.setFitWidth(10.0);
                    tagCancel.setGraphic(cancelIcon);
                    tagCancel.getStyleClass().add("tag-cancel");
                    
                    tagCancel.setOnAction((e) -> {
                        System.out.println("cancel clicked");
                        selectedItems.remove(tag);
                    });
                    getChildren().addAll(text, tagCancel);
                    break;
                case PROMPT:
                    text = new Text(prompt);
                    text.getStyleClass().addAll("tag-prompt");
                    HBox.setMargin(text, new Insets(0, 2, 0, 2));
                    getChildren().add(text);
                    break;
                case ADD:
                    Button tagAdd = new Button();
                    tagAdd.getStyleClass().add("tag-add");
//                    ImageView addIcon = new ImageView(
//                            new Image(getClass().getResourceAsStream(
//                                    "/rr_trial_2/resources/icon/149629-essential-compilation/png/add-1-grey.png")));
                    ImageView addIcon = new ImageView(
                            new Image("file:resources/Icon/TagBar/add-1-grey.png"));
                    addIcon.setFitHeight(8.0);
                    addIcon.setFitWidth(8.0);
                    tagAdd.setGraphic(addIcon);
                    tagAdd.setOnMouseClicked((e) -> {
                        //calculatePopupOffset();
                        double offsetX, offsetY;
                        offsetX = -popup.getWidth()/8;
                        offsetY = -popup.getHeight()/4;
                        
                        popup.show(this, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, offsetX, offsetY);
                        
                    });
                    getChildren().add(tagAdd);
                    break;
                default:
                    break;
            }
        }
        
        public Tag(Boolean dumbAdd){
            getStyleClass().addAll("tag");
            Button tagAdd = new Button("+");
            tagAdd.getStyleClass().add("tag-add");
            tagAdd.setOnMouseClicked((e) -> {
                
                System.out.println("x : " + e.getX() + ", y : "+ e.getY() );
                System.out.println("y : " + e.getScreenX() + ", y : "+e.getScreenY());
                System.out.println(JFXPopup.PopupHPosition.LEFT + " "+ JFXPopup.PopupHPosition.LEFT.ordinal());
                //System.out.println(JFXPopup.PopupHPosition);
                
                popup.show(this, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, e.getX(), e.getY());
            });
            getChildren().add(tagAdd);
        }
    }
    
    public Class<S> getType(){
        return clazz;
    }
    
    protected boolean isModelCompatible(){
        return new PropertyReference(getType(), "id").hasProperty()
                || new PropertyReference(getType(), "ID").hasProperty();
    }
    
    public static enum TagType {
        PROMPT, TAG, ADD;
    }
    
    
}
