package hu.unideb.inf.controller;

import hu.unideb.inf.model.Contact;
import hu.unideb.inf.model.Phone;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

import java.awt.*;

public class AddEditContactController {

    private Contact contact;

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

    }
}
