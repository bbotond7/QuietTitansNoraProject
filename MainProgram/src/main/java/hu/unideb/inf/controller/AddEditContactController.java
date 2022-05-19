package hu.unideb.inf.controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javax.imageio.IIOException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;

public class AddEditContactController  implements Initializable {

    private Contact contact;
    private PhoneDAO phoneDAO = new PhoneDAOImpl();
    private ContactDAO contactDAO = new ContactDAOImpl();

    @FXML
    private Label nameErrors;
    @FXML
    private Label emailErrors;
    @FXML
    private Label addressErrors;
    @FXML
    private Label positionErrors;
    @FXML
    private Button saveBtn;
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
    @FXML
    private TextField nameSearch;
    @FXML
    private TextField emailSearch;

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
        saveBtn.disableProperty().bind(name.textProperty().isEmpty()

                .or(position.textProperty().isEmpty())
                .or(address.textProperty().isEmpty())
                .or(email.textProperty().isEmpty())
                .or(dateOfBirth.valueProperty().isNull()));

        name.textProperty().addListener((observableValue, s, t1) ->{
            if(t1 != null && t1.isEmpty()){
                nameErrors.setText("Name is required");
            }
            else{
                nameErrors.setText("");
            }
        } );
        email.textProperty().addListener((observableValue, s, t1) -> {
            if(t1 != null && t1.isEmpty()){
                emailErrors.setText("Email is required");
            } else if(t1 != null && t1.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\n" +
                    "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\n" +
                    "\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:\n" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])/")){
                emailErrors.setText("Not a valid email");
            }
            else{
                emailErrors.setText("");
            }
        });
        address.textProperty().addListener((observableValue, s, t1) ->{
            if(t1 != null && t1.isEmpty()){
                addressErrors.setText("Address is required");
            }
            else{
                addressErrors.setText("");
            }
        } );
        address.textProperty().addListener((observableValue, s, t1) ->{
            if(t1 != null && t1.isEmpty()){
                positionErrors.setText("Position is required");
            }
            else{
                positionErrors.setText("");
            }
        } );

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
