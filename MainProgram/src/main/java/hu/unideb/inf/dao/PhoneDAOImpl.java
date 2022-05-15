package hu.unideb.inf.dao;

import hu.unideb.inf.model.Contacts;
import hu.unideb.inf.model.Phone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class PhoneDAOImpl implements PhoneDAO{

    @Override
    public List<Phone> findAllByContactId(Contacts contact) {
        List<Phone> result = new ArrayList<>();
        try(Connection c = DriverManager.getConnection()) {

        }
    }

    @Override
    public List<Phone> findAllByContactId(int contactId) {
        return null;
    }

    @Override
    public Phone save(Phone p) {
        return null;
    }

    @Override
    public Phone delete(Phone p) {
        return null;
    }
}
