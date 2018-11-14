/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.view.feature_user_access.user_access;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCComboBoxTablePopup;
import hotelfx.helper.ClassDataUserAccess;
import hotelfx.helper.ClassMessage;
import hotelfx.helper.ClassSession;
import hotelfx.helper.ClassViewSetting;
import hotelfx.persistence.model.TblSystemFeature;
import hotelfx.persistence.model.TblSystemRoleSystemFeature;
import hotelfx.view.feature_user_access.FeatureUserAccessController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import hotelfx.persistence.model.TblSystemRole;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

/**
 * FXML Controller class
 *
 * @author ANDRI
 */
public class UserAccessController implements Initializable {

    /**
     * TOP
     */
    @FXML
    private AnchorPane ancFunctionalUserAccessLayout;

    private JFXCComboBoxTablePopup<TblSystemRole> cbpRole;

    private boolean isCBPRoleEventRunning = false;

    @FXML
    private JFXButton btnSave;

    private void setFunctionalButton() {
        //set data popup - data role
        initDataPopup();
        cbpRole.hide();
        //button save
        btnSave.setOnMouseClicked((e) -> {
            dataUserAccessSaveHandle();
        });
        initImportantFieldColor();
    }

    private void initImportantFieldColor() {
        ClassViewSetting.setImportantField(
                cbpRole);
    }

    private void initDataPopup() {
        //Role
        TableView<TblSystemRole> tableRole = new TableView<>();

        TableColumn<TblSystemRole, String> roleName = new TableColumn<>("Role");
        roleName.setCellValueFactory(cellData -> cellData.getValue().roleNameProperty());
        roleName.setMinWidth(140);

        tableRole.getColumns().addAll(roleName);

        ObservableList<TblSystemRole> roleItems = FXCollections.observableArrayList(parentController.getFUserAccessManager().getAllDataRole());

        cbpRole = new JFXCComboBoxTablePopup<>(
                TblSystemRole.class, tableRole, roleItems, "", "Role *", false, 200, 200
        );

        //attached to layout
        AnchorPane.setBottomAnchor(cbpRole, 10.0);
        AnchorPane.setTopAnchor(cbpRole, 10.0);
        AnchorPane.setLeftAnchor(cbpRole, 70.0);
        ancFunctionalUserAccessLayout.getChildren().add(cbpRole);

    }

    private void refreshDataPopup() {
        //Role
        ObservableList<TblSystemRole> roleItems = FXCollections.observableArrayList(parentController.getFUserAccessManager().getAllDataRole());
        cbpRole.setItems(roleItems);
        cbpRole.setValue(null);
//       cbpRole.valueProperty().addListener((obs,oldVal,newVal)->{
//           itemRoot = new TreeItem();
//            getTreeItem(itemRoot,tableSystemRole,newVal);
//        });
    }

    /**
     * CENTER
     */
    @FXML
    private AnchorPane ancTreeTableViewUserAccessLayout;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TreeTableView tableUserAccess;

    private List<TblSystemRoleSystemFeature> dataRoleUpdate;

    private List<ClassDataUserAccess> newDataCurrentUser;

    TreeTableView<ClassDataUserAccess> tableSystemRole;

    TreeItem<ClassDataUserAccess> itemRoot = new TreeItem<>();

    private void initTableUserAccess() {

        setTableDataUserAccess();

        //dataUserAccessHandle();
        //setTableControlDataRoomTransferItem();
        //initTableFormDataProperty();
        ancTreeTableViewUserAccessLayout.getChildren().clear();
        AnchorPane.setTopAnchor(tableUserAccess, 15.0);
        AnchorPane.setLeftAnchor(tableUserAccess, 15.0);
        AnchorPane.setBottomAnchor(tableUserAccess, 15.0);
        AnchorPane.setRightAnchor(tableUserAccess, 15.0);
        ancTreeTableViewUserAccessLayout.getChildren().add(tableUserAccess);
    }

    private void setTableDataUserAccess() {
        tableSystemRole = new TreeTableView();
        tableSystemRole.setEditable(true);

        TreeTableColumn<ClassDataUserAccess, String> nameFeature = new TreeTableColumn("Fitur");
        nameFeature.setCellValueFactory((TreeTableColumn.CellDataFeatures<ClassDataUserAccess, String> param)
                -> Bindings.createStringBinding(() -> param.getValue().getValue().getSystemRoleFeature().getTblSystemFeature().getFeatureName(), param.getValue().getValue().getSystemRoleFeature().getTblSystemFeature().featureNameProperty()));
        nameFeature.setPrefWidth(200);

        TreeTableColumn<ClassDataUserAccess, Boolean> view = new TreeTableColumn("Lihat");
        //create.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(create));
        view.setCellFactory((TreeTableColumn<ClassDataUserAccess, Boolean> param) -> new CheckBoxCell());
        view.setCellValueFactory(cellData -> cellData.getValue().getValue().isViewProperty());
        view.setMinWidth(100);

        TreeTableColumn<ClassDataUserAccess, Boolean> create = new TreeTableColumn("Tambah");
        //create.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(create));
        create.setCellFactory((TreeTableColumn<ClassDataUserAccess, Boolean> param) -> new CheckBoxCell());
        create.setCellValueFactory(cellData -> cellData.getValue().getValue().isCreateProperty());
        create.setMinWidth(100);

        TreeTableColumn<ClassDataUserAccess, Boolean> update = new TreeTableColumn("Ubah");
        update.setCellFactory((TreeTableColumn<ClassDataUserAccess, Boolean> param) -> new CheckBoxCell());
        update.setCellValueFactory(cellData -> cellData.getValue().getValue().isUpdateProperty());
        update.setMinWidth(100);

        TreeTableColumn<ClassDataUserAccess, Boolean> approve = new TreeTableColumn("Setujui");
        approve.setCellFactory((TreeTableColumn<ClassDataUserAccess, Boolean> param) -> new CheckBoxCell());
        approve.setCellValueFactory(cellData -> cellData.getValue().getValue().isApproveProperty());
        approve.setMinWidth(100);

        TreeTableColumn<ClassDataUserAccess, Boolean> delete = new TreeTableColumn("Hapus");
        delete.setCellFactory((TreeTableColumn<ClassDataUserAccess, Boolean> param) -> new CheckBoxCell());
        delete.setCellValueFactory(cellData -> cellData.getValue().getValue().isDeleteProperty());
        delete.setMinWidth(100);

        TreeTableColumn<ClassDataUserAccess, Boolean> print = new TreeTableColumn("Cetak");
        print.setCellFactory((TreeTableColumn<ClassDataUserAccess, Boolean> param) -> new CheckBoxCell());
        print.setCellValueFactory(cellData -> cellData.getValue().getValue().isPrintProperty());
        print.setMinWidth(100);

        //TreeItem<ClassDataUserAccess>itemRoot = new TreeItem<>();
        cbpRole.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!isCBPRoleEventRunning) {
                isCBPRoleEventRunning = true;
                if (!ClassSession.unSavingDataInput.get()) {
                    itemRoot = new TreeItem();
                    getTreeItem(itemRoot, tableSystemRole, newVal);
                } else {
                    Alert alert = ClassMessage.showConfirmationUnSavingDataInputMessage("", null);
                    if (alert.getResult() == ButtonType.OK) {
                        itemRoot = new TreeItem();
                        getTreeItem(itemRoot, tableSystemRole, newVal);
                        ClassSession.unSavingDataInput.set(false);
                    } else {
                        cbpRole.setValue(oldVal);
                    }
                }
                isCBPRoleEventRunning = false;
            }
        });

        // getTreeItemRoleChoice(itemRootTemp,tableSystemRole);
        tableSystemRole.getColumns().addAll(nameFeature, view, create, update, approve, delete, print);

        tableUserAccess = tableSystemRole;
    }

    /* private List<ClassDataUserAccess> loadDataRoleFeatureByCurrentUserRole() {
     List<ClassDataUserAccess>dataCurrentUser = parentController.getFUserAccessManager().getAllDataRoleFeatureByIdRole(ClassSession.currentUser.getTblSystemRole().getIdrole());
     List<ClassDataUserAccess>newDataCurrentUser = new ArrayList();
        
     for(ClassDataUserAccess getDataCurrentUser : dataCurrentUser)
     {
     ClassDataUserAccess getNewDataCurrentUser = new ClassDataUserAccess();
     getNewDataCurrentUser.setSystemRoleFeature(getDataCurrentUser.getSystemRoleFeature());
     newDataCurrentUser.add(getDataCurrentUser);
     cbpRole.valueProperty().addListener((obs,oldVal,newVal)->{
     List<ClassDataUserAccess>dataRoleChoice = parentController.getFUserAccessManager().getAllDataRoleFeatureByIdRole(newVal.getIdrole());
     for(ClassDataUserAccess getDataRoleChoice : dataRoleChoice)
     {
     if(getDataRoleChoice.getSystemRoleFeature().getTblSystemFeature().getIdfeature()==getNewDataCurrentUser.getSystemRoleFeature().getTblSystemFeature().getIdfeature())
     {
     getNewDataCurrentUser.setIsCreate(true);
     System.out.println(getNewDataCurrentUser.getIsCreate());
     }
     }
     });
     }
     return newDataCurrentUser;
     }*/
    private List<ClassDataUserAccess> loadDataRoleFeature(TblSystemRole role) {

        List<ClassDataUserAccess> dataCurrentUser = parentController.getFUserAccessManager().getAllDataRoleFeatureByIdRole(ClassSession.currentUser.getTblSystemRole().getIdrole());
        List<ClassDataUserAccess> newDataCurrentUser = new ArrayList();
        List<ClassDataUserAccess> dataRoleChoice = new ArrayList();
        dataRoleUpdate = new ArrayList<>();
        System.out.println("Data role Update size:" + dataRoleUpdate.size());
        for (ClassDataUserAccess getDataCurrentUser : dataCurrentUser) {
            TblSystemRoleSystemFeature dataNew = new TblSystemRoleSystemFeature();
            dataNew.setTblSystemFeature(getDataCurrentUser.getSystemRoleFeature().getTblSystemFeature());
            dataNew.setTblSystemRole(role);
            dataNew.setCreateData(Boolean.FALSE);
            dataNew.setUpdateData(Boolean.FALSE);
            dataNew.setApproveData(Boolean.FALSE);
            dataNew.setDeleteData(Boolean.FALSE);
            dataNew.setPrintData(Boolean.FALSE);
            ClassDataUserAccess getNewDataCurrentUser = new ClassDataUserAccess(dataNew, false, dataNew.getCreateData(), dataNew.getUpdateData(), dataNew.getApproveData(), dataNew.getDeleteData(), dataNew.getPrintData(), dataRoleUpdate);
            newDataCurrentUser.add(getNewDataCurrentUser);
        }

        if (role != null) {
            dataRoleChoice = parentController.getFUserAccessManager().getAllDataRoleFeatureByIdRole(role.getIdrole());
        }

        for (ClassDataUserAccess getDataRoleChoice : dataRoleChoice) {
            for (int i = 0; i < newDataCurrentUser.size(); i++) {

                if (getDataRoleChoice.getSystemRoleFeature().getTblSystemFeature().getTblSystemFeature() != null) {
                    if (getDataRoleChoice.getSystemRoleFeature().getTblSystemFeature().getIdfeature() == newDataCurrentUser.get(i).getSystemRoleFeature().getTblSystemFeature().getIdfeature()) {
                        newDataCurrentUser.set(i, new ClassDataUserAccess(getDataRoleChoice.getSystemRoleFeature(), true, getDataRoleChoice.getIsCreate(), getDataRoleChoice.getIsUpdate(), getDataRoleChoice.getIsApprove(), getDataRoleChoice.getIsDelete(), getDataRoleChoice.getIsPrint(), dataRoleUpdate));
                    }
                }
            }
            /* for(ClassDataUserAccess getNewDataCurrentUser : newDataCurrentUser)
             {
             if(getDataRoleChoice.getSystemRoleFeature().getTblSystemFeature().getTblSystemFeature()!=null)
             {
             //getNewDataCurrentUser.getSystemRoleFeature().setTblSystemRole(getDataRoleChoice.getSystemRoleFeature().getTblSystemRole());
             if(getDataRoleChoice.getSystemRoleFeature().getTblSystemFeature().getIdfeature()==getNewDataCurrentUser.getSystemRoleFeature().getTblSystemFeature().getIdfeature())
             {
             /*getNewDataCurrentUser.setIsCreate(getDataRoleChoice.getIsCreate());
             getNewDataCurrentUser.setIsDelete(getDataRoleChoice.getIsDelete());
             getNewDataCurrentUser.setIsUpdate(getDataRoleChoice.getIsUpdate());
             getNewDataCurrentUser.setIsPrint(getDataRoleChoice.getIsPrint());
             getNewDataCurrentUser = new ClassDataUserAccess(getDataRoleChoice.getSystemRoleFeature(),false,true,true,true,true,dataRoleUpdate);
             }
             }
             }*/
        }

        //System.out.println("size:" +dataRoleUpdate.size());
        return newDataCurrentUser;
    }

    //add TreeItem
    private void getTreeItem(TreeItem<ClassDataUserAccess> itemRoot, TreeTableView<ClassDataUserAccess> tableSystemRole, TblSystemRole role) {
        //parent
        for (ClassDataUserAccess roleFeature : loadDataRoleFeature(role)) {
            if (roleFeature.getSystemRoleFeature().getTblSystemFeature().getTblSystemFeature() == null) {

                TreeItem<ClassDataUserAccess> uaParent = new TreeItem<>(roleFeature);
                uaParent.setExpanded(true);

                boolean found = false;
                //check parent ada di udh ada di root atau belum
                for (TreeItem<ClassDataUserAccess> parent : itemRoot.getChildren()) {
                    if (parent.getValue().getSystemRoleFeature().getTblSystemFeature().getIdfeature()
                            == uaParent.getValue().getSystemRoleFeature().getTblSystemFeature().getIdfeature()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    itemRoot.getChildren().add(uaParent);
                }
            } else {
                //child
                TreeItem<ClassDataUserAccess> uaChild = new TreeItem<>(roleFeature);
                boolean found = false;
                for (TreeItem<ClassDataUserAccess> parent : itemRoot.getChildren()) {
                    if (parent.getValue().getSystemRoleFeature().getTblSystemFeature().getIdfeature()
                            == uaChild.getValue().getSystemRoleFeature().getTblSystemFeature().getTblSystemFeature().getIdfeature()) {
                        found = true;
                        parent.getChildren().add(uaChild);
                    }

                }
                if (!found) {
                    ClassDataUserAccess data = new ClassDataUserAccess();
                    data.getSystemRoleFeature().setTblSystemFeature(parentController.getFUserAccessManager().getFeature(roleFeature.getSystemRoleFeature().getTblSystemFeature().getIdfeature()));
                    TreeItem<ClassDataUserAccess> uaParent = new TreeItem<>(data);
                    uaParent.setExpanded(true);
                    itemRoot.getChildren().add(uaParent);
                    uaParent.getChildren().add(uaChild);
                }
            }
        }
        tableSystemRole.setRoot(itemRoot);
        tableSystemRole.setShowRoot(false);
        //return itemRoot;
    }

    private void dataUserAccessSaveHandle() {

        //System.out.println("Result size:"+dataRoleUpdate.size());
        //getTreeItem(itemRoot,tableSystemRole,cbpRole.getValue());
        Alert alert = ClassMessage.showConfirmationSavingDataMessage("", null);
        if (alert.getResult() == ButtonType.OK) {
            //dummy data
            TblSystemRole dummySelectedDataRole = null;
            if (cbpRole.getValue() != null) {
                dummySelectedDataRole = new TblSystemRole(cbpRole.getValue());
            }
            List<TblSystemRoleSystemFeature> dummyDataRoleUpdates = new ArrayList<>();
            for (TblSystemRoleSystemFeature dataRU : dataRoleUpdate) {
                TblSystemRoleSystemFeature dummyDataRoleUpdate = new TblSystemRoleSystemFeature(dataRU);
                dummyDataRoleUpdate.setTblSystemRole(new TblSystemRole(dummyDataRoleUpdate.getTblSystemRole()));
                dummyDataRoleUpdate.setTblSystemFeature(new TblSystemFeature(dummyDataRoleUpdate.getTblSystemFeature()));
                dummyDataRoleUpdates.add(dummyDataRoleUpdate);
            }
            if (parentController.getFUserAccessManager().updateDataRoleFeature(dummySelectedDataRole, dummyDataRoleUpdates)) {
                ClassMessage.showSucceedUpdatingDataMessage("", null);
                itemRoot = new TreeItem();
                getTreeItem(itemRoot, tableSystemRole, cbpRole.getValue());
                //set unsaving data input -> 'false'
                ClassSession.unSavingDataInput.set(false);
            } else {
                ClassMessage.showFailedUpdatingDataMessage(parentController.getFUserAccessManager().getErrorMessage(), null);
            }
        } else {
            setTableDataUserAccess();
        }

        //List<TblSystemRoleSystemFeature>dataUpdateRole = new ArrayList();
    }
    /*private void getTreeItemRoleChoice(TreeItem<TblSystemRoleSystemFeature>itemRoot,TreeTableView<TblSystemRoleSystemFeature>tableSystemRole){
     //parent
    
     cbpRole.valueProperty().addListener((obs,oldVal,newVal)->{
     TreeItem<TblSystemRoleSystemFeature>roleItemChoice = null;
     System.out.println(">>"+newVal.getRoleName());
     for(TreeItem<TblSystemRoleSystemFeature>roleFeature : itemRoot.getChildren())
     {
     for(TblSystemRoleSystemFeature roleChoice : loadDataRoleFeatureByRoleChoice(newVal.getIdrole()))
     {
     if(roleChoice.getTblSystemFeature().getIdfeature()==roleFeature.getValue().getTblSystemFeature().getIdfeature())
     {
     roleFeature.setValue(roleChoice);
     }
     else
     {
     roleFeature.setValue(null);
     }
     }
     }
        
     tableSystemRole.setRoot(itemRoot);
     tableSystemRole.setShowRoot(false);
     });
      
      
     } */

    public class CheckBoxCell<S, T> extends TreeTableCell<S, T> {

        private final CheckBox checkBox;
        private ObservableValue<T> ov;

        public CheckBoxCell() {
            this.checkBox = new CheckBox();
            this.checkBox.setAlignment(Pos.CENTER);
            setAlignment(Pos.CENTER);
            setGraphic(checkBox);
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (this.getTreeTableRow().getTreeItem() != null) {
                Object data = this.getTreeTableRow().getTreeItem();
                //System.out.println("Data:"+((TblSystemRoleSystemFeature)data).getTblSystemFeature().getFeatureName());
                if (((TreeItem<ClassDataUserAccess>) data).getValue().getSystemRoleFeature().getTblSystemFeature().getTblSystemFeature() == null) {
                    checkBox.setVisible(false);
                    //System.out.println(((TreeItem<TblSystemRoleSystemFeature>)data).getValue().getTblSystemFeature().getFeatureName());
                } else {
                    checkBox.setVisible(true);
                }

            }
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(checkBox);
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
                }
                ov = getTableColumn().getCellObservableValue(getIndex());
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
                }
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (isEmpty()) {
                return;
            }
            checkBox.setDisable(false);
            checkBox.requestFocus();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            checkBox.setDisable(true);
        }

    }
    /*class BooleanCell extends TreeTableCell<TblSystemRoleSystemFeature, Boolean> {
     private CheckBox checkBox;
     public BooleanCell() {
     checkBox = new CheckBox();
     checkBox.setDisable(true);
     checkBox.selectedProperty().addListener(new ChangeListener<Boolean> () {
     public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
     if(isEditing())
     commitEdit(newValue == null ? false : newValue);
     }
     });
     this.setGraphic(checkBox);
     this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
     this.setEditable(true);
     }
     @Override
     public void startEdit() {
     super.startEdit();
     if (isEmpty()) {
     return;
     }
     checkBox.setDisable(false);
     checkBox.requestFocus();
     }
     @Override
     public void cancelEdit() {
     super.cancelEdit();
     checkBox.setDisable(true);
     }
        
     @Override
     public void commitEdit(Boolean value) {
     super.commitEdit(value);
     checkBox.setDisable(true);
     }
        
     @Override
     public void updateItem(Boolean item, boolean empty) {
     super.updateItem(item, empty);
     if (!isEmpty()) {
     checkBox.setSelected(item);
     }
            
     TblSystemRoleSystemFeature dataParent = this.getTreeTableRow().getTreeItem().getValue();
     if(dataParent.getTblSystemFeature().getTblSystemFeature()==null){
     checkBox.setDisable(true);
     }
     }
     }
    
     /* private void setContentLayout() {
     //refresh data treeview
     cbpRole.valueProperty().addListener((obs, oldVal, newVal) -> {
     if (newVal == null) {
     rootNode = new TreeItem<>();
     rootNode.setExpanded(true);
     treeView = new TreeView<>(rootNode);
     treeView.setShowRoot(false);
     } else {
     refreshTreeView();
     }
     });
     //first data set
     rootNode = new TreeItem<>();
     rootNode.setExpanded(true);
     treeView = new TreeView<>(rootNode);
     treeView.setShowRoot(false);
     //set data tree view to layout
     scrollPane.setContent(treeView);
     scrollPane.setFitToWidth(true);
     scrollPane.setFitToHeight(true);
     }

     private TreeView<UserAccessNode> treeView;

     private TreeItem<UserAccessNode> rootNode;

     private void refreshTreeView() {
     rootNode = new TreeItem<>();
     rootNode.setExpanded(true);
     for (TblSystemRoleSystemFeature roleFeature : loadDataRoleFeatureByCurrentUserRole()) {
     boolean selected = false;
     for (TblSystemRoleSystemFeature rf : loadDataRoleFeatureBySelectedRole()) {
     if (roleFeature.getTblSystemFeature().getIdfeature() == rf.getTblSystemFeature().getIdfeature()) {
     selected = true;
     break;
     }
     }
     UserAccessNode data = new UserAccessNode(roleFeature, selected, rootNode);
     if (roleFeature.getTblSystemFeature().getTblSystemFeature() == null) {   //parent
     TreeItem<UserAccessNode> uaParent = new TreeItem<>(data);
     uaParent.setExpanded(true);
     boolean found = false;
     for (TreeItem<UserAccessNode> parent : rootNode.getChildren()) {
     if (parent.getValue().getRoleFeatureValue().getTblSystemFeature().getIdfeature()
     == uaParent.getValue().getRoleFeatureValue().getTblSystemFeature().getIdfeature()) {
     found = true;
     break;
     }
     }
     if (!found) {
     rootNode.getChildren().add(uaParent);
     }
     } else {  //child
     TreeItem<UserAccessNode> uaChild = new TreeItem<>(data);
     boolean found = false;
     for (TreeItem<UserAccessNode> parent : rootNode.getChildren()) {
     if (parent.getValue().getRoleFeatureValue().getTblSystemFeature().getIdfeature()
     == uaChild.getValue().getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature()) {
     //                        uaChild.getValue().selectedProperty().bind(parent.getValue().selectedProperty());
     parent.getChildren().add(uaChild);
     found = true;
     break;
     }
     }
     if (!found) {
     TblSystemRoleSystemFeature dataParent = new TblSystemRoleSystemFeature();
     dataParent.setTblSystemFeature(parentController.getFUserAccessManager().getFeature(data.getRoleFeatureValue().getTblSystemFeature().getTblSystemFeature().getIdfeature()));
     TreeItem<UserAccessNode> uaParent = new TreeItem<>(new UserAccessNode(
     dataParent,
     selected,
     rootNode));
     uaParent.setExpanded(true);
     //                    uaChild.getValue().selectedProperty().bind(uaParent.getValue().selectedProperty());
     rootNode.getChildren().add(uaParent);
     uaParent.getChildren().add(uaChild);
     }
     }

     }
     treeView = new TreeView<>(rootNode);
     treeView.setShowRoot(false);
     //set data tree view to layout
     scrollPane.setContent(treeView);
     }

     private List<TblSystemRoleSystemFeature> loadDataRoleFeatureByCurrentUserRole() {
     return parentController.getFUserAccessManager().getAllDataRoleFeatureByIdRole(ClassSession.currentUser.getTblSystemRole().getIdrole());
     }

     private List<TblSystemRoleSystemFeature> loadDataRoleFeatureBySelectedRole() {
     return parentController.getFUserAccessManager().getAllDataRoleFeatureByIdRole(cbpRole.getValue().getIdrole());
     }

     class UserAccessNode extends HBox {

     private TblSystemRoleSystemFeature roleFeatureValue;

     private final JFXCheckBox chbRoleFeature = new JFXCheckBox();

     private final JFXCheckBox chbCreate = new JFXCheckBox("Create");
     private final JFXCheckBox chbUpdate = new JFXCheckBox("Update");
     private final JFXCheckBox chbDelete = new JFXCheckBox("Delete");
     private final JFXCheckBox chbPrint = new JFXCheckBox("Print");

     private final AnchorPane tombStone = new AnchorPane();

     public UserAccessNode(TblSystemRoleSystemFeature roleFeature,
     boolean selected,
     TreeItem<UserAccessNode> root) {
     roleFeatureValue = roleFeature;

     //feature parent
     if (roleFeature.getTblSystemFeature().getTblSystemFeature() == null) {
     chbCreate.setVisible(false);
     chbUpdate.setVisible(false);
     chbDelete.setVisible(false);
     chbPrint.setVisible(false);
     }

     //            selectedProperty.bindBidirectional(chbRoleFeature.selectedProperty());
     chbRoleFeature.setText(roleFeatureValue.getTblSystemFeature().getCodeFeature() + " - " + roleFeatureValue.getTblSystemFeature().getFeatureName());
     chbRoleFeature.setSelected(selected);

     //            chbRoleFeature.setAllowIndeterminate(true);
     chbRoleFeature.setIndeterminate(true);

     //init first value
     roleFeatureValue.setCreateData(selected);
     roleFeatureValue.setUpdateData(selected);
     roleFeatureValue.setDeleteData(selected);
     roleFeatureValue.setPrintData(selected);

     chbCreate.selectedProperty().bindBidirectional(roleFeatureValue.createDataProperty());
     chbUpdate.selectedProperty().bindBidirectional(roleFeatureValue.updateDataProperty());
     chbDelete.selectedProperty().bindBidirectional(roleFeatureValue.deleteDataProperty());
     chbPrint.selectedProperty().bindBidirectional(roleFeatureValue.printDataProperty());

     chbRoleFeature.selectedProperty().addListener((obs, oldVal, newVal) -> {
     if (newVal) {
     chbRoleFeature.setIndeterminate(false);
     if (roleFeature.getTblSystemFeature().getTblSystemFeature() == null) {
     updateCheckBoxFeatureChildRender();
     } else {
     updateCheckBoxFeatureParentRender();
     }
     //enable
     chbCreate.setDisable(false);
     chbUpdate.setDisable(false);
     chbDelete.setDisable(false);
     chbPrint.setDisable(false);
     } else {
     chbRoleFeature.setIndeterminate(false);
     if (roleFeature.getTblSystemFeature().getTblSystemFeature() == null) {
     updateCheckBoxFeatureChildRender();
     } else {
     updateCheckBoxFeatureParentRender();
     }
     //set to default : false
     chbCreate.setSelected(false);
     chbUpdate.setSelected(false);
     chbDelete.setSelected(false);
     chbPrint.setSelected(false);
     //disbale
     chbCreate.setDisable(true);
     chbUpdate.setDisable(true);
     chbDelete.setDisable(true);
     chbPrint.setDisable(true);
     }
     });

     chbCreate.selectedProperty().addListener((obs, oldVal, newVal) -> {
     updateCheckBoxFeatureChildRender();
     if (chbRoleFeature.isIndeterminate()) {
     updateCheckBoxFeatureParentRender();
     }
     });

     chbUpdate.selectedProperty().addListener((obs, oldVal, newVal) -> {
     updateCheckBoxFeatureChildRender();
     if (chbRoleFeature.isIndeterminate()) {
     updateCheckBoxFeatureParentRender();
     }
     });

     chbDelete.selectedProperty().addListener((obs, oldVal, newVal) -> {
     updateCheckBoxFeatureChildRender();
     if (chbRoleFeature.isIndeterminate()) {
     updateCheckBoxFeatureParentRender();
     }
     });

     chbPrint.selectedProperty().addListener((obs, oldVal, newVal) -> {
     updateCheckBoxFeatureChildRender();
     if (chbRoleFeature.isIndeterminate()) {
     updateCheckBoxFeatureParentRender();
     }
     });

     tombStone.minWidthProperty().bind((new SimpleDoubleProperty(300.0)).subtract(chbRoleFeature.widthProperty()));
     tombStone.prefWidthProperty().bind((new SimpleDoubleProperty(300.0)).subtract(chbRoleFeature.widthProperty()));
     tombStone.maxWidthProperty().bind((new SimpleDoubleProperty(300.0)).subtract(chbRoleFeature.widthProperty()));

     setSpacing(15);

     getChildren().addAll(chbRoleFeature, tombStone, chbCreate, chbUpdate, chbDelete, chbPrint);

     }

     public TblSystemRoleSystemFeature getRoleFeatureValue() {
     return roleFeatureValue;
     }

     public void setRoleFeatureValue(TblSystemRoleSystemFeature roleFeature) {
     roleFeatureValue = roleFeature;
     }

     public boolean isSelected() {
     return chbRoleFeature.selectedProperty().get();
     }

     public void setSelected(boolean selected) {
     chbRoleFeature.selectedProperty().set(selected);
     }

     private void updateCheckBoxFeatureChildRender() {
     if (chbCreate.isSelected()
     && chbUpdate.isSelected()
     && chbDelete.isSelected()
     && chbPrint.isSelected()) {   //all selected
     chbRoleFeature.setSelected(true);
     } else {
     if (!chbCreate.isSelected()
     && !chbUpdate.isSelected()
     && !chbDelete.isSelected()
     && !chbPrint.isSelected()) {   //all selected
     chbRoleFeature.setSelected(false);
     }else{
     chbRoleFeature.setIndeterminate(true);
     }
     }
     }

     private void updateCheckBoxFeatureParentRender() {

     }

     }

     /**
     * HANDLE
     */
    /*private void saveHandle() {
     //get data user access from tree view
     List<TblSystemRoleSystemFeature> userAccess = new ArrayList<>();
     getRoleFeature(treeView.getRoot(), userAccess);
     if (parentController.getFUserAccessManager().updateDataRoleFeature(cbpRole.getValue(), userAccess)) {
     HotelFX.showAlert(Alert.AlertType.INFORMATION, "SUCCESSED", "Updating data successed..!", "System will be out..!  \nAnd another user with 'Selected Role' will be out..!");
     } else {
     HotelFX.showAlert(Alert.AlertType.ERROR, "FAILED", "Updating data failed..!", parentController.getFUserAccessManager().getErrorMessage());
     }
     }

     private void getRoleFeature(TreeItem<UserAccessNode> uaNode,
     List<TblSystemRoleSystemFeature> userAccess) {
     if (uaNode.getChildren().isEmpty()) {
     //stop
     } else {
     if (uaNode.getValue() == null) {  //root - just next
     for (TreeItem<UserAccessNode> cNode : uaNode.getChildren()) {
     getRoleFeature(cNode, userAccess);
     }
     } else {
     if (uaNode.getValue().isSelected()) { //value is selected, saved value and next
     userAccess.add(uaNode.getValue().getRoleFeatureValue());
     for (TreeItem<UserAccessNode> cNode : uaNode.getChildren()) {
     getRoleFeature(cNode, userAccess);
     }
     }
     }
     }
     }*/

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set funtional button
        setFunctionalButton();
        refreshDataPopup();

        initTableUserAccess();

        //set content layout
        // setContentLayout();
    }

    public UserAccessController(FeatureUserAccessController parentController) {
        this.parentController = parentController;
    }

    private final FeatureUserAccessController parentController;

}
