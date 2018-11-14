/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCToggleButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.DBConnectionSetting;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class LoginController implements Initializable {

    /*
     * ROOT : BASE BACKGROUND
     */
    @FXML
    BorderPane bpBaseBackground;

    private Image imgBaseBackgroundImage;

    /*
     * HEADER : LOGIN-DATABASE_CONNECTION_SETTING
     */
    @FXML
    JFXCToggleButton tbtnLogin_DBConnection;

    private void setTBTNLogin_DBConnectionListener() {
        tbtnLogin_DBConnection.setOnMouseClicked((e) -> {
            if (tbtnLogin_DBConnection.isSelected()) {    //toggle
                dbConnectionSetting.loadDataConnection();
                gpLogin.setVisible(false);
                gpDBConnection.setVisible(true);
                gpDBConnection.toFront();
            } else {  //untoggle
                gpDBConnection.setVisible(false);
                gpLogin.setVisible(true);
                gpLogin.toFront();
            }
        });
    }

    /*
     * CENTER : LOGIN
     */
    @FXML
    GridPane gpLogin;

    @FXML
    Label lblLogo;

    @FXML
    JFXTextField txtUserLogin;

    @FXML
    JFXPasswordField txtPasswordLogin;

    @FXML
    JFXButton btnLogin;

    private Image imgLoginBaseBackground;

    private Image imgAppLogo;

    @FXML
    private void doLogin() {
        if (checkDataInputLogin()) {
            if (checkDataUserPassword()) {
                FLoginManager loginManager = new FLoginManagerImpl();
                //load user
                ClassSession.currentUser = loginManager.doLogin(txtUserLogin.getText(), txtPasswordLogin.getText());
                if (ClassSession.currentUser != null) {
                    //load feature and show 'dashboard'
                    ClassSession.currentRoleFeature = loginManager.getRoleFeatureByRoleOrderByIDFeature(ClassSession.currentUser.getTblSystemRole().getIdrole());
                    //set unsaving data input to 'false'
                    ClassSession.unSavingDataInput.set(false);
                    showDashboardLayout();
                }else{
                    HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Gagal Login", loginManager.getErrorMessage());
                }
            } else {
                HotelFX.showAlert(Alert.AlertType.ERROR, "GAGAL", "Username dan Password tidak sesuai..! ", "");
            }
        } else {
            ClassMessage.showWarningInputDataMessage("", null);
        }
    }

    private boolean checkDataInputLogin() {
        return true;
    }

    private boolean checkDataUserPassword() {
        return true;
    }

    private void showDashboardLayout() {
        mainApp.showDashboardLayout();
    }

    /*
     * CENTER : DATABASE_CONNECTION_SETTING
     */
    @FXML
    GridPane gpDBConnection;

    @FXML
    JFXTextField txtHostname;

    @FXML
    JFXTextField txtPort;

    @FXML
    JFXTextField txtDatabaseName;

    @FXML
    JFXTextField txtDBUserName;

    @FXML
    JFXPasswordField txtDBPassword;

    @FXML
    JFXButton btnTestConnection;

    @FXML
    JFXButton btnSaveChanged;

    private Image imgDBConnectionBaseBackground;

    private DBConnectionSetting dbConnectionSetting;

    private void loadAndSetDataDBConnectionSetting() {
        //load data db_connection setting
        dbConnectionSetting = new DBConnectionSetting();
        dbConnectionSetting.loadDataConnection();

        //set data db_connection form (bin)
        txtHostname.textProperty().bindBidirectional(dbConnectionSetting.hostNameProperty());
        txtPort.textProperty().bindBidirectional(dbConnectionSetting.portProperty());
        txtDatabaseName.textProperty().bindBidirectional(dbConnectionSetting.dbDatabaseNameProperty());
        txtDBUserName.textProperty().bindBidirectional(dbConnectionSetting.dbUsernameProperty());
        txtDBPassword.textProperty().bindBidirectional(dbConnectionSetting.dbPasswordProperty());
    }

    @FXML
    private void testDataDBConnection() {
        if (checkDataDBConnection()) {
            System.out.println("check");
            if (dbConnectionSetting.testConnectionToDB()) {
                System.out.println(" >> success");
                HotelFX.showAlert(Alert.AlertType.INFORMATION, "Success", "Access Database Enable", "Data Configuration was valid to access database..!");
            } else {
                System.out.println(" >> failed");
                HotelFX.showAlert(Alert.AlertType.ERROR, "Error", "Cann't Access Database", dbConnectionSetting.getErrMsg());
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "Invalid Data", "Invalid Data Input", "Please check data input first..!");
        }
    }

    @FXML
    private void saveDataDBConnection() {
        if (checkDataDBConnection()) {
            if (dbConnectionSetting.saveFileDataConnection()) {
                HotelFX.showAlert(Alert.AlertType.INFORMATION, "Data Inserted", "Data Configuration Saved", "Data Configuration has been saved..!");
            } else {
                HotelFX.showAlert(Alert.AlertType.ERROR, "Error", "Data Configuration Failed To Save", dbConnectionSetting.getErrMsg());
            }
        } else {
            HotelFX.showAlert(Alert.AlertType.WARNING, "Invalid Data", "Invalid Data Input", "Please check data input first..!");
        }
    }

    private boolean checkDataDBConnection() {
        return true;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set base background
        imgBaseBackgroundImage = new Image("file:resources/Image/hotel_base_background.jpg");
        bpBaseBackground.setBackground(new Background(new BackgroundImage(imgBaseBackgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        //set button tooltip
        btnLogin.setTooltip(new Tooltip("Login"));
        btnTestConnection.setTooltip(new Tooltip("Test (Data Connection to Database)"));
        btnSaveChanged.setTooltip(new Tooltip("Save (Data Connection to Database)"));
        
        //set listener for toggle button(login-DBConnection)
        setTBTNLogin_DBConnectionListener();

        //set login form
        gpDBConnection.setVisible(false);
        gpLogin.setVisible(true);
        gpLogin.toFront();

        //set db_connection form
        loadAndSetDataDBConnectionSetting();
        
    }

    private HotelFX mainApp;

    public void setMainApp(HotelFX mainApp) {
        this.mainApp = mainApp;
    }

}
