/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.tagbar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Andreas
 */
public final class MYFXOptMenu<S> extends MYFXBaseOptMenu<S>{
    
    private ObservableList<S> items;
    private ObjectProperty<S> selectedItem;
    
    public MYFXOptMenu(Class<S> clazz, String header, ObservableList<S> items){
        super(clazz, header, items);
        
        if(isModelCompatible()){
            this.items = getItems();

            selectedItem = new SimpleObjectProperty();
            //getChildren().add(initOptMenu(header));
            getItemList().setOnMouseClicked((e) -> {
                if(getItemList().getSelectionModel().selectedItemProperty().get() != null){
                    selectedItem.set((S)getItemList().getSelectionModel().selectedItemProperty().get());   
                }
            });
        }else {
            throw new UnsupportedOperationException("Model not compatible with this OptMenu.");
            //To change body of generated methods, choose Tools | Templates.
        }
    }
    
//    private VBox initOptMenu(String header){
//        VBox optMenu = new VBox();
//        
//        optMenu.getChildren().add(new Label(header));
//        
//        FilteredList<BaseModel> filteredItems = new FilteredList<>(items, s -> true);
//        TextField filterField = new TextField();
//        filterField.textProperty().addListener((obs, oldFilter, newFilter) -> {
//            String filter = newFilter.toString();
//            if(filter == null || filter.length() == 0){
//                filteredItems.setPredicate(s -> true);
//            }else {
//                filteredItems.setPredicate(s -> s.toString().contains(filter));
//            }
//        });
//        optMenu.getChildren().add(filterField);
//        
//        ListView itemList = new ListView(filteredItems);
//        itemList.getStyleClass().add("option");
//        itemList.setPrefSize(USE_COMPUTED_SIZE, 160.0);
//        itemList.setOnMouseClicked((e) -> {
//            if(itemList.getSelectionModel().selectedItemProperty().get() != null){
//                selectedItem.set((BaseModel)itemList.getSelectionModel().selectedItemProperty().get());
//                itemList.getSelectionModel().clearSelection();
//            }
//        });
//        optMenu.getChildren().add(itemList);
//        
//        return optMenu;
//    }
    
    @Override
    public void initState(){
//        getItemList().getSelectionModel().clearSelection();
    }
    
    @Override
    protected boolean isModelCompatible(){
        return super.isModelCompatible();
    }
//    @Override
//    public void setQtyItems(ObservableList<S> items) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
}
