package hu.unideb.inf.dao;

import hu.unideb.inf.model.Contacts;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ContactsDAOImpl implements ContactDAO{
    private static final String SELECT_ALL_CONTACTS = "SELECT * FROM CONTACT";

    //https://www.sqlite.org/2022/sqlite-tools-win32-x86-3380500.zip    SQLite

    private Properties props = new Properties();
    private static String connectionURL;

    public ContactsDAOImpl() {
        try {
            props.load(getClass().getResourceAsStream("/application.properties"));
            connectionURL = props.getProperty("db.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contacts> findAll() {

        List<Contacts> result = new ArrayList<>();

        try(Connection c = DriverManager.getConnection(connectionURL);
            Statement stat = c.createStatement();
            ResultSet rs = stat.executeQuery(SELECT_ALL_CONTACTS);
        ) {
            while(rs.next()) {
                Contacts contact = new Contacts();
                contact.setId(rs.getInt("id"));
                contact.setName(rs.getString("name"));
                contact.setEmail(rs.getString("email"));
                contact.setAddress(rs.getString("address"));
                Date date = Date.valueOf(rs.getString("dateOfBirth"));
                contact.setDateOfBirth(date == null ? LocalDate.now() : date.toLocalDate());
                contact.setPosition(rs.getString("position"));

                result.add(contact);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    @Override
    public Contacts save(Contacts contact) {
        return null;
    }

    @Override
    public void delete(Contacts contact) {

    }
}
