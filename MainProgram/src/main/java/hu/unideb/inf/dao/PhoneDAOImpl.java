package hu.unideb.inf.dao;

import hu.unideb.inf.config.ContactConfiguration;
import hu.unideb.inf.model.Contact;
import hu.unideb.inf.model.Phone;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PhoneDAOImpl implements PhoneDAO{

    private static final String SELECT_PHONES_BY_CONTACT_ID = "SELECT * FROM PHONE WHERE contact_id = ?";
    private static final String INSERT_PHONE = "INSERT INTO PHONE (number, phoneType, contact_id) VALUES (?,?,?)";
    private static final String UPDATE_PHONE = "UPDATE PHONE SET number = ?, phoneType = ? WHERE id = ?";
    private static final String DELETE_PHONE = "DELETE FROM PHONE WHERE id = ?";
    private String connectionUrl;

    public PhoneDAOImpl() {
        this.connectionUrl = ContactConfiguration.getValue("db.url");
    }

    @Override
    public List<Phone> findAllByContactsId(Contact contact) {
        return findAllByContactsId(contact.getId());
    }

    @Override
    public List<Phone> findAllByContactsId(int contactsId) {
        List<Phone> result = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement statement = c.prepareStatement(SELECT_PHONES_BY_CONTACT_ID)){

            statement.setInt(1, contactsId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
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
    public Phone save(Phone phone, int contactId) {
        try(Connection c = DriverManager.getConnection(connectionUrl);
            PreparedStatement stmt = phone.getId() <= 0 ? c.prepareStatement(INSERT_PHONE, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(UPDATE_PHONE);
        ){
            if (phone.getId() > 0){ // UPDATE
                stmt.setInt(3, phone.getId());
            } else { // INSERT
                stmt.setInt(3, contactId);
            }

            stmt.setString(1, phone.getNumber());
            stmt.setInt(2, phone.getPhoneType().ordinal());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0){
                return null;
            }

            if (phone.getId() <= 0){ // INSERT
                ResultSet genKeys = stmt.getGeneratedKeys();
                if (genKeys.next()){
                    phone.setId(genKeys.getInt(1));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return phone;
    }

    @Override
    public void delete(Phone phone) {
        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt =  c.prepareStatement(DELETE_PHONE);
        ) {
            stmt.setInt(1, phone.getId());
            stmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteALL(int contactId) {
        findAllByContactsId(contactId).forEach(this::delete);
    }
}
