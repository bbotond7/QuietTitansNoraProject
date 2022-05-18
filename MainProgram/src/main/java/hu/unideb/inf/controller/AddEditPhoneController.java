package hu.unideb.inf.controller;

import hu.unideb.inf.model.Contact;
import hu.unideb.inf.model.Phone;
import javafx.stage.Stage;

public class AddEditPhoneController {

    private Stage stage;
    private Phone phone;
    private Contact contact;

    public void init(Stage stage, Phone phone, Contact contact) {
        this.stage = stage;
        this.phone = phone;
        this.contact = contact;
    }

}
