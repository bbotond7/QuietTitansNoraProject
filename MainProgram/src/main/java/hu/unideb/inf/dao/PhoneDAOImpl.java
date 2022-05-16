package hu.unideb.inf.dao;

import hu.unideb.inf.config.ContactConfiguration;
import hu.unideb.inf.model.Contacts;
import hu.unideb.inf.model.Phone;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PhoneDAOImpl implements PhoneDAO{

    private static final String SELECT_PHONES_BY_CONTACT_ID = "SELECT * FROM PHONE WHERE contact_id=?";
    private String connectionURL;

    public PhoneDAOImpl() {
        this.connectionURL = ContactConfiguration.getValue("db.url");
    }

    @Override
    public List<Phone> findAllByContactId(Contacts contact) {
        return findAllByContactId(contact.getId());
    }

    @Override
    public List<Phone> findAllByContactId(int contactId) {
        List<Phone> result = new ArrayList<>();
        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement statement = c.prepareStatement(SELECT_PHONES_BY_CONTACT_ID)
        ) {
            statement.setInt(1, contactId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Phone phone = new Phone();
                phone.setId(rs.getInt("id"));
                phone.setNumber(rs.getString("number"));
                int ordinalValue = rs.getInt("phoneType");
                Optional<Phone.PhoneType> pt = Arrays.stream(Phone.PhoneType.values()).filter(phoneType -> phoneType.ordinal() == ordinalValue).findAny();
                phone.setPhoneType(pt.orElse(Phone.PhoneType.UNKNOWN));

                result.add(phone);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
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
