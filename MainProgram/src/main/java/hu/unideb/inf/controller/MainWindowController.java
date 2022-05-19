package hu.unideb.inf.controller;

import hu.unideb.inf.MainApp;
import hu.unideb.inf.dao.ContactDAO;
import hu.unideb.inf.dao.ContactDAOImpl;
import hu.unideb.inf.model.Contact;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    private List<Contact> all;
    ContactDAO dao = new ContactDAOImpl();
    @FXML
    private TableView<Contact> contactsTable;

    @FXML
    private TableColumn<Contact, String> nameColumn;

    @FXML
    private TableColumn<Contact, String> emailColumn;

    @FXML
    private TableColumn<Contact, Void> actionsColumn;
    @FXML
    private TextField nameSearch;
    @FXML
    private TextField emailSearch;
    @FXML
    public void onSearch(){
        List<Contact> filtered = all.stream().filter(contact -> contact.getName().contains(nameSearch.getText()) && contact.getEmail().contains(emailSearch.getText())).collect(Collectors.toList());
        contactsTable.getItems().setAll(filtered);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshTable();

       nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
       emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

       actionsColumn.setCellFactory(param -> new TableCell<>(){

           private final Button deleteBtn = new Button("Delete");
           private final Button editBtn = new Button("Edit");

           {
               deleteBtn.setOnAction(event -> {
                   Contact c = getTableRow().getItem();
                  deleteContacts(c);
                   refreshTable();
               });

               editBtn.setOnAction(event -> {
                   Contact c = getTableRow().getItem();
                   editContact(c);
                   refreshTable();
               });
           }

           @Override
           protected void updateItem(Void item, boolean empty) {
               super.updateItem(item, empty);
               if (empty){
                   setGraphic(null);
               }
               else {
                   HBox container = new HBox();
                   container.getChildren().addAll(editBtn, deleteBtn);
                   container.setSpacing(10.0);
                   setGraphic(container);
               }
           }
       });

    }

    private void editContact(Contact c) {
        FXMLLoader fxmlLoader = MainApp.loadFXML("/fxml/add_edit_contact.fxml");
        AddEditContactController controller = fxmlLoader.getController();
        controller.setContact(c);

    }
    private void deleteContacts(Contact c) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this contact?" + c.getName(), ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.YES)){
                dao.delete(c);
            }
        });
    }
    private void refreshTable() {
        all = dao.findAll();
        contactsTable.getItems().setAll(all);
    }
    @FXML
    public void onExit(){
        Platform.exit();
    }
    @FXML
    public void onAddNewContact(){
        FXMLLoader fxmlLoader = MainApp.loadFXML("/fxml/add_edit_contact.fxml");
        AddEditContactController controller = fxmlLoader.getController();
        controller.setContact(new Contact());
    }
}
