/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import hotelfx.HotelFX;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.SysDataHardCode;
import hotelfx.persistence.model.TblSystemUser;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class CurrentUserAccountSettingController implements Initializable {

    @FXML
    private AnchorPane ancFormCurrentUserAccountSetting;

    @FXML
    private GridPane gpFormDataCurrentUserAccountSetting;

    @FXML
    private ImageView imgUserAccount;

    private String imgSourcePath = "";

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXCheckBox chbChangePassword;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtReTypePassword;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    private void initFormDataCurrentUserAccountSetting() {

        btnSave.setTooltip(new Tooltip("Simpan (Data Perubahan)"));
        btnSave.setOnAction((e) -> {
            dataCurrentUserAccountSaveHandle();
        });

        btnCancel.setTooltip(new Tooltip("Batal"));
        btnCancel.setOnAction((e) -> {
            dataCurrentUserAccountCancelHandle();
        });

        initImportantFieldColor();
        
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                txtUserName, 
                txtPassword, 
                txtReTypePassword);
    }
    
    private void setSelectedDataToInputForm() {

        txtUserName.setText(ClassSession.currentUser.getCodeUser());

        imgUserAccount.setPreserveRatio(false);
        if (ClassSession.currentUser.getUserUrlImage() == null
                || ClassSession.currentUser.getUserUrlImage().equals("")) {
            imgSourcePath = ClassFolderManager.imageSystemRootPath + "/" + "no_profile_img.gif";
            imgUserAccount.setImage(new Image("file:///" + imgSourcePath));
        } else {
            imgSourcePath = ClassFolderManager.imageClientRootPath + "/" + ClassSession.currentUser.getUserUrlImage();
            imgUserAccount.setImage(new Image("file:///" + imgSourcePath));
        }
        imgUserAccount.setOnMouseClicked((e) -> {
            if (e.getClickCount() == 2) {
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(
                        new File(System.getProperty("user.home"))
                );
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File file = fc.showOpenDialog(HotelFX.primaryStage);
                if (file != null) {
                    imgSourcePath = file.getAbsoluteFile().toString();
                    imgUserAccount.setImage(new Image("file:///" + imgSourcePath));
                }
            }
        });

        chbChangePassword.setSelected(false);
        chbChangePassword.selectedProperty().addListener((obs, oldVal, newVal) -> {
            txtPassword.setText("");
            txtReTypePassword.setText("");
            if (newVal) {
                txtPassword.setVisible(true);
                txtReTypePassword.setVisible(true);
            } else {
                txtPassword.setVisible(false);
                txtReTypePassword.setVisible(false);
            }
        });

        txtPassword.setText("");
        txtPassword.setVisible(false);
        txtReTypePassword.setText("");
        txtReTypePassword.setVisible(false);

    }

    private void dataCurrentUserAccountSaveHandle() {
        if (checkDataCurrentUserAccount()) {
            Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin menyimpan data ini?", "", dashboardController.dialogStage);
            if (alert.getResult() == ButtonType.OK) {
                //dummy entry
                TblSystemUser dummyCurrentUser = new TblSystemUser(ClassSession.currentUser);
                dummyCurrentUser.setCodeUser(txtUserName.getText());
                if (chbChangePassword.isSelected()) {
                    dummyCurrentUser.setUserPassword(txtPassword.getText());
                }
                if (fLoginManager.updateDataUserAccount(dummyCurrentUser, 
                        imgSourcePath.split("\\.")[1])) {
                    ClassMessage.showSucceedUpdatingDataMessage("", dashboardController.dialogStage);
                    //try copy image
                    if (!imgSourcePath.contains(ClassFolderManager.imageClientRootPath)) {
                        try {
                            //save image
                            ClassFolderManager.copyImage(
                                    imgSourcePath,
                                    dummyCurrentUser.getUserUrlImage(),
                                    "Client");
                        } catch (IOException ex) {
                            Logger.getLogger(CurrentUserAccountSettingController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //refresh data current user
                    dashboardController.refreshDataCurrentUserAccount();
                    //close form data current user account setting
                    dashboardController.dialogStage.close();
                } else {
                    ClassMessage.showFailedUpdatingDataMessage(fLoginManager.getErrorMessage(), dashboardController.dialogStage);
                }
            }
        } else {
            ClassMessage.showWarningInputDataMessage(errDataInput, dashboardController.dialogStage);
        }
    }

    private void dataCurrentUserAccountCancelHandle() {
        //close form data current user account setting
        dashboardController.dialogStage.close();
    }

    private String errDataInput;

    private boolean checkDataCurrentUserAccount() {
        boolean dataInput = true;
        errDataInput = "";
        if (txtUserName.getText() == null || txtUserName.getText().equals("")) {
            dataInput = false;
            errDataInput += "User Name : " + ClassMessage.defaultErrorNullValueMessage + " \n";
        }
        if (chbChangePassword.isSelected()) {
            if (txtPassword.getText() == null || txtPassword.getText().equals("")) {
                dataInput = false;
                errDataInput += "Password : " + ClassMessage.defaultErrorNullValueMessage + " \n";
            } else {
                int defaultMinimumUserAccountPasswordLength = 0;
                SysDataHardCode sdhDafaultMinimumUserAccountPasswordLength = fLoginManager.getDataSysDataHardCode(10);    //DefaultMinimumUserAccountPasswordLength = '10'
                if (sdhDafaultMinimumUserAccountPasswordLength != null
                        && sdhDafaultMinimumUserAccountPasswordLength.getDataHardCodeValue() != null) {
                    defaultMinimumUserAccountPasswordLength = Integer.parseInt(sdhDafaultMinimumUserAccountPasswordLength.getDataHardCodeValue());
                }
                if (txtPassword.getText().length() < defaultMinimumUserAccountPasswordLength) {
                    dataInput = false;
                    errDataInput += "Password : Panjang password tidak dapat kurang dari " + defaultMinimumUserAccountPasswordLength + " \n";
                } else {
                    if (txtReTypePassword.getText() == null
                            || txtReTypePassword.getText().equals("")
                            || !(txtReTypePassword.getText().equals(txtPassword.getText()))) {
                        dataInput = false;
                        errDataInput += "Re-type Password : Data tidak sama..! \n";
                    }
                }
            }
        }
        return dataInput;
    }

    /**
     * Service
     */
    private FLoginManager fLoginManager;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set service
        fLoginManager = new FLoginManagerImpl();
        //init form input
        initFormDataCurrentUserAccountSetting();
        //refresh data form input
        setSelectedDataToInputForm();
    }

    public CurrentUserAccountSettingController(DashboardController parentController) {
        dashboardController = parentController;
    }

    private final DashboardController dashboardController;

}
