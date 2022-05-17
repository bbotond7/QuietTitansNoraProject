package hu.unideb.inf.dao;

import hu.unideb.inf.model.Contact;

import java.util.List;

public interface ContactDAO {

    List<Contact> findAll();

    Contact save(Contact contact);

    void delete(Contact contact);
}
