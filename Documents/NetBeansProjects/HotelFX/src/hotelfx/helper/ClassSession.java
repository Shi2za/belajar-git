/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import hotelfx.HotelFX;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import hotelfx.persistence.model.TblSystemUser;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;

/**
 *
 * @author ANDRI
 */
public class ClassSession {

    public static HotelFX mainProgram;

    public static TblSystemUser currentUser;

    public static List<TblSystemRoleSystemFeature> currentRoleFeature;

    public static BooleanProperty unSavingDataInput = new SimpleBooleanProperty();
    
    public static boolean checkUserSession() {
//        if (currentUser != null) {
//            FLoginManager loginManager = new FLoginManagerImpl();
//            if (loginManager.getDataUserAccountByIDUserAccountAndGUID(currentUser.getIduser(),
//                    currentUser.getUserGuid()) == null) {   //not found
//                HotelFX.showAlert(Alert.AlertType.ERROR, "Session Expired!", "User has been login with new session..!", "");
//                mainProgram.showLoginLayout();
//                return false;
//            }
//        }
        return true;
    }

    public static boolean checkUserSession(boolean mustBeChecked) {
        if (mustBeChecked
                && currentUser != null) {
            FLoginManager loginManager = new FLoginManagerImpl();
            if (loginManager.getDataUserAccountByIDUserAccountAndGUID(currentUser.getIduser(),
                    currentUser.getUserGuid()) == null) {   //not found
                HotelFX.showAlert(Alert.AlertType.ERROR, "Session Expired!", "User has been login with new session..!", "");
                mainProgram.showLoginLayout();
                return false;
            }
        }
        return true;
    }
    
}
