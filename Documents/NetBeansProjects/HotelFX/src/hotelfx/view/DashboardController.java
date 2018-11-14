/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import static com.sun.glass.ui.MenuItem.Separator;
import hotelfx.HotelFX;
import hotelfx.helper.ClassDataUserAccess;
import hotelfx.helper.ClassFeature;
import hotelfx.helper.ClassFolderManager;
import hotelfx.helper.ClassFormatter;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import hotelfx.persistence.service.FLoginManager;
import hotelfx.persistence.service.FLoginManagerImpl;
import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class DashboardController implements Initializable {

    /*
     * ROOT : BASE BACKGROUND
     */
    @FXML
    AnchorPane bpBaseBackground;

    private Image imgBaseBackgroundImage;

    /*
     * HEADER
     */
    /*
     * HAMBURGER : LIST-MENU(FEATURES)
     */
    @FXML
    JFXHamburger hamburger;

    private final BooleanProperty status = new SimpleBooleanProperty(true);

    @FXML
    private BorderPane bpListMenu;

    @FXML
    private VBox showMenuBox;

    private JFXButton btnLogOut = new JFXButton();

    private AnchorPane showContentMenuLayout = new AnchorPane();

    @FXML
    private AnchorPane imgUserLayout;

    @FXML
    private Label nameUser;

    @FXML
    private StackPane listMenuBoard;

    private TreeView listMenu;

    public static ClassFeature feature;

    private String statusMenu;

    private PseudoClass subMenuPseudoClass = PseudoClass.getPseudoClass("sub-menu");

    private PseudoClass mainMenuPseudoClass = PseudoClass.getPseudoClass("main-menu");

    private void setButtonFeature() {
        //set all feature (accessable)
        statusMenu = "addChild";
        initAllFeatures();

        //set hamburger button + listener
        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburger);
        burgerTask.rateProperty().addListener((obs, oldVal, newVal) -> {
//            burgerTask.play();
        });
        burgerTask.setRate(-1);
        hamburger.setOnMouseClicked((e) -> {
            status.set(burgerTask.getRate() != 1);
        });

        //set list menu-layout listener
        //bpListMenu.getStyleClass().add("hamburger-layout");
        bpListMenu.setOnMouseClicked((e) -> {
            status.set(false);
        });

        //set status listener
        status.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                burgerTask.setRate(1);
                mainPane.setVisible(newVal);
                bpListMenu.setVisible(newVal);
                showMenuBox.setVisible(newVal);
                bpListMenu.toFront();
                showMenuBox.toFront();
            } else {
                burgerTask.setRate(-1);
                mainPane.setVisible(!newVal);
                bpListMenu.setVisible(newVal);
                showMenuBox.setVisible(newVal);
                bpListMenu.toBack();
                showMenuBox.toBack();
            }
        });

        btnLogOut.setOnAction((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                doLogout();
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    doLogout();
                }
            }
        });
    }

    private void initAllFeatures() {
        VBox boxMenu = new VBox();
        if (!ClassSession.currentRoleFeature.isEmpty()) {
            //generate feature-class
            feature = new ClassFeature();
            TreeItem<NameItems> root = new TreeItem<>();
            for (TblSystemRoleSystemFeature tblRoleFeature : ClassSession.currentRoleFeature) {
                feature.addFeature(tblRoleFeature);
                NameItems data = new NameItems(tblRoleFeature);
                if (statusMenu.equalsIgnoreCase("addChild")) {
                    if (tblRoleFeature.getTblSystemFeature().getTblSystemFeature() == null) {
                        TreeItem<NameItems> itemParents = new TreeItem(data);

                        //itemParents.setExpanded(true);
                        boolean found = false;
                        for (TreeItem<NameItems> parent : root.getChildren()) {
                            if (parent.getValue().getRoleFeatureValue().getTblSystemFeature().getIdfeature()
                                    == itemParents.getValue().getRoleFeatureValue().getTblSystemFeature().getIdfeature()) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            root.getChildren().add(itemParents);
                        }
                    } else {
                        TreeItem<NameItems> uaChild = new TreeItem<>(data);
                        boolean found = false;
                        for (TreeItem<NameItems> parent : root.getChildren()) {
                            if (parent.getValue().getRoleFeatureValue().getTblSystemFeature().getIdfeature()
                                    == uaChild.getValue().getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature()) {
                                parent.getChildren().add(uaChild);
                                found = true;
                                break;
                            }
                        }
                    }
                } else {
                    if (tblRoleFeature.getTblSystemFeature().getTblSystemFeature() == null) {
                        JFXButton buttonMenu = new JFXButton(tblRoleFeature.getTblSystemFeature().getFeatureName());
                        buttonMenu.setPrefSize(200, 200);

                        boxMenu.getChildren().addAll(buttonMenu);
                    }
                }

                /*feature.addFeature(tblRoleFeature);
                 if (!feature.isFeatureParent(tblRoleFeature.getTblSystemFeature())) {
                 feature.getFeatureParent(tblRoleFeature.getTblSystemFeature().getTblSystemFeature().getIdfeature()).getFeatureChild(tblRoleFeature.getTblSystemFeature().getIdfeature()).setOnMouseClicked((e) -> {
                 feature.setSelectedRoleFeature(feature.getFeatureParent(tblRoleFeature.getTblSystemFeature().getTblSystemFeature().getIdfeature()).getFeatureChild(tblRoleFeature.getTblSystemFeature().getIdfeature()).getRoleFeatureInstance());
                 setTitleFeature(tblRoleFeature.getTblSystemFeature().getFeatureName());
                 setDashboardContent(tblRoleFeature.getTblSystemFeature().getFxmlpath());
                 status.set(false);
                 });
                 }*/
            }

            if (statusMenu.equalsIgnoreCase("addChild")) {
                listMenu = new TreeView(root);
                listMenu.setShowRoot(false);
                root.expandedProperty().addListener((obs, loadVal, newVal) -> {
                    if (newVal == true) {
                        newVal = false;
                    }
                });
                //expandedTreeView(root);
                //listMenu.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                listMenu.setPrefHeight(500);
                listMenu.setMaxHeight(Double.MAX_VALUE);
                listMenu.setPrefWidth(200);

                listMenu.setCellFactory(tv -> {
                    TreeCell<NameItems> cell = new TreeCell<NameItems>() {

                        @Override
                        public void updateItem(NameItems item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(null);
                            setGraphic(null);
                            setOnMouseClicked(null);

                            if (!empty) {
                                setText(item.getName());

                                setOnMouseClicked((e) -> {
                                    if (e.getClickCount() == 2) {
                                        if (!feature.isFeatureParent(item.getRoleFeatureValue().getTblSystemFeature())) {
                                            if (!ClassSession.unSavingDataInput.get()) {
                                                feature.setSelectedRoleFeature(feature.getFeatureParent(item.getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature()).getFeatureChild(item.getRoleFeatureValue().getTblSystemFeature().getIdfeature()).getRoleFeatureInstance());
                                                setTitleFeature(item.getRoleFeatureValue().getTblSystemFeature().getFeatureName());
                                                setDashboardContent(item.getRoleFeatureValue().getTblSystemFeature().getFxmlpath());
                                                status.set(false);
                                            } else {  //unsaving data input
                                                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                                                if (alert.getResult() == ButtonType.OK) {
                                                    ClassSession.unSavingDataInput.set(false);
                                                    feature.setSelectedRoleFeature(feature.getFeatureParent(item.getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature()).getFeatureChild(item.getRoleFeatureValue().getTblSystemFeature().getIdfeature()).getRoleFeatureInstance());
                                                    setTitleFeature(item.getRoleFeatureValue().getTblSystemFeature().getFeatureName());
                                                    setDashboardContent(item.getRoleFeatureValue().getTblSystemFeature().getFxmlpath());
                                                    status.set(false);
                                                }
                                            }
                                        }
                                        /*if(item.getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature()!=null){
                                         feature.setSelectedRoleFeature(feature.getFeatureParent(item.getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature()).getFeatureChild(item.getRoleFeatureValue().getTblSystemFeature().getIdfeature()).getRoleFeatureInstance());
                                         setTitleFeature(item.getRoleFeatureValue().getTblSystemFeature().getFeatureName());
                                         setDashboardContent(item.getRoleFeatureValue().getTblSystemFeature().getFxmlpath());
                                         status.set(false);
                                         }*/

                                        System.out.println(">>" + item.getRoleFeatureValue().getTblSystemFeature().getFeatureName());
                                    }
                                });
                            }

                        }

                    };

                    //((ActiveCellMenu)cell).activeProperty().set(false);
                    cell.treeItemProperty().addListener((obs, oldItem, newItem) -> {
                        cell.pseudoClassStateChanged(subMenuPseudoClass, newItem != null && newItem.getParent() != cell.getTreeView().getRoot());
                        cell.pseudoClassStateChanged(mainMenuPseudoClass, newItem != null && newItem.getParent() == cell.getTreeView().getRoot());
                    });
                    return cell;
                    //return null;
                });

                VBox.setVgrow(listMenu, Priority.SOMETIMES);
                showMenuBox.getChildren().add(listMenu);
                Separator separate = new Separator();
                separate.setOrientation(Orientation.HORIZONTAL);

//                setScrollInListMenu(listMenu, listMenu.);
                showMenuBox.getChildren().add(separate);
                showContentMenuLayout.setStyle("-fx-background-color:#e8f5e9;");
                btnLogOut.setText("Log Out");
                btnLogOut.getStyleClass().add("button-dashboard");
                btnLogOut.setButtonType(JFXButton.ButtonType.RAISED);
                showContentMenuLayout.getChildren().clear();
                AnchorPane.setTopAnchor(btnLogOut, 25.0);
                AnchorPane.setLeftAnchor(btnLogOut, 25.0);
                AnchorPane.setRightAnchor(btnLogOut, 25.0);
                AnchorPane.setBottomAnchor(btnLogOut, 25.0);
                //showContentMenuLayout.getStyleClass().add("anchor-pane-showContent-menu");
                showContentMenuLayout.getChildren().add(btnLogOut);
                showMenuBox.getChildren().add(showContentMenuLayout);

            } else {
                /*showMenuLayout.getChildren().clear();
                 AnchorPane.setTopAnchor(boxMenu, 0.0);
                 AnchorPane.setLeftAnchor(boxMenu, 0.0);
                 AnchorPane.setRightAnchor(boxMenu, 0.0);
                 AnchorPane.setBottomAnchor(boxMenu, 0.0);
                 //showContentMenuLayout.getStyleClass().add("anchor-pane-showContent-menu");
                 showMenuLayout.getChildren().add(boxMenu);*/
            }

            /*listMenu.setCellFactory(tv->{
             TreeCell<NameItems>cell = new TreeCell<NameItems>(){
             @Override
             public void updateItem(NameItems item, boolean empty) {
             super.updateItem(item, empty);
             setDisclosureNode(null);

             if (empty) {
             setText("");
             setGraphic(null);
             } else {
             //System.out.println(">>"+item.getRoleFeatureValue().getTblSystemFeature().getFeatureName());
             setText(item.getRoleFeatureValue().getTblSystemFeature().getFeatureName()); // appropriate text for item
             }
             } 
             };
              
             cell.treeItemProperty().addListener((obs, oldTreeItem, newTreeItem) -> {
             cell.pseudoClassStateChanged(subElementPseudoClass,
             newTreeItem != null && newTreeItem.getValue().getRoleFeatureValue().getTblSystemFeature().getIdfeature()==newTreeItem.getValue().getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature());
             });
             return cell;
             });*/
            //treeListMenu = listMenu;
            //add feature to list menu layout
            /*listMenuBoard.getChildren().addAll(feature.getFeatureParents());
             listMenuBoard.setOnKeyReleased((KeyEvent event) -> {
             switch (event.getCode()) {
             case LEFT:
             feature.toPrevious();
             break;
             case RIGHT:
             feature.toNext();
             break;
             }
             });*/
            //set selected parent-feature (DEFAULT)
            //feature.setSelectedFeatureParent(feature.getFirstFeatureParent());
        }
    }

    /*
     * TITLE : LIST-MENU(FEATURES)
     */
    @FXML
    private Label titleFeature;

    private final PseudoClass onScrollPseudoClass = PseudoClass.getPseudoClass("onScroll");

    private final BooleanProperty isFormScroll = new SimpleBooleanProperty(false);

    private int scrollCounter = 0;

    private void setScrollInListMenu(Node layout, ScrollPane scrollPane) {
        isFormScroll.addListener((obs, oldScroll, newScroll) -> {
            scrollPane.pseudoClassStateChanged(onScrollPseudoClass, newScroll);
        });

        layout.setOnScroll((ScrollEvent scroll) -> {
            isFormScroll.set(true);

            scrollCounter++;

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        if (scrollCounter == 1) {
                            isFormScroll.set(false);
                        }
                        scrollCounter--;
                    });
                } catch (Exception e) {
                    System.out.println("err" + e.getMessage());
                }
            });
            thread.setDaemon(true);
            thread.start();
        });
    }

    private void setTitleFeature(String selectedFeature) {
        titleFeature.setText(selectedFeature);
    }

    private void setProfileUser() {
        ImageView imgUser = new ImageView();
        if (ClassSession.currentUser.getUserUrlImage() != null) {
            imgUser.setImage(new Image("file:///" + ClassFolderManager.imageClientRootPath + "/" + ClassSession.currentUser.getUserUrlImage()));
        } else {
            imgUser.setImage(new Image("file:///" + ClassFolderManager.imageSystemRootPath + "/no_profile_img.gif"));
        }

        Circle cirImage = new Circle(300, 250, 60);
        cirImage.setStroke(Color.SEAGREEN);
        cirImage.setFill(new ImagePattern(imgUser.getImage()));
        //cirImage.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        cirImage.setOnMouseClicked((e) -> {
//            if(e.getClickCount() == 2){
            if (!ClassSession.unSavingDataInput.get()) {
                updateCurrentUserAccount();
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    updateCurrentUserAccount();
                }
            }
//            }
        });

        imgUserLayout.getChildren().clear();
        AnchorPane.setTopAnchor(cirImage, 0.0);
        AnchorPane.setLeftAnchor(cirImage, 20.0);
        AnchorPane.setRightAnchor(cirImage, 20.0);
        AnchorPane.setBottomAnchor(cirImage, 0.0);
        //showContentMenuLayout.getStyleClass().add("anchor-pane-showContent-menu");
        imgUserLayout.getChildren().add(cirImage);

        nameUser.setText(ClassSession.currentUser.getTblEmployeeByIdemployee().getTblPeople().getFullName());

    }

    /*
     *  HELPER-BUTTON
     */
    @FXML
    private JFXButton btnHome;

    @FXML
    private JFXButton btnReminder;
    
    @FXML
    private JFXButton btnLogBook;

    private void setHelperButton() {
        btnHome.setTooltip(new Tooltip("Home"));
        btnHome.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                //set title
                titleFeature.setText("HOME");
                //show page
                showHomePage();
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    //set title
                    titleFeature.setText("HOME");
                    //show page
                    showHomePage();
                }
            }
        });

        btnReminder.setTooltip(new Tooltip("Reminder"));
        btnReminder.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                //set title
                titleFeature.setText("REMINDER");
                //show page
                showReminderPage();
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    //set title
                    titleFeature.setText("REMINDER");
                    //show page
                    showReminderPage();
                }
            }
        });
        
        btnLogBook.setTooltip(new Tooltip("LogBook"));
        btnLogBook.setOnMouseClicked((e) -> {
            if (!ClassSession.unSavingDataInput.get()) {
                //set title
                titleFeature.setText("LOGBOOK");
                //show page
                showLogBookPage();
            } else {  //unsaving data input
                Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                if (alert.getResult() == ButtonType.OK) {
                    ClassSession.unSavingDataInput.set(false);
                    //set title
                    titleFeature.setText("LOGBOOK");
                    //show page
                    showLogBookPage();
                }
            }
        });
    }

    /*
     * CONTEXT-MENU : PROFILE
     */
    @FXML
    private ImageView profile;

    @FXML
    private final ContextMenu cm = new ContextMenu();

    private void loadProfileContextMenu() {
        //set data menu items and separators
        MenuItem cmItem0 = new MenuItem("Profile");
        MenuItem cmItem1 = new MenuItem("Setting");
        MenuItem cmItem2 = new MenuItem("Help");
        MenuItem cmItem3 = new MenuItem("About");
        SeparatorMenuItem sp = new SeparatorMenuItem();
        MenuItem cmItem4 = new MenuItem("LogOut");
        cmItem4.setOnAction((e) -> {
            doLogout();
        });

        //add menu item to context menu
        cm.getItems().setAll(cmItem0, cmItem1, cmItem2, cmItem3, sp, cmItem4);

        //set event listener for image view (user_account) to show context menu
        profile.setOnMouseClicked((e) -> showProfileContextMenu(e));
    }

    private void showProfileContextMenu(javafx.scene.input.MouseEvent e) {
        cm.show(profile, e.getScreenX(), e.getScreenY());
    }

    @FXML
    private void doLogout() {
        Alert alert = HotelFX.showAlertConfirm(Alert.AlertType.CONFIRMATION, "KONFIRMASI", "Apakah anda yakin untuk 'Log-Out' dari Sistem?", "");
        if (alert.getResult() == ButtonType.OK) {
            FLoginManager loginManager = new FLoginManagerImpl();
            if (loginManager.doLogout(ClassSession.currentUser.getIduser())) {
                showLoginLayout();
            } else {

            }
        }
    }

    private void showLoginLayout() {
        mainApp.showLoginLayout();
    }

    /*
     * CENTER-CENTER : MAIN-CONTENT
     */
    @FXML
    private AnchorPane mainPane;

    //HOME-PAGE
    private void showHomePage() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/HomePageView.fxml"));
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

    //REMINDER-PAGE
    private void showReminderPage() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/ReminderPageView.fxml"));
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

    //LOGBOOK-PAGE
    private void showLogBookPage() {
        try {
            //loader data (path)
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/LogBookView.fxml"));
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
    
    private void setDashboardContent(String path) {
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

    /*
     * FOOTER
     */
    private void setFooter() {

    }

    class NameItems {

        private TblSystemRoleSystemFeature tblSystemRole;
        private String name;
        //private AnchorPane header;

        private final double BASE_WIDTH = 200;
        private final double BASE_HEIGHT = 100;

        public NameItems(TblSystemRoleSystemFeature tblSystemRole) {
            super();

            this.tblSystemRole = tblSystemRole;
            this.name = tblSystemRole.getTblSystemFeature().getFeatureName();

        }

        public TblSystemRoleSystemFeature getRoleFeatureValue() {
            return tblSystemRole;
        }

        public void setRoleFeatureValue(TblSystemRoleSystemFeature roleFeature) {
            tblSystemRole = roleFeature;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /*private class ActiveCellMenu<T> extends TreeCell<T>{
     private BooleanProperty active;
    
     public ActiveCellMenu(){
     super();
     this.active = new SimpleBooleanProperty(false);
     }
    
     public BooleanProperty activeProperty(){
     return active;
     }
    
     public boolean isActive(){
     return active.get();
     }
     }*/
    private void expandedTreeView(TreeItem<NameItems> selectedItem) {

        if (selectedItem != null) {
            selectedItem.setExpanded(false);
            expandedTreeView(selectedItem.getParent());

            if (!selectedItem.isLeaf()) {
                selectedItem.setExpanded(true);
            }

        }

    }

    /**
     * CURRENT USER ACCOUNT - SETTING
     */
    public Stage dialogStage;

    private void updateCurrentUserAccount() {
        showCurrentUserAccountSettingView();
    }

    private void showCurrentUserAccountSettingView() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HotelFX.class.getResource("view/CurrentUserAccountSettingView.fxml"));

            CurrentUserAccountSettingController controller = new CurrentUserAccountSettingController(this);
            loader.setController(controller);

            Region page = loader.load();

            // Create the dialog Stage.
            dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(HotelFX.primaryStage);

            //undecorated
            Undecorator undecorator = new Undecorator(dialogStage, page);
            undecorator.getStylesheets().add("skin/undecorator.css");
            undecorator.getMenuButton().setVisible(false);
            undecorator.getMaximizeButton().setVisible(false);
            undecorator.getMinimizeButton().setVisible(false);
            undecorator.getFullScreenButton().setVisible(false);
            undecorator.getCloseButton().setVisible(false);

            Scene scene = new Scene(undecorator);
            scene.setFill(Color.TRANSPARENT);

            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Err >> " + e.toString());
        }
    }

    public void refreshDataCurrentUserAccount() {
        //refresh data
        FLoginManager loginManager = new FLoginManagerImpl();
        ClassSession.currentUser = loginManager.getDataUserAccount(ClassSession.currentUser.getIduser());
        //refresh interface
        setProfileUser();
        setTitleFeature("HOME");
        status.set(false);
        //show main-content, default : HOME
        showHomePage();
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

        //set image and load context-menu (profile)
        // profile.setImage(new Image("file:resources/Icon/UserAccount.png"));
        //loadProfileContextMenu();
        //set hamburger and list menu (features) function, default : HOME
        setButtonFeature();
        setHelperButton();
        setProfileUser();
        setTitleFeature("HOME");
        status.set(false);

        //show main-content, default : HOME
        showHomePage();

//        mainBoard.setImage(new Image("file:resources/Image/1.jpg"));
//        hamburger.getCssMetaData().stream().map(CssMetaData::getProperty).sorted().forEach(System.out::println);
    }

    private HotelFX mainApp;

    public void setMainApp(HotelFX mainApp) {
        this.mainApp = mainApp;
    }

}
