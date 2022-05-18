package hu.unideb.inf.controller;

import com.sun.glass.ui.MenuItem;
import hu.unideb.inf.MainApp;
import hu.unideb.inf.dao.ContactDAO;
import hu.unideb.inf.dao.ContactDAOImpl;
import hu.unideb.inf.dao.PhoneDAO;
import hu.unideb.inf.dao.PhoneDAOImpl;
import hu.unideb.inf.model.Contact;
import hu.unideb.inf.model.Phone;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;
import java.util.ResourceBundle;

public class AddEditContactController  implements Initializable {

    private Contact contact;
    private PhoneDAO phoneDAO = new PhoneDAOImpl();
    private ContactDAO contactDAO = new ContactDAOImpl();

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    ListView<Phone> phones;

    @FXML
    private TextField address;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private TextField position;

    public void setContact(Contact c) {
        this.contact = c;
        List<Phone> phonesList = phoneDAO.findAllByContactsId(c.getId());
        contact.setPhones(FXCollections.observableArrayList(phonesList));

        name.textProperty().bindBidirectional(contact.nameProperty());
        email.textProperty().bindBidirectional(contact.emailProperty());
        phones.itemsProperty().bindBidirectional(contact.phonesProperty());
        address.textProperty().bindBidirectional(contact.addressProperty());
        dateOfBirth.valueProperty().bindBidirectional(contact.dateOfBirthProperty());
        position.textProperty().bindBidirectional(contact.positionProperty());

    }
    @FXML
    public void onCancel(){
        MainApp.loadFXML("/fxml/main_window.fxml");
    }

    @FXML
    public void onSave(){
        contact = contactDAO.save(contact);
        phoneDAO.deleteALL(contact.getId());
        contact.getPhones().forEach(phone -> {
            phone.setId(0);
            phoneDAO.save(phone, contact.getId());
        });
        MainApp.loadFXML("/fxml/main_window.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phones.setCellFactory(param -> {
            ListCell<Phone> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            javafx.scene.control.MenuItem editItem = new javafx.scene.control.MenuItem("Edit");
            javafx.scene.control.MenuItem deleteItem = new javafx.scene.control.MenuItem("Delete");

            //MenuItem editItem = new MenuItem("Edit");
            //MenuItem deleteItem = new MenuItem("Delete");

            contextMenu.getItems().addAll(editItem, deleteItem);
            editItem.setOnAction(event -> {
                Phone item = cell.getItem();
                showPhoneDialog(item);
            });
            deleteItem.setOnAction(event ->{
                contact.getPhones().remove(cell.getItem());
            });

            StringBinding cellTextBinding = new When(cell.itemProperty().isNotNull()).then(cell.itemProperty().asString()).otherwise("");
            cell.textProperty().bind(cellTextBinding);

            cell.emptyProperty().addListener((observableValue, wasEmpty, isNowEmpty) -> {
                if(isNowEmpty){
                    cell.setContextMenu(null);
                }
                else{
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;

        });
    }
    @FXML
    public void addNewPhone(){
        showPhoneDialog();
    }

    private void showPhoneDialog(Phone phone) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_edit_phone.fxml"));
        try {
            Parent root = loader.load();
            AddEditPhoneController controller = loader.getController();
            controller.init(stage, phone, contact);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private void showPhoneDialog(){
        showPhoneDialog(new Phone());
    }
}
