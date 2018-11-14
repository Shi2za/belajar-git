/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.persistence.model.TblItem;
import hotelfx.persistence.model.TblReservationAdditionalItem;
import hotelfx.persistence.model.TblRoomTypeItem;
import java.math.BigDecimal;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataRoomCheck implements java.io.Serializable{
       private final ObjectProperty<TblItem> item;
       private final ObjectProperty<BigDecimal> qtySysItem;
       private final ObjectProperty<BigDecimal> qtyRealItem;
       private final StringProperty codeWorkSheet;


       public ClassDataRoomCheck() {
       this.item = new SimpleObjectProperty<>();
       this.qtySysItem = new SimpleObjectProperty<>();
       this.qtyRealItem = new SimpleObjectProperty<>();
       this.codeWorkSheet = new SimpleStringProperty();

       }

       public ClassDataRoomCheck(TblItem item, BigDecimal qtySysItem, BigDecimal qtyRealItem, String codeWorkSheet) {
       this();
       itemProperty().set(item);
       qtySysItemProperty().set(qtySysItem);
       qtyRealItemProperty().set(qtyRealItem);
       codeWorkSheetProperty().set(codeWorkSheet);
       }

       public final ObjectProperty<TblItem> itemProperty() {
               return this.item;
       }

       public TblItem getItem() {
               return itemProperty().get();
       }

       public void setItem(TblItem item) {
               itemProperty().set(item);
       }

       public final ObjectProperty<BigDecimal> qtySysItemProperty() {
               return this.qtySysItem;
       }

       public BigDecimal getQtySysItem() {
               return qtySysItemProperty().get();
       }

       public void setQtySysItem(BigDecimal qtySysItem) {
               qtySysItemProperty().set(qtySysItem);
       }

       public final ObjectProperty<BigDecimal> qtyRealItemProperty() {
               return this.qtyRealItem;
       }

       public BigDecimal getQtyRealItem() {
               return qtyRealItemProperty().get();
       }

       public void setQtyRealItem(BigDecimal qtyRealItem) {
               qtyRealItemProperty().set(qtyRealItem);
       }

       public final StringProperty codeWorkSheetProperty() {
               return this.codeWorkSheet;
       }

       public String getCodeWorkSheet() {
               return codeWorkSheetProperty().get();
       }

       public void setCodeWorkSheet(String codeWorkSheet) {
               codeWorkSheetProperty().set(codeWorkSheet);
       }

}
