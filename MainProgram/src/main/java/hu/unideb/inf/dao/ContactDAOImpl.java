package hu.unideb.inf.dao;

import hu.unideb.inf.config.ContactConfiguration;
import hu.unideb.inf.model.Contact;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ContactDAOImpl implements ContactDAO{
    private static final String SELECT_ALL_CONTACTS = "SELECT * FROM CONTACT";
    private static final String INSERT_CONTACT = "INSERT INTO CONTACT (name, email, address, dateOfBirth, position) VALUES (?,?,?,?,?)";
    private static final String UPDATE_CONTACT = "UPDATE  CONTACT  SET name = ?, email = ?, address = ?, dateOfBirth = ?, position = ? WHERE  id = ?";
    private static final String DELETE_CONTACT = "DELETE  FROM CONTACT WHERE id = ?";

    //SQLite3 az adatbázishoz GitHub-on lesz

    private Properties props = new Properties();
    private static String connectionURL;

    public ContactDAOImpl() {
       connectionURL = ContactConfiguration.getValue("db.url");

    }

    @Override
    public List<Contact> findAll() {

        List<Contact> result = new ArrayList<>();

        try(Connection c = DriverManager.getConnection(connectionURL);
            Statement stat = c.createStatement();
            ResultSet rs = stat.executeQuery(SELECT_ALL_CONTACTS);
        ) {
            while(rs.next()) {
                Contact contact = new Contact();
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
    public Contact save(Contact contact) {
        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = contact.getId() <= 0 ? c.prepareStatement(INSERT_CONTACT, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(UPDATE_CONTACT)
        ){
            if (contact.getId() > 0){ // UPDATE
                stmt.setInt(6, contact.getId());
            }

            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getEmail());
            stmt.setString(3, contact.getAddress());
            stmt.setString(4, contact.getDateOfBirth().toString());
            stmt.setString(5, contact.getPosition());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0){
                return null;
            }

            if (contact.getId() <= 0){ // INSERT
                ResultSet genKeys = stmt.getGeneratedKeys();
                if (genKeys.next()){
                    contact.setId(genKeys.getInt(1));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return contact;
    }

    @Override
    public void delete(Contact contact) {

        try (Connection c = DriverManager.getConnection(connectionURL);
             PreparedStatement stmt =  c.prepareStatement(DELETE_CONTACT);
        ) {
            stmt.setInt(1, contact.getId());
            stmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
