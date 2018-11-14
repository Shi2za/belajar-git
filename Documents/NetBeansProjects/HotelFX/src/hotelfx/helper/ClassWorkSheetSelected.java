/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.view.feature_room_check.room_status.RoomStatusController.DataRoomStatus;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Andreas
 */
public class ClassWorkSheetSelected {
  private final ObjectProperty<DataRoomStatus> roomStatus;
  private final BooleanProperty isSelected;


    public ClassWorkSheetSelected() {
    this.roomStatus = new SimpleObjectProperty<>();
    this.isSelected = new SimpleBooleanProperty();

    }

    public ClassWorkSheetSelected(DataRoomStatus roomStatus, boolean isSelected) {
    this();
    roomStatusProperty().set(roomStatus);
    isSelectedProperty().set(isSelected);
    }

    public final ObjectProperty<DataRoomStatus> roomStatusProperty() {
            return this.roomStatus;
    }

    public DataRoomStatus getRoomStatus() {
            return roomStatusProperty().get();
    }

    public void setRoomStatus(DataRoomStatus roomStatus) {
            roomStatusProperty().set(roomStatus);
    }

    public final BooleanProperty isSelectedProperty() {
            return this.isSelected;
    }

    public boolean getIsSelected() {
            return isSelectedProperty().get();
    }

    public void setIsSelected(boolean isSelected) {
            isSelectedProperty().set(isSelected);
    }



}
