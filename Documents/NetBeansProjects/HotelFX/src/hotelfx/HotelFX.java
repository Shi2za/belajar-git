/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx;

import hotelfx.helper.ClassCoder;
import hotelfx.helper.ClassSession;
import hotelfx.helper.tooltip.ToolTipDefaultsFixer;
import hotelfx.persistence.HibernateUtil;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import hotelfx.view.DashboardController;
import hotelfx.view.HomeController;
import hotelfx.view.LoginController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author ANDRI
 */
public class HotelFX extends Application {

    public static Stage primaryStage;
    private AnchorPane rootLayout;

    @Override
    public void start(Stage currentStage) {
        //set primary stage
        primaryStage = currentStage;
        primaryStage.setTitle("LENORA-HOTEL");

        //set the application icon
        primaryStage.getIcons().add(new Image("file:resources/Icon/hotel-icon.png"));

        primaryStage.setMinWidth(1200 + 10);
        primaryStage.setMinHeight(680 + 10);
        
        //set static main-program (hotelfx)
        ClassSession.mainProgram = this;
        
        //set tooltip (timer)
        ToolTipDefaultsFixer.setTooltipTimers(0, 120000, 0);

        //init dassboard layout (login- first)
        initRootLayout();
    }

    @Override
    public void stop() {
        //log out - current user
        FLoginManager loginManager = new FLoginManagerImpl();
        if (ClassSession.currentUser != null) {
            loginManager.doLogout(ClassSession.currentUser.getIduser());
        }
        //close connection to DB
        HibernateUtil.getSessionFactory().close();
    }

    private void initRootLayout() {
        rootLayout = new AnchorPane();

        //set content root_layout (first:loginlayout)
        showLoginLayout();

        //show the scene containing the dashboard layout.
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    private void setRootContent(Node mainData,
            double aTop, double aLeft, double aRight, double aBottom) {
        //set anchor position
        AnchorPane.setTopAnchor(mainData, aTop);
        AnchorPane.setLeftAnchor(mainData, aLeft);
        AnchorPane.setRightAnchor(mainData, aRight);
        AnchorPane.setBottomAnchor(mainData, aBottom);

        //set root_contennt into the center of root_layout.
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(mainData);
    }

    public void showLoginLayout() {
        try {
            //load login layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/LoginView.fxml"));
            BorderPane loginLayout = (BorderPane) loader.load();

            //insert the login layout to root_content
            setRootContent(loginLayout,
                    0, 0, 0, 0);

            //give the controller access to the primary stage
            LoginController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            System.out.println("error : " + e.getMessage());
        }
    }

    public void showDashboardLayout() {
        try {
            //load dashboard layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/DashboardView.fxml"));
//            loader.setLocation(HotelFX.class.getResource("view/HomeView.fxml"));

//            DashboardController aaa = new DashboardController();
//            aaa.setMainApp(this);
//            loader.setController(aaa);
            AnchorPane dashboardLayout = (AnchorPane) loader.load();

            //insert the dashboard layout to root_content
            setRootContent(dashboardLayout,
                    0, 0, 0, 0);

            //give the controller access to the primary stage
            DashboardController controller = loader.getController();
//            HomeController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            System.out.println("error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * SHOW ALERT
     */
    public static void showAlert(
            Alert.AlertType alertType,
            String title,
            String header,
            String content) {
        //set alert
        Alert alert = new Alert(alertType);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        //show and wait respone..
        alert.showAndWait();
    }

    /*
     * SHOW ALERT
     */
    public static void showAlert(
            Alert.AlertType alertType,
            String title,
            String header,
            String content,
            Stage anotherStage) {
        //set stage icon
        anotherStage.getIcons().add(new Image("file:resources/Icon/hotel-icon.png"));
        //set alert
        Alert alert = new Alert(alertType);
        alert.initOwner(anotherStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        //show and wait respone..
        alert.showAndWait();
    }

    public static Alert showAlertConfirm(Alert.AlertType alertType,
            String title,
            String header,
            String content) {
        Alert alert = new Alert(alertType);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        //show and wait respone..
        alert.showAndWait();

        return alert;
    }

    public static Alert showAlertConfirm(Alert.AlertType alertType,
            String title,
            String header,
            String content,
            Stage anotherStage) {
        Alert alert = new Alert(alertType);
        alert.initOwner(anotherStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        //show and wait respone..
        alert.showAndWait();

        return alert;
    }

    public static Alert showAlertChooseButton(Alert.AlertType alertType,
            String title,
            String header,
            String content,
            Stage anotherStage, 
            ButtonType... chooseButtons) {
        Alert alert = new Alert(alertType);
        alert.initOwner(anotherStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getButtonTypes().setAll(chooseButtons);
        
        //show and wait respone..
        alert.showAndWait();

        return alert;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
