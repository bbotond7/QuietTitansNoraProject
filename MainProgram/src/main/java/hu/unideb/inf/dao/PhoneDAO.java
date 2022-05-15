package hu.unideb.inf.dao;

import hu.unideb.inf.model.Contacts;
import hu.unideb.inf.model.Phone;

import java.util.List;

public interface PhoneDAO {

    List<Phone> findAllByContactId(Contacts contact);

    List<Phone> findAllByContactId(int contactId);

    Phone save(Phone p);

    Phone delete(Phone p);

}
