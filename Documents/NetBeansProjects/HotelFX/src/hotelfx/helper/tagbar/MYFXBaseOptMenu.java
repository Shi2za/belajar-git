/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper.tagbar;

import com.sun.javafx.property.PropertyReference;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Andreas
 */
public abstract class MYFXBaseOptMenu<S> extends StackPane{   
    
    private ListView<S> itemList;
    private ObservableList<S> items;
    private ObjectProperty<S> selectedItem;
    
    private Class<S> clazz;
    
    protected MYFXBaseOptMenu(Class<S> clazz, String header, ObservableList<S> items){
        super();
//        getStyleClass().add("myfx-opt-menu");
        paddingProperty().set(new Insets(10.0));
        setPrefSize(210.0, USE_COMPUTED_SIZE);
        
        this.clazz = clazz;
        
        this.items = items;
        selectedItem = new SimpleObjectProperty(this,"selectedItem");
        getChildren().add(initOptMenu(header));
    }
    
    protected VBox getOptMenu(){
        return (VBox)lookup(".opt-menu");
    }
    
    private VBox initOptMenu(String header){
        VBox optMenu = new VBox();
        optMenu.getStyleClass().add("opt-menu");
        
        optMenu.getChildren().add(new Label(header));
        FilteredList filteredItems = new FilteredList(items, s -> true);
        TextField filterField = new TextField();
        filterField.textProperty().addListener((obs, oldFilter, newFilter) -> {
            String filter = newFilter.toString();
            if(filter == null || filter.length() == 0){
                filteredItems.setPredicate(s -> true);
            } else {
                filteredItems.setPredicate(s -> s.toString().contains(filter));
            }
        });
        
        optMenu.getChildren().add(filterField);
        
        itemList = new ListView(filteredItems);
//        itemList.getStyleClass().add("option");
        itemList.setPrefSize(USE_COMPUTED_SIZE, 160.0);
        optMenu.getChildren().add(itemList);
        
        return optMenu; 
    }
    
    public abstract void initState();
    
    public final Class<S> getType(){
        return clazz;
    }
    
    public final ListView<S> getItemList(){
        return itemList;
    }
    
    public final ObservableList<S> getItems(){
        return items;
    }
    
    public final void setItems(ObservableList<S> items){
        this.items = items;
    }
    
    public final S getSelectedItem(){
        return selectedItem.get();
    }
    
    protected final ObjectProperty<S> selectedItemProperty(){
        return selectedItem;
    }
    
    protected boolean isModelCompatible(){
        return new PropertyReference(getType(), "id").hasProperty()
                || new PropertyReference(getType(), "ID").hasProperty();
    }
    
    //public abstract void setQtyItems(ObservableList<S> items);
    
}
