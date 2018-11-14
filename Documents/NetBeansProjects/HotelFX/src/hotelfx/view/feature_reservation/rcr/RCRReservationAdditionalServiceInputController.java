/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_reservation.rcr;

import hotelfx.view.feature_reservation.ReservationChangeRoomInputController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class RCRReservationAdditionalServiceInputController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public RCRReservationAdditionalServiceInputController(ReservationChangeRoomInputController parentController) {
        reservationChangeRoomInputController = parentController;
    }

    private final ReservationChangeRoomInputController reservationChangeRoomInputController;

}
