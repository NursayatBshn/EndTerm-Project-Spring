package kz.nursayat.repository;

import kz.nursayat.exception.DatabaseOperationException;
import kz.nursayat.exception.ResourceNotFoundException;
import kz.nursayat.model.Freelancer;
import kz.nursayat.repository.interfaces.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FreelancerRepository implements CrudRepository<Freelancer> {

    private final DataSource dataSource;

    public FreelancerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Freelancer freelancer) {
        String sql = "INSERT INTO freelancers (first_name, last_name, email, rating, joined_at, phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, freelancer.getFirstName());
            ps.setString(2, freelancer.getLastName());
            ps.setString(3, freelancer.getEmail());
            ps.setDouble(4, freelancer.getRating());
            ps.setDate(5, Date.valueOf(freelancer.getJoinedAt()));
            ps.setString(6, freelancer.getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create freelancer", e);
        }
    }

    @Override
    public List<Freelancer> getAll() { // Название строго по интерфейсу
        List<Freelancer> freelancers = new ArrayList<>();
        String sql = "SELECT * FROM freelancers";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                freelancers.add(mapRowToFreelancer(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch freelancers", e);
        }
        return freelancers;
    }

    @Override
    public Freelancer getById(int id) { // Название строго по интерфейсу
        String sql = "SELECT * FROM freelancers WHERE freelancer_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToFreelancer(rs);
                }
            }
            throw new ResourceNotFoundException("Freelancer not found with id: " + id);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch freelancer", e);
        }
    }

    public Freelancer findByEmail(String email) {
        String sql = "SELECT * FROM freelancers WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToFreelancer(rs); // Использует наш Builder [cite: 25-31]
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding freelancer by email", e);
        }
    }

    @Override
    public void update(int id, Freelancer freelancer) { // Сигнатура по интерфейсу (int, T)
        String sql = "UPDATE freelancers SET first_name = ?, last_name = ?, email = ?, rating = ?, phone = ? WHERE freelancer_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, freelancer.getFirstName());
            ps.setString(2, freelancer.getLastName());
            ps.setString(3, freelancer.getEmail());
            ps.setDouble(4, freelancer.getRating());
            ps.setString(5, freelancer.getPhone());
            ps.setInt(6, id);
            if (ps.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Freelancer not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update freelancer", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM freelancers WHERE freelancer_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new ResourceNotFoundException("Freelancer not found with id: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete freelancer", e);
        }
    }

    private Freelancer mapRowToFreelancer(ResultSet rs) throws SQLException {
        // Сборка объекта через Builder для выполнения требований Endterm
        return new Freelancer.Builder()
                .id(rs.getInt("freelancer_id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .rating(rs.getDouble("rating"))
                .joinedAt(rs.getDate("joined_at").toLocalDate())
                .phone(rs.getString("phone"))
                .build();
    }
}