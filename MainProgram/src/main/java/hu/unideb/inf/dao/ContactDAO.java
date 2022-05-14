package hu.unideb.inf.dao;

import hu.unideb.inf.model.Contacts;

import java.util.List;

public interface ContactDAO {

    List<Contacts> findAll();

    Contacts save(Contacts contact);

    void delete(Contacts contact);
}
