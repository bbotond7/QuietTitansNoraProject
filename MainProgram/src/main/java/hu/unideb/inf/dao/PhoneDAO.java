package hu.unideb.inf.dao;

import hu.unideb.inf.model.Contact;
import hu.unideb.inf.model.Phone;

import java.util.List;

public interface PhoneDAO {

    List<Phone> findAllByContactsId(Contact contact);
    List<Phone> findAllByContactsId(int contactsId);
    Phone save(Phone p, int contactId);
    void delete(Phone p);
}
