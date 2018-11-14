/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view;

import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class HomePageController implements Initializable {

    @FXML
    private AnchorPane ancHomeLayout;
    
    @FXML
    private AnchorPane ancHomeContent;
    
    private void initFormDataHome(){
        
    }
    
    /**
     * Service
     */
    private FLoginManager fLoginManager;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service
        fLoginManager = new FLoginManagerImpl();
        //init form input
        initFormDataHome();
    }    
    
}
