/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view;

import com.jfoenix.controls.JFXButton;
import hotelfx.HotelFX;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblPeople;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class HomeController implements Initializable {

    /*
     * ROOT : BASE BACKGROUND
     */
    @FXML
    private AnchorPane bpBaseBackground;
    
    private Image imgBaseBackgroundImage;
    
    @FXML
    private BorderPane bpMainBackground;
    
    /*
     * HEADER
     */
    @FXML
    private JFXButton btnHome;
    
    @FXML
    private Label lblHotelName;
    
    @FXML
    private Label lblMyAccount;
    
    @FXML
    private JFXButton btnMyAccount;
    
    @FXML
    private JFXButton btnSetting;
    
    @FXML
    private JFXButton btnLogout;
    
    /*
     * CENTER-CENTER : MAIN-CONTENT
     */
    @FXML
    private AnchorPane mainPane;
    
    private void setAllComponent(){
        
        //Header
        setAllHeaderComponent();
        
        //Body
        setAllBodyComponent();
        
        //Footer
        setAllFooterComponent();
        
    }
    
    private void setAllHeaderComponent(){
        FLoginManager loginManager = new FLoginManagerImpl();
        //button home
        btnHome.setOnAction((e) -> {
            doHome();
        });
        //label hotel name
        SysDataHardCode sdhHotelName = loginManager.getDataSysDataHardCode((long) 12);  //Hotel Name = '12'
        if(sdhHotelName != null){
            lblHotelName.setText(sdhHotelName.getDataHardCodeValue());
        }else{
            lblHotelName.setText("... Hotel");
        }
        //label my account
        TblPeople dataPeople = loginManager.getDataPeopleByUserAccount(ClassSession.currentUser);
        if(dataPeople != null){
            lblMyAccount.setText(dataPeople.getFullName());
        }else{
            lblMyAccount.setText("... ???");
        }
        //button my account
        btnMyAccount.setOnAction((e) -> {
            doMyAccount();
        });
        //button setting
        btnSetting.setOnAction((e) -> {
            doSetting();
        });
        //button logout
        btnLogout.setOnMouseClicked((e) -> {
            doLogout();
        });
    }
    
    private void doHome(){
        setBodyContent("view/HomeFeatureView.fxml");
    }
    
    private void doMyAccount(){
        
    }
    
    private void doSetting(){
        
    }
    
    private void doLogout() {
        FLoginManager loginManager = new FLoginManagerImpl();
        if (loginManager.doLogout(ClassSession.currentUser.getIduser())) {
            showLoginLayout();
        } else {

        }
    }

    private void showLoginLayout() {
        mainApp.showLoginLayout();
    }
    
    private void setAllBodyComponent(){
        
    }
    
    public void setBodyContent(String path) {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource(path));
            Node dashboardContent = loader.load();

            //set anchor position
            AnchorPane.setTopAnchor(dashboardContent, 0.0);
            AnchorPane.setLeftAnchor(dashboardContent, 0.0);
            AnchorPane.setRightAnchor(dashboardContent, 0.0);
            AnchorPane.setBottomAnchor(dashboardContent, 0.0);

            //set 'dashboard' into the center of dashboard.
            mainPane.getChildren().clear();
            mainPane.getChildren().add(dashboardContent);

        } catch (Exception e) {
            System.out.println("err >> " + e.getMessage());
        }
    }
    
    private void setAllFooterComponent(){
        
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set base background
        imgBaseBackgroundImage = new Image("file:resources/Image/hotel_base_background.jpg");
        bpBaseBackground.setBackground(new Background(new BackgroundImage(imgBaseBackgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        
        //set all component
        setAllComponent();
        
        //set to 'Home'
        doHome();
    }    
    
    private HotelFX mainApp;

    public void setMainApp(HotelFX mainApp) {
        this.mainApp = mainApp;
    }
    
}
