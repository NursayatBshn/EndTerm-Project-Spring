package kz.nursayat.repository;

import kz.nursayat.exception.DatabaseOperationException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Client;
import kz.nursayat.repository.interfaces.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepository implements CrudRepository<Client> {

    private final DataSource dataSource;

    public ClientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Client client) {
        String sql = "INSERT INTO clients (first_name, last_name, email, registered_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getEmail());
            ps.setDate(4, Date.valueOf(client.getRegisteredAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create client", e);
        }
    }

    @Override
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clients.add(mapRowToClient(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch clients", e);
        }
        return clients;
    }

    @Override
    public Client getById(int id) {
        String sql = "SELECT * FROM clients WHERE client_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToClient(rs);
                }
            }
            throw new ResourceNotFoundException("Client not found with id: " + id);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch client", e);
        }
    }

    public Client findByEmail(String email) {
        String sql = "SELECT * FROM clients WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToClient(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding client by email", e);
        }
    }

    @Override
    public void update(int id, Client client) {
        String sql = "UPDATE clients SET first_name = ?, last_name = ?, email = ? WHERE client_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getLastName());
            ps.setString(3, client.getEmail());
            ps.setInt(4, id);
            if (ps.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Client not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update client", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM clients WHERE client_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Client not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete client", e);
        }
    }

    private Client mapRowToClient(ResultSet rs) throws SQLException {
        return new Client.Builder()
                .id(rs.getInt("client_id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .registeredAt(rs.getDate("registered_at").toLocalDate())
                .build();
    }
}